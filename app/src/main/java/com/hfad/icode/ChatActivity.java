package com.hfad.icode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;


public class ChatActivity extends AppCompatActivity {
    private SendMessageEditText messageEditText;
    private Adapter adapter;
    private TextView usernameTextView;
    private ImageButton settingsButton;
    private ImageView avatarImageView;
    private String editBuffer, baseText, userName;
    private JSONArray array;
    private HTTPHandler handler;
    private RecyclerView recyclerView;
    private LinearLayoutManager layout_manager;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        timer();

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                editBuffer = charSequence.toString();
                editBuffer = editBuffer.trim();
                if (editBuffer.length() >= 1) {
                    Drawable newIconDrawable = ContextCompat.getDrawable(ChatActivity.this, R.drawable.send_on);
                    messageEditText.setIconDrawable(newIconDrawable);
                } else {
                    Drawable newIconDrawable = ContextCompat.getDrawable(ChatActivity.this, R.drawable.send_off);
                    messageEditText.setIconDrawable(newIconDrawable);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        messageEditText.setIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(messageEditText.getText()) && !messageEditText.getText().toString().trim().equals("")) {
                    baseText = handler.makeRequest("http://94.198.220.135:3001/insert_message?student_name=" + prefs.getString("username", "") + "&message=" + messageEditText.getText());
                    hideKeyboard();
                    messageEditText.setText("");
                    getMessages();
                }
            }
        });

    }

    public void getMessages() {
        String web_result = handler.makeRequest("http://94.198.220.135:3001/get_messages");
        try {
            array = new JSONArray(web_result);
            getUserName(array);
            adapter.setActivity(this);
            adapter.setArray(array);
            recyclerView.setLayoutManager(layout_manager);
            recyclerView.setAdapter(adapter);

            recyclerView.scrollToPosition(array.length() - 1);

        } catch (JSONException e) {
        }
    }

    private void getUserName(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            try {
                String name = array.getJSONObject(i).getString("student_name");

                if (!name.equals(prefs.getString("username", ""))) {
                    usernameTextView.setText(name);

                    String avatar = handler.makeRequest("http://94.198.220.135:3001/get_user?username=" + name);
                    String image = new JSONArray(avatar).getJSONObject(0).getString("useravatar");
                    String packageName = getPackageName(); // Получаем имя пакета приложения
                    int resId = getResources().getIdentifier(image, "drawable", packageName);
                    Drawable drawable = getResources().getDrawable(resId);
                    avatarImageView.setImageDrawable(drawable);

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void timer() {
        new CountDownTimer(600000, 10000) {
            public void onTick(long millisUntilFinished) {
                getMessages();
            }

            public void onFinish() {
                timer();
            }
        }.start();
    }


    private void initViews() {
        messageEditText = findViewById(R.id.messageEditText);
        settingsButton = findViewById(R.id.settingsButton);
        recyclerView = findViewById(R.id.recyclerView);
        avatarImageView = findViewById(R.id.avatarImageView);
        usernameTextView = findViewById(R.id.usernameTextView);
        handler = new HTTPHandler();
        prefs = getApplicationContext().getSharedPreferences("auth", 0);
        layout_manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new Adapter();
    }


}

