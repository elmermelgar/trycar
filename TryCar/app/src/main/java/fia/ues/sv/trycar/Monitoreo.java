package fia.ues.sv.trycar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class Monitoreo extends AppCompatActivity {
EditText edtrpm;
EditText edtspeed;
EditText edttempamb;
EditText edttemprefri;
EditText edttempoil;
EditText edtengine;
EditText edtper_fuel;
EditText edtlevelfuel;

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
    }
}
