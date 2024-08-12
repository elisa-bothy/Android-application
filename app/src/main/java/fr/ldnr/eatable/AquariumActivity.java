package fr.ldnr.eatable;

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

    public static String CLE_RETOUR_CAROTTE="messageCarotte";
    public static String CLE_RETOUR_POPCORN="messagePopcorn";
    private long debut, fin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new AquariumView(this));
        Log.i("AquariumActivity", "activité d'eau créée");
        Toast.makeText(this, "Bienvenue dans l'eau", Toast.LENGTH_LONG).show();
        debut = System.currentTimeMillis();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getActionMasked()==MotionEvent.ACTION_DOWN) {
            fin = System.currentTimeMillis();
            long duree = fin - debut;

            int largeur = findViewById(android.R.id.content).getWidth();
            if((int)event.getX()< largeur/2 ) {
                Intent i = new Intent(this, PopcornActivity.class);
                i.putExtra("temps", duree);
                startActivityForResult(i, 0);
            }else{
                Intent i = new Intent(this, CarotteActivity.class);
                i.putExtra("temps", duree);
                startActivityForResult(i, 0);
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null && data.getBooleanExtra(CLE_RETOUR_CAROTTE, false))
            Log.i("AquariumActivity", "Message carotte affiché");
        else
            Log.i("AquariumActivity", "Message carotte PAS affiché");
        if (data!=null && data.getBooleanExtra(CLE_RETOUR_POPCORN, false))
            Log.i("AquariumActivity", "Message popcorn affiché");
        else
            Log.i("AquariumActivity", "Message popcorn PAS affiché");
    }

    public static class AquariumView extends View {
        public AquariumView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.aquarium);
            Bitmap b = Bitmap.createScaledBitmap(bmp, 1000, 1500, false);
            bmp.recycle();
            canvas.drawBitmap(b, 0, 0, null);
        }
    }
}
