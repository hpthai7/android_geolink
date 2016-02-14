package com.android.pfe.metravel.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.pfe.metravel.R;

public class StartupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mLoginBtn;
    private Button mSignupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        mLoginBtn = (Button) findViewById(R.id.stu_login);
        mSignupBtn = (Button) findViewById(R.id.stu_signup);

        mLoginBtn.setOnClickListener(this);
        mSignupBtn.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stu_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.stu_signup:
                break;
            default:
                break;
        }
    }
}
