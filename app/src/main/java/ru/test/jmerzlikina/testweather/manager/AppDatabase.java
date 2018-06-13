package ru.test.jmerzlikina.testweather.manager;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.test.jmerzlikina.testweather.db.Location;
import ru.test.jmerzlikina.testweather.db.LocationDao;
import ru.test.jmerzlikina.testweather.db.SelectedLocation;
import ru.test.jmerzlikina.testweather.db.SelectedLocationDao;

@Database(entities = {Location.class, SelectedLocation.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
    public abstract SelectedLocationDao selectedLocationDao();
}