package es.zgames.laguerradelasvajillasfree.activities;

import es.zgames.laguerradelasvajillasfree.R;
import es.zgames.utils.GlobalInformation;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class LaGuerradelasVajillasAnimatedLocationActivity extends LaGuerradelasVajillasActivity {
	static final int DIALOG_CLOSE_ID = 1;
	private GlobalInformation globalInformation = GlobalInformation.getInstance();
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (  Integer.valueOf(android.os.Build.VERSION.SDK) < 7 //Instead use android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
	            && keyCode == KeyEvent.KEYCODE_BACK
	            && event.getRepeatCount() == 0) {
	        // Take care of calling this method on earlier versions of
	        // the platform where it doesn't exist.
	        onBackPressed();
	    }

	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
	    // This will be called either automatically for you on 2.0
	    // or later, or by the code above on earlier versions of the
	    // platform.
	    return;
	}
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.spanish_animated_layout);
        
        ImageView location_ImageView= (ImageView ) findViewById(R.id.location_ImageView);
        int resId = getResources().getIdentifier(globalInformation.getAnimatedImage(), "drawable", getPackageName());
        location_ImageView.setImageResource(resId);		
        
        TextView deadLine1_TextView = (TextView) findViewById(R.id.animatedLine1_TextView);
        deadLine1_TextView.setText(globalInformation.getAnimatedText1());
        TextView deadLine2_TextView = (TextView) findViewById(R.id.animatedLine2_TextView);
        deadLine2_TextView.setText(globalInformation.getAnimatedText2());
		TextView deadLine3_TextView = (TextView) findViewById(R.id.animatedLine3_TextView);
		deadLine3_TextView.setText(globalInformation.getAnimatedText3());

        
        startAnimating();
        
    }
    private void startAnimating() {
		 Animation dead_line1_move_to= AnimationUtils.loadAnimation(this, R.anim.dead_line1_move_to);
		 Animation dead_line2_move_to= AnimationUtils.loadAnimation(this, R.anim.dead_line2_move_to);
		 Animation dead_line3_move_to= AnimationUtils.loadAnimation(this, R.anim.dead_line3_move_to);
		 Animation dead_line4_move_to= AnimationUtils.loadAnimation(this, R.anim.dead_line4_move_to);
		 
		 TextView deadLine1_TextView = (TextView) findViewById(R.id.animatedLine1_TextView);		 
		 TextView deadLine2_TextView = (TextView) findViewById(R.id.animatedLine2_TextView);
		 TextView deadLine3_TextView = (TextView) findViewById(R.id.animatedLine3_TextView);
		 TextView deadLine4_TextView = (TextView) findViewById(R.id.animatedLine4_TextView);
		 
		 deadLine1_TextView.setAnimation(dead_line1_move_to);
		 deadLine2_TextView.setAnimation(dead_line2_move_to);
		 deadLine3_TextView.setAnimation(dead_line3_move_to);
		 deadLine4_TextView.setAnimation(dead_line4_move_to);
		 
		 dead_line4_move_to.setAnimationListener(new AnimationListener() {
			 	public void onAnimationEnd(Animation animation) {
	                // The animation has ended, transition to the Main Menu screen
			  		  startActivity(new Intent(LaGuerradelasVajillasAnimatedLocationActivity.this, 
			  				  				   LaGuerradelasVajillasPart2LocationActivity.class));
			  			
	            }

	            public void onAnimationRepeat(Animation animation) {
	            }

	            public void onAnimationStart(Animation animation) {
	            }
	        });
	 }

   
    
   
}
