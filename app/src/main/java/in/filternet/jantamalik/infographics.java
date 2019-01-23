package in.filternet.jantamalik;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class infographics extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infographics_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
