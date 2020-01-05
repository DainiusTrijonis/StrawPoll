package com.example.strawpoll;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PollFragment extends Fragment {

    private Poll poll;
    private String id;

    private TextView textViewTitle;
    private TextView textViewEmail;
    private TextView textViewExpired;

    public PollFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poll, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bundle = getArguments();
        poll = (Poll) bundle.getSerializable("poll");
        id = bundle.getString("id");
        textViewTitle = getActivity().findViewById(R.id.text_title);
        textViewEmail = getActivity().findViewById(R.id.text_email);
        textViewExpired = getActivity().findViewById(R.id.text_expired);


        textViewTitle.setText(poll.getTitle());
        textViewEmail.setText(poll.getUser());
        textViewExpired.setText(poll.getExpired().toString());
    }
}
