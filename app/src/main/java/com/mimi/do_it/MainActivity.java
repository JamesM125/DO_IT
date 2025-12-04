package com.mimi.do_it;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mimi.do_it.Adapter.ToDoAdapter;
import com.mimi.do_it.Model.ToDoModel;
import com.mimi.do_it.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    RecyclerView tasksRecyclerView;

    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private DatabaseHandler db;
    private List<ToDoModel> taskList = new ArrayList<>();



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        if (tasksRecyclerView != null) {
            tasksRecyclerView.setHasFixedSize(true);
        }

        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHandler(this);
        db.openDatabase();

        fab = findViewById(R.id.fab);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);


        tasksAdapter = new ToDoAdapter(db, this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);


        fab.setOnClickListener((v) -> {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);

        });
    }


    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();

    }
}