package com.wenjian.commonskill.room;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Description: BaseDao
 * Date: 2018/7/19
 *
 * @author jian.wen@ubtrobot.com
 */
public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T... ts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<T> list);

    @Delete
    void delete(T t);

    @Update
    void update(T t);

}
