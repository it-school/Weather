package itschool.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import static android.support.v4.content.ContextCompat.getSystemService;

class WeatherGetter extends AsyncTask<Void, Void, Void>
{
    String currWeatherURL = "http://api.openweathermap.org/data/2.5/weather?id=698740&appid=dac392b2d2745b3adf08ca26054d78c4&lang=ru";
    //String urlForecast = "api.openweathermap.org/data/2.5/forecast?id=698740&appid=dac392b2d2745b3adf08ca26054d78c4&lang=ru";

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public void ConnectAndGetData(String url)
    {
        InputStream is = null;


        try {
            is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                try {
                    MainActivity.jsonIn = readAll(rd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ConnectAndGetData(currWeatherURL);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //ParseWeather();
    }

    @Override
    protected void onCancelled()
    {
        super.onCancelled();
        Log.d("", "Process cancelled");
    }
}