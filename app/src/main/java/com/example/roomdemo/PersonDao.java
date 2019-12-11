package com.example.roomdemo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface PersonDao {
    @Insert
    void insertPerson(Person...people);

    @Delete
    void deletePerson(Person...people);

    @Update
    void updatePerson(Person...people);

    @Query("SELECT * FROM Person ORDER BY id DESC")
    LiveData<List<Person>> getAllPerson();

    @Query("SELECT * FROM Person WHERE ID =:id")
    Person queryPerson(int id);
}
