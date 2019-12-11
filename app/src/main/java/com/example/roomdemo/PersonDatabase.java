package com.example.roomdemo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Person.class},version = 2,exportSchema = false)
public abstract class PersonDatabase extends RoomDatabase {
    private static PersonDatabase INSTANCE;
    static synchronized PersonDatabase getDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    PersonDatabase.class,"person_database")
//                    .addMigrations(MIGRATION_1_2)//版本迁移，需要添加一个参数
                    .build();
        }
        return INSTANCE;
    }

    public abstract PersonDao getPersonDao();

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {//偏底层，需要手写SQL语句
            database.execSQL("ALTER TABLE Person ADD COLUMN identity INTEGER NOT NULL DEFAULT 1");
        }
    };
}
