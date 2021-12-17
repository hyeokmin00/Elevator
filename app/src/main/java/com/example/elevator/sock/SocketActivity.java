package com.example.elevator.sock;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elevator.ConnectionMgr;
import com.example.elevator.R;
import com.example.elevator.api.APIController;
import com.example.elevator.api.model.ErrorResult;
import com.example.elevator.api.model.LiftError;

import org.json.JSONObject;

import java.util.ArrayList;

public class SocketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        ConnectionMgr connectionMgr = new ConnectionMgr();
        APIController apiController = new APIController();
        SockClient sc = new SockClient();

        Log.d("Test", "SocketActivity OnCreated");

       /* try {
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

            Log.d("Test", "SocketActivity - OnCreated Json : " + jo);

       *//*     //todo ErrorCode dummydata 연결함. -> Socket 연결 부분 수정 후 변경 필요
            ArrayList<ErrorResult> errorResult = new ArrayList<ErrorResult>();
            String lift_id = "1234567";
            String lift_status = "1234567";
            ArrayList<LiftError> lift_error = new ArrayList<LiftError>();
            String errCode = "121";
            String datetime = "2021-11-03 20:24:31";


            lift_error.add(new LiftError(errCode, datetime));
            Log.d("Test", "SocketActivity - lift_error.get(0).getErrCode() : " + lift_error.get(0).getErrCode());
            errorResult.add(new ErrorResult(lift_id, lift_status, lift_error));
            Log.d("Test", "SocketActivity - errorResult.get(0).getLiftId() : " + errorResult.get(0).getLiftId());


            //기기로부터 ErrorCode 받아 온 후 disable 해야함.
            connectionMgr.disableWifi();

            //todo jo를 LiftError로 변환 후 ErrorPost -> finish();
            apiController.setRetrofitInit();
            apiController.ErrorPost(errorResult);


            finish();
*//*
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        //todo ErrorCode dummydata 연결함. -> Socket 연결 부분 수정 후 변경 필요
        ErrorResult errorResult;
        String lift_id = "1234569";
        ArrayList<LiftError> lift_errors = new ArrayList<LiftError>();
        String errCode = "123";
        String datetime = "2021-11-17 21:21:01";


        lift_errors.add(new LiftError(Integer.parseInt(errCode), datetime));
        Log.d("Test", "SocketActivity - lift_error.get(0).getErrCode() : " + lift_errors.get(0).getErrCode());
        errorResult = new ErrorResult(Integer.parseInt(lift_id), lift_errors);
        Log.d("Test", "SocketActivity - errorResult.get(0).getLiftId() : " + errorResult.getLiftId());


        //기기로부터 ErrorCode 받아 온 후 disable 해야함.
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.disconnect();

        //todo jo를 LiftError로 변환 후 ErrorPost -> finish();
        apiController.setRetrofitInit();
        apiController.ErrorPost(errorResult);
        finish();




    }
}
