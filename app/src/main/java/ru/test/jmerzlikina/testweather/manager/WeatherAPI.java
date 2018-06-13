package ru.test.jmerzlikina.testweather.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.test.jmerzlikina.testweather.object.WeatherDay;
import ru.test.jmerzlikina.testweather.object.WeatherForecast;

/**
 * Класс для получения данных о погоде с openweathermap.org
 */
public class WeatherAPI {
    public static String KEY = "30293d159198d4e0b8c2d55c0856de75";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static Retrofit retrofit = null;

    public interface ApiInterface {
        @GET("weather")
        Call<WeatherDay> getToday(
            @Query("id") Integer id,
            @Query("units") String units,
            @Query("appid") String appid
        );

        @GET("forecast")
        Call<WeatherForecast> getForecast(
                @Query("id") Integer id,
                @Query("units") String units,
                @Query("appid") String appid
        );
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(new CallAdapter.Factory() {
                        @Override
                        public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
                            return null;
                        }
                    })
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}