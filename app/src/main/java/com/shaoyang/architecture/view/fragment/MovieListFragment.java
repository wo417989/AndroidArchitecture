package com.shaoyang.architecture.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.ProgressView;
import com.shaoyang.architecture.R;
import com.shaoyang.architecture.model.entity.HttpResult;
import com.shaoyang.architecture.model.entity.Subject;
import com.shaoyang.architecture.presenter.MoviePresenter;
import com.shaoyang.architecture.view.MovieView;
import com.shaoyang.architecture.view.adapter.MoviesListAdapter;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class MovieListFragment extends Fragment implements MovieView {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;

    @Bind(R.id.progress_pv_circular_inout_fragment)
    ProgressView progress_pv_circular_inout_fragment;

    private View contentView;
    private LinearLayoutManager mRecycleViewLayoutManager;

    private MoviesListAdapter mAdapter;
    private MoviePresenter moviePresenter;
    private List<Subject> currentSubjects;
    private int pageNum;
    public static final int countPerPage = 10;
    private boolean isCanLoadMore = false; //是否能加载更多

    public MovieListFragment() {
        // Required empty public constructor
    }

    public static MovieListFragment newInstance() {
        MovieListFragment fragment = new MovieListFragment();
        return fragment;
    }

    public void setPresenter( MoviePresenter moviePresenter) {
        this.moviePresenter = moviePresenter;
    }

    public void setView() {
        this.moviePresenter.setView(this);
    }

    //进行网络请求
    public void getMovie(int start ,int count){
        if (moviePresenter != null) {
            pageNum = 0;
            isCanLoadMore = false;
            mAdapter.setShowFooter(false);
            moviePresenter.getTopMovie(start , count);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this,contentView);

        initRecyclerView();
//        if (moviePresenter == null) {
//            moviePresenter = new MoviePresenter(this);
//        }
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (moviePresenter == null) {
//            moviePresenter.setView(this);
//        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initRecyclerView() {
        //设置分割线
//        recyclerview.addItemDecoration(new ListItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        mRecycleViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mRecycleViewLayoutManager);
        mAdapter = new MoviesListAdapter(getActivity(), new ArrayList<Subject>());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MoviesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Snackbar.make(view, "you click "+ currentSubjects.get(position).getTitle() ,Snackbar.LENGTH_SHORT).show();
            }
        });
        registerLoadMoreCallBack();
    }

    /**
     * 设置加载更多接口
     *
     */
    public void registerLoadMoreCallBack() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int lastVisibleItem;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mRecycleViewLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {

                    if (isCanLoadMore == true) {
                        //加载更多
//                        mAdapter.setShowFooter(true);
//                        mAdapter.notifyDataSetChanged();
                        pageNum = pageNum+countPerPage;
                        moviePresenter.getTopMovie(pageNum,countPerPage);
                    }
                }
            }
        });
    }

    @Override
    public void showMovies(HttpResult<List<Subject>> result) {
        if (pageNum == 0) {
            if (result.getSubjects() == null || result.getSubjects().size() == 0) {
                Snackbar.make(contentView,"没有数据", Snackbar.LENGTH_SHORT).show();
                return;
            }
            currentSubjects = result.getSubjects();
            mAdapter.setData(result.getSubjects());
            if (result.getTotal() > countPerPage ) {
                isCanLoadMore = true;
            }
        } else {
            if (result.getSubjects() == null || result.getSubjects().size() == 0) {
                isCanLoadMore = false;
                Snackbar.make(contentView,"没有更多数据", Snackbar.LENGTH_SHORT).show();
                mAdapter.setShowFooter(false);
                mAdapter.notifyDataSetChanged();
                return;
            }
            currentSubjects.addAll(result.getSubjects());
            mAdapter.addData(result.getSubjects());
            if (result.getTotal() % countPerPage != 0) {
                isCanLoadMore = false;
                mAdapter.setShowFooter(false);
            } else {
                isCanLoadMore = true;
                mAdapter.setShowFooter(true);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadDataError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Snackbar.make(contentView,"网络中断，请检查您的网络状态", Snackbar.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Snackbar.make(contentView, "网络中断，请检查您的网络状态", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(contentView, "error:" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoadingView() {
        progress_pv_circular_inout_fragment.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        progress_pv_circular_inout_fragment.setVisibility(View.GONE);
    }

    @Override
    public void showDataLoadCompleted() {

    }

    @Override
    public void showNetworkError() {

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
