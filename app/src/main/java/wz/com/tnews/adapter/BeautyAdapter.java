package wz.com.tnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import wz.com.tnews.R;
import wz.com.tnews.bean.News;
import wz.com.tnews.interfaces.OnItemClickListener;


/**
 * Created by v_wangzhan on 2017/9/7.
 */

public class BeautyAdapter extends ItemClickAdapter {
    private String TAG = "BeautyAdapter";
    private Context context;
    private List<News> list;


    public BeautyAdapter(Context context, List<News> list) {
        this.context = context;
        this.list = list;
    }

    OnItemClickListener listener;

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.item_zhihu_home_first,
                    parent, false);
            holder = new ItemHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,
                    parent, false);
            holder = new FooterHolder(view);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position == getItemCount() - 1) {
            if (state == ItemClickAdapter.STATE_LOAD_FAIL) {
                ((FooterHolder) holder).tv.setVisibility(View.VISIBLE);
                ((FooterHolder) holder).tv.setText("加载失败，请重试");
            } else if (state == ItemClickAdapter.STATE_LOADING) {
                ((FooterHolder) holder).tv.setVisibility(View.VISIBLE);
                ((FooterHolder) holder).tv.setText("加载中");
            } else if (state == ItemClickAdapter.STATE_LOAD_NONE){
                Log.i("wangzhanzhan", "onBindViewHolder: ...没有更多数据了..");
                ((FooterHolder) holder).tv.setVisibility(View.VISIBLE);
                ((FooterHolder) holder).tv.setText("没有更多数据了");
            } else {
                ((FooterHolder) holder).tv.setVisibility(View.GONE);
            }
        } else {
            News news = list.get(position);
//            news = news.formatTime(news.time);
            ((ItemHolder) holder).tvTitle.setText(news.desc);
            Log.i(TAG, "onBindViewHolder: .." + news.url);
            Glide.with(context).load(news.url).diskCacheStrategy(DiskCacheStrategy.ALL).into(((ItemHolder) holder).img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position, v);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView img;

        public ItemHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }

    private class FooterHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public FooterHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(android.R.id.text1);
        }
    }

}
