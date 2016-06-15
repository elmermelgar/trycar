package fia.ues.sv.trycar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class Estadisticas extends AppCompatActivity {
EditText edtconsultar;
EditText edtmaximo;
EditText edtminimo;
EditText edtpromedio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        edtconsultar=(EditText)findViewById(R.id.editcodigo);
        edtmaximo=(EditText)findViewById(R.id.editmaximo);
        edtminimo=(EditText)findViewById(R.id.editminimo);
        edtpromedio=(EditText)findViewById(R.id.editpromedio);
    }
}
