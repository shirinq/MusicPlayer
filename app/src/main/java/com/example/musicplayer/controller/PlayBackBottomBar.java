package com.example.musicplayer.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.Utils.ID3Tags;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jaudiotagger.tag.datatype.Artwork;

import de.hdodenhof.circleimageview.CircleImageView;
import me.tankery.lib.circularseekbar.CircularSeekBar;

public class PlayBackBottomBar {

    private RelativeLayout mParentLayout;
    private CircularSeekBar mSeekBar;
    private CircleImageView mCover;
    private TextView mDuration;
    private ImageView mForward;
    private ImageView mPlay;
    private Activity mActivity;
    private PlayerService mPlayer;
    private Handler mHandler = new Handler();
    private Runnable UpdateSongTime;
    private ForBackListener mForwardListener = new ForBackListener(){
        @Override
        public void run() {
            mPlayer.onFastForward();
            super.run();
        }
    };

    public PlayBackBottomBar(Activity activity, PlayerService service) {
        mActivity = activity;
        mPlayer = service;
        initView();
        setupArtwork();
        BottomAppBarListener();
        UpdateSongTime();
        SeekBar();
    }

    private void initView(){
        mParentLayout = mActivity.findViewById(R.id.song_bar);
        mCover = mActivity.findViewById(R.id.song_bar_artwork);
        mSeekBar = mActivity.findViewById(R.id.song_bar_seek_bar);
        mDuration = mActivity.findViewById(R.id.song_bar_duration);
        mForward = mActivity.findViewById(R.id.skip);
        mPlay = mActivity.findViewById(R.id.play_pause);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void BottomAppBarListener() {
        mParentLayout.setOnClickListener(view -> mActivity.startActivity(SingleSongActivity.newIntent(mActivity, mPlayer.getmCurrentSongIndex())));
        mPlay.setOnClickListener(view -> {
            mPlayer.Pause();
            if (!mPlayer.isPlaying())
                mPlay.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_play_arrow));
            else
                mPlay.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pause));
        });

        mForward.setOnClickListener(view -> mPlayer.goForward());

        mForward.setOnTouchListener(mForwardListener);

        mForward.setOnLongClickListener(mForwardListener);
    }

    private void SeekBar() {
        Runnable mSeekToRun = new Runnable() {
            @Override
            public void run() {
                mSeekBar.setMax(mPlayer.getDuration());
                mSeekBar.setProgress(mPlayer.getCurrentPosition());
                mHandler.postDelayed(this, 130);
            }
        };
        mActivity.runOnUiThread(mSeekToRun);

    }

    private void UpdateSongTime() {
        UpdateSongTime = new Runnable() {
            @Override
            public void run() {
                int sTime = mPlayer.getCurrentPosition();
                int mns = (sTime / 60000) % 60000;
                int scs = sTime % 60000 / 1000;
                String songTime = String.format("%02d:%02d", mns, scs);
                mDuration.setText(songTime);
                mHandler.postDelayed(this, 100);
            }
        };
        mActivity.runOnUiThread(UpdateSongTime);
    }

    private void setupArtwork(){

        if (mPlayer.getmCurrentSongIndex() == null)
            return;

        Artwork artwork = ID3Tags.getArtwork(mPlayer.getmCurrentSongIndex().getFilePath());
        Bitmap bitmap;

        if (artwork == null)
            bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.song_placeholder);
        else
            bitmap = BitmapFactory.decodeByteArray(artwork.getBinaryData(), 0, artwork.getBinaryData().length);

        Glide.with(mActivity).asDrawable()
                .placeholder(R.drawable.song_placeholder)
                .load(bitmap)
                .into(mCover);
    }

    public static class ForBackListener implements View.OnLongClickListener, Runnable, View.OnTouchListener {

        private Handler mHandler = new Handler();

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                mHandler.removeCallbacks(ForBackListener.this);
            return false;
        }


        @Override
        public boolean onLongClick(View view) {
            mHandler.post(this);
            return true;
        }

        @Override
        public void run() {
            mHandler.postDelayed(this, 500);
        }
    }
}
