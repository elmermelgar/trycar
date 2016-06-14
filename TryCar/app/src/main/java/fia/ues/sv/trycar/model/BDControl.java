package fia.ues.sv.trycar.model;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by David-PC on 13/6/2016.
 */
public class BDControl {


    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;


    public BDControl(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }




    private static class DatabaseHelper extends SQLiteOpenHelper {
        //private static final String BASE_DATOS = "tarea1.s3db" ;
        private static final String BASE_DATOS = "trycar.s3db";
        private static final int VERSION = 1;


        public DatabaseHelper(Context context) {
            super(context, BASE_DATOS, null, VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            try {



                db.execSQL("CREATE TABLE USUARIO(USERNAME VARCHAR(20) PRIMARY KEY NOT NULL,EMAIL VARCHAR(100));");
                db.execSQL("CREATE TABLE MONITOREO(ID_MONITOREO INTEGER NOT NULL AUTOINCREMENT PRIMARY KEY,RPM NUMERIC NULL,SPEED NUMERIC NULL,OIL_TEMP NUMERIC NULL,AMBI_TEMP NUMERIC NULL,REFRI_TEMP NUMERIC NULL,LOAD_ENGINE NUMERIC NULL,LEVEL_FUEL NUMERIC NULL,RATE_FUEL NUMERIC NULL,FECHA DATE NOT NULL, LATITUD NUMERIC NULL, LONGITUD NUMERIC NULL, ALTITUD NUMERIC NULL);");


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }


    }
    public void abrir() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return;
    }


    public void cerrar() {
        DBHelper.close();
    }






    }




