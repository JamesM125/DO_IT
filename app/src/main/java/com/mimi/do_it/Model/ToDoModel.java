package com.mimi.do_it.Model;

public class ToDoModel {
    private int id;
    private int status;
    private String task;
    private long dueDateMillis; // milliseconds since epoch

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getTask() { return task; }
    public void setTask(String task) { this.task = task; }

    public long getDueDateMillis() { return dueDateMillis; }
    public void setDueDateMillis(long dueDateMillis) { this.dueDateMillis = dueDateMillis; }
}
