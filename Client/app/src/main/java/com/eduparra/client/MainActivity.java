package com.eduparra.client;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.view.View;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    TextView serverMessage;
    Thread m_objThreadClient;
    Socket clientSocket;
    Sensor accelerometer;
    SensorManager sm;
    TextView acceleration;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    String accelerometerMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverMessage = (TextView)findViewById(R.id.textView);

        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //sm.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        acceleration = (TextView)findViewById(R.id.accelerometerMessage);
        this.start();
    }

    public void onClick(View view)
    {
        //this.onPause();
        Intent intent = new Intent(view.getContext(),StarterActivity.class);
        startActivity(intent);
    }

    public void start()
    {
        this.onResume();
        m_objThreadClient = new Thread(new Runnable() {
         @Override
         public void run() {
             try {
                 clientSocket = new Socket("127.0.0.1", 2001);
                 oos = new ObjectOutputStream(clientSocket.getOutputStream());
                 oos.writeObject(accelerometerMessage);
                 Message serverMessage = Message.obtain();
                 ois = new ObjectInputStream(clientSocket.getInputStream());
                 String strMessage = (String) ois.readObject();
                 serverMessage.obj = strMessage;

                 mHandler.sendMessage(serverMessage);
                 oos.close();
                 ois.close();
             }
             catch (Exception e)
             {
                 e.printStackTrace();
             }

         }
     });
        m_objThreadClient.start();
    }

    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            messageDisplay(msg.obj.toString());
        }
    };

    public void messageDisplay(String serverMessage)
    {
        this.serverMessage.setText(""+serverMessage);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        accelerometerMessage = "X: "+event.values[0]+
                "\nY: "+event.values[1]+
                "\nZ: "+event.values[2];
        acceleration.setText(accelerometerMessage);
        //m_objThreadClient.run();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

}
