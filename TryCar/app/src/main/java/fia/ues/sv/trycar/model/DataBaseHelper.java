package fia.ues.sv.trycar.model;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by New on 14/6/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String BASE_DATOS = "trycar.s3db";
    private static final int VERSION = 1;

    public DataBaseHelper(Context context) {
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
    }
}
