package com.mike_milk.map.view.activity;

import android.app.Activity;
import android.app.NativeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.view.PoiInputItemWidget;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.mike_milk.map.R;
import com.mike_milk.map.view.adpter.SearchAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *时间：2020/7/14
 *创建者：MIKE-MILK
 *描述：搜索提示，点击列表跳转至导航
 */
public class InputtipsActivity extends Activity implements TextWatcher, InputtipsListener, PoiSearch.OnPoiSearchListener,AdapterView.OnItemClickListener,View.OnTouchListener {

	private String city = "";
	private AutoCompleteTextView mKeywordText;
	private ListView listView;
	private List<Tip>mTip;
	private Poi poi;
	private static final String TAG="activity_navi";
	private int pointType;
	private SearchAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inputtips);
		listView = (ListView)findViewById(R.id.inputlist);
		mKeywordText = (AutoCompleteTextView)findViewById(R.id.auto_keyWord);
		listView.setOnItemClickListener(this);
		listView.setOnTouchListener(this);
        mKeywordText.addTextChangedListener(this);
        mKeywordText.requestFocus();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
        InputtipsQuery inputquery = new InputtipsQuery(newText, city);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(InputtipsActivity.this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
	}

	@Override
	public void afterTextChanged(Editable s) {}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mTip!=null){
			Tip tip=(Tip)parent.getItemAtPosition(position);
			poi=new Poi(tip.getName(),new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude()), tip.getPoiID());
			if (!TextUtils.isEmpty(poi.getPoiId())){
				PoiSearch.Query query = new PoiSearch.Query(poi.getName(), "", city);
				query.setDistanceSort(false);
				query.requireSubPois(true);
				PoiSearch poiSearch = new PoiSearch(getApplicationContext(), query);
				poiSearch.setOnPoiSearchListener(this);
				poiSearch.searchPOIIdAsyn(poi.getPoiId());
			}
		}
	}
	//回调
	@Override
	public void onGetInputtips(final List<Tip> tipList, int rCode) {

		try {
			if (rCode == 1000) {
				mTip = new ArrayList<Tip>();
				for (Tip tip : tipList) {
					if (null == tip.getPoint()) {
						continue;
					}
					mTip.add(tip);
				}
					listView.setVisibility(View.VISIBLE);
					adapter = new SearchAdapter(getApplicationContext(), mTip);
					listView.setAdapter(adapter);
					adapter.notifyDataSetChanged();

			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
//				SimpleAdapter aAdapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.item_layout,
//						new String[]{"name", "address"}, new int[]{R.id.poi_field_id, R.id.poi_value_id});
//				minputlist.setAdapter(aAdapter);
//				//ListView的item的点击事件
//				minputlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//						//点击的那个位置，传出name，address
//						TextView field=findViewById(R.id.poi_field_id);
//						TextView value=findViewById(R.id.poi_value_id);
//						LatLonPoint aPoint;
//						LatLonPoint bPoint;
//						Tip tip=tipList.get(position);
//						Toast.makeText(InputtipsActivity.this,"点击了"+tip.getPoiID()
//								,Toast.LENGTH_SHORT).show();
//						//传出值，到下一个activity
//						Intent intent=new Intent(InputtipsActivity.this, AMapNaviActivity.class);
//						intent.putExtra("name",tipList.get(position).getName());
//						intent.putExtra("address",tipList.get(position).getDistrict());
//						startActivity(intent);
//					}
//				});
//				aAdapter.notifyDataSetChanged();
			}
//        } else {
//			Toast.makeText(this,"错误代码:"+rCode,Toast.LENGTH_LONG).show();
//		}
//	}

	@Override
	public void onPoiSearched(PoiResult poiResult, int i) {

	}

	@Override
	public void onPoiItemSearched(PoiItem poiItem, int i) {
		try {
			LatLng latLng = null;
			int code = 0;
			if (i == AMapException.CODE_AMAP_SUCCESS) {
				if (poiItem == null) {
					return;
				}
				LatLonPoint exitP = poiItem.getExit();
				LatLonPoint enterP = poiItem.getEnter();
				if (pointType == PoiInputItemWidget.TYPE_START) {
					code = 100;
					if (exitP != null) {
						latLng = new LatLng(exitP.getLatitude(), exitP.getLongitude());
					} else {
						if (enterP != null) {
							latLng = new LatLng(enterP.getLatitude(), enterP.getLongitude());
						}
					}
				}
				if (pointType == PoiInputItemWidget.TYPE_DEST) {
					code = 200;
					if (enterP != null) {
						latLng = new LatLng(enterP.getLatitude(), enterP.getLongitude());
					}
				}
				Bundle bundle=new Bundle();
				bundle.putDouble("lat",enterP.getLatitude());
				bundle.putDouble("log",enterP.getLongitude());
				Intent intent = new Intent(InputtipsActivity.this, RestRouteActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			Poi apoi;
			if (latLng != null) {
				apoi = new Poi(poi.getName(), latLng, poi.getPoiId());
			} else {
				apoi = poi;
			}
			Log.d(TAG, String.valueOf(latLng));
			Log.d(TAG, String.valueOf(apoi));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
}

