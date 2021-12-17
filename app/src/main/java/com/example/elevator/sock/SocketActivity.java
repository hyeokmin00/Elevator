package com.example.elevator.sock;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elevator.R;

import org.json.JSONObject;

public class SocketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        SockClient sc = new SockClient();

        Log.d("Test", "SocketActivity OnCreated");

        try {
            JSONObject obj = new JSONObject();
            obj.put("cmd", (byte) 0x21);
            obj.put("length", (byte) 0x06);
            obj.put("data", null);

            ThreadSend tmpThread = new ThreadSend(obj);
            Thread threadSend = new Thread(tmpThread);

            ThreadRecieve threadRecieve = new ThreadRecieve();

            threadSend.start();
            try {
                threadSend.join();
            } catch (Exception e) {
                e.printStackTrace();
            }

            threadRecieve.start();

            try {
                threadRecieve.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            JSONObject jo = threadRecieve.getResult(); // 이거 출력 되면 성공(.toString() 으로 로그출력,,)
            Log.d("Test", "SocketActivity - OnCreated Json : "+jo);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
