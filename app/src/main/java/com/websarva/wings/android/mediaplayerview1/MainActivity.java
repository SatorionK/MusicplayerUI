package com.websarva.wings.android.mediaplayerview1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //フィールド
    private MediaPlayer player;

    private ImageButton play_bt;
    private ImageButton fore_bt;
    private ImageButton back_bt;
    private ImageButton shaff_bt;
    private ImageButton repeat_bt;

//--------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_bt = findViewById(R.id.play);
        fore_bt = findViewById(R.id.skip);
        back_bt = findViewById(R.id.skippre);
        shaff_bt = findViewById(R.id.shuff);
        repeat_bt = findViewById(R.id.repeat);

        player = new MediaPlayer();

        String file_uri = "android.resource://" + getPackageName() + "/" + R.raw.change_the_world;
        Uri mediaFileUri = Uri.parse(file_uri);

        try{
            player.setDataSource(MainActivity.this, mediaFileUri);
            player.setOnPreparedListener(new PlaypreparedListener());
            player.setOnCompletionListener(new PlayCompletionListener());
            player.prepareAsync();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
//--------------------------------------------------------------------------------------------------
    //再生ボタンを押した際の処理
    public void onPlayButtonClick(View view){
        //playerが再生中の場合と違う場合
        if(player.isPlaying()){
            player.pause();
            play_bt.setImageResource(R.drawable.ic_play);
            play_bt.setEnabled(true);

            //サービス追加（停止）
            Intent intent = new Intent(MainActivity.this, SoundManageService.class);
            stopService(intent);

         //   play_bt.setEnabled(true);
        }
        else{
           // player.start();
            play_bt.setImageResource(R.drawable.ic_pause_black_24dp);

            //サービス追加（開始）
            Intent intent = new Intent(MainActivity.this, SoundManageService.class);
            startService(intent);

            play_bt.setEnabled(false);
        }

    }
//--------------------------------------------------------------------------------------------------
    //Mediaplayerオブジェクトの解放処理
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(player.isPlaying()){
            player.stop();
        }
        player.release();
        player = null;
    }


//==================================================================================================
//非同期で音楽の準備が完了した際のリスナクラス

private class PlaypreparedListener implements MediaPlayer.OnPreparedListener {

    @Override
    public void onPrepared(MediaPlayer mp){
        //ボタンタップ可能状態
        play_bt.setEnabled(true);
        fore_bt.setEnabled(true);
        back_bt.setEnabled(true);
        shaff_bt.setEnabled(true);
        repeat_bt.setEnabled(true);
    }
}
//==================================================================================================
//非同期で音楽が終了した際のリスナクラス
private class PlayCompletionListener implements MediaPlayer.OnCompletionListener{

    @Override
    public void onCompletion(MediaPlayer mp){

    }
}
//==================================================================================================
//リピート再生(不採用)
private class LoopSwitchListener implements CompoundButton.OnCheckedChangeListener{
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        player.setLooping(isChecked);
    }
}


//==================================================================================================
}
