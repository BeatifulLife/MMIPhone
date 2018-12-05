package com.example.xu.mmiphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            new MMIPhone().getCalibrate();
        }
    }
}
