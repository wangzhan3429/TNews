package wz.com.tnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wz.com.tnews.R;
import wz.com.tnews.bean.News;
import wz.com.tnews.interfaces.OnItemClickListener;


/**
 * 发现Discover里的子Fragment  Adapter
 * Created by YoKeyword on 16/2/1.
 */
public class NormalAdapter extends ItemClickAdapter {
    private List<String> mItems = new ArrayList<>();
    private LayoutInflater mInflater;

    private OnItemClickListener mClickListener;
    private List<News> newsList;
    private Context context;

    public NormalAdapter(Context context, List<News> newsList) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.newsList = newsList;
    }

    public void setDatas(List<String> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_FOOTER) {
            view = mInflater.inflate(android.R.layout.simple_list_item_1,
                    parent, false);
            holder = new FooterHolder(view);
        } else {
            view = mInflater.inflate(R.layout.item_pager, parent, false);
            holder = new MyViewHolder(view);
            final RecyclerView.ViewHolder finalHolder = holder;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = finalHolder.getAdapterPosition();
                    if (mClickListener != null) {
                        mClickListener.onItemClick(position, v);
                    }
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            if (state == ItemClickAdapter.STATE_LOAD_FAIL) {
                ((FooterHolder) holder).tv.setVisibility(View.VISIBLE);
                ((FooterHolder) holder).tv.setText("加载失败，请重试");
            } else if (state == ItemClickAdapter.STATE_LOADING) {
                ((FooterHolder) holder).tv.setVisibility(View.VISIBLE);
                ((FooterHolder) holder).tv.setText("加载中");
            } else if (state == ItemClickAdapter.STATE_LOAD_NONE) {
                Log.i("wangzhanzhan", "onBindViewHolder: ...没有更多数据了..");
                ((FooterHolder) holder).tv.setVisibility(View.VISIBLE);
                ((FooterHolder) holder).tv.setText("没有更多数据了");
            } else {
                ((FooterHolder) holder).tv.setVisibility(View.GONE);
            }
        } else {
            News news = newsList.get(position);
//            if (news != null) {
//                news = news.formatTime(news.time);
//            }
            String desc = news.desc;
            String author = news.author;
            String time = news.time;
            if (time.trim().contains("T")) {
                time = time.split("T")[0];
            }
            Log.i("wangzhanzhan", "onBindViewHolder: ..." + news.timestemp);
            ((MyViewHolder) holder).tvTitle.setText(desc);
            ((MyViewHolder) holder).tvAuthor.setText(author == null ? "匿名" : author);
            ((MyViewHolder) holder).tvTime.setText(time);
            ((MyViewHolder) holder).imgConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() != null && String.valueOf(v.getTag()) == "true") {
                        ((MyViewHolder) holder).imgConnect.setImageResource(R.drawable
                                .list_open_collect);
                        v.setTag("false");
//                         update database TODO
                    } else {
                        ((MyViewHolder) holder).imgConnect.setImageResource(R.drawable
                                .list_open_collected);
                        v.setTag("true");
//                        update  database TODO
                    }

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvTime;
        private ImageView imgConnect;
        ;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            imgConnect = (ImageView) itemView.findViewById(R.id.img_connect);
        }
    }

    private class FooterHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public FooterHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(android.R.id.text1);
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


}
