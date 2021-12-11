package com.example.elevator.ui.report;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.elevator.R;
import com.example.elevator.api.APIController;
import com.example.elevator.api.model.ReportList;

import retrofit2.Retrofit;

public class WriteReportActivity extends AppCompatActivity {
    //activity_writereport

    AppCompatButton appCompatButton;
    Context context;

    String liftId;
    EditText edContent = findViewById(R.id.write_ed_content);
    TextView tvDate = findViewById(R.id.write_tv_date);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_report);

        Intent intent = getIntent();
        String liftId = intent.getStringExtra("lift_id");

        Log.d("Test", "WriteReportActivity - liftId : " + liftId);


        // todo send Btn 클릭 시 전송됨 - 인터넷 연결 시 api 사용해 전송, 아닐 시 sharedPreferences에 저장
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportList reportList = new ReportList(Integer.parseInt(liftId), edContent.getText().toString(), tvDate.getText().toString());
                isConnected(context, reportList);
                finish();
            }
        });
    }

    public static void SendReport(ReportList reportList) {

        APIController apiController = new APIController();
        apiController.setRetrofitInit();
        apiController.WriteRepoLift(reportList);

    }


    public static void isConnected(Context context, ReportList reportList) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        manager.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                // 네트워크를 사용할 준비가 되었을 때
                SendReport(reportList);
            }

            @Override
            public void onLost(@NonNull Network network) {
                // 네트워크가 끊겼을 때
                //sharedPreferences에 점검내용 저장


            }
        });
    }
}