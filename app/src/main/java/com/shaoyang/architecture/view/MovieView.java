package com.shaoyang.architecture.view;

import com.shaoyang.architecture.model.entity.Subject;

import java.util.List;

/**
 * Created by shaoyang on 2016/6/6.
 */
public interface MovieView extends BaseView{
    void showMovies(List<Subject> subjects);
    void showLoadDataError(Throwable e);
}
