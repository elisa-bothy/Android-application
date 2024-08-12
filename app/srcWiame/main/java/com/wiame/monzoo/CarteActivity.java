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

public class CarteActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CarteView(this));
        Log.i("CarteActivity", "activité créée");
        Toast.makeText(this, getString(R.string.carte_bienvenue), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            int largeur = findViewById(android.R.id.content).getWidth();

            Intent i = new Intent(this, AquariumActivity.class);
            startActivity(i);
        }
        return true;
    }

    public class CarteView extends View {

        public CarteView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.carte);
            canvas.drawBitmap(bmp, 0, 0, null);
            // Log donnant une information
        }
    }
}
