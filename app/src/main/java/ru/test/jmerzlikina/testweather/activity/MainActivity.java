package ru.test.jmerzlikina.testweather.activity;

import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import com.robohorse.pagerbullet.PagerBullet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.test.jmerzlikina.testweather.R;
import ru.test.jmerzlikina.testweather.WeatherApplication;
import ru.test.jmerzlikina.testweather.adapters.LocationPagerAdapter;
import ru.test.jmerzlikina.testweather.adapters.LocationAdapter;
import ru.test.jmerzlikina.testweather.db.Location;
import ru.test.jmerzlikina.testweather.manager.WeatherManager;
import ru.test.jmerzlikina.testweather.object.LocationContainer;

public class MainActivity extends AppCompatActivity {
    private WeatherManager weatherManager;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.vp_pager)
    protected PagerBullet viewPager;

    private LocationAdapter locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherManager = WeatherApplication.getPresenter();

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        locationAdapter = new LocationAdapter(new ArrayList<Location>());
        viewPager.setAdapter(new LocationPagerAdapter(this, new ArrayList<LocationContainer>()));
        setSupportActionBar(toolbar);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                weatherManager.getAppSharedPreference().setSelectedPosition(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        weatherManager.setRecordObserver(this, selectedObserver);
        weatherManager.setLocationObserver(this, locationObserver);
    }


    @OnClick(R.id.fab)
    protected void onClickFab() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_town);

        builder.setAdapter(locationAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Location location = (Location) locationAdapter.getItem(i);

                if (weatherManager.isExistsLocation(location)) {
                    Toast.makeText(MainActivity.this, R.string.town_was_added, Toast.LENGTH_LONG).show();
                    return;
                }
                weatherManager.addSelectedLocation(location);
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // признак первого запуска приложения
    private boolean isFirstAppStart = true;

    private Observer<List<LocationContainer>> selectedObserver = new Observer<List<LocationContainer>>() {
        @Override
        public void onChanged(@Nullable List<LocationContainer> recordList) {
            // получаем выбранный элемент для позиционирования после обновления адаптера
            int currentItem = viewPager.getCurrentItem();
            // если количество записей увеличилось - перейдем на последний
            if (recordList != null && viewPager.getViewPager().getAdapter() != null && recordList.size() > viewPager.getViewPager().getAdapter().getCount()) {
                currentItem = recordList.size() - 1;
            }
            // сохраняем данные в адаптер
            viewPager.setAdapter(new LocationPagerAdapter(MainActivity.this, recordList));
            // если это первый запуск, то получим выбранный элемент из sharedpref
            if (isFirstAppStart) {
                currentItem = weatherManager.getAppSharedPreference().getSelectedPosition();
                isFirstAppStart = false;
            }
            // позиционируемся на выбранный элемент
            viewPager.setCurrentItem(currentItem);
        }
    };


    private Observer<List<Location>> locationObserver = new Observer<List<Location>>() {
        @Override
        public void onChanged(@Nullable List<Location> recordList) {
            locationAdapter.setLocationList(recordList);
        }
    };
}
