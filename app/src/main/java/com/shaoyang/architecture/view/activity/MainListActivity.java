package com.shaoyang.architecture.view.activity;

import android.os.Bundle;
import android.widget.Button;

import com.shaoyang.architecture.R;
import com.shaoyang.architecture.presenter.MoviePresenter;
import com.shaoyang.architecture.view.fragment.MovieListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.rey.material.widget.SnackBar;

public class MainListActivity extends BaseActivity  {

    @Bind(R.id.click_me_BN)
    Button clickMeBN;

    private MovieListFragment movieListFragment;

//    private MoviePresenter moviePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycleview);
        ButterKnife.bind(this);

//        movieListFragment =
//                (MovieListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (savedInstanceState == null) {
            movieListFragment = MovieListFragment.newInstance();
            addFragment(getSupportFragmentManager() ,R.id.contentFrame ,movieListFragment);
        }
//        ((Button) (findViewById(R.id.click_me_BN))).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != movieListFragment) {
//                    movieListFragment.getMovie();
//                }
//            }
//        });
    }

    @OnClick(R.id.click_me_BN)
    public void onClick() {
        if (null != movieListFragment) {
            movieListFragment.setPresenter(new MoviePresenter());
            movieListFragment.setView();
            movieListFragment.getMovie(0, 10);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
