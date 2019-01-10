package filternetfoundation.com.jantamaalik.VoteJava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import filternetfoundation.com.jantamaalik.MainActivity;
import filternetfoundation.com.jantamaalik.R;

public class VoteFragment extends Fragment implements ItemClickListener {
   private View view;
   private RecyclerView recyclerView;
   private RecyclerView.Adapter adapter;
   private RecyclerView.LayoutManager layoutManager;
   private List<String> voteData;

    private SharedPreferences mSharedPref;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String current_language = mSharedPref.getString(MainActivity.sUSER_CURRENT_LANGUAGE, null);
        if(current_language != null && current_language.equals(MainActivity.sLANGUAGE_HINDI)) {
            MainActivity.setUI_Lang(getActivity(), "hi");
        }

        view = inflater.inflate(R.layout.vote_fragment,container,false);
        recyclerView = view.findViewById(R.id.Vote_recyclerview);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        voteData = new ArrayList<>();
        voteData.add(getString(R.string.MP));
        voteData.add(getString(R.string.MLA));
        voteData.add(getString(R.string.Parshad));
        adapter = new VoteFragmentRecyclerViewAdapter(voteData);
        ((VoteFragmentRecyclerViewAdapter) adapter).setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View view, int position) {
        if (position==0){
            Intent intent = new Intent(view.getContext(), VoteMP.class);
            startActivity(intent);
        }
    }
}
