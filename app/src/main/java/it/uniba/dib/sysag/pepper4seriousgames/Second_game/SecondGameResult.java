package it.uniba.dib.sysag.pepper4seriousgames.Second_game;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class SecondGameResult extends RobotActivity implements RobotLifecycleCallbacks {

    MediaPlayer end;
    Button mRiprova;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QiSDK.register(this, this);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);

        setContentView(R.layout.second_game_result);

        // Avvia suono applausi
        end = MediaPlayer.create(this, R.raw.applausi_short);
        end.setVolume(1.5f, 1.5f);
        end.start();

        // Tasto riprova
        mRiprova = (Button) findViewById(R.id.riprova);
        mRiprova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondGameResult.this, SecondGame01.class));
                SecondGameResult.this.finish();
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
                .withResources(R.raw.nicereaction_a001)
                .build();

        // Monta animazione
        Animate animateFinal = AnimateBuilder.with(qiContext)
                .withAnimation(vittoria)
                .build();

        // Crea frase finale
        Say sayFrasefinale = SayBuilder.with(qiContext)
                .withText("Fantastico!").build();

        Say sayFrasefinale1 = SayBuilder.with(qiContext)
                .withText("Mi sono divertito molto con te!").build();

        Say sayFrasefinale2 = SayBuilder.with(qiContext)
                .withText("\\rspd=90\\ Puoi riprovare o tornare indietro cliccando sulla casetta").build();

        // Esegui animazione synchronously
        animateFinal.run();
        // Esegui frase finale synchronously
        sayFrasefinale.run();
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

