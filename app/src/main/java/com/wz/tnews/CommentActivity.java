package com.wz.tnews;

import com.wz.tnews.bean.CommentObject;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by v_wangzhan on 2017/10/18.
 */
public class CommentActivity extends BaseActivity {
    private EditText mTitle;
    private EditText mContent;

    @Override
    public String getToolTitle() {
        return "我要反馈";
    }

    @NonNull
    @Override
    public View childView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_comment, null);
        mTitle = (EditText) view.findViewById(R.id.comment_title);
        mContent = (EditText) view.findViewById(R.id.comment_content);
        view.findViewById(R.id.comment_submit).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.comment_submit) {
            submitComment();
        } else {
            finish();
        }
    }


    private void submitComment() {
        if (!TextUtils.isEmpty(mTitle.getText().toString()) && !TextUtils.isEmpty(mContent.getText
                ().toString())) {
            CommentObject commentObject = new CommentObject();
            commentObject.setTitle(mTitle.getText().toString());
            commentObject.setContent(mContent.getText().toString());
            commentObject.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Toast.makeText(CommentActivity.this, "意见反馈成功，谢谢你的参与！", Toast
                                .LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CommentActivity.this, "网络可能有点问题，请稍候再试",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}
