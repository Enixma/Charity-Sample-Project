package com.enixma.sample.charity.presentation.creditcard;

import org.joda.time.YearMonth;

import co.omise.android.ui.NumberRangeSpinnerAdapter;


public class ExpiryYearSpinnerAdapter extends NumberRangeSpinnerAdapter {
    protected ExpiryYearSpinnerAdapter() {
        super(YearMonth.now().getYear(), YearMonth.now().getYear() + 12);
    }

    protected String getItemDropDownLabel(int number) {
        return Integer.toString(number);
    }

    protected String getItemLabel(int number) {
        return Integer.toString(number).substring(2, 4);
    }
}