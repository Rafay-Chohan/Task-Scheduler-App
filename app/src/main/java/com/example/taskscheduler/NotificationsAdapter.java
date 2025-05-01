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

public class NotificationsAdapter extends ArrayAdapter<Notifications> {
    Context context;

    public NotificationsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Notifications> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View singleItemView, @NonNull ViewGroup parent) {


        if(singleItemView == null)
        {
            singleItemView = LayoutInflater.from(context).inflate(R.layout.single_notification_list_item_design, parent, false);
        }

        Notifications N = getItem(position);

        // hooks for single item view
        TextView tvId=singleItemView.findViewById(R.id.tvId);
        TextView tvMessage = singleItemView.findViewById(R.id.tvMessage);
        TextView tvDate = singleItemView.findViewById(R.id.tvDate);

        // bind the data on hooks
        tvId.setText(String.valueOf(N.getId()));

        tvDate.setText(N.getDate());
        tvMessage.setText(N.getMessage());


        return singleItemView;
    }
}
