package fia.ues.sv.trycar;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.github.pires.obd.commands.ObdMultiCommand;
import com.github.pires.obd.commands.fuel.AirFuelRatioCommand;

import java.io.File;

public class MainActivity extends Activity {






    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
