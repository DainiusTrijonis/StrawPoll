package com.example.strawpoll;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;



/**
 * A simple {@link Fragment} subclass.
 */
public class PollFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference answerOptionsRef;
    private AnswerOptionAdapter adapter;

    private FirebaseUser user;

    private Poll poll; // poll object
    private String id; // id of Poll

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

        //for votes storing uid
        user = FirebaseAuth.getInstance().getCurrentUser();

        //initializing textviews and recyclerview
        textViewTitle = getActivity().findViewById(R.id.text_title);
        textViewEmail = getActivity().findViewById(R.id.text_email);
        textViewExpired = getActivity().findViewById(R.id.text_expired);

        //Setting text on textViews
        textViewTitle.setText(poll.getTitle());
        textViewEmail.setText(poll.getUser());
        if(poll.getExpired()) {
            textViewExpired.setText("Closed");
        }
        else {
            textViewExpired.setText("Open");
        }

        answerOptionsRef = FirebaseFirestore.getInstance().collection("polls").document(id).collection("answerOption");
        setUpRecyclerView();
    }


    private void setUpRecyclerView() {
        Query query = answerOptionsRef.orderBy("answer",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<AnswerOption> options = new FirestoreRecyclerOptions.Builder<AnswerOption>()
                .setQuery(query,AnswerOption.class)
                .build();
        adapter = new AnswerOptionAdapter(options);

        RecyclerView recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AnswerOptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {
                Query query = answerOptionsRef.whereArrayContains("votes", user.getUid());
                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()==0 && !poll.getExpired()) {
                            answerOptionsRef.document(documentSnapshot.getId()).update("votes",FieldValue.arrayUnion(user.getUid()));
                        }
                        else {
                            Toast.makeText(getActivity(), "You have already voted or Poll have expired ",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
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
