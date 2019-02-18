package com.gzq.lib_core.http.cache;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RoomCacheDao {
    /**
     * 插入缓存，如果有冲突直接替换
     *
     * @param cacheEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCache(RoomCacheEntity cacheEntity);

    /**
     * 查询所有缓存
     *
     * @return
     */
    @Query("SELECT * FROM RoomCache")
    List<RoomCacheEntity> getAllCache();

    /**
     * 根据key查询
     *
     * @param key
     * @return
     */
    @Query("SELECT * FROM RoomCache WHERE `key`=:key")
    RoomCacheEntity queryByKey(String key);

    /**
     * 根据key删除缓存
     *
     * @param key
     */
    @Query("DELETE FROM RoomCache WHERE `key`=:key")
    void deleteByKey(String key);

}
