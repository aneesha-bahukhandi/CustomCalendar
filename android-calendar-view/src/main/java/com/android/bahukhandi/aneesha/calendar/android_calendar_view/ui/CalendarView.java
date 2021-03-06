package com.android.bahukhandi.aneesha.calendar.android_calendar_view.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.android.bahukhandi.aneesha.calendar.android_calendar_view.R;
import com.android.bahukhandi.aneesha.calendar.android_calendar_view.adapters.BaseMonthAdapter;
import com.android.bahukhandi.aneesha.calendar.android_calendar_view.adapters.RangeInMonthAdapter;
import com.android.bahukhandi.aneesha.calendar.android_calendar_view.adapters.SingleSelectionInMonthAdapter;
import com.android.bahukhandi.aneesha.calendar.android_calendar_view.enums.DateSelectionMode;
import com.android.bahukhandi.aneesha.calendar.android_calendar_view.listeners.DateSelectionListener;
import com.android.bahukhandi.aneesha.calendar.android_calendar_view.models.DateStateDescriptor;
import com.android.bahukhandi.aneesha.calendar.android_calendar_view.models.MonthDescriptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by aneesha.bahukhandi on 15/05/17
 */

public abstract class CalendarView extends LinearLayout {

    private Calendar mToday;
    private List<MonthDescriptor> mMonthDescriptorsList;
    private SimpleDateFormat mFullMonthNameFormat;
    private DateSelectionListener mDateSelectionListener;

    private int mNumberOfPreviousMonths;
    private int mNumberOfFutureMonths;
    private int mScrollPosition = 0;
    private DateSelectionMode mSelectionMode = DateSelectionMode.SINGLE;
    private int[] mStartPredefinedRange = {NA, NA, NA};  //[day, month, year]
    private int[] mEndPredefinedRange = {NA, NA, NA};  //[day, month, year]

    private static final int NA = -1;
    private static final int rotation = 1;
    private static final int defaultMonths = 0;
    private static final int monthsInAYear = 12;

    protected abstract void initCalendarView(RecyclerView recyclerView);

    public CalendarView(Context context) {
        this(context, null, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.calendar_size);
        this.mNumberOfPreviousMonths = typedArray.getInteger(R.styleable.calendar_size_prev_months, NA);
        this.mNumberOfFutureMonths = typedArray.getInteger(R.styleable.calendar_size_next_months, NA);
        typedArray.recycle();
        reCreateCalendar();
    }

    public void setNumberOfPreviousMonths(int numberOfPreviousMonths) {
        this.mNumberOfPreviousMonths = numberOfPreviousMonths;
        reCreateCalendar();
    }

    public void setNumberOfFutureMonths(int numberOfFutureMonths) {
        this.mNumberOfFutureMonths = numberOfFutureMonths;
        reCreateCalendar();
    }

    public void reCreateCalendar() {
        initData();
        initView();
    }

    public void setDateSelectionListener(DateSelectionListener mDateSelectionListener) {
        this.mDateSelectionListener = mDateSelectionListener;
        reCreateCalendar();
    }

    public void setSelectionMode(DateSelectionMode mSelectionMode) {
        this.mSelectionMode = mSelectionMode;
        reCreateCalendar();
    }

    public void setPredefinedRange(Calendar start, Calendar end) {
        if (start.compareTo(end) < 0) { //start < end
            populateCalendarInArray(this.mStartPredefinedRange, start);
            populateCalendarInArray(this.mEndPredefinedRange, end);
        } else {
            throw new IllegalArgumentException("start date cannot be greater than end date");
        }
        reCreateCalendar();
    }

    public DateSelectionListener getDateSelectionListener() {
        return mDateSelectionListener;
    }

    public int getScrollPosition() {
        return mScrollPosition;
    }

    private void populateCalendarInArray(int[] predefinedRange, Calendar calendar) {
        predefinedRange[0] = calendar.get(Calendar.DAY_OF_MONTH);
        predefinedRange[1] = calendar.get(Calendar.MONTH);
        predefinedRange[2] = calendar.get(Calendar.YEAR);
    }

    private void initData() {
        this.mToday = Calendar.getInstance();
        this.mMonthDescriptorsList = new ArrayList<>();
        this.mFullMonthNameFormat = new SimpleDateFormat(getContext().getString(R.string.header_month_name_format),
                Locale.getDefault());
        initMonths();
    }

