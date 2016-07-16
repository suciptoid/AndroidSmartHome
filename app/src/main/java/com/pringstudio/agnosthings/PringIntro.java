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
                "Teknologi Cloud",
                "Akses dan monitor rumah anda dimana saja, kapan saja dengan teknologi cloud",
                R.drawable.splash_access,
                Color.parseColor("#8bc34a"))
        );

        addSlide(AppIntroFragment.newInstance(
                "Monitor Suhu",
                "Monitoring data suhu ruangan rumah anda",
                R.drawable.splash_suhu,
                Color.parseColor("#7e57c2"))
        );

        addSlide(AppIntroFragment.newInstance(
                "Monitoring PDAM",
                "Memonitor pemakaian Air PDAM anda",
                R.drawable.splash_air,
                Color.parseColor("#ff9800"))
        );

        addSlide(AppIntroFragment.newInstance(
                "Saklar Jarak jauh",
                "Kini anda tidak takut lupa mematikan lampu, karena dapat anda kontrol dari SmartPhone anda",
                R.drawable.splash_lampu,
                Color.parseColor("#ff5722"))
        );

        addSlide(AppIntroFragment.newInstance(
                "Monitor Konsumsi Listrik",
                "Memonitor pemakaian pulsa listrik anda",
                R.drawable.splash_listrik,
                Color.parseColor("#03a9f4"))
        );

        addSlide(AppIntroFragment.newInstance(
                "Tak perlu khawatir lagi pakai LPG",
                "Memonitor pemakaian gas LPG yang ada di dapur anda",
                R.drawable.splash_lpg,
                Color.parseColor("#7e57c2"))
        );
/*
        addSlide(AppIntroFragment.newInstance(
                "Notifikasi Pintar",
                "Pemberitahuan yang membuat anda selalu waspada",
                R.drawable.splash_notif,
                Color.parseColor("#7e57c2"))
        );

*/
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
