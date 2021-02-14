package com.infinity.i_attendance.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.infinity.i_attendance.DashboardActivity;
import com.infinity.i_attendance.R;
import com.infinity.i_attendance.api.ApiManager;
import com.infinity.i_attendance.api.http.ApiResponse;
import com.infinity.i_attendance.ui.individual_user.SingleUserActivity;
import com.infinity.i_attendance.ui.manage_user.model.User;
import com.infinity.i_attendance.ui.setting.role.Role;
import com.infinity.i_attendance.utils.ConstantKey;
import com.infinity.i_attendance.utils.SharedPrefsHelper;
import com.infinity.i_attendance.utils.Utils;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText etName, etPass;
    private String uid, password;
    private TextView register;
    private Button btnLogin;
    private SpinKitView spin_kit;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //
        // Initialize Realm (just once per application)
        Realm.init(this);
        //

        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        btnLogin = findViewById(R.id.btnLogin);
        etName = findViewById(R.id.inputPhn);
        etPass = findViewById(R.id.inputPass);
        register = findViewById(R.id.register);
        spin_kit = findViewById(R.id.spin_kit);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (/*_isValid()*/ true) {
                    ApiManager.requestLogin("1", "123456", new ApiManager.OnApiResponse<User>() {

                        @Override
                        public void onSucceed(ApiResponse<User> body) {
                            // TODO: 8/25/2020 save user information
                            User user = body.getResults().get(0);
                            //
                            SharedPrefsHelper.setSuperUser(LoginActivity.this, user);
                            //
                            Utils.showDialogWaiting(LoginActivity.this);
                            //
                            Intent intent;
                            if (user.getAccess_code() == Role.Setting.GENERAL.getAccess_code()) {
                                intent = new Intent(getApplicationContext(), SingleUserActivity.class);
                                intent.putExtra(ConstantKey.SELECTED_USER, new Gson().toJson(user));
                            } else {
                                intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            }
                            startActivity(intent);

                        }

                        @Override
                        public void onFailed(String errorMsg) {
                            Log.d(TAG, "onFailed: " + errorMsg);
                            Toast.makeText(LoginActivity.this, errorMsg.trim(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterDialog dialog = new RegisterDialog();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialog.show(ft, RegisterDialog.TAG);
            }
        });

    }

    private boolean _isValid() {
        boolean feedback = true;

        uid = etName.getText().toString();
        if (uid.isEmpty()) {
            etName.setError("Field can't be empty");
            feedback = false;
        }

        password = etPass.getText().toString();
        if (password.isEmpty()) {
            etPass.setError("Field can't be empty");
            feedback = false;
        }
        return feedback;
    }
}