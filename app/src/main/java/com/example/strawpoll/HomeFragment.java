package com.example.strawpoll;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference poolRef = db.collection("polls");
    private PollAdapter adapter;

    private TextView searchTitle;
    private Button searchButton;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //search button initialize
        searchTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.edit_text_search_title);
        searchButton = Objects.requireNonNull(getActivity()).findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchTitle.getText().toString();
                adapter.stopListening();
                setUpRecyclerView(searchText);
                adapter.startListening();

            }
        });
        setUpRecyclerView();

    }
    private void setUpRecyclerView() {
        Query query = poolRef.orderBy("title",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Poll> options = new FirestoreRecyclerOptions.Builder<Poll>()
                .setQuery(query,Poll.class)
                .build();
        adapter = new PollAdapter(options);

        RecyclerView recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.recycler_view);
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
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_homeFragment_to_pollFragment,bundle);
            }
        });
    }
    private void setUpRecyclerView(String title) {
        Query query = poolRef.orderBy("title").startAt(title).endAt(title + "\uf8ff");

        FirestoreRecyclerOptions<Poll> options = new FirestoreRecyclerOptions.Builder<Poll>()
                .setQuery(query,Poll.class)
                .build();
        adapter = new PollAdapter(options);

        RecyclerView recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.recycler_view);
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
                Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_homeFragment_to_pollFragment,bundle);
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
