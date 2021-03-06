package com.example.dolphin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class FireButton extends androidx.appcompat.widget.AppCompatButton implements View.OnClickListener{
    private IFireListener fireListener;
    public FireButton(Context context) {
        super(context);
        init();
    }

    public FireButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FireButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (null != fireListener) {
            fireListener.onFire();
        }
    }

    public void setFireListener(IFireListener fireListener) {
        this.fireListener = fireListener;
    }

    public interface IFireListener {
        void onFire();
    }
}
