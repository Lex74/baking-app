package com.lexoid.bakingapp.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.lexoid.bakingapp.BuildConfig;
import com.lexoid.bakingapp.R;
import com.lexoid.bakingapp.data.models.Step;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepDetailFragment extends Fragment {
    public static final String ARG_STEPS = "steps";
    public static final String ARG_STEP_ID = "step_id";

    public static final String CURRENT_POSITION_KEY = "current_position";
    public static final String CURRENT_STATE_KEY = "current_state";
    public static final String PLAY_WHEN_READY_KEY = "play_when_ready";

    Unbinder unbinder;

    private Step step;

    @BindView(R.id.playerView)
    PlayerView playerView;
    private ExoPlayer exoPlayer;

    @Nullable
    @BindView(R.id.long_description)
    TextView descriptionTv;

    @Nullable
    @BindView(R.id.step_thumb_iv)
    ImageView thumbIv;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat playbackState;

    public StepDetailFragment() {
    }

    public static StepDetailFragment getFragment(Step step) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEPS, step);
        stepDetailFragment.setArguments(args);
        return stepDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEPS)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            step = getArguments().getParcelable(ARG_STEPS);

            AppCompatActivity activity = (AppCompatActivity) this.getActivity();

            ActionBar appBarLayout = activity.getSupportActionBar();
            if (appBarLayout != null) {
                appBarLayout.setTitle(step.getShortDescription());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        if (step != null) {
            if (descriptionTv != null) {
                descriptionTv.setText(step.getDescription());
            }
            if (thumbIv != null && !TextUtils.isEmpty(step.getThumbnailURL())){
                Picasso.get()
                        .load(step.getThumbnailURL())
                        .into(thumbIv);
            }
            initializeMediaSession();
            playerInit(Uri.parse(step.getVideoURL()));
        }
        if (savedInstanceState != null) {
            long position = savedInstanceState.getLong(CURRENT_POSITION_KEY);
            int state = savedInstanceState.getInt(CURRENT_STATE_KEY);
            boolean playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
            exoPlayer.setPlayWhenReady(playWhenReady);
            if (state == PlaybackStateCompat.STATE_PLAYING){
                exoPlayer.setPlayWhenReady(true);
            }
            exoPlayer.seekTo(position);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (exoPlayer != null) {
            long currentPosition = exoPlayer.getCurrentPosition();
            int state = playbackState.getState();
            boolean playWhenReady = exoPlayer.getPlayWhenReady();
            outState.putLong(CURRENT_POSITION_KEY, currentPosition);
            outState.putInt(CURRENT_STATE_KEY, state);
            outState.putBoolean(PLAY_WHEN_READY_KEY, playWhenReady);
        }
        super.onSaveInstanceState(outState);
    }

    private void initializeMediaSession(){
        mediaSession = new MediaSessionCompat(getContext(), "StepDetailFragment");

        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        playbackState = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                            PlaybackStateCompat.ACTION_PAUSE |
                            PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .build();

        mediaSession.setPlaybackState(playbackState);

        mediaSession.setCallback(new MySessionCallback());

        mediaSession.setActive(true);
    }

    private void playerInit(Uri mediaUri) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        playerView.setPlayer(exoPlayer);
        setDefaultArtwork();

        if (!TextUtils.isEmpty(step.getThumbnailURL())) {
            Picasso.get()
                    .load(step.getThumbnailURL())
                    .into(new PlayerViewImage());
        }

        String userAgent = Util.getUserAgent(getContext(), "Baking App");
        MediaSource mediaSource = new ExtractorMediaSource(
                mediaUri,
                new DefaultDataSourceFactory(getContext(), userAgent),
                new DefaultExtractorsFactory(),
                null,
                null);
        exoPlayer.prepare(mediaSource);
    }

    private void setDefaultArtwork() {
        Bitmap videoIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_movies);
        playerView.setUseArtwork(true);
        playerView.setDefaultArtwork(videoIcon);
    }

    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    public void onPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        mediaSession.setActive(false);
        super.onDestroy();
    }

    private class PlayerViewImage implements Target {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            playerView.setDefaultArtwork(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            e.printStackTrace();
            if (getContext() != null) {
                setDefaultArtwork();
            }
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
        }
    }
}
