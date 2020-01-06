package com.example.strawpoll;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class PollFragment extends Fragment {

    private Poll poll; // poll object
    private String id; // id of Poll

    private TextView textViewTitle;
    private TextView textViewEmail;
    private TextView textViewExpired;
    private RecyclerView answerOptionRecyclerView;

    // getting for recycleview answeroptions
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference adbookRef = db.collection("answerOption");
    private AnswerOptionAdapter adapter;


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

        // getting from parent fragment bundle
        Bundle bundle = getArguments();
        poll = (Poll) bundle.getSerializable("poll");
        id = bundle.getString("id");

        //initializing textviews and recyclerview
        textViewTitle = getActivity().findViewById(R.id.text_title);
        textViewEmail = getActivity().findViewById(R.id.text_email);
        textViewExpired = getActivity().findViewById(R.id.text_expired);
        answerOptionRecyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.answer_option_recycler_view);

        //Setting text on textViews
        textViewTitle.setText(poll.getTitle());
        textViewEmail.setText(poll.getUser());
        if(poll.getExpired()) {
            textViewExpired.setText("Closed");
        }
        else {
            textViewExpired.setText("Open");
        }

        //setting for recycleView query for specific PoolAnswers
        //adbookRef = db.collection("polls").document(id).collection("answerOption");
        //getData();
        setUpRecyclerView();
    }

    private void getData() {
        db.collection("answerOption" )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("asd --", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("asd eee", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void setUpRecyclerView() {
        Query query = adbookRef;

        FirestoreRecyclerOptions<AnswerOption> options = new FirestoreRecyclerOptions.Builder<AnswerOption>()
                .setQuery(query,AnswerOption.class)
                .build();
        adapter = new AnswerOptionAdapter(options);
        adapter.setOnItemClickListener(new AnswerOptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

            }
        });

        answerOptionRecyclerView.setHasFixedSize(true);
        answerOptionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        answerOptionRecyclerView.setAdapter(adapter);


    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
