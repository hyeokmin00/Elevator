package com.example.elevator.ui.main;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import com.example.elevator.R;
import com.example.elevator.api.APIActivity;
import com.example.elevator.api.model.Lift;
import com.example.elevator.ui.SharedExample;


public class MainActivity extends AppCompatActivity {

    APIActivity apiActivity = new APIActivity();

    RecyclerView recyclerView;
    private ArrayList<Lift> liftArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);

        List<String> data = new ArrayList<String>();
        data.add("엘레베이터 1");
        data.add("엘레베이터 2");
        data.add("엘레베이터 3");

        apiActivity.setRetrofitInit();
        apiActivity.LiftList();


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
