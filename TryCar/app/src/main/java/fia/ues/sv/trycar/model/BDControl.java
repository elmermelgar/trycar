package fia.ues.sv.trycar.model;

import android.content.ContentValues;
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
    private DataBaseHelper DBHelper;
    private SQLiteDatabase db;


    public BDControl(Context ctx) {
        this.context = ctx;
        DBHelper = new DataBaseHelper(context);
    }

    public void abrir() throws SQLException {
        db = DBHelper.getWritableDatabase();
    }

    public void cerrar() {
        DBHelper.close();
    }

    public String[] consultarEstadistica(Context ctx, String indicador){
        String[] respuesta=new String[]{};
        Cursor cursor = db.rawQuery("Select avg("+indicador+") from MONITOREO",new String[]{});
        if(cursor.moveToFirst()){
            respuesta[0]=cursor.getString(0);
        }
        cursor.close();
        cursor=db.rawQuery("Select min("+indicador+") from MONITOREO",new String[]{});
        if(cursor.moveToFirst()){
            respuesta[1]=cursor.getString(0);
        }
        cursor.close();
        cursor=db.rawQuery("Select max("+indicador+") from MONITOREO",new String[]{});
        if(cursor.moveToFirst()){
            respuesta[2]=cursor.getString(0);
        }
        return respuesta;
    }

    public boolean insertar(Monitoreo monitoreo){


        long contador=0;
        ContentValues moni = new ContentValues();
        moni.put("RPM" , monitoreo.getRpm());
        moni.put("SPEED" , monitoreo.getSpeed());
        moni.put("OIL_TEMP",monitoreo.getTempOil());
        moni.put("AMBI_TEMP", monitoreo.getTempAmb());
        moni.put("REFRI_TEMP" , monitoreo.getTempRefri());
        moni.put("LOAD_ENGINE" , monitoreo.getEngine());
        moni.put("LEVEL_FUEL" , monitoreo.getLevelFuel());
        moni.put("RATE_FUEL" , monitoreo.getPerFuel());
        moni.put("FECHA" , monitoreo.getFecha().toString());
        /*moni.put("LATITUD" , monitoreo.getLatitud());
        moni.put("LONGITUD" , monitoreo.getLongitud());
        moni.put("ALTITUD" , monitoreo.getAltitud());*/
        contador=db.insert("MONITOREO" , null, moni);
        if(contador==-1 || contador==0)
        {
            return false;

        }
        else {
            contador++;
            return true;
        }



    }

}




