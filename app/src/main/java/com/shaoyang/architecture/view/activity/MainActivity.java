package com.shaoyang.architecture.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.shaoyang.architecture.R;
import com.shaoyang.architecture.model.entity.HttpResult;
import com.shaoyang.architecture.model.entity.Subject;
import com.shaoyang.architecture.presenter.MoviePresenter;
import com.shaoyang.architecture.view.MovieView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MovieView {

    @Bind(R.id.click_me_BN)
    Button clickMeBN;
    @Bind(R.id.result_TV)
    TextView resultTV;
    @Bind(R.id.progress_pv_circular_inout)
    ProgressView progress_pv_circular_inout;

    private MoviePresenter moviePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        moviePresenter = new MoviePresenter(this);
    }

    @OnClick(R.id.click_me_BN)
    public void onClick() {
        getMovie();
    }

    //进行网络请求
    private void getMovie(){
        moviePresenter.getTopMovie(0 , 10);
    }

    @Override
    public void showMovies(HttpResult<List<Subject>> result) {
        resultTV.setText(result.getSubjects().toString());
    }

    @Override
    public void showLoadDataError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoadingView() {
        progress_pv_circular_inout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        progress_pv_circular_inout.setVisibility(View.GONE);
    }

    @Override
    public void showDataLoadCompleted() {

    }

    @Override
    public void showNetworkError() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
