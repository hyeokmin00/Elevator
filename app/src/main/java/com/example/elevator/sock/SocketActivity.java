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

import org.json.JSONException;
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

            Log.d("Test", "SocketActivity - OnCreated Json : " + jo);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //todo ErrorCode dummydata 연결함. -> Socket 연결 부분 수정 후 변경 필요
        ErrorResult errorResult;
        String lift_id = "1234572";
        ArrayList<LiftError> lift_errors = new ArrayList<LiftError>();
        String errCode = "128";
        String datetime = "2021-11-18 21:21:01";

        lift_errors.add(new LiftError(Integer.parseInt(errCode), datetime));
        Log.d("Test", "SocketActivity - lift_error.get(0).getErrCode() : " + lift_errors.get(0).getErrCode());
        errorResult = new ErrorResult(Integer.parseInt(lift_id), lift_errors);
        Log.d("Test", "SocketActivity - errorResult.get(0).getLiftId() : " + errorResult.getLiftId());


        //wifi 기기는 서버로 통신 불가
        connectionMgr.disableWifi();

        //todo jo를 LiftError로 변환 후 ErrorPost -> finish();
        apiController.setRetrofitInit();
        apiController.ErrorPost(errorResult);
        finish();

    }
}
