package com.android.bahukhandi.aneesha.calendar.android_calendar_view.listeners;

import java.util.Date;

/**
 * Created by aneesha.bahukhandi on 16/05/17
 */

public interface DateSelectionListener {
    void onRangeSelected(Date startDate, Date endDate);
    void onDateSelected(Date date);
}
