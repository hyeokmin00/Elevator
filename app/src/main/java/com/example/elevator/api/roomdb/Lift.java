package com.example.elevator.api.roomdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.net.InterfaceAddress;


//Entity 정의
@Entity(tableName = "Lift")
public class Lift {

    @PrimaryKey(autoGenerate = true)
    public int id ;

    @ColumnInfo(name="liftId")
    public String liftId;


    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="status")
    public String status;

    @ColumnInfo(name="addr")
    public String addr;

    @ColumnInfo(name="createAt")
    public String createAt;

    public Lift( String liftId, String name, String status,
                String addr, String createAt){
        this.liftId = liftId;
        this.name = name;
        this.status = status;
        this.addr = addr;
        this.createAt = createAt;
    }
    public int getId(){
        return id;
    }
    public String getLiftId(){return liftId; }
    public String getName(){
        return name;
    }
    public String getStatus(){
        return status;
    }
    public String getAddr(){
        return addr;
    }
    public String getCreateAt(){return createAt;}


    public int setId(){
        return id;
    }
    public String setLiftId(){
        return liftId;
    }
    public String setName(){
        return name;
    }
    public String setStatus(){
        return status;
    }
    public String setAddr(){
        return addr;
    }
    public String setCreate(){
        return createAt;
    }




}
