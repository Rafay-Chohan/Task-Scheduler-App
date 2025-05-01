package com.example.taskscheduler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ProfileFragment extends Fragment {
    private TextView tvProfileName, tvProfileEmail, tvProfilePhone, tvProfileGender;
    private FloatingActionButton fabEdit;
    private SharedPreferences sharedPreferences;

    // SharedPreferences keys
    private static final String PREFS_NAME = "UserProfile";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_THEME = "dark_mode";
    private Switch switchTheme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvProfilePhone = view.findViewById(R.id.tvProfilePhone);
        tvProfileGender = view.findViewById(R.id.tvProfileGender);
        fabEdit = view.findViewById(R.id.fabEdit);
        switchTheme = view.findViewById(R.id.switchTheme);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Load saved profile data
        loadProfileData();

        // Set up FAB click listener
        fabEdit.setOnClickListener(v -> showEditProfileDialog());
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_THEME, isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });
    }

    private void loadProfileData() {
        // Load saved values or use default placeholders
        String name = sharedPreferences.getString(KEY_NAME, "Your Name");
        String email = sharedPreferences.getString(KEY_EMAIL, "your.email@example.com");
        String phone = sharedPreferences.getString(KEY_PHONE, "+1234567890");
        String gender = sharedPreferences.getString(KEY_GENDER, "Not specified");

        // Update TextViews
        tvProfileName.setText(name);
        tvProfileEmail.setText(email);
        tvProfilePhone.setText(phone);
        tvProfileGender.setText(gender);

        boolean isDarkMode = sharedPreferences.getBoolean(KEY_THEME, false);
        switchTheme.setChecked(isDarkMode);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Profile");

        // Inflate custom dialog layout
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null);

        // Initialize dialog views
        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etEmail = dialogView.findViewById(R.id.etEmail);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);
        EditText etGender = dialogView.findViewById(R.id.etGender);

        // Pre-fill with current values
        etName.setText(sharedPreferences.getString(KEY_NAME, ""));
        etEmail.setText(sharedPreferences.getString(KEY_EMAIL, ""));
        etPhone.setText(sharedPreferences.getString(KEY_PHONE, ""));
        etGender.setText(sharedPreferences.getString(KEY_GENDER, ""));

        builder.setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    // Get edited values
                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String phone = etPhone.getText().toString();
                    String gender = etGender.getText().toString();

                    // Save to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_NAME, name);
                    editor.putString(KEY_EMAIL, email);
                    editor.putString(KEY_PHONE, phone);
                    editor.putString(KEY_GENDER, gender);
                    editor.apply();

                    // Update UI
                    loadProfileData();
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}