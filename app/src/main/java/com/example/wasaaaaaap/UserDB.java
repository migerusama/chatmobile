package com.example.wasaaaaaap;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.wasaaaaaap.dao.UserDao;
import com.example.wasaaaaaap.entity.Usuario;

@Database(entities = {Usuario.class}, version = 1)
public abstract class UserDB extends RoomDatabase {
    private static UserDB instance;

    public abstract UserDao userDao();

    public static synchronized UserDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), UserDB.class, "usuarios_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    //Callback para la room db
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //Se llama cuando se crea la db y to populate our data
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    // Async task class para realizar tareas en el fondo
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(UserDB instance) {
            UserDao dao = instance.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
