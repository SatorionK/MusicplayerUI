package com.websarva.wings.android.mediaplayerview1;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

//フォアグラウンドで再生し、アプリがバックグラウンドに移っても再生はされるが
//暫く、うらで生きている（しかし、ローメモリーキラーにより消される可能性あり）
//それを防ぐため、フォワグラウンド化する。

public class SoundManageService extends Service {

    //フィールド
    private MediaPlayer player;



//--------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(){
        player = new MediaPlayer();

        //通知チャンネル作成
        //チャンネルID1
        String id = "notificationtest";
        String name = getString(R.string.norifi);
        //通知重要度
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        //チャンネル生成
        NotificationChannel chanel = new NotificationChannel(id, name, importance);
        //NotificationManagerのオブジェクト化
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //チャンネル登録
        manager.createNotificationChannel(chanel);

    }


//--------------------------------------------------------------------------------------------------
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //startServiceでサービスが開始要求を受けたときのコールバック（サービスが始まる。つまりstartserviceメソッド
        // が使われた時、このメソッドの処理が自動的に処理される（コールバック））
        String media_file_uri_str = "android.resource://" + getPackageName() + "/" + R.raw.change_the_world;
        Uri media_file_uri = Uri.parse(media_file_uri_str);

        try{
            player.setDataSource(SoundManageService.this, media_file_uri);
            player.setOnPreparedListener(new PlayerPreparedListener());
            player.setOnCompletionListener(new PlayCompletionListener());
            player.prepareAsync();
        }catch(IOException e){
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }
//--------------------------------------------------------------------------------------------------
    @Override
    public void onDestroy(){
        //プレイヤーが再生中の場合　T：プレイヤ停止　F：なし
        if(player.isPlaying()){
            player.stop();
        }

        player.release();
        player = null;
    }
//--------------------------------------------------------------------------------------------------
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
//--------------------------------------------------------------------------------------------------


    //==================================================================================================
    //media再生準備OK時のリスナクラス
    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener{
        //
        @Override
        public void onPrepared(MediaPlayer mp){
            //再生開始通知
            NotificationCompat.Builder builder = new NotificationCompat.Builder(SoundManageService.this, "notificationtest");
            builder.setSmallIcon(android.R.drawable.ic_media_next);
            builder.setContentTitle(getString(R.string.norifibegin));
            builder.setContentText(getString(R.string.norifibegin));
            Notification notification = builder.build();

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, notification);


            mp.start();
        }
}
//==================================================================================================
//media再生終了時のリスナクラス
private class PlayCompletionListener implements MediaPlayer.OnCompletionListener{
    //
    @Override
    public void onCompletion(MediaPlayer mp){

        //再生終了通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SoundManageService.this, "notificationtest");
        builder.setSmallIcon(android.R.drawable.ic_media_next);
        builder.setContentTitle(getString(R.string.norififinish));
        builder.setContentText(getString(R.string.norififinish));
        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification);
        stopSelf();
    }



}
//==================================================================================================
}
