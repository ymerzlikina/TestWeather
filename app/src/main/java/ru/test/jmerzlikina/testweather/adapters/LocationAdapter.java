package ru.test.jmerzlikina.testweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.test.jmerzlikina.testweather.R;
import ru.test.jmerzlikina.testweather.db.Location;

public class LocationAdapter extends BaseAdapter {

    private List<Location> locationList;

    public LocationAdapter(List<Location> locationList) {
        this.locationList = locationList;
    }

    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public Object getItem(int i) {
        return locationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return locationList.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup ) {
        LocationHolder locationHolder;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_location, viewGroup, false);
            locationHolder = new LocationHolder(convertView);
            convertView.setTag(locationHolder);
        } else {
            locationHolder = (LocationHolder) convertView.getTag();
        }

        locationHolder.bind(locationList.get(i));

        return convertView;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }


    public class LocationHolder {

        protected Location location;

        @BindView(R.id.tv_location_name)
        protected TextView tvLocationName;

        LocationHolder(View view) {
            ButterKnife.bind(this, view);
        }


        void bind(Location location) {
            this.location = location;
            tvLocationName.setText(location.getLocation());
        }

    }
}
