package com.enixma.sample.charity.presentation.creditcard;

import java.util.Locale;

import co.omise.android.ui.NumberRangeSpinnerAdapter;


public class ExpiryMonthSpinnerAdapter extends NumberRangeSpinnerAdapter {
    protected ExpiryMonthSpinnerAdapter() {
        super(1, 12);
    }

    protected String getItemDropDownLabel(int number) {
        return String.format(Locale.getDefault(), "%02d", new Object[]{Integer.valueOf(number)});
    }

    protected String getItemLabel(int number) {
        return String.format(Locale.getDefault(), "%02d", new Object[]{Integer.valueOf(number)});
    }
}

