package ru.test.jmerzlikina.testweather;

import android.app.Application;

import ru.test.jmerzlikina.testweather.manager.WeatherManager;

/**
 * Приложение "прогноз погоды"
 * Особенности реализации:
 * - список доступных локаций загружается из файла city_list.json
 * - список встроенных локаций (по умолчанию) загружается из файла city_list_default.json
 * - список всех локаций (для добавления в список доступных) хранится в файле city_list_all.json
 * - данные о погоде загружаются с openweathermap.org с помощью retrofit
 * - доступен функционал добавления локации (float button), удаления локации (long click)
 * - выбранные и доступные локации хранятся в БД (room)
 * - загрузка иконки прогноза погоды осуществляется с помощью Glide
 * - отображение списка выбранных локаций реализовано с использованием pagerbullet
 * - загрузка данных из json файла с применением библиотеки gson
 * - работа с визуальными компонентами реализована с помощью butterknife
 */

public class WeatherApplication extends Application {

    private static WeatherManager presenter;

    @Override
    public void onCreate() {
        super.onCreate();

        presenter = new WeatherManager(this);
    }


    public static WeatherManager getPresenter() {
        return presenter;
    }
}
