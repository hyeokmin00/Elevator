package com.example.elevator.sock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.elevator.ConnectionMgr;
import com.example.elevator.R;
import com.example.elevator.api.APIController;
import com.example.elevator.ui.report.WriteReportActivity;
import com.example.elevator.utils.NetStat;

import org.json.JSONObject;

import java.util.ArrayList;

//todo EnableWifi 를 통해 기기 연결 후 에러 목록 변수에 저장하기
// lte 네트워크 연결 APIController에서 ErroPost로 보냄 -> WriteActivity로 화면 전환

public class SocketActivity extends AppCompatActivity {
    /* EnableWifi 이용해서 와이파이 기기 연결 후 ErrorLift 저장
    -> wifi 연결 해제 후 ErrorPost 메서드 이용해 서버로 전송
    -> liftID WriteReportActivity에 전달 및 화면 전환*/

    String ssidPattern;
    String password;
    public String liftId;

    static final int PERMISSIONS_REQUEST = 0x0000001;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    APIController apiController = new APIController();
    Context context = this;
    NetStat netStat = new NetStat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        ConnectionMgr connectionMgr = new ConnectionMgr(context);
        APIController apiController = new APIController();
        SockClient sc = new SockClient();

        Intent intent = getIntent();
        liftId = intent.getStringExtra("lift_id");

        Log.d("Test", "SocketActivity OnCreated");

        Boolean wifiStat = netStat.isWIFIConnected(this);
        Boolean mobileStat = netStat.isMOBILEConnected(this);
        //wifi 기기와 연결 - 기기가 하나뿐이라 임시로 하드 코딩함
        ssidPattern = "CarKey";
        password = "1234qqqq";

        ConnectionMgr cmg = new ConnectionMgr(context);
        cmg.disableWifi(); // 네트워크가 되는 와이파이가 연결되어 있는 경우 ssid가 일치하는 기기로 연결해야하기 때문에
        if (!wifiStat) {
        //    cmg.disableWifi();
           // cmg.enableWifi();
            try {

                JSONObject sendObj = new JSONObject();
                sendObj.put("cmd", (byte) 0x21);
                sendObj.put("length", (byte) 0x06);
                sendObj.put("data", null);

                ThreadSendAndRecieve sarThread = new ThreadSendAndRecieve(sendObj);
                Thread thread = new Thread(sarThread);

                thread.start();
                try {
                    thread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONObject Data = sarThread.getResult();
                Log.d("Test",Data.toString());

                cmg.disableWifi();


       /*
        jsonObject ErrorPost = JSONObject ErrorPost 맞게 형변환
        ErrorResult errorResult;
                String lift_id = "1234572";
                ArrayList<LiftError> lift_errors = new ArrayList<LiftError>();
                String errCode = "128";
                String datetime = "2021-11-18 21:21:01";

                lift_errors.add(new LiftError(Integer.parseInt(errCode), datetime));
                errorResult = new ErrorResult(Integer.parseInt(lift_id), lift_errors);

                if (networkCallback == true) {
                    apiController.setRetrofitInit();
                    apiController.ErrorPost(errorResult);
                    apiController return값이 200 인 경우 화면전환
                } */
                Log.d("Test", "JsonData : " +Data.toString());

                Intent intent2 = new Intent(context, WriteReportActivity.class);
                intent2.putExtra("lift_id", liftId);
                context.startActivity(intent2);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {

                JSONObject sendObj = new JSONObject();
                sendObj.put("cmd", (byte) 0x21);
                sendObj.put("length", (byte) 0x06);
                sendObj.put("data", null);

                ThreadSendAndRecieve sarThread = new ThreadSendAndRecieve(sendObj);
                Thread thread = new Thread(sarThread);

                thread.start();
                try {
                    thread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONObject Data = sarThread.getResult();
                Log.d("Test",Data.toString());

                cmg.disableWifi();
                Log.d("Test", Data.toString());


                Intent intent2 = new Intent(context, WriteReportActivity.class);
                intent2.putExtra("lift_id", liftId);
                context.startActivity(intent2);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void OnCheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //사용자가 권한 요청을 명시적으로 거부한 경우 - true 처음 보거나 다시 묻지 않음 선택한 경우 false 반환
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "앱 실행을 위해서는 권한을 설정해야 합니다", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "앱 실행을 위한 권한이 설정 되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "앱 실행을 위한 권한이 취소 되었습니다", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}