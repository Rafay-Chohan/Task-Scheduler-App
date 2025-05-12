package com.example.taskscheduler;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity  {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter adapter;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserProfile";
    private static final String KEY_THEME = "dark_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        //scheduleHourlyNotifications();
        boolean isDarkMode = sharedPreferences.getBoolean(KEY_THEME, false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        new TabLayoutMediator(
                tabLayout,
                viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position)
                        {
                            case 0:
//                                View v = LayoutInflater.from(MainActivity.this)
//                                        .inflate(R.layout.tab_design, null, false);
//                                tab.setCustomView(v);
                                tab.setText("Pending Tasks");
                                //BadgeDrawable badge = tab.getOrCreateBadge();
                                //badge.setNumber(10);
                                //badge.setMaxCharacterCount(2);
                                tab.setIcon(R.drawable.btn_pending_tasks);
                                break;
                            case 1:
                                tab.setText("Past Tasks");
                                //BadgeDrawable badge1 = tab.getOrCreateBadge();
                                //badge1.setNumber(100);
                                //badge1.setMaxCharacterCount(3);
                                tab.setIcon(R.drawable.btn_past_tasks);
                                break;
                            case 2:
                                tab.setText("Notifications");
                                //BadgeDrawable badge2 = tab.getOrCreateBadge();
                                //badge2.setNumber(55);
                                tab.setIcon(R.drawable.btn_notifications);
                                break;
                            case 3:
                                tab.setText("Profile");
                                tab.setIcon(R.drawable.btn_profile);
                                break;
                        }
                    }
                }
        ).attach();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BadgeDrawable badge = tabLayout.getTabAt(position).getOrCreateBadge();
                badge.setVisible(false);
                badge.setNumber(0);
            }
        });
//        TaskDB db = new TaskDB(this);
//        db.open();
//        if (db.addTask("Test1", "Testing tasks", LocalDateTime.now()) > 0) {
//            Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();
//        }
//        if(db.addTask("Test2","Testing taskss", LocalDateTime.now())>0)
//        {
//            Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();
//        }
//        db.close();
    }
    public void init()
    {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewpager2);
        adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}