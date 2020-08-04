package com.dexter.nfc.hce;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.dexter.nfc.hce.controller.HCEService;

public class HCEApp extends Application {

    public static Context gContext;

    @Override
    public void onCreate() {
        super.onCreate();
        gContext = getApplicationContext();
    }

    public static void startHCEService(String cmd) {
        Intent intent = new Intent(gContext, HCEService.class);
        intent.putExtra("cmd", cmd);
        gContext.startService(intent);
    }
}
