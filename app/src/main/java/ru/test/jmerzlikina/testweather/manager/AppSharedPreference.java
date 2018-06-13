package ru.test.jmerzlikina.testweather.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * класс для работ с SharedPreference
 */
public class AppSharedPreference {

    private SharedPreferences preferences;

    private static final String PREFS_TAG = "prefs";

    private static final String IS_DATA_PRESENT = "isData";
    private static final String SELECTED_POS = "selectedPosition";


    public AppSharedPreference(Context context) {
        preferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE);
    }

    /**
     * устанавливаем признак загруженных данных
     * @param isData boolean
     */
    public void setDataSourceIfPresent(boolean isData){
        preferences.edit().putBoolean(IS_DATA_PRESENT, isData).apply();
    }


    /**
     * получаем признак загруженных данных
     * @return boolean
     */
    public boolean getDataSourceIfPresent(){
        return preferences.getBoolean(IS_DATA_PRESENT, false);
    }


    /**
     * получаем выбранную позицию в списке локаций
     * @return int
     */
    public int getSelectedPosition(){
        return preferences.getInt(SELECTED_POS, 0);
    }


    /**
     * сохраняем выбранную позицию в списке локаций
     * @param selectedPosition
     */
    public void setSelectedPosition(int selectedPosition) {
        preferences.edit().putInt(SELECTED_POS, selectedPosition).apply();
    }
}
