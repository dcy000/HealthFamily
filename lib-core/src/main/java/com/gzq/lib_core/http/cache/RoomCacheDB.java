package com.gzq.lib_core.http.cache;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {RoomCacheEntity.class}, version = 1, exportSchema = false)
@TypeConverters(HeadsConverter.class)
public abstract class RoomCacheDB extends RoomDatabase {
    public abstract RoomCacheDao roomCacheDao();
}
