package com.kystudio.learnservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {
    private TextView tvOut;
    private EditText etData;
    private Intent intent;
    private MyService.Binder binder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(MainActivity.this, MyService.class);

        tvOut = (TextView) findViewById(R.id.tvOut);
        etData = (EditText) findViewById(R.id.etData);

        findViewById(R.id.btnStartService).setOnClickListener(this);
        findViewById(R.id.btnStopService).setOnClickListener(this);
        findViewById(R.id.btnBindService).setOnClickListener(this);
        findViewById(R.id.btnUnBindService).setOnClickListener(this);
        findViewById(R.id.btnSyncData).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartService:
                intent.putExtra("data",etData.getText().toString());
                startService(intent);
                break;
            case R.id.btnStopService:
                stopService(intent);
                break;
            case R.id.btnBindService:
                bindService(intent, this, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btnUnBindService:
                unbindService(this);
                break;
            case R.id.btnSyncData:
                if (binder != null) {
                    binder.setData(etData.getText().toString());
                }
                break;
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        System.out.println("Service conntected!");
        binder = (MyService.Binder) iBinder;
        binder.getService().setCallback(new MyService.Callback(){
            @Override
            public void onDataChange(String data) {
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putString("data",data);
                msg.setData(b);
                handler.sendMessage(msg);
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvOut.setText(msg.getData().getString("data"));
        }
    };

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        // 异常崩溃退出的时候调用
        System.out.println("Service disconntected!");
    }
}
