package com.wiame.monzoo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AquariumActivity extends Activity {
    public final static String CLE_RETOUR = "messageAffiche";
    public static String message = "activité créée";
    private long debut, fin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new com.wiame.monzoo.AquariumActivity.AquariumView(this));
        Log.i("AquariumActivity", "activité créée");
        // Milisecondes depuis le 1er janvier 1970
        debut = System.currentTimeMillis();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            fin = System.currentTimeMillis();
            // si int le 19 Janvier 2038 l'appli crachera car on sera a 2^32
            long duree = fin -debut;

            Intent i = new Intent(this, PopCornActivity.class);
            i.putExtra("temps", duree);
            startActivityForResult(i, 0);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && data.getBooleanExtra(CLE_RETOUR, false)){
            Log.i("AquariumActivity", "Message popcorn affiché");
        } else
            Log.i("AquariumActivity", "Message popcorn pas affiché");
    }


    public class AquariumView extends View {

            public AquariumView(Context context) {
                super(context);
            }

            @Override
            protected void onDraw(@NonNull Canvas canvas) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.aquarium);
                Bitmap b = Bitmap.createScaledBitmap(bmp, 750, 1000, false);
                bmp.recycle();
                canvas.drawBitmap(b, 0, 0, null);
                // Log donnant une information
            }
        }

}

