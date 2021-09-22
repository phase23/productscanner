package ai.axcess.productscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.device.ScanDevice;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Inventoryadmin extends AppCompatActivity {

        ScanDevice sm;
        private final static String SCAN_ACTION = "scan.rcv.message";
        private String barcodeStr;
        private EditText recievedbarcode;
        private EditText ptitle;
        private EditText descript;
        private EditText category;
        private EditText image;
        private EditText ccount;
        TextView scanr;
        TextView showisle;
        Button takepic;
        Button scan;
        Button addstock;
        String imagepath;
        String postaction;
        String thistitle;
        String thisdescript;
        String thiscat;
        String thiscount;
        AlertDialog dialog;
        public String thisbarcode;
        String pullisles;
    String pullshop;
    String getthistitle;
    String getthisdescript ;
    String getthiscat;
    String getimagepath ;
    String getthiscount ;


    Button logout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventoryadmin);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


            logout = (Button) findViewById(R.id.logout);
            scan = (Button) findViewById(R.id.scan);
            takepic = (Button) findViewById(R.id.takepic);
            addstock = (Button) findViewById(R.id.addstock);

            imagepath = getIntent().getExtras().getString("passthis");
            thisbarcode = getIntent().getExtras().getString("thisbarcode");
            thistitle = getIntent().getExtras().getString("thistitle");
            thisdescript = getIntent().getExtras().getString("thisdescript");
            thiscat = getIntent().getExtras().getString("thiscat");


            SharedPreferences shared = getSharedPreferences("autoLogin", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = shared.edit();
            pullisles = shared.getString("keepisles", "");
            pullshop = shared.getString("theshop", "");

            scanr = (TextView) findViewById(R.id.scanr);
            scanr.setText("ready..");

            showisle = (TextView) findViewById(R.id.showisle);
            showisle.setText("Isle no : " + pullisles);

            recievedbarcode = (EditText) findViewById(R.id.barcode);
            ptitle = (EditText) findViewById(R.id.ptitle);
            descript = (EditText) findViewById(R.id.descript);
            category = (EditText) findViewById(R.id.category);
            image = (EditText) findViewById(R.id.image);
            ccount = (EditText) findViewById(R.id.count);

            image.setEnabled(false);

            ptitle.setText(thistitle);
            descript.setText(thisdescript);
            category.setText(thiscat);
            image.setText(imagepath);


            //set texttobarcodes
            recievedbarcode.setText(thisbarcode);





            addstock.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    Log.d("ckil", "button click");

                     getthistitle = ptitle.getText().toString();
                     getthisdescript = descript.getText().toString();
                     getthiscat = category.getText().toString();
                     getimagepath = image.getText().toString();
                     getthiscount = ccount.getText().toString();


                    if (getthistitle.matches("")) {
                        Toast.makeText(getApplicationContext(), "You have to Enter a title", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (getthiscat.matches("")) {
                        Toast.makeText(getApplicationContext(), "You have to Enter a category", Toast.LENGTH_SHORT).show();
                        return;
                    }



                    if (getimagepath.matches("")) {
                        Toast.makeText(getApplicationContext(), "You have to take a picture:", Toast.LENGTH_SHORT).show();
                        return;
                    }




                    dialog = new SpotsDialog.Builder()
                            .setMessage("Adding Wait...")
                            .setContext(Inventoryadmin.this)
                            .build();
                    dialog.show();



                    try {



                        Log.i("[print]","https://axcess.ai/barapp/shopper_addproduct.php?&barcode=" + barcodeStr +   "&barcode=" + barcodeStr + "&getthistitle=" + getthistitle + "&getthisdescript=" + getthisdescript + "&getthiscat="+getthiscat + "&getimagepath="+getimagepath + "&getthiscount=" + getthiscount);
                       // doaddproduct("https://axcess.ai/barapp/shopper_addproduct.php?&barcode=" + barcodeStr +   "&barcode=" + barcodeStr + "&getthistitle=" + getthistitle + "&getthisdescript=" + getthisdescript + "&getthiscat="+getthiscat + "&getimagepath="+getimagepath + "&getthiscount=" + getthiscount);
                        doaddproduct("https://axcess.ai/barapp/shopper_addproduct.php");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }







                }
            });





            logout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Log.d("ckil", "button click");

                Intent intent = new Intent(Inventoryadmin.this, MainActivity.class);
                startActivity(intent);

            }
        });






            takepic.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    Log.d("ckil", "button click");
                   // unregisterReceiver(mScanReceiver);


                     thistitle = ptitle.getText().toString();
                     thisdescript = descript.getText().toString();
                     thiscat = category.getText().toString();


                    Intent intent = new Intent(Inventoryadmin.this, Startcamera.class);
                    intent.putExtra("thisbarcode",barcodeStr);
                    intent.putExtra("thistitle",thistitle);
                    intent.putExtra("thisdescript",thisdescript);
                    intent.putExtra("thiscat",thiscat);


                    startActivity(intent);




                }
            });



            scan.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    Log.d("ckil", "button click");

                    sm = new ScanDevice();
                    sm.setOutScanMode(0);//启动就是广播模式

                    sm.openScan();
                    sm.startScan();

                }
            });



        }









        private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("ckil", "iji");
                byte[] barocode = intent.getByteArrayExtra("barocode");
                int barocodelen = intent.getIntExtra("length", 0);
                byte temp = intent.getByteExtra("barcodeType", (byte) 0);
                byte[] aimid = intent.getByteArrayExtra("aimid");
                barcodeStr = new String(barocode, 0, barocodelen);
                sm.stopScan();

                Log.d("ckil", barcodeStr);
                //Toast.makeText(getApplicationContext(), "barcode: " + barcodeStr, Toast.LENGTH_LONG).show();
                recievedbarcode.setText(barcodeStr);
                //recievedbarcode.append("\n");

                try {


                    Log.i("[print]", "https://axcess.ai/barapp/barcodelookup.php?upc=" + barcodeStr );
                    doScanupc("https://axcess.ai/barapp/barcodelookup.php?upc=" + barcodeStr );
                } catch (IOException e) {
                    e.printStackTrace();
                }




            }
        };


    void doaddproduct(String url) throws IOException {


        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("getthistitle", getthistitle)
                .addFormDataPart("getthisdescript", getthisdescript)
                .addFormDataPart("getthiscat", getthiscat)
                .addFormDataPart("getimagepath", getimagepath)
                .addFormDataPart("barcodeStr", barcodeStr)
                .addFormDataPart("pullshop", pullshop)

                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // For the example, you can show an error dialog or a toast
                                // on the main UI thread
                                Log.i("[print]","error" + e);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        postaction = response.body().string();
                        Log.i("assyn url",postaction);
                        // Do something with the response


                        Log.i("[print]",postaction);
                        postaction = postaction.trim();


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // For the example, you can show an error dialog or a toast
                                // on the main UI thread
                                ptitle.setEnabled(true);
                                descript.setEnabled(true);
                                category.setEnabled(true);
                                scanr.setText("Found");
                                ptitle.setText("");
                                descript.setText("");
                                category.setText("");
                                ccount.setText("");
                                image.setText("");
                                recievedbarcode.setText("");

                            }
                        });



                        dialog.dismiss();

                    }
                });
    }







    void doScanupc(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // For the example, you can show an error dialog or a toast
                                // on the main UI thread
                                Log.i("[print]","error" + e);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        postaction = response.body().string();
                        Log.i("assyn url",postaction);
                        // Do something with the response


                        Log.i("[print]",postaction);
                        postaction = postaction.trim();

                        String[] pieces = postaction.split(Pattern.quote("~"));

                        String outtitle = pieces[1].trim();
                        String outcat = pieces[2].trim();
                        String outdescript = pieces[3].trim();
                        String outimage = pieces[4].trim();


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // For the example, you can show an error dialog or a toast
                                // on the main UI thread
                                ptitle.setEnabled(true);
                                descript.setEnabled(true);
                                category.setEnabled(true);


                                if(!outtitle.equals("Notfound")) {

                                    scanr.setText("Found");
                                    ptitle.setText(outtitle);
                                    descript.setText(outdescript);
                                    category.setText(outcat);
                                    image.setText(outimage);

                                    ptitle.setEnabled(false);
                                    descript.setEnabled(false);
                                    category.setEnabled(false);


                                }else{


                                    scanr.setText("Not Found");

                                    ptitle.setText("");
                                    descript.setText(outdescript);
                                    category.setText(outcat);
                                    image.setText(outimage);

                                    takepic.setVisibility(View.VISIBLE);
                                    //scan.setVisibility(View.INVISIBLE);


                                }

                            }
                        });





                    }
                });
    }



    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mScanReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sm != null) {
            sm.stopScan();
            sm.setScanLaserMode(8);
            sm.closeScan();
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        View focusedView = this.getCurrentFocus();

        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        return true;
    }



}
