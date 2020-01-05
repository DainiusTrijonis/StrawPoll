package com.example.strawpoll;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class PollAdapter extends FirestoreRecyclerAdapter<Poll, PollAdapter.PollHolder> {

    private OnItemClickListener listener;


    public PollAdapter(@NonNull FirestoreRecyclerOptions<Poll> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PollHolder pollHolder, int i, @NonNull Poll poll) {
        pollHolder.textViewTitle.setText(poll.getTitle());
        pollHolder.textViewEmail.setText(poll.getUser());
        if(poll.getExpired())
        {
            pollHolder.textViewExpired.setText("Closed");
        }
        else
        {
            pollHolder.textViewExpired.setText("Open");
        }

    }

    @NonNull
    @Override
    public PollHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_item,parent,false);
        return new PollHolder(v);
    }

    class PollHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewExpired;
        TextView textViewEmail;
        public PollHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewExpired = itemView.findViewById(R.id.text_view_expired);
            textViewEmail = itemView.findViewById(R.id.text_view_email);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener !=null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

