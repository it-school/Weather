package itschool.weather;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button buttonLoad;
    static String jsonIn, text;
    TextView textView;
    Resources res;
    Main main;
    boolean isDataLoaded;
    boolean isConnected;
    String currWeatherURL;
    Document page = null;
    private String FLAG;
    WeatherGetter wg;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.w1);


        buttonLoad = findViewById(R.id.buttonLoad);
        textView = findViewById(R.id.textView);
        jsonIn = "";//"{\"coord\":{\"lon\":30.73,\"lat\":46.48},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"ясно\",\"icon\":\"01d\"}],\"base\":\"stations\",\"main\":{\"temp\":296.15,\"pressure\":1020,\"humidity\":33,\"temp_min\":296.15,\"temp_max\":296.15},\"visibility\":10000,\"wind\":{\"speed\":3,\"deg\":150},\"clouds\":{\"all\":0},\"dt\":1528381800,\"sys\":{\"type\":1,\"id\":7366,\"message\":0.0021,\"country\":\"UA\",\"sunrise\":1528337103,\"sunset\":1528393643},\"id\":698740,\"name\":\"Odessa\",\"cod\":200}";
        text = "";
        isDataLoaded = false;
        isConnected = true;
        message = "";
        //   currWeatherURL = "http://api.openweathermap.org/data/2.5/weather?id=698740&appid=dac392b2d2745b3adf08ca26054d78c4&lang=ru";
        currWeatherURL = "http://api.openweathermap.org/data/2.5/weather?lat=30&lon=42&appid=dac392b2d2745b3adf08ca26054d78c4&lang=ru";
// repair static properties
        //wg = new WeatherGetter();
        //wg.execute();
    }


    public void ParseWeather() {
        boolean cont = false;
        JSONObject json = null;
        try {
            json = new JSONObject(jsonIn);
            cont = true;
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }

        if (cont)

            try {
                String temp1 = "";
                JSONObject jsonMain = (JSONObject) json.get("main");
                double temp = jsonMain.getDouble("temp") - 273.15;
                int pressure = jsonMain.getInt("pressure");
                int humidity = jsonMain.getInt("humidity");

                SimpleDateFormat sm = new SimpleDateFormat("d.M.Y H:m");  // https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
                sm.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                Date date = new Date(json.getLong("dt") * 1000);

                JSONArray jsonWeather = (JSONArray) json.get("weather");
                String description = jsonWeather.getJSONObject(0).getString("description");

                JSONObject jsonWind = (JSONObject) json.get("wind");
                int speed = jsonWind.getInt("speed");
                int deg = jsonWind.getInt("deg");

                JSONObject jsonClouds = (JSONObject) json.get("clouds");
                int clouds = jsonClouds.getInt("all");

                String name = json.getString("name");

                main = new Main(temp, pressure, humidity, date, description, speed, deg, clouds, name);
                isDataLoaded = true;
            } catch (JSONException e) {
                e.printStackTrace();
                //drawWeather();
            }
        textView.setText(main.toString());
        drawWeather();
    }

    public void btnLoadData(View view) {

        currWeatherURL = "http://api.openweathermap.org/data/2.5/weather?lat=30&lon=42&appid=dac392b2d2745b3adf08ca26054d78c4&lang="+R.string.lang;
        Log.d("", currWeatherURL);

        wg = new WeatherGetter();
        wg.execute();

        ParseWeather();
        drawWeather();
    }

    public void drawWeather() {

            if (main.getClouds() < 5) {
                imageView.setImageResource(R.drawable.transparent);
            } else if (main.getClouds() < 25) {
                imageView.setImageResource(R.drawable.cloud1);
            } else if (main.getClouds() < 50) {
                imageView.setImageResource(R.drawable.cloud2);
            } else if (main.getClouds() < 75) {
                imageView.setImageResource(R.drawable.cloud3);
            } else
                imageView.setImageResource(R.drawable.cloud4);

            imageView.setBackgroundResource(R.drawable.sun);

    }

}
