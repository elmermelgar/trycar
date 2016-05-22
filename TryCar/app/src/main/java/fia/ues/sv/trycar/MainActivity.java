package fia.ues.sv.trycar;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.github.pires.obd.commands.ObdMultiCommand;
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

public class MainActivity extends Activity {


    BluetoothConnector bluetoothConnector;
    BluetoothAdapter bluetoothAdapter;
    List<UUID> list;
    BluetoothSocketWrapper bluetoothSocketWrapper;
    BluetoothDevice bluetoothDevice;
    ArrayList deviceStrs = new ArrayList();
    final ArrayList devices = new ArrayList();
    //test RPM
    RPMCommand rpmCommand;
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
                System.out.println(dev.getName() + "\n" + dev.getAddress());
                Toast.makeText(this,dev.getName() + "\n" + dev.getAddress(),Toast.LENGTH_SHORT).show();
            }
        }

       /* bluetoothConnector=new BluetoothConnector(,true, BluetoothAdapter.getDefaultAdapter(), list);
        try {
            bluetoothSocketWrapper=bluetoothConnector.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get RPM
        rpmCommand= new RPMCommand();
        while(!Thread.currentThread().isInterrupted()){
            try {
                rpmCommand.run(bluetoothSocketWrapper.getInputStream(),bluetoothSocketWrapper.getOutputStream());
                Toast.makeText(MainActivity.this, "RPM:"+rpmCommand.getFormattedResult(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

*/

    }
}
