package com.eduparra.client;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ReadingSensors extends IntentService implements SensorEventListener {

    private static SensorManager sm;
    private Sensor mSensor;
    private static Socket socket = null;
    private static final int SERVERPORT = 5000;
    private static final String SERVER_IP = "192.168.1.129";
    public  Thread m_objThread;
    private String accelerometerMessage = "Client Connected";
    private static boolean running;

    public ReadingSensors() {
        super("ReadingSensor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (intent != null) {
            sm.registerListener(this, mSensor,sm.SENSOR_DELAY_NORMAL);
        }

        m_objThread = new Thread(new Runnable() {
            @Override
            public void run() {
                running = true;
                while (running) {
                    try {
                        InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                        socket = new Socket(serverAddr, SERVERPORT);
                        PrintWriter out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())), true);
                        out.println(accelerometerMessage);
                        Log.i("Message send", accelerometerMessage);
                        out.close();
                        socket.close();
                    } catch (Exception e) {
                        Log.i("Message Excep. Thread", e.getMessage());
                    } catch (Throwable throwable) {
                        Log.i("Message Excep. Thread", throwable.getMessage());
                    }
                }
            }
        });

        m_objThread.start();

    }


    public static void terminate()
    {
        running = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerMessage = "X: " + event.values[0] +
                    ", Y: " + event.values[1] +
                    ", Z: " + event.values[2];

            Log.i("Service Accelerometer", accelerometerMessage);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
