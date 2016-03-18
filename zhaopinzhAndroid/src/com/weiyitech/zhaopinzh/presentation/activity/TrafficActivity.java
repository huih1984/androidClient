package com.weiyitech.zhaopinzh.presentation.activity;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.*;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteStep;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.*;
import com.baidu.mapapi.search.route.*;
import com.weiyitech.zhaopinzh.Interface.BusinessInterface;
import com.weiyitech.zhaopinzh.ZhaopinzhApp;
import com.weiyitech.zhaopinzh.business.QueryFair;
import com.weiyitech.zhaopinzh.presentation.R;
import com.weiyitech.zhaopinzh.presentation.adapter.SearchHistoryAdapter;
import com.weiyitech.zhaopinzh.presentation.component.MapAddressDictionaryDatabase;
import com.weiyitech.zhaopinzh.presentation.component.MapAddressDictionaryProvider;
import com.weiyitech.zhaopinzh.presentation.component.MessageProvider;
import com.weiyitech.zhaopinzh.struct.LocationInMapReq;
import com.weiyitech.zhaopinzh.struct.LocationInMapRsp;
import com.weiyitech.zhaopinzh.util.Common;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * UserBusiness: zanma
 * Date: 13-10-10
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class TrafficActivity extends CommonActivity implements BusinessInterface, BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener, OnGetGeoCoderResultListener {
    final static int BY_BUS = 1;
    final static int BY_CAR = 2;
    final static int BY_FOOT = 3;
    String fromWhere;
    RouteLine route = null;//保存驾车/步行路线数据的变量，供浏览节点时使用
    OverlayManager routeOverlay = null;
    //浏览路线节点相关
    Button mBtnPre = null;//上一个节点
    Button mBtnNext = null;//下一个节点
    MapView mMapView = null;    // 地图View
    BaiduMap mBaidumap = null;
    Button preButton = null;
    Button nextButton = null;
    TextView routeTextView = null;
    Button busButton = null;
    Button carButton = null;
    Button footButton = null;
    LinearLayout preNextLayout = null;
    //搜索相关
    RoutePlanSearch routePlanSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    GeoCoder geoCoderSearch = null; // 搜索模块，也可去掉地图模块独立使用
    MyLocationData myLocationData = null;
    ScrollView routeMessageScrollView = null;
    private TextView popupText = null;//泡泡view
    private View viewCache = null;
    int nodeIndex = -2;//节点索引,供浏览节点时使用
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true;// 是否首次定位

    List<LocationInMapRsp> locationInMapRspList = new ArrayList<LocationInMapRsp>();
    List<LocationInMapRsp> newLocationInMapRspList = new ArrayList<LocationInMapRsp>();

    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);
    Marker mMarker;
    private InfoWindow mInfoWindow;
    HashMap<Marker, String> markerInfoHashMap = new HashMap<Marker, String>();
    LatLng destLatLng;
    boolean isStringDestPath;

    /**
     * 查詢相關變量
     */
    android.support.v7.widget.SearchView searchView;
    ListView searchHistoryListView;
    SearchHistoryAdapter searchHistoryAdapter;
    ArrayList<String> searchHistoryList;

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZhaopinzhApp app = (ZhaopinzhApp) this.getApplication();
        setContentView(R.layout.traffic_activity);
        /**
         * 设置地图的放大图标不显示
         */
