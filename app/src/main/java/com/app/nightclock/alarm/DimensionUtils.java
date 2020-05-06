package com.app.nightclock.alarm;

import android.content.Context;
import android.util.TypedValue;

class DimensionUtils {

    static int convertDpToPixels(Context ctx, int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, ctx.getResources().getDisplayMetrics());
    }

}
