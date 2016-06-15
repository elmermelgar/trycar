package fia.ues.sv.trycar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TablaAdapter extends ArrayAdapter<Tabla> { Context context;
    int layoutResourceId;
    Tabla data[] = null;

    public TablaAdapter(Context context, int layoutResourceId, Tabla[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TablaHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TablaHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (TablaHolder)row.getTag();
        }

        Tabla tabla = data[position];
        holder.txtTitle.setText(tabla.title);
        holder.imgIcon.setImageResource(tabla.icon);

        return row;
    }

    static class TablaHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
