package com.example.meteoservice;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    ImageView imageView;
    EditText editText;
    Button button;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(receiver, new IntentFilter("MeteoService"), RECEIVER_EXPORTED);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MeteoService.class);
        startService(intent);
        textView = findViewById(R.id.temp);
        imageView = findViewById(R.id.imageView);
        editText = findViewById(R.id.city);
        button = findViewById(R.id.button);
        textView2 = findViewById(R.id.feelstemp);
        textView3 = findViewById(R.id.bar);
        textView4 = findViewById(R.id.wind);
        textView5 = findViewById(R.id.cityname);
        LinearLayout layout = findViewById(R.id.layout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView5.setText(editText.getText().toString());
                HttpsRequest.CITY = editText.getText().toString();
                Intent intent = new Intent(MainActivity.this, MeteoService.class);
                startService(intent);
            }
        });
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("RESULT", intent.getStringExtra("INFO"));
            String str = intent.getStringExtra("INFO");
            try {
                JSONObject start = new JSONObject(str);
                JSONObject current = start.getJSONObject("current");
                JSONObject condition = current.getJSONObject("condition");
                String status = condition.getString("text");
                if (status.equals("Sunny") || status.equals("Clear")){
                    imageView.setImageResource(R.drawable.sunny);
                }
                if (status.equals("Partly cloudy")){
                    imageView.setImageResource(R.drawable.partly_cloudy);
                }
                if (status.equals("Cloudy")){
                    imageView.setImageResource(R.drawable.cloudy);
                }
                if (status.equals("Overcast")){
                    imageView.setImageResource(R.drawable.overcast);
                }
                if (status.equals("Mist")){
                    imageView.setImageResource(R.drawable.mist);
                }
                if (status.equals("Patchy rain possible") || status.equals("Patchy light rain") || status.equals("Light rain") || status.equals("Moderate rain at times") || status.equals("Moderate rain") || status.equals("Heavy rain at times") || status.equals("Heavy rain") || status.equals("Light freezing rain") || status.equals("Moderate or heavy freezing rain") || status.equals("Light rain shower") || status.equals("Moderate or heavy rain shower") || status.equals("Torrential rain shower") || status.equals("Light rain shower") || status.equals("Moderate or heavy rain shower") || status.equals("Torrential rain shower") || status.equals("Light sleet showers") || status.equals("Moderate or heavy sleet showers") || status.equals("Light snow showers") || status.equals("Moderate or heavy snow showers") || status.equals("Light showers of ice pellets") || status.equals("Moderate or heavy showers of ice pellets") || status.equals("Patchy light rain with thunder") || status.equals("Moderate or heavy rain with thunder") || status.equals("Patchy light snow with thunder") || status.equals("Moderate or heavy snow with thunder")){
                    imageView.setImageResource(R.drawable.rain);
                }
                if (status.equals("Patchy snow possible") || status.equals("Patchy sleet possible") || status.equals("Blowing snow") || status.equals("Blizzard") || status.equals("Patchy light snow") || status.equals("Light snow") || status.equals("Patchy moderate snow") || status.equals("Moderate snow") || status.equals("Patchy heavy snow") || status.equals("Heavy snow") || status.equals("Ice pellets") || status.equals("Light sleet") || status.equals("Moderate or heavy sleet") || status.equals("Patchy light snow with thunder") || status.equals("Moderate or heavy snow with thunder")){
                    imageView.setImageResource(R.drawable.snow);
                }
                double temp = current.getDouble("temp_c");
                double feelslike = current.getDouble("feelslike_c");
                textView.setText(temp+"°C");
                textView2.setText("Feels like "+feelslike+"°C");
                double pressure = current.getDouble("pressure_mb");
                textView3.setText("Pressure "+pressure+" mb");
                double wind = current.getDouble("wind_kph");
                textView4.setText("Wind "+wind+" kph");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this, MeteoService.class);
        stopService(intent);
    }
}