package com.example.familymap.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familymap.R;
import com.example.familymap.web.LoginTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Request.EventRequest;
import Request.LoginRequest;
import Request.PersonRequest;
import Request.RegisterRequest;
import Result.LoginResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private static final String LOG_TAG = "LoginFragment";
    private static final String STATUS_KEY = "StatusKey";

    private EditText editHostname;
    private EditText editPort;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private Button male;
    private Button female;
    private Button login;
    private Button register;
    private String gender = null;
    private String personID = null;
    private String authToken = null;


    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        editHostname = (EditText)v.findViewById(R.id.editHostname);
//        editHostname.setText("10.5.205.3");
        editPort = (EditText)v.findViewById(R.id.editPort);
//        editPort.setText("8080");
        editUsername = (EditText)v.findViewById(R.id.editUsername);
//        editUsername.setText("sheila");
        editPassword = (EditText)v.findViewById(R.id.editPassword);
//        editPassword.setText("parker");
        editFirstName = (EditText)v.findViewById(R.id.editFirstName);
        editLastName = (EditText)v.findViewById(R.id.editLastName);
        editEmail = (EditText)v.findViewById(R.id.editEmail);
        male = (Button)v.findViewById(R.id.male);
        male.setOnClickListener((listen) -> {
            gender = "m";
        });
        female = (Button)v.findViewById(R.id.female);
        female.setOnClickListener((listen) -> {
            gender = "f";
        });
        login = (Button)v.findViewById(R.id.login);
        login.setOnClickListener((listen) ->{
            try {
                Handler uiThreadMessageHandler =  new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        Bundle bundle = msg.getData();
                        String status = bundle.getString(STATUS_KEY);
                        Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                        if (status.toLowerCase().contains("success")){
                            ((MainActivity)getActivity()).loginSuccessful();
                        }
                    }
                };
                LoginTask task = new LoginTask(uiThreadMessageHandler, getLogin(), getServer());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        });

        register = (Button)v.findViewById(R.id.register);
        register.setOnClickListener((listen) ->{
            try {
                Handler uiThreadMessageHandler =  new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        Bundle bundle = msg.getData();
                        String status = bundle.getString(STATUS_KEY);
                        Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                        if (status.toLowerCase().contains("success")){
                            ((MainActivity)getActivity()).loginSuccessful();
                        }
                    }
                };
                LoginTask task = new LoginTask(uiThreadMessageHandler, getRegister(), getServer());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        });


        return v;
    }

    private LoginRequest getLogin() {
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        LoginRequest login = new LoginRequest(username, password);
        return login;
    }

    private RegisterRequest getRegister() {
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        String email = editEmail.getText().toString();
        String firstName = editFirstName.getText().toString();
        String lastName = editLastName.getText().toString();
        RegisterRequest register = new RegisterRequest(username, password, email, firstName, lastName, gender);
        return register;
    }

    private String getServer() {
        return editHostname.getText().toString() + ":" + editPort.getText().toString();
    }

}