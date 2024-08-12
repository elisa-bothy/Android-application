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

public class JardinActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new JardinView(this));
        Log.i("JardinActivity", "activité créée");
        Toast.makeText(this, getString(R.string.jardin_welcome), Toast.LENGTH_LONG).show();
         }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getActionMasked()==MotionEvent.ACTION_DOWN) {
            int largeur = findViewById(android.R.id.content).getWidth();
            if((int)event.getX()< largeur/2 ) {
                Intent i = new Intent(this, AquariumActivity.class);
                startActivity(i);
            }else{
                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.oceanografic.org/tickets/"));
                try {
                    startActivity(i);
                }catch (ActivityNotFoundException ex){
                    Log.i("JardinActivity", "Pas de navigateur ?", ex);
                    Toast.makeText(
                            this, getString(R.string.jardin_nonavigator),
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        return true;
    }

    public class JardinView extends View{

            public JardinView(Context context) {
                super(context);
            }

            @Override
            protected void onDraw(@NonNull Canvas canvas) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cartejardin2);
                Bitmap b = Bitmap.createScaledBitmap(bmp, 1000, 1500, false);
                bmp.recycle();
                canvas.drawBitmap(b, 0, 0, null);
            }
        }
}
