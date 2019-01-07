package filternetfoundation.com.jantamaalik.IssuesJava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import filternetfoundation.com.jantamaalik.R;

public class IssuesFragment extends android.support.v4.app.Fragment {
    View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.issues_fragment,container,false);
        recyclerView = view.findViewById(R.id.Issues_recyclerview);

        //use a linear layout manager
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
         recyclerView.setHasFixedSize(true);
        //setting the adapter
        String arr[] = {"one","two","three is also know as","four","five","six","seven","eight","nine","ten","eleven","twelve"};

        adapter = new MyAdapter(arr);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
