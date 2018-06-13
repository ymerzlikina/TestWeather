package ru.test.jmerzlikina.testweather.object;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import ru.test.jmerzlikina.testweather.db.SelectedLocation;

/**
 * контейнер выбранной локации (выбранная локация + данные о погоде)
 */
public class LocationContainer {
    // выбранная локация
    private SelectedLocation location;
    // температура
    private String temp;
    // ссылка на иконку с картинкой погоды
    private String iconUrl;
    // погода на следующие дни
    private List<WeatherDay> dayItems;

    public LocationContainer(SelectedLocation location) {
        this.location = location;
        this.dayItems = new ArrayList<>();
        this.temp = "";
        this.iconUrl = "";
    }


    public SelectedLocation getLocation() {
        return location;
    }

    public String getTemp() {
        return temp;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setDayItems(List<WeatherDay> dayItemsAll) {
        this.dayItems.clear();

        for (WeatherDay day : dayItemsAll) {
            if (day.getDate().get(Calendar.HOUR_OF_DAY) == 12) {
                this.dayItems.add(day);
            }
        }
    }

    public List<WeatherDay> getDayItems() {
        return dayItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationContainer container = (LocationContainer) o;
        return Objects.equals(location, container.location);
    }

    @Override
    public int hashCode() {

        return Objects.hash(location);
    }
}
