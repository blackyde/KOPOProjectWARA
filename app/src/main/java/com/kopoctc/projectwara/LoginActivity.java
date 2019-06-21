package com.kopoctc.projectwara;

import android.app.Activity;
import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Base64;
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
//import java.security.MessageDigest;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        try{
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String key = new String(Base64.encode(md.digest(), 0));
//                Log.d("Hash key:", "!!!!!!!"+key+"!!!!!!");
//            }
//        } catch (Exception e){
//            Log.e("name not found", e.toString());
//        }

        Button login = (Button)findViewById(R.id.btnLogin);
        Button signUp = (Button)findViewById(R.id.btnSignUp);

        login.setOnClickListener(view -> {
            try {
                String result;
                CustomTask task = new CustomTask();
                EditText inputId = (EditText)findViewById(R.id.inputId);
                EditText inputPw = (EditText)findViewById(R.id.inputPw);
                String id = inputId.getText().toString();
                String pwd = inputPw.getText().toString();
                result = task.execute(id, pwd).get();
                Log.i("리턴 값", result);
                if(result.equals("true")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "ID 나 PW 가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

            }
        });

        signUp.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), SignActivity.class);

            startActivity(intent);
        });
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://35.221.189.227/login.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&pwd="+strings[1];
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
