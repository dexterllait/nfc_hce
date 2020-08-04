package com.dexter.nfc.hce.view;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dexter.nfc.hce.HCEApp;
import com.dexter.nfc.hce.R;
import com.dexter.nfc.hce.common.Constant;

public class EmulateCardActivity extends AppCompatActivity {

    public static final String TAG = "EmulateCardActivity";

    TextView txtReceiveData;
    TextView txtMessage;

    ServiceReceiver serviceReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emulate_card);

        initVariables();
        initUI();

        doMiscTask();
    }

    private void initVariables() {
        serviceReceiver = new ServiceReceiver();
        registerReceiver(serviceReceiver, new IntentFilter(Constant.ACTION_SERVICE_STATUS));
    }

    private void initUI() {
        txtReceiveData = findViewById(R.id.txt_receive_data);
        txtReceiveData.setVisibility(View.GONE);
        txtMessage = findViewById(R.id.txt_message);
        txtMessage.setMovementMethod(new ScrollingMovementMethod());
    }

    private void doMiscTask() {
        HCEApp.startHCEService("START");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        HCEApp.startHCEService("STOP");

        unregisterReceiver(serviceReceiver);
        serviceReceiver = null;
    }

    private class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;

            int cmd = intent.getIntExtra("cmd", -1);
            switch (cmd) {
                case 0: // Received AID
                    txtReceiveData.setVisibility(View.GONE);
                    txtMessage.setText("");
                    break;
                case 1: // Start receiving
                    txtReceiveData.setVisibility(View.VISIBLE);
                    break;
                case 2: // End Receiving
                    txtReceiveData.setVisibility(View.GONE);
                    String msg = intent.getStringExtra("data");
                    txtMessage.setText(msg);
                    break;
                case 3: // Error
                    txtReceiveData.setVisibility(View.GONE);
                    txtMessage.setText("Something went wrong. Please try again.");
                    break;
            }
        }
    }
}
