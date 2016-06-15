package fia.ues.sv.trycar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class DatosUsuario extends AppCompatActivity {
EditText edtnombre;
EditText edtemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);
        edtnombre=(EditText)findViewById(R.id.editnombre);
        edtemail=(EditText)findViewById(R.id.editemail);
    }
}
