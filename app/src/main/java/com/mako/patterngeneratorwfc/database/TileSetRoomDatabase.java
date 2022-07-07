package com.mako.patterngeneratorwfc.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mako.patterngeneratorwfc.TileSet;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TileSet.class}, version = 1, exportSchema = false)
public abstract class TileSetRoomDatabase extends RoomDatabase {

    public abstract TileSetDao tileSetDao();

    private static volatile TileSetRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                TileSetDao dao = INSTANCE.tileSetDao();
                dao.deleteAll();
                TileSet tileSet = new TileSet("new Tile set(1)", new int[][]{{1,2},{3,4},{5,6}}, new ArrayList<String>(){{
                    add("Q");
                    add("W");
                    add("E");
                    add("R");
                }});

                dao.insert(tileSet);

                tileSet = new TileSet("new Tile set(2)", new int[][]{{1,2},{3,4},{5,6}}, new ArrayList<String>(){{
                    add("Q");
                    add("W");
                    add("E");
                    add("R");
                }});

                dao.insert(tileSet);
            });
        }
    };

    static TileSetRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (TileSetRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TileSetRoomDatabase.class, "tileset_database")
                            .enableMultiInstanceInvalidation()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
