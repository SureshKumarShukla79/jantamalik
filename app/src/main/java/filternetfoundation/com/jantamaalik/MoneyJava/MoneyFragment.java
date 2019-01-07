package filternetfoundation.com.jantamaalik.MoneyJava;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import filternetfoundation.com.jantamaalik.R;

public class MoneyFragment extends Fragment {
    private GridView gridView;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.money_fragment,container,false);
        gridView = view.findViewById(R.id.money_tab_gridview);
        gridView.setAdapter(new ImageAdapter(view.getContext()));
        return view;
    }
}
