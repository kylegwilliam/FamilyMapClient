package com.example.familymapclient;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.internal.TextWatcherAdapter;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.transform.Result;

import Request.AllEventRequest;
import Request.LoginRequest;
import Request.PersonFamilyRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.PersonFamilyResult;
import Result.RegisterResult;
import model.Event;
import model.Person;

import Result.AllEventResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to
 * create an instance of this fragment.
 */

public class LoginFragment extends Fragment {

    private Button registerButton;
    private Button signInButton;

    RadioButton maleButton;
    RadioButton femaleButton;

    private EditText serverHost;
    private EditText serverPort;
    private EditText userName;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private String gender = "m";


    public LoginFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        maleButton = view.findViewById(R.id.radio_one);

        RadioButton maleButton = view.findViewById(R.id.radio_one);
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "m";
            }
        });

        femaleButton = view.findViewById(R.id.radio_two);

        RadioButton femaleButton = view.findViewById(R.id.radio_two);
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "f";
            }
        });

        serverHost = view.findViewById(R.id.serverHostField);
        serverPort = view.findViewById(R.id.serverPortField);
        userName = view.findViewById(R.id.usernameField);
        password = view.findViewById(R.id.passwordField);
        firstName = view.findViewById(R.id.firstNameField);
        lastName = view.findViewById(R.id.lastNameField);
        email = view.findViewById(R.id.emailField);
        signInButton = view.findViewById(R.id.signInButton);
        registerButton = view.findViewById(R.id.registerButton);
        maleButton = view.findViewById(R.id.radio_one);
        femaleButton = view.findViewById(R.id.radio_two);


        serverHost.addTextChangedListener(loginTextWatcher);
        serverPort.addTextChangedListener(loginTextWatcher);
        userName.addTextChangedListener(loginTextWatcher);
        password.addTextChangedListener(loginTextWatcher);
        firstName.addTextChangedListener(loginTextWatcher);
        lastName.addTextChangedListener(loginTextWatcher);
        email.addTextChangedListener(loginTextWatcher);
        maleButton.addTextChangedListener(loginTextWatcher);
        femaleButton.addTextChangedListener(loginTextWatcher);


        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            private static final String LOG_TAG = "log_tag";

            @Override
            public void onClick(View v) {

                try {
                    Handler uiThreadMessageHandler = new Handler() {

                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();

                            if (bundle.getBoolean("key")) {

                                //Toast.makeText(getActivity(), bundle.getString("firstName") + " " +
                                //        bundle.getString("lastName"), Toast.LENGTH_LONG).show();

                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragmentFrameLayout, new MapFragment(null, false)).commit();

                            }
                            else {
                                Toast.makeText(getContext(), "Person wasn't registered", Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(userName.getText().toString(), password.getText().toString(),
                            email.getText().toString(), firstName.getText().toString(), lastName.getText().toString(), gender);

                    RegisterTask registerTask = new RegisterTask(uiThreadMessageHandler, registerRequest,serverHost.getText().toString(),
                            serverPort.getText().toString());

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(registerTask);

                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage(), e);

                }
            }
        });


        Button signInButton = view.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Handler uiThreadMessageHandler = new Handler() {

                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();

                            if (bundle.getBoolean("key")) {

                                //Toast.makeText(getActivity(), bundle.getString("firstName") + " " +
                                //                    bundle.getString("lastName"), Toast.LENGTH_LONG).show();

                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragmentFrameLayout, new MapFragment(null, false)).commit();

                            }
                            else {
                                Toast.makeText(getActivity(), "Login wasn't successful", Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(userName.getText().toString(), password.getText().toString());

                    LoginTask loginTask = new LoginTask(uiThreadMessageHandler, loginRequest,
                                                                serverHost.getText().toString(), serverPort.getText().toString());

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(loginTask);

                } catch (Exception e) {
                    //Log.e(LOG_TAG, e.getMessage(), e);
                }
            }
        });

        checkForEmptyValues();

        return view;
    }

    private static class RegisterTask implements Runnable {

        private Handler messageHandler;
        private RegisterRequest request;
        private String host;
        private String port;

        public RegisterTask(Handler messageHandler, RegisterRequest request, String host, String port) {
            this.messageHandler = messageHandler;
            this.request = request;
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {

            Log.d("Login Fragment", "Run");
            ServerProxy serverProxy = new ServerProxy();
            RegisterResult result = serverProxy.registerResult(request, host, port);
            Person registerPerson = null;

            if (result.getAuthToken() == null) {
                sendMessage(result, registerPerson);
            }

            String authToken = result.getAuthToken();

            DataCache dataCache;
            dataCache = DataCache.getInstance();

            if (result.isSuccess()) {

                if (serverProxy.getFamily(authToken, host, port) != null) {
                    //Take longer try to clean it up.
                    PersonFamilyResult familyResult = serverProxy.getFamily(authToken, host, port);
                    AllEventResult eventResult = serverProxy.event(authToken, host, port);

                    if (familyResult.getData() != null) {
                        Log.d("Login Fragment", "getData");

                        for (int i = 0; i < familyResult.getData().size(); i++) {
                            if (familyResult.getData().get(i).getPersonID().equals(result.getPersonID())) {
                                Person person = (Person) familyResult.getData().get(i);
                                registerPerson = person;
                                dataCache.setPerson(person);
                            }
                            else {
                                Person person = (Person) familyResult.getData().get(i);
                                dataCache.setPerson(person);
                            }
                        }

                        for (int i = 0; i < eventResult.getData().size(); i++) {
                            Event event = (Event) eventResult.getData().get(i);
                            dataCache.setEvent(event);
                        }
                    }
                }
            }

            sendMessage(result, registerPerson);
        }

        private void sendMessage(RegisterResult result, Person registerPerson) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean("key", result.isSuccess());

            if (registerPerson != null) {
                messageBundle.putString("firstName", registerPerson.getFirstName());
                messageBundle.putString("lastName", registerPerson.getLastName());
            }

            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }
    }
