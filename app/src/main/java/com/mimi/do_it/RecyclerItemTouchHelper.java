package com.mimi.do_it;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.mimi.do_it.Adapter.ToDoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private final ToDoAdapter adapter;

    public RecyclerItemTouchHelper(ToDoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();

        if (direction == ItemTouchHelper.LEFT) {
            new AlertDialog.Builder(adapter.getContext())
                    .setTitle(R.string.delete_task_title)
                    .setMessage(R.string.delete_task_message)
                    .setPositiveButton(R.string.delete, (dialog, which) -> adapter.deleteItem(position))
                    .setNegativeButton(R.string.cancel, (dialog, which) -> adapter.notifyItemChanged(position))
                    .create()
                    .show();
        } else {
            adapter.editItem(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;

        if (dX > 0) {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit);
            background = new ColorDrawable(Color.parseColor("#006400"));
        } else {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete);
            background = new ColorDrawable(Color.RED);
        }

        int iconMargin = (itemView.getHeight() - (icon != null ? icon.getIntrinsicHeight() : 0)) / 2;

        if (dX > 0) {
            int left = itemView.getLeft() + iconMargin;
            int right = left + (icon != null ? icon.getIntrinsicWidth() : 0);
            if (icon != null)
                icon.setBounds(left, itemView.getTop() + iconMargin, right,
                        itemView.getTop() + iconMargin + icon.getIntrinsicHeight());

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX), itemView.getBottom());

        } else {
            int left = itemView.getRight() - iconMargin - (icon != null ? icon.getIntrinsicWidth() : 0);
            int right = itemView.getRight() - iconMargin;
            if (icon != null)
                icon.setBounds(left, itemView.getTop() + iconMargin, right,
                        itemView.getTop() + iconMargin + icon.getIntrinsicHeight());

            background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(),
                    itemView.getRight(), itemView.getBottom());
        }

        background.draw(c);
        if (icon != null) icon.draw(c);
    }
}
