package it.uniba.dib.sysag.pepper4seriousgames;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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

public class SecondActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private ImageView Pepper2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QiSDK.register(this, this);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);

        setContentView(R.layout.activity_second);

        // Visualizza il primo piano di Pepper
        Pepper2 = (ImageView) findViewById(R.id.pepper2);

        // Animazione fade in per l'immagine di Pepper
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_4animals);
        Pepper2.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

        // Crea animazione saluto
        Animation saluto = AnimationBuilder.with(qiContext) // Create the builder with the context
                .withResources(R.raw.hello_a010) // Set the action to say
                .build(); // Build the say action

        // Monta animazione
        Animate animate = AnimateBuilder.with(qiContext)
                .withAnimation(saluto)
                .build();

        // Crea saluto
        Say sayCiao = SayBuilder.with(qiContext) // Create the builder with the context
                .withText("Ciao! Sono Pepperrr!") // Set the text to say
                .build(); // Build the say action

        // Esegui animazione asynchronously
        sayCiao.async().run();
        // Esegui saluto synchronously
        animate.run();

        // Vai alla schermata successiva
        Intent activityIntent = new Intent(getApplicationContext(), ThirdActivity.class);
        startActivity(activityIntent);

    }

    @Override
    public void onRobotFocusLost() {
    }

    @Override
    public void onRobotFocusRefused(String reason) {
    }

}
