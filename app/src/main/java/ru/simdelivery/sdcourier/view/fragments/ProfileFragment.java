package ru.simdelivery.sdcourier.view.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.model.Order;
import ru.simdelivery.sdcourier.network.ApiClient;
import ru.simdelivery.sdcourier.network.GetUserToken;
import ru.simdelivery.sdcourier.network.Logout;

public class ProfileFragment extends Fragment {

    TextView userNameText;
    TextView emailText;
    ImageButton settingsBtn;
    Button logoutBtn;
    Button ordersHistory;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile_view, container, false);

        userNameText = v.findViewById(R.id.user_name_view);
        emailText = v.findViewById(R.id.user_email_view);
        settingsBtn = v.findViewById(R.id.user_settings_button);
        logoutBtn = v.findViewById(R.id.user_logout_button);
        ordersHistory = v.findViewById(R.id.user_order_history_button);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.auth_token), "");

        userNameText.setText(sharedPref.getString(getString(R.string.user_name), ""));

        emailText.setText(sharedPref.getString(getString(R.string.user_email), ""));

        settingsBtn.setOnClickListener(v1 -> openSettingsFragment());

        ordersHistory.setOnClickListener(v12 -> openHistoryFragment());

        logoutBtn.setOnClickListener(v13 -> logout(token));

        return v;

    }
    private void openSettingsFragment() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new SettingsFragment()).addToBackStack(null).commit();
    }

    private void openHistoryFragment() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new OrdersHistoryFragment()).addToBackStack(null).commit();
    }

    public void logout(String token) {

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        editor = sharedPref.edit();
        editor.putString(getString(R.string.auth_token), "");
        editor.commit();

        Logout service = ApiClient.getRetrofitInstance(token).create(Logout.class);
        Call<ResponseBody> call = service.sendLogout();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
                    AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 500, // one second
                            PendingIntent.getActivity(getActivity(), 0, getActivity().getIntent(), PendingIntent.FLAG_ONE_SHOT
                                    | PendingIntent.FLAG_CANCEL_CURRENT));
                    Intent i = getActivity()
                            .getBaseContext()
                            .getPackageManager()
                            .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                    else {
                        Toast.makeText(getContext(), "При попытке выхода произошла ошибка. Повторите попытку позже", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ошибка при запросе", "onFailure");
            }
        });





    }



}
