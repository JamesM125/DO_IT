package com.mimi.do_it;

import android.app.Activity;
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

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddTaskDialog";

    private EditText newTaskText;
    private Button newTaskSaveButton;
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

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            newTaskText.setText(bundle.getString("task"));
            newTaskSaveButton.setTextColor(
                    ContextCompat.getColor(getContext(), R.color.green_dark));
        }

        boolean finalIsUpdate = isUpdate;

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

        newTaskSaveButton.setOnClickListener(v -> {
            String text = newTaskText.getText().toString();
            if (finalIsUpdate) {
                db.updateTask(bundle.getInt("id"), text);
            } else {
                ToDoModel task = new ToDoModel();
                task.setTask(text);
                task.setStatus(0);
                db.insertTask(task);
            }
            dismiss();
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener)
            ((DialogCloseListener) activity).handleDialogClose(dialog);
    }
}
