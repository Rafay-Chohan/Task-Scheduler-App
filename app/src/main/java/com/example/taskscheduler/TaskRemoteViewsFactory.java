package com.example.taskscheduler;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class TaskRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private ArrayList<Tasks> taskList = new ArrayList<>();

    public TaskRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        loadTasks();
    }

    private void loadTasks() {
        SchedulerDB db = new SchedulerDB(context);
        db.open();
        taskList = db.readPendingTaskData(); // Or a custom method for top 2 tasks
        db.close();
    }

    @Override
    public void onDataSetChanged() {
        loadTasks();
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Tasks task = taskList.get(position);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        views.setTextViewText(R.id.tvTitle, task.getTitle());
        views.setTextViewText(R.id.tvDate, task.Date);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("task_id", task.id); // Optional
        views.setOnClickFillInIntent(R.id.tvTitle, fillInIntent);

        return views;
    }

    @Override public RemoteViews getLoadingView() { return null; }
    @Override public int getViewTypeCount() { return 1; }
    @Override public long getItemId(int position) { return position; }
    @Override public boolean hasStableIds() { return true; }
    @Override public void onDestroy() { taskList.clear(); }
}

