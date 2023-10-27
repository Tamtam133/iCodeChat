package com.hfad.icode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;

public class ChangeAvatarActivity extends AppCompatActivity {

    private Button buttonSave;
    private ImageButton imageButtonBack;
    private ImageView image1, image2, image3, image4, image5, image6;
    private String imageName, web_result, avatar;
    private HTTPHandler handler;
    private JSONArray array;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);
        initViews();

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeAvatarActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick("image1");
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick("image2");
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick("image3");
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick("image4");
            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick("image5");
            }
        });

        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick("image6");
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web_result = handler.makeRequest("http://94.198.220.135:3001/edit_user?username=" + prefs.getString("username", "") + "&useravatar=" + imageName);
                launchNextScreen(imageName);
            }
        });
        buttonSave.setClickable(false);
    }

    private void handleImageClick(String image) {
        switch (image) {
            case "image1":
                buttonSave.setClickable(true);
                image1.setBackgroundColor(getResources().getColor(R.color.grey));
                imageName = "image1";
                break;
            case "image2":
                buttonSave.setClickable(true);
                image2.setBackgroundColor(getResources().getColor(R.color.grey));
                imageName = "image2";
                break;
            case "image3":
                buttonSave.setClickable(true);
                image3.setBackgroundColor(getResources().getColor(R.color.grey));
                imageName = "image3";
                break;
            case "image4":
                buttonSave.setClickable(true);
                image4.setBackgroundColor(getResources().getColor(R.color.grey));
                imageName = "image4";
                break;
            case "image5":
                buttonSave.setClickable(true);
                image5.setBackgroundColor(getResources().getColor(R.color.grey));
                imageName = "image5";
                break;
            case "image6":
                buttonSave.setClickable(true);
                image6.setBackgroundColor(getResources().getColor(R.color.grey));
                imageName = "image6";
                break;
            default:
                buttonSave.setClickable(false);
        }
    }

    private void launchNextScreen(String imageName) {
        Intent intent = SettingsActivity.newIntent(this, imageName);
        startActivity(intent);
    }

    private void initViews() {
        buttonSave = findViewById(R.id.buttonSave);
        imageButtonBack = findViewById(R.id.imageButtonBack);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        image6 = findViewById(R.id.image6);
        handler = new HTTPHandler();
        prefs = getApplicationContext().getSharedPreferences("auth", 0);
    }
}