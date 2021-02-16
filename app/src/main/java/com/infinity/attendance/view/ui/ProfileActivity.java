package com.infinity.attendance.view.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.Utils;

public class ProfileActivity extends AppCompatActivity {

    private TextView headline;
    private TextView uid;
    private TextView inputName;
    private TextView inputPhn;
    private TextView inputEmail;
    private TextView inputDesignation;
    private TextView inputDepartment;
    private AutoCompleteTextView inputJoiningDate;
    private AutoCompleteTextView inputBloodGroup;
    private FloatingActionButton fabAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //
        Utils.showDialogWaiting(this);
        //
        headline = findViewById(R.id.headline);
        uid = findViewById(R.id.uid);
        inputName = findViewById(R.id.inputName);
        inputPhn = findViewById(R.id.inputPhn);
        inputEmail = findViewById(R.id.inputEmail);
        inputDesignation = findViewById(R.id.inputDesignation);
        inputDepartment = findViewById(R.id.inputDepartment);
        inputJoiningDate = findViewById(R.id.inputJoiningDate);
        inputBloodGroup = findViewById(R.id.inputBloodGroup);
        fabAddUser = findViewById(R.id.fabAddUser);

        SharedPreferences prefs = getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
        String stringUser = prefs.getString("USER", null);
        User superUser = new Gson().fromJson(stringUser, User.class);

        uid.setText("ID: " + superUser.getUid());
        inputName.setHint(superUser.getName());
        inputPhn.setHint(superUser.getPhone());
        inputEmail.setHint(superUser.getEmail());
        inputDesignation.setHint(superUser.getDg_name());
        inputDepartment.setHint(superUser.getDpt_name());
        inputJoiningDate.setText(superUser.getJoining_date());
        inputBloodGroup.setText(superUser.getBlood_group());


    }
}