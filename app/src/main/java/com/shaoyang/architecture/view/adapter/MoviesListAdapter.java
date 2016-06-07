package com.shaoyang.architecture.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaoyang.architecture.R;
import com.shaoyang.architecture.model.entity.Subject;
import com.shaoyang.architecture.utils.GlideUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <Pre>
 * 新闻列表适配器
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p/>
 *          Create by 2016/1/27 16:26
 */
public class MoviesListAdapter extends RecyclerView.Adapter {
    private static final int TYPE_ITEM = 0x00;//内容
    private static final int TYPE_FOOTER = 0x01;//加载更多

    private Activity context;
    private List<Subject> subjects;


    /**
     * 条目点击监听
     */
    private OnItemClickListener mOnItemClickListener;

    /**
     * 是否显示加载更多视图
     */
    private boolean mShowFooter = false;

    public MoviesListAdapter(Activity context, List<Subject> subjects) {
        this.context = context;
        this.subjects = subjects;
    }

    /**
     * 刷新界面添加数据
     */
    public void setData(List<Subject> subjects) {
        if (this.subjects != null && this.subjects.size()>0) {
            this.subjects.clear();
        }
        this.subjects.addAll(subjects);
    }

    /**
     * 加载更多添加数据
     */
    public void addData(List<Subject> subjects) {
        this.subjects.addAll(subjects);
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (!mShowFooter) {
            return TYPE_ITEM;
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_movie_list, parent, false);
            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Subject subject = subjects.get(position);
            ItemViewHolder holder1 = (ItemViewHolder) holder;

            if (subject.getImages() != null) {
                GlideUtil.loadImage(context, subject.getImages().getMedium(), holder1.imageView);
            } else {
                GlideUtil.loadImage(context, "", holder1.imageView);
            }
            holder1.title.setText(subject.getTitle());
            if (subject.getCasts() != null && subject.getCasts().size() > 0) {
                holder1.desc.setText("主演："+subject.getCasts().get(0).getName());
            }
        }
    }

    @Override
    public int getItemCount() {
        int begin = mShowFooter ? 1 : 0;
        if (this.subjects == null) {
            return begin;
        }
        return this.subjects.size() + begin;
    }

    public void setShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.tv_desc)
        TextView desc;
        @Bind(R.id.iv_desc)
        ImageView imageView;
        @Bind(R.id.tv_title)
        TextView title;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(v, this.getPosition());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 点击条目接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
