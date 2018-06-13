package ru.test.jmerzlikina.testweather.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface SelectedLocationDao {

    @Query("SELECT * FROM SelectedLocation")
    Flowable<List<SelectedLocation>> getAll();

    @Query("SELECT * FROM SelectedLocation")
    List<SelectedLocation> getAllElement();

    @Query("SELECT * FROM SelectedLocation WHERE id = :id")
    Location getById(long id);

    @Insert
    void insert(SelectedLocation selectedLocation);

    @Update
    void update(SelectedLocation selectedLocation);

    @Delete
    void delete(SelectedLocation selectedLocation);

}
