package it.uniba.dib.sysag.pepper4seriousgames.First_game;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import it.uniba.dib.sysag.pepper4seriousgames.R;

public class FirstGame extends RobotActivity implements View.OnClickListener, RobotLifecycleCallbacks {

    private ImageView mAnimals;
    private TextView mScoreView, mQuestion;
    private Button mMareButton, mTerraButton, mSoundButton;
    private MediaPlayer player, playRight, playWrong;

    private String mAnswer;
    private int mScore = 0;
    private int mQuestionNumber = 0;

    int turn = 1;

    private QiContext qiContext;
    private static final String TAG = "FirstGame";
    private Animate animate;

    // Dichiarazione variabili per registrare audio e salvarlo //
    private ImageButton mRecordBtn, mStopBtn;
    private TextView mRecordLabel;

    private MediaRecorder recorder;

    private String mFileName = null;
    private static final String LOG_TAG = "Record_log";

    private StorageReference mStorage;

    private ProgressDialog mProgress;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QiSDK.register(this, this);

        setSpeechBarDisplayStrategy(SpeechBarDisplayStrategy.IMMERSIVE);

        setContentView(R.layout.first_game);

        mAnimals = (ImageView) findViewById(R.id.animals);
        // Animazione fade in per gli animali
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_4animals);
        mAnimals.startAnimation(animation);

        mScoreView = (TextView) findViewById(R.id.punti);

        mQuestion = (TextView) findViewById(R.id.domanda);

        mMareButton = (Button) findViewById(R.id.mareButton);
        mTerraButton = (Button) findViewById(R.id.terraButton);

        mSoundButton = (Button) findViewById(R.id.soundButton);
        mSoundButton.setOnClickListener(this);

        // Suoni in base alla risposta
        playRight = MediaPlayer.create(this, R.raw.right_sound);
        playWrong = MediaPlayer.create(this, R.raw.wrong_sound);

        updateQuestion();

        // Logica per il tasto "mare"
        mMareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ferma il player se è attivo
                if (player != null && player.isPlaying())
                    player.stop();

                // se la risposta è esatta
                if (mAnswer == "mare") {
                    mScore++;
                    updateScore(mScore);
                    // avvia suono risposta esatta
                    playRight.start();

                    if (mQuestionNumber == DatabaseAnimals.questions.length) {
                        Intent i = new Intent(FirstGame.this, FirstGameResult.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("finalscore", mScore);
                        i.putExtras(bundle);
                        FirstGame.this.finish();
                        startActivity(i);
                    } else {
                        updateQuestion();
                    }
                }
                // se la risposta è sbagliata
                else {
                    // avvia suono risposta errata
                    playWrong.start();
                    if (mQuestionNumber == DatabaseAnimals.questions.length) {
                        Intent i = new Intent(FirstGame.this, FirstGameResult.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("finalscore", mScore);
                        i.putExtras(bundle);
                        FirstGame.this.finish();
                        startActivity(i);
                    } else {
                        updateQuestion();
                    }
                }
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_4animals);
                mAnimals.startAnimation(animation);
            }
        });

        // Logica per il tasto "terra"
        mTerraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ferma il player se è attivo
                if (player != null && player.isPlaying())
                    player.stop();

                // se la risposta è esatta
                if (mAnswer == "terra") {
                    mScore++;
                    updateScore(mScore);
                    // avvia suono risposta esatta
                    playRight.start();

                    if (mQuestionNumber == DatabaseAnimals.questions.length) {
                        Intent i = new Intent(FirstGame.this, FirstGameResult.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("finalscore", mScore);
                        i.putExtras(bundle);
                        FirstGame.this.finish();
                        startActivity(i);
                    } else {
                        updateQuestion();
                    }
                }
                // se la risposta è sbagliata
                else {
                    // avvia suono risposta errata
                    playWrong.start();
                    if (mQuestionNumber == DatabaseAnimals.questions.length) {
                        Intent i = new Intent(FirstGame.this, FirstGameResult.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("finalscore", mScore);
                        i.putExtras(bundle);
                        FirstGame.this.finish();
                        startActivity(i);
                    } else {
                        updateQuestion();
                    }
                }
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_4animals);
                mAnimals.startAnimation(animation);
            }
        });

        // Per registrazione audio //
        mStorage = FirebaseStorage.getInstance().getReference("Gioco_animali"); // Cartella creata su Firebase > Storage

        mRecordBtn = (ImageButton) findViewById(R.id.rec);
        mStopBtn = (ImageButton) findViewById(R.id.recStop);
        mRecordLabel = (TextView) findViewById(R.id.textView);

        mProgress = new ProgressDialog(this);

        // Record to the external cache directory for visibility
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audio_00.3gp";

        mRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecordBtn != null) {
                    startRecording();
                    mRecordLabel.setText("Registrazione avviata!");
                }
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
                mRecordLabel.setText("Registrazione stoppata.");
            }
        });

    }

    // Avvia registrazione vocale
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(mFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    // Ferma registrazione vocale
    public void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        uploadAudio();
    }

    private void uploadAudio() {
        mProgress.setMessage("Uploading file audio...");
        mProgress.show();

        StorageReference filepath = mStorage.child("Audio").child("new_audio.mp3");

        Uri mUri = Uri.fromFile(new File(mFileName));

        filepath.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mProgress.dismiss();
                mRecordLabel.setText("Upload terminato!");
            }
        });
    }
    // Fine registrazione vocale //

    private String upperCaseFirstLetter(String animals) {
        StringBuilder str = new StringBuilder(animals);
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private void updateQuestion() {
        mAnimals.setImageResource(DatabaseAnimals.images[mQuestionNumber]);
        mQuestion.setText(DatabaseAnimals.questions[mQuestionNumber]);
        mAnswer = DatabaseAnimals.answers[mQuestionNumber];
        mQuestionNumber++;
    }

    // Visualizza i punti raccolti
    public void updateScore(int point) {
        mScoreView.setText("" + mScore);
    }

    // Logica per i versi degli animali
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.soundButton: {
                int animalSoundId = this.getResources().getIdentifier(mQuestion.getText().toString().toLowerCase(), "raw", this.getPackageName());
                player = MediaPlayer.create(this, animalSoundId);
                player.setLooping(false);
                player.setVolume(1.5f, 1.5f);
                player.start();
            }
        }
    }

    // Pulisci memoria player
    protected void onStop() {
        super.onStop();
        if (playRight != null) {
            playRight.stop();
            playRight.release();
            playRight = null;
        }
        if (playWrong != null) {
            playWrong.stop();
            playWrong.release();
            playWrong = null;
        }
    }

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        this.qiContext = qiContext;
        Log.i(TAG, "Robot focus gained, running.");

        // Crea domanda animale
        Say sayAnimale = SayBuilder.with(qiContext)
                .withText("Come si chiama l'animale che vedi?").build();

        // Esegui domanda synchronously
        sayAnimale.run();

        // Animali che riconosce Pepper
        PhraseSet phraseSetCorrect = PhraseSetBuilder.with(qiContext)
                .withTexts("Cane", "Il cane", "Un cane").build();  //from DatabaseAnimals.questions

        PhraseSet phraseSetIncorrect = PhraseSetBuilder.with(qiContext)
                .withTexts("Gatto", "Bau", "Bau bau").build();

        // Pepper ascolta le risposte
        Listen listen = ListenBuilder.with(qiContext).withPhraseSets(phraseSetCorrect, phraseSetIncorrect).build();
        ListenResult listenResult = listen.run();
        PhraseSet matchedPhraseSet = listenResult.getMatchedPhraseSet();

        if(PhraseSetUtil.equals(matchedPhraseSet, phraseSetCorrect)){
            com.aldebaran.qi.sdk.object.actuation.Animation correctAnswer = AnimationBuilder.with(qiContext)
                    .withResources(R.raw.affirmation_a006).build();
            Animate animateCorrect = AnimateBuilder.with(qiContext)
                    .withAnimation(correctAnswer).build();
            animateCorrect.run();
        }
        else{
            com.aldebaran.qi.sdk.object.actuation.Animation incorrectAnswer = AnimationBuilder.with(qiContext)
                    .withResources(R.raw.negation_shake_head_b001).build();
            Animate animateIncorrect = AnimateBuilder.with(qiContext)
                    .withAnimation(incorrectAnswer).build();
            animateIncorrect.run();
        }

        // Crea animazione mostra tablet
        com.aldebaran.qi.sdk.object.actuation.Animation showTablet3 = AnimationBuilder.with(qiContext)
                .withResources(R.raw.tap_display_right_hand_b001).build();
        // Monta animazione
        Animate animate2 = AnimateBuilder.with(qiContext)
                .withAnimation(showTablet3).build();
        this.animate = animate2;

        // Crea domanda ambiente
        Say sayAmbiente = SayBuilder.with(qiContext)
                .withText("Ora tocca dove abita!").build();

        // Esegui domanda asynchronously
        sayAmbiente.async().run();
        // Esegui animazione synchronously
        animate2.async().run();
    }

    @Override
    public void onRobotFocusLost() {
    }

    @Override
    public void onRobotFocusRefused(String reason) {
    }

}
