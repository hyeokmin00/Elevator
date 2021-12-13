package com.example.elevator.api.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;



@Database(entities = {Lift.class}, version = 1)
public abstract class LiftDB extends RoomDatabase {

    public abstract LiftDao liftDao();
    private static volatile LiftDB INSTANCE;

    //싱글톤 패턴 사용
    public static LiftDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    LiftDB.class, "liftdb.db").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
