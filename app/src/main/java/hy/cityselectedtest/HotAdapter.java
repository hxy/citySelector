package hy.cityselectedtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by huangyue on 2016/12/27.
 */

public class HotAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> hotCities;
    public HotAdapter(Context context){
        this.context = context;
        hotCities = new ArrayList<>();
        hotCities.add("北京");
        hotCities.add("上海");
        hotCities.add("广州");
        hotCities.add("深圳");
        hotCities.add("杭州");
        hotCities.add("南京");
        hotCities.add("天津");
        hotCities.add("武汉");
        hotCities.add("重庆");
    }
    @Override
    public int getCount() {
        return hotCities.size();
    }

    @Override
    public Object getItem(int position) {
        return hotCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_hot_item,null);
        ((TextView)convertView).setText(hotCities.get(position));
        return convertView;
    }
}
