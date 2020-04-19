package com.example.kalendarz.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kalendarz.R;
import com.example.kalendarz.utils.DateFormatter;
import io.realm.RealmResults;

import java.util.Calendar;
import java.util.Date;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    public  RealmResults<Event> mEventList;
    private LayoutInflater mInflater;

    private final static String TIME_RANGE_FORMAT = "%s - %s";

    public EventListAdapter(Context context, RealmResults<Event> eventList) {
        mInflater = LayoutInflater.from(context);
        this.mEventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mEventRowView = mInflater.inflate(R.layout.event_row, parent, false);
        return new EventViewHolder(mEventRowView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event mEventForCurrentRow = mEventList.get(position);
        holder.contentView.setText(mEventForCurrentRow.getContent());

        String formatedTime = formatTimeRange(mEventForCurrentRow.getDate(), mEventForCurrentRow.getEndDate());

        if (formatedTime != null) {
            holder.timeRangeView.setText(formatedTime);
        } else {
            holder.timeRangeView.setText(R.string.default_time);
            holder.timeRangeView.setVisibility(View.INVISIBLE);
        }

        holder.eventCheckBox.setChecked(false);
    }

    private String formatTimeRange(Date startDate, Date endDate) {
        if (startDate != null & endDate != null) {
            final Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            int startHour = c.get(Calendar.HOUR_OF_DAY);
            int startMin = c.get(Calendar.MINUTE);
            c.setTime(endDate);
            int endHour = c.get(Calendar.HOUR_OF_DAY);
            int endMin = c.get(Calendar.MINUTE);
            return String.format(TIME_RANGE_FORMAT, DateFormatter.formatTimeHHMM(startHour,startMin), DateFormatter.formatTimeHHMM(endHour,endMin));
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public void updateData(RealmResults<Event> events) {
        mEventList = events;
        notifyDataSetChanged();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        public final TextView timeRangeView;
        public final TextView contentView;
        public final CheckBox eventCheckBox;
        final EventListAdapter mAdapter;

        public EventViewHolder(@NonNull View itemView, EventListAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;
            timeRangeView = itemView.findViewById(R.id.eventTimeRange);
            contentView = itemView.findViewById(R.id.eventContent);
            eventCheckBox = itemView.findViewById(R.id.eventCheckBox);
        }


    }
}


