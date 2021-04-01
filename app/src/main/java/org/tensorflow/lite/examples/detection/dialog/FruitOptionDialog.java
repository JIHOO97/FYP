package org.tensorflow.lite.examples.detection.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.tensorflow.lite.examples.detection.R;

public class FruitOptionDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String[] fruits = {"Apple",
                "Peach",
                "Pomegranate",
                "Lemon",
                "Mango",
                "Orange",
                "Pear",
                "Strawberry",
                "Tomato",
                "Watermelon"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Information")
                .setMessage("This is a Dialog")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        final ArrayAdapter<String> fruitAdapter = new ArrayAdapter<String>(getActivity(), R.layout.listview);
        for(String fruit: fruits) {
            fruitAdapter.add(fruit);
        }
        builder.setAdapter(fruitAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("Dialog", "You clicked " + fruits[i]);
            }
        });


        return builder.create();
    }
}
