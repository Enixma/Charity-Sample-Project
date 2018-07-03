package com.enixma.sample.charity.presentation.creditcard.mapper;

import android.content.Context;

import java.io.IOError;

import co.omise.android.models.APIError;

public class ApiErrorMapper {
    public static String getError(Context context, Throwable throwable) {
        String message = null;
        if (throwable instanceof IOError) {
            message = context.getString(co.omise.android.R.string.error_io, throwable.getMessage());
        } else if (throwable instanceof APIError) {
            message = context.getString(co.omise.android.R.string.error_api, ((APIError) throwable).message);
        } else {
            message = context.getString(co.omise.android.R.string.error_unknown, throwable.getMessage());
        }
        return message;
    }
}
