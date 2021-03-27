package it.uniba.dib.sysag.pepper4seriousgames;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.conversation.Say;

public class ThirdActivity extends RobotActivity implements RobotLifecycleCallbacks {

    ImageButton mPlayButton, mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QiSDK.register(this, this);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);

        setContentView(R.layout.activity_third);

        mPlayButton = (ImageButton) findViewById(R.id.playButton);
        mBackButton = (ImageButton) findViewById(R.id.backButton);

        // Animazione fade in per i pulsanti
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_4arrows);
        mPlayButton.startAnimation(animation);
        mBackButton.startAnimation(animation);
    }

    // Passa all'attività di selezione gioco = SelectActivity
    public void GoToSelectActivity (View view) {
        Intent GoToSelectIntent = new Intent(getApplicationContext(), SelectActivity.class);
        startActivity(GoToSelectIntent);
    }

    // Torna all'inizio = MainActivity
    public void CameToMainActivity (View view) {
        Intent CameToMainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(CameToMainIntent);
    }

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

        // Crea animazione mostra tablet
        com.aldebaran.qi.sdk.object.actuation.Animation showTablet = AnimationBuilder.with(qiContext)
                .withResources(R.raw.show_tablet_a006).build();
        // Monta animazione
        Animate animate2 = AnimateBuilder.with(qiContext)
                .withAnimation(showTablet).build();

        // Domanda di gioco
        Say sayGioco = SayBuilder.with(qiContext)
                .withText("Sei pronto a giocare con me?").build();

        // Esegui domanda di gioco asynchronously
        sayGioco.async().run();
        // Esegui animazione synchronously
        animate2.run();
    }

    @Override
    public void onRobotFocusLost() {
    }

    @Override
    public void onRobotFocusRefused(String reason) {
    }

}
