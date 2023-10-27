package com.hfad.icode;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

public class SendMessageEditText extends AppCompatEditText {
    private Drawable iconDrawable;
    private OnClickListener iconClickListener;


    public SendMessageEditText(@NonNull Context context) {
        super(context);
        init();
    }

    public SendMessageEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SendMessageEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Получите иконку из атрибутов EditText (drawableRight)
        iconDrawable = getCompoundDrawables()[2]; // Индекс 2 соответствует иконке справа

        // Обработчик клика по иконке
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && iconClickListener != null) {
                    // Проверка на нажатие иконки
                    if (event.getRawX() >= (getRight() - getCompoundDrawables()[2].getBounds().width())) {
                        if (iconClickListener != null) {
                            iconClickListener.onClick(SendMessageEditText.this);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void setIconClickListener(OnClickListener listener) {
        iconClickListener = listener;
    }

    public void setIconColor(int color) {
        // Устанавливаем цвет иконке
        if (iconDrawable != null) {
            iconDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    public void setIconDrawable(Drawable drawable) {
        iconDrawable = drawable;
        setCompoundDrawablesWithIntrinsicBounds(null, null, iconDrawable, null);
    }
}
