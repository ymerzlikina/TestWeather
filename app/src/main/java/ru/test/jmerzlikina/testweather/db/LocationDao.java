package ru.test.jmerzlikina.testweather.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM location")
    Flowable<List<Location>> getAll();

    @Query("SELECT * FROM location WHERE id = :id")
    Location getById(long id);

    @Insert
    void insert(Location location);

    @Update
    void update(Location location);

    @Delete
    void delete(Location location);

}
