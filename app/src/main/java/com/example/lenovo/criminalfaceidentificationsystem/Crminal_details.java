package com.example.lenovo.criminalfaceidentificationsystem;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Crminal_details extends AppCompatActivity implements View.OnClickListener{
  String  police_phone;
    String loc="";
    String loc1="";
    EditText number;
    public FusedLocationProviderClient client;
    public DatabaseReference mDatabase;
    @Override
    public void onClick(View v) {
        requestPermission();
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        client = LocationServices.getFusedLocationProviderClient(this);
        onSend();

    }

    private TextView tv1,tv2,tv3,tv4,tv5,tv6;
    private ImageView img;
    private Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crminal_details);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        tv6 = (TextView) findViewById(R.id.tv6);
        img = (ImageView) findViewById(R.id.cimage);

        send = (Button) findViewById(R.id.send);

        send.setOnClickListener(this);

        // Recieve data
        Intent intent = getIntent();
        String Name = intent.getExtras().getString("Name");
        String nbw = intent.getExtras().getString("NBW");
        String loc= intent.getExtras().getString("LOC");
        String rcn = intent.getExtras().getString("RCN");
        String Description = intent.getExtras().getString("Description");
        String locality = intent.getExtras().getString("locality");
        String police_name = intent.getExtras().getString("police_name");
        police_phone = intent.getExtras().getString("police_phone");
        String Crimeid = intent.getExtras().getString("crime_id");
        String image = intent.getExtras().getString("Image") ;
        Picasso.get()
                .load(image)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(img);
        // Setting values

        tv1.setText(Name);
        tv2.setText(nbw);
        tv3.setText(loc);
        tv4.setText(rcn);
        tv5.setText(Description);
        tv6.setText(locality);

    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }


    public void onSend(){
        Toast.makeText(this, "clicked and in send", Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(Crminal_details.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            return;
        }

        client.getLastLocation().addOnSuccessListener(Crminal_details.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location!= null){//Toast.makeText(MainActivity.this, "sadsd", Toast.LENGTH_SHORT).show();
                    loc = location.toString();
                    Toast.makeText(Crminal_details.this, ""+loc, Toast.LENGTH_LONG).show();
                    loc1=loc.substring(15,34);
                    mDatabase = FirebaseDatabase.getInstance().getReference("Users-location");
                    mDatabase.child("u").setValue(loc1);
                    String phoneNumber =police_phone;
                    String smsMessage;
                    smsMessage="https://www.google.com/maps?q="+loc1;
                    Toast.makeText(Crminal_details.this, ""+phoneNumber, Toast.LENGTH_LONG).show();
                    if(phoneNumber == null || phoneNumber.length() == 0 ||
                            smsMessage == null || smsMessage.length() == 0){
                        return;
                    }

                    if(checkPermission(permission.SEND_SMS)){
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
                        Toast.makeText(Crminal_details.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Crminal_details.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }


    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}
