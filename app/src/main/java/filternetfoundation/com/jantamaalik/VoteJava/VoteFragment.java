package filternetfoundation.com.jantamaalik.VoteJava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import filternetfoundation.com.jantamaalik.R;

public class VoteFragment extends Fragment {
   private View view;
   private RecyclerView recyclerView;
  private RecyclerView.Adapter adapter;
  private RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.vote_fragment,container,false);
        recyclerView = view.findViewById(R.id.Vote_recyclerview);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        String[] voteData = {"Member of Parliament", "MLA","Parshad"};
        adapter = new MyAdapter(voteData);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
