package com.example.taskscheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TasksAdapter extends ArrayAdapter<Tasks> {

    Context context;

    public TasksAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Tasks> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View singleItemView, @NonNull ViewGroup parent) {

        if(singleItemView == null)
        {
            singleItemView = LayoutInflater.from(context).inflate(R.layout.single_tasks_list_item_design, parent, false);
        }

        Tasks t = getItem(position);

        // hooks for single item view
        TextView tvId=singleItemView.findViewById(R.id.tvId);
        TextView tvName = singleItemView.findViewById(R.id.tvName);
        TextView tvDate = singleItemView.findViewById(R.id.tvDate);
        TextView tvDescription = singleItemView.findViewById(R.id.tvDescription);
        TextView tvStatus = singleItemView.findViewById(R.id.tvStatus);

        // bind the data on hooks
        tvId.setText(String.valueOf(t.getId()));
        tvName.setText(t.getTitle());
        tvDate.setText(t.getDate());
        tvDescription.setText(t.getDescription());
        tvStatus.setText("Status: "+t.getStatus());


        return singleItemView;
    }
}
