package wz.com.tnews.net;


import android.util.Log;

/**
 * 默认的任务类
 *
 * @author John Kenrinus Lee
 * @version 2016-05-31
 * @see SimpleTaskExecutor.Task
 * @see SimpleTaskExecutor.AbstractSafeTask
 */
public class DefaultTask extends SimpleTaskExecutor.AbstractSafeTask {
    private final String mName;

    public DefaultTask(String name) {
        mName = name;
    }

    @Override
    public void doTask() {
        throw new UnsupportedOperationException("Not implement!");
    }

    @Override
    public final void onThrowable(Throwable t) {
        Log.w(mName, t);
    }

    @Override
    public final String getName() {
        return mName;
    }
}
