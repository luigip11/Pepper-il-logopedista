package it.uniba.dib.sysag.pepper4seriousgames;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;

/**
 * Created on 01.03.2021.
 * @author Virtual LP - Matricola 603451 - luigi.p11@outlook.it
 */

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private ImageView Pepper1, Logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the RobotLifecycleCallbacks to this Activity
        QiSDK.register(this, this);

        // Modifica speech bar di Pepper
        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);

        // Visualizza il layout del main
        setContentView(R.layout.activity_main);

        // Visualizza il primo piano di Pepper
        Pepper1 = (ImageView) findViewById(R.id.pepper1);

        // Visualizza il logo copyright
        Logo = (ImageView) findViewById(R.id.copyright);

        // Animazione fade in per l'immagine di Pepper e del logo
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_4animals);
        Pepper1.startAnimation(animation);
        Logo.startAnimation(animation);

    }

    public void GoToSecondActivity (View view) {
        Intent activity2Intent = new Intent(getApplicationContext(), SecondActivity.class);
        startActivity(activity2Intent);
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // Create a new say action
    }

    @Override
    public void onRobotFocusLost() {
        // The robot focus is lost
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused
    }

}