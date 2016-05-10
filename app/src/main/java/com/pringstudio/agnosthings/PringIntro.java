package com.pringstudio.agnosthings;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by sucipto on 5/10/16.
 */
public class PringIntro extends AppIntro2 {
    // Please DO NOT override onCreate. Use init.

    @Override
    public void init(Bundle savedInstanceState) {


        addSlide(AppIntroFragment.newInstance(
                "Judul",
                "Deskripsi",
                R.drawable.ic_cached_grey_200_36dp,
                Color.parseColor("#8bc34a"))
        );

        addSlide(AppIntroFragment.newInstance(
                "Judul2",
                "Deskripsi2",
                R.drawable.ic_cached_grey_200_36dp,
                Color.parseColor("#ff9800"))
        );

        addSlide(AppIntroFragment.newInstance(
                "Judul2",
                "Deskripsi2",
                R.drawable.ic_cached_grey_200_36dp,
                Color.parseColor("#ff5722"))
        );

        addSlide(AppIntroFragment.newInstance(
                "Judul2",
                "Deskripsi2",
                R.drawable.ic_cached_grey_200_36dp,
                Color.parseColor("#03a9f4"))
        );

        addSlide(AppIntroFragment.newInstance(
                "Judul2",
                "Deskripsi2",
                R.drawable.ic_cached_grey_200_36dp,
                Color.parseColor("#7e57c2"))
        );


        setProgressButtonEnabled(true);


        setDepthAnimation();
    }



    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        this.finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }
}
