package com.example.hanb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    private Button btn_login;
    private TextView btn_register;
    private TextView btn_find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.userNameText_login);
        et_pass = findViewById(R.id.pwText_login);
        btn_find = findViewById(R.id.find_login);
        btn_login = findViewById(R.id.signInButton_login);
        btn_register = findViewById(R.id.signUp_login);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btn_find.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FindActivity.class);
            startActivity(intent);
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // TODO : 인코딩 문제때문에 한글 DB인 경우 로그인 불가
                            System.out.println("hongchul" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 로그인에 성공한 경우
                                String userID = jsonObject.getString("userID");
                                String userPass = jsonObject.getString("userPassword");

                                Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("userPass", userPass);
                                startActivity(intent);
                            } else { // 로그인에 실패한 경우
                                Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) e.getX(), y = (int) e.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                if(imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(e);
    }
}