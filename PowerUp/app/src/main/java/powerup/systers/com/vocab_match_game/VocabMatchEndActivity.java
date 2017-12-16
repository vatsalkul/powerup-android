package powerup.systers.com.vocab_match_game;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import powerup.systers.com.MapActivity;
import powerup.systers.com.R;
import powerup.systers.com.ScenarioOverActivity;
import powerup.systers.com.powerup.PowerUpUtils;

public class VocabMatchEndActivity extends AppCompatActivity {

    public TextView scoreView, correctView, wrongView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_match_end);
        Intent intent = getIntent();
        int score = intent.getExtras().getInt(PowerUpUtils.SCORE);
        int correctAnswers= score;
        int wrongAnswers= PowerUpUtils.VOCAB_TILES_IMAGES.length - score;
        scoreView = (TextView) findViewById(R.id.vocab_score);
        correctView = (TextView) findViewById(R.id.correct);
        wrongView = (TextView) findViewById(R.id.wrong);
        scoreView.setText(""+score);
        correctView.setText(""+correctAnswers);
        wrongView.setText(""+wrongAnswers);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void continuePressed(View view){
        Intent intent = new Intent(VocabMatchEndActivity.this, ScenarioOverActivity.class);
        finish();
        startActivity(intent);
    }
}
