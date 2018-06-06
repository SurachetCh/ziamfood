package revenue_express.ziamfood.model;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by surachet on 1/12/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String databaseName = "orderlist.sqlite";
    private static final int databaseVersion = 1;

    public DbHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS order_list(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, id_menu VARCHAR,title VARCHAR,price DOUBLE,amount INTEGER,addition VARCHAR,note VARCHAR);");
            Log.d("CREATE TABLE","Create Table Successfully.");
        } catch (SQLException e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //Delete Query
    public void removeFav(int id) {
        SQLiteDatabase.openDatabase("orderlist", null, Context.MODE_PRIVATE);
        String countQuery = "DELETE FROM " + "order_list" + " where " + "id" + "= " + id ;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(countQuery);
    }
    public static void deleteAll(SQLiteDatabase db)
    {
        //SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        //db.execSQL("delete * from"+ TABLE_NAME);
        SQLiteDatabase.openDatabase("orderlist", null, Context.MODE_PRIVATE);
        db.execSQL("delete from "+ "order_list");
        db.close();
    }

}












