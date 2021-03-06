package com.android.bahukhandi.aneesha.calendar.android_calendar_view.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.android.bahukhandi.aneesha.calendar.android_calendar_view.R;
import com.android.bahukhandi.aneesha.calendar.android_calendar_view.models.DateStateDescriptor;


public class CalendarCellView extends AppCompatTextView {
    private static final int[] STATE_SELECTABLE = {
            R.attr.state_selectable
    };
    private static final int[] STATE_TODAY = {
            R.attr.state_today
    };
    private static final int[] STATE_RANGE_START = {
            R.attr.state_range_start
    };
    private static final int[] STATE_RANGE_END = {
            R.attr.state_range_end
    };
    private static final int[] STATE_RANGE_MIDDLE = {
            R.attr.state_range_middle
    };
    private static final int[] STATE_PREDEFINED_RANGE_START = {
            R.attr.state_pre_defined_range_start
    };
    private static final int[] STATE_PREDEFINED_RANGE_END = {
            R.attr.state_pre_defined_range_end
    };
    private static final int[] STATE_PRE_DEFINED_RANGE_MIDDLE = {
            R.attr.state_pre_defined_range_middle
    };
    private static final int[] STATE_SINGLE_SELECTION = {
            R.attr.state_single_selection
    };
    private static final int[] STATE_RANGE_OPEN = {
            R.attr.state_range_open
    };

    private boolean isSelectable = false;
    private boolean isToday = false;
    private boolean isSingleSelection = false;

    private DateStateDescriptor.RangeState rangeState = DateStateDescriptor.RangeState.NONE;
    private DateStateDescriptor.RangeState predefinedRangeState = DateStateDescriptor.RangeState.NONE;

    public CalendarCellView(Context context) {
        super(context);
    }

    public CalendarCellView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
        refreshDrawableState();
    }

    public void setToday(boolean isToday) {
        this.isToday = isToday;
        refreshDrawableState();
    }

    public void setRangeState(DateStateDescriptor.RangeState rangeState) {
        this.rangeState = rangeState;
        refreshDrawableState();
    }

    public void setPredefinedRangeState(DateStateDescriptor.RangeState rangeState) {
        this.predefinedRangeState = rangeState;
        refreshDrawableState();
    }

    public void setSingleSelection(boolean singleSelection) {
        isSingleSelection = singleSelection;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 5);

        setClickable(isSelectable);

        if (isSelectable) {
            mergeDrawableStates(drawableState, STATE_SELECTABLE);
        }

        if (isToday) {
            setTypeface(getTypeface(), Typeface.BOLD);
            mergeDrawableStates(drawableState, STATE_TODAY);
        }

        if (isSingleSelection){
            mergeDrawableStates(drawableState, STATE_SINGLE_SELECTION);
        }

        if (rangeState == DateStateDescriptor.RangeState.START) {
            mergeDrawableStates(drawableState, STATE_RANGE_START);
        } else if (rangeState == DateStateDescriptor.RangeState.MIDDLE) {
            mergeDrawableStates(drawableState, STATE_RANGE_MIDDLE);
        } else if (rangeState == DateStateDescriptor.RangeState.END) {
            mergeDrawableStates(drawableState, STATE_RANGE_END);
        }

        if (predefinedRangeState == DateStateDescriptor.RangeState.START){
            mergeDrawableStates(drawableState, STATE_PREDEFINED_RANGE_START);
        } else if (predefinedRangeState == DateStateDescriptor.RangeState.MIDDLE) {
            mergeDrawableStates(drawableState, STATE_PRE_DEFINED_RANGE_MIDDLE);
        } else if (predefinedRangeState == DateStateDescriptor.RangeState.END) {
            mergeDrawableStates(drawableState, STATE_PREDEFINED_RANGE_END);
        } else if (predefinedRangeState == DateStateDescriptor.RangeState.OPEN) {
            mergeDrawableStates(drawableState, STATE_RANGE_OPEN);
        }

        return drawableState;
    }
}
