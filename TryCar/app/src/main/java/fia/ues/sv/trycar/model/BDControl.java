package fia.ues.sv.trycar.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;


import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;



public class BDControl {

    private final Context context;
    private DataBaseHelper DBHelper;
    private SQLiteDatabase db;
    private ArrayList<ArrayList> longLat;

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

    public String[] consultarEstadistica(Context ctx, String indicador) {
        String[] respuesta = new String[]{"", "", ""};
        Log.v("indicador", indicador);
        Cursor cursor = db.rawQuery("Select min(" + indicador + ") from MONITOREO", new String[]{});
        if (cursor.moveToFirst()) {
            respuesta[0] = cursor.getString(0);
        }
        cursor.close();
        cursor = db.rawQuery("Select max(" + indicador + ") from MONITOREO", new String[]{});
        if (cursor.moveToFirst()) {
            respuesta[1] = cursor.getString(0);
        }
        cursor.close();
        cursor = db.rawQuery("Select avg(" + indicador + ") from MONITOREO", new String[]{});
        if (cursor.moveToFirst()) {
            respuesta[2] = cursor.getString(0);
        }
        cursor.close();
        return respuesta;
    }

    public String[] consultarDatosUsuario() {
        String[] respuesta = new String[]{"", ""};
        Cursor cursor = db.rawQuery("Select * from USUARIO", new String[]{});
        if (cursor.moveToFirst()) {
            respuesta[0] = cursor.getString(0);
            respuesta[1] = cursor.getString(1);
        }
        cursor.close();
        return respuesta;
    }

    public String guardarDatosUsuario(String nombre, String email) {
        String respuesta = "No se pudo guardar los datos";
        long contador = 0;
        ContentValues cv = new ContentValues();
        cv.put("USERNAME", nombre);
        cv.put("EMAIL", email);
        Cursor cursor = db.rawQuery("Select * from USUARIO", new String[]{});
        if (cursor.moveToFirst()) {
            String username = cursor.getString(0);
            cursor.close();
            contador = db.update("USUARIO", cv, "USERNAME = ?", new String[]{username});
        } else {
            cursor.close();
            contador = db.insert("USUARIO", null, cv);
        }
        if (contador == 1) {
            respuesta = "Datos guardados";
        }
        return respuesta;
    }

    public boolean insertar(Monitoreo monitoreo) {


        long contador = 0;
        ContentValues moni = new ContentValues();
        moni.put("RPM", monitoreo.getRpm());
        moni.put("SPEED", monitoreo.getSpeed());
        moni.put("OIL_TEMP", monitoreo.getPosAcel());
        moni.put("AMBI_TEMP", monitoreo.getTempAir());
        moni.put("REFRI_TEMP", monitoreo.getTempRefri());
        moni.put("LOAD_ENGINE", monitoreo.getEngine());
        moni.put("LEVEL_FUEL", monitoreo.getLevelFuel());
        moni.put("RATE_FUEL", monitoreo.getStar());
        moni.put("FECHA", monitoreo.getFecha().toString());
        moni.put("LATITUD" , monitoreo.getLatitud());
        moni.put("LONGITUD" , monitoreo.getLongitud());
        moni.put("ALTITUD" , monitoreo.getAltitud());
        contador = db.insert("MONITOREO", null, moni);
        if (contador == -1 || contador == 0) {
            return false;

        } else {
            contador++;
            return true;
        }

    }

    public String getEmailUser() {
        String email = null;
        Cursor cursor = db.rawQuery("Select EMAIL from USUARIO", null);
        if (cursor.moveToFirst()) {
            email = cursor.getString(0);
        }
        cursor.close();

        return email;
    }


    public String getNameUser() {
        String username = null;
        Cursor cursor = db.rawQuery("Select USERNAME from USUARIO", null);
        if (cursor.moveToFirst()) {
            username = cursor.getString(0);
        }
        cursor.close();

        return username;
    }


    public boolean sendMail(String nombreRemitente, String direccionRemite, String direccionDestino, String subject, String contenido) {
        final String miCorreo = "trycar1000@gmail.com";
        final String miContraseña = "trycar123";
        final String servidorSMTP = "smtp.gmail.com";
        final String puertoEnvio = "465";

        boolean enviado = false;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.user", miCorreo);
        props.put("mail.smtp.host", servidorSMTP);
        props.put("mail.smtp.port", puertoEnvio);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", puertoEnvio);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");


