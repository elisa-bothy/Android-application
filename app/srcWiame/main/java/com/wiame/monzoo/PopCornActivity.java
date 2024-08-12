package com.wiame.monzoo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PopCornActivity extends Activity {

    public static String message = "activité créée";
    private long debut, fin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new com.wiame.monzoo.PopCornActivity.PopCornView(this));
        Log.i("PopCornActivity", "activité créée");
        // Milisecondes depuis le 1er janvier 1970
        long duree = getIntent().getLongExtra("temps", 0);
        if(duree > 500) {
            String secondes = getResources().getQuantityString(R.plurals.popcorn_secondes, (int)duree/100);
            String texte = getString( R.string.popcorn_avertissement,
                    (int) duree / 1000,
                    secondes );
            Toast.makeText(this, texte, Toast.LENGTH_LONG).show();
            Intent resultat = new Intent();
            resultat.putExtra("messageAffiche", true);
            setResult(0, resultat);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            Intent i = new Intent(this, CarteActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
            return true;
    }

    public class PopCornView extends View {

        public PopCornView(Context context) {
            super(context);
        }

        /**
         * Implement this to do your drawing.
         *
         * @param canvas the canvas on which the background will be drawn
         */
        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.popcorn);
            Bitmap b = Bitmap.createScaledBitmap(bmp, 750, 1000, false);
            bmp.recycle();
            canvas.drawBitmap(b, 0, 0, null);
        }
    }
}
