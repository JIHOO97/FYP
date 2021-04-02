package org.tensorflow.lite.examples.detection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.detection.dialog.LoadingDialog;
import org.tensorflow.lite.examples.detection.env.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RipenessActivity extends AppCompatActivity {
    private static final int INPUT_SIZE = 300;
    private static final int MAX_TIMEOUT_DURATION = 120000;

    private ImageView screenImage;
    private RequestQueue mQueue;
    private LoadingDialog loadingDialog;
    private Bitmap ripenessBitmap;
    private float[] croppedBoxRatios;
    private Paint paint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ripeness_layout);

        loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();

        String fruitName = getIntent().getStringExtra("fruitName");
        croppedBoxRatios = getIntent().getFloatArrayExtra("croppedBoxRatios");

        screenImage = (ImageView) findViewById(R.id.screen_imageview);
        String screenShotFileName = "screenshot.jpg";
        Bitmap screenShotBitmap = loadFruitBitmap(screenShotFileName);
        ripenessBitmap = screenShotBitmap.copy(Bitmap.Config.ARGB_8888, true); // to put it into the canvas
        screenImage.setImageBitmap(screenShotBitmap);

        // get information about the cropped image
        String croppedFruitFileName = "croppedFruit.jpg";
        Bitmap croppedFruitBitmap = loadFruitBitmap(croppedFruitFileName);
        int[][][][] fruitBitmapToArray = getFruitArray(croppedFruitBitmap);
        JSONObject fruitArrayToObject = getFruitJSONObject(fruitBitmapToArray);

        // send request to server
        mQueue = Volley.newRequestQueue(this);
        getRipeness(fruitArrayToObject);

        // set the paint to draw text
        paint = new Paint();
        paint.setColor(Color.BLUE); // Text Color
        paint.setTextSize(12); // Text Size
    }

    private Bitmap loadFruitBitmap(String filename) {
        final String root = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "tensorflow";
        final File myDir = new File(root);
        final File file = new File(myDir, filename);
        String filePath = file.getPath();
        Bitmap screenBitmap = BitmapFactory.decodeFile(filePath);

        return screenBitmap;
    }

    private int[][][][] getFruitArray(Bitmap croppedFruitBitmap) {
        int[] croppedFruitPixels = new int[300 * 300];
        croppedFruitBitmap = Utils.processBitmap(croppedFruitBitmap, 300);
        croppedFruitBitmap.getPixels(croppedFruitPixels, 0, croppedFruitBitmap.getWidth(), 0, 0, croppedFruitBitmap.getWidth(), croppedFruitBitmap.getHeight());

        int pixel = 0;
        int[][][][] ripenessInputArray = new int[1][300][300][3];

        for (int i = 0; i < INPUT_SIZE; ++i) {
            for (int j = 0; j < INPUT_SIZE; ++j) {
                int index = 0;
                final int val = croppedFruitPixels[pixel++];
                ripenessInputArray[0][i][j][index++] = (((val >> 16) & 0xFF));
                ripenessInputArray[0][i][j][index++] = (((val >> 8) & 0xFF));
                ripenessInputArray[0][i][j][index++] = ((val & 0xFF));
            }
        }

        return ripenessInputArray;
    }

    private JSONObject getFruitJSONObject(int[][][][] ripenessInputArray) {
        JSONObject object = new JSONObject();
        try {
            int index = 0;
            for(int i = 0; i < 300; i++)
            {
                for(int j = 0; j < 300; j++)
                {
                    for(int r = 0; r < 3; r++)
                    {
                        object.put("user" + index++, ripenessInputArray[0][i][j][r]);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    private void getRipeness(JSONObject jsonFruitObject) {
        String url = "http://192.168.55.120:5000";

        Log.d("RipenessServer", "Sending Request...");

        // Request a string response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonFruitObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingDialog.dismissDialog();

                Log.d("RipenessServer", response.toString());

                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("prediction");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String ripeness = null;
                try {
                    ripeness = getRipenessPercentage(jsonArray.get(0).toString());
                    Log.d("RipenessServer", ripeness);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                float leftRatio = croppedBoxRatios[0];
                float topRatio = croppedBoxRatios[1];

                Canvas canvas = new Canvas(ripenessBitmap);
                canvas.drawText("Ripeness is: " + ripeness, leftRatio*300, topRatio*300, paint);
                screenImage.setImageBitmap(ripenessBitmap);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("RipenessServer", "Error: " + error);
            }
        }) {
            @Override       //Send Header
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("content-type", "application/json");

                return params;
            }
        };

        // set the timeout of the request
        request.setRetryPolicy(new DefaultRetryPolicy(
                MAX_TIMEOUT_DURATION,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        mQueue.add(request);
    }

    private String getRipenessPercentage(String jsonArray) {
        String ripePercentage = "";
        for(int i = 1; i < jsonArray.length(); ++i) {
            if(jsonArray.charAt(i) == ',') break;
            ripePercentage += jsonArray.charAt(i);
        }

        float floatRipePercentage = Float.parseFloat(ripePercentage);
        floatRipePercentage = (float) ((float)Math.round(floatRipePercentage * 10000d) / 10000d);
        floatRipePercentage *= 100;

        ripePercentage = String.valueOf(floatRipePercentage);

        return ripePercentage + "%";
    }
}
