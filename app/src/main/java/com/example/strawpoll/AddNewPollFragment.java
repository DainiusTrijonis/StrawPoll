package com.example.strawpoll;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewPollFragment extends Fragment {

    private EditText editTextTitle;
    private ListView listViewOptions;
    private Button addNewOption;
    public AddNewPollFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_new_poll, container, false);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = Objects.requireNonNull(getActivity()).getMenuInflater();
        menuInflater.inflate(R.menu.add_new_poll_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_poll) {
            savePoll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.edit_text_title);
        listViewOptions = Objects.requireNonNull(getActivity()).findViewById(R.id.list_view_options);
        addNewOption = Objects.requireNonNull(getActivity()).findViewById(R.id.add_new_option);

    }

    private void addNewOption() {
        
    }

    private void savePoll() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String ownerId = null;
        if (currentUser != null) {
            ownerId = currentUser.getUid();
        }

        String title = editTextTitle.getText().toString();

        if( title.trim().isEmpty())
        {
            Toast.makeText(getActivity(), "Poll failed to create fill info",Toast.LENGTH_LONG).show();
            return;
        }
        CollectionReference pollRef = FirebaseFirestore.getInstance()
                .collection("polls");
        pollRef.add(new Poll(false,title,currentUser.getEmail()));

    }
}
