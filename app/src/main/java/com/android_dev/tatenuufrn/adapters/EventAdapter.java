package com.android_dev.tatenuufrn.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.Rating;
import android.os.CountDownTimer;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.activities.EventDetail;
import com.android_dev.tatenuufrn.domain.Event;
import com.android_dev.tatenuufrn.domain.Event$Adapter;
import com.android_dev.tatenuufrn.domain.Event$Table;
import com.android_dev.tatenuufrn.helpers.EventCountDownTimer;
import com.raizlabs.android.dbflow.list.FlowQueryList;
import com.raizlabs.android.dbflow.sql.language.Select;

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

    public EventAdapter(Context context, int id) {
        super(context, id);
        this.context = context;
        this.update();
    }

    private Context context;
    public void update() {
        this.clear();
        events.refresh();
        List<Event> eventsList = new Select()
                .from(Event.class)
                .where()
                .orderBy(true, Event$Table.STARTTIME)
                .queryList();
//        List<Event> eventsList = events.getCursorList().getAll();
        this.addAll(eventsList);
        this.notifyDataSetChanged();
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;

        final Event event = getItem(position);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.event_row, null);

            holder = new ViewHolder();

            holder.title = (TextView) view.findViewById(R.id.titleEventRowTextView);
            holder.image = (ImageView) view.findViewById(R.id.imageEventRowImageView);
            holder.like = (RatingBar) view.findViewById(R.id.eventRowLikeRatingBar);
            holder.rating = (RatingBar) view.findViewById(R.id.eventRowRatingRatingBar);
            holder.timeTitle = (TextView) view.findViewById(R.id.timeTitleEventRowTextView);
            holder.time = (TextView) view.findViewById(R.id.timeEventRowTextView);

            // Set Rating Stars Color
            LayerDrawable stars = (LayerDrawable) holder.rating.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

            view.setTag(holder);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String event_id = event.getId();
                    Intent intent = new Intent(context, EventDetail.class);
                    intent.putExtra("event_id", event_id);
                    context.startActivity(intent);
                }
            });
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (event != null) {
            holder.title.setText(event.getTitle());
            holder.image.setImageBitmap(event.getImageBitmap());
            holder.like.setRating(event.getLikes());
            holder.rating.setRating(event.getRating().floatValue());

//          Calendar nowCalendar = Calendar.getInstance();
//          nowCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));

            long nowMillis = System.currentTimeMillis();
            nowMillis -= 10800000;

            long startTimeMillis = event.getStartTime().longValue() * 1000;
            long endTimeMillis = event.getEndTime().longValue() * 1000;

            long timeToEventMillis = startTimeMillis - nowMillis;
            long timeLeftOfEventMillis = endTimeMillis - nowMillis;

            long daysToEvent = TimeUnit.MILLISECONDS.toDays(timeToEventMillis);

            Date startDate = new Date(startTimeMillis);
            String startDateString = new SimpleDateFormat("dd/MM").format(startDate);

            if (holder.countDownTimer != null)
                holder.countDownTimer.cancel();

            if (daysToEvent > 0) {
                holder.timeTitle.setText("DATE");
                holder.time.setText(startDateString);
            } else if (timeToEventMillis > 0) {
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
        RatingBar like;
        RatingBar rating;
        EventCountDownTimer countDownTimer;
    }
}
