package com.sjcoders.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static ProgressBar progressBar1,progressBar2,progressBar3,progressBar4;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    private static Context context;
    private Button btnTrackOrder,btnReportIssue;
    private ImageButton btnBack;
    private TextView textViewOrderLocation;
    private static TextView textViewProgress;
    private Toolbar toolbar;
    private static ArrayList<Pair<ImageView,Integer>> pairArrayList;
    private static ArrayList<String> orderStatusArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        context = OrderDetailsActivity.this;
        textViewOrderLocation = findViewById(R.id.textViewLocationDetails);
        btnTrackOrder = findViewById(R.id.btnTrackOrder);
        btnReportIssue = findViewById(R.id.btnReportIssue);
        btnBack = findViewById(R.id.btnBack);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar3 = findViewById(R.id.progressBar3);
        progressBar4 = findViewById(R.id.progressBar4);
        imageView1 = findViewById(R.id.imageViewOrderIconStatus1);
        imageView2 = findViewById(R.id.imageViewOrderIconStatus2);
        imageView3 = findViewById(R.id.imageViewOrderIconStatus3);
        imageView4 = findViewById(R.id.imageViewOrderIconStatus4);
        imageView5 = findViewById(R.id.imageViewOrderIconStatus5);
        textViewProgress =findViewById(R.id.textViewProgress);
        btnTrackOrder.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnReportIssue.setClickable(false);

        progressBar1.setMax(100);
        progressBar2.setMax(100);
        progressBar3.setMax(100);
        progressBar4.setMax(100);

        pairArrayList = new ArrayList<>();
        orderStatusArrayList = new ArrayList<>();
        pairArrayList.add(Pair.create(imageView1,R.drawable.ic_baseline_fastfood_24));
        pairArrayList.add(Pair.create(imageView2,R.drawable.ic_baseline_picked_24));
        pairArrayList.add(Pair.create(imageView3,R.drawable.ic_baseline_on_way_24));
        pairArrayList.add(Pair.create(imageView4,R.drawable.ic_baseline_done_24));
        pairArrayList.add(Pair.create(imageView5,R.drawable.ic_baseline_done_all_24));

        orderStatusArrayList.add("Preparing Dinner.");
        orderStatusArrayList.add("Order picked up.");
        orderStatusArrayList.add("Delivery guy on the way.");
        orderStatusArrayList.add("Order delivered ");
        orderStatusArrayList.add("Order Done");

        AsyncTask<Void, Integer, Integer> orderProgressTask = AsyncOrderProgressTask.getInstance(OrderDetailsActivity.this);//new AsyncOrderProgressTask(OrderDetailsActivity.this);
        Log.d("TASKORDER","requesting task");
        if(orderProgressTask!=null){
            Log.d("TASKORDER","started task");
            orderProgressTask.execute();
        }else{
            Log.d("TASKORDER","DENIED");
        }


    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btnTrackOrder){

            startGoogleMaps();

        }
        else if(v.getId()==R.id.btnBack){

            finish();

        }
    }

    public void startGoogleMaps(){

        String locationQuery = textViewOrderLocation.getText().toString().trim();
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(locationQuery));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }


    public static class AsyncOrderProgressTask extends AsyncTask<Void,Integer,Integer> {


        Activity mContext = null;
        static AsyncTask<Void,Integer,Integer> myAsyncTaskInstance = null;

        private AsyncOrderProgressTask(Activity iContext) {
            mContext = iContext;
        }

        public static AsyncTask<Void, Integer, Integer> getInstance(Activity iContext) {

            if (myAsyncTaskInstance != null && myAsyncTaskInstance.getStatus() == Status.RUNNING) {

                if (myAsyncTaskInstance.isCancelled()) {
                    myAsyncTaskInstance = new AsyncOrderProgressTask(iContext);
                } else {
                    return null;
                }
            }

            if (myAsyncTaskInstance != null && myAsyncTaskInstance.getStatus() == Status.PENDING) {
                return myAsyncTaskInstance;
            }

            if (myAsyncTaskInstance != null && myAsyncTaskInstance.getStatus() == Status.FINISHED) {
                myAsyncTaskInstance = new AsyncOrderProgressTask(iContext);
            }

            if (myAsyncTaskInstance == null) {
                myAsyncTaskInstance = new AsyncOrderProgressTask(iContext);
            }
            return myAsyncTaskInstance;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            changeOrderStatusIcons(4);
            Toast.makeText(mContext, "Order Done!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("valueOrder","-> "+values[0]);
            if(values[0]>=0 && values[0]<100)
                progressBar1.setProgress(values[0]);
            else if(values[0]>=100 && values[0]<200) {
                progressBar1.setProgress(100);
                changeOrderStatusIcons(1);
                if(values[0]==100)
                    changeOrderStatusIcons(1);
                progressBar2.setProgress(values[0] - 100);
            }
            else if(values[0]>=200 && values[0]<300) {
                progressBar1.setProgress(100);
                progressBar2.setProgress(100);
                changeOrderStatusIcons(1);
                changeOrderStatusIcons(2);
                if(values[0]==200)
                    changeOrderStatusIcons(2);
                progressBar3.setProgress(values[0] - 200);
            }
            else {
                progressBar1.setProgress(100);
                progressBar2.setProgress(100);
                progressBar3.setProgress(100);
                changeOrderStatusIcons(1);
                changeOrderStatusIcons(2);
                changeOrderStatusIcons(3);
                if(values[0]==300)
                    changeOrderStatusIcons(3);
                progressBar4.setProgress(values[0] - 300);
            }
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            
            for(int i=0;i<400;i++){
                publishProgress(i);
                try 
                { 
                    Thread.sleep(25);
                    
                } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
            
            return null;
        }
    }

    public static void changeOrderStatusIcons(int position){

        ImageView imageView = pairArrayList.get(position).first;
        textViewProgress.setText(orderStatusArrayList.get(position));
        int resId = pairArrayList.get(position).second;
        imageView.setBackgroundResource(R.drawable.border_circle);
        imageView.setImageResource(resId);
        imageView.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen.circle_icon);
        imageView.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen.circle_icon);
        int padding = (int) context.getResources().getDimension(R.dimen.circle_padding);
        imageView.setPadding(padding,padding,padding,padding);
        imageView.requestLayout();

    }

}

