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

    private static String millisToCountDownTimeString(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        String countDownTimeString = (days > 0) ?
                days + "D " + hours + ":" + minutes + ":" + seconds : hours + ":" + minutes + ":" + seconds;

        return countDownTimeString;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        this.timeTextView.setText(EventCountDownTimer.millisToCountDownTimeString(millisUntilFinished));
    }

    @Override
    public void onFinish() {
        this.timeTextView.setText(finishMessage);
    }
}
