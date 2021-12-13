package com.example.elevator.api.roomdb;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
interface LiftDao {

    @Insert
    void insert(Lift memo);

    @Update
    void update(Lift memo);

    @Delete
    void delete(Lift memo);

    // api에서 받아온 모든 데이터 받아옴
    @Query("SELECT * FROM Lift")
    LiveData<List<Lift>> getAll(); //LiveData

    @Query("DELETE FROM Lift")
    void deleteAll();
}
