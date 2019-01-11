package com.wenjian.commonskill.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.wenjian.commonskill.AppConfigure;

/**
 * Description: AppDatabase
 * Date: 2018/7/19
 *
 * @author jian.wen@ubtrobot.com
 */
@Database(entities = {User.class},version = 1)
public abstract class AppDatabase extends RoomDatabase{

    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;

    private static final String DB_NAME = "app.db";

    private static final Callback CALLBACK = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                            .addCallback(CALLBACK)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
