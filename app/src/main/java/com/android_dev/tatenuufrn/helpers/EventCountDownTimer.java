package com.android_dev.tatenuufrn.helpers;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by adelinosegundo on 10/14/15.
 */
public class EventCountDownTimer extends CountDownTimer {

    private TextView timeTextView;
    private String finishMessage;

    public EventCountDownTimer(TextView timeTexView, long millisInFuture, long countDownInterval, String finishMessage) {
        super(millisInFuture, countDownInterval);
        this.timeTextView = timeTexView;
        this.finishMessage = finishMessage;

    }

    @Override
    public void onTick(long millisUntilFinished) {
        this.timeTextView.setText(EventCountDownTimer.getTimeBetweenDates(millisUntilFinished));
    }

    private static String getTimeBetweenDates(long miliesLeft) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(miliesLeft);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(miliesLeft);
        long hours = TimeUnit.MILLISECONDS.toHours(miliesLeft);
        long days = TimeUnit.MILLISECONDS.toDays(miliesLeft);

        String timeBetweenDates = "";
        if (days > 0){
            timeBetweenDates += days + " days ";
            seconds -= days * 24 * 60 * 60;
        }
        timeBetweenDates += hours + ":";
        seconds -= hours * 60 * 60;
        timeBetweenDates += minutes + ":";
        seconds -= minutes * 60;
        timeBetweenDates += seconds;
        return timeBetweenDates;
    }

    @Override
    public void onFinish() {
        this.timeTextView.setText(finishMessage);
    }
}
