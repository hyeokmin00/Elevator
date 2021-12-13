package com.example.elevator.api.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LiftDao {

    //Lift 형식으로 된 Data를 저장함.
    //APIController의 Liftlist를 통해 받아온 데이터를 Lift 형태로 바꾼 후 저장
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Lift lift);

    @Update
    void update(Lift lift);

    @Delete
    void delete(Lift lift);

    // api에서 받아온 모든 데이터 받아옴
    @Query("SELECT * FROM Lift")
    LiveData<List<Lift>> getAll(); //LiveData

    @Query("DELETE FROM Lift")
    void deleteAll();

}
