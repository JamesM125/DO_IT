package com.mimi.do_it.Adapter;

import android.content.Context;

import android.app.Activity;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mimi.do_it.AddNewTask;
import com.mimi.do_it.MainActivity;
import com.mimi.do_it.Model.ToDoModel;
import com.mimi.do_it.R;
import com.mimi.do_it.Utils.DatabaseHandler;
import com.mimi.do_it.Utils.Utils;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private final MainActivity activity;
    private final DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ToDoModel item = todoList.get(position);

        // Avoid triggering listener during bind
        holder.taskCheckBox.setOnCheckedChangeListener(null);
        holder.taskCheckBox.setText(item.getTask());
        holder.taskCheckBox.setChecked(item.getStatus() == 1);

        // Display due date if available
        if (item.getDueDateMillis() > 0) {
            holder.dueDateText.setVisibility(View.VISIBLE);
            holder.dueDateText.setText(Utils.formatDateTime(item.getDueDateMillis(), activity));
        } else {
            holder.dueDateText.setVisibility(View.GONE);
        }

        // Checkbox listener
        holder.taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            db.updateStatus(item.getId(), isChecked ? 1 : 0);

            // Animate checkbox when checked
            if (isChecked) {
                holder.taskCheckBox.setButtonDrawable(R.drawable.anim_checkbox_check);
                AnimatedVectorDrawable anim = (AnimatedVectorDrawable) holder.taskCheckBox.getButtonDrawable();
                anim.start();
            } else {
                holder.taskCheckBox.setButtonDrawable(R.drawable.ic_checkbox_unchecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList != null ? todoList.size() : 0;
    }

    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return activity;
    }

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoModel item = todoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskCheckBox;
        TextView dueDateText;
        TextView taskDueDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskCheckBox = itemView.findViewById(R.id.todoCheckBox);
            dueDateText = itemView.findViewById(R.id.taskDueDate);
        }
    }
}
