package ru.test.jmerzlikina.testweather.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import ru.test.jmerzlikina.testweather.R;
import ru.test.jmerzlikina.testweather.WeatherApplication;
import ru.test.jmerzlikina.testweather.object.LocationContainer;

public class LocationPagerAdapter extends PagerAdapter {

    private final List<LocationContainer> containerList;
    private Context mContext;

    @BindView(R.id.tv_location_name)
    protected TextView tvLocationName;

    @BindView(R.id.tv_temp)
    protected TextView tvTemp;

    @BindView(R.id.iv_Image)
    protected ImageView ivImage;

    @BindView(R.id.tv_first_day)
    protected TextView tvFirstDay;


    @BindView(R.id.tv_second_day)
    protected TextView tvSecondDay;


    @BindView(R.id.tv_third_day)
    protected TextView tvThirdDay;


    public LocationPagerAdapter(Context context, List<LocationContainer> containerList) {
        mContext = context;
        this.containerList = containerList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        LocationContainer record = containerList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_selected_location, collection, false);
        collection.addView(layout);
        ButterKnife.bind(this, layout);

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle(R.string.delete_town);
                alert.setMessage(R.string.confirm_delete);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        WeatherApplication.getPresenter().deleteSelectedLocation(containerList.get(position).getLocation());
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();

                return false;
            }
        });

        tvLocationName.setText(record.getLocation().getLocation());
        tvTemp.setText(record.getTemp());
        if (!record.getIconUrl().isEmpty()) {
            Glide.with(mContext).load(record.getIconUrl()).into(ivImage);
        }

        if (record.getDayItems() != null && record.getDayItems().size() >= 3) {
            tvFirstDay.setText(record.getDayItems().get(0).toString());
            tvSecondDay.setText(record.getDayItems().get(1).toString());
            tvThirdDay.setText(record.getDayItems().get(2).toString());
        }
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return containerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        LocationContainer customPagerEnum = containerList.get(position);
        return customPagerEnum.getLocation().getLocation();
    }
}