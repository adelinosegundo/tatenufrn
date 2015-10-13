package com.android_dev.tatenuufrn.adapters;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android_dev.tatenuufrn.R;
import com.android_dev.tatenuufrn.domain.Event;
import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.list.FlowQueryList;

import java.util.List;

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

            holder.nameText =  (TextView) view.findViewById(R.id.titleTextView);
            holder.descriptionText =  (TextView) view.findViewById(R.id.descriptionTextView);
            holder.image = (ImageView) view.findViewById(R.id.imageView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Event event = getItem(position);
        if (event!= null) {
            holder.nameText.setText(event.getTitle());
            holder.descriptionText.setText(event.getDescription());
            holder.image.setImageBitmap(event.getImageBitmap());
        }
        return view;
    }
}
