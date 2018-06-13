package ru.test.jmerzlikina.testweather.manager;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.test.jmerzlikina.testweather.WeatherApplication;
import ru.test.jmerzlikina.testweather.db.Location;
import ru.test.jmerzlikina.testweather.db.SelectedLocation;
import ru.test.jmerzlikina.testweather.object.LocationJsonObject;
import ru.test.jmerzlikina.testweather.object.LocationContainer;
import ru.test.jmerzlikina.testweather.object.WeatherDay;
import ru.test.jmerzlikina.testweather.object.WeatherForecast;
import ru.test.jmerzlikina.testweather.receiver.NetworkChangeReceiver;
import ru.test.jmerzlikina.testweather.util.JsonUtil;

public class WeatherManager {

    // broadcastreceiver для получения данных о подключении с сети
    private NetworkChangeReceiver networkReceiver;
    // база данных (room)
    private AppDatabase database;
    // контест приложения
    private Context context;
    // класс для работы c sharedpreference
    private AppSharedPreference appSharedPreference;
    // класс для получения данных по погоде
    private WeatherAPI.ApiInterface weatherApi;

    // livedata для обновления списка выбранных локациц
    private MutableLiveData<List<LocationContainer>> recordLiveData;
    // livedata для обновления списка доступных локациц
    private MutableLiveData<List<Location>> locationLiveData;
    // хранилище доступных локаций
    private LinkedHashMap<Integer, LocationContainer> containerList = new LinkedHashMap<>();


    public WeatherManager(WeatherApplication weatherApplication) {

        recordLiveData = new MutableLiveData<>();
        locationLiveData = new MutableLiveData<>();

        weatherApi = WeatherAPI.getClient().create(WeatherAPI.ApiInterface.class);

        database =  Room.databaseBuilder(weatherApplication, AppDatabase.class, "database").build();
        database.selectedLocationDao().getAll().subscribe(consumerSelected);
        database.locationDao().getAll().subscribe(consumerLocation);

        appSharedPreference = new AppSharedPreference(weatherApplication);
        context = weatherApplication;

        networkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcast();

        prepareDatabase();
    }


    private void registerNetworkBroadcast() {
        context.registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @SuppressLint("StaticFieldLeak")
    private void prepareDatabase() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                // Если список локаций не был заполнен ранее
                if (!appSharedPreference.getDataSourceIfPresent()) {
                    // получим список доступных локаций из json-файла
                    List<LocationJsonObject> locationData = JsonUtil.getJsonFromFile(context, "city_list.json");
                    // сохраним в БД
                    addLocation(locationData);

                    // получим список выбранных локаций (по умолчанию) из json-файла
                    List<LocationJsonObject> selectedData = JsonUtil.getJsonFromFile(context, "city_list_default.json");
                    // сохраним в БД
                    addSelectedLocation(selectedData);

                    appSharedPreference.setDataSourceIfPresent(true);
                }
                return null;
            }
        }.execute();
    }


    public void setRecordObserver(@NonNull LifecycleOwner lifecycleOwner, Observer<List<LocationContainer>> observer) {
        recordLiveData.observe(lifecycleOwner, observer);
    }


    public void setLocationObserver(@NonNull LifecycleOwner lifecycleOwner, Observer<List<Location>> observer) {
        locationLiveData.observe(lifecycleOwner, observer);
    }

    /**
     * добавляем локацию в список выбранных
     * @param location локация
     */
    public void addSelectedLocation(final Location location) {
        final SelectedLocation selectedLocation = new SelectedLocation(location.getCode(), location.getLocation(), location.getCoordLon(), location.getCoordLat());
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                database.selectedLocationDao().insert(selectedLocation);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }


    /**
     * загружаем список выбранных локаций из Json
     * @param jsonObjectList список json-объектов
     */
    private void addSelectedLocation(List<LocationJsonObject> jsonObjectList) {
        for (LocationJsonObject source : jsonObjectList) {
            addSelectedLocation(new Location(Integer.valueOf(source.get_id()), source.getName(), source.getCoord().getLon(), source.getCoord().getLat()));
        }
    }


    /**
     * загружаем список доступных локаций из Json
     * @param jsonObjectList список json-объектов
     */
    private void addLocation(List<LocationJsonObject> jsonObjectList) {
        for (LocationJsonObject source : jsonObjectList) {
            database.locationDao().insert(new Location(Integer.valueOf(source.get_id()), source.getName(), source.getCoord().getLon(), source.getCoord().getLat()));
        }
    }


    /**
     * удаляем выбранную локацию из списка
     * @param selectedLocation выбранная локация
     */
    public void deleteSelectedLocation(final SelectedLocation selectedLocation) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                containerList.remove(selectedLocation.getCode());
                database.selectedLocationDao().delete(selectedLocation);
                recordLiveData.postValue(new ArrayList<>(containerList.values()));
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }


    /**
     * обновляем список выбранных локаций после подключения к сети
     */
    public void notifyAdapter() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                consumerSelected.accept(database.selectedLocationDao().getAllElement());
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }


    /**
     * проверяем есть ли локация в списке выбранных
     * @param location локация
     * @return boolean
     */
    public boolean isExistsLocation(Location location) {
        return containerList.containsKey(location.getCode());
    }


    public AppSharedPreference getAppSharedPreference() {
        return appSharedPreference;
    }


    private Consumer<List<SelectedLocation>> consumerSelected = new Consumer<List<SelectedLocation>>() {
        @Override
        public void accept(List<SelectedLocation> selectedLocations) throws Exception {

            for (final SelectedLocation record : selectedLocations) {
                final LocationContainer container = new LocationContainer(record);

                LocationContainer findContainer = containerList.get(container.getLocation().getCode());

                if (findContainer == null || findContainer.getTemp().isEmpty()) {
                    containerList.put(container.getLocation().getCode(), container);
                    recordLiveData.postValue(new ArrayList<>(containerList.values()));
                    Call<WeatherDay> callToday = weatherApi.getToday(record.getCode(), "metric", WeatherAPI.KEY);
                    callToday.enqueue(new Callback<WeatherDay>() {
                        @Override
                        public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {
                            WeatherDay data = response.body();
                            if (response.isSuccessful()) {
                                containerList.get(record.getCode()).setTemp(data.getTempWithDegree());
                                containerList.get(record.getCode()).setIconUrl(data.getIconUrl());
                                recordLiveData.postValue(new ArrayList<>(containerList.values()));
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherDay> call, Throwable t) {

                        }
                    });


                    final Call<WeatherForecast> callForecast = weatherApi.getForecast(record.getCode(), "metric", WeatherAPI.KEY);
                    callForecast.enqueue(new Callback<WeatherForecast>() {
                        @Override
                        public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                            WeatherForecast forecast = response.body();
                            if (response.isSuccessful()) {
                                containerList.get(record.getCode()).setDayItems(forecast.getItems());
                                recordLiveData.postValue(new ArrayList<>(containerList.values()));
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherForecast> call, Throwable t) {
                        }
                    });
                }
            }
        }
    };


    private Consumer<List<Location>> consumerLocation = new Consumer<List<Location>>() {
        @Override
        public void accept(List<Location> locations) throws Exception {
            locationLiveData.postValue(locations);
        }
    };

}
