package ai.axcess.productscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int ALL_PERMISSIONS = 102;
    SharedPreferences sharedpreferences;
    private EditText isles;
    private EditText shopid;
    int autoSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


        isles = (EditText) findViewById(R.id.isle);
        shopid = (EditText) findViewById(R.id.shopid);
        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedpreferences.getInt("key", 0);


        if(!hasPermissions(this, permissions)){
            ActivityCompat.requestPermissions(this, permissions, ALL_PERMISSIONS);
        }


        Button startup = (Button) findViewById(R.id.startid);


        startup.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Log.d("ckil", "button click");

                String islenumb = isles.getText().toString();
                String thisshop = shopid.getText().toString();

                if (islenumb.matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter working Isle no:", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (thisshop.matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter shop ID:", Toast.LENGTH_SHORT).show();
                    return;
                }


                autoSave = 1;
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("keepisles", islenumb);
                editor.putString("theshop", thisshop);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, Inventoryadmin.class);
                intent.putExtra("passthis","");
                intent.putExtra("thisbarcode","");
                intent.putExtra("thistitle","");
                intent.putExtra("thisdescript","");
                intent.putExtra("thiscat","");



                startActivity(intent);

            }
        });






    }



    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



}