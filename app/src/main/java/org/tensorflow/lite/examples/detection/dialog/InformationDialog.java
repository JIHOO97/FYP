package org.tensorflow.lite.examples.detection.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.tensorflow.lite.examples.detection.R;

public class InformationDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    public InformationDialog(Activity mActivity) {
        activity = mActivity;
    }

    public void startLoadingDialog(String fruitName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View informationView = inflater.inflate(R.layout.information_layout, null);
        builder.setView(informationView);
        builder.setCancelable(false);

        alertDialog = builder.create();

        informationView.post(new Runnable() {

            @Override
            public void run() {

                Window dialogWindow = alertDialog.getWindow();

                // Make the dialog possible to be outside touch
                dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
                dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                informationView.invalidate();
            }
        });

        TextView informationUnripeFruitName = (TextView) informationView.findViewById(R.id.information_unripe_fruit_name);
        TextView informationUnripeContent = (TextView) informationView.findViewById(R.id.information_unripe_content);
        TextView informationGoodFruitName = (TextView) informationView.findViewById(R.id.information_good_fruit_name);
        TextView informationGoodContent = (TextView) informationView.findViewById(R.id.information_good_content);

        String unripeFruitName, unripeContent, goodFruitName, goodContent;

        switch(fruitName) {
            case "Apple":
                unripeFruitName = "Unripe apple may be";
                unripeContent = "- Toxic \n- Bad for pancrease \n- Sour";
                goodFruitName = "How are apples good for your health?";
                goodContent = "- Helps with weight loss \n- Lowers risk of heart disease \n- Lowers risk of diabetes";

                informationUnripeFruitName.setText(unripeFruitName);
                informationUnripeContent.setText(unripeContent);
                informationGoodFruitName.setText(goodFruitName);
                informationGoodContent.setText(goodContent);

                break;
            case "Orange":
                break;
            case "Peach":
                break;
            case "Tomato":
                break;
            case "Mango":
                break;
        }

        alertDialog.show();
    }

    public void dismissDialog() {
        alertDialog.dismiss();
    }
}
