package com.example.elevator.api.roomdb;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Lift.class}, version = 1, exportSchema = false)
public abstract class LiftDB extends RoomDatabase {

    public abstract LiftDao liftDao();
    private static volatile LiftDB INSTANCE=null;


    // public abstract LiftDao liftDao();

    //싱글톤 패턴 사용
    public static LiftDB getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (LiftDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LiftDB.class, "liftdb.db")
                            //기본 데이터를 버리고 다음 버전으로 넘어감
                   //         .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    public static void destroyInstance() {
        INSTANCE = null;
    }

}
