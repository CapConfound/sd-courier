package ru.simdelivery.sdcourier.view.fragments.profile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

import ru.simdelivery.sdcourier.LauncherActivity;
import ru.simdelivery.sdcourier.R;
import ru.simdelivery.sdcourier.view.fragments.login.LoginFragment;
import ru.simdelivery.sdcourier.view.fragments.orders_history.OrdersHistoryFragment;
import ru.simdelivery.sdcourier.view.fragments.settings.SettingsFragment;

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

        SettingsFragment settingsFragment = new SettingsFragment();
        OrdersHistoryFragment ordersHistoryFragment = new OrdersHistoryFragment();
        View v = inflater.inflate(R.layout.fragment_profile_view, container, false);

        userNameText = v.findViewById(R.id.user_name_view);
        emailText = v.findViewById(R.id.user_email_view);
        settingsBtn = v.findViewById(R.id.user_settings_button);
        logoutBtn = v.findViewById(R.id.user_logout_button);
        ordersHistory = v.findViewById(R.id.user_order_history_button);

        settingsBtn.setOnClickListener(v1 -> openSettingsFragment());

        ordersHistory.setOnClickListener(v12 -> openHistoryFragment());

        logoutBtn.setOnClickListener(v13 -> logout());

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

    private void logout() {
        //TODO удаление токена из памяти при выходе
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        editor = sharedPref.edit();
        editor.putString(getString(R.string.auth_token), "");
        editor.commit();
        AlarmManager am = (AlarmManager)   getActivity().getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 500, // one second
                PendingIntent.getActivity(getActivity(), 0, getActivity().getIntent(), PendingIntent.FLAG_ONE_SHOT
                        | PendingIntent.FLAG_CANCEL_CURRENT));
        Intent i = getActivity().getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

}
