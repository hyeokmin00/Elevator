package com.example.elevator.ui.report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.elevator.R;
import com.example.elevator.api.APIController;
import com.example.elevator.api.model.ReportList;

public class WriteReportActivity extends AppCompatActivity {
    //activity_writereport
    APIController apiController = new APIController();
    EditText edContent;
    TextView tvDate;
    AppCompatButton appCompatButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_report);

        Intent intent = getIntent();

        String liftId = intent.getStringExtra("lift_id");
        edContent = findViewById(R.id.write_ed_content);
        tvDate = findViewById(R.id.write_tv_date);
        appCompatButton = findViewById(R.id.write_btn_send);

        Log.d("Test", "WriteReportActivity - liftId : " + liftId);


        // send Btn 클릭 시 전송됨 - 인터넷 연결 시 api 사용해 전송, 아닐 시 sharedPreferences에 저장
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  ReportList reportList = new ReportList(Integer.parseInt(liftId),  edContent.getText().toString(),tvDate.getText().toString());
                ReportList reportList = new ReportList(Integer.parseInt(liftId),  edContent.getText().toString(),tvDate.getText().toString());

                Log.d("Test", "WriteReportActivity - checkinglist : " + reportList.getLift_id());
                Log.d("Test", "WriteReportActivity - checkinglist : " + reportList.getContent());
                Log.d("Test", "WriteReportActivity - checkinglist : " + reportList.getReport_date());

                apiController.WriteRepoLift(reportList);

                finish();
            }
        });
    }
}
