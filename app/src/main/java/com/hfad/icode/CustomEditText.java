package com.hfad.icode;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.graphics.drawable.DrawableCompat;

public class CustomEditText extends AppCompatEditText {
    private boolean passwordVisibility = false;
    private final int RIGHT = 2;
    private final int LEFT = 0;

    public CustomEditText(@NonNull Context context) {
        super(context);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Drawable drawable = getCompoundDrawables()[RIGHT];
            if (drawable != null) {
                Rect bounds = drawable.getBounds();
                int x = (int) event.getX();
                if (x >= (getWidth() - getPaddingRight() - bounds.width())) {
                    togglePasswordVisibility();
                    performClick();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }



    private void togglePasswordVisibility() {
        int selection = getSelectionEnd();
        if (passwordVisibility) {
            setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.visibility_off, 0);
            setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordVisibility = false;
        } else {
            setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.visibility_on, 0);
            setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordVisibility = true;
        }
        setSelection(selection);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        // Дополнительный код, если необходимо, когда происходит "клик" на пользовательском виджете
        return true;
    }

    public void setRightIconColor(int color) {
        // Получите текущую иконку справа
        Drawable rightIcon = getCompoundDrawables()[RIGHT]; // Индекс 2 соответствует правой иконке
        Drawable leftIconDrawable = getCompoundDrawables()[LEFT];

        // Если правая иконка существует, измените ей цвет
        if (rightIcon != null) {
            Drawable wrappedDrawable = DrawableCompat.wrap(rightIcon);
            DrawableCompat.setTint(wrappedDrawable, color);

            // Установите измененную иконку снова в представление
            setCompoundDrawablesWithIntrinsicBounds(leftIconDrawable, null, wrappedDrawable, null);
        }
    }
}
