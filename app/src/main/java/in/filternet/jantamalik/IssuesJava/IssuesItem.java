package in.filternet.jantamalik.IssuesJava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import in.filternet.jantamalik.R;

public class IssuesItem extends AppCompatActivity {
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.issues_fragment_item_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
