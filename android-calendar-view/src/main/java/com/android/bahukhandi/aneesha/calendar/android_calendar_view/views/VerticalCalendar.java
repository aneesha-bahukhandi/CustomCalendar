package com.android.bahukhandi.aneesha.calendar.android_calendar_view.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.android.bahukhandi.aneesha.calendar.android_calendar_view.adapters.BaseMonthAdapter;
import com.android.bahukhandi.aneesha.calendar.android_calendar_view.ui.CalendarView;

/**
 * Created by aneesha.bahukhandi on 17/05/17
 */

public class VerticalCalendar extends CalendarView {


    public VerticalCalendar(Context context) {
        super(context);
    }

    public VerticalCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initCalendarView(RecyclerView monthsList) {
        monthsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //attach adapter
        BaseMonthAdapter adapter = getAdapter();
        adapter.setDateSelectionListener(getDateSelectionListener());
        monthsList.setAdapter(adapter);
        monthsList.scrollToPosition(getScrollPosition());
    }
}
