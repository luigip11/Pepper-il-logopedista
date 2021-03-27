package it.uniba.dib.sysag.pepper4seriousgames.Second_game;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.design.activity.conversationstatus.SpeechBarDisplayStrategy;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.util.PhraseSetUtil;

import it.uniba.dib.sysag.pepper4seriousgames.R;

public class SecondGame04 extends RobotActivity implements RobotLifecycleCallbacks {

    MediaPlayer correctSound, incorrectSound, help;
    ImageView mCard10, mCard11, mCard12;
    ImageButton mHelp;
    Animation animTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QiSDK.register(this, this);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);

        setContentView(R.layout.second_game04);

        mCard10 = (ImageView) findViewById(R.id.card_lego);
        mCard11 = (ImageView) findViewById(R.id.card_cupcake);
        mCard12 = (ImageView) findViewById(R.id.card_horse);

        // Animazione translate per le carte
        animTranslate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
        mCard10.startAnimation(animTranslate);
        mCard11.startAnimation(animTranslate);
        mCard12.startAnimation(animTranslate);

        // Logica delle carte cliccate
        mCard10.setOnClickListener(v -> startIncorrect());
        mCard11.setOnClickListener(v -> startCorrect());
        mCard12.setOnClickListener(v -> startIncorrect());

        // Suono aiuto per risposta corretta
        mHelp = (ImageButton) findViewById(R.id.aiuto);
    }

    // Ferma il gioco e vai al risultato = SecondGameResult
    public void EndActivity (View view) {
        Intent EndIntent = new Intent(getApplicationContext(), SecondGameResult.class);
        startActivity(EndIntent);
    }

    // Avvio suono aiuto
    public void HelpSound (View view) {
        switch (view.getId()) {
            case R.id.aiuto: {
                help = MediaPlayer.create(this, R.raw.dolcetto);
                help.setLooping(false);
                help.setVolume(1.5f, 1.5f);
                help.start();
            }
        }
    }

    public void startCorrect(){
        correctSound = MediaPlayer.create(this, R.raw.correct_sound);
        correctSound.start();
        Intent NextRound = new Intent(getApplicationContext(), SecondGame05.class);
        startActivity(NextRound);
    }

    public void startIncorrect(){
        incorrectSound = MediaPlayer.create(this, R.raw.wrong_sound);
        incorrectSound.start();
        Intent NextRound = new Intent(getApplicationContext(), SecondGame05.class);
        startActivity(NextRound);
    }

    // Pulisci memoria player
    protected void onStop() {
        super.onStop();
        if (help != null) {
            help.stop();
            help.release();
            help = null;
        }
        if (correctSound != null) {
            correctSound.stop();
            correctSound.release();
            correctSound = null;
        }
        if (incorrectSound != null) {
            incorrectSound.stop();
            incorrectSound.release();
            incorrectSound = null;
        }
    }

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {

        // Risposte che riconosce Pepper
        PhraseSet phraseSetCorrect = PhraseSetBuilder.with(qiContext)
                .withTexts("Dolce", "Dolcetto", "Cupcake", "Il dolce", "Il dolcetto", "Il cupcake").build();

        PhraseSet phraseSetIncorrect1 = PhraseSetBuilder.with(qiContext)
                .withTexts("Lego", "Costruzioni", "I lego", "Le costruzioni").build();

        PhraseSet phraseSetIncorrect2 = PhraseSetBuilder.with(qiContext)
                .withTexts("Cavalluccio", "Cavallo a dondolo", "Cavalluccio a dondolo", "Dondolo",
                        "Cavallo", "Il cavallo", "Il cavalluccio", "Il cavallo a dondolo", "Il cavalluccio a dondolo",
                        "Il dondolo").build();

        // Pepper ascolta le risposte
        Listen listen = ListenBuilder.with(qiContext).withPhraseSets(phraseSetCorrect, phraseSetIncorrect1, phraseSetIncorrect2).build();
        ListenResult listenResult = listen.run();
        PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();

        if(PhraseSetUtil.equals(matchedPhraseSet, phraseSetCorrect)){
            Say sayCorrect = SayBuilder.with(qiContext)
                    .withText("Complimenti! Il dolcetto non è un gioco!").build();
            sayCorrect.run();
            com.aldebaran.qi.sdk.object.actuation.Animation correctAnswer = AnimationBuilder.with(qiContext)
                    .withResources(R.raw.affirmation_a006).build();
            Animate animateCorrect = AnimateBuilder.with(qiContext)
                    .withAnimation(correctAnswer).build();
            animateCorrect.run();
            Intent NextRound = new Intent(getApplicationContext(), SecondGame05.class);
            startActivity(NextRound);
        }
        else if(PhraseSetUtil.equals(matchedPhraseSet, phraseSetIncorrect1)){
            Say sayIncorrect1 = SayBuilder.with(qiContext)
                    .withText("Oh Oh! Attenzione! I lego sono un gioco!").build();
            sayIncorrect1.run();
            com.aldebaran.qi.sdk.object.actuation.Animation incorrectAnswer = AnimationBuilder.with(qiContext)
                    .withResources(R.raw.negation_shake_head_b001).build();
            Animate animateIncorrect = AnimateBuilder.with(qiContext)
                    .withAnimation(incorrectAnswer).build();
            animateIncorrect.run();
            Intent NextRound = new Intent(getApplicationContext(), SecondGame05.class);
            startActivity(NextRound);
        }
        else{Say sayIncorrect2 = SayBuilder.with(qiContext)
                .withText("Oh Oh! Attenzione! Il cavallo a dondolo è un gioco!").build();
            sayIncorrect2.run();
            com.aldebaran.qi.sdk.object.actuation.Animation incorrectAnswer = AnimationBuilder.with(qiContext)
                    .withResources(R.raw.negation_shake_head_b001).build();
            Animate animateIncorrect = AnimateBuilder.with(qiContext)
                    .withAnimation(incorrectAnswer).build();
            animateIncorrect.run();
            Intent NextRound = new Intent(getApplicationContext(), SecondGame05.class);
            startActivity(NextRound);
        }
    }

    @Override
    public void onRobotFocusLost() {
    }

    @Override
    public void onRobotFocusRefused(String reason) {
    }

}
