package com.kopoctc.projectwara;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        Button check = (Button)findViewById(R.id.btnCheck);
        Button sign = (Button)findViewById(R.id.btnSign);
        Button cancel = (Button)findViewById(R.id.btnCancel);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wishId = ((EditText)findViewById(R.id.inputWishId)).getText().toString();
                if(wishId.getBytes().length <= 0) {
                    Toast.makeText(getApplicationContext(), "ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "ID 사용 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
