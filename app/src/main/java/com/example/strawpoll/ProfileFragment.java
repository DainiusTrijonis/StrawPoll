package com.example.strawpoll;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FirebaseUser user;
    private TextView nameTextView;
    private TextView emailTextView;
    private ImageView profileImageView;
    private Button signOutButton;
    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        nameTextView = getActivity().findViewById(R.id.textViewName);
        emailTextView = getActivity().findViewById(R.id.textViewEmail);
        profileImageView = getActivity().findViewById(R.id.profileImageView);

        if(user != null) {
            signOutButton = view.findViewById(R.id.signOutButton);
            signOutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AuthUI.getInstance()
                            .signOut(Objects.requireNonNull(getContext()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_profileFragment_to_authenticationFragment);
                                }
                            });
                }
            });
            emailTextView.setText(user.getEmail());
            nameTextView.setText(user.getDisplayName());
            Picasso.get().load(user.getPhotoUrl()).into(profileImageView);
        }
        else {
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_authenticationFragment);
        }
    }
}