package org.tensorflow.lite.examples.detection.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.tensorflow.lite.examples.detection.DetectorActivity;
import org.tensorflow.lite.examples.detection.R;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView realTimeImage = getView().findViewById(R.id.real_time);
        realTimeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetectorActivity();
            }
        });
    }

    private void goToDetectorActivity() {
        Intent intent = new Intent(getActivity(), DetectorActivity.class);
        startActivity(intent);
    }
}
