package university4credit.universitygear;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class SearchActivity2 extends Activity implements LocationListener {
    ToggleButton button1,button2,button3;
    ArrayList<SchoolList> list = new ArrayList<SchoolList>();
    double mindistance = 99999999999.;
    int min;
    Button mbutton, mbutton2;
    EditText edit;
    int temp1, temp2, temp3;
    String schoolname = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        if(ContextCompat.checkSelfPermission(SearchActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SearchActivity2.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        TrackGPS gps = new TrackGPS(SearchActivity2.this);
        SchoolList list = gps.getSchoolList();

                // Called when a new location is found by the network location provider.


        button1 = (ToggleButton)findViewById(R.id.toggleButton);
        button2 = (ToggleButton)findViewById(R.id.toggleButton2);
        button3 = (ToggleButton)findViewById(R.id.toggleButton3);
        button1.setTextOn(list.Schools[0]);
        button2.setTextOn(list.Schools[1]);
        button3.setTextOn(list.Schools[2]);
        button1.setText(list.Schools[0]);
        button2.setText(list.Schools[1]);
        button3.setText(list.Schools[2]);
        button1.setTextOff(list.Schools[0]);
        button2.setTextOff(list.Schools[1]);
        button3.setTextOff(list.Schools[2]);
        mbutton = (Button)findViewById(R.id.nextbutton);
        mbutton2 = (Button)findViewById(R.id.nextbutton2);
        edit = (EditText)findViewById(R.id.editText1);

        mbutton2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                schoolname = "";
                if(button1.isChecked()){
                    schoolname+=button1.getText().toString();
                    schoolname+=" ";
                }
                if(button2.isChecked()){
                    schoolname+=button2.getText().toString();
                    schoolname+=" ";
                }
                if(button3.isChecked()){
                    schoolname+=button3.getText().toString();
                    schoolname+=" ";
                }
                Intent intent = new Intent(v.getContext(),SearchActivity.class);
                Log.d("schoolname", schoolname);
                intent.putExtra("asdf",schoolname);
                startActivity(intent);
            }
        });
        mbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),SearchActivity.class);
                Log.d("schoolname2", edit.getText().toString());
                intent.putExtra("asdf",edit.getText().toString());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
