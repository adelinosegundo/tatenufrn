package com.android_dev.tatenuufrn.adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.domain.Event;
import com.android_dev.tatenuufrn.domain.Event$Adapter;
import com.android_dev.tatenuufrn.domain.Event$Table;
import com.android_dev.tatenuufrn.helpers.EventCountDownTimer;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by adelinosegundo on 10/8/15.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    private FlowQueryList<Event> events = new FlowQueryList<Event>(Event.class);

    public void update() {
        this.clear();
        events.refresh();
        List<Event> eventsList = new Select()
                .from(Event.class)
                .where()
                .orderBy(false, Event$Table.UPDATEDAT)
                .queryList();
//        List<Event> eventsList = events.getCursorList().getAll();
        this.addAll(eventsList);
        this.notifyDataSetChanged();
    }

    static class ViewHolder{
        TextView nameText;
        TextView descriptionText;
        TextView timeTitleText;
        TextView timeText;
        ImageView image;
        EventCountDownTimer countDownTimer;
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
            //holder.descriptionText =  (TextView) view.findViewById(R.id.descriptionEventRowTextView);
            holder.image = (ImageView) view.findViewById(R.id.eventRowImageView);
            holder.timeTitleText = (TextView) view.findViewById(R.id.timeTitleEventRowTextView);
            holder.timeText = (TextView) view.findViewById(R.id.timeEventRowTextView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Event event = getItem(position);

        if (event!= null) {
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            long nowMilis = System.currentTimeMillis();
            nowMilis -= 10800000;

            long startTime = event.getStartTime().longValue()*1000;
            long endTime = event.getEndTime().longValue()*1000;

            long timeToEvent = startTime - nowMilis;
            long timeLeftOfEvent = endTime - nowMilis;

            holder.nameText.setText(event.getTitle());
            //holder.descriptionText.setText(event.getDescription());
            holder.image.setImageBitmap(event.getImageBitmap());
            if (holder.countDownTimer != null)
                holder.countDownTimer.cancel();
            if (timeToEvent > 0) {
                holder.timeTitleText.setText("TIME TO EVENT");
                holder.countDownTimer = new EventCountDownTimer(holder.timeText, timeToEvent, 1000, "Started");
                holder.countDownTimer.start();
            } else if (timeLeftOfEvent > 0) {
                holder.timeTitleText.setText("TIME LEFT");
                holder.countDownTimer = new EventCountDownTimer(holder.timeText, timeLeftOfEvent, 1000, "Over");
                holder.countDownTimer.start();
            } else {
                holder.timeTitleText.setText("DATE");
                holder.timeText.setText("15/10");
            }
        }
        return view;
    }
}
