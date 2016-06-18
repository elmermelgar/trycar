package fia.ues.sv.trycar;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.protocol.*;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.fuel.AirFuelRatioCommand;
import com.github.pires.obd.enums.ObdProtocols;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import fia.ues.sv.trycar.util.BluetoothConnector;
import fia.ues.sv.trycar.util.BluetoothSocketWrapper;

public class MainActivity_old extends Activity {
    //Initial configuration ODB Adapter
    EchoOffCommand echoOffCommand;
    //Bluetooth configuration
    BluetoothConnector bluetoothConnector;
    BluetoothAdapter bluetoothAdapter;
    List<UUID> list;
    BluetoothSocketWrapper bluetoothSocketWrapper;
    BluetoothDevice bluetoothDevice;
    ArrayList deviceStrs = new ArrayList();
    final ArrayList devices = new ArrayList();
    //test RPM
    RPMCommand rpmCommand;
    SpeedCommand speedCommand;
    AirFuelRatioCommand airFuelRatioCommand;
    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Set prueba=new HashSet(1);
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set pairedDevices = btAdapter.getBondedDevices()!=null?btAdapter.getBondedDevices():prueba;
        if ( pairedDevices.size() > 0)
        {
            for (Object device : pairedDevices)
            {
                BluetoothDevice dev= (BluetoothDevice)device;
                deviceStrs.add(dev.getName() + "\n" + dev.getAddress());
                devices.add(dev.getAddress());
                //System.out.println(dev.getName() + "\n" + dev.getAddress());
                //Toast.makeText(this,dev.getName() + "\n" + dev.getAddress(),Toast.LENGTH_SHORT).show();
            }
        }
        System.out.println("adaptador:"+BluetoothAdapter.getDefaultAdapter().getAddress());
        list=new ArrayList();
        BluetoothAdapter btAdapter1 = BluetoothAdapter.getDefaultAdapter();

        BluetoothDevice device = btAdapter.getRemoteDevice("00:1D:A5:68:98:8D");

        bluetoothConnector=new BluetoothConnector(device,true, btAdapter1, list);
        try {
            bluetoothSocketWrapper=bluetoothConnector.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new EchoOffCommand().run( bluetoothSocketWrapper.getInputStream(),   bluetoothSocketWrapper.getOutputStream());
            new LineFeedOffCommand().run(  bluetoothSocketWrapper.getInputStream(),   bluetoothSocketWrapper.getOutputStream());
            new TimeoutCommand(10).run( bluetoothSocketWrapper.getInputStream(),   bluetoothSocketWrapper.getOutputStream());
            new SelectProtocolCommand(ObdProtocols.AUTO).run(  bluetoothSocketWrapper.getInputStream(),   bluetoothSocketWrapper.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //get RPM
        rpmCommand= new RPMCommand();
        speedCommand=new SpeedCommand();
       // airFuelRatioCommand=new AirFuelRatioCommand();
        while(!Thread.currentThread().isInterrupted()){
            try {
                speedCommand.run(bluetoothSocketWrapper.getInputStream(),bluetoothSocketWrapper.getOutputStream());
                rpmCommand.run(bluetoothSocketWrapper.getInputStream(),bluetoothSocketWrapper.getOutputStream());
                //airFuelRatioCommand.run(bluetoothSocketWrapper.getInputStream(),bluetoothSocketWrapper.getOutputStream());
                System.out.println("RPM:" + rpmCommand.getFormattedResult());
                System.out.println("Velocidad:"+speedCommand.getFormattedResult());
                //System.out.println("Aire gas:"+airFuelRatioCommand.getFormattedResult());
                //Toast.makeText(MainActivity_old.this, "RPM:"+rpmCommand.getFormattedResult(),Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
