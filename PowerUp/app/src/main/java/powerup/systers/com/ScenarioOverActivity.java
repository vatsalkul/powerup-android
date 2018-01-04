/**
 * @desc finishes dialogue situation when the “continue” or “back” button is pressed.
 * Brings user to ending screen displaying current progress of power/health
 * bars and karma points.
 */

package powerup.systers.com;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;

import powerup.systers.com.datamodel.Scenario;
import powerup.systers.com.datamodel.SessionHistory;
import powerup.systers.com.db.DatabaseHandler;
import powerup.systers.com.powerup.PowerUpUtils;

import static powerup.systers.com.R.string.scene;

public class ScenarioOverActivity extends AppCompatActivity {

    public Activity scenarioOverActivityInstance;
    public static int scenarioActivityDone;
    private DatabaseHandler mDbHandler;
    public Scenario scene;

    public ScenarioOverActivity() {
        scenarioOverActivityInstance = this;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmDbHandler(new DatabaseHandler(this));
        getmDbHandler().open();
        setContentView(R.layout.activity_scenario_over);
        scene = getmDbHandler().getScenario();
        scenarioActivityDone = 1;
        ImageView replayButton = (ImageView) findViewById(R.id.replayButton);
        ImageView continueButton = (ImageView) findViewById(R.id.continueButton);
        Button mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ScenarioOverActivity.this, MapActivity.class));
            }
        });

        TextView karmaPoints = (TextView) findViewById(R.id.karmaPoints);
        
        karmaPoints.setText(String.valueOf(SessionHistory.totalPoints));
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GameActivity().gameActivityInstance.finish();
                startActivity(new Intent(ScenarioOverActivity.this, GameActivity.class));
            }
        });
        if (getIntent().getExtras()!=null && PowerUpUtils.MAP.equals(getIntent().getExtras().getString(PowerUpUtils.SOURCE))){
            continueButton.setVisibility(View.GONE);
            continueButton.setOnClickListener(null);
        }


        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionHistory.currSessionID = SessionHistory.prevSessionID;
                SessionHistory.totalPoints -= SessionHistory.currScenePoints;
                SessionHistory.currScenePoints = 0;
                scenarioActivityDone = 0;
                DatabaseHandler dbHandler = new DatabaseHandler(ScenarioOverActivity.this);
                dbHandler.resetCompleted(SessionHistory.currSessionID);
                dbHandler.resetReplayed(SessionHistory.currSessionID);
                new GameActivity().gameActivityInstance.finish();
                scenarioOverActivityInstance.finish();
                startActivity(new Intent(ScenarioOverActivity.this, GameActivity.class));
            }
        });
        String alertTitle = getResources().getString(R.string.Alert_title);
        String alertMessageStart = getResources().getString(R.string.Alert_message_start);
        String alertMessageEnd = getResources().getString(R.string.Alert_messgae_end);
        String alertOkMessage = getResources().getString(R.string.Alert_ok_message);
        AlertDialog.Builder dialogBox = new AlertDialog.Builder(this);
        dialogBox.setTitle(alertTitle);
        dialogBox.setMessage(alertMessageStart + " " +  SessionHistory.currScenePoints + " " +  alertMessageEnd);
        dialogBox.setCancelable(true);

        AlertDialog.Builder ok = dialogBox.setPositiveButton(alertOkMessage,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = dialogBox.create();
        alertDialog.show();
    }

    /**
     * If the "back" button is pressed, the current situation closes itself.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new GameActivity().gameActivityInstance.finish();
    }

    public DatabaseHandler getmDbHandler() {
        return mDbHandler;
    }

    public void setmDbHandler(DatabaseHandler mDbHandler) {
        this.mDbHandler = mDbHandler;
    }
}
