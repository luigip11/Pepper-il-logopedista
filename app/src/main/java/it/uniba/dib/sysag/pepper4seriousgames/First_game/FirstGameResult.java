package it.uniba.dib.sysag.pepper4seriousgames.First_game;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Say;

import it.uniba.dib.sysag.pepper4seriousgames.R;
import it.uniba.dib.sysag.pepper4seriousgames.SelectActivity;

public class FirstGameResult extends RobotActivity implements RobotLifecycleCallbacks {

    TextView mGrado, mRisultato;
    MediaPlayer end;
    Button mRiprova;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QiSDK.register(this, this);

        // Modifica speech bar
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);

        setContentView(R.layout.first_game_result);

        mGrado = (TextView) findViewById(R.id.grado);
        mRisultato = (TextView) findViewById(R.id.risultato);

        mRiprova = (Button) findViewById(R.id.riprova);

        Bundle bundle = getIntent().getExtras();
        int score = bundle.getInt("finalscore");

        mRisultato.setText("" + score + ""); /* + Database.questions.length */

        /* if (score == 12){
         mGrado.setText("Perfetto!");
         } else if (score == 11) {
         mGrado.setText("Complimenti!");
         } else if (score == 10) {
         mGrado.setText("Molto bene!");
         }else if (score == 9) {
         mGrado.setText("Bene!");
         } else if (score == 8) {
         mGrado.setText("Non male!");
         } else if (score == 7) {
         mGrado.setText("Puoi far meglio!");
         } else if (score == 6) {
         mGrado.setText("Riprova!");
         } else if (score == 5) {
         mGrado.setText("Riprova!");
         } else if (score == 4) {
         mGrado.setText("Riprova!");
         } else if (score == 3) {
         mGrado.setText("Riprova!");
         } else if (score == 2) {
         mGrado.setText("Riprova!");
         } else if (score == 1) {
         mGrado.setText("Riprova!");
         } else if (score == 0) {
         mGrado.setText("Riprova!");
         } */

        // Avvia suono vittoria
        end = MediaPlayer.create(this, R.raw.applausi_short);
        end.setVolume(1.5f, 1.5f);
        end.start();

        // Tasto riprova
        mRiprova = (Button) findViewById(R.id.riprova);
        mRiprova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstGameResult.this, FirstGame.class));
                FirstGameResult.this.finish();
            }
        });
    }

    // Torna alla scelta del gioco = SelectActivity
    public void CameToSelectActivity (View view) {
        Intent CameToSelectIntent = new Intent(getApplicationContext(), SelectActivity.class);
        startActivity(CameToSelectIntent);
    }

    // Pulisci memoria player
    protected void onStop() {
        super.onStop();
        if (end != null) {
            end.stop();
            end.release();
            end = null;
        }
    }

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // Crea animazione vittoria
        Animation vittoria = AnimationBuilder.with(qiContext)
                .withResources(R.raw.right_hand_high_b001)
                .build();

        // Monta animazione
        Animate animateFinal = AnimateBuilder.with(qiContext)
                .withAnimation(vittoria)
                .build();

        // Crea frase finale
        Say sayFrasefinale1 = SayBuilder.with(qiContext)
                .withText("È stato così divertente giocare con te!").build();

        Say sayFrasefinale2 = SayBuilder.with(qiContext)
                .withText("\\rspd=90\\ Ora puoi riprovare o tornare indietro cliccando sulla casetta").build();

        // Esegui animazione synchronously
        animateFinal.run();
        // Esegui frase finale synchronously
        sayFrasefinale1.run();
        sayFrasefinale2.run();
    }

    @Override
    public void onRobotFocusLost() {
    }

    @Override
    public void onRobotFocusRefused(String reason) {
    }

}
