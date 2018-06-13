package ru.test.jmerzlikina.testweather.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.test.jmerzlikina.testweather.object.LocationJsonObject;

/**
 * утилита работы с Json
 */
public class JsonUtil {

    /**
     * загрузка данных о локациях из json-файла
     * @param context контекст
     * @param fileName имя файла
     * @return список json-объектов с описанием локаций
     */
    public static List<LocationJsonObject> getJsonFromFile(Context context, String fileName) {
        InputStream stream = null;

        JsonReader reader;
        List<LocationJsonObject> messages = new ArrayList<LocationJsonObject>();
        try {
            stream = context.getAssets().open(fileName, AssetManager.ACCESS_BUFFER);

            if (stream == null) {
                return messages;
            }

            reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            Gson gson = new GsonBuilder().create();
            reader.beginArray();
            while (reader.hasNext()) {
                LocationJsonObject message = gson.fromJson(reader, LocationJsonObject.class);
                messages.add(message);
            }
            reader.endArray();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(messages, new Comparator<LocationJsonObject>() {
            @Override
            public int compare(LocationJsonObject locationJsonObject, LocationJsonObject nextLocationJsonObject) {
                return locationJsonObject.getName().compareToIgnoreCase(nextLocationJsonObject.getName());
            }
        });
        return messages;
    }
}
