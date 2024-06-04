package es.zgames.laguerradelasvajillasfree.activities;


import es.zgames.laguerradelasvajillasfree.R;
import es.zgames.utils.AnimationsContainer;
import es.zgames.utils.FramesSequenceAnimation;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class LaGuerradelasVajillasSplashActivity 
	extends LaGuerradelasVajillasActivity 
	implements OnClickListener {
	
	private FramesSequenceAnimation anim = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.splash_layout);
        ImageView splashScreenImageView = (ImageView) findViewById(R.id.ImageView_SplashScreen); 
        splashScreenImageView.setOnClickListener(this);
        anim = AnimationsContainer.getInstance().createSplashAnim(splashScreenImageView); 
        anim.start(); 
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		anim.stop();
		startActivity(new Intent(this, LaGuerradelasVajillasMainActivity.class));
	}

}
