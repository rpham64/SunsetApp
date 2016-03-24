package com.bignerdranch.android.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Rudolf on 3/23/2016.
 */
public class SunsetFragment extends Fragment {

    private static final String TAG = "SunsetFragment";

    private View mSceneView;
    private View mSunView;
    private View mSunReflectionView;
    private View mSkyView;
    private View mSeaView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSunReflectionView = view.findViewById(R.id.sun_reflection);
        mSkyView = view.findViewById(R.id.sky);
        mSeaView = view.findViewById(R.id.sea);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        mSceneView.setOnClickListener(new View.OnClickListener() {

            boolean isSunset = true;

            @Override
            public void onClick(View v) {

                if (isSunset) {
                    sunsetAnimation();
                    isSunset = false;
                } else {
                    sunriseAnimation();
                    isSunset = true;
                }

            }
        });

        return view;
    }

    private void sunsetAnimation() {

        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        float sunReflectionYStart = mSunReflectionView.getTop();
        float sunReflectionYEnd = -mSeaView.getHeight();

        /** Sun Animator */
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);

        // Speed up sun animation
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        /** Sun Reflection Animator */
        ObjectAnimator reflectAnimator = ObjectAnimator
                .ofFloat(mSunReflectionView, "y", sunReflectionYStart, sunReflectionYEnd)
                .setDuration(4000);

        reflectAnimator.setInterpolator(new AccelerateInterpolator());

        /** Sunset Sky Animator */
        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(3000);

        // Improves ObjectAnimator's color transition
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        /** Night Sky Animator */
        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        // Sync all animators
        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet
                .play(heightAnimator)
                .with(reflectAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);

        animatorSet.start();
    }

    private void sunriseAnimation() {

        float sunYStart = mSkyView.getHeight();
        float sunYEnd = mSunView.getTop();

        float sunReflectionYStart = -mSeaView.getHeight();
        float sunReflectionYEnd = mSunReflectionView.getTop();

        /** Sun Animator */
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);

        // Speed up sun animation
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        /** Sun Reflection Animator */
        ObjectAnimator reflectAnimator = ObjectAnimator
                .ofFloat(mSunReflectionView, "y", sunReflectionYStart, sunReflectionYEnd)
                .setDuration(3000);

        reflectAnimator.setInterpolator(new AccelerateInterpolator());

        /** Sunset Sky Animator */
        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
                .setDuration(1500);

        // Improves ObjectAnimator's color transition
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        /** Night Sky Animator */
        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mNightSkyColor, mSunsetSkyColor)
                .setDuration(3000);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        // Sync all animators
        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet
                .play(nightSkyAnimator)
                .before(sunsetSkyAnimator)
                .with(heightAnimator)
                .with(reflectAnimator);

        animatorSet.start();
    }
}
