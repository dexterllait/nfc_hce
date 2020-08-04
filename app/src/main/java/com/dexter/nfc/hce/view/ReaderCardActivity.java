package com.dexter.nfc.hce.view;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dexter.nfc.hce.R;
import com.dexter.nfc.hce.common.StringHelper;
import com.dexter.nfc.hce.controller.HCEManager;

public class ReaderCardActivity  extends AppCompatActivity {

    public static final String TAG = "ReaderCardActivity";

    private EditText edtMessage;

    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    public HCEManager mHCEManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_card);

        initVariables();
        initUI();
    }

    private void initVariables() {
        mHCEManager = new HCEManager(new HCEManager.HCEListener() {
            @Override
            public void onDataSent() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ReaderCardActivity.this, "Data sent successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFail(final int errorCode) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        switch (errorCode) {
                            case HCEManager.HCE_ERROR_NOT_CONNECTED:
                                Toast.makeText(ReaderCardActivity.this, "Error to connect emulated card", Toast.LENGTH_SHORT).show();
                                break;

                            case HCEManager.HCE_ERROR_FORMAT_DATA:
                                Toast.makeText(ReaderCardActivity.this, "Error to format data", Toast.LENGTH_SHORT).show();
                                break;

                            case HCEManager.HCE_ERROR_SEND_DATA:
                                Toast.makeText(ReaderCardActivity.this, "Error to send data", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
        });

        // Disable Android Beam and register our card reader callback
        enableReaderMode();
    }

    private void initUI() {
        edtMessage = findViewById(R.id.edt_message);
        Button btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edtMessage.getText().toString();
                if (StringHelper.isEmpty(message)) {
                    Toast.makeText(ReaderCardActivity.this, "Please input message to send", Toast.LENGTH_SHORT).show();
                    return;
                }
                mHCEManager.setData(message.getBytes());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        enableReaderMode();
    }

    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
    }

    private void enableReaderMode() {
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.enableReaderMode(this, mHCEManager, READER_FLAGS, null);
        }
    }

    private void disableReaderMode() {
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.disableReaderMode(this);
        }
    }
}
