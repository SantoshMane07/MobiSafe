package com.example.mobisafe;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = ContactModel.class,exportSchema = false,version = 1)
public abstract class DataBaseHelper extends RoomDatabase {
    private static final String DB_NAME = "contactsdb";
    private static DataBaseHelper instance;

    public static synchronized DataBaseHelper getDB(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context,DataBaseHelper.class,DB_NAME )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().
                    build();
        }
        return instance;
    }
    public abstract ContactsDao contactsDao();
}
