package fia.ues.sv.trycar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import fia.ues.sv.trycar.model.BDControl;

public class EstadisticasActivity extends Activity implements AdapterView.OnItemSelectedListener{
    String indicador;
    private final String[] indicadores=new String[]{"RPM","SPEED","OIL_TEMP","AMBI_TEMP","REFRI_TEMP","LOAD_ENGINE","LEVEL_FUEL","RATE_FUEL"};
    Spinner indicadorSpinner;
    EditText editMaximo;
    EditText editMinimo;
    EditText editPromedio;
    BDControl db= new BDControl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        editMaximo =(EditText)findViewById(R.id.editMaximo);
        editMinimo =(EditText)findViewById(R.id.editMinimo);
        editPromedio =(EditText)findViewById(R.id.editPromedio);
        indicadorSpinner = (Spinner) findViewById(R.id.indicadorSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.indicadores_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        indicadorSpinner.setAdapter(adapter);
        indicadorSpinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        indicador=indicadores[pos];
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void consultarEstadistica(View view) {
        db.abrir();
        String[] resultado=db.consultarEstadistica(this,indicador);
        db.cerrar();
        editMinimo.setText(resultado[0]);
        editMaximo.setText(resultado[1]);
        editPromedio.setText(resultado[2]);
    }
}
