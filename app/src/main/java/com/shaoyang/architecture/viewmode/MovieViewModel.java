package com.shaoyang.architecture.viewmode;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.orhanobut.logger.Logger;
import com.shaoyang.architecture.BR;
import com.shaoyang.architecture.presenter.MoviePresenter;

/**
 * Created by shaoyang on 2016/6/8.
 */
public class MovieViewModel extends BaseObservable{
    private final MoviePresenter mPresenter;
    private Context mContext;

    private int stateVar = 0;//0-invisible 1:visible

    public MovieViewModel(Context context, MoviePresenter presenter) {
        mContext = context;
        mPresenter = presenter;
    }

    @Bindable
    public int getStateVar() {
        Logger.e("get State "+this.stateVar);
        return this.stateVar;
    }

    public void setStateVar(int stateVar) {
        Logger.e("set State "+stateVar);
        this.stateVar = stateVar;
        notifyPropertyChanged(BR.stateVar);
//        notifyPropertyChanged(BR.movieViewModel);
    }

}
