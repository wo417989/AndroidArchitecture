package com.shaoyang.architecture.model.remote;

import com.shaoyang.architecture.model.entity.HttpResult;
import com.shaoyang.architecture.model.entity.Subject;
import com.shaoyang.architecture.model.remote.http.ApiException;
import com.shaoyang.architecture.model.remote.http.RetrofitService;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by shaoyang on 2016/6/3.
 */
public class MovieModel {
    /**
     * 用于获取豆瓣电影Top250的数据
     * @param subscriber  由调用者传过来的观察者对象
     * @param start 起始位置
     * @param count 获取长度
     */
    public Observable<HttpResult<List<Subject>>> getTopMovie(Subscriber<List<Subject>> subscriber, int start, int count){

        MovieService movieService = RetrofitService.getInstance().createRetrofit().create(MovieService.class);
        Observable observable = movieService.getTopMovie(start, count)
                .map(new HttpResultFunc<List<Subject>>());

        return observable;
    }

//    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
//        o.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(s);
//    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCount() == 0) {
                throw new ApiException(100);
            }
            return httpResult.getSubjects();
        }
    }
}
