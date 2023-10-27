package com.hfad.icode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class AuthorizationActivity extends AppCompatActivity {
    private static EditText editTextLogin;
    private static CustomEditText editTextPassword;
    static Button buttonSignIn;
    private Button buttonPopUpMenu;
    private HTTPHandler handler;
    private static Boolean authorization_error_login, authorization_error_password;
    private String selectedLanguage;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefs_editor;
    private static int textColor;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        initViews();

        buttonPopUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popUpMenu = new PopupMenu(AuthorizationActivity.this, buttonPopUpMenu);

                // Inflating popup menu from popup_menu.xml file
                popUpMenu.inflate(R.menu.popup_menu);
                popUpMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getTitle().toString()) {
                            case "English":
                                selectedLanguage = "en";
                                menuItem.setChecked(true);
                                break;
                            case "Русский":
                                selectedLanguage = "ru";
                                menuItem.setChecked(true);
                                break;
                            case "Тоҷикӣ":
                                selectedLanguage = "tg";
                                menuItem.setChecked(true);
                                break;
                        }
                        // Assume 'selectedLanguage' is the user-selected language code (e.g., "en" for English, "es" for Spanish)
                        Locale locale = new Locale(selectedLanguage);
                        Locale.setDefault(locale);
                        Configuration configuration = new Configuration();
                        configuration.locale = locale;
                        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                        recreate();

                        return true;
                    }
                });

                MenuBuilder menuBuilder = (MenuBuilder) popUpMenu.getMenu();
                MenuPopupHelper menuHelper = new MenuPopupHelper(
                        AuthorizationActivity.this,
                        menuBuilder,
                        buttonPopUpMenu
                );
                menuHelper.setForceShowIcon(true); // If you want to force showing icons
                menuHelper.show();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (
                        !TextUtils.isEmpty(editTextLogin.getText()) &&
                                !TextUtils.isEmpty(editTextPassword.getText()) &&
                                !authorization_error_login &&
                                !authorization_error_password &&
                                editTextLogin.getText().toString().equals("alice123") ||
                                editTextLogin.getText().toString().equals("bob45678")
                ) {
                    prefs_editor.putString("username", editTextLogin.getText().toString());
                    prefs_editor.apply();

                    Intent intent = new Intent(AuthorizationActivity.this, ChatActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        if (!prefs.getString("username", "").equals("")) {
            Intent intent = new Intent(AuthorizationActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();
        }

        int minInputLength = 8;
        int maxInputLength = 12; // Set your desired maximum input length
        editTextPassword.setFilters(new InputFilter[]{new LatinDigitsSymbolsInputFilter(minInputLength, maxInputLength)});
        editTextLogin.setFilters(new InputFilter[]{new LatinDigitsInputFilter(minInputLength, maxInputLength)});
    }


    private void initViews() {
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonPopUpMenu = findViewById(R.id.popupMenu);
        handler = new HTTPHandler();
        context = getApplicationContext();
        prefs = getApplicationContext().getSharedPreferences("auth", 0);
        prefs_editor = prefs.edit();
    }

    public static EditText getEditTextLogin() {
        return editTextLogin;
    }

    public static EditText getEditTextPassword() {
        return editTextPassword;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();

        if (TextUtils.isEmpty(editTextLogin.getText())) {
            editTextLogin.setBackgroundResource(R.drawable.default_edit_text_border);
        }
        if (TextUtils.isEmpty(editTextPassword.getText())) {
            editTextPassword.setBackgroundResource(R.drawable.default_edit_text_border);
        }
    }

    public static void changePasswordTextColor(int state) {

        switch (state) {
            case 1:
                textColor = ContextCompat.getColor(context, R.color.error);
                editTextPassword.setTextColor(textColor);
                editTextPassword.setRightIconColor(textColor);
                break;
            case 2:
                textColor = ContextCompat.getColor(context, R.color.active);
                editTextPassword.setTextColor(textColor);
                editTextPassword.setRightIconColor(textColor);
                break;
        }
    }

    public static void changeLoginTextColor(int state) {

        switch (state) {
            case 1:
                textColor = ContextCompat.getColor(context, R.color.error);
                editTextLogin.setTextColor(textColor);

                break;
            case 2:
                textColor = ContextCompat.getColor(context, R.color.active);
                editTextLogin.setTextColor(textColor);
                break;
        }
    }


    public static void setAuthorizationErrorLogin(Boolean authorization_error) {
        authorization_error_login = authorization_error;
    }


    public static void setAuthorizationErrorPassword(Boolean authorization_error) {
        authorization_error_password = authorization_error;
    }

}