package es.zgames.laguerradelasvajillasfree.activities;


import java.util.Vector;

import es.zgames.laguerradelasvajillasfree.R;
import es.zgames.textadventures.engine.Adventure;
import es.zgames.textadventures.engine.AdventureLocation;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class LaGuerradelasVajillasDeadLocationActivity extends LaGuerradelasVajillasActivity {
	static final int DIALOG_CLOSE_ID = 1;
 	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        
     
       	setContentView(R.layout.spanish_dead_layout);
        
        Adventure<?, ?> part1_adventure = Adventure.getInstance();
        AdventureLocation currentLocation = part1_adventure.getCurrentLocation();
        Vector<String> messages = currentLocation.getMessages();

        int resId = getResources().getIdentifier(currentLocation.getImage(), "drawable", getPackageName());
        ImageView location_ImageView= (ImageView) findViewById(R.id.location_ImageView);
        location_ImageView.setImageResource(resId);
		
		
		
		TextView location_TextView = (TextView) findViewById(R.id.location_TextView);
		location_TextView.setText(currentLocation.getTitle());
        
		TextView actionsValue_TextView = (TextView) findViewById(R.id.actionsValue_TextView);
		actionsValue_TextView.setText(String.valueOf(part1_adventure.getActionsCounter()));
		
        TextView deadLine1_TextView = (TextView) findViewById(R.id.deadLine1_TextView);
        deadLine1_TextView.setText(messages.get(0));
        TextView deadLine2_TextView = (TextView) findViewById(R.id.deadLine2_TextView);
        deadLine2_TextView.setText(messages.get(1));
		TextView deadLine3_TextView = (TextView) findViewById(R.id.deadLine3_TextView);
		deadLine3_TextView.setText(messages.get(2));
		TextView deadLine4_TextView = (TextView) findViewById(R.id.deadLine4_TextView);
		deadLine4_TextView.setText("");

        
        startAnimating();
        
    }
    private void startAnimating() {
		 Animation dead_line1_move_to= AnimationUtils.loadAnimation(this, R.anim.dead_line1_move_to);
		 Animation dead_line2_move_to= AnimationUtils.loadAnimation(this, R.anim.dead_line2_move_to);
		 Animation dead_line3_move_to= AnimationUtils.loadAnimation(this, R.anim.dead_line3_move_to);
		 Animation dead_line4_move_to= AnimationUtils.loadAnimation(this, R.anim.dead_line4_move_to);
		 
		 TextView deadLine1_TextView = (TextView) findViewById(R.id.deadLine1_TextView);		 
		 TextView deadLine2_TextView = (TextView) findViewById(R.id.deadLine2_TextView);
		 TextView deadLine3_TextView = (TextView) findViewById(R.id.deadLine3_TextView);
		 TextView deadLine4_TextView = (TextView) findViewById(R.id.deadLine4_TextView);
		 deadLine1_TextView.setAnimation(dead_line1_move_to);
		 deadLine2_TextView.setAnimation(dead_line2_move_to);
		 deadLine3_TextView.setAnimation(dead_line3_move_to);
		 deadLine4_TextView.setAnimation(dead_line4_move_to);
		 
		 dead_line4_move_to.setAnimationListener(new AnimationListener() {
			 	@SuppressWarnings("deprecation")
				public void onAnimationEnd(Animation animation) {
	                // The animation has ended, transition to the Main Menu screen
	            	 showDialog(DIALOG_CLOSE_ID);	
	            }

	            public void onAnimationRepeat(Animation animation) {
	            }

	            public void onAnimationStart(Animation animation) {
	            }
	        });
	 }

    protected Dialog onCreateDialog(int id) {
		 final Dialog dialog = new Dialog(this);
		 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		 dialog.setContentView(R.layout.close_custom_dialog_layout);
		 //dialog.setTitle("Atencion!!");
		 dialog.setCancelable(true);
		 
		 TextView dialogLine1_TextView = (TextView) dialog.findViewById(R.id.dialogLine1_TextView);		 
		 TextView dialogLine2_TextView = (TextView) dialog.findViewById(R.id.dialogLine2_TextView);

		 
		 dialogLine1_TextView.setText(R.string.str_you_are_dead);
		 dialogLine2_TextView.setText(R.string.str_new_game);
		  
		 ImageButton cancelImageButton = (ImageButton) dialog.findViewById(R.id.cancel_ImageButton);
		 cancelImageButton.setOnClickListener(new OnClickListener() {
			 public void onClick(View v) {
				 startActivity(new Intent(LaGuerradelasVajillasDeadLocationActivity.this, LaGuerradelasVajillasMainActivity.class));
		     }
		  });

		 ImageButton okImageButton = (ImageButton) dialog.findViewById(R.id.ok_ImageButton);
		 okImageButton .setOnClickListener(new OnClickListener() {
			 public void onClick(View v) {
				  startActivity(new Intent(LaGuerradelasVajillasDeadLocationActivity.this, LaGuerradelasVajillasSelectAdventurePartActivity.class));
			    	
		     }
		  });

		 
		  return dialog;
		}

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
}
