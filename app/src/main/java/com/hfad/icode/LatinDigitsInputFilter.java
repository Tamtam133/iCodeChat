package com.hfad.icode;

import android.graphics.drawable.Drawable;
import android.text.InputFilter;

import java.util.regex.Pattern;

import android.text.Spanned;
import android.widget.EditText;

public class LatinDigitsInputFilter implements InputFilter {
    private EditText editTextLogin;
    private final int minLength;
    private final int maxLength;
    private final Pattern pattern;

    public LatinDigitsInputFilter(int minLength, int maxLength) {
        // Define the regular expression pattern for Latin letters and digits
        pattern = Pattern.compile("[a-zA-Z0-9]*");
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        String input = dest.toString() + source.subSequence(start, end);
        editTextLogin = AuthorizationActivity.getEditTextLogin();

        if (input.length() < minLength || input.length() > maxLength) {
            editTextLogin.setBackgroundResource(R.drawable.error_edit_text_border);
            AuthorizationActivity.changeLoginTextColor(1);
            AuthorizationActivity.setAuthorizationErrorLogin(true);
        } else {
            editTextLogin.setBackgroundResource(R.drawable.active_edit_text_border);
            AuthorizationActivity.changeLoginTextColor(2);
            AuthorizationActivity.setAuthorizationErrorLogin(false);
        }

        if (!pattern.matcher(source).matches()) {
            editTextLogin.setHint("Use latin alphabet or digits");
            editTextLogin.setBackgroundResource(R.drawable.error_edit_text_border);
            AuthorizationActivity.changeLoginTextColor(1);
            AuthorizationActivity.setAuthorizationErrorLogin(true);
            return "";
        }
        return null; // Accept the input
    }
}
