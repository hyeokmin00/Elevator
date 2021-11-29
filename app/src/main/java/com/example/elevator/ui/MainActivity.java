package com.example.elevator.ui;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.view.View.OnClickListener;

import com.example.elevator.R;
import com.example.elevator.ui.SharedExample;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView list;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView)findViewById(R.id.list);

        List<String> data = new ArrayList<>();
        /* data.add("엘레베이터 1");
        data.add("엘레베이터 2");
        data.add("엘레베이터 3"); */

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);

        list.setAdapter(adapter);

       // ((MainActivity) mContext).open메서드();





        adapter.notifyDataSetChanged();

///요 코딩은 list를 클릭 했을경우 실행되는 코드
        list.setOnItemClickListener((AdapterView.OnItemClickListener) this);


    }
    /// 리스트 클릭하면 Shared 로 화면 전환하게 하는거 ......


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(MainActivity.this, SharedExample.class);
        startActivity(intent);
    }



}
