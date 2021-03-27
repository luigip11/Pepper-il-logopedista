package it.uniba.dib.sysag.pepper4seriousgames;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

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

public class SelectActivity extends RobotActivity implements RobotLifecycleCallbacks {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QiSDK.register(this, this);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);

        setContentView(R.layout.activity_select);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ViewPagerAdapter viewPageAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPageAdapter);

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
                .withResources(R.raw.show_tablet_a004).build();
        // Monta animazione
        Animate animateTablet = AnimateBuilder.with(qiContext)
                .withAnimation(showTablet).build();

        // Domanda di gioco
        Say sayGioco = SayBuilder.with(qiContext)
                .withText("Scegli il gioco!").build();

        // Esegui domanda di gioco synchronously
        sayGioco.run();
        // Esegui animazione synchronously
        animateTablet.run();

    }

    @Override
    public void onRobotFocusLost() {
    }

    @Override
    public void onRobotFocusRefused(String reason) {
    }

}
