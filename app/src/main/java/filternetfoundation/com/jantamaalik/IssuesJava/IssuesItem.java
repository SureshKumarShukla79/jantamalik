package filternetfoundation.com.jantamaalik.IssuesJava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import filternetfoundation.com.jantamaalik.R;

public class IssuesItem extends AppCompatActivity {
   private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.issues_fragment_item_layout);
        toolbar = findViewById(R.id.toolbar_issuesFragmentItem);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
