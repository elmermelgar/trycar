package fia.ues.sv.trycar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.engine.LoadCommand;
import com.github.pires.obd.commands.engine.OilTempCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.fuel.AirFuelRatioCommand;
import com.github.pires.obd.commands.fuel.ConsumptionRateCommand;
import com.github.pires.obd.commands.fuel.FuelLevelCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.ObdRawCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.commands.temperature.AmbientAirTemperatureCommand;
import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.UUID;

import fia.ues.sv.trycar.model.BDControl;
import fia.ues.sv.trycar.util.BluetoothConnector;
import fia.ues.sv.trycar.util.BluetoothSocketWrapper;

public class Monitoreo extends Activity {

EditText edtrpm;
EditText edtspeed;
EditText edttempamb;
EditText edttemprefri;
EditText edttempoil;
EditText edtengine;
EditText edtper_fuel;
EditText edtlevelfuel;


     CheckBox ledGps;
     ArrayAdapter<CharSequence> adapter;
    LocationManager locationManager;
     String latitud="0";
     String longitud="0";


    //Initial configuration ODB Adapter
    EchoOffCommand echoOffCommand;
    //Bluetooth configuration
    BluetoothConnector bluetoothConnector;
    BluetoothAdapter bluetoothAdapter;
    List<UUID> listUUID;
    BluetoothSocketWrapper bluetoothSocketWrapper;
    BluetoothDevice bluetoothDevice;
    //Commands OBD
    ObdRawCommand obdRawCommand; //PIDS que soporta el carro del modo 01
    RPMCommand rpmCommand; //Revoluciones por minuto
    SpeedCommand speedCommand; //Velocidad Km/h
    AmbientAirTemperatureCommand ambientAirTemperatureCommand; //Temperatura ambiente interna
    EngineCoolantTemperatureCommand engineCoolantTemperatureCommand; //Temperatura del refigerante
    OilTempCommand oilTempCommand; //Temperatura del aceite
    LoadCommand loadCommand;   //Carga del motor
    ConsumptionRateCommand consumptionRateCommand; //Tasa de consumo de combustible
    FuelLevelCommand fuelLevelCommand; //Nivel de combustible


    BDControl db;

