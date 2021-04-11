package org.tensorflow.lite.examples.detection.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.StartingActivity;
import org.tensorflow.lite.examples.detection.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class InformationFragment extends Fragment {

    private List<String> titlesList;
    private List<String> detailsList;
    ViewPager2 viewPager2;
    ImageView imageView;

    String[] titles = {"Welcome!", "Center!", "Focus on the objects", "Notice!"};
    String[] details = {"Your presence in DES01 is our honor.\nDES01 helps you to identify 5 kinds of\nfruits about their ripeness",
                        "Take centered, well-lit photos of leaf, flowers, fruits!",
                        "Make sure that the focus is on the\nphotographed organ and not on the\nbackground of the image",
                        "Don't snap plants that are too far of frame or\nplant not belonging to the desired species\n(pot,ruler,etc.)"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        titlesList = new ArrayList<>();
        detailsList = new ArrayList<>();

        addToList(titles, details);

        viewPager2 = getView().findViewById(R.id.view_pager2);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(titlesList, detailsList);
        viewPager2.setAdapter(viewPagerAdapter);
        CircleIndicator3 indicator3 = getView().findViewById(R.id.indicator);
        indicator3.setViewPager(viewPager2);
    }

    private void addToList(String[] titles, String[] descriptions) {
        for(int i = 0; i < titles.length; ++i) {
            titlesList.add(titles[i]);
            detailsList.add(descriptions[i]);
        }
    }

}
