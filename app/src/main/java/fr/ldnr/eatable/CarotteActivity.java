package fr.ldnr.eatable;

import static fr.ldnr.eatable.AquariumActivity.CLE_RETOUR_CAROTTE;
import static fr.ldnr.eatable.AquariumActivity.CLE_RETOUR_POPCORN;

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

public class CarotteActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CarotteView(this));
        Log.i("CarotteActivity", "activité de repas sain créée");
        Toast.makeText(this, "Miam miam", Toast.LENGTH_LONG).show();
        long duree = getIntent().getLongExtra("temps", 0);
        if (duree > 500) {
            String secondes = getResources().getQuantityString(R.plurals.temps_secondes,
                    (int)duree/1000);
            String text = getString(R.string.carotte_warning, (int)duree/1000, secondes);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            Intent resultat = new Intent();
            resultat.putExtra(CLE_RETOUR_CAROTTE, true);
            setResult(0, resultat);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getActionMasked()==MotionEvent.ACTION_DOWN) {
            Intent i = new Intent(this, JardinActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }
        return true;
    }


    public static class CarotteView extends View {
        public CarotteView(Context context) {
            super(context);
        }

            @Override
            protected void onDraw(@NonNull Canvas canvas) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.carottes);
                Bitmap b = Bitmap.createScaledBitmap(bmp, 1000, 1500, false);
                bmp.recycle();
                canvas.drawBitmap(b, 0, 0, null);
            }
    }
}
