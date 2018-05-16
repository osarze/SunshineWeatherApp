package com.example.projectsunshine.osarz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.view.View;

import com.example.projectsunshine.osarz.data.SunshinePreferences;
import com.example.projectsunshine.osarz.utilities.NetworkUtils;
import com.example.projectsunshine.osarz.utilities.OpenWeatherJsonUtils;

import java.net.URL;

public class WeatherLoader extends AsyncTaskLoader<String[]> {
    private Context mContext;
    /* This String array will hold and help cache our weather data */
    private String[] mWeatherData = null;

    public WeatherLoader(@NonNull Context context, String[] weatherData) {
        super(context);
        mWeatherData = weatherData;
        mContext = context;
    }
    /**
     * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
     */
    @Override
    protected void onStartLoading() {
        if (mWeatherData != null) {
            deliverResult(mWeatherData);
        } else {
//            TODo show loading sign
//            mLoadingIndicator.setVisibility(View.VISIBLE);
            forceLoad();
        }
    }

    @Nullable
    @Override
    public String[] loadInBackground() {
        String locationQuery = SunshinePreferences
                .getPreferredWeatherLocation(mContext);

        URL weatherRequestUrl = NetworkUtils.buildUrl(locationQuery);

        try {
            String jsonWeatherResponse = NetworkUtils
                    .getResponseFromHttpUrl(weatherRequestUrl);

            String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                    .getSimpleWeatherStringsFromJson(mContext, jsonWeatherResponse);

            return simpleJsonWeatherData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sends the result of the load to the registered listener.
     *
     * @param data The result of the load
     */
    public void deliverResult(String[] data) {
        mWeatherData = data;
        super.deliverResult(data);
    }

}
