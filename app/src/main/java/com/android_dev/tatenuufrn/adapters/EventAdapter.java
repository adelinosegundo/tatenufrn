package com.android_dev.tatenuufrn.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.domain.Event;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by adelinosegundo on 10/8/15.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    private FlowQueryList<Event> events = new FlowQueryList<Event>(Event.class);

    public void update() {
        this.clear();
        events.refresh();
        this.addAll(events.getCursorList().getAll());
        this.notifyDataSetChanged();
    }

    static class ViewHolder{
        TextView nameText;
        TextView descriptionText;
        TextView timeTitleText;
        TextView timeText;
        ImageView image;
    }

    public EventAdapter(Context context, int id) {
        super(context, id);
        this.update();
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.event_row, null);
            holder = new ViewHolder();

            holder.nameText =  (TextView) view.findViewById(R.id.titleEventRowTextView);
            holder.descriptionText =  (TextView) view.findViewById(R.id.descriptionEventRowTextView);
            holder.image = (ImageView) view.findViewById(R.id.eventRowImageView);
            holder.timeTitleText = (TextView) view.findViewById(R.id.timeTitleEventRowTextView);
            holder.timeText = (TextView) view.findViewById(R.id.timeEventRowTextView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Event event = getItem(position);
        Calendar nowCalendar = Calendar.getInstance();
        Calendar eventStartCalendar = Calendar.getInstance();
        Calendar eventEndCalendar = Calendar.getInstance();
        eventStartCalendar.setTimeInMillis(event.getStartTime()*1000);
        eventEndCalendar.setTimeInMillis(event.getEndTime() * 1000);
        int nowSeconds = nowCalendar.get(Calendar.SECOND);
        SimpleDateFormat sDate = new SimpleDateFormat("HH:mm");
        int timeToEvent = event.getStartTime()-nowSeconds;
        int timeLeftOfEvent = event.getEndTime()-nowSeconds;
        if (event!= null) {
            holder.nameText.setText(event.getTitle());
            holder.descriptionText.setText(event.getDescription());
            holder.image.setImageBitmap(event.getImageBitmap());
            if (timeToEvent > 0) {
                holder.timeTitleText.setText("TIME TO EVENT");
                holder.timeText.setText(DateUtils.formatElapsedTime(timeToEvent));
            } else if (timeLeftOfEvent > 0) {
                holder.timeTitleText.setText("TIME LEFT");
                holder.timeText.setText(DateUtils.formatElapsedTime(timeLeftOfEvent));
            } else {
                holder.timeTitleText.setText("EVENT IS OVER");
                holder.timeText.setText("");
            }

        }
        return view;
    }
}
