package com.mimi.do_it;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mimi.do_it.Model.ToDoModel;
import com.mimi.do_it.Utils.DatabaseHandler;
import com.mimi.do_it.Utils.Utils;

import java.util.Calendar;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddTaskDialog";

    private EditText newTaskText;
    private Button newTaskSaveButton;
    private Button dueDateButton;
    private long selectedDueMillis;

    private DatabaseHandler db;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_task, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        newTaskText = view.findViewById(R.id.newTaskText);
        newTaskSaveButton = view.findViewById(R.id.newTaskButton);
        dueDateButton = view.findViewById(R.id.dueDateButton);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        newTaskSaveButton.setEnabled(false);

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newTaskSaveButton.setEnabled(!s.toString().trim().isEmpty());
                newTaskSaveButton.setTextColor(s.toString().trim().isEmpty()
                        ? Color.GRAY
                        : ContextCompat.getColor(getContext(), R.color.green_dark));
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        dueDateButton.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(getContext(), (view1, y, m, d) -> {
                Calendar selected = Calendar.getInstance();
                selected.set(y, m, d);

                // Show TimePicker
                new TimePickerDialog(getContext(), (view2, hour, minute) -> {
                    selected.set(Calendar.HOUR_OF_DAY, hour);
                    selected.set(Calendar.MINUTE, minute);
                    selectedDueMillis = selected.getTimeInMillis();
                    dueDateButton.setText(Utils.formatDateTime(selectedDueMillis, getContext()));
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();

            }, year, month, day).show();
        });

        newTaskSaveButton.setOnClickListener(v -> {
            String text = newTaskText.getText().toString();
            ToDoModel task = new ToDoModel();
            task.setTask(text);
            task.setStatus(0);
            task.setDueDateMillis(selectedDueMillis);

            Utils.scheduleAfterInsert(db, getContext(), task);

            dismiss();
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (getActivity() instanceof DialogCloseListener) {
            ((DialogCloseListener) getActivity()).handleDialogClose(dialog);
        }
    }
}
