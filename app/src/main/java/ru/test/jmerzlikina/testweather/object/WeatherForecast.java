package ru.test.jmerzlikina.testweather.object;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * данные о погоде на несколько дней
 */
public class WeatherForecast {
    @SerializedName("list")
    private List<WeatherDay> items;

    public WeatherForecast(List<WeatherDay> items) {
        this.items = items;
    }

    public List<WeatherDay> getItems() {
        return items;
    }
}