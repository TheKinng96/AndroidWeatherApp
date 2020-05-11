package com.example.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText countryName;
    TextView weatherTextView, textView, weatherTextView2;


    public void checkWeather(View view) {
        try {
            DownloadTask task = new DownloadTask();

            String encodedCityName = URLEncoder.encode(countryName.getText().toString(), "UTF-8");
            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=75ebf758ab5a96b3c782cb5955389a20");
            Log.i("button", "clicked");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(weatherTextView.getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not find the data..." , Toast.LENGTH_SHORT).show();
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while ( data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }catch (Exception e){
                e.printStackTrace();

                Toast.makeText(getApplicationContext(), "Could not find the data..." , Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather content", weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);
                String message = "";

                for (int i=0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                    Log.i("Info", main);
                    weatherTextView.setText(main);
                    weatherTextView2.setText(description);
                    if (!message.equals("") && !message.equals("")) {
                        message += main + ": " + description + "\r\n";
                    }
                }

                if (!message.equals("")) {
                    weatherTextView.setText(message);
                }


                textView.setText(countryName.getText().toString());

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not find the data..." , Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryName = findViewById(R.id.countryName);
        weatherTextView = findViewById(R.id.weatherTextView);
        textView = findViewById(R.id.textView);
        weatherTextView2 = findViewById(R.id.weatherTextView2);

    }
}
