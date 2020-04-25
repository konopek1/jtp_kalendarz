package com.example.kalendarz.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kalendarz.R;
import com.example.kalendarz.utils.DateFormatter;
import com.example.kalendarz.utils.RealmProvider;
import io.realm.Realm;
import io.realm.RealmResults;

import java.util.Calendar;
import java.util.Date;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    public RealmResults<Event> mEventList;
    private LayoutInflater mInflater;
    private Realm realm;

    private final static String TIME_RANGE_FORMAT = "%s - %s";

    public EventListAdapter(Context context, RealmResults<Event> eventList) {
        mInflater = LayoutInflater.from(context);
        realm = RealmProvider.getRealm();
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
        renderTimeRangeView(holder, mEventForCurrentRow);

        holder.removeIcon.setOnClickListener((View -> {
            realm.beginTransaction();
            mEventForCurrentRow.deleteFromRealm();
            realm.commitTransaction();
        }));

        holder.checkBox.setOnClickListener((View)->{
            realm.beginTransaction();
            mEventForCurrentRow.setDone(!mEventForCurrentRow.isDone());
            realm.commitTransaction();
        });

        holder.checkBox.setChecked(mEventForCurrentRow.isDone());
    }

    private void renderTimeRangeView(EventViewHolder holder, Event event) {
        if (!event.isToDo()) {
            String formatedTime = formatTimeRange(event.getDate(), event.getEndDate());
            holder.timeRangeView.setText(formatedTime);
            holder.timeRangeView.setVisibility(View.VISIBLE);
        } else {
            holder.timeRangeView.setText(R.string.default_time);
            holder.timeRangeView.setVisibility(View.INVISIBLE);
        }
    }

    private String formatTimeRange(Date startDate, Date endDate) {
        final Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        int startHour = c.get(Calendar.HOUR_OF_DAY);
        int startMin = c.get(Calendar.MINUTE);
        c.setTime(endDate);
        int endHour = c.get(Calendar.HOUR_OF_DAY);
        int endMin = c.get(Calendar.MINUTE);
        return String.format(TIME_RANGE_FORMAT, DateFormatter.formatTimeHHMM(startHour, startMin), DateFormatter.formatTimeHHMM(endHour, endMin));
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
        public final CheckBox checkBox;
        public final ImageView removeIcon;
        final EventListAdapter mAdapter;

        public EventViewHolder(@NonNull View itemView, EventListAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;
            timeRangeView = itemView.findViewById(R.id.eventTimeRange);
            contentView = itemView.findViewById(R.id.eventContent);
            checkBox = itemView.findViewById(R.id.eventCheckBox);
            removeIcon = itemView.findViewById(R.id.removeEventIcon);
        }


    }
}


