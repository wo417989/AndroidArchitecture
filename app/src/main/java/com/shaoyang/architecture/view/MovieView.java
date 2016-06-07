package com.shaoyang.architecture.view;

import com.shaoyang.architecture.model.entity.HttpResult;
import com.shaoyang.architecture.model.entity.Subject;

import java.util.List;

/**
 * Created by shaoyang on 2016/6/6.
 */
public interface MovieView extends BaseView{
    void showMovies(HttpResult<List<Subject>> result);
    void showLoadDataError(Throwable e);
}
