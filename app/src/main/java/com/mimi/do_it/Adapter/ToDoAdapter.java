package com.mimi.do_it.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mimi.do_it.AddNewTask;
import com.mimi.do_it.MainActivity;
import com.mimi.do_it.Model.ToDoModel;
import com.mimi.do_it.R;
import com.mimi.do_it.Utils.DatabaseHandler;

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

        holder.taskCheckBox.setText(item.getTask());
        holder.taskCheckBox.setChecked(item.getStatus() != 0);

        holder.taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                db.updateStatus(item.getId(), isChecked ? 1 : 0));
    }

    @Override
    public int getItemCount() {
        return todoList == null ? 0 : todoList.size();
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskCheckBox = itemView.findViewById(R.id.todoCheckBox);
        }
    }
}