    private void initMonths() {
        Calendar calculationCalendar = Calendar.getInstance();
        clearCalendar(calculationCalendar);
        int startMonth = mToday.get(Calendar.MONTH);
        if (this.mNumberOfPreviousMonths > defaultMonths) {
            int currMonth = startMonth;
            startMonth = currMonth - this.mNumberOfPreviousMonths + 1;
            while (startMonth < 0) {
                calculationCalendar.set(Calendar.YEAR, mToday.get(Calendar.YEAR) - 1);
                startMonth += monthsInAYear;
            }
        }
        calculationCalendar.set(Calendar.MONTH, startMonth);
        while (calculationCalendar.get(Calendar.MONTH) != mToday.get(Calendar.MONTH)
                || calculationCalendar.get(Calendar.YEAR) != mToday.get(Calendar.YEAR)) {
            this.mMonthDescriptorsList.add(getNewMonthDescriptorForMonth(calculationCalendar));
            if (calculationCalendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
                calculationCalendar.set(Calendar.YEAR, calculationCalendar.get(Calendar.YEAR) + 1);
                calculationCalendar.set(Calendar.MONTH, Calendar.JANUARY);
            } else {
                calculationCalendar.set(Calendar.MONTH, calculationCalendar.get(Calendar.MONTH) + 1);
            }
        }
        this.mScrollPosition = mMonthDescriptorsList.size();
        this.mMonthDescriptorsList.add(getNewMonthDescriptorForMonth(calculationCalendar));
        if (this.mNumberOfFutureMonths > defaultMonths) {
            int i = 0;
            int currMonth = mToday.get(Calendar.MONTH);
            while (++i <= this.mNumberOfFutureMonths) {
                currMonth++;
                if (currMonth > Calendar.DECEMBER) {
                    currMonth = Calendar.JANUARY;
                    calculationCalendar.set(Calendar.YEAR, calculationCalendar.get(Calendar.YEAR) + 1);
                }
                calculationCalendar.set(Calendar.MONTH, currMonth);
                this.mMonthDescriptorsList.add(getNewMonthDescriptorForMonth(calculationCalendar));
            }
        }
    }

    private void clearCalendar(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private MonthDescriptor getNewMonthDescriptorForMonth(Calendar calendar) {
        String monthName = mFullMonthNameFormat.format(calendar.getTime());

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 - rotation; //SUN - SAT :: 1 - 7 in Java
        if (firstDayOfWeek < 0) {
            firstDayOfWeek += 7;
        }

        int daysCount = getNumberOfDays(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        int currDate = mToday.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && mToday.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) ?
                mToday.get(Calendar.DAY_OF_MONTH) : DateStateDescriptor.noCurrDateInMonth;
        if (currDate == DateStateDescriptor.noCurrDateInMonth) {
            if (calendar.compareTo(mToday) < 0) {  //for prev months everything is unselectable. So currDate == daysCount
                currDate = daysCount + 1;
            }
            //for future months everything is selectable. So currDate == 0
        }

        MonthDescriptor monthDescriptor = new MonthDescriptor(daysCount, firstDayOfWeek, monthName, currDate,
                calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

        int startPredefinedRange = NA, endPredefinedRange = daysCount + 1;
        if (this.mStartPredefinedRange[1] == (int) calendar.get(Calendar.MONTH) &&
                this.mStartPredefinedRange[2] == (int) calendar.get(Calendar.YEAR)) {
            startPredefinedRange = this.mStartPredefinedRange[0];
        }
        if (this.mEndPredefinedRange[1] == (int) calendar.get(Calendar.MONTH) &&
                this.mEndPredefinedRange[2] == (int) calendar.get(Calendar.YEAR)) {
            endPredefinedRange = this.mEndPredefinedRange[0];
            //predefined range ends in another month
            startPredefinedRange = startPredefinedRange == NA ? 1 : startPredefinedRange;
        }
        monthDescriptor.setPredefinedRange(startPredefinedRange, endPredefinedRange);

        return monthDescriptor;
    }

    private int getNumberOfDays(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.FEBRUARY:
                if (year % 100 == 0) {
                    return year % 4 == 0 ? 29 : 28;
                }
                return year % 4 == 0 ? 29 : 28;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
            default:
                return 30;
        }
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parentLayout = (LinearLayout) inflater.inflate(R.layout.calendar_view, this, true);
        RecyclerView monthsList = (RecyclerView) parentLayout.findViewById(R.id.rv_months_list);
        initCalendarView(monthsList);
    }

    protected BaseMonthAdapter getAdapter() {
        switch (mSelectionMode) {
            case RANGE:
                return new RangeInMonthAdapter(getContext(), this.mMonthDescriptorsList);
            case SINGLE:
            default:
                return new SingleSelectionInMonthAdapter(getContext(), mMonthDescriptorsList);
        }
    }

}