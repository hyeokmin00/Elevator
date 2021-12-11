package com.example.elevator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;

import com.example.elevator.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SharedExample extends AppCompatActivity {

    EditText et_save;
    String shared = "file";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_report);

        et_save = (EditText)findViewById(R.id.write_ed_content);

        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        String value = sharedPreferences.getString("content", "");
        et_save.setText(value);

        /* int netWork = 0;
        if (netWork == true ){
        ((class)class.mContext).open메서드();
        } */

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = et_save.getText().toString();
        editor.putString("yoon", value);
        editor.commit();
    }
}