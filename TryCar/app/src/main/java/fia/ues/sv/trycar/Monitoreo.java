package fia.ues.sv.trycar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.engine.LoadCommand;
import com.github.pires.obd.commands.engine.OilTempCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.fuel.AirFuelRatioCommand;
import com.github.pires.obd.commands.fuel.ConsumptionRateCommand;
import com.github.pires.obd.commands.fuel.FuelLevelCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.commands.temperature.AmbientAirTemperatureCommand;
import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import fia.ues.sv.trycar.model.BDControl;
import fia.ues.sv.trycar.util.BluetoothConnector;
import fia.ues.sv.trycar.util.BluetoothSocketWrapper;

public class Monitoreo extends AppCompatActivity {

EditText edtrpm;
EditText edtspeed;
EditText edttempamb;
EditText edttemprefri;
EditText edttempoil;
EditText edtengine;
EditText edtper_fuel;
EditText edtlevelfuel;

    //Initial configuration ODB Adapter
    EchoOffCommand echoOffCommand;
    //Bluetooth configuration
    BluetoothConnector bluetoothConnector;
    BluetoothAdapter bluetoothAdapter;
    List<UUID> listUUID;
    BluetoothSocketWrapper bluetoothSocketWrapper;
    BluetoothDevice bluetoothDevice;
    //Commands OBD
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
        edtrpm=(EditText)findViewById(R.id.editrpm);
        edtspeed=(EditText)findViewById(R.id.editspeed);
        edttempamb=(EditText)findViewById(R.id.edittempamb);
        edttemprefri=(EditText)findViewById(R.id.edittemprefri);
        edttempoil=(EditText)findViewById(R.id.edittempoil);
        edtengine=(EditText)findViewById(R.id.editengine);
        edtper_fuel=(EditText)findViewById(R.id.editpercentfuel);
        edtlevelfuel=(EditText)findViewById(R.id.editlevelfuel);


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
            new EchoOffCommand().run( bluetoothSocketWrapper.getInputStream(),   bluetoothSocketWrapper.getOutputStream());
            new LineFeedOffCommand().run(  bluetoothSocketWrapper.getInputStream(),   bluetoothSocketWrapper.getOutputStream());
            new TimeoutCommand(10).run( bluetoothSocketWrapper.getInputStream(),   bluetoothSocketWrapper.getOutputStream());
            new SelectProtocolCommand(ObdProtocols.AUTO).run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());

            monitorear();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void monitorear() throws InterruptedException {


        rpmCommand= new RPMCommand();
        speedCommand=new SpeedCommand();
        engineCoolantTemperatureCommand=new EngineCoolantTemperatureCommand();
        ambientAirTemperatureCommand=new AmbientAirTemperatureCommand();
        oilTempCommand=new OilTempCommand();
        loadCommand= new LoadCommand();
        consumptionRateCommand=new ConsumptionRateCommand();
        fuelLevelCommand=new FuelLevelCommand();


        while(!Thread.currentThread().isInterrupted()){

            try {
                speedCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
                rpmCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
                ambientAirTemperatureCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
                oilTempCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
                loadCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
                consumptionRateCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
                engineCoolantTemperatureCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());
                fuelLevelCommand.run(bluetoothSocketWrapper.getInputStream(), bluetoothSocketWrapper.getOutputStream());

                //Setting the EditText
                //Si el comando retorna un null le pone 0.00
                this.edtrpm.setText(rpmCommand.getCalculatedResult()!=null?rpmCommand.getCalculatedResult():"0.00");
                this.edtspeed.setText(speedCommand.getCalculatedResult()!=null?speedCommand.getCalculatedResult():"0.00");
                this.edttempamb.setText(ambientAirTemperatureCommand.getCalculatedResult()!=null?ambientAirTemperatureCommand.getCalculatedResult():"0.00");
                this.edttempoil.setText(oilTempCommand.getCalculatedResult()!=null?oilTempCommand.getCalculatedResult():"0.00");
                this.edtengine.setText(loadCommand.getCalculatedResult()!=null?loadCommand.getCalculatedResult():"0.00");
                this.edtper_fuel.setText(consumptionRateCommand.getCalculatedResult()!=null?consumptionRateCommand.getCalculatedResult():"0.00");
                this.edtlevelfuel.setText(fuelLevelCommand.getCalculatedResult()!=null?fuelLevelCommand.getCalculatedResult():"0.00");
                this.edttemprefri.setText(engineCoolantTemperatureCommand.getCalculatedResult()!=null?engineCoolantTemperatureCommand.getCalculatedResult():"0.00");

                Thread.currentThread().sleep(5000);



            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void guardar(View v){


        //Thread.currentThread().stop();

        Double rpm=Double.parseDouble(edtrpm.getText().toString());
        Double speed=Double.parseDouble(edtrpm.getText().toString());
        Double oilTem=Double.parseDouble(edtrpm.getText().toString());
        Double ambTem=Double.parseDouble(edtrpm.getText().toString());
        Double refriTem=Double.parseDouble(edtrpm.getText().toString());
        Double engine=Double.parseDouble(edtrpm.getText().toString());
        Double levelFuel=Double.parseDouble(edtrpm.getText().toString());
        Double rateFuel=Double.parseDouble(edtrpm.getText().toString());
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        String fecha=dateFormat.format(new Date());

        fia.ues.sv.trycar.model.Monitoreo monitoreo=new fia.ues.sv.trycar.model.Monitoreo();

        monitoreo.setRpm(rpm);
        monitoreo.setSpeed(speed);
        monitoreo.setTempOil(oilTem);
        monitoreo.setTempAmb(ambTem);
        monitoreo.setTempRefri(refriTem);
        monitoreo.setEngine(engine);
        monitoreo.setLevelFuel(levelFuel);
        monitoreo.setPerFuel(rateFuel);
        monitoreo.setFecha(new Date(fecha));

        db.abrir();
        boolean insertado=db.insertar(monitoreo);
        db.cerrar();
        if(insertado)
            Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();

    }



}
