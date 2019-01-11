package com.wenjian.commonskill.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Description: UserDao
 * Date: 2018/7/19
 *
 * @author jian.wen@ubtrobot.com
 */
@Dao
public interface UserDao extends BaseDao<User> {

    @Query("SELECT * FROM user")
    List<User> loadAll();

    @Query("SELECT * FROM user WHERE id=:userId")
    User findById(int userId);
}
