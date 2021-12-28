package com.example.elevator.ui.report;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.elevator.R;
import com.example.elevator.api.APIController;
import com.example.elevator.api.model.ReportList;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Retrofit;

public class WriteReportActivity extends AppCompatActivity {
    /* liftId intent로 전달 받은 후 API Controller의 SendReport를 통해 서버로 전달하거나
    sharedPref에 임시 저장 */
    
    
    //activity_writereport

    AppCompatButton send;
    Context context = this;

    public String liftId;
    TextView tvLiftNum;
    EditText worker;
    EditText edContent;
    EditText edDate;
    EditText edWorker;

    //SharedPreferences 관련 설정
    ArrayAdapter<String> adapter;
    ArrayList<String> reportContents = new ArrayList<>();
    // ArrayList -> Json으로 변환
    static final String WORKER = "worker";
    static final String REPORT = "ReportContents";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_report);

        tvLiftNum = findViewById(R.id.write_tv_lift_id);
        send = findViewById(R.id.write_btn_send);
        worker = findViewById(R.id.write_ed_worker);
        edContent = findViewById(R.id.write_ed_content);
        edDate = findViewById(R.id.write_ed_date);
        edWorker = findViewById(R.id.write_ed_worker);




        Intent intent = getIntent();
        liftId = intent.getStringExtra("lift_id");
        tvLiftNum.setText("승강기 번호 : " + liftId);

        //날짜 기본 값 : 오늘 날짜
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        edDate.setText(sdf.format(dt));


        //저장된 sharpref 가져옴
        String worker = getWorkerItem(WORKER);
        edWorker.setText(worker);

        // 작성 중이던 보고서가 없는 경우
        if(getStringArrayPref(context,REPORT) !=null){
            ArrayList<String> list = getStringArrayPref(context, REPORT);
            if (list.size() >= 1) {
                liftId = list.get(0);
                Toast.makeText(this, "이전에 작성된 보고서입니다.", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "승강기 번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                tvLiftNum.setText("승강기 번호 : " + liftId);
                edContent.setText(list.get(1));
                edDate.setText(list.get(2));

            }
        }

        edContent.getText();
        // send Btn 클릭 시 전송됨 - 인터넷 연결 시 api 사용해 전송, 아닐 시 sharedPreferences에 저장
        // 해당 내용 isConnected에서 제어
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String report = edWorker.getText().toString() + " / " + edContent.getText().toString();
                ReportList reportList = new ReportList(Integer.parseInt(liftId), report, edDate.getText().toString());
                isConnected(context, reportList);
                finish();
            }
        });

    }


    public static void SendReport(Context context, ReportList reportList) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().remove(REPORT).commit();

        APIController apiController = new APIController();
        apiController.setRetrofitInit();
        apiController.WriteRepoLift(reportList);
    }


    public void isConnected(Context context, ReportList reportList) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        manager.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                // 네트워크를 사용할 준비가 되었을 때
                SendReport(context, reportList);
            }

            @Override
            public void onLost(@NonNull Network network) {
                // 네트워크가 끊겼을 때
                //sharedPreferences에 점검내용 저장
                setReportPref(liftId, edContent.getText().toString(), edDate.getText().toString());
            }
        });
    }

    //onDestroy 시 sharedPreference에 작업자 이름 저장.
    //todo 이름, 작업 내용, lift id, 날짜 저장

    public void setReportPref(String liftId, String content, String date) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(liftId);
        list.add(content);
        list.add(date);
        setStringArrayPref(context, REPORT, list);
        Log.d("Test", "Put json");
    }


    private void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    private ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }


    //작업자명 저장 sharedPref
    private void putWorkerItem(String key, String value) {
        Log.d("Test", "Put " + key + " (value : " + value + " ) to " + WORKER);
        SharedPreferences preferences = getSharedPreferences(WORKER, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getWorkerItem(String key) {
        Log.d("Test", "Get " + key + " from " + WORKER);
        return getSharedPreferences(WORKER, 0).getString(key, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //작업자명 저장
        putWorkerItem(WORKER, worker.getText().toString());
        // setReportPref(liftId.toString(), edContent.getText().toString(), edDate.getText().toString());

        Log.d("Test", "Put json");
    }
}