package com.example.elevator.ui.main;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import com.example.elevator.R;
import com.example.elevator.api.APIController;
import com.example.elevator.api.model.LiftInfo;


public class MainActivity extends AppCompatActivity {

    //api로 받아온 데이터 sharedPreferences에 저장 된 데이터 표현
    //리스트 중 아이템 클릭 시
    //todo 엘리베이터와의 wifi 연결 후 상태정보 수신 - 다른 액티비티 또는 다이얼로그?
    //엘리베이터와 wifi 연결  끊은 후 상태 api 서버에 전송 - id : lift_id
    //점검 내용 입력할 수 있는 화면으로 전환


    APIController apiController = new APIController();
    RecyclerView recyclerView;
    private ArrayList<LiftInfo> liftInfoArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);

        //retrofitInit - Retrofit 객체 생성
        apiController.setRetrofitInit();
        //LiftList 받아옴
        RecyclerView recycler = findViewById(R.id.recyclerview);
        //현재 apiacontroller의 liftlist에서 바로 recycler뷰로 데이터 붙이게 되어있음
        //해당 내용 시나리오에 맞춰 변경 필요
        apiController.LiftList(this, recycler, recyclerView);


    }
}
