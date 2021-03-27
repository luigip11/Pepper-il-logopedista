package it.uniba.dib.sysag.pepper4seriousgames.Fourth_game;

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

import it.uniba.dib.sysag.pepper4seriousgames.Fourth_game.Scenario1.Scenario1_01;
import it.uniba.dib.sysag.pepper4seriousgames.Fourth_game.Scenario2.Scenario2_01;
import it.uniba.dib.sysag.pepper4seriousgames.R;
import it.uniba.dib.sysag.pepper4seriousgames.SelectActivity;

public class SelectScenario extends RobotActivity implements RobotLifecycleCallbacks {

    ImageButton mScenario1, mScenario2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QiSDK.register(this, this);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);

        setContentView(R.layout.scenario_select);

        // Visualizza i tasti per gli scenari
        mScenario1 = (ImageButton) findViewById(R.id.scenarioFirst);
        mScenario2 = (ImageButton) findViewById(R.id.scenarioSecond);

        // Animazione fade in per gli scenari
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        mScenario1.startAnimation(animation);
        mScenario2.startAnimation(animation);
    }

    // Vai al primo scenario = Scenario1_01
    public void GoToScenario1 (View view) {
        Intent GoToScenario1Intent = new Intent(getApplicationContext(), Scenario1_01.class);
        startActivity(GoToScenario1Intent);
    }

    // Vai al secondo scenario = Scenario2_01
    public void GoToScenario2 (View view) {
        Intent GoToScenario2Intent = new Intent(getApplicationContext(), Scenario2_01.class);
        startActivity(GoToScenario2Intent);
    }

    // Torna alla scelta del gioco = SelectActivity
    public void CameToSelectActivity (View view) {
        Intent CameToSelectIntent = new Intent(getApplicationContext(), SelectActivity.class);
        startActivity(CameToSelectIntent);
    }

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // Crea animazione mostra tablet
        com.aldebaran.qi.sdk.object.actuation.Animation showTablet2 = AnimationBuilder.with(qiContext)
                .withResources(R.raw.show_tablet_a004).build();
        // Monta animazione
        Animate animate = AnimateBuilder.with(qiContext)
                .withAnimation(showTablet2).build();

        // Crea frase finale
        Say sayScenario = SayBuilder.with(qiContext)
                .withText("Seleziona lo scenario!").build();

        // Esegui frase scenario synchronously
        sayScenario.run();
        // Esegui animazione synchronously
        animate.run();
    }

    @Override
    public void onRobotFocusLost() {
    }

    @Override
    public void onRobotFocusRefused(String reason) {
    }

}
