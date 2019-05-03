package com.udemyandroid.minitwitter.ui.profile;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.udemyandroid.minitwitter.R;
import com.udemyandroid.minitwitter.common.Constantes;
import com.udemyandroid.minitwitter.data.ProfileViewModel;
import com.udemyandroid.minitwitter.retrofit.request.RequestUserProfile;
import com.udemyandroid.minitwitter.retrofit.response.ResponseUserProfile;

import java.security.Permission;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    ImageView ivAvatar;
    EditText etUsername, etEmail, etPassword, etWebsite, etDescripcion;
    Button btnSave, btnChangePassword;
    boolean loadingData = true;
    PermissionListener allPermissionsListener;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        ivAvatar = view.findViewById(R.id.imageViewAvatar);
        etUsername = view.findViewById(R.id.editTextUsername);
        etEmail = view.findViewById(R.id.editTextEmail);
        etPassword = view.findViewById(R.id.editTextCurrentPassword);
        etWebsite = view.findViewById(R.id.editTextWebsite);
        etDescripcion = view.findViewById(R.id.editTextDescripcion);
        btnSave = view.findViewById(R.id.buttonSave);
        btnChangePassword = view.findViewById(R.id.buttonChangePassword);

        //EVENTOS
        btnSave.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            String website = etWebsite.getText().toString();
            String password = etPassword.getText().toString();

            if (username.isEmpty()) {
                etUsername.setError("El nombre de usuario es requerido");
            } else if (email.isEmpty()) {
                etEmail.setError("El email es requerido");
            } else if (password.isEmpty()) {
                etPassword.setError("La contraseña es requerida");
            } else {
                RequestUserProfile requestUserProfile = new RequestUserProfile(username, email, descripcion, website, password);
                profileViewModel.updateProfile(requestUserProfile);
                Toast.makeText(getActivity(), "Enviando información al servidor", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(false);
            }

            RequestUserProfile requestUserProfile = new RequestUserProfile(username, email, descripcion, website, password);
            profileViewModel.updateProfile(requestUserProfile);
        });

        btnChangePassword.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Click on save", Toast.LENGTH_SHORT).show();
        });

        ivAvatar.setOnClickListener(v -> {
            // invocar a la seleccion d ela fotografia
            checkPermissions();
        });

        // ViewModel
        profileViewModel.userProfile.observe(getActivity(), new Observer<ResponseUserProfile>() {
            @Override
            public void onChanged(@Nullable ResponseUserProfile responseUserProfile) {
                loadingData = false;
                etUsername.setText(responseUserProfile.getUsername());
                etEmail.setText(responseUserProfile.getEmail());
                etWebsite.setText(responseUserProfile.getWebsite());
                etDescripcion.setText(responseUserProfile.getDescripcion());

                if (!loadingData) {
                    btnSave.setEnabled(true);
                    Toast.makeText(getActivity(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                }

                if (!responseUserProfile.getPhotoUrl().isEmpty()) {
                    Glide.with(getActivity())
                            .load(Constantes.API_MINITWITTER_FILES_URL + responseUserProfile.getPhotoUrl())
                            .centerCrop()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(ivAvatar);
                }
            }
        });

        profileViewModel.photoProfile.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String photo) {
                if (!photo.isEmpty()) {
                    Glide.with(getActivity())
                            .load(Constantes.API_MINITWITTER_FILES_URL + photo)
                            .centerCrop()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(ivAvatar);
                }
            }
        });

        return view;
    }

    private void checkPermissions() {
        PermissionListener dialogOnDeniedPermissionListener =
                DialogOnDeniedPermissionListener.Builder.withContext(getActivity())
                        .withTitle("Permisos")
                        .withMessage("Los permisos solicitados son necesarios para poder seleccionar una foto de perfil")
                        .withButtonText("Aceptar")
                        .withIcon(R.mipmap.ic_launcher)
                        .build();

        allPermissionsListener = new CompositePermissionListener(
                (PermissionListener) getActivity(),
                dialogOnDeniedPermissionListener
        );

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(allPermissionsListener)
                .check();
    }

}
