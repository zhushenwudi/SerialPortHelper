package com.ilab.serialporthelper.serialport;

import android.util.Log;

import com.ilab.serialporthelper.serialport.config.SerialPort2Config;

import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import top.keepempty.sph.library.SerialPortHelper;
import top.keepempty.sph.library.SphResultCallback;

/**
 * @author hoshizora-rin
 */
public class Serial2Util {
    private static final String TAG = "Serial2Util";
    private SerialPortHelper serialPortHelper;
    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r);
                }
            });

    public void start() {
        try {
            serialPortHelper = new SerialPortHelper(16);
            serialPortHelper.setConfigInfo(SerialPort2Config.getSingleton());
            //判断是否打开设备
            if (!serialPortHelper.openDevice()) {
                Log.d(TAG, "serial2 can't open");
            }
            //初始化监听
            serialPortHelper.setSphResultCallback(new SphResultCallback() {
                @Override
                public void onReceiveData(byte[] data) {
                    String str = Arrays.toString(data);
                    Log.i(TAG, str);
                }

                @Override
                public void onComplete() {

                }
            });
            //循环发送
            executorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    serialPortHelper.sendHex("0507FFF001006A50");
                }
            }, 0, 500, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        Log.d(TAG, "serial2 stop");
        try {
            if (serialPortHelper.isOpenDevice()) {
                serialPortHelper.closeDevice();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!executorService.isTerminated()) {
                executorService.shutdownNow();
            }
            serialPortHelper = null;
            executorService = null;
        }
    }
}
