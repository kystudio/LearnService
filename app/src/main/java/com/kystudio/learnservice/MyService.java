package com.kystudio.learnservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {
    private boolean serviceRunning;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceRunning = true;
        System.out.println("service create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(){
            @Override
            public void run() {
                super.run();

                while (serviceRunning) {
                    System.out.println("服务正在运行...");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("service destory");
        serviceRunning = false;
    }
}
