package fia.ues.sv.trycar;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivityOtto extends ListActivity {
    private ListView listViewTabla;

    String[] menu = {"Usuario","Monitorear","Estadisticas"};
    String[] activities = {null,"DatosUsuarioActivity","Monitoreo","EstadisticasActivity" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tabla tabla_data[] = new Tabla[]
                {

                        new Tabla(R.drawable.usuario, "Usuario"),
                        new Tabla(R.drawable.monitoreo, "Monitorear"),
                        new Tabla(R.drawable.estadistica, "Estadistica"),

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
                if (position != 3) {
                    if (activities[position] != null) {
                        nombreValue = activities[position];
                        try {
                            Class<?> clase = Class.forName("tarea2.trycar." + nombreValue);
                            Intent inte = new Intent();
                            inte.setClass(view.getContext(), clase);
                            startActivity(inte);


                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }

            }
        });


    }


   /* @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.i("22", "PRUEBA LOFG");
        if (position != 11) {
            String nombreValue = activities[position];
            try {
                Class<?> clase = Class.forName("work.tarea1." + nombreValue);
                Intent inte = new Intent(this, clase);
                this.startActivity(inte);


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {          //CODIGO PARA LLENAR BASE DE DATOS

            BDhelper.abrir();
            String tost = BDhelper.llenarBD();
            BDhelper.cerrar();
            Toast.makeText(this,tost, Toast.LENGTH_SHORT).show();

        }
    }*/
}