    AirFuelRatioCommand airFuelRatioCommand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoreo);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        edtrpm=(EditText)findViewById(R.id.editrpm);
        edtspeed=(EditText)findViewById(R.id.editspeed);
        edttempamb=(EditText)findViewById(R.id.edittempamb);
        edttemprefri=(EditText)findViewById(R.id.edittemprefri);
        edttempoil=(EditText)findViewById(R.id.edittempoil);
        edtengine=(EditText)findViewById(R.id.editengine);
        edtper_fuel=(EditText)findViewById(R.id.editpercentfuel);
        edtlevelfuel=(EditText)findViewById(R.id.editlevelfuel);

        ledGps = (CheckBox) findViewById(R.id.ledGps);

        locationManager = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);
        db=new BDControl(this);



        //Getting the default bluetooth adapter
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        //Getting the OBD device
        BluetoothDevice device = btAdapter.getRemoteDevice("00:1D:A5:68:98:8D");//MAC addres
        //Create the bluetooth connector
        bluetoothConnector=new BluetoothConnector(device,true, btAdapter, listUUID);
        try {
            bluetoothSocketWrapper=bluetoothConnector.connect(); //Connect
            System.out.println("Conectado a :"+device.getName()); //Show the name of the bluetooth connector
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //Initialization of OBD
            new ObdRawCommand("AT D").run(bluetoothSocketWrapper.getInputStream(),bluetoothSocketWrapper.getOutputStream());
            new ObdRawCommand("AT Z").run(bluetoothSocketWrapper.getInputStream(),bluetoothSocketWrapper.getOutputStream());
            new EchoOffCommand().run( bluetoothSocketWrapper.getInputStream(),   bluetoothSocketWrapper.getOutputStream());
            new LineFeedOffCommand().run(  bluetoothSocketWrapper.getInputStream(),   bluetoothSocketWrapper.getOutputStream());
            new TimeoutCommand(500).run( bluetoothSocketWrapper.getInputStream(),   bluetoothSocketWrapper.getOutputStream());
            new SelectProtocolCommand(ObdProtocols.AUTO).run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());

           monitorear();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void monitorear() throws InterruptedException {
        //Comando para determinar que pids del modo 01 soporta el carro
        obdRawCommand=new ObdRawCommand("01 00");
        rpmCommand= new RPMCommand();
        speedCommand=new SpeedCommand();
        engineCoolantTemperatureCommand=new EngineCoolantTemperatureCommand();
        ambientAirTemperatureCommand=new AmbientAirTemperatureCommand();
        oilTempCommand=new OilTempCommand();
        loadCommand= new LoadCommand();
        consumptionRateCommand=new ConsumptionRateCommand();
        fuelLevelCommand=new FuelLevelCommand();

        new Thread() {
            public void run() {
                int i=0;
                while (i++ < 50) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try{
                                obdRawCommand.run(bluetoothSocketWrapper.getInputStream(),bluetoothSocketWrapper.getOutputStream());


                speedCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
                rpmCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
              //  ambientAirTemperatureCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
              //  oilTempCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
                loadCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
               // consumptionRateCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
               // engineCoolantTemperatureCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
               // fuelLevelCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());

                                //Setting the EditText
                                //Si el comando retorna un null le pone 0.00
                                //Thread.currentThread().sleep(5000);
                                edtrpm.setText(rpmCommand.getCalculatedResult()!=null?rpmCommand.getCalculatedResult():"0.00");
                                edtspeed.setText(speedCommand.getCalculatedResult()!=null?speedCommand.getCalculatedResult():"0.00");
                                //             this.edttempamb.setText(ambientAirTemperatureCommand.getCalculatedResult()!=null?ambientAirTemperatureCommand.getCalculatedResult():"0.00");
                                //this.edttempoil.setText(oilTempCommand.getCalculatedResult()!=null?oilTempCommand.getCalculatedResult():"0.00");
                                edtengine.setText(loadCommand.getCalculatedResult()!=null?loadCommand.getCalculatedResult():"0.00");
                                // this.edtper_fuel.setText(consumptionRateCommand.getCalculatedResult()!=null?consumptionRateCommand.getCalculatedResult():"0.00");
                                edtlevelfuel.setText(fuelLevelCommand.getCalculatedResult()!=null?fuelLevelCommand.getCalculatedResult():"0.00");
                                edttemprefri.setText(engineCoolantTemperatureCommand.getCalculatedResult()!=null?engineCoolantTemperatureCommand.getCalculatedResult():"0.00");

                System.out.println("RPM:" + rpmCommand.getFormattedResult());
                System.out.println("Velocidad:" + speedCommand.getFormattedResult());
                System.out.println("Algo: " + loadCommand.getFormattedResult());


                                System.out.println("PID:" + obdRawCommand.getFormattedResult());

                                System.out.println("Velocidad:" + speedCommand.getFormattedResult());
                                System.out.println("RPM:" + rpmCommand.getFormattedResult());
                                //           System.out.println("Temp Amb:" + ambientAirTemperatureCommand.getFormattedResult());
                                //              System.out.println("Temp Oil:" + oilTempCommand.getFormattedResult());
                                System.out.println("Carga del motor: " + loadCommand.getFormattedResult());
                                System.out.println("Engine Coolant:" + engineCoolantTemperatureCommand.getFormattedResult());
                                //              System.out.println("Consumo combustible(%):" + consumptionRateCommand.getFormattedResult());

                                System.out.println("Nivel de combustible:" + fuelLevelCommand.getFormattedResult());
                                //rpm=rpmCommand.getCalculatedResult();
                                //Thread.currentThread().interrupt();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            }
                        });
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


    }

    public void guardar(View v){


        //Thread.currentThread().stop();

        Double rpm=Double.parseDouble(edtrpm.getText().toString());
        Double speed=Double.parseDouble(edtspeed.getText().toString());
        //Double oilTem=Double.parseDouble(edtrpm.getText().toString());
        //Double ambTem=Double.parseDouble(edtrpm.getText().toString());
        Double refriTem=Double.parseDouble(edttemprefri.getText().toString());
        Double engine=Double.parseDouble(edtengine.getText().toString());
        Double levelFuel=Double.parseDouble(edtlevelfuel.getText().toString());
        //Double rateFuel=Double.parseDouble(edtrpm.getText().toString());


        SimpleDateFormat sdfAmerica = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
        sdfAmerica.setTimeZone(TimeZone.getTimeZone("America/El_Salvador"));
        Date date = new Date();
        String sDateInAmerica = sdfAmerica.format(date);

        fia.ues.sv.trycar.model.Monitoreo monitoreo=new fia.ues.sv.trycar.model.Monitoreo();

        monitoreo.setRpm(rpm);
        monitoreo.setSpeed(speed);
       // monitoreo.setTempOil(oilTem);
        //monitoreo.setTempAmb(ambTem);
        monitoreo.setTempRefri(refriTem);
        monitoreo.setEngine(engine);
        monitoreo.setLevelFuel(levelFuel);
        //monitoreo.setPerFuel(rateFuel);
        monitoreo.setFecha(sDateInAmerica);
        monitoreo.setLatitud(latitud);
        monitoreo.setLongitud(longitud);
        db.abrir();
        boolean insertado=db.insertar(monitoreo);
        db.cerrar();
        if(insertado)
            Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();

    }

    public void enviarEmail(View v) throws ParseException {

        boolean enviado;
        SimpleDateFormat sdfAmerica = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
        sdfAmerica.setTimeZone(TimeZone.getTimeZone("America/El_Salvador"));

        Date date = new Date();

        String sDateInAmerica = sdfAmerica.format(date);
        String nombreRemitente=null;
        String direccionDestino=null;
        String direccionRemite="trycar1000@gmail.com";
        String subject="Diagnóstico del vehiculo \t"+sDateInAmerica;
        String contenido=null;

        db.abrir();
        //nombreRemitente=db.getNameUser();
        direccionDestino=db.getEmailUser();
        db.cerrar();

        nombreRemitente="TryCar";

       contenido=rpmCommand.getFormattedResult()+"\n"+
                speedCommand.getFormattedResult()+"\n"+
                //oilTempCommand.getFormattedResult()+"\n"+
               // airFuelRatioCommand.getFormattedResult()+"\n"+
                loadCommand.getFormattedResult()+"Carga del motor"+"\n"+
                //consumptionRateCommand.getFormattedResult()+"\n"+
                fuelLevelCommand.getFormattedResult()+"Nivel de combustible"+"\n"+
                engineCoolantTemperatureCommand.getFormattedResult()+"Temperatura del motor";

       // contenido="Esto es una prueba";


        enviado=db.sendMail(nombreRemitente,direccionRemite,direccionDestino,subject,contenido);

        if(enviado)
            Toast.makeText(this,"Correo enviado",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Error al enviar correo",Toast.LENGTH_LONG).show();



    }

    public void mostrarRuta(View view) {
        Class<?> clase = null;
        try {
            clase = Class.forName("fia.ues.sv.trycar.MAPS");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent inte = new Intent();
        inte.setClass(view.getContext(), clase);
        startActivity(inte);

    }


    LocationListener locationListener = new LocationListener() {


        public void onLocationChanged(Location location) {
// TODO Auto-generated method stub
            latitud = String.valueOf(location.getLatitude());
            longitud = String.valueOf(location.getLongitude());


            ledGps.setChecked(true);



        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            ledGps.setChecked(true);


        }

        @Override
        public void onProviderEnabled(String provider) {


        }

        @Override
        public void onProviderDisabled(String provider) {
            latitud ="0";
            longitud="0";



        }
    };

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }
    @Override
    public void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }
}
