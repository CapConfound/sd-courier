package ru.simdelivery.sdcourier.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.simdelivery.sdcourier.LauncherActivity;
import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.model.Auth;
import ru.simdelivery.sdcourier.model.AuthResponse;
import ru.simdelivery.sdcourier.network.ApiClient;
import ru.simdelivery.sdcourier.network.GetUserToken;

public class LoginFragment extends Fragment {

    private EditText loginEdit;
    private EditText passwordEdit;
    private Button loginBtn;
    private LauncherActivity la;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_view, container, false);
        la = (LauncherActivity) getActivity();
        assert la != null;
        la.setNavGone();
        loginEdit = v.findViewById(R.id.login_username_view);
        passwordEdit = v.findViewById(R.id.login_password_view);
        loginBtn = v.findViewById(R.id.login_button);
        loginBtn.setOnClickListener(v1 -> authentication());
        return v;
    }

    private void authentication() {
        String login_string = loginEdit.getText().toString();
        String password_string = passwordEdit.getText().toString();
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String gcmToken = sharedPref.getString(getString(R.string.gcm_token), "");
        Auth data = new Auth(login_string, password_string, gcmToken);
        GetUserToken service = ApiClient.getAuthData().create(GetUserToken.class);
        Call<AuthResponse> call = service.getAuthResponse(data);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if(response.code() == 401){
                    Toast.makeText(getContext(), "Введённые данные неверны", Toast.LENGTH_SHORT).show();
                }
                else if (response.code() == 200) {Log.d("код не 401", "точно");
                    AuthResponse userData = response.body();
                    Log.d("response.body()", response.toString());
                    sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    editor = sharedPref.edit();
                    String token = response.body().getToken();
                    //insert token into SharedPreferences
                    editor.putString(getString(R.string.auth_token), token);
                    editor.commit();
                    openApp();

                } else {
                    Toast.makeText(getContext(), "При загрузке данных произошла ошибка", Toast.LENGTH_SHORT).show();
                    la.showIncorrectLogin();
                }

                editor = sharedPref.edit();
                editor.putString(getString(R.string.user_email), response.body().getEmail());
                editor.putString(getString(R.string.user_name), response.body().getUsername());
                editor.commit();
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.w("message", t.getMessage());
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openApp() {
        LauncherActivity la = (LauncherActivity) getActivity();
        assert la != null;
        la.setNavVisible();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new OrdersFragment()).addToBackStack(null).commit();
    }

}
