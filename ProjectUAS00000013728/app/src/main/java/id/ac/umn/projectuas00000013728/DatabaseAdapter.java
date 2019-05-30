package id.ac.umn.projectuas00000013728;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseAdapter extends SQLiteOpenHelper {
    public static String DB_PATH;
    public static String DATABASE_NAME;
    public SQLiteDatabase db;
    public Context context;

    private static final String TABLE_NAME = "users";
    public static final String COLUMN_USERNAME = "user_name";
    public static final String COLUMN_USERPASSWORD = "user_password";

    public DatabaseAdapter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);

        String packageName = context.getPackageName();
        DB_PATH = "/data/data/" + packageName + "/databases/";
        DATABASE_NAME = name;
        this.context = context;
        openDatabase();
    }

    public SQLiteDatabase openDatabase(){
        String path = DB_PATH + DATABASE_NAME;
        if(db==null) {
            createDatabase();
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        }
        return db;
    }

    public void createDatabase(){
        boolean dbExist = checkDatabase();
        if(!dbExist){
            this.getReadableDatabase();
            try{
                copyDatabase();
            } catch(IOException e){
                Log.e(this.getClass().toString(),"Copying error");
                throw new Error("Error copying database!");
            }
        }else{
            Log.i(this.getClass().toString(),"Database already exists!");
        }
    }
    private boolean checkDatabase(){
        String path = DB_PATH + DATABASE_NAME;
        File dbFile = new File(path);
        return dbFile.exists();
    }

    private void copyDatabase() throws IOException{
        InputStream externalDBStream = context.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + DATABASE_NAME;
        OutputStream localDBStream = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while((bytesRead=externalDBStream.read(buffer)) > 0){
            localDBStream.write(buffer,0,bytesRead);
        }
        localDBStream.flush();
        localDBStream.close();
        externalDBStream.close();
    }
    public synchronized void close(){
        if(db!=null){
            db.close();
        }
        super.close();
    }

    public Cursor getAllUsers(){
        Cursor cursor = db.query(TABLE_NAME,
                new String[] {COLUMN_USERNAME,COLUMN_USERPASSWORD},
                null,null,null,null,null);
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
