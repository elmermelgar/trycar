package fia.ues.sv.trycar.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.UnsupportedEncodingException;



import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


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

    public String[] consultarEstadistica(Context ctx, String indicador) {
        String[] respuesta = new String[]{"","",""};
        Log.v("indicador",indicador);
        Cursor cursor = db.rawQuery("Select avg(" + indicador + ") from MONITOREO", new String[]{});
        if (cursor.moveToFirst()) {
            respuesta[0] = cursor.getString(0);
        }
        cursor.close();
        cursor = db.rawQuery("Select min(" + indicador + ") from MONITOREO", new String[]{});
        if (cursor.moveToFirst()) {
            respuesta[1] = cursor.getString(0);
        }
        cursor.close();
        cursor = db.rawQuery("Select max(" + indicador + ") from MONITOREO", new String[]{});
        if (cursor.moveToFirst()) {
            respuesta[2] = cursor.getString(0);
        }
        cursor.close();
        return respuesta;
    }
    public String[] consultarDatosUsuario() {
        String[] respuesta = new String[]{"",""};
        Cursor cursor=db.rawQuery("Select * from USUARIO",new String[]{});
        if (cursor.moveToFirst()){
            respuesta[0]=cursor.getString(0);
            respuesta[1]=cursor.getString(1);
        }
        cursor.close();
        return respuesta;
    }
    public String guardarDatosUsuario(String nombre, String email) {
        String respuesta="No se pudo guardar los datos";
        long contador=0;
        ContentValues cv = new ContentValues();
        cv.put("USERNAME", nombre);
        cv.put("EMAIL", email);
        Cursor cursor=db.rawQuery("Select * from USUARIO",new String[]{});
        if (cursor.moveToFirst()){
            String username=cursor.getString(0);
            cursor.close();
            contador=db.update("USUARIO", cv, "USERNAME = ?", new String[]{username});
        }
        else{
            cursor.close();
            contador=db.insert("USUARIO",null,cv);
        }
        if (contador==1){
            respuesta="Datos guardados";
        }
        return respuesta;
    }

    public boolean insertar(Monitoreo monitoreo) {


        long contador = 0;
        ContentValues moni = new ContentValues();
        moni.put("RPM", monitoreo.getRpm());
        moni.put("SPEED", monitoreo.getSpeed());
        moni.put("OIL_TEMP", monitoreo.getTempOil());
        moni.put("AMBI_TEMP", monitoreo.getTempAmb());
        moni.put("REFRI_TEMP", monitoreo.getTempRefri());
        moni.put("LOAD_ENGINE", monitoreo.getEngine());
        moni.put("LEVEL_FUEL", monitoreo.getLevelFuel());
        moni.put("RATE_FUEL", monitoreo.getPerFuel());
        moni.put("FECHA", monitoreo.getFecha().toString());
        /*moni.put("LATITUD" , monitoreo.getLatitud());
        moni.put("LONGITUD" , monitoreo.getLongitud());
        moni.put("ALTITUD" , monitoreo.getAltitud());*/
        contador = db.insert("MONITOREO", null, moni);
        if (contador == -1 || contador == 0) {
            return false;

        } else {
            contador++;
            return true;
        }

    }

    public String getEmailUser(){
        String email=null;
        Cursor cursor = db.rawQuery("Select EMAIL from USUARIO",null);
        if (cursor.moveToFirst()) {
            email = cursor.getString(0);
        }
        cursor.close();

        return email;
    }


    public String getNameUser(){
        String username=null;
        Cursor cursor = db.rawQuery("Select USERNAME from USUARIO",null);
        if (cursor.moveToFirst()) {
            username = cursor.getString(0);
        }
        cursor.close();

        return username;
    }


    public boolean sendMail(String nombreRemitente, String direccionRemite, String direccionDestino, String subject, String contenido) {
        final String miCorreo = "trycar1000@gmail.com";
        final String miContraseña="trycar123";
        final String servidorSMTP = "smtp.gmail.com";
        final String puertoEnvio = "465";

        boolean enviado = false;

        Properties props = new Properties();
        props.put("mail.smtp.user", miCorreo);
        props.put("mail.smtp.host", servidorSMTP);
        props.put("mail.smtp.port", puertoEnvio);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", puertoEnvio);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");


    try{

            Session mailSession = Session.getInstance(props, new Authenticator() {

            private PasswordAuthentication auth;
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                auth = new PasswordAuthentication(miCorreo,miContraseña);
                return auth;
            }
        });

        Transport transport = mailSession.getTransport();
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(direccionRemite, nombreRemitente));
        message.setSender(new InternetAddress(direccionRemite, nombreRemitente));
        message.setSubject(subject);
        message.setContent(contenido, "text/html");
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(direccionDestino));
        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
        enviado = true;
    }catch (UnsupportedEncodingException ex) {
        Logger.getLogger(BDControl.class.getName()).log(Level.SEVERE, null, ex);
    } catch (MessagingException ex) {
        Logger.getLogger(BDControl.class.getName()).log(Level.SEVERE, null, ex);
    }
        return enviado;
    }

}
