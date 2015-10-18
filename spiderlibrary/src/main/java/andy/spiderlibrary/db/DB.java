package andy.spiderlibrary.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EmptyStackException;

import andy.spiderlibrary.utils.Log;


/**
 * Created by andyli on 2015/8/1.
 */
public class DB {

    public  String DATABASE_NAME = "andyDB";
    public  int DATAVERSION = 1;

    DBHelper dbHelper;

    public interface OnDBChangeListener{
         void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
         void onCreate(SQLiteDatabase db);
    }
    public  class DBHelper extends SQLiteOpenHelper {
        OnDBChangeListener onDBChangeListener;
        public DBHelper(Context context , OnDBChangeListener onDBChangeListener) {
            super(context, DATABASE_NAME, null, DATAVERSION);
             this.onDBChangeListener = onDBChangeListener;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if(this.onDBChangeListener!=null)
                this.onDBChangeListener.onUpgrade(db, oldVersion, newVersion);
            onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if(this.onDBChangeListener!=null)
                this.onDBChangeListener.onCreate(db);
        }
    }

    Cursor cursor;
    public DB(Context context ,String dbName , int dbVersion ,OnDBChangeListener onDBChangeListener ){
        if(TextUtils.isEmpty(DATABASE_NAME)){
            throw new NullPointerException("db name is not empty");
        }

        if(onDBChangeListener == null){
            throw new EmptyStackException();
        }

        this.DATABASE_NAME = dbName;
        this.DATAVERSION = dbVersion;
        dbHelper = new DBHelper(context ,onDBChangeListener);
    }
    public  String dropTable(Class<?> cls){
        try {
            String sql = "";
           String DROP_TABLE = "DROP TABLE IF EXISTS ";
            String tableName = "";
            Table table = cls.getAnnotation(Table.class);
            tableName = table.tableName();

            sql += DROP_TABLE +tableName;
            return sql;
        }catch (Exception e){
            Log.exception(e);
        }

        return null;
    }

    public  String createTable(Class<?> cls){
        try {
            String sql = "";
            String fieldSql = "";
            String tableName = "";
            Field[] fields = cls.getDeclaredFields();
            Table table = cls.getAnnotation(Table.class);
            tableName = table.tableName();
            Arrays.sort(fields, new Comparator<Field>() {
                @Override
                public int compare(Field f1, Field f2) {
                    if (f1.isAnnotationPresent(Column.class)
                            && f2.isAnnotationPresent(Column.class)) {
                        Column co1 = f1.getAnnotation(Column.class);
                        Column co2 = f2.getAnnotation(Column.class);
                        return co1.index() - co2.index();
                    } else {
                        return 0;
                    }

                }
            });
            for(int i = 0;i<fields.length;i++){
                Field a = fields[i];
                if(a.isAnnotationPresent(Column.class)){
                    Column col = a.getAnnotation(Column.class);
                    fieldSql+= (i==0?"":" ,")+col.name()+" "+col.type();
                }
            }
            sql += " create table "+tableName+" ( "+fieldSql+" )";
            return sql;
        }catch (Exception e){
            Log.exception(e);
        }

        return null;
    }
    public  void checkDBVersion(){

    }

    public SQLiteDatabase getWriteDateBase(){
            return dbHelper.getWritableDatabase();
    }
    public SQLiteDatabase getReadableDatabase(){
        return dbHelper.getReadableDatabase();
    }

    public void closeCursor(Cursor cursor){
        try {
            if (cursor != null) {
                cursor.close();
            }
        }catch (Exception e){
            Log.exception(e);
        }
    }
}
