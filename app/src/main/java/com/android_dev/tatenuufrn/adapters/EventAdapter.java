package com.android_dev.tatenuufrn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.domain.Event;
import com.android_dev.tatenuufrn.helpers.EventCountDownTimer;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by adelinosegundo on 10/8/15.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    private FlowQueryList<Event> events = new FlowQueryList<Event>(Event.class);

    public EventAdapter(Context context, int id) {
        super(context, id);
        this.update();
    }

    public void update() {
        this.clear();
        events.refresh();
        List<Event> eventsList = events.getCursorList().getAll();
        Collections.reverse(eventsList);
        this.addAll(eventsList);
        this.notifyDataSetChanged();
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.event_row, null);

            holder = new ViewHolder();

            holder.title = (TextView) view.findViewById(R.id.titleEventRowTextView);
            holder.image = (ImageView) view.findViewById(R.id.imageEventRowImageView);
            holder.timeTitle = (TextView) view.findViewById(R.id.timeTitleEventRowTextView);
            holder.time = (TextView) view.findViewById(R.id.timeEventRowTextView);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Event event = getItem(position);

        if (event != null) {
            holder.title.setText(event.getTitle());
            holder.image.setImageBitmap(event.getImageBitmap());

//            Calendar nowCalendar = Calendar.getInstance();
//            nowCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));

            long nowMillis = System.currentTimeMillis();
            nowMillis -= 10800000;

            long startTimeMillis = event.getStartTime().longValue() * 1000;
            long endTimeMillis = event.getEndTime().longValue() * 1000;

            long timeToEventMillis = startTimeMillis - nowMillis;
            long timeLeftOfEventMillis = endTimeMillis - nowMillis;

            if (holder.countDownTimer != null)
                holder.countDownTimer.cancel();

            if (timeToEventMillis > 0) {
                holder.timeTitle.setText("TIME TO EVENT");
                holder.countDownTimer = new EventCountDownTimer(holder.time, timeToEventMillis, 1000, "Started");
                holder.countDownTimer.start();
            } else if (timeLeftOfEventMillis > 0) {
                holder.timeTitle.setText("TIME LEFT");
                holder.countDownTimer = new EventCountDownTimer(holder.time, timeLeftOfEventMillis, 1000, "Over");
                holder.countDownTimer.start();
            } else {
                holder.timeTitle.setText("");
                holder.time.setText("");
            }
        }
        return view;
    }

    static class ViewHolder {
        TextView title;
        TextView timeTitle;
        TextView time;
        ImageView image;
        EventCountDownTimer countDownTimer;
    }
}
