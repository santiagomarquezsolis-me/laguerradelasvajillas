package es.zgames.laguerradelasvajillasfree.activities;

import es.zgames.laguerradelasvajillasfree.R;
import es.zgames.textadventures.engine.AdventureCodes;
import es.zgames.utils.GlobalInformation;
//import es.zgames.utils.GlobalInformation;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LaGuerradelasVajillasIntroAdventureActivity extends LaGuerradelasVajillasActivity {
	//private int photoIndex = 2;
	private GlobalInformation globalInformation = GlobalInformation.getInstance();
	private MediaPlayer mp;
	@Override
	public void onBackPressed() {
	    // This will be called either automatically for you on 2.0
	    // or later, or by the code above on earlier versions of the
	    // platform.
	    return;
	}
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Screen Properties
		 //////////////////////////////////////////////////////////////////////////////////////
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				  				   WindowManager.LayoutParams.FLAG_FULLSCREEN);
		  
		 setContentView(R.layout.spanish_intro_adventure_layout);
		  
	
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Play Behaviour
		 //////////////////////////////////////////////////////////////////////////////////////

		 RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
		 relativeLayout.setOnClickListener(new OnClickListener() {
	    	 public void onClick(View v) {
	    		  startActivity(new Intent(LaGuerradelasVajillasIntroAdventureActivity.this, LaGuerradelasVajillasPart1LocationActivity.class));
	    		  
			}
	    	  
	      });
		
		 startAnimating();
		 
		  mp = MediaPlayer.create(getApplicationContext(), R.raw.music02);
//		     
		     if (globalInformation.getMusic().equals(AdventureCodes.FALSE)){
		    	 mp.stop();
		     } else {	 	
		    	 mp.setLooping(true);
		    	 //mp.start();
		     }
		 
	 }
	 
	 @Override
	 public void onPause(){
		 super.onPause();
		 mp.pause();
	 }
	 
	 @Override
	 public void onResume(){
		 super.onResume();
		 if (globalInformation.getMusic().equals(AdventureCodes.FALSE)){
	    	 mp.stop();
		 } else {
			 mp.start();
		 }
		 
	 }

	private void startAnimating() {
		Animation move_from_down = AnimationUtils.loadAnimation(this, R.anim.move_from_down);
		
		TextView textView0 = (TextView) findViewById(R.id.textView0);
		TextView textView1 = (TextView) findViewById(R.id.textView1);
		TextView textView1111 = (TextView) findViewById(R.id.textView1111);
		TextView textView11111 = (TextView) findViewById(R.id.textView11111);
		TextView textView2 = (TextView) findViewById(R.id.textView2);
		TextView textView3 = (TextView) findViewById(R.id.textView3);
		TextView textView4 = (TextView) findViewById(R.id.textView4);
		TextView textView5 = (TextView) findViewById(R.id.textView5);
		TextView textView6 = (TextView) findViewById(R.id.textView6);
		TextView textView7 = (TextView) findViewById(R.id.textView7);
		TextView textView8 = (TextView) findViewById(R.id.textView8);
		TextView textView9 = (TextView) findViewById(R.id.textView9);
		TextView textView10 = (TextView) findViewById(R.id.textView10);
		TextView textView11 = (TextView) findViewById(R.id.textView11);
		TextView textView12 = (TextView) findViewById(R.id.textView12);
		TextView textView13 = (TextView) findViewById(R.id.textView13);
		TextView textView14 = (TextView) findViewById(R.id.textView14);
		TextView textView15 = (TextView) findViewById(R.id.textView15);
		TextView textView16 = (TextView) findViewById(R.id.textView16);
		TextView textView17 = (TextView) findViewById(R.id.textView17);
		TextView textView18 = (TextView) findViewById(R.id.textView18);
		TextView textView19 = (TextView) findViewById(R.id.textView19);
		TextView textView20 = (TextView) findViewById(R.id.textView20);
		TextView textView21 = (TextView) findViewById(R.id.textView21);
		TextView textView22 = (TextView) findViewById(R.id.textView22);
		TextView textView23 = (TextView) findViewById(R.id.textView23);
		TextView textView24 = (TextView) findViewById(R.id.textView24);
		TextView textView25 = (TextView) findViewById(R.id.textView25);
		TextView textView26 = (TextView) findViewById(R.id.textView26);
		TextView textView27 = (TextView) findViewById(R.id.textView27);
		TextView textView28 = (TextView) findViewById(R.id.textView28);
		TextView textView29 = (TextView) findViewById(R.id.textView29);
		TextView textView30 = (TextView) findViewById(R.id.textView30);
		TextView textView31 = (TextView) findViewById(R.id.textView31);
		TextView textView32 = (TextView) findViewById(R.id.textView32);
		TextView textView33 = (TextView) findViewById(R.id.textView33);
		TextView textView34 = (TextView) findViewById(R.id.textView34);
		TextView textView35 = (TextView) findViewById(R.id.textView35);
		TextView textView36 = (TextView) findViewById(R.id.textView36);
		TextView textView37 = (TextView) findViewById(R.id.textView37);
		
		textView0.startAnimation(move_from_down);
		textView1.startAnimation(move_from_down);
		textView1111.startAnimation(move_from_down);
		textView11111.startAnimation(move_from_down);
		textView2.startAnimation(move_from_down);
		textView3.startAnimation(move_from_down);
		textView4.startAnimation(move_from_down);
		textView5.startAnimation(move_from_down);
		textView6.startAnimation(move_from_down);
		textView7.startAnimation(move_from_down);
		textView8.startAnimation(move_from_down);
		textView9.startAnimation(move_from_down);
		textView10.startAnimation(move_from_down);
		textView11.startAnimation(move_from_down);
		textView12.startAnimation(move_from_down);
		textView13.startAnimation(move_from_down);
		textView14.startAnimation(move_from_down);
		textView15.startAnimation(move_from_down);
		textView16.startAnimation(move_from_down);
		textView17.startAnimation(move_from_down);
		textView18.startAnimation(move_from_down);
		textView19.startAnimation(move_from_down);
		textView20.startAnimation(move_from_down);
		textView21.startAnimation(move_from_down);
		textView22.startAnimation(move_from_down);
		textView23.startAnimation(move_from_down);
		textView24.startAnimation(move_from_down);
		textView25.startAnimation(move_from_down);
		textView26.startAnimation(move_from_down);
		textView27.startAnimation(move_from_down);
		textView28.startAnimation(move_from_down);
		textView29.startAnimation(move_from_down);
		textView30.startAnimation(move_from_down);
		textView31.startAnimation(move_from_down);
		textView32.startAnimation(move_from_down);
		textView33.startAnimation(move_from_down);
		textView34.startAnimation(move_from_down);
		textView35.startAnimation(move_from_down);
		textView36.startAnimation(move_from_down);
		textView37.startAnimation(move_from_down);
	 
		
}
	 
	 
}
