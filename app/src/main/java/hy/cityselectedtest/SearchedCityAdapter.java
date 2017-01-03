package hy.cityselectedtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by huangyue on 2016/12/30.
 */

public class SearchedCityAdapter extends BaseAdapter {
    private Context context;
    private List<City> cityList;
    public SearchedCityAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return cityList == null ? 0 : cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item1,null);
            holder = new ViewHolder();
            holder.cityTV = (TextView) convertView.findViewById(R.id.city_name);
            convertView.setTag(holder);
        }
        holder = (ViewHolder)convertView.getTag();
        holder.cityTV.setText(cityList.get(position).getName());
        return convertView;
    }

    private class ViewHolder{
        TextView cityTV;
    }


    public void setCityList(List<City> cityList){
        this.cityList = cityList;
        notifyDataSetChanged();
    }
}
