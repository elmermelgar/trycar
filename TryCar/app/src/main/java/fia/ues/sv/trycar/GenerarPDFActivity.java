package fia.ues.sv.trycar;

import android.app.Activity;
import android.os.Bundle;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import fia.ues.sv.trycar.model.BDControl;

public class GenerarPDFActivity extends Activity {

    private static final String NOMBRE_CARPETA_APP = "trycar.pdfgenerator";
    private static final String GENERADOS = "MisPDFs";
    BDControl db;

    Button btn_generar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= new BDControl(this);
        setContentView(R.layout.activity_generar_pdf);

        btn_generar=(Button)findViewById(R.id.btn_generarPDF);
    }
    public void generarPDFOnClick(View v){

        Document document=new Document(PageSize.LETTER);
        String NOMBRE_ARCHIVO ="MiArchivoPDF.pdf";
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        File pdfDir= new File(tarjetaSD + File.separator + NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()){
            pdfDir.mkdir();
        }

        File pdfSubDir=new File(pdfDir.getPath() + File.separator + GENERADOS);
        if (!pdfSubDir.exists()){
            pdfSubDir.mkdir();
        }

        String nombre_completo=tarjetaSD + File.separator + NOMBRE_CARPETA_APP
                + File.separator + GENERADOS + File.separator + NOMBRE_ARCHIVO;

        File outputfile = new File(nombre_completo);
        if (outputfile.exists()){
            outputfile.delete();
        }

        try {
            PdfWriter pdfWriter=PdfWriter.getInstance(document, new FileOutputStream(nombre_completo));
            //Crear El documento
            document.open();
            document.addAuthor("Elmer Melgar");
            document.addCreator("Creador");
            document.addSubject("Informe de Monitoreo");
            document.addCreationDate();
            document.addTitle("Informe de Monitoreo TryCar");
            Font font= new Font(Font.FontFamily.TIMES_ROMAN,Font.BOLD,Color.BLUE);
            document.add(new Paragraph("TryCar"));
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());


            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image imagen = null;
            try {
                imagen = Image.getInstance(stream.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            document.add(imagen);

            Bitmap bittry = BitmapFactory.decodeResource(this.getResources(), R.drawable.trycar);
            ByteArrayOutputStream streamtry = new ByteArrayOutputStream();
            bittry.compress(Bitmap.CompressFormat.PNG, 100, streamtry);
            Image imagentry = null;
            try {
                imagentry = Image.getInstance(streamtry.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            document.add(imagentry);
            document.add(new Paragraph(" "));
            document.add(new VerticalPositionMark());

            // Insertamos una tabla.
            String[] resultado=db.consultarMonitoreo(this);
            if (resultado==null){
                document.add(new Paragraph("No Existen DATOS para Mostrar"));
                Toast.makeText(this, "No existen datos en la Base de Datos", Toast.LENGTH_LONG).show();
            }else {
                // Insertamos una tabla.
                PdfPTable tabla = new PdfPTable(9);
                tabla.addCell("RPM");
                tabla.addCell("Speed");
                tabla.addCell("Temp Oil");
                tabla.addCell("Temp Amb");
                tabla.addCell("Temp Refri");
                tabla.addCell("Engine");
                tabla.addCell("Level Fuel");
                tabla.addCell("%Fuel");
                tabla.addCell("Fecha");

                for (int i = 0; i < resultado.length; i++) {
                    tabla.addCell(resultado[i]);
                }
                document.add(tabla);
            }




        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            document.close();
            Toast.makeText(this, "PDF esta Generado", Toast.LENGTH_LONG).show();
            muestraPDF(nombre_completo, this);
        }

    }

    public void muestraPDF(String archivo, Context context){

        Toast.makeText(context,"Leyendo el archivo",Toast.LENGTH_LONG).show();

        File file= new File(archivo);
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);

        }catch (ActivityNotFoundException e){
            Toast.makeText(context,"No tiene una aplicacion para leer este tipo de archivos", Toast.LENGTH_LONG).show();
        }
    }
}
