package com.eduparra.client;

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

public class MainActivity extends AppCompatActivity {

    TextView serverMessage;
    Thread m_objThreadClient;
    Socket clientSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverMessage = (TextView)findViewById(R.id.textView);
    }

    public void start(View view)
    {
     m_objThreadClient = new Thread(new Runnable() {
         @Override
         public void run() {
             try {
                 clientSocket = new Socket("127.0.0.1", 2001);
                 ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                 oos.writeObject("Hello");
                 Message serverMessage = Message.obtain();
                 ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
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
}
