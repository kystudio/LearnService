package com.kystudio.learnservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    private boolean serviceRunning = false;
    private String data = "这是默认信息";

    public MyService() {
    }

    public class Binder extends android.os.Binder {
        public void setData(String data) {
            MyService.this.data = data;
        }

        public MyService getService(){
            return MyService.this;
        }
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
        new Thread() {
            @Override
            public void run() {
                super.run();

                int i = 0;

                while (serviceRunning) {
                    i++;
                    String str = i + ":" + data;
                    System.out.println(str);

                    if (callback!=null){
                        callback.onDataChange(str);
                    }

                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("service startcommand");
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//
//                while (serviceRunning) {
//                    System.out.println("服务正在运行...");
//                    try {
//                        sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
        data = intent.getStringExtra("data");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("service Unbind");
        serviceRunning = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("service destory");
        serviceRunning = false;
    }

    private Callback callback = null;

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public static interface Callback{
        void onDataChange(String data);
    }
}
