package com.android.bahukhandi.aneesha.calendar.android_calendar_view.adapters;

import android.content.Context;

import com.android.bahukhandi.aneesha.calendar.android_calendar_view.models.DateStateDescriptor;
import com.android.bahukhandi.aneesha.calendar.android_calendar_view.models.MonthDescriptor;
import com.android.bahukhandi.aneesha.calendar.android_calendar_view.ui.CalendarCellView;

import java.util.List;


/**
 * Created by aneesha.bahukhandi on 17/05/17
 */

public class SingleSelectionInMonthAdapter extends BaseMonthAdapter {

    private DateStateDescriptor prevSelected;

    public SingleSelectionInMonthAdapter(Context context, List<MonthDescriptor> monthsList){
        super(context, monthsList);
    }

    @Override
    protected void validateAndMarkBoundaries(DateStateDescriptor descriptor) {
        descriptor.setSingleSelection(true);
        if (prevSelected != null){
            prevSelected.setSingleSelection(false);
        }
        startCal.set(descriptor.getYear(), descriptor.getMonth(), descriptor.getDayOfMonth());
        notifyDataSetChanged();
        if (sListener != null) {
            sListener.onDateSelected(startCal.getTime());
        }
        prevSelected = descriptor;
    }

    @Override
    protected void setCalendarCellState(DateStateDescriptor state, CalendarCellView cell) {
        cell.setSingleSelection(state.isSingleSelection());
        cell.setSelectable(state.isSelectable());
        cell.setToday(state.isToday());
        cell.setTag(state);
    }
}