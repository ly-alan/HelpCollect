package come.help.collect.location;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.business.utillibrary.util.TimeUtils;

import come.help.collect.HelpApplication;
import come.help.collect.R;

public class GDLocationService extends Service {

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        applyNotiKeepMech();
        startLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unApplyNotiKeepMech();
        stopLocation();
    }

    /**
     * 触发利用notification增加进程优先级
     */
    protected void applyNotiKeepMech() {
        startForeground(92011, buildNotification(getBaseContext()));
    }

    public void unApplyNotiKeepMech() {
        stopForeground(true);
    }

    public static Notification buildNotification(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentText(context.getString(R.string.app_name));
        return builder.build();
    }


    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            showLoctaionInfo(aMapLocation);
            //发送结果的通知
            if (aMapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
                //定位成功
            } else {
                //失败
            }
        }

        private void showLoctaionInfo(AMapLocation aMapLocation) {
            //记录信息并发送广播
            long callBackTime = System.currentTimeMillis();
            StringBuffer sb = new StringBuffer();
            sb.append("定位完成 时间: " + TimeUtils.millis2String(callBackTime) + "\n");
            if (null == aMapLocation) {
                sb.append("定位失败：location is null!!!!!!!");
            } else {
                sb.append(getLocationStr(aMapLocation));
            }
            Log.i("liao", sb.toString());
        }

    };

    /**
     * 启动定位
     */
    public void startLocation() {
        stopLocation();
        if (null == mLocationClient) {
            mLocationClient = new AMapLocationClient(HelpApplication.getInstence());
        }

        mLocationOption = new AMapLocationClientOption();
        // 使用连续
        mLocationOption.setOnceLocation(false);
        mLocationOption.setLocationCacheEnable(false);
        // 每10秒定位一次
        mLocationOption.setInterval(30 * 1000);
        // 地址信息
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(locationListener);
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
        }
    }

    /**
     * 根据定位结果返回定位信息的字符串
     *
     * @param location
     * @return
     */
    private synchronized static String getLocationStr(AMapLocation location) {
        if (null == location) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");

            sb.append("海    拔    : " + location.getAltitude() + "米" + "\n");
            sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
            sb.append("角    度    : " + location.getBearing() + "\n");
            if (location.getProvider().equalsIgnoreCase(
                    android.location.LocationManager.GPS_PROVIDER)) {
                // 以下信息只有提供者是GPS时才会有
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : "
                        + location.getSatellites() + "\n");
            }

            //逆地理信息
            sb.append("国    家    : " + location.getCountry() + "\n");
            sb.append("省            : " + location.getProvince() + "\n");
            sb.append("市            : " + location.getCity() + "\n");
            sb.append("城市编码 : " + location.getCityCode() + "\n");
            sb.append("区            : " + location.getDistrict() + "\n");
            sb.append("区域 码   : " + location.getAdCode() + "\n");
            sb.append("地    址    : " + location.getAddress() + "\n");
            sb.append("兴趣点    : " + location.getPoiName() + "\n");
            //定位完成的时间
            sb.append("定位时间: " + TimeUtils.millis2String(location.getTime()) + "\n");

        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        //定位之后的回调时间
        sb.append("回调时间: " + TimeUtils.millis2String(System.currentTimeMillis()) + "\n");
        return sb.toString();
    }
}
