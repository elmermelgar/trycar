package fia.ues.sv.trycar.model;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
            db.execSQL("DROP TABLE IF EXISTS 'MONITOREO';");
            db.execSQL("CREATE TABLE USUARIO(USERNAME VARCHAR(20) PRIMARY KEY NOT NULL,EMAIL VARCHAR(100))");
            db.execSQL("CREATE TABLE MONITOREO(ID_MONITOREO INTEGER NOT NULL PRIMARY KEY,RPM NUMERIC NULL,SPEED NUMERIC NULL,OIL_TEMP NUMERIC NULL,AMBI_TEMP NUMERIC NULL,REFRI_TEMP NUMERIC NULL,LOAD_ENGINE NUMERIC NULL,LEVEL_FUEL NUMERIC NULL,RATE_FUEL NUMERIC NULL,FECHA DATE NOT NULL, LATITUD VARCHAR NULL, LONGITUD VARCHAR NULL, ALTITUD VARCHAR NULL)");

            db.execSQL("INSERT INTO 'MONITOREO' VALUES(1,111,123,34345,56756,4564,678,789,345,'2006-02-27','13.7167056','-89.2179743',NULL);");
            db.execSQL("INSERT INTO 'MONITOREO' VALUES(2,345,456,678,234,456,345,456,678,'2015-05-27','13.721960','-89.200759',NULL);");
            db.execSQL("INSERT INTO 'MONITOREO' VALUES(3,456456,345345,234,2342,234,2344,234234,234234,'2015-05-22','13.719375','-89.209621',NULL);");
            db.execSQL("INSERT INTO 'MONITOREO' VALUES(4,234,234,234,234,234,234,234,234234,'2015-05-17','13.713335','-89.203321',NULL);\n");
            db.execSQL("INSERT INTO 'MONITOREO' VALUES(5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,'2015-09-27',NULL,NULL,NULL);");
            db.execSQL("INSERT INTO 'MONITOREO' VALUES(6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,'2014-09-27','','',''");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
