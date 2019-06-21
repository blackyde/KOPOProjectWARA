package com.kopoctc.projectwara;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
//import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        Button check = (Button)findViewById(R.id.btnCheck);
        Button sign = (Button)findViewById(R.id.btnSign);
        Button cancel = (Button)findViewById(R.id.btnCancel);

        check.setOnClickListener(view -> {
            String wishId = ((EditText)findViewById(R.id.inputWishId)).getText().toString();
            if(wishId.getBytes().length <= 0) {
                Toast.makeText(getApplicationContext(), "ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String result;
                    SignActivity.CheckTask task = new SignActivity.CheckTask();
                    result = task.execute(wishId).get();
                    Log.i("리턴 값", result);
                    if(result.equals("true")) {
                        Toast.makeText(getApplicationContext(), "ID 사용 가능합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "ID를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        ((EditText) findViewById(R.id.inputWishId)).setText("");
                    }

                } catch (Exception e) {

                }

            }
        });

        sign.setOnClickListener(view -> {
            String wishId = ((EditText)findViewById(R.id.inputWishId)).getText().toString();
            String wishPw = ((EditText)findViewById(R.id.inputWishPw)).getText().toString();
            String wishPw2 = ((EditText)findViewById(R.id.inputWishPw2)).getText().toString();
            String phone = ((EditText)findViewById(R.id.inputPhone)).getText().toString();
            String email = ((EditText)findViewById(R.id.inputEmail)).getText().toString();
            Boolean bool = true;
            if(wishId.getBytes().length <= 0) bool = false;
            if(wishPw.getBytes().length <= 0) bool = false;
            if(wishPw2.getBytes().length <= 0) bool = false;
            if(phone.getBytes().length <= 0) bool = false;
            if(email.getBytes().length <= 0) bool = false;
            if(bool == false) {
                Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                if(wishPw.equals(wishPw2) && bool) {
                    try {
                        String result;
                        SignActivity.SignTask task = new SignActivity.SignTask();

                        result = task.execute(wishId, wishPw, phone, email).get();
                        Log.i("리턴 값", result);

                        Toast.makeText(getApplicationContext(), "가입 되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (Exception e) {

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "패스워드가 서로 다릅니다.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        cancel.setOnClickListener(view -> onBackPressed());
    }

    class CheckTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://35.221.189.227/check.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0];
                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }

    class SignTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://35.221.189.227/sign.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&pwd="+strings[1]+"&phone="+strings[2]+"&email="+strings[3];
                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }
}
