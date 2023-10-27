package com.hfad.icode;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.util.regex.Pattern;

public class LatinDigitsSymbolsInputFilter implements InputFilter {
    private EditText editTextPassword;
    private final int minLength;
    private final int maxLength;
    private final Pattern pattern;

    public LatinDigitsSymbolsInputFilter(int minLength, int maxLength) {
        // Define the regular expression pattern for Latin letters and digits
        pattern = Pattern.compile("[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]*");
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        String input = dest.toString() + source.subSequence(start, end);
        editTextPassword = AuthorizationActivity.getEditTextPassword();

        if (input.length() < minLength || input.length() > maxLength) {
            editTextPassword.setBackgroundResource(R.drawable.error_edit_text_border);
            AuthorizationActivity.changePasswordTextColor(1);
            AuthorizationActivity.setAuthorizationErrorPassword(true);

        } else {
            editTextPassword.setBackgroundResource(R.drawable.active_edit_text_border);
            AuthorizationActivity.changePasswordTextColor(2);
            AuthorizationActivity.setAuthorizationErrorPassword(false);
        }

        if (!pattern.matcher(source).matches()) {
            editTextPassword.setHint("Use latin alphabet or digits");
            editTextPassword.setBackgroundResource(R.drawable.error_edit_text_border);
            AuthorizationActivity.changePasswordTextColor(1);
            AuthorizationActivity.setAuthorizationErrorPassword(true);
            return "";
        }
        return null; // Accept the input
    }
}
