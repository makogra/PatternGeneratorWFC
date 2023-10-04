package com.mako.patterngeneratorwfc.database;

import android.content.Context;
import android.util.Log;

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

    private static final String TAG = "TileSetRoomDatabase";
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                Log.d(TAG, "onCreate: Database");
                TileSetDao dao = INSTANCE.tileSetDao();
                dao.deleteAll();
                TileSet tileSet = new TileSet("new Tile set(1)", new int[][]{{1,2,3},{2,3,1},{3,1,1}}, new ArrayList<String>(){{
                    add("_");
                    add("G");
                    add("C");
                    add("S");
                    add("M");
                }});

                db.beginTransaction();

                dao.insert(tileSet);

                db.endTransaction();

                tileSet = new TileSet("new Tile set(2)", new int[][]{{1,1,1,1},{2,2,2,2},{3,3,3,3},{4,4,4,4}}, new ArrayList<String>(){{
                    add("_");
                    add("G");
                    add("C");
                    add("S");
                    add("M");
                }});

                dao.insert(tileSet);
            });
        }
    };

    static TileSetRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (TileSetRoomDatabase.class) {
                if (INSTANCE == null) {
                    Log.d(TAG, "getDatabase: new");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TileSetRoomDatabase.class, "tileset_database")
                            .enableMultiInstanceInvalidation()
                            //.addCallback(sRoomDatabaseCallback)
                            .build();
                    new Thread(() -> INSTANCE.runInTransaction(() -> {
                        Log.d(TAG, "getDatabase: lambda");
                        INSTANCE.tileSetDao().insert( new TileSet(
                                "new Tile set(laske)",
                                new int[][]{
                                        {1, 1, 1, 1, 1, 1},
                                        {1, 2, 2, 2, 2, 1},
                                        {1, 2, 3, 3, 2, 1},
                                        {1, 2, 3, 3, 2, 1},
                                        {1, 2, 2, 2, 2, 1},
                                        {1, 1, 1, 1, 1, 1}
                                },
                                new ArrayList<String>(){{
                                    add("_");
                                    add("G");
                                    add("C");
                                    add("S");
                                    add("M");
                                }}));
                        INSTANCE.tileSetDao().insert(new TileSet(
                                "new Tile set(standard)",
                                new int[][]{
                                        {1,1,1,1,2,3,3,3},
                                        {1,1,1,2,2,3,3,3},
                                        {1,1,1,2,3,3,3,3},
                                        {1,1,1,2,3,3,3,3},
                                        {1,1,1,2,3,3,3,3}
                                },
                                new ArrayList<String>(){{
                                    add("_");
                                    add("G");
                                    add("C");
                                    add("S");
                                    add("M");
                                }}));
                    })).start();
                } else
                    Log.d(TAG, "getDatabase: old");
            }
        }
        return INSTANCE;
    }

}
