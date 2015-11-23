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
        int days = (int) TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        int hours = (int) TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(millis);

        String hoursString = String.format("%02d", hours);
        String minutesString = String.format("%02d", minutes);
        String secondsString = String.format("%02d", seconds);


//        String countDownTimeString = (days > 0) ? days + "D " + hours + ":" + minutes + ":" + seconds : hours + ":" + minutes + ":" + seconds;

        String countDownTimeString = (days > 0) ?
                days + "D " + hoursString + ":" + minutesString + ":" + secondsString : hoursString + ":" + minutesString;

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