//        BaiduMapOptions baiduMapOptions = new BaiduMapOptions();
//        baiduMapOptions.scaleControlEnabled(false);
//        baiduMapOptions.zoomControlsEnabled(false);
//        mMapView = new MapView(this, baiduMapOptions);
//        ViewGroup parent = (ViewGroup) findViewById(R.id.traffic_layout);
//        mMapView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mMapView.setId(R.id.traffic_activity_bmapView);
//        parent.addView(mMapView, 0);
        mMapView = (MapView) findViewById(R.id.traffic_activity_bmapView);
        preButton = (Button) findViewById(R.id.pre);
        nextButton = (Button) findViewById(R.id.next);
        routeTextView = (TextView) findViewById(R.id.traffic_activity_text_view);
        mBaidumap = mMapView.getMap();
        mBtnPre = (Button) findViewById(R.id.pre);
        mBtnNext = (Button) findViewById(R.id.next);
        routeMessageScrollView = (ScrollView) findViewById(R.id.traffic_activity_scroll_view);
        busButton = (Button) findViewById(R.id.traffic_activity_popup_bus_button);
        busButton.setPressed(true);
        carButton = (Button) findViewById(R.id.traffic_activity_popup_car_button);
        footButton = (Button) findViewById(R.id.traffic_activity_popup_foot_button);
        preNextLayout = (LinearLayout) findViewById(R.id.traffic_activity_pre_next_layout);

        /**
         * 设置地图的参数
         */
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(12.5f);
        mBaidumap.setMapStatus(msu);
        mBaidumap.setOnMapClickListener(this);

        /** 开启定位图层
         * *
         */
        mBaidumap.setMyLocationEnabled(true);

        /**定位初始化
         *
         */
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(4000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        /** 初始化搜索模块，注册事件监听
         *
         */
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(this);
        geoCoderSearch = GeoCoder.newInstance();
        geoCoderSearch.setOnGetGeoCodeResultListener(this);

        /**
         * 查询招聘地点地理坐标位置 ，并将地理位置设置到地图上
         */
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2006);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DAY_OF_MONTH, 3);
        cal.add(Calendar.DATE, -4);
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(MessageProvider.LOCATION_IN_MAP_URI, null, null, null, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                LocationInMapRsp locationInMapRsp = new LocationInMapRsp();
                locationInMapRsp.locationId = cursor.getInt(cursor.getColumnIndex("location_id"));
                locationInMapRsp.latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
                locationInMapRsp.longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
                locationInMapRsp.address = cursor.getString(cursor.getColumnIndex("address"));
                locationInMapRsp.addTime = cursor.getString(cursor.getColumnIndex("add_time"));
                try {
                    if (date.before(df.parse(locationInMapRsp.addTime))) {
                        date = df.parse(locationInMapRsp.addTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                locationInMapRspList.add(locationInMapRsp);
                addOverLayItem(locationInMapRsp);
            }
        }

        /**数据库中查询完毕再向服务器发送请求，查询新增的位置信息
         *
         */
        String dateStr = df.format(date);
        LocationInMapReq locationInMapReq = new LocationInMapReq();
        locationInMapReq.setAddTime(dateStr);
        QueryFair queryFair = new QueryFair();
        queryFair.queryLocationInMap(locationInMapReq, this);

        /**
         * 地图上的mark标记监听响应
         */
        mBaidumap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.traffic_activity_popup, null);
                TextView textView = (TextView) view.findViewById(R.id.traffic_activity_popup_text_view);
                final LatLng ll = marker.getPosition();
                Point p = mBaidumap.getProjection().toScreenLocation(ll);
                p.y -= 47;
                LatLng llInfo = mBaidumap.getProjection().fromScreenLocation(p);
                InfoWindow.OnInfoWindowClickListener listener = null;
                textView.setText(markerInfoHashMap.get(marker));
                destLatLng = marker.getPosition();
                listener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
//                        LatLng llNew = new LatLng(ll.latitude + 0.005,
//                                ll.longitude + 0.005);
//                        marker.setPosition(llNew);
                        mBaidumap.hideInfoWindow();
                    }
                };
                try {
                    mInfoWindow = new InfoWindow(view, llInfo, listener);
                    mBaidumap.showInfoWindow(mInfoWindow);
                    searchButtonProcess(searchType);
                    isStringDestPath = false;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /**
         *
         */
        searchHistoryListView = (ListView) findViewById(R.id.traffic_activity_search_history_listView);
        searchHistoryList = new ArrayList<String>();
        searchHistoryAdapter = new SearchHistoryAdapter(this, searchHistoryList);
        searchHistoryListView.setAdapter(searchHistoryAdapter);

        searchHistoryListView.setVisibility(View.GONE);
    }


    int searchType = BY_BUS;

    /**
     * 发起路线规划搜索示例
     *
     * @param
     */
    void searchButtonProcess(int searchType) {
        //重置浏览节点的路线数据
        route = null;
        if (routeOverlay != null) {
            routeOverlay.removeFromMap();
        }
        if (myLocationData == null) {
            return;
        }
        LatLng ptCenter = new LatLng(myLocationData.latitude, myLocationData.longitude);
        // 反Geo搜索
//        geoCoderSearch.reverseGeoCode(new ReverseGeoCodeOption()
//                .location(ptCenter));
        //设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode;
        stNode = PlanNode.withLocation(ptCenter);
        PlanNode enNode;
        if (isStringDestPath) {
            enNode = PlanNode.withCityNameAndPlaceName("南京", searchWord);
        } else {
            enNode = PlanNode.withLocation(destLatLng);
        }

        if (searchType == BY_CAR) {
            routePlanSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
        } else if (searchType == BY_BUS) {
            routePlanSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode)
                    .city("南京")
                    .to(enNode));
        } else if (searchType == BY_FOOT) {
            routePlanSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
        }
        this.searchType = searchType;
    }

    public void searchBusButtonProcess(View v) {
        searchButtonProcess(BY_BUS);
        isStringDestPath = false;
        v.setBackgroundResource(R.drawable.action_bybus_pressed);
        carButton.setBackgroundResource(R.drawable.action_bycar_unpressed);
        footButton.setBackgroundResource(R.drawable.action_byfoot_unpressed);
    }

    public void searchCarButtonProcess(View v) {
        searchButtonProcess(BY_CAR);
        isStringDestPath = false;
        v.setBackgroundResource(R.drawable.action_bycar_pressed);
        busButton.setBackgroundResource(R.drawable.action_bybus_unpressed);
        footButton.setBackgroundResource(R.drawable.action_byfoot_unpressed);
    }

    public void searchFootButtonProcess(View v) {
        searchButtonProcess(BY_FOOT);
        isStringDestPath = false;
        v.setBackgroundResource(R.drawable.action_byfoot_pressed);
        carButton.setBackgroundResource(R.drawable.action_bycar_unpressed);
        busButton.setBackgroundResource(R.drawable.action_bybus_unpressed);
    }

    /**
     * 节点浏览示例
     *
     * @param v
     */
    public void nodeClick(View v) {
        if (nodeIndex < -1 || route == null ||
                route.getAllStep() == null
                || nodeIndex > route.getAllStep().size()) {
            return;
        }
        //设置节点索引
        if (v.getId() == R.id.next && nodeIndex < route.getAllStep().size() - 1) {
            nodeIndex++;
        } else if (v.getId() == R.id.pre && nodeIndex > 1) {
            nodeIndex--;
        }
        if (nodeIndex < 0 || nodeIndex >= route.getAllStep().size()) {
            return;
        }

        //获取节结果信息
        LatLng nodeLocation = null;
        String nodeTitle = null;
        Object step = route.getAllStep().get(nodeIndex);
        if (step instanceof DrivingRouteLine.DrivingStep) {
            nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace().getLocation();
            nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
        } else if (step instanceof WalkingRouteLine.WalkingStep) {
            nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace().getLocation();
            nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
        } else if (step instanceof TransitRouteLine.TransitStep) {
            nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace().getLocation();
            nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
        }

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }
        //移动节点至中心
        mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        // show popup
        viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
        popupText = (TextView) viewCache.findViewById(R.id.textcache);
        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setText(nodeTitle);
        mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, null));
    }

    //定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        mBaidumap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        return false;
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaidumap.setMyLocationData(locData);
            myLocationData = locData;
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaidumap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }

    }

    void onGetResultDoEvent(int type) {
        String routeStr = "";
        for (int i = 0; i < route.getAllStep().size(); ++i) {
            Object step = route.getAllStep().get(i);
            if (type == BY_BUS) {
                routeStr += ((TransitRouteLine.TransitStep) step).getInstructions() + "\n\n";
            } else if (type == BY_CAR) {
                routeStr += ((DrivingRouteLine.DrivingStep) step).getInstructions() + "\n\n";
            } else if (type == BY_FOOT) {
                routeStr += ((WalkingRouteLine.WalkingStep) step).getInstructions() + "\n\n";
            }
        }
        routeTextView.setText("约" + ((float) route.getDistance()) / 1000 + "公里\n\n" + routeStr);
        routeMessageScrollView.setVisibility(View.VISIBLE);
        preNextLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(TrafficActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            route = result.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
            //       mBaidumap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            onGetResultDoEvent(BY_FOOT);
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(TrafficActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            route = result.getRouteLines().get(0);
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
            // mBaidumap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            onGetResultDoEvent(BY_BUS);
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(TrafficActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
            routeOverlay = overlay;
            // mBaidumap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            onGetResultDoEvent(BY_CAR);
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(TrafficActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
        }
        //mBaidumap.clear();
        mBaidumap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_gcoding)));
        mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
    }

    void addOverLayItem(LocationInMapRsp locationInMapRsp) {
        LatLng ll = new LatLng(locationInMapRsp.latitude, locationInMapRsp.longitude);
        OverlayOptions oo = new MarkerOptions().position(ll).icon(bd)
                .zIndex(9);
        mMarker = (Marker) (mBaidumap.addOverlay(oo));
        markerInfoHashMap.put(mMarker, locationInMapRsp.getAddress());
    }

    @Override
    public void getDataFromBusiness(int dataType, Object t, int... position) {
        if (dataType == Common.LOCATION_IN_MAP_TYPE) {
            newLocationInMapRspList = (ArrayList<LocationInMapRsp>) t;
            ContentResolver resolver = getContentResolver();
            for (int i = 0; i < newLocationInMapRspList.size(); ++i) {
                ContentValues contentValues = new ContentValues();
                Common.putLocationInMap(contentValues, newLocationInMapRspList.get(i));
                resolver.insert(MessageProvider.LOCATION_IN_MAP_URI, contentValues);
                addOverLayItem(newLocationInMapRspList.get(i));
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        if (Common.getIsUNSpecified(this).equals("竖屏显示")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    void goHome() {
        // action bar中的应用程序图标被点击了，返回home
        if (fromWhere == null) {
            Intent intent = new Intent(this, TalkActivity.class);
            startActivity(intent);
        } else if (fromWhere.equals(HomeActivity.class.getName())) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (fromWhere.equals(JobsOfFairActivity.class.getName())) {
            Intent intent = new Intent(this, JobsOfFairActivity.class);
            startActivity(intent);
        }
        //overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }


    void searchByWord() {
        /**
         * 按输入地址查询路线
         */
        mBaidumap.hideInfoWindow();
        isStringDestPath = true;
        searchButtonProcess(searchType);
    }

    private String searchWord;

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            // handles a click on a search suggestion; launches activity to show word
            Intent wordIntent = new Intent();
            Uri uri = intent.getData();
            Cursor cursor = managedQuery(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int iIndex = cursor.getColumnIndexOrThrow(MapAddressDictionaryDatabase.INPUTTEXT);
                try {
                    searchWord = cursor.getString(iIndex);
                } catch (Exception e) {

                }
            }
            searchByWord();
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            searchWord = intent.getStringExtra(SearchManager.QUERY);
            ContentResolver resolver = getContentResolver();
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            ContentValues contentValues = new ContentValues();
            contentValues.put(MapAddressDictionaryDatabase.ADDTIME, date);
            contentValues.put(MapAddressDictionaryDatabase.INPUTTEXT, searchWord);
            resolver.insert(MapAddressDictionaryProvider.CONTENT_URI, contentValues);
            searchByWord();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goHome();
                return true;
            case R.id.search_activity_action_search:
                onSearchRequested();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        goHome();
    }

    /**
     * 创建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.traffic_activity_actions, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(SEARCH_SERVICE);

        MenuItem searchItem = menu.findItem(R.id.traffic_activity_action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaidumap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        routeMessageScrollView.setVisibility(View.GONE);
//        mBtnPre.setVisibility(View.GONE);
//        mBtnNext.setVisibility(View.GONE);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}