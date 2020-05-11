package ru.simdelivery.sdcourier.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.iid.FirebaseInstanceId;

import ru.simdelivery.sdcourier.R;

public class SettingsFragment extends Fragment {

    Button notificationsSwitch;
    Button instructionsBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        notificationsSwitch = v.findViewById(R.id.settings_notifications_button);
        notificationsSwitch.setOnClickListener(v1 -> openSettings());

        return v;
    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra("android.provider.extra.APP_PACKAGE", getActivity().getPackageName());
        startActivity(intent);

    }
}
