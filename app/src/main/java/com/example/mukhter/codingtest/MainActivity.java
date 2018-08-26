package com.example.mukhter.codingtest;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;


import com.example.mukhter.codingtest.Adapter.Adapter;
import com.example.mukhter.codingtest.Model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    JSONObject jsonpart;
    JSONObject object3;
    JSONObject object4;
    RecyclerView recyclerView;
   ArrayList<Model> modelArrayList; // arraylist that holds all the data
    ProgressDialog dialog;

    private Adapter adapter;
    private Paint p = new Paint();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        modelArrayList =new ArrayList<>();

        dialog = new ProgressDialog(this);
        DownloadTask task = new DownloadTask();
        try {
            task.execute("https://restcountries.eu/rest/v2/all");//api

        } catch (Exception e) {
            e.printStackTrace();
        }
recyclerView= (RecyclerView)findViewById(R.id.recycler);


        adapter = new Adapter(this, modelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        enableSwipe();


    }

    public class DownloadTask extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {

            String result = "";

            URL url;

            HttpURLConnection urlConnection = null;

            try {

                url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }


                Log.i("URL Content", result);// shows all the contents fetched from the api


                JSONArray jsonArray = new JSONArray(result);

                //removes the items part in the api

                for (int i = 0; i < jsonArray.length(); i++) {


                    jsonpart = jsonArray.getJSONObject(i);  //helps you get specific values
                    Log.i("NAME", jsonpart.getString("name"));
                    Model m = new Model();
                    m.setName(jsonpart.getString("name"));

                    JSONArray jArray3 = jsonpart.getJSONArray("currencies");
                    JSONArray jArray4 = jsonpart.getJSONArray("languages");
                    for (int k = 0; k < jArray3.length(); k++) {
                        object3 = jArray3.getJSONObject(k);
                        Log.i("Curr", object3.getString("name"));
                        m.setCurrency(object3.getString("name"));


                    }
                    for (int l = 0; l < jArray4.length(); l++) {
                        object4 = jArray4.getJSONObject(l);
                        Log.i("Lang", object4.getString("name"));
                         m.setLanguage(object4.getString("name"));

                    }
                    modelArrayList.add(m);


                }
                return true;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }
        protected void onPreExecute() {
            dialog.setMessage("Loading Info...");
            dialog.setCancelable(true);
            dialog.show();
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.cancel();
            adapter.notifyDataSetChanged();
            super.onPostExecute(aBoolean);
        }
    }

    private void enableSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }



            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
if (direction == ItemTouchHelper.LEFT) {

                    final Model deletedModel = modelArrayList.get(position);
                    final int deletedPosition = position;
                    adapter.removeItem(position);


                } else {
                    final Model deletedModel = modelArrayList.get(position);
                    final int deletedPosition = position;
                    adapter.removeItem(position);

                }
            }





    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        Bitmap icon;
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

            if(dX > 0){
                p.setColor(Color.parseColor("#800080"));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                c.drawRect(background,p);
                icon = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
                RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);
            } else {
                p.setColor(Color.parseColor("#800080"));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background,p);
                icon = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
};
ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        }


        }
