package ru.simdelivery.sdcourier;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import ru.simdelivery.sdcourier.view.fragments.LoginFragment;
import ru.simdelivery.sdcourier.view.fragments.MyOrdersFragment;
import ru.simdelivery.sdcourier.view.fragments.OrdersFragment;
import ru.simdelivery.sdcourier.view.fragments.ProfileFragment;

public class LauncherActivity extends AppCompatActivity {
    public static final String FIREBASE_TAG = "1";
    private static final String TAG = FIREBASE_TAG;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String gcmToken = task.getResult().getToken();
                        sharedPref = getPreferences(Context.MODE_PRIVATE);

                        editor = sharedPref.edit();
                        //insert token into SharedPreferences
                        editor.putString(getString(R.string.gcm_token), gcmToken);
                        editor.commit();

                        Log.i("GCM_TOKEN", gcmToken);

                    }
                });

        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
//        bottomNavigationView.setVisibility(View.GONE);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.auth_token), "");
        Log.i("auth_TOKEN", token);

        if(!token.equals("")) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new OrdersFragment())
                    .commit();
        }
        else {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .commit();
        }
    }

    public void showIncorrectLogin(){
        TextView warning = findViewById(R.id.login_incorrect_view);
        warning.setVisibility(View.VISIBLE);
    }

    public void setNavGone() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void setNavVisible() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {

        FragmentTransaction fragmentTransaction;
        FragmentManager fragmentManager = getSupportFragmentManager();
        OrdersFragment ordersFragment = new OrdersFragment();
        MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        switch (item.getItemId()){
            case R.id.nav_new_orders:

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, ordersFragment);
                fragmentTransaction.commit();
                return true;
            case R.id.nav_my_orders:


                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, myOrdersFragment);
                fragmentTransaction.commit();
                return true;
            case R.id.nav_profile:

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, profileFragment);
                fragmentTransaction.commit();
                return true;
        }
        return false;
    };

}
