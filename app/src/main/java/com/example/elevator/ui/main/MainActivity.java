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

    //api로 받아온 데이터 RoodDB 저장 된 데이터 표현
    //리스트 중 아이템 클릭 시
    //todo 엘리베이터와의 wifi 연결 후 상태정보 수신 - 다른 액티비티 또는 다이얼로그?
    //엘리베이터와 wifi 연결  끊은 후 상태 api 서버에 전송 - id : lift_id
    //점검 내용 입력할 수 있는 화면으로 전환

    private LiftDB liftDb;
    Context context;
    RecyclerView recycler;
    List<Lift> liftList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recyclerview);
        //todo RoodDB ListInfo 데이터 받아와 adapter에 값 넘겨주기
        //db 객체 초기화
        liftDb = LiftDB.getInstance(this);
        //Main Thread에서 DB 접근 불가 -> Thread 이용
        context = this;

        class InsertRunnable implements Runnable{
            @Override
            public void run() {
                liftList = liftDb.getInstance(context).liftDao().getAll();
                LiftRecyAdapter liftRecyAdapter = new LiftRecyAdapter(liftList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                recycler.setLayoutManager(linearLayoutManager);
                recycler.setAdapter(liftRecyAdapter);
            }
        }
        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();


        // Recycler view item click event 처리

    }

}
