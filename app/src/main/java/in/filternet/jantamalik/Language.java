package in.filternet.jantamalik;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Language extends AppCompatActivity {

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();

        setContentView(R.layout.language);
    }

    public void onclick_select_language(View view) {
        String language = null;
        switch(view.getId()) {
            case R.id.urdu:
                language = "Urdu";
                break;
            case R.id.punjabi:
                language = "Punjabi";
                break;
            case R.id.hindi:
                language = "Hindi";
                break;
            case R.id.english:
                language = "English";
                break;
            case R.id.bangla:
                language = "Bangla";
                break;
            case R.id.gujrati:
                language = "Gujrati";
                break;
            case R.id.odia:
                language = "Odia";
                break;
            case R.id.marathi:
                language = "Marathi";
                break;
            case R.id.kannada:
                language = "Kannada";
                break;
            case R.id.malyalam:
                language = "Malyalam";
                break;
            case R.id.tamil:
                language = "Tamil";
                break;
            case R.id.telgu:
                language = "Telgu";
                break;
            default:
                language = null;
                break;
        }

        mEditor.putString(MainActivity.sUSER_SELECT_LANGUAGE, language).commit();
        FirebaseLogger.send(this, language);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {//Android device back
        moveTaskToBack(true);
    }
}