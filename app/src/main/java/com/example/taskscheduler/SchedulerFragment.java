package com.example.taskscheduler;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class SchedulerFragment extends Fragment {




    Context c;
    ListView lvTasks;
    TasksAdapter adapter;
    ArrayList<Tasks> Tasks=new ArrayList<>();
    FloatingActionButton fabAdd;

    public SchedulerFragment() {
        // Required empty public constructor
    }
    public interface OnTaskAddedListener {
        void onTaskAdded();
    }
    private OnTaskAddedListener callback;

    @Override
    public void onAttach(@NonNull Context context) {
        c=context;
        super.onAttach(context);

    }

    public static SchedulerFragment newInstance(String param1, String param2) {
        SchedulerFragment fragment = new SchedulerFragment();
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
        return inflater.inflate(R.layout.fragment_scheduler, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabAdd=view.findViewById(R.id.fabAdd);
        lvTasks=view.findViewById(R.id.lvPendingTasks);
        adapter = new TasksAdapter(c, R.layout.single_tasks_list_item_design, Tasks);
        lvTasks.setAdapter(adapter);
        loadTasks();
        fabAdd.setOnClickListener(v -> addTaskDialog());

    }
    private void loadTasks() {
        SchedulerDB db=new SchedulerDB(c);
        db.open();
        Tasks.clear();
        Tasks.addAll(db.readPendingTaskData());
        db.close();
        adapter.notifyDataSetChanged();
    }
    private void addTaskDialog(){
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null);

        EditText etTaskName = dialogView.findViewById(R.id.etTaskName);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        EditText etDateTime = dialogView.findViewById(R.id.etDateTime);
        EditText etTaskStatus = dialogView.findViewById(R.id.etTaskStatus);

        etDateTime.setOnClickListener(v -> showDateTimePicker(etDateTime));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Add New Task")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (d, which) -> {
                    Tasks task=new Tasks(0,etTaskName.getText().toString(),etDescription.getText().toString(),etDateTime.getText().toString(),etTaskStatus.getText().toString());

                    if (!task.getTitle().isEmpty() && !task.Date.isEmpty()) {
                        addTask(task);
                    } else {
                        Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();


        // 6. Show the dialog
        dialog.show();
    }
    private void addTask(Tasks task){
        SchedulerDB db = new SchedulerDB(requireContext());
        db.open();
        db.addTask(task);
        db.close();
        Tasks.add(task);
        adapter.notifyDataSetChanged();

        AppWidgetManager manager = AppWidgetManager.getInstance(requireContext());
        ComponentName widget = new ComponentName(requireContext(), AddTaskWidget.class);
        manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(widget), R.id.lvWidgetTasks);

    }
    private void showDateTimePicker(EditText targetEditText) {
        Calendar calendar = Calendar.getInstance();

        // Date Picker
        DatePickerDialog datePicker = new DatePickerDialog(
                requireContext(),
                (view, year, month, day) -> {
                    calendar.set(year, month, day);

                    // Time Picker (shows after date is selected)
                    TimePickerDialog timePicker = new TimePickerDialog(
                            requireContext(),
                            (view1, hour, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);

                                // Format: "Jun 15, 2:30 PM"
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                targetEditText.setText(sdf.format(calendar.getTime()));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                    );
                    timePicker.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.show();
    }
}