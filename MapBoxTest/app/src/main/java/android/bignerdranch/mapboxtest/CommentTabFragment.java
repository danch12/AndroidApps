package android.bignerdranch.mapboxtest;

import android.bignerdranch.mapboxtest.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class CommentTabFragment extends Fragment {

    public CommentTabFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.comment_tab_fragment, container, false);
    }
}
