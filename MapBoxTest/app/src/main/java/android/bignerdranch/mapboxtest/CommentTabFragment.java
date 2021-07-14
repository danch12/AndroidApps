package android.bignerdranch.mapboxtest;

import android.bignerdranch.mapboxtest.R;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Comment;
import com.amplifyframework.datastore.generated.model.Walk;

import java.util.ArrayList;
import java.util.List;

public class CommentTabFragment extends Fragment {
    private Integer numComments;
    private List<Comment> comments;
    private RecyclerView mCommentRecyclerView;
    private CommentTabFragment.CommentAdapter mCommentAdapter;
    private String walkId;
    private Button commentButton;


    public CommentTabFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        mCommentRecyclerView = (RecyclerView) view.findViewById(R.id.comment_recycler_view);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        walkId = getArguments().getString(ExistingPlaylistTabActivity.EXTRA_ID);
        comments= new ArrayList<>();
        commentButton = view.findViewById(R.id.comment_button);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = view.findViewById(R.id.comment_text);
                if(text.getText().toString().length()>0){
                    Comment comment = Comment.builder().text(text.getText().toString()).walkId(walkId).build();
                    Amplify.DataStore.save(comment,
                            success -> updateUI(),
                            error -> Log.e("Comment", "Could not save item to DataStore", error)
                    );
                    text.setText("");

                }
            }
        });


        updateUI();
        return view;
    }


    private void updateUI(){
        getComments();
        mCommentAdapter = new CommentTabFragment.CommentAdapter(comments);
        mCommentRecyclerView.setAdapter(mCommentAdapter);

    }


    private void getComments(){
        Amplify.DataStore.query(Comment.class, Where.matches(Comment.WALK_ID.eq(walkId)), response->{
                    while(response.hasNext()){
                        Comment comment =response.next();
                        if(!comments.contains(comment)){
                            comments.add(comment);
                        }
                    }
                    refreshSongs(comments.size());
                }, error -> Log.e("MyAmplifyApp", "Query failure", error)

        );
    }

    public void refreshSongs(Integer numComments){
        if(this.numComments != null){
            mCommentAdapter.notifyItemRangeRemoved(0, numComments);
        }
        this.numComments= numComments;

        mCommentAdapter.notifyItemRangeInserted(0,numComments);
    }

    private class CommentHolder extends RecyclerView.ViewHolder {
        private final TextView mComment;


        public CommentHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.comment_tab_fragment,parent,false));
            //item view is created by parent class when you implement onclicklistener
            // itemView.setOnClickListener(this);
            mComment = (TextView) itemView.findViewById(R.id.comment);

        }

        public void bind(Comment comment){
            mComment.setText(comment.getText());
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentTabFragment.CommentHolder>{
        private List<Comment> mComments;
        public CommentAdapter(List<Comment> comments){
            mComments = comments;
        }

        @NonNull
        @Override
        public CommentTabFragment.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CommentTabFragment.CommentHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentTabFragment.CommentHolder holder, int position) {
            Comment comment = mComments.get(position);
            if(position%2==0){
                holder.itemView.setBackgroundColor(Color.rgb(112,128,144));
            }else{
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
            holder.bind(comment);
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }
    }
}