        try {

            Session mailSession = Session.getInstance(props, new Authenticator() {

                private PasswordAuthentication auth;

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    auth = new PasswordAuthentication(miCorreo, miContraseña);
                    return auth;
                }
            });

            Transport transport = mailSession.getTransport();
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(direccionRemite, nombreRemitente));
            message.setSender(new InternetAddress(direccionRemite, nombreRemitente));
            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(contenido);
            MimeMultipart _multipart = new MimeMultipart();
            _multipart.addBodyPart(messageBodyPart);
            message.setContent(_multipart);
            //message.setContent(contenido, "text/html");

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(direccionDestino));
            transport.connect();
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            enviado = true;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BDControl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(BDControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enviado;
    }

    public String[] consultarMonitoreo(Context ctx){
        abrir();
        Cursor cursor = db.rawQuery("select * from MONITOREO", new String[]{});
        if(!cursor.moveToFirst()){
            String msg="No hay registros";
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
            return null;
        }
        else{
            String[] resultado=new String[cursor.getCount()*9];
            int indice=0;
            while(!cursor.isAfterLast()) {
                //ACA LO QUE QUERRAS HACER CON CADAD REGISTRO
                for(int i=1;i<10;i++){
                    resultado[indice]=cursor.getString(i);
                    indice++;
                }
                cursor.moveToNext();
            }
            cursor.close();
            cerrar();
            return resultado;
        }
    }

    public ArrayList<ArrayList> getLongLat() {
        ArrayList<ArrayList> allData = new ArrayList<ArrayList>();
        Cursor cursor = db.rawQuery("select * from Monitoreo where latitud  NOT NULL and longitud NOT NULL and latitud != '0' and longitud != ''  order by ID_monitoreo  DESC", new String[]{});
        // Obtenemos los índices de las columnas
        int LATITUD = cursor.getColumnIndex("LATITUD");
        int LONGITUD = cursor.getColumnIndex("LONGITUD");
        int FECHA = cursor.getColumnIndex("FECHA");
        int ID_MONITOREO = cursor.getColumnIndex("ID_MONITOREO");
        int RPM = cursor.getColumnIndex("RPM");
        int OIL_TEMP = cursor.getColumnIndex("OIL_TEMP");
        int AMBI_TEMP = cursor.getColumnIndex("AMBI_TEMP");
        int REFRI_TEMP = cursor.getColumnIndex("REFRI_TEMP");
        int LEVEL_FUEL = cursor.getColumnIndex("LEVEL_FUEL");
        int RATE_FUEL = cursor.getColumnIndex("RATE_FUEL");
        int LOAD_ENGINE = cursor.getColumnIndex("LOAD_ENGINE");

//Recorremos el cursor
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            ArrayList<String> fila = new ArrayList<String>();

            String lat = cursor.getString(LATITUD);
            String lon = cursor.getString(LONGITUD);
            String FECHA1 = cursor.getString(FECHA);
            String ID_MONITOREO1 = cursor.getString(ID_MONITOREO);
            String OIL_TEMP1 = cursor.getString(OIL_TEMP);
            String AMBI_TEMP1 = cursor.getString(AMBI_TEMP);
            String REFRI_TEMP1 = cursor.getString(REFRI_TEMP);
            String LEVEL_FUEL1 = cursor.getString(LEVEL_FUEL);
            String RATE_FUEL1 = cursor.getString(RATE_FUEL);
            String LOAD_ENGINE1 = cursor.getString(LOAD_ENGINE);
            String RPM1 = cursor.getString(RPM);

            fila.add(lat);
            fila.add(lon);
            fila.add("# "+ ID_MONITOREO1);
            fila.add(
                    "Fecha "+ FECHA1+
                            "\nTemp. Aceite "+ OIL_TEMP1+
                            "\nTemp. Ambiente "+ AMBI_TEMP1+
                            "\nTemp. Refrigetante "+ REFRI_TEMP1+
                            "\nNivel Combustible "+ LEVEL_FUEL1 +
                            "\nTasa Combustible "+ RATE_FUEL1+ "%"+
                            "\nCarga de Combustible "+ LOAD_ENGINE1+
                            "\nRPM1 "+ RPM1
            );

            allData.add(fila);

        }

        cursor.close();

        return allData;
    }
}
