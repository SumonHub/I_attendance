package com.infinity.attendance.view.ui.individual_user.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.data.model.UserInfo;
import com.infinity.attendance.utils.ConstantKey;
import com.infinity.attendance.utils.Utils;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import render.animations.Attention;
import render.animations.Render;

import static android.app.Activity.RESULT_OK;

public class PersonalDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PersonalDetailsFragment";
    private static final int FILE_REQUEST_CODE = 110;
    private final ArrayList<MediaFile> mediaFiles = new ArrayList<>();
    private MODE mode = MODE.DISABLE;
    private ImageView pPhoto;
    private TextInputLayout lFatherName, lMotherName, lDOB, lReligion, lGender, lMaritalStatus, lNationality, lNID;
    private TextInputEditText inputFatherName, inputMotherName, inputNationality, inputNIDNo;
    private AutoCompleteTextView inputDOB, inputReligion, inputGender, inputMaritalStatus;
    private boolean isSelectDOB = false, isSelectReligion = false, isSelectGender = false,
            isSelectMaritalStatus = false, isSelectPphoto = false;
    private String valueFatherName, valueMotherName, valueNationality, valueNIDNo, valueDOB,
            valueReligion, valueGender, valueMaritalStatus, valuePphoto;
    private User selectedUser;
    private UserInfo selectedUserInfo = new UserInfo();


    public PersonalDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PersonalDetailsFragment newInstance(String stringSelectedUser) {
        PersonalDetailsFragment fragment = new PersonalDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ConstantKey.SELECTED_USER, stringSelectedUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String stringSelectedUser = getArguments().getString(ConstantKey.SELECTED_USER);
            selectedUser = new Gson().fromJson(stringSelectedUser, User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lFatherName = view.findViewById(R.id.lFatherName);
        lMotherName = view.findViewById(R.id.lMotherName);
        lDOB = view.findViewById(R.id.lDOB);
        lReligion = view.findViewById(R.id.lReligion);
        lGender = view.findViewById(R.id.lGender);
        lMaritalStatus = view.findViewById(R.id.lMaritalStatus);
        lNationality = view.findViewById(R.id.lNationality);
        lNID = view.findViewById(R.id.lNID);

        inputFatherName = view.findViewById(R.id.inputFatherName);
        inputMotherName = view.findViewById(R.id.inputMotherName);
        inputNationality = view.findViewById(R.id.inputNationality);
        inputNIDNo = view.findViewById(R.id.inputNIDNo);
        //
        inputDOB = view.findViewById(R.id.inputDOB);
        inputReligion = view.findViewById(R.id.inputReligion);
        inputGender = view.findViewById(R.id.inputGender);
        inputMaritalStatus = view.findViewById(R.id.inputMaritalStatus);
        //
        enableUI(false);
        bindData();

        ExtendedFloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mode == MODE.DISABLE) {
                    // enable view
                    enableUI(true);
                    mode = MODE.ENABLE;
                    fab.setIcon(getContext().getDrawable(R.drawable.icon_double_tick_32));
                    fab.setBackgroundColor(Color.RED);
                    fab.setText("Done");

                } else if (mode == MODE.ENABLE) {
                    // disable view
                    enableUI(false);
                    mode = MODE.DISABLE;
                    fab.setIcon(getContext().getDrawable(R.drawable.icon_edit_64));
                    fab.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
                    fab.setText("Edit");

                    _buildSelectedUserInfo();

                    DataViewModel dataViewModel = new DataViewModel();
                    dataViewModel.addUserInfo(selectedUser.getApi_key(), selectedUserInfo).observe(getViewLifecycleOwner(), new Observer<ApiResponse>() {
                        @Override
                        public void onChanged(ApiResponse apiResponse) {
                            if (apiResponse != null && !apiResponse.isError()) {
                                Toast.makeText(getContext(), apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        pPhoto = view.findViewById(R.id.pPhoto);

        Glide.with(getContext())
                .asDrawable()
                .load(R.drawable.pp)
                .centerCrop()
                .circleCrop()
                .into(pPhoto);

        pPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setSelectedMediaFiles(mediaFiles)
                        .setShowFiles(true)
                        .setShowImages(true)
                        .setShowAudios(false)
                        .setShowVideos(false)
                        .setIgnoreNoMedia(false)
                        .enableVideoCapture(false)
                        .enableImageCapture(true)
                        .setIgnoreHiddenFile(true)
                        .setMaxSelection(1)
                        //  .setTitle("Select a file")
                        .setSingleChoiceMode(true)
                        .build());
                startActivityForResult(intent, FILE_REQUEST_CODE);

            }
        });

    }

    private void bindData() {

        DataViewModel dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        dataViewModel.getUserInfoByKeyLiveData(selectedUser.getApi_key()).observe(getViewLifecycleOwner(),
                new Observer<ApiResponse<UserInfo>>() {
                    @Override
                    public void onChanged(ApiResponse<UserInfo> userInfoApiResponse) {
                        if (userInfoApiResponse != null && !userInfoApiResponse.isError()) {
                            UserInfo userInfo = userInfoApiResponse.getResults().get(0);
                            selectedUserInfo = userInfo;
                            inputFatherName.setText(userInfo.getF_name());
                            inputMotherName.setText(userInfo.getM_name());
                            inputDOB.setText(userInfo.getDob());
                            inputReligion.setText(userInfo.getReligion());
                            inputGender.setText(userInfo.getGender());
                            inputMaritalStatus.setText(userInfo.getMarital_status());
                            inputNationality.setText(userInfo.getNationality());
                            inputNIDNo.setText(userInfo.getNid_no());
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.inputDOB:
                _configDOB();
                break;
            case R.id.inputReligion:
                _configReligion();
                break;
            case R.id.inputGender:
                _configGender();
                break;
            case R.id.inputMaritalStatus:
                _configMaritalStatus();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {

            List<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);

            if (mediaFiles.size() > 0) {
                try {

                    Bitmap srcBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mediaFiles.get(0).getUri());

                    Matrix matrix = new Matrix();
                    matrix.postRotate(-90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);

                    Glide.with(this)
                            .asBitmap()
                            .load(rotatedBitmap)
                            .centerCrop()
                            .circleCrop()
                            .into(pPhoto);

                    isSelectPphoto = true;
                    valuePphoto = Utils.convertBitmapToString(rotatedBitmap);
                    selectedUserInfo.setP_photo(valuePphoto);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getContext(), "File not selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void _buildSelectedUserInfo() {

        valueFatherName = inputFatherName.getText().toString();
        valueMotherName = inputMotherName.getText().toString();
        valueNationality = inputNationality.getText().toString();
        valueNIDNo = inputNIDNo.getText().toString();

        if (!valueFatherName.isEmpty()) {
            selectedUserInfo.setF_name(valueFatherName);
        }
        if (!valueMotherName.isEmpty()) {
            selectedUserInfo.setM_name(valueFatherName);
        }
        if (!valueNationality.isEmpty()) {
            selectedUserInfo.setNationality(valueFatherName);
        }
        if (!valueNIDNo.isEmpty()) {
            selectedUserInfo.setNid_no(valueFatherName);
        }

    }

    private void animateView(View view) {
        Render render = new Render(getContext());
        render.setAnimation(Attention.Wobble(view));
        render.start();
    }

    private void enableUI(boolean isEnable) {

        inputFatherName.setEnabled(isEnable);
        inputMotherName.setEnabled(isEnable);
        inputDOB.setEnabled(isEnable);
        inputReligion.setEnabled(isEnable);
        inputGender.setEnabled(isEnable);
        inputMaritalStatus.setEnabled(isEnable);
        inputNationality.setEnabled(isEnable);
        inputNIDNo.setEnabled(isEnable);

        if (isEnable) {
            inputDOB.setOnClickListener(this);
            _configDOB();
            inputReligion.setOnClickListener(this);
            _configReligion();
            inputGender.setOnClickListener(this);
            _configGender();
            inputMaritalStatus.setOnClickListener(this);
            _configMaritalStatus();
        } else {
            inputDOB.setOnClickListener(null);
            lDOB.setEndIconMode(TextInputLayout.END_ICON_NONE);
            inputReligion.setOnClickListener(null);
            lReligion.setEndIconMode(TextInputLayout.END_ICON_NONE);
            inputGender.setOnClickListener(null);
            lGender.setEndIconMode(TextInputLayout.END_ICON_NONE);
            inputMaritalStatus.setOnClickListener(null);
            lMaritalStatus.setEndIconMode(TextInputLayout.END_ICON_NONE);
        }
    }

    private void _configMaritalStatus() {
        Log.d(TAG, "_configMaritalStatus: ");
        String[] stringArray = new String[]{"Married", "Unmarried"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        stringArray);

        inputMaritalStatus.setAdapter(adapter);
        inputMaritalStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isSelectMaritalStatus = true;
                valueMaritalStatus = stringArray[i];
                //selectedUserInfo.setMarital_status(stringArray[i]);
            }
        });
        lMaritalStatus.setEndIconMode(TextInputLayout.END_ICON_DROPDOWN_MENU);
    }

    private void _configGender() {

        Log.d(TAG, "_configGender: ");
        String[] stringArray = new String[]{"Male", "Female"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        stringArray);

        inputGender.setAdapter(adapter);
        inputGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isSelectGender = true;
                valueGender = stringArray[i];
                selectedUserInfo.setGender(stringArray[i]);
            }
        });
        lGender.setEndIconMode(TextInputLayout.END_ICON_DROPDOWN_MENU);
    }

    private void _configReligion() {
        Log.d(TAG, "_configReligion: ");
        String[] stringArray = new String[]{"Islam", "Hindu", "Cristian", "Buddha"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        stringArray);

        inputReligion.setAdapter(adapter);
        inputReligion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isSelectReligion = true;
                valueReligion = stringArray[i];
                selectedUserInfo.setReligion(stringArray[i]);
            }
        });
        lReligion.setEndIconMode(TextInputLayout.END_ICON_DROPDOWN_MENU);

    }

    private void _configDOB() {
        Log.d(TAG, "_configDOB: ");
        // Get Current Date
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        inputDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                Date date = calendar.getTime();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                inputDOB.setText(dateFormat.format(date));
                                valueDOB = dateFormat.format(date);
                                selectedUserInfo.setDob(valueDOB);
                                isSelectDOB = true;
                            }
                        }, mYear, mMonth, mDay).show();
            }
        });

        lDOB.setEndIconMode(TextInputLayout.END_ICON_DROPDOWN_MENU);

    }

    private enum MODE {
        ENABLE,
        DISABLE
    }
}