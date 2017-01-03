package hy.cityselectedtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView backIV;
    private ImageView clearIV;
    private EditText searchET;
    private ListView cityListLV;
    private TextView selectedTV;
    private LetterBar letterBar;
    private ListView searchedCityList;
    private List<City> cities;
    private CityAdapter adapter;
    private SearchedCityAdapter searchedCityAdapter;
    /*定位相关*/
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backIV = (ImageView)findViewById(R.id.iv_back);
        clearIV = (ImageView)findViewById(R.id.iv_search_clear);
        searchET = (EditText)findViewById(R.id.et_search);
        cityListLV = (ListView) findViewById(R.id.city_list);
        selectedTV = (TextView)findViewById(R.id.selected_tv);
        letterBar = (LetterBar)findViewById(R.id.letterbar);
        searchedCityList = (ListView)findViewById(R.id.searched_city_list);

        backIV.setOnClickListener(clickListener);
        clearIV.setOnClickListener(clickListener);
        searchET.addTextChangedListener(textWatcher);
        letterBar.setSelectedListener(selectedListener);



        if (DBManager.copyDB(this)) {
            cities = DBManager.getAllCity();
            adapter = new CityAdapter(this, cities);
            adapter.setAdapterListener(adapterListener);
            cityListLV.setAdapter(adapter);
            cityListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MainActivity.this,cities.get(position).getName(),Toast.LENGTH_SHORT).show();
                }
            });

            searchedCityAdapter = new SearchedCityAdapter(this);
            searchedCityList.setAdapter(searchedCityAdapter);
            searchedCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MainActivity.this,((City)searchedCityAdapter.getItem(position)).getName(),Toast.LENGTH_SHORT).show();
                }
            });

            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        }
    }


    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    adapter.setCurrentPosition(aMapLocation.getCity());
                } else {
                    adapter.setCurrentPosition("定位失败");
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("yue.huang", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    private CityAdapter.AdapterListener adapterListener = new CityAdapter.AdapterListener() {
        @Override
        public void startPosition() {
            mLocationClient.startLocation();
        }

        @Override
        public void hotCityClick(String city) {
            Toast.makeText(MainActivity.this,city,Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back:
                    onBackPressed();
                    break;
                case R.id.iv_search_clear:
                    searchET.setText("");
                    break;
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            clearIV.setVisibility(View.VISIBLE);
            searchedCityList.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            List<City> cities = DBManager.searchCity(s.toString());
            searchedCityAdapter.setCityList(cities);
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().equals("")){
                clearIV.setVisibility(View.GONE);
                searchedCityList.setVisibility(View.GONE);
            }
        }
    };

    private LetterBar.SelectedListener selectedListener = new LetterBar.SelectedListener() {
        @Override
        public void onSelected(String letter) {
            selectedTV.setText(letter);
            selectedTV.setVisibility(View.VISIBLE);
            cityListLV.setSelection(adapter.getLetterPosition(letter.toLowerCase()));
        }

        @Override
        public void onCancel() {
            selectedTV.setVisibility(View.GONE);
        }
    };
}
