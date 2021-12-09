package com.example.elevator.ui.main;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import com.example.elevator.R;
import com.example.elevator.api.APIActivity;
import com.example.elevator.api.model.LiftInfo;


public class MainActivity extends AppCompatActivity {

    APIActivity apiActivity = new APIActivity();

    RecyclerView recyclerView;
    private ArrayList<LiftInfo> liftInfoArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);

        List<String> data = new ArrayList<String>();
        data.add("엘레베이터 1");
        data.add("엘레베이터 2");
        data.add("엘레베이터 3");


        //retrofitInit - Retrofit 객체 생성
        apiActivity.setRetrofitInit();


        //LiftList 받아옴
        RecyclerView recycler = findViewById(R.id.recyclerview);
        apiActivity.LiftList(this, recycler,recyclerView);



/*
        liftRecyAdapter = new TotalRecyAdapter(result, MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(totalRecyAdapter);

        recyclerView.setAdapter(adapter);

       // ((class)class.mContext).open메서드();


        adapter.notifyDataSetChanged();*/

///요 코딩은 list를 클릭 했을경우 실행되는 코드
     //   recyclerView.setOnItemClickListener((AdapterView.OnItemClickListener) this);


    }
    /// 리스트 클릭하면 Shared 로 화면 전환하게 하는거 ......



}
