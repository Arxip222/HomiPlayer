package homi.play.player;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import homi.play.player.ui.slideshow.SlideshowFragment;

import static homi.play.player.SearchActivity.UserF;
import static homi.play.player.VideoListActivity.file;

public class VideoActivity extends AppCompatActivity {
    String url;
    ImageButton btn;
    TextView textView;
    VideoView videoView;
    Toolbar toolbar;
    boolean pos = true;
    LinearLayout linn;
    int type, position;
    ImageView Vback, Vplay, Vnext;
    String name, thumb;
    TextView text1, text2;
    SeekBar seekBar;
    Handler handler = new Handler();
    Runnable runnable;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar = findViewById(R.id.toolbar);
        linn = findViewById(R.id.linn);
        toolbar.getBackground().setAlpha(0);
        btn = findViewById(R.id.btn);
        seekBar = findViewById(R.id.timeSeek);
        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#7B0E0E"), PorterDuff.Mode.SRC_ATOP);
        seekBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#111111"), PorterDuff.Mode.SRC_ATOP);
        seekBar.getThumb().setColorFilter(Color.parseColor("#9C1A1A"), PorterDuff.Mode.SRC_ATOP);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        position = getIntent().getIntExtra("position", -1);
        textView = findViewById(R.id.textView);
        Vback = findViewById(R.id.Vback);
        Vplay = findViewById(R.id.Vplay);
        Vnext = findViewById(R.id.Vnext);
        videoView = findViewById(R.id.fullScreenVideoView);
        url = getIntent().getStringExtra("url");
        if(url == null){
            playLocalVideo();
        }
        else{
            playUrlVideo(url);
        }



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        Vback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type == 1){
                    try{
                        thumb = file.get(position-1).getThumb();
                        --position;
                        playLocalVideo();
                    }catch (Exception e){
                        Toast.makeText(VideoActivity.this, "Это первое видео в этой папке!", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(type == 0){
                    try{
                        thumb = UserF.get(position-1).getThumb();
                        --position;
                        playLocalVideo();
                    }catch (Exception e){
                        Toast.makeText(VideoActivity.this, "Это первое видео в этой папке!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Vnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type == 1){
                    try{
                        thumb = file.get(position+1).getThumb();
                        ++position;
                        playLocalVideo();
                    }catch (Exception e){
                        Toast.makeText(VideoActivity.this, "Это последнее видео в этой папке!", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(type == 0){
                    try{
                        thumb = UserF.get(position+1).getThumb();
                        ++position;
                        playLocalVideo();
                    }catch (Exception e){
                        Toast.makeText(VideoActivity.this, "Это последнее видео в этой папке!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Vplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoView.isPlaying()){
                    Vplay.setBackground(getDrawable(R.drawable.ic_play));
                    videoView.pause();
                }
                else{
                    Vplay.setBackground(getDrawable(R.drawable.pause));
                    videoView.start();
                }
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(VideoActivity.this, "Произошла ошибка!!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });




        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(pos) {
                    FullScreencall();
                    linn.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                    pos = false;
                }
                else{
                    FullScreencall();
                    linn.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    pos = true;
                }
                FullScreencall();

                return false;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b) {
                    mediaPlayer.seekTo(i);
                    seekBar.setProgress(i);
                    text1.setText(""+getDuration(mediaPlayer.getCurrentPosition()));
                    text2.setText(""+getDuration(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()));

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void playLocalVideo() {
        try {

            if (getIntent().getIntExtra("t", -1) == 0) {
                name = UserF.get(position).getName();
                thumb = UserF.get(position).getThumb();
                type = 0;
            } else if (getIntent().getIntExtra("t", -1) == 1) {
                name = file.get(position).getName();
                thumb = file.get(position).getThumb();
                type = 1;
            }
            textView.setText(name);
            FullScreencall();
            videoView.setVideoPath(thumb);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer = mp;
                    seekBar.setMax(mediaPlayer.getDuration());
                    Vplay.setBackground(getDrawable(R.drawable.pause));
                    updateSeekbar();
                    videoView.start();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Произошла ошибка!!", Toast.LENGTH_SHORT).show();
        }
    }
    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void playUrlVideo(String url){
        try {
            videoView.setVideoURI(Uri.parse(url));
            FullScreencall();
            videoView.requestFocus();
            https://samplelib.com/lib/preview/mp4/sample-30s.mp4
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer = mp;
                    text1.setText(getDuration(mp.getCurrentPosition()));
                    text2.setText(getDuration(mp.getDuration()));
                    seekBar.setMax(mediaPlayer.getDuration());
                    Vplay.setBackground(getDrawable(R.drawable.pause));
                    updateSeekbar();
                    videoView.start();
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (getIntent().getStringExtra("url").equals("") || getIntent().getStringExtra("url") == null) {
                        Toast.makeText(VideoActivity.this, "" + getResources().getString(R.string.war), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VideoActivity.this, "" + getResources().getString(R.string.type), Toast.LENGTH_SHORT).show();
                    }

                    return false;
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Произошла ошибка!!", Toast.LENGTH_SHORT).show();
            Log.d("ARKHIP", e.toString());
            onBackPressed();
            onBackPressed();
        }
    }


    public String getDuration(int duration) {
        String min_result = "";
        String sec_result = "";
        String hrs_result = "";
        duration /= 1000;

        if(duration / 3600 < 10){
            hrs_result = "0"+duration/3600;
        }
        else{
            hrs_result = ""+duration/3600;
        }
        duration -= duration/3600*3600;
        if(duration / 60 < 10){
            min_result = "0"+duration/60;
        }else{
            min_result = ""+duration/60;
        }
        duration -= duration/60*60;
        if(duration < 10){
            sec_result = "0"+duration;
        }
        else{
            sec_result =""+duration;
        }
        return hrs_result+":"+min_result+":"+sec_result;
    }

    public void updateSeekbar() {
        try {


            int currPos = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(currPos);
            text1.setText("" + getDuration(mediaPlayer.getCurrentPosition()));
            text2.setText("" + getDuration(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()));

            runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekbar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }catch (Exception e){

        }
    }
}
