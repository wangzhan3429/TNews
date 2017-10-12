package wz.com.tnews.adapter;

import android.app.Application;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import wz.com.tnews.interfaces.OnItemClickListener;


/**
 * Created by v_wangzhan on 2017/9/7.
 */

public abstract class ItemClickAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public boolean isLoading = false;
    public int TYPE_ITEM = 0;
    public int TYPE_FOOTER = 1;
    public String tips = "加载中";
    public static final int STATE_LOADING = 10;
    public static final int STATE_LOAD_SUCCESS = 11;
    public static final int STATE_LOAD_FAIL = 12;
    public static final int STATE_LOAD_NONE = 13;

    public static int state;

    public abstract void setOnItemClickListener(OnItemClickListener listener);

    public void startLoading(String tips) {
        isLoading = true;
        this.tips = tips;
    }

    public void setLoadState(int state) {
        this.state = state;
    }


    public void stopLoading() {
        isLoading = false;
    }
}
