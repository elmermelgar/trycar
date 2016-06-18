package fia.ues.sv.trycar;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import fia.ues.sv.trycar.model.BDControl;

public class MainActivity extends Activity {
    private ListView listViewTabla;

    String[] menu = {"Usuario","Monitorear","Estadisticas","Generar PDF"};
    String[] activities = {null,"DatosUsuarioActivity","Monitoreo","EstadisticasActivity","GenerarPDFActivity" };
    BDControl db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new BDControl(this);
        Tabla tabla_data[] = new Tabla[]
                {

                        new Tabla(R.drawable.usuario, "Usuario"),
                        new Tabla(R.drawable.monitoreo, "Monitorear"),
                        new Tabla(R.drawable.estadistica, "Estadistica"),
                        new Tabla(R.drawable.pdf, "Reporte"),

                };

        TablaAdapter adapter = new TablaAdapter(this, R.layout.listview_item_row, tabla_data);


        listViewTabla = (ListView)findViewById(R.id.listViewTablas);

        View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        listViewTabla.addHeaderView(header);

        listViewTabla.setAdapter(adapter);

        listViewTabla.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nombreValue = null;
                    if (activities[position] != null) {
                        nombreValue = activities[position];
                        try {
                            Class<?> clase = Class.forName("fia.ues.sv.trycar." + nombreValue);
                            Intent inte = new Intent();
                            inte.setClass(view.getContext(), clase);
                            startActivity(inte);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    return;
            }
        });


    }

}