//DataCash.getInstance();
    private static class LoginTask implements Runnable {

        private Handler messageHandler;
        private LoginRequest request;
        private String host;
        private String port;

        public LoginTask(Handler messageHandler, LoginRequest request, String host, String port) {
            this.messageHandler = messageHandler;
            this.request = request;
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {

            ServerProxy serverProxy = new ServerProxy();
            LoginResult result = serverProxy.login(request, host, port);
            Person userPerson = null;

            if (result.authtoken == null) {
                sendMessage(result, userPerson);

            }

            String authToken = result.getAuthToken();

            DataCache dataCache;
            dataCache = DataCache.getInstance();

            if (serverProxy.getFamily(authToken, host, port) != null) {
                PersonFamilyResult familyResult = serverProxy.getFamily(authToken, host, port);
                AllEventResult eventResult = serverProxy.event(authToken, host, port);

                //loop to check persons id
                if (familyResult.getData() != null) {

                    for (int i = 0; i < familyResult.getData().size(); i++) {
                        if (familyResult.getData().get(i).getPersonID().equals(result.getPersonID())) {
                            Person person = (Person) familyResult.getData().get(i);
                            userPerson = person;
                            dataCache.setPerson(person);
                        }
                        else {
                            Person person = (Person) familyResult.getData().get(i);
                            dataCache.setPerson(person);
                        }

                    }
                    for (int i = 0; i < eventResult.getData().size(); i++) {
                        Event event = (Event) eventResult.getData().get(i);
                        dataCache.setEvent(event);
                    }
                }
            }

            sendMessage(result, userPerson);
        }

        private void sendMessage(LoginResult result, Person userPerson) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean("key", result.isSuccess());

            if (userPerson != null) {
                messageBundle.putString("firstName", userPerson.getFirstName());
                messageBundle.putString("lastName", userPerson.getLastName());
            }

            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            checkForEmptyValues();
        }
    };

    void checkForEmptyValues() {

        String host = serverHost.getText().toString().trim();
        String port = serverPort.getText().toString().trim();
        String user = userName.getText().toString().trim();
        String pass = password.getText().toString().trim();

        signInButton.setEnabled(!host.isEmpty() && !port.isEmpty() && !user.isEmpty() && !pass.isEmpty());

        String firstN = firstName.getText().toString().trim();
        String lastN = lastName.getText().toString().trim();
        String mail = email.getText().toString().trim();

        registerButton.setEnabled(!host.isEmpty() && !port.isEmpty() && !user.isEmpty() && !pass.isEmpty()
                && !firstN.isEmpty() && !lastN.isEmpty() && !mail.isEmpty());

    }

}