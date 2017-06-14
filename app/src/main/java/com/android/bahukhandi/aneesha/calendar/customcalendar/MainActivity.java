package com.android.bahukhandi.aneesha.calendar.customcalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.bahukhandi.aneesha.calendar.android_calendar_view.listeners.DateSelectionListener;
import com.android.bahukhandi.aneesha.calendar.android_calendar_view.ui.CalendarView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements DateSelectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarView calendar = (CalendarView) findViewById(R.id.calendar_view);
        calendar.setDateSelectionListener(this);
    }

    @Override
    public void onRangeSelected(Date startDate, Date endDate) {
        Log.e("Range :: start - end  ", startDate + "  " + endDate);
    }

    @Override
    public void onDateSelected(Date date) {
        Log.e("Date :: ", date + "  ");
    }
}
