package fia.ues.sv.trycar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import fia.ues.sv.trycar.model.BDControl;

public class DatosUsuarioActivity extends AppCompatActivity {
    EditText editNombre;
    EditText editEmail;
    BDControl db= new BDControl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);
        editNombre = (EditText) findViewById(R.id.editNombre);
        editEmail = (EditText) findViewById(R.id.editEmail);
        consultarDatosUsuario();
    }

    public void consultarDatosUsuario() {
        db.abrir();
        String[] respuesta=db.consultarDatosUsuario();
        db.cerrar();
        editNombre.setText(respuesta[0]);
        editEmail.setText(respuesta[1]);
    }

    public void guardarDatosUsuario(View view) {
        String nombre = editNombre.getText().toString();
        String email = editEmail.getText().toString();
        db.abrir();
        String respuesta=db.guardarDatosUsuario(nombre,email);
        db.cerrar();
        Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();
    }
}
