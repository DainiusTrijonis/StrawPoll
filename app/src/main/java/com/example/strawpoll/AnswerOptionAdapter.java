package com.example.strawpoll;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;



public class AnswerOptionAdapter extends FirestoreRecyclerAdapter<AnswerOption, AnswerOptionAdapter.AnswerOptionHolder> {

    private OnItemClickListener listener;
    private FirebaseAuth mAuth;

    public AnswerOptionAdapter(@NonNull FirestoreRecyclerOptions<AnswerOption> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AnswerOptionHolder answerOptionHolder, int i, @NonNull AnswerOption answerOption) {
        answerOptionHolder.checkBoxAnswer.setText(answerOption.getAnswer());
        FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
        String userEmail = currentUser != null ? currentUser.getEmail() : "";
        if(answerOption.getVotes().contains(userEmail)) {
            answerOptionHolder.checkBoxAnswer.setChecked(true);
        }
        else {
            answerOptionHolder.checkBoxAnswer.setChecked(false);
        }


        answerOptionHolder.textViewCount.setText(String.valueOf(answerOption.getVotes().size()));
    }

    @NonNull
    @Override
    public AnswerOptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_option_item,parent,false);
        return new AnswerOptionHolder(v);
    }

    class AnswerOptionHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxAnswer;
        TextView textViewCount;

        public AnswerOptionHolder(@NonNull View itemView) {
            super(itemView);

            checkBoxAnswer = itemView.findViewById(R.id.check_box_answer);
            textViewCount = itemView.findViewById(R.id.text_view_count);
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
