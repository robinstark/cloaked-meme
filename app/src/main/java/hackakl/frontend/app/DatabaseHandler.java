package hackakl.frontend.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adam on 5/24/2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private final String BS_TABLE = "bus_stop";
    private final String BS_NAME = "bs_friendly_name";
    private final String BS_ID = "bs_id";
    private final String RT_TABLE = "route";
    private final String RT_ID = "rt_id";
    private final String RT_NAME = "rt_short_name";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_BS_TABLE = "CREATE TABLE " + BS_TABLE + "(" + BS_ID + " TEXT PRIMARY KEY, " +
                BS_NAME + " TEXT)";
        String CREATE_RT_TABLE = "CREATE TABLE " + RT_TABLE + "(" + RT_ID + " TEXT PRIMARY KEY, " +
                RT_NAME + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_BS_TABLE);
        sqLiteDatabase.execSQL(CREATE_RT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public Map<String, String> getRoutes()    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + BS_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        Map<String, String> routes = new HashMap<>();
        if ( cursor.moveToFirst())  {
            do {
                routes.put(cursor.getString(0), cursor.getString(1));
            }   while (cursor.moveToNext());
        }
        return routes;
    }

    public Map<String, String> getBusStops()    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + BS_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        Map<String, String> busStops = new HashMap<>();
        if ( cursor.moveToFirst())  {
            do {
                busStops.put(cursor.getString(0), cursor.getString(1));
            }   while (cursor.moveToNext());
        }
        return busStops;
    }

    public void addRoute(String id, String shortName) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(RT_NAME, shortName);
        values.put(RT_ID, id);

        db.insert(RT_TABLE, null, values);
        db.close();
    }

    public void addBusStop(String id, String friendlyName) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(BS_NAME, friendlyName);
        values.put(BS_ID, id);

        db.insert(BS_TABLE, null, values);
        db.close();
    }
}
