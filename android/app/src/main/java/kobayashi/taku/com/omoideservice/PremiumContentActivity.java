package kobayashi.taku.com.omoideservice;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

public class PremiumContentActivity extends AppCompatActivity {
    private VideoView mVideoView;
    private ImageButton mPlayerButton;
    private Handler mHandler;
    private Runnable updateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premum_content_view);

        mHandler = new Handler();
        mVideoView = (VideoView) findViewById(R.id.contentVideoView);
        mVideoView.setVideoURI(getContentUri());
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                setupMediaController();
            }
        });
        setupMediaController();
    }

    private void setupMediaController(){
        mPlayerButton = (ImageButton) findViewById(R.id.MediaPlayerButton);
        mPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVideoView.isPlaying()){
                    mVideoView.pause();
                    mPlayerButton.setImageResource(R.mipmap.audioplay_icon);
                }else{
                    mVideoView.start();
                    mPlayerButton.setImageResource(R.mipmap.audiopause_icon);
                }
            }
        });
        mPlayerButton.setImageResource(R.mipmap.audiopause_icon);
        SeekBar seekBar = (SeekBar) findViewById(R.id.MediaPlayerSeek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                if(!mVideoView.isPlaying()) {
                    seekBar.setProgress(progress);
                    mVideoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mPlayerButton.setImageResource(R.mipmap.audioplay_icon);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(!mVideoView.isPlaying()){
                    mVideoView.start();
                    mPlayerButton.setImageResource(R.mipmap.audiopause_icon);
                }
            }
        });
        seekBar.setMax(mVideoView.getDuration());
        TextView endTimeText = (TextView) findViewById(R.id.EndTimeText);
        endTimeText.setText(ApplicationHelper.ConversionTime(mVideoView.getDuration()));

        TextView nowTimeText = (TextView) findViewById(R.id.NowTimeText);
        nowTimeText.setText(ApplicationHelper.ConversionTime(mVideoView.getCurrentPosition()));
    }

    private void setupCountDownTimer(){
        updateText = new Runnable() {
            public void run() {
                if(mVideoView.isPlaying()) {
                    TextView nowTimeText = (TextView) findViewById(R.id.NowTimeText);
                    nowTimeText.setText(ApplicationHelper.ConversionTime(mVideoView.getCurrentPosition()));
                    SeekBar seekBar = (SeekBar) findViewById(R.id.MediaPlayerSeek);
                    seekBar.setProgress(mVideoView.getCurrentPosition());
                }
                mHandler.removeCallbacks(updateText);
                mHandler.postDelayed(updateText, 1000);
            }
        };
        mHandler.postDelayed(updateText, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mVideoView.isPlaying()) {
            mVideoView.start();
        }
        setupCountDownTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mVideoView.isPlaying()){
            mVideoView.pause();
        }
        mHandler.removeCallbacks(updateText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApplicationHelper.releaseVideoView(mVideoView);
    }

    private Uri getContentUri(){
        return Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.nebuta);
//        return Uri.parse("file:///android_asset/nebuta.mp4");
    }
}
