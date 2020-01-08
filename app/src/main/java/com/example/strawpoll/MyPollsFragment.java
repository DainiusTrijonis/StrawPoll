package com.example.strawpoll;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPollsFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference myPoolRef = db.collection("polls");
    private PollAdapter adapter;

    private FirebaseUser user;

    public MyPollsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_polls, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = Objects.requireNonNull(getActivity()).getMenuInflater();
        menuInflater.inflate(R.menu.my_polls_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_poll) {
            addPoll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();

        setUpRecyclerView();
    }

    private void addPoll() {
        //navigation to new poll fragment
        if (user == null) {
            Toast.makeText(getActivity(), "You need to be logged in to create polls",Toast.LENGTH_LONG).show();
        } else {
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_myPollsFragment_to_addNewPollFragment);
        }
    }


    private void setUpRecyclerView() {
        String userEmail = user != null ? user.getEmail() : "";
        Query query = myPoolRef.whereEqualTo("user", userEmail).orderBy("title",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Poll> options = new FirestoreRecyclerOptions.Builder<Poll>()
                .setQuery(query,Poll.class)
                .build();
        adapter = new PollAdapter(options);

        RecyclerView recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.recycler_view3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PollAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Poll poll = documentSnapshot.toObject(Poll.class);
                String id = documentSnapshot.getId();
                Bundle bundle = new Bundle();
                bundle.putString("id",id);
                bundle.putSerializable("poll",poll);
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_myPollsFragment_to_pollFragment,bundle);
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
