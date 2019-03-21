package com.example.quartermaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
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
        setFullscreen(Objects.requireNonNull(getActivity()));

    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Random random = new Random();

        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        TextView day = view.findViewById(R.id.Day);
        day.setText(new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH).format(date.getTime()));

        ImageButton button = view.findViewById(R.id.homeAppStart);
        button.setOnClickListener(mListener);

//        TextView temperature = view.findViewById(R.id.weatherTemp);
//        temperature.setText(String.valueOf(random.nextInt(100)));
//
//        TextView type = view.findViewById(R.id.weatherType);
//        type.setText("Undefined");

//        FragHolder  parentActivity = (FragHolder) getActivity();
//        if (parentActivity != null && parentActivity.getCityString() != null) {
//            System.out.println("parentActivity != null");
//            cityNum = parentActivity.getCityString();
//        } else
        String cityNum;
        if (this.getArguments() != null && this.getArguments().getString("cityID") != null) {
            cityNum = this.getArguments().getString("cityID");
        } else {
            Log.e("noID", "No Valid City ID found when creating the view. Defaulting to city ID 4473083.");
            cityNum = "4473083";
        }

//        System.out.println("cityID = " + cityNum);
        setWeatherText(view, cityNum);

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
    private void setWeatherText(View view, String cityID) {
        TextView temperature = view.findViewById(R.id.weatherTemp);
        TextView type = view.findViewById(R.id.weatherType);
        GetData data = new GetData();
        String weatherData = null;
        try {
            weatherData = data.execute(cityID).get();
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
        } catch (StringIndexOutOfBoundsException e) {
            Log.e("String Out of Bounds", weatherData);
            temp = 810.372;
        }
        temp = (temp - 273.15)*9/5 + 32;
        //System.out.println(temp.intValue());

        temperature.setText(String.valueOf(temp.intValue()) + "\u00B0");

        int typeID;
        try {
            tempIndex = weatherData.indexOf("weather");
            String weatherDataSub = weatherData.substring(tempIndex);
            tempIndex = weatherDataSub.indexOf("id");
            //System.out.println(tempIndex);
            typeID = Integer.parseInt(weatherDataSub.substring(tempIndex + 4, tempIndex + 7));
        } catch (StringIndexOutOfBoundsException e) {
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Debug Mode Active, enter a valid city ID to reset.", Toast.LENGTH_SHORT).show();
            typeID = 762;
        }
        System.out.println("Weather type ID: " + typeID);

        switch (typeID) {
            case 200:
                type.setText("Thunderstorm, Light Rain");
                break;
            case 201:
                type.setText("Thunderstorm, Moderate Rain");
                break;
            case 202:
                type.setText("Thunderstorm, Heavy Rain");
                break;
            case 210:
                type.setText("Thunderstorm");
                break;
            case 211:
                type.setText("Thunderstorm");
                break;
            case 212:
                type.setText("Heavy Thunderstorm");
                break;
            case 221:
                type.setText("Isolated Thunderstorms");
                break;
            case 230:
                type.setText("Thunderstorm, Light Drizzle");
                break;
            case 231:
                type.setText("Thunderstorm, Moderate Drizzle");
                break;
            case 232:
                type.setText("Thunderstorm, Rain/Drizzle");
                break;
            case 300:
                type.setText("Light Drizzle");
                break;
            case 301:
                type.setText("Drizzle");
                break;
            case 302:
                type.setText("Heavy Drizzle");
                break;
            case 310:
                type.setText("Light Drizzle/Rain");
                break;
            case 311:
                type.setText("Drizzle/Rain");
                break;
            case 312:
                type.setText("Heavy Drizzle/Rain");
                break;
            case 313:
                type.setText("Shower/Drizzle");
                break;
            case 314:
                type.setText("Heavy Shower/Drizzle");
                break;
            case 321:
                type.setText("Shower/Drizzle");
                break;
            case 500:
                type.setText("Light Rain");
                break;
            case 501:
                type.setText("Rain");
                break;
            case 502:
                type.setText("Heavy Rain");
                break;
            case 503:
                type.setText("Very Heavy Rain");
                break;
            case 504:
                type.setText("Torrential Downpour");
                break;
            case 511:
                type.setText("Freezing Rain");
                break;
            case 520:
                type.setText("Light Showers");
                break;
            case 521:
                type.setText("Showers");
                break;
            case 522:
                type.setText("Heavy Showers");
                break;
            case 531:
                type.setText("Isolated Showers");
                break;
            case 600:
                type.setText("Flurries");
                break;
            case 601:
                type.setText("Snow");
                break;
            case 602:
                type.setText("Heavy Snow");
                break;
            case 611:
                type.setText("Sleet");
                break;
            case 612:
                type.setText("Light Sleet/Rain");
                break;
            case 613:
                type.setText("Sleet/Rain");
                break;
            case 615:
                type.setText("Light Snow/Rain");
                break;
            case 616:
                type.setText("Snow/Rain");
                break;
            case 620:
                type.setText("Light Snow/Showers");
                break;
            case 621:
                type.setText("Snow/Showers");
                break;
            case 622:
                type.setText("Heavy Snow/Showers");
                break;
            case 701:
                type.setText("Misty");
                break;
            case 711:
                type.setText("Smoky [Warning]");
                break;
            case 721:
                type.setText("Hazy");
                break;
            case 731:
                type.setText("Sand/Dust [Warning]");
                break;
            case 741:
                type.setText("Foggy");
                break;
            case 751:
                type.setText("Sandy Air [Warning]");
                break;
            case 761:
                type.setText("Dusty [Warning]");
                break;
            case 762:
                //REEEEAAAAllY hoping this one never needs to be used...
                type.setText("Volcanic Ash [Warning]");
                break;
            case 771:
                type.setText("Temporary Heavy Rain");
                break;
            case 781:
                type.setText("Tornado [Warning]");
                break;
            case 800:
                type.setText("Clear");
                break;
            case 801:
                type.setText("Scattered Clouds");
                break;
            case 802:
                type.setText("Partially Cloudy");
                break;
            case 803:
                type.setText("Mostly Cloudy");
                break;
            case 804:
                type.setText("Cloudy");
                break;
            default:
                type.setText("Unassigned");
                break;
        }
    }

    private void setFullscreen(Activity activity) {
        int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
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
//            params = new String[1];
//            params[0] = cityNum;
//            System.out.println("Params pre Null = " + Arrays.toString(params));
            try {
                URL url = null;
                try {
                    url = new URL("http://api.openweathermap.org/data/2.5/weather?id="+ URLEncoder.encode(params[0], "UTF-8")+"&APPID=5167c48bdd03fe44dcc005400d4b35e4");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    System.out.println("Params post Null" + Arrays.toString(params));
                    e.printStackTrace();
                }
                conn = (HttpURLConnection) Objects.requireNonNull(url).openConnection();
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
//            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Fetching Weather Changes...", Toast.LENGTH_SHORT).show();
        }

    }
}
