package com.hungsum.framework.utils;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class HsLocationUtil
{
    /**
     * 默认定位超时时间，10秒
     */
    public final static int DEFAULT_LOCATION_TIMEOUT = 10000;

    /**
     * 最大定位超时时间，120秒
     */
    public final static int MAX_LOCATION_TIMEOUT = 120000;

    /**
     * 默认定位结果有效时间，5分钟
     */
    public final static int DEFAULT_LOCATION_RESULTEXPIREDTIME = 300000;

    private final static int ERR = 0x0;

    private final static int LOCATION = 0x1;

    private HsLocatinProvider[] mHsLocationProviders;

    /**
     * 定位结果有效时间数，单位毫秒（默认5分钟）
     */
    private static int mLocationResultExpiredTime = 300000;

    private LocationResultStores mLocationStores = new LocationResultStores();

    private EDispatchEventFrequency mdispatchEventFrequency = EDispatchEventFrequency.Continuous;

    private boolean hasDispatchEvent = false;

    private static HsLocationUtil util;

    private Context context;

    /**
     * 定位线程
     */
    private List<LocationThread> mLocationThreads = new ArrayList<LocationThread>();

    // {{ 实例化

    /**
     * 单例模型工厂方法
     *
     * @param context
     *            上下文
     * @param locationResultExpiredTime
     *            定位结果的有效期（毫秒），超出此时间的定位结果视为无效。
     * @return
     */
    public static HsLocationUtil getInstance(Context context,
                                             int locationResultExpiredTime)
    {
        if (util == null)
        {
            util = new HsLocationUtil(context);
        }

        HsLocationUtil.mLocationResultExpiredTime = locationResultExpiredTime;

        return util;
    }

    private HsLocationUtil(Context context)
    {
        this.context = context;

        setLocationProviders(new HsLocatinProvider[]
                { new HsLocatinProvider(LocationManager.NETWORK_PROVIDER, 1),
                        new HsLocatinProvider(LocationManager.GPS_PROVIDER, 2) });

        mLocaitonListeners = new ArrayList<HsLocationListener>();
    }

    // }}

    /**
     * 集中处理函数
     */
    private Handler mLocationUtilHandler = new Handler(new Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case HsLocationUtil.ERR:
                    exceptionReceivedNotify((Exception) msg.obj);
                    break;
                case HsLocationUtil.LOCATION:
                    HsLocation location = (HsLocation) msg.obj;
                    if (mdispatchEventFrequency == EDispatchEventFrequency.Continuous
                            || (mdispatchEventFrequency == EDispatchEventFrequency.Once && !hasDispatchEvent))
                    {
                        // 分发locationChanged事件
                        locationChangedNotifies(location);

                        hasDispatchEvent = true; // 已经分发过事件了。
                    }

                    if(location != null)
                    {
                        mLocationStores.add(location);
                    }

                    break;
            }

            return false;
        }
    });

    // {{ 自定义事件处理

    // {{ LocationListener

    private List<HsLocationListener> mLocaitonListeners;

    public void addOnHsLocationListener(HsLocationListener listener)
    {
        mLocaitonListeners.add(listener);
    }

    public void removeOnHsLocationListener(HsLocationListener listener)
    {
        mLocaitonListeners.remove(listener);
    }

    private void locationChangedNotifies(Location location)
    {
        for (HsLocationListener hsLocationListener : mLocaitonListeners)
        {
            hsLocationListener.onLocationChanged(location);
        }
    }

    // }}

    // {{ ExceptionListener

    private HsExceptionListener mExceptionListener;

    public void setOnExceptionListener(HsExceptionListener listener)
    {
        mExceptionListener = listener;
    }

    public void removeOnExceptionListener(HsExceptionListener listener)
    {
        mExceptionListener = null;
    }

    private void exceptionReceivedNotify(Exception exception)
    {
        if (mExceptionListener != null)
        {
            mExceptionListener.onExceptionReceived(exception);
        }
    }

    // }}

    // }}

    // {{ 属性

    /**
     * 设置定位时间发送频率
     *
     * @param value
     */
    public void setDispatchEventFrequency(EDispatchEventFrequency value)
    {
        mdispatchEventFrequency = value;
    }

    /**
     * 获取定位结果有效时间，毫秒
     *
     * @return
     */
    public int getLocationResultExpiredTime()
    {
        return mLocationResultExpiredTime;
    }

    /**
     * 设置定位结果有效时间，毫秒
     *
     * @param value
     */
    public void setLocationResultExpiredTime(int value)
    {
        mLocationResultExpiredTime = value;
    }

    /**
     * 设置定位方式
     *
     * @param providers
     */
    public void setLocationProviders(HsLocatinProvider[] providers)
    {
        this.mHsLocationProviders = providers;
    }

    /**
     * 获取最好的定位结果。
     *
     * @return
     */
    public Location getBestLocation()
    {
        return mLocationStores.getBestLocation();
    }

    // }}

    // {{ 定位与结束

    /**
     * 请求一次定位，此方法使用network与gps同时定位，定位分别在工作线程中进行。
     *
     * @param forceLocation
     *            是否强制进行定位，true 立即定位 false 如果有未过时的结果使用未过时的数据，如果没有进行定位
     * @param timeout
     *            每种定位方式的超时时间，毫秒单位
     * @throws InterruptedException
     */
    public void requestSingleLocation(boolean forceLocation, int timeout) throws Exception
    {
        Location bestLocation = this.getBestLocation();

        if (forceLocation || bestLocation == null)
        {
            hasDispatchEvent = false;

            //实例化定位线程
            if(mLocationThreads.size() == 0)
            {
                for (int i = 0; i < mHsLocationProviders.length; i++)
                {
                    LocationThread thread = new LocationThread(mHsLocationProviders[i],timeout);
                    mLocationThreads.add(thread);
                    thread.start();
                }
            }

            Thread.sleep(1000); //暂停1s，等待工作线程启动

            //发起定位
            for (LocationThread thread : mLocationThreads)
            {
                thread.startOnceLocation();
            }

        } else
        {
            this.locationChangedNotifies(bestLocation);
        }
    }

    /**
     * 关闭定位，释放相关资源
     */
    public void close()
    {
        for (LocationThread thread : mLocationThreads)
        {
            thread.close();
        }

        mLocationThreads.clear();
    }

    // }}

    // {{ 枚举

    /**
     * 定位时间分发频率
     *
     * @author zhaixuan
     *
     */
    public enum EDispatchEventFrequency
    {
        /**
         * 从不分发
         */
        None,
        /**
         * 第一次定位成功后分发
         */
        Once,
        /**
         * 每次定位成功后都分发
         */
        Continuous
    }

    /**
     * 是否需要定位信息
     *
     * @author zhaixuan
     *
     */
    public enum ENeedLocationInformation
    {
        /**
         * 不需要定位信息
         */
        None,

        /**
         * 可选定位信息
         */
        Optional,

        /**
         * 必须定位信息
         */
        Required
    }

    // }}

    // {{ 事件接口

    public interface HsLocationListener extends EventListener
    {
        public void onLocationChanged(Location location);
    }

    /**
     * 定位提供者类，增加了优先级信息
     *
     * @author zhaixuan
     *
     */
    public class HsLocatinProvider
    {
        private String mProvider;

        private int mPriority;

        public HsLocatinProvider(String provider, int priority)
        {
            mProvider = provider;
            mPriority = priority;
        }

        public String getProvider()
        {
            return mProvider;
        }

        public int getPriority()
        {
            return mPriority;
        }
    }

    // }}

    // {{ 内部类

    /**
     * 扩展Location，增加优先级。
     *
     * @author zhaixuan
     *
     */
    private static class HsLocation extends Location
    {
        public HsLocation(Location l, int priority)
        {
            super(l);

            setPriority(priority);
        }

        private int mPriority;

        public void setPriority(int value)
        {
            mPriority = value;
        }

        public int getPriority()
        {
            return mPriority;
        }

    }

    /**
     * 已定位信息存储类，线程安全
     *
     * @author zhaixuan
     *
     */
    private static class LocationResultStores extends Vector<HsLocation>
    {

        private static final long serialVersionUID = 6312467612795657024L;

        private Lock lock = new ReentrantLock();

        /**
         * 获取最好的定位结果
         *
         * @return
         */
        public Location getBestLocation()
        {
            reset();

            HsLocation bestLocation = null;

            if (this.size() > 0)
            {
                for (HsLocation location : this)
                {
                    if (bestLocation == null)
                    {
                        bestLocation = location;
                    } else
                    {
                        if (location.getPriority() > bestLocation.getPriority()) // 优先考虑优先级数值大的
                        {
                            bestLocation = location;
                        } else if (location.getPriority() == bestLocation
                                .getPriority())
                        {
                            if (location.getTime() > bestLocation.getTime()) // 相同优先级优先考虑日期近的。
                            {
                                bestLocation = location;
                            }
                        }
                    }
                }
            }

            if (bestLocation != null)
            {
                Log.i("存储的定位数量", String.valueOf(this.size()));

                Log.i("最好定位的时间", HsDate.TransDateToString(
                        "yyyy-MM-dd HH:mm:ss", bestLocation.getTime()));
            }

            return bestLocation;
        }

        /**
         * 清除过期的数据
         */
        private void reset()
        {
            lock.lock();

            for (int i = 0; i < this.size(); i++)
            {
                Location location = this.get(i);
                if (System.currentTimeMillis() - location.getTime() > mLocationResultExpiredTime)
                {
                    Log.i("CurrentTime", HsDate.TransDateToString(
                            "yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()));
                    Log.i("LocationTime", HsDate.TransDateToString(
                            "yyyy-MM-dd HH:mm:ss", location.getTime()));
//                    Log.i("CurrentTime - LocationTime",
//                            String.valueOf(String.valueOf(System
//                                    .currentTimeMillis() - location.getTime())));

                    this.remove(i);
                    i--;
                }
            }

            lock.unlock();
        }
    }

    /**
     * 定位工作线程
     *
     * @author zhaixuan
     *
     */
    private class LocationThread extends Thread
    {
        public final static int QUIT = 0x1000;

        public final static int LOCATION_START = 0x1001;

        public final static int LOCATION_COMPLETED = 0x1002;

        private LocationManager mLocationManager;

        /**
         * 定位侦听器
         */
        private LocationListener mListener;

        private Handler mLocationThreadHandler;

        /**
         * 定位方式
         */
        private HsLocatinProvider mProvider;

        /**
         * 定位超时时间，默认20秒
         */
        private int mTimeout = 20000;

        /**
         * 计时器
         */
        private Timer mTimer;

        public LocationThread(HsLocatinProvider provider,int timeout)
        {
            super(provider.getProvider() + "_LocationThread");

            mProvider = provider;

            mTimeout = timeout;

            mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        }

        @Override
        public void run()
        {
            if(mLocationManager.isProviderEnabled(mProvider.getProvider()))
            {
                Looper.prepare();

                Log.i("thread", "Start thread " + Thread.currentThread().getName());

                //配置集中处理方法
                mLocationThreadHandler = new Handler(new Handler.Callback()
                {
                    @Override
                    public boolean handleMessage(Message msg)
                    {
                        switch (msg.what)
                        {
                            case QUIT:

                                //移除侦听器
                                mLocationManager.removeUpdates(mListener);

                                //关闭消息循环
                                Log.i("thread", "Stop thread " + Thread.currentThread().getName());

                                Looper.myLooper().quit();
                                break;
                            case LOCATION_START: //发起一次定位
                                //定位一次
                                mLocationManager.requestSingleUpdate(mProvider.getProvider(), mListener, Looper.myLooper());

                                //设置超时
                                mTimer = new Timer(mProvider.getProvider() + "_Timers");
                                mTimer.schedule(new TimerTask()
                                {
                                    @Override
                                    public void run()
                                    {
                                        mLocationThreadHandler.sendEmptyMessage(LOCATION_COMPLETED);

                                        mTimer.purge();
                                        mTimer.cancel();
                                    }
                                }, mTimeout);
                                break;
                            case LOCATION_COMPLETED: //定位结束
                                //移除侦听器，关闭Timer
                                mLocationManager.removeUpdates(mListener);

                                //
                                Message message = Message.obtain();
                                message.what = HsLocationUtil.LOCATION;
                                message.obj = msg.obj;

                                mLocationUtilHandler.sendMessage(message);

                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });

                /**
                 * 定位侦听器
                 */
                mListener = new LocationListener()
                {
                    @Override
                    public void onStatusChanged(String provider, int status,
                                                Bundle extras) { }

                    @Override
                    public void onProviderEnabled(String provider) { }

                    @Override
                    public void onProviderDisabled(String provider) { }

                    @Override
                    public void onLocationChanged(Location location)
                    {
                        if (location != null)
                        {
                            Log.i("当前时间", HsDate.TransDateToString(
                                    "yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()));
                            Log.i("定位时间", HsDate.TransDateToString(
                                    "yyyy-MM-dd HH:mm:ss", location.getTime()));
                            // 用手机本地时间替代远端服务器定位时间，因为网络定位中的时间实际是远端服务器的时间，为了规避手机的时间与远端服务器的时间相差太大导致结果不准确，此处使用手机时间替换。
                            location.setTime(System.currentTimeMillis());
                            Log.i("定位时间替换后", HsDate.TransDateToString(
                                    "yyyy-MM-dd HH:mm:ss", location.getTime()));

                            Message msg = Message.obtain();
                            msg.what = LOCATION_COMPLETED;
                            msg.obj = new HsLocation(location, mProvider.getPriority());

                            mLocationThreadHandler.sendMessage(msg);
                        }
                    }
                };

                Looper.loop();
            }
        }

        /**
         * 结束线程
         */
        public void close()
        {
            if(mLocationThreadHandler != null)
            {
                mLocationThreadHandler.sendEmptyMessage(QUIT);
            }
        }

        /**
         * 发起一次定位
         */
        public void startOnceLocation()
        {
            if(mLocationThreadHandler != null)
            {
                mLocationThreadHandler.sendEmptyMessage(LOCATION_START);
            }
        }
    }

}
