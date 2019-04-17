package com.example.rpi_online;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button btnUp;
    Button btnDown;
    EditText txtAddress;

    Socket myAppSocket = null;
    public static String wifiModuleIp="";
    public static int wifiModulePort=0;
    public static String CMD ="0";
    public static Socket socket;
    public static String InMSG;
    public static DataOutputStream dataOutputStream;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUp = (Button) findViewById(R.id.btnUP);
        btnDown = (Button) findViewById(R.id.btnDown);

        txtAddress = (EditText) findViewById(R.id.ipAddress);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "UP";
                Soket_AsyncTask cmd_increase_servo = new Soket_AsyncTask();
                cmd_increase_servo.execute();
                //Waits for a reply
                while (InMSG == null) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //Do whatever you need with In MSG
                InMSG = null;
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "DOWN";
                Soket_AsyncTask cmd_decrease_servo = new Soket_AsyncTask();
                cmd_decrease_servo.execute();
                //Waits for a reply
                while (InMSG == null) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //Do whatever you need with In MSG
                InMSG = null;
            }
        });
        Connect connectServer = new Connect();
        connectServer.execute();

        
    }

    public void getIPandPort(){
        String iPandPort = txtAddress.getText().toString();
        String temp[]= iPandPort.split(":");
        wifiModuleIp = temp[0];
        wifiModulePort = Integer.valueOf(temp[1]);


        //Log.d("MYTEST","IP String" +iPandPort);
        //Log.d("MY TEST","IP:"+wifiModuleIp);
        // Log.d("MY TEST","PORT"+wifiModulePort);


    }
    public class Soket_AsyncTask extends AsyncTask<Void,Void,Void>
    {
        protected Void doInBackground(Void... params){
            try{
                dataOutputStream.writeBytes(CMD);
                Scanner sc1 = new Scanner(socket.getInputStream());
                InMSG = sc1.nextLine();


            }catch (UnknownHostException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;

        }

    }
    //Installises connection
    public class Connect extends AsyncTask<Void,Void,Void>
    {
        protected Void doInBackground(Void... params){
            try{
                socket = new Socket("192.168.2.58",5555);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            }catch (UnknownHostException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;

        }

    }

}
