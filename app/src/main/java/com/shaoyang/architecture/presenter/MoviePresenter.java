package com.shaoyang.architecture.presenter;

import com.shaoyang.architecture.model.entity.Subject;
import com.shaoyang.architecture.model.remote.MovieModel;
import com.shaoyang.architecture.presenter.subscriber.DefaultSubscriber;
import com.shaoyang.architecture.view.MovieView;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shaoyang on 2016/6/3.
 */
public class MoviePresenter implements Presenter{

    private MovieModel movieModel = new MovieModel();
    private MovieView movieView;
    public MoviePresenter(MovieView movieView) {
        this.movieView = movieView;
    }
    /**
     * 用于获取豆瓣电影Top250的数据
     * @param start 起始位置
     * @param count 获取长度
     */
    public void getTopMovie(int start, int count){
        Observable observable = movieModel.getTopMovie(start ,count);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MovieListSubscriber());
    }

    private final class MovieListSubscriber extends DefaultSubscriber<List<Subject>> {
        @Override
        public void onCompleted() {
            movieView.hideLoadingView();
        }

        @Override
        public void onError(Throwable e) {
            movieView.hideLoadingView();
            movieView.showLoadDataError(e);
        }

        @Override
        public void onNext(List<Subject> subjects) {
            movieView.showMovies(subjects);
        }

        @Override
        public void onStart() {
            movieView.showLoadingView();
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
