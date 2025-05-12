package com.example.taskscheduler;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class PastTabFragment extends Fragment {



    public PastTabFragment() {
        // Required empty public constructor
    }


    Context c;
    ListView lvTasks;
    TasksAdapter adapter;
    ArrayList<Tasks> Tasks=new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        c=context;
        super.onAttach(context);
    }
    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
    }
    public static PastTabFragment newInstance(String param1, String param2) {
        PastTabFragment fragment = new PastTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_past_tab, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvTasks=view.findViewById(R.id.lvPastTasks);


        adapter = new TasksAdapter(c, R.layout.single_tasks_list_item_design, Tasks);
        lvTasks.setAdapter(adapter);
        loadTasks();
    }
    private void loadTasks() {
        SchedulerDB db=new SchedulerDB(c);
        db.open();
        Tasks.clear();
        Tasks.addAll(db.readPastTaskData());
        db.close();
        adapter.notifyDataSetChanged();
        AppWidgetManager manager = AppWidgetManager.getInstance(requireContext());
        ComponentName widget = new ComponentName(requireContext(), AddTaskWidget.class);
        manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(widget), R.id.lvWidgetTasks);

    }
}