package com.vishalpro.meme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutput;
import java.text.BreakIterator;
import java.text.ChoiceFormat;

public class MainActivity extends AppCompatActivity {

    //var  currentImageUrl  = null;

    String currentImageUrl = " ";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMeme();
    }

    private void loadMeme(){

        final ProgressBar progressBar =  findViewById(R.id.progressBar);

        //set progressBar visible
        progressBar.setVisibility(View.VISIBLE);

            // Instantiate the RequestQueue.
          // RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            String url ="https://meme-api.herokuapp.com/gimme";

         // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            ImageView MemeImageView = findViewById(R.id.MemeImageView);
                            try {
                               String url = response.getString("url");
                                currentImageUrl = response.getString("url");
                               // currentImageUrl = response.getString("currentImageUrl");
                                Glide.with(MainActivity.this).load(url).
                                        listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    //    ProgressBar p = findViewById(R.id.progressBar);
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                }).into(MemeImageView);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(MainActivity.this,"Somethings is wrong", Toast.LENGTH_LONG).show();
                }
            });

       // Add the request to the RequestQueue.
          //  queue.add(jsonObjectRequest);
       MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    public  void shareMeme(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
       // intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout a Meme"+ currentImageUrl);
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout a Meme " + currentImageUrl);
        intent.setType("text/plain");
        Intent chooser = Intent.createChooser(intent, "Share this to...");
        startActivity(chooser);
    }

    public void nextMeme(View view) {
        loadMeme();
    }
}