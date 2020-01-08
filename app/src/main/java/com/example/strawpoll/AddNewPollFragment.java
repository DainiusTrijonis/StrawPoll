package com.example.strawpoll;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewPollFragment extends Fragment {

    private EditText editTextTitle;

    private ListView listViewOptions;
    private AnswerOptionsAdapter adapter;
    private ArrayList<AnswerOption> arrayOfAnswerOptions;
    private Button addNewOption;
    private Integer x=3;
    private CollectionReference pollRef;
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

        arrayOfAnswerOptions = new ArrayList<AnswerOption>();

        arrayOfAnswerOptions.add(new AnswerOption("Option1", new ArrayList<String>()));
        arrayOfAnswerOptions.add(new AnswerOption("Option2", new ArrayList<String>()));

        adapter = new AnswerOptionsAdapter(getActivity(), arrayOfAnswerOptions);

        listViewOptions.setAdapter(adapter);



        addNewOption = Objects.requireNonNull(getActivity()).findViewById(R.id.add_new_option);
        addNewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewOption();
            }
        });
    }

    private void NewOption() {
        arrayOfAnswerOptions.add(new AnswerOption("Option" + x.toString(), new ArrayList<String>()));
        x++;
        adapter = new AnswerOptionsAdapter(getActivity(), arrayOfAnswerOptions);
        listViewOptions.setAdapter(adapter);
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
        pollRef = FirebaseFirestore.getInstance()
                .collection("polls");
        pollRef.add(new Poll(false,title,currentUser.getEmail())).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                CollectionReference answerOptionRef = pollRef.document(documentReference.getId()).collection("answerOption");
                for (AnswerOption answerOption:arrayOfAnswerOptions) {
                    answerOptionRef.add(answerOption);
                }
            }
        });



    }

    public class AnswerOptionsAdapter extends ArrayAdapter<AnswerOption> {

        public AnswerOptionsAdapter(Context context, ArrayList<AnswerOption> answerOption) {

            super(context, 0, answerOption);

        }
        @Override

        public View getView(int position, View convertView, ViewGroup parent) {

            AnswerOption answerOption = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.answer_option_add_item, parent, false);
            }

            EditText optionTitle =  convertView.findViewById(R.id.edit_text_option);

            optionTitle.setText(answerOption.getAnswer());

            return convertView;
        }
    }
}
