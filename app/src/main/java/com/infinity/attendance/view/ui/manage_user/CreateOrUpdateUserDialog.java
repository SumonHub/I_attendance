package com.infinity.attendance.view.ui.manage_user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.infinity.attendance.R;
import com.infinity.attendance.data.model.Department;
import com.infinity.attendance.data.model.Designation;
import com.infinity.attendance.data.model.Role;
import com.infinity.attendance.data.model.User;
import com.infinity.attendance.utils.ConstantKey;
import com.infinity.attendance.utils.OnDataUpdateListener;
import com.infinity.attendance.utils.SharedPrefsHelper;
import com.infinity.attendance.viewmodel.DataViewModel;
import com.infinity.attendance.viewmodel.repo.ApiResponse;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import render.animations.Attention;
import render.animations.Render;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateOrUpdateUserDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateOrUpdateUserDialog extends DialogFragment {

    public static final String TAG = "CreateOrUpdateUser";
    private static final int FILE_REQUEST_CODE = 152;
    // TODO: Rename and change types of parameters
    private boolean isCreateNewUser = true;
    private String stringUser;

    private TextView headline;
    private TextView uid;
    private TextView inputName;
    private TextView inputPhn;
    private TextView inputEmail;
    private TextInputLayout dgLayout;
    private AutoCompleteTextView inputDesignation;
    private TextInputLayout dptLayout;
    private AutoCompleteTextView inputDepartment;
    private TextInputLayout joiningDateLayout;
    private AutoCompleteTextView inputJoiningDate;
    private AutoCompleteTextView inputBloodGroup;
    private TextInputLayout roleInputLayout;
    private AutoCompleteTextView inputUserRole;
    private TextInputLayout lInputCV;
    private TextInputEditText inputCv;
    private FloatingActionButton fabAddUser;
    private OnDataUpdateListener onDataUpdateListener;

    private int dpt_id, dg_id, roleId;
    private String nameText, phoneNumberText, emailText, designationText, departmentText, joiningDateText, bloodGroupText;
    private User superUser, selectedUser;
    private boolean isSelectJoiningDate = false, isSelectRole = false, isSelectDpt = false, isSelectDg = false;
    private ArrayList<MediaFile> mediaFiles;
    private String encodedFile;

    public CreateOrUpdateUserDialog() {
        // Required empty public constructor
    }

    public static CreateOrUpdateUserDialog newInstance(@Nullable Bundle bundle) {
        CreateOrUpdateUserDialog fragment = new CreateOrUpdateUserDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener onDataUpdateListener) {
        this.onDataUpdateListener = onDataUpdateListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        if (getArguments() != null) {
            isCreateNewUser = false;
            stringUser = getArguments().getString(ConstantKey.SELECTED_USER, null);
            Gson gson = new Gson();
            selectedUser = gson.fromJson(stringUser, User.class);
            Log.d(TAG, "selectedUser: " + selectedUser);
        }

        superUser = SharedPrefsHelper.getSuperUser(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_add_new_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabBack = view.findViewById(R.id.fabBack);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateOrUpdateUserDialog.this.dismiss();
            }
        });
        //
        headline = view.findViewById(R.id.headline);
        uid = view.findViewById(R.id.uid);
        roleInputLayout = view.findViewById(R.id.roleInputLayout);
        inputUserRole = view.findViewById(R.id.inputUserRole);
        inputName = view.findViewById(R.id.inputName);
        inputPhn = view.findViewById(R.id.inputPhn);
        inputEmail = view.findViewById(R.id.inputEmail);
        dgLayout = view.findViewById(R.id.dgLayout);
        inputDesignation = view.findViewById(R.id.inputDesignation);
        dptLayout = view.findViewById(R.id.dptLayout);
        inputDepartment = view.findViewById(R.id.inputDepartment);
        joiningDateLayout = view.findViewById(R.id.joiningDateLayout);
        inputJoiningDate = view.findViewById(R.id.inputJoiningDate);
        inputBloodGroup = view.findViewById(R.id.inputBloodGroup);
        lInputCV = view.findViewById(R.id.lInputCV);
        inputCv = view.findViewById(R.id.inputCv);
        fabAddUser = view.findViewById(R.id.fabAddUser);


        if (isCreateNewUser) {
            // add new user
            headline.setText("Add");
            uid.setVisibility(View.GONE);
        } else {
            // update user
            headline.setText("Edit");
            uid.setText("ID: " + selectedUser.getUid());
            inputName.setText(selectedUser.getName());
            inputPhn.setText(selectedUser.getPhone());
            inputEmail.setText(selectedUser.getEmail());

            //
            dpt_id = selectedUser.getDpt_id();
            if (dpt_id != 0) {
                isSelectDpt = true;
                inputDepartment.setText(selectedUser.getDpt_name());
                _configDg(dpt_id);
            }
            //
            dg_id = selectedUser.getDg_id();
            if (dg_id != 0) {
                isSelectDg = true;
                inputDesignation.setText(selectedUser.getDg_name());
            }
            //
            isSelectJoiningDate = true;
            inputJoiningDate.setText(selectedUser.getJoining_date());
            joiningDateText = selectedUser.getJoining_date();
            //
            inputBloodGroup.setText(selectedUser.getBlood_group());
            bloodGroupText = selectedUser.getBlood_group();
            //
            roleId = selectedUser.getRole_id();
            if (roleId != 0) {
                isSelectRole = true;
                inputUserRole.setText(selectedUser.getRole_name());
            }

        }

        //
        _configDpt();
        //
        _configJoiningDate();
        _configBloodGroupSpinner();
        _configRoleSelect();
        _configSelectCV();
        //
        _prepareFab();
    }

    private void _configSelectCV() {
        Log.d(TAG, "_configSelectCV: ");
        inputCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: ");

                Intent intent = new Intent(getContext(), FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setSelectedMediaFiles(mediaFiles)
                        .setShowFiles(true)
                        .setShowImages(false)
                        .setShowAudios(true)
                        .setShowVideos(false)
                        .setIgnoreNoMedia(false)
                        .enableVideoCapture(false)
                        .enableImageCapture(false)
                        .setIgnoreHiddenFile(true)
                        .setSuffixes("pdf")
                        .setMaxSelection(1)
                        .setSingleChoiceMode(true)
                        .build());
                startActivityForResult(intent, FILE_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {

            List<MediaFile> mediaFiles = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);

            if (mediaFiles.size() > 0) {
                inputCv.setText(mediaFiles.get(0).getName());
                Uri uri = mediaFiles.get(0).getUri();
                File file = new File(mediaFiles.get(0).getPath());

                encodedFile = getStringFile(file);

            } else {
                Toast.makeText(getContext(), "File not selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Converting File to Base64.encode String type using Method
    public String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile = output.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }

    private void _prepareFab() {

        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!_isValid()) {
                    Toast.makeText(getContext(), "Please fill required field(s).", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isCreateNewUser) {
                    // TODO: 8/22/2020 CREATE NEW USER
                    _createUser();

                } else {
                    // TODO: 8/22/2020 UPDATE USER
                    _updateUser();
                }
            }
        });
    }

    private void _updateUser() {
        User updatedUser = new User();

        updatedUser.setName(nameText);
        updatedUser.setPhone(phoneNumberText);
        updatedUser.setEmail(emailText);
        updatedUser.setDpt_id(dpt_id);
        updatedUser.setDg_id(dg_id);
        updatedUser.setJoining_date(joiningDateText);
        updatedUser.setBlood_group(bloodGroupText);
        updatedUser.setRole_id(roleId);

        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.updateUser(selectedUser.getApi_key(), updatedUser)
                .observe(getViewLifecycleOwner(), new Observer<ApiResponse>() {
                    @Override
                    public void onChanged(ApiResponse apiResponse) {
                        if (apiResponse != null && !apiResponse.isError()) {
                            onDataUpdateListener.onSuccessfulDataUpdated();
                            CreateOrUpdateUserDialog.this.dismiss();
                            Toast.makeText(getContext(), apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void _createUser() {
        User newUser = new User();
        newUser.setName(nameText);
        newUser.setPhone(phoneNumberText);
        newUser.setEmail(emailText);
        newUser.setDg_id(dg_id);
        newUser.setDpt_id(dpt_id);
        newUser.setJoining_date(joiningDateText);
        newUser.setBlood_group(bloodGroupText);
        newUser.setRole_id(roleId);
        newUser.setEncoded_cv(encodedFile);

        Log.d(TAG, "createUser: " + newUser);

        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.createUser(selectedUser.getApi_key(), newUser)
                .observe(getViewLifecycleOwner(), new Observer<ApiResponse>() {
                    @Override
                    public void onChanged(ApiResponse apiResponse) {
                        if (apiResponse != null && !apiResponse.isError()) {
                            onDataUpdateListener.onSuccessfulDataUpdated();
                            CreateOrUpdateUserDialog.this.dismiss();
                            Toast.makeText(getContext(), apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void _configJoiningDate() {
        // Get Current Date
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        inputJoiningDate.setOnClickListener(new View.OnClickListener() {
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
                                inputJoiningDate.setText(dateFormat.format(date));
                                joiningDateText = dateFormat.format(date);
                                isSelectJoiningDate = true;
                            }
                        }, mYear, mMonth, mDay).show();
            }
        });

    }

    private void _configBloodGroupSpinner() {
        List<String> list = Arrays.asList("AB+", "AB-", "A+", "A-", "B+", "B-", "O+", "O-");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, list);
        inputBloodGroup.setAdapter(adapter);
        inputBloodGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bloodGroupText = (String) adapterView.getItemAtPosition(i);
            }
        });
    }

    private void _configRoleSelect() {
        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.getAllUserRole(selectedUser.getApi_key())
                .observe(getViewLifecycleOwner(), new Observer<ApiResponse<Role>>() {
                    @Override
                    public void onChanged(ApiResponse<Role> roleApiResponse) {
                        if (roleApiResponse != null && !roleApiResponse.isError()) {
                            ArrayAdapter<Role> adapter = new ArrayAdapter<>(
                                    getContext(),
                                    android.R.layout.simple_dropdown_item_1line,
                                    roleApiResponse.getResults());

                            inputUserRole.setAdapter(adapter);
                            inputUserRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    isSelectRole = true;
                                    Role selectedRole = (Role) adapterView.getItemAtPosition(i);
                                    roleId = selectedRole.getId();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void _configDpt() {

        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.getAllDepartment(selectedUser.getApi_key())
                .observe(getViewLifecycleOwner(), new Observer<ApiResponse<Department>>() {
                    @Override
                    public void onChanged(ApiResponse<Department> roleApiResponse) {
                        if (roleApiResponse != null && !roleApiResponse.isError()) {
                            ArrayAdapter<Department> adapter = new ArrayAdapter<>(
                                    getContext(),
                                    android.R.layout.simple_dropdown_item_1line,
                                    roleApiResponse.getResults());

                            inputDepartment.setAdapter(adapter);
                            inputDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    isSelectDpt = true;
                                    isSelectDg = false;
                                    Department department = (Department) adapterView.getItemAtPosition(i);
                                    //
                                    dpt_id = department.getId();
                                    inputDesignation.setText(null);

                                    _configDg(dpt_id);
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void _configDg(int id) {
        DataViewModel dataViewModel = new DataViewModel();
        dataViewModel.getDesignation(superUser.getApi_key(), id)
                .observe(getViewLifecycleOwner(), new Observer<ApiResponse<Designation>>() {
                    @Override
                    public void onChanged(ApiResponse<Designation> designationApiResponse) {
                        if (designationApiResponse != null && !designationApiResponse.isError()) {
                            if (designationApiResponse.getResults().size() == 0) {
                                Toast.makeText(getContext(), "Designation not added yet!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            ArrayAdapter<Designation> adapter = new ArrayAdapter<>(
                                    getContext(),
                                    android.R.layout.simple_dropdown_item_1line,
                                    designationApiResponse.getResults());

                            inputDesignation.setAdapter(adapter);
                            inputDesignation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    isSelectDg = true;
                                    Designation designation = (Designation) adapterView.getItemAtPosition(i);
                                    //
                                    dg_id = designation.getId();

                                }
                            });
                        }
                    }
                });
    }

    private boolean _isValid() {
        boolean feedback = true;

        //-----------------name validation---------------------

        nameText = inputName.getText().toString();

        if (nameText.isEmpty()) {
            inputName.setError("Field cannot be empty");
            feedback = false;
        } else {
            inputName.setError(null);
        }

        //-----------------email validation---------------------

        emailText = inputEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (emailText.isEmpty()) {
            inputEmail.setError("Field cannot be empty");
            feedback = false;
        } else if (!emailText.matches(emailPattern)) {
            inputEmail.setError("Invalid email address");
            feedback = false;
        } else {
            inputEmail.setError(null);
        }

        //-----------------phone validation---------------------

        phoneNumberText = inputPhn.getText().toString();

        if (phoneNumberText.isEmpty()) {
            inputPhn.setError("Field cannot be empty");
            feedback = false;
        } else if (phoneNumberText.length() != 11) {
            inputPhn.setError("Invalid phone number");
        } else {
            inputPhn.setError(null);
        }

        //------------------- designation ----------------------

        if (!isSelectDg) {
            Render render = new Render(getContext());
            render.setAnimation(Attention.Wobble(dgLayout));
            render.start();
            feedback = false;
        }

        //------------------- department ---------------------

        if (!isSelectDpt) {
            Render render = new Render(getContext());
            render.setAnimation(Attention.Wobble(dptLayout));
            render.start();
            feedback = false;
        }

        //------------- JoiningDate ------------

        if (!isSelectJoiningDate) {
            Render render = new Render(getContext());
            render.setAnimation(Attention.Wobble(joiningDateLayout));
            render.start();
            feedback = false;
        }

        if (!isSelectRole) {
            Render render = new Render(getContext());
            render.setAnimation(Attention.Wobble(roleInputLayout));
            render.start();
            feedback = false;
        }

        if (encodedFile == null) {
            Render render = new Render(getContext());
            render.setAnimation(Attention.Wobble(lInputCV));
            render.start();
            feedback = false;
        }

        //passwordText = String.valueOf(ThreadLocalRandom.current().nextInt(111111, 999999));

        return feedback;
    }

}