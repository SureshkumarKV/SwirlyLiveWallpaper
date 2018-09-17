package in.sureshkumarkv.swirlylivewallpaper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by SureshkumarKV on 17/09/2018.
 */

public class SwirlyPreferencesActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, android.R.id.copy, 0, "Reset").setIcon(android.R.drawable.ic_popup_sync).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

            case android.R.id.copy:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            dialog.dismiss();

                            SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            mSharedPreferences.edit().putBoolean("reset", ! mSharedPreferences.getBoolean("reset", false)).commit();

                            finish();
                            startActivity(new Intent(SwirlyPreferencesActivity.this, SwirlyPreferencesActivity.class));
                            overridePendingTransition(0, 0);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to reset to default settings?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
