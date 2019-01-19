package com.johnny.john.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.johnny.john.bakingapp.R;
import com.johnny.john.bakingapp.model.Steps;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullStepsFragment extends Fragment {


    private static final String CURRENT_VIDEO_PROGRESS = "CVP";
    private List<Steps> mSteps;
    private int position;
    public static final String TEXT_ID_LIST = "text_ids";
    public static final String LIST_INDEX = "list_index";
    @BindView(R.id.step_next_instruction)
    Button nextButton;
    @BindView(R.id.step_previous_instruction)
    Button previousButton;
    @BindView(R.id.short_description_tv)
    TextView shortDescriptionView;
    @BindView(R.id.description_tv)
    TextView descriptionView;
    @BindView(R.id.video_player_view)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.description_linear_layout_view)
    LinearLayout mLinearLayoutDescription;


    private SimpleExoPlayer mExoPlayer;


    private static final String TAG = "MyActivity";
    private String CURRENT_WINDOW = "extra_current_window";
    private String WHEN_IS_READY = "extra_when_is_ready";
    private boolean twoPane;
    private String videoState;
    private String VIDEO_URL_STATE = "extra_video_state";
    private int currentWindowState;
    private boolean playWhenReady;
    private int counterInitiation;
    private int positionCount;
    private int count;
    private long mCurrentProgress;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        // retrieving the steps
        assert args != null;
        if (args.containsKey(LIST_INDEX)) {

            // getting the json str
            String stepsJson = args.getString(LIST_INDEX);

            // specify the type
            Type stepsListType = new TypeToken<List<Steps>>() {
            }.getType();

            // convert back to List
            this.mSteps = new Gson().fromJson(stepsJson, stepsListType);
        }


    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {

        currentState.putString(VIDEO_URL_STATE, videoState);

        currentState.putInt(CURRENT_WINDOW, currentWindowState);
        currentState.putBoolean(WHEN_IS_READY, playWhenReady);
        currentState.putLong(CURRENT_VIDEO_PROGRESS, mCurrentProgress);

        super.onSaveInstanceState(currentState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_step_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            videoState = savedInstanceState.getString(VIDEO_URL_STATE);
            currentWindowState = savedInstanceState.getInt(CURRENT_WINDOW);
            playWhenReady = savedInstanceState.getBoolean(WHEN_IS_READY);
            counter(position, mSteps);
            mCurrentProgress = savedInstanceState.getLong(CURRENT_VIDEO_PROGRESS);
            mExoPlayer.seekTo(mCurrentProgress);


        } else {
            playWhenReady = true;
            currentWindowState = 0;
            counter(position, mSteps);

        }

        if (twoPane) {
            LargerScreens();
        } else {
            SmallerScreens();
        }

        count = mSteps.size() - 1;
        positionCount = position;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextButton();
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackButton();
            }
        });

    }

    private void NextButton() {

        if (positionCount != count) {

            positionCount++;
            onDestroyView();
            counter(positionCount, mSteps);
            setPosition(positionCount);
            hideVideo();
            counterInitiation = 0;


        } else {

            Toast.makeText(getActivity(), "You are in the last step", Toast.LENGTH_SHORT).show();
        }

    }

    private void BackButton() {

        if (positionCount != 0) {
            positionCount--;
            onDestroyView();
            counter(positionCount, mSteps);
            setPosition(positionCount);
            hideVideo();
            counterInitiation = 0;
        } else {
            Toast.makeText(getActivity(), "You are already in the first step", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideVideo() {
        if (mSteps.get(position).getVideoURL().equals("") && mSteps.get(position).getThumbnailURL().equals("")) {
            mExoPlayerView.setVisibility(View.GONE);
            shortDescriptionView.setPadding(0, 350, 0, 0);
        } else {
            shortDescriptionView.setPadding(0, 0, 0, 0);
            mExoPlayerView.setVisibility(View.VISIBLE);
            if (mExoPlayer != null) {
                mExoPlayer.seekTo(0);
            }
        }
    }

    private void SmallerScreens() {
        mExoPlayerView.getLayoutParams().height = 4 * 170;
        mExoPlayerView.requestLayout();
        mLinearLayoutDescription.getLayoutParams().height = 500;
        mLinearLayoutDescription.requestLayout();
        nextButton.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.VISIBLE);
    }

    private void LargerScreens() {
        mExoPlayerView.getLayoutParams().height = 4 * 170;
        mExoPlayerView.requestLayout();
        mLinearLayoutDescription.getLayoutParams().height = 400;
        mLinearLayoutDescription.requestLayout();
        nextButton.setVisibility(View.GONE);
        previousButton.setVisibility(View.GONE);
    }

    private void counter(int positionCount, List<Steps> recipe1) {

        final Steps currentItem = recipe1.get(positionCount);
        String description = currentItem.getDescription();
        String shortDescription = currentItem.getShortDescription();
        String videoURL = currentItem.getVideoURL();
        String videoURL2 = currentItem.getThumbnailURL();
        descriptionView.setText(description);
        shortDescriptionView.setText(shortDescription);
        mediaPlayerProcessor(videoURL, videoURL2);

    }

    private void mediaPlayerProcessor(String video, String video2) {

        if (video.equals("")) {
            videoState = video2;
            initializePlayer(videoState);

        } else {
            videoState = video;
            initializePlayer(videoState);

        }

    }

    public void setStep(List<Steps> recipe1) {
        mSteps = recipe1;

    }

    public void setPosition(int position) {
        this.position = position;

    }


    public static FullStepsFragment newInstanceDetail(List<Steps> steps) {

        FullStepsFragment fragmentDetail = new FullStepsFragment();
        Bundle args = new Bundle();
        String stepsDetail = new Gson().toJson(steps);
        args.putString(LIST_INDEX, stepsDetail);

        fragmentDetail.setArguments(args);

        return fragmentDetail;
    }


    private void initializePlayer(String videoState) {


        if (mExoPlayer == null) {


            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mExoPlayerView.setUseController(true);
            mExoPlayerView.requestFocus();
            mExoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(playWhenReady);

            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    Objects.requireNonNull(getContext()),
                    Util.getUserAgent(getContext(), "BakingApp"),
                    defaultBandwidthMeter);

            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource videoSource = new ExtractorMediaSource(
                    Uri.parse(videoState),
                    dataSourceFactory,
                    extractorsFactory,
                    null,
                    null);


            boolean startingPosition = currentWindowState != C.INDEX_UNSET;
            if (startingPosition) {
                mExoPlayer.seekTo(currentWindowState, mCurrentProgress);
            }

            mExoPlayer.prepare(videoSource);


        }

    }

    public void setTwoPane(boolean mTwoPane) {
        this.twoPane = mTwoPane;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        Log.e(TAG, "it is onDestroyView " + counterInitiation);


    }

    @Override
    public void onStart() {
        super.onStart();


        Log.e(TAG, "it is onStart ");
        initializePlayer(videoState);


    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(TAG, "it is onResume");


    }

    @Override
    public void onPause() {
        super.onPause();


        Log.e(TAG, "it is onPause");
        releasePlayer();

    }

    @Override
    public void onStop() {
        super.onStop();

        Log.e(TAG, "it is onStop");
        releasePlayer();


    }

    public void releasePlayer() {

        if (mExoPlayer != null) {
            currentWindowState = mExoPlayer.getCurrentWindowIndex();
            mCurrentProgress = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;

        }
    }

}
