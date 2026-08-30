package com.mj.natasha;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.widget.*;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {
    TextView chatText, statusText;
    EditText inputText;
    TextToSpeech tts;
    SpeechRecognizer recognizer;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);
        chatText=findViewById(R.id.chatText);
        statusText=findViewById(R.id.statusText);
        inputText=findViewById(R.id.inputText);
        tts=new TextToSpeech(this, r -> { if(r==TextToSpeech.SUCCESS) tts.setLanguage(Locale.getDefault()); });
        findViewById(R.id.sendButton).setOnClickListener(v -> send());
        findViewById(R.id.micButton).setOnClickListener(v -> speakInput());
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},10);
    }

    void send() {
        String q=inputText.getText().toString().trim();
        if(q.isEmpty()) return;
        chatText.append("\n\nYou: "+q);
        inputText.setText("");
        String a=reply(q);
        chatText.append("\nNatasha: "+a);
        tts.speak(a,TextToSpeech.QUEUE_FLUSH,null,"natasha");
    }

    String reply(String q) {
        String s=q.toLowerCase(Locale.getDefault());
        if(s.contains("hello")||s.contains("hi")||s.contains("नमस्कार"))
            return "नमस्कार! मी M.J Natasha AI आहे. मी मदत करण्यासाठी तयार आहे.";
        if(s.contains("name")||s.contains("नाव"))
            return "माझे नाव M.J Natasha AI आहे.";
        return "तुमचा संदेश समजला. अधिक स्मार्ट उत्तरांसाठी AI API जोडता येईल.";
    }

    void speakInput() {
        if(!SpeechRecognizer.isRecognitionAvailable(this)) { statusText.setText("Speech recognition unavailable"); return; }
        Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        recognizer=SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(new android.speech.RecognitionListener() {
            public void onReadyForSpeech(Bundle b){statusText.setText("Listening...");}
            public void onBeginningOfSpeech(){} public void onRmsChanged(float r){}
            public void onBufferReceived(byte[] b){} public void onEndOfSpeech(){statusText.setText("Processing...");}
            public void onError(int e){statusText.setText("Try again");}
            public void onResults(Bundle b){
                ArrayList<String> x=b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(x!=null&&!x.isEmpty()){inputText.setText(x.get(0));send();}
                statusText.setText("Ready"); recognizer.destroy();
            }
            public void onPartialResults(Bundle b){} public void onEvent(int a,Bundle b){}
        });
        recognizer.startListening(i);
    }

    @Override protected void onDestroy(){
        if(recognizer!=null) recognizer.destroy();
        if(tts!=null){tts.stop();tts.shutdown();}
        super.onDestroy();
    }
}
