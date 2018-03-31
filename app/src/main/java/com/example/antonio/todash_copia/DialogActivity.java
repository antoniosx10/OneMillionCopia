package com.example.antonio.todash_copia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import unisa.it.pc1.todash.R;

public class DialogActivity extends AppCompatActivity {

    private TextView dialog_name;
    private TextView dialog_descrizione;
    private ImageView dialog_immagine;

    private ProgressBar avanzamento;

    private TextCrawler textCrawler;

    private RelativeLayout item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_send);

        Intent intent = getIntent();

        String link = intent.getStringExtra("link");

        dialog_name = findViewById(R.id.dialog_name);
        dialog_descrizione = findViewById(R.id.dialog_descrizione);
        dialog_immagine = findViewById(R.id.dialog_immagine);

        avanzamento = new ProgressBar(this);

        textCrawler = new TextCrawler();


        if(isLink(link)){
            textCrawler.makePreview( linkPreviewCallback, link);
            avanzamento = new ProgressBar(getApplicationContext());
            avanzamento.setVisibility(ProgressBar.VISIBLE);
            for (int i = 1; i < 11; i++) {
                sleep();
                avanzamento.setProgress(i * 10);
            }
        }else{
            dialog_name.setText(link);
        }


    }

    LinkPreviewCallback linkPreviewCallback = new LinkPreviewCallback() {
        @Override
        public void onPre() {
            item = findViewById(R.id.main);
        }

        @Override
        public void onPos(final SourceContent sourceContent, boolean b) {
            new ImageLoadTask(sourceContent.getImages().get(0), dialog_immagine).execute();

            dialog_name.setText(sourceContent.getTitle());

            dialog_descrizione.setText(sourceContent.getDescription());

        }
    };

    private boolean isLink(String testo){
        String subString = testo.substring(0,4);

        if(subString.equalsIgnoreCase("http")){
            return true;
        }else{
            return false;
        }
    }


    class ImageLoadTask extends AsyncTask<Void, Integer, Bitmap> {

        private String url;
        private ImageView imageView;



        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            avanzamento.setVisibility(ProgressBar.INVISIBLE);
            avanzamento.setProgress(0);
            imageView.setImageBitmap(result);
        }




    }

    private void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
