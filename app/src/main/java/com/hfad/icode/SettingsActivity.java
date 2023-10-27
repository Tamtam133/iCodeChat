package com.hfad.icode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private Button logOutButton;
    private static CircleImageView avatar;
    private ImageButton imageButtonBack, imageButtonEdit;
    private EditText editTextBirthday;
    private SharedPreferences prefs;
    private HTTPHandler handler;
    private String web_result, image;
    private JSONArray array;
    private SharedPreferences.Editor prefs_editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();

        web_result = handler.makeRequest("http://94.198.220.135:3001/get_user?username=" + prefs.getString("username", ""));
        try {
            array = new JSONArray(web_result);
            image = array.getJSONObject(0).getString("useravatar");
            getAvatar(image);
        }
        catch (JSONException e) {
        }
        imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ChangeAvatarActivity.class);
                startActivity(intent);
            }
        });

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });

        editTextBirthday.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String dateFormat = "##-##-####"; // Желаемый формат даты

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(current)) {
                    String clean = editable.toString().replaceAll("[^\\d.]", "");
                    String formatted = "";

                    int dateFormatIndex = 0;
                    int cleanIndex = 0;

                    while (dateFormatIndex < dateFormat.length() && cleanIndex < clean.length()) {
                        if (dateFormat.charAt(dateFormatIndex) == '#') {
                            formatted += clean.charAt(cleanIndex);
                            cleanIndex++;
                        } else {
                            formatted += dateFormat.charAt(dateFormatIndex);
                        }
                        dateFormatIndex++;
                    }

                    current = formatted;
                    editTextBirthday.setText(formatted);
                    editTextBirthday.setSelection(formatted.length());
                }
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs_editor.putString("username", "");
                prefs_editor.apply();

                Intent intent = new Intent(SettingsActivity.this, AuthorizationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String image = getIntent().getStringExtra("image");
        if (image != null) {
            getAvatar(image);
        }
    }

    public void getAvatar(String image) {
        String packageName = getPackageName(); // Получаем имя пакета приложения
        int resId = getResources().getIdentifier(image, "drawable", packageName);
        Drawable drawable = getResources().getDrawable(resId);
        avatar.setImageDrawable(drawable);
    }

    public static Intent newIntent(Context context, String image) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.putExtra("image", image);
        return intent;
    }


    private void initViews() {
        logOutButton = findViewById(R.id.logOutButton);
        avatar = findViewById(R.id.avatar);
        imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonEdit = findViewById(R.id.imageButtonEdit);
        editTextBirthday = findViewById(R.id.editTextBirthday);
        handler = new HTTPHandler();
        prefs = getApplicationContext().getSharedPreferences("auth", 0);
        prefs_editor = prefs.edit();
    }

}