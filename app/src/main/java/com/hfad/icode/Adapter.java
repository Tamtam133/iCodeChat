package com.hfad.icode;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int MY_MESSAGE_TYPE = 0;
    private final int OTHER_USER_MESSAGE_TYPE = 1;
    private String name;
    private Boolean lastMessage;
    private SharedPreferences prefs;
    private ChatActivity activity;
    private Integer hour, minutes, day, month, previousMonth, previousDay, year;
    private Date currentDate, previousDate;
    private SimpleDateFormat format;
    private JSONArray array;
    private RecyclerView.ViewHolder holder;
    private JSONObject currentMessage, previousMessage;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MY_MESSAGE_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_item_message, parent, false);
            holder = new MyMessageViewHolder(view);
        } else if (viewType == OTHER_USER_MESSAGE_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_item_message, parent, false);
            holder = new OtherUserMessageViewHolder(view);
        }

        return holder;
    }


    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {

            currentMessage = array.getJSONObject(position);
            previousMessage = position > 0 ? array.getJSONObject(position - 1) : null;

            TimeZone timeZone = TimeZone.getTimeZone("Tajikistan");
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
            format.setTimeZone(timeZone);
            currentDate = format.parse(currentMessage.getString("ts"));

            if (previousMessage != null)
            {
                previousDate = format.parse(previousMessage.getString("ts"));
                lastMessage = false;
            } else {
                previousDate = format.parse(currentMessage.getString("ts"));
                lastMessage = true;
            }

            previousMonth = previousDate.getMonth() + 1;
            previousDay = previousDate.getDate();
            month = currentDate.getMonth() + 1;
            day = currentDate.getDate();
            year = currentDate.getYear()+ 1900;
            hour = currentDate.getHours();
            minutes = currentDate.getMinutes();


            String formattedMonth = String.format("%02d", month);
            String formattedDay = String.format("%02d", day);

            if (holder instanceof MyMessageViewHolder) {
                ((MyMessageViewHolder) holder).messageTextView.setText(currentMessage.getString("message"));
                ((MyMessageViewHolder) holder).timeTextView.setText(hour.toString() + ":" + minutes.toString());

                if (!Objects.equals(month, previousMonth) || !Objects.equals(day, previousDay) || lastMessage) {
                    ((MyMessageViewHolder) holder).dateTextView2.setText(formattedDay + "/" + formattedMonth + "/" + year);
                    ((MyMessageViewHolder) holder).dateTextView2.setVisibility(View.VISIBLE);
                }
            } else if (holder instanceof OtherUserMessageViewHolder) {
                ((OtherUserMessageViewHolder) holder).messageTextView.setText(currentMessage.getString("message"));
                ((OtherUserMessageViewHolder) holder).timeTextView.setText(hour.toString() + ":" + minutes.toString());

                if (!Objects.equals(month, previousMonth) || !Objects.equals(day, previousDay) || lastMessage) {
                    ((OtherUserMessageViewHolder) holder).dateTextView1.setText(formattedDay + "/" + formattedMonth + "/" + year);
                    ((OtherUserMessageViewHolder) holder).dateTextView1.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static class MyMessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView, timeTextView, dateTextView2;

        public MyMessageViewHolder(View itemView) {
            super(itemView);
            this.messageTextView = itemView.findViewById(R.id.messageTextView);
            this.timeTextView = itemView.findViewById(R.id.timeTextView);
            this.dateTextView2 = itemView.findViewById(R.id.dateTextView);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class OtherUserMessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView, timeTextView, dateTextView1;

        public OtherUserMessageViewHolder(View itemView) {
            super(itemView);
            this.messageTextView = itemView.findViewById(R.id.messageTextView);
            this.timeTextView = itemView.findViewById(R.id.timeTextView);
            this.dateTextView1 = itemView.findViewById(R.id.dateTextView);

        }
    }


    @Override
    public int getItemViewType(int position) {
        try {
            name = array.getJSONObject(position).getString("student_name");
            prefs = activity.getSharedPreferences("auth", 0);

            if (name.equals(prefs.getString("username", ""))) {
                return MY_MESSAGE_TYPE;
            } else {
                return OTHER_USER_MESSAGE_TYPE;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public void setActivity(ChatActivity activity) {
        this.activity = activity;
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }


}
