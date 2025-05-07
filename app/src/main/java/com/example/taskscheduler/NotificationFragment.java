package com.example.taskscheduler;

import android.app.AlertDialog;
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


public class NotificationFragment extends Fragment {


    Context c;
    ListView lvNotifications;
    NotificationsAdapter adapter;
    ArrayList<Notifications> Notifications=new ArrayList<>();


    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        c=context;
        super.onAttach(context);

    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvNotifications=view.findViewById(R.id.lvNotifications);
        adapter = new NotificationsAdapter(c, R.layout.single_notification_list_item_design, Notifications);
        lvNotifications.setAdapter(adapter);
        loadNotifications();
        lvNotifications.setOnItemClickListener((parent, v, position, id) -> {
            Notifications notification = (Notifications) parent.getItemAtPosition(position);

            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Notification")
                    .setMessage("Are you sure you want to delete this notification?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        // Delete from database
                        deleteNotification(notification.getId(),position);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotifications();
    }

    private void loadNotifications() {
        SchedulerDB db=new SchedulerDB(requireContext());
        db.open();

        Notifications.clear();
        db.checkUpcomingTasks();//Can be scheduled, right now hard coded
        Notifications.addAll(db.readNotificationsData());
        db.close();
        adapter.notifyDataSetChanged();
    }
    private void deleteNotification(int id,int position){
        SchedulerDB db=new SchedulerDB(requireContext());
        db.open();

        db.deleteNotification(id);
        db.close();
        // Refresh the list
        Notifications.remove(position);
        adapter.notifyDataSetChanged();
    }
}