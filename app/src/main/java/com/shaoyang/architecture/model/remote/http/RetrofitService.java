package com.shaoyang.architecture.model.remote.http;

import android.support.annotation.NonNull;

import com.shaoyang.architecture.MyApplication;
import com.shaoyang.architecture.utils.NetUtil;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by shaoyang on 2016/6/3.
 */
public class RetrofitService {
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";
    //设缓存有效期为两天
    protected static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    protected static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
    protected static final String CACHE_CONTROL_NETWORK = "max-age=0";

    private static OkHttpClient mOkHttpClient;
    private static final int DEFAULT_TIMEOUT = 10;

    private RetrofitService() {
    }

    private volatile static RetrofitService instance = null;

    public static RetrofitService getInstance() {
        if (instance == null) {
            synchronized (RetrofitService.class) {
                if (instance == null) {
                    instance = new RetrofitService();
                }
            }
        }
        return instance;
    }

    private volatile static Retrofit retrofit = null;

    public static Retrofit createRetrofit() {
        if (retrofit == null) {
            synchronized (RetrofitService.class) {
                if (retrofit == null) {
                    initOkHttpClient();
                    retrofit = new Retrofit.Builder()
                            .client(mOkHttpClient)
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    // 配置OkHttpClient
    private static void initOkHttpClient() {
        if (mOkHttpClient == null) {
            // 因为BaseUrl不同所以这里Retrofit不为静态，但是OkHttpClient配置是一样的,静态创建一次即可
//            File cacheFile = new File(MyApplication.getContext().getCacheDir(),
//                    "HttpCache"); // 指定缓存路径
//            Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); // 指定缓存大小100Mb
            // 云端响应头拦截器，用来配置缓存策略
//            Interceptor rewriteCacheControlInterceptor = new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request();
//                    if (!NetUtil.isConnected(MyApplication.getContext())) {
//                        request = request.newBuilder()
//                                .cacheControl(CacheControl.FORCE_CACHE).build();
////                        Logger.e("no network");
//                    }
//                    Response originalResponse = chain.proceed(request);
//                    if (NetUtil.isConnected(MyApplication.getContext())) {
//                        //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
//                        String cacheControl = request.cacheControl().toString();
//                        return originalResponse.newBuilder()
//                                .header("Cache-Control", cacheControl)
//                                .removeHeader("Pragma").build();
//                    } else {
//                        return originalResponse.newBuilder().header("Cache-Control",
//                                "public, only-if-cached," + CACHE_STALE_SEC)
//                                .removeHeader("Pragma").build();
//                    }
//                }
//            };
            mOkHttpClient = new OkHttpClient();
//            mOkHttpClient.setCache(cache);
//            mOkHttpClient.networkInterceptors().add(rewriteCacheControlInterceptor);
//            mOkHttpClient.interceptors().add(rewriteCacheControlInterceptor);
            mOkHttpClient.setConnectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            //okhttp 3
//                    mOkHttpClient = new OkHttpClient.Builder().cache(cache)
//                            .addNetworkInterceptor(rewriteCacheControlInterceptor)
//                            .addInterceptor(rewriteCacheControlInterceptor)
//                            .connectTimeout(30, TimeUnit.SECONDS).build();

        }
    }
    /**
     * 根据网络状况获取缓存的策略
     *
     * @return
     */
    @NonNull
    public static String getCacheControl() {
        return NetUtil.isConnected(MyApplication.getContext()) ? CACHE_CONTROL_NETWORK : CACHE_CONTROL_CACHE;
    }
}
