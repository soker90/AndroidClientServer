package com.eduparra.server;

import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.ReadWriteLock;

import android.os.Handler;

public class MyServer {
    Thread m_objThread;
    ServerSocket m_server;
    DataDisplay m_dataDisplay;

    public MyServer()
    {

    }

    public void setEventListener(DataDisplay dataDisplay)
    {
        m_dataDisplay = dataDisplay;
    }

    public void startListening()
    {
        m_objThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    m_server = new ServerSocket(5000);
                    Socket m_soker = m_server.accept();
                    BufferedReader br = new BufferedReader(new InputStreamReader(m_soker.getInputStream()));
                    String a = br.readLine();
                    m_dataDisplay.Display(a);

                    /*Message clientmessage = Message.obtain();
                    ObjectInputStream ois = new ObjectInputStream(connectedSocket.getInputStream());
                    String strMessage = (String)ois.readObject();
                    clientmessage.obj = strMessage;
                    mHandler.sendMessage(clientmessage);
                    ObjectOutputStream oos = new ObjectOutputStream(connectedSocket.getOutputStream());
                    oos.writeObject("Recived: " + strMessage);
                    oos.close();
                    ois.close();*/
                } catch (Exception e)
                {
                    /*Message msg3 = Message.obtain();
                    msg3.obj = e.getMessage();
                    mHandler.sendMessage(msg3);*/
                    e.printStackTrace();
                }
            }
        });
        m_objThread.start();
    }

    /*Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message status) {
            m_dataDisplay.Display(status.obj.toString());

        }
    };*/

}
