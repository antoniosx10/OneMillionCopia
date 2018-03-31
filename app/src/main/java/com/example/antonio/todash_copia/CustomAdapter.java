package com.example.antonio.todash_copia;

/**
 * Created by Antonio on 22/03/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import unisa.it.pc1.todash.R;

public class CustomAdapter extends ArrayAdapter<Task> {
    private int resource;
    private LayoutInflater inflater;

    public CustomAdapter(Context context, int resourceId, List<Task> objects) {
        super(context, resourceId, objects);
        resource = resourceId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            v = inflater.inflate(R.layout.list_element, null);
        }

        Task t = getItem(position);


        TextView nomeTask;
        TextView dataTask;
        Button invia;
        Button dettagli;

        nomeTask = (TextView) v.findViewById(R.id.task);
        dataTask = (TextView) v.findViewById(R.id.dataTask);
        invia =v.findViewById(R.id.btnInvia);
        dettagli = v.findViewById(R.id.btnDettagli);

        nomeTask.setText(t.getContenuto());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date today = t.getData();
        String reportDate = df.format(today);
        dataTask.setText(reportDate);

        invia.setTag(position);
        dettagli.setTag(position);

        return v;
    }
}

