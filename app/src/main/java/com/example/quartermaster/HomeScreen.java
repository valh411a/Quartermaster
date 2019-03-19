package com.example.quartermaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeScreen.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HomeScreen extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Date date;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar calendar = Calendar.getInstance();
        date = calendar.getTime();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Random random = new Random();

        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        TextView day = view.findViewById(R.id.Day);
        day.setText(new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH).format(date.getTime()));

        Button button = view.findViewById(R.id.homeAppStart);
        button.setOnClickListener(mListener);

//        TextView temperature = view.findViewById(R.id.weatherTemp);
//        temperature.setText(String.valueOf(random.nextInt(100)));
//
//        TextView type = view.findViewById(R.id.weatherType);
//        type.setText("Undefined");
        setWeatherText(view);

        Log.i("fragResponse", "Fragment View Created.");
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public HomeScreen() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    public void setWeatherText(View view){
        TextView temperature = view.findViewById(R.id.weatherTemp);
        TextView type = view.findViewById(R.id.weatherType);
        GetData data = new GetData();
        String weatherData = null;
        try {
            weatherData = data.execute("4473083").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println(weatherData);

        int tempIndex = Objects.requireNonNull(weatherData).indexOf("temp");
        //System.out.println(tempIndex);
        Double temp;
        try {
            temp = Double.valueOf(weatherData.substring(tempIndex+6, tempIndex+12));
        } catch (NumberFormatException e) {
            Log.e("DoubleSubstringTooLong", "Substring Too Long, parsing truncated substring..." + weatherData.substring(tempIndex+6, tempIndex+12) + "->" + weatherData.substring(tempIndex+6, tempIndex+9));
            temp = Double.valueOf(weatherData.substring(tempIndex+6, tempIndex+9));
        }
        temp = (temp - 273.15)*9/5 + 32;
        //System.out.println(temp.intValue());

        temperature.setText(String.valueOf(temp.intValue()) + "\u00B0");

        tempIndex = weatherData.indexOf("weather");
        String weatherDataSub = weatherData.substring(tempIndex);
        tempIndex = weatherDataSub.indexOf("id");
        //System.out.println(tempIndex);
        Integer typeID = Integer.valueOf(weatherDataSub.substring(tempIndex+4, tempIndex+7));
        //System.out.println(typeID);


        switch (typeID) {
            case 200:
                type.setText("Thunderstorm, Light Rain");
            case 201:
                type.setText("Thunderstorm, Moderate Rain");
            case 202:
                type.setText("Thunderstorm, Heavy Rain");
            case 210:
                type.setText("Thunderstorm");
            case 211:
                type.setText("Thunderstorm");
            case 212:
                type.setText("Heavy Thunderstorm");
            case 221:
                type.setText("Isolated Thunderstorms");
            case 230:
                type.setText("Thunderstorm, Light Drizzle");
            case 231:
                type.setText("Thunderstorm, Moderate Drizzle");
            case 232:
                type.setText("Thunderstorm, Rain/Drizzle");
            case 300:
                type.setText("Light Drizzle");
            case 301:
                type.setText("Drizzle");
            case 302:
                type.setText("Heavy Drizzle");
            case 310:
                type.setText("Light Drizzle/Rain");
            case 311:
                type.setText("Drizzle/Rain");
            case 312:
                type.setText("Heavy Drizzle/Rain");
            case 313:
                type.setText("Shower/Drizzle");
            case 314:
                type.setText("Heavy Shower/Drizzle");
            case 321:
                type.setText("Shower/Drizzle");
            case 500:
                type.setText("Light Rain");
            case 501:
                type.setText("Rain");
            case 502:
                type.setText("Heavy Rain");
            case 503:
                type.setText("Very Heavy Rain");
            case 504:
                type.setText("Torrential Downpour");
            case 511:
                type.setText("Freezing Rain");
            case 520:
                type.setText("Light Showers");
            case 521:
                type.setText("Showers");
            case 522:
                type.setText("Heavy Showers");
            case 531:
                type.setText("Isolated Showers");
            case 600:
                type.setText("Flurries");
            case 601:
                type.setText("Snow");
            case 602:
                type.setText("Heavy Snow");
            case 611:
                type.setText("Sleet");
            case 612:
                type.setText("Light Sleet/Rain");
            case 613:
                type.setText("Sleet/Rain");
            case 615:
                type.setText("Light Snow/Rain");
            case 616:
                type.setText("Snow/Rain");
            case 620:
                type.setText("Light Snow/Showers");
            case 621:
                type.setText("Snow/Showers");
            case 622:
                type.setText("Heavy Snow/Showers");
            case 701:
                type.setText("Misty");
            case 711:
                type.setText("Smoky [Warning]");
            case 721:
                type.setText("Hazy");
            case 731:
                type.setText("Sand/Dust [Warning]");
            case 741:
                type.setText("Foggy");
            case 751:
                type.setText("Sandy Air [Warning]");
            case 761:
                type.setText("Dusty [Warning]");
            case 762:
                //REEEEAAAAllY hoping this one never needs to be used...
                type.setText("Volcanic Ash [Warning]");
            case 771:
                type.setText("Temporary Heavy Rain");
            case 781:
                type.setText("Tornado [Warning]");
            case 800:
                type.setText("Clear");
            case 801:
                type.setText("Scattered Clouds");
            case 802:
                type.setText("Partially Cloudy");
            case 803:
                type.setText("Mostly Cloudy");
            case 804:
                type.setText("Cloudy");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener extends View.OnClickListener {

        @Override
        void onClick(View v);
    }

    @SuppressLint("StaticFieldLeak")
    class GetData extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection conn = null;
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?id="+ URLEncoder.encode(params[0], "UTF-8")+"&APPID=5167c48bdd03fe44dcc005400d4b35e4");
                conn = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line;

                while ((line = bufferedReader.readLine()) != null)
                    result.append(line).append("\n");
                in.close();
                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(conn!=null)
                    conn.disconnect();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Fetching Weather Changes...", Toast.LENGTH_SHORT).show();
        }

    }
}
