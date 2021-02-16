package com.infinity.attendance.view.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.Role;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.ConstantKey;
import com.infinity.attendance.utils.SharedPrefsHelper;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.view.ui.DashboardActivity;
import com.infinity.attendance.view.ui.individual_user.SingleUserActivity;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;

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
                    DataViewModel dataViewModel = new DataViewModel();
                    dataViewModel.requestLogin("1", "123456").observe(LoginActivity.this, new Observer<ApiResponse<User>>() {
                        @Override
                        public void onChanged(ApiResponse<User> userApiResponse) {
                            if (userApiResponse != null && !userApiResponse.isError()) {
                                User user = userApiResponse.getResults().get(0);
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
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                            }
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