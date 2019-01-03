package filternetfoundation.com.jantamaalik.IssuesJava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import filternetfoundation.com.jantamaalik.R;

public class IssuesFragment extends android.support.v4.app.Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.issues_fragment,container,false);
        return view;
    }
}
