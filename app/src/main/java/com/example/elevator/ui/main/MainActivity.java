package com.example.elevator.ui.main;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import com.example.elevator.R;
import com.example.elevator.api.APIController;
import com.example.elevator.api.model.LiftInfo;
import com.example.elevator.api.roomdb.Lift;
import com.example.elevator.api.roomdb.LiftDB;
import com.example.elevator.ui.main.adapter.LiftRecyAdapter;


public class MainActivity extends AppCompatActivity {
    /* 승강기 목록 RecyclerView로 보여줌.
    아이템 클릭 시 SocketActivity에서 SSID와 PW 비교해 기기연결 후 ErrorList 반환받음
    해당 목록 lte 연결 후 ErrorPost를 통해 서버에 전송함.
    이때 다음 동작을 위해 Lift_id 같이 보내야함 */

    //api로 받아온 데이터 RoodDB 저장 된 데이터 표현
    //리스트 중 아이템 클릭 시
    //엘리베이터와 wifi 연결  끊은 후 상태 api 서버에 전송 - id : lift_id
    //점검 내용 입력할 수 있는 화면으로 전환

    private LiftDB liftDb;
    Context context;
    RecyclerView recycler;
    List<Lift> liftList;
    LinearLayoutManager linearLayoutManager;
    LiftRecyAdapter liftRecyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recyclerview);
        context = this;

        class GetAppRunnable implements Runnable {
            @Override
            public void run() {
                liftList = LiftDB.getInstance(context).getInstance(context).liftDao().getAll();
                liftRecyAdapter = new LiftRecyAdapter(context, liftList);
                linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recycler.setLayoutManager(linearLayoutManager);
                recycler.setAdapter(liftRecyAdapter);

            }
        }

        GetAppRunnable getAppRunnable = new GetAppRunnable();
        Thread t = new Thread(getAppRunnable);
        t.start();

    }
}
