package es.zgames.laguerradelasvajillasfree.activities;


import es.zgames.laguerradelasvajillasfree.R;
import es.zgames.textadventures.engine.AdventureCodes;
import es.zgames.utils.GlobalInformation;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;


public class LaGuerradelasVajillasMainActivity extends LaGuerradelasVajillasActivity {

	private MediaPlayer mp;
	private  GlobalInformation globalInformation = GlobalInformation.getInstance();
	
//	private MA air; //Declare here
	
	
	@Override
	    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Screen Properties
		 //////////////////////////////////////////////////////////////////////////////////////
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				  				   WindowManager.LayoutParams.FLAG_FULLSCREEN);
		  
		 

		 setContentView(R.layout.spanish_main_layout);
			 
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Play Behaviour
		 //////////////////////////////////////////////////////////////////////////////////////

		 ImageButton play_ImageButton = (ImageButton) findViewById(R.id.play_ImageButton);
	     play_ImageButton.setOnClickListener(new OnClickListener() {
	    	 public void onClick(View v) {
	    		
	    		  startActivity(new Intent(LaGuerradelasVajillasMainActivity.this, LaGuerradelasVajillasSelectAdventurePartActivity.class));
	    		
	    	 }
	    	  
	      });
		
	     
		//////////////////////////////////////////////////////////////////////////////////////
		// Options Behaviour
		//////////////////////////////////////////////////////////////////////////////////////
		
		ImageButton options_ImageButton = (ImageButton) findViewById(R.id.options_ImageButton);
		options_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			
				startActivity(new Intent(LaGuerradelasVajillasMainActivity.this, LaGuerradelasVajillasOptionsActivity.class));
				
			}		
		});
	   
		ImageButton facebook_ImageButton = (ImageButton) findViewById(R.id.facebook_ImageButton );
		facebook_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				openBrowser("www.facebook.com");
			}		
		});

		ImageButton twitter_ImageButton = (ImageButton) findViewById(R.id.twitter_ImageButton );
		twitter_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				openBrowser("www.twitter.com");
			}		
		});

		startAnimating();
	    
	  

	     mp = MediaPlayer.create(getApplicationContext(), R.raw.music01);
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
	 @Override
	    public void onDestroy(){
	    	super.onDestroy();
	    	System.gc();
	    	finish();
	    }
	 
	 private void startAnimating() {
		 Animation move_from_left = AnimationUtils.loadAnimation(this, R.anim.move_from_left);
		 
		 Animation custom_anim = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
		 
		 ImageView title_ImageView = (ImageView) findViewById(R.id.title_ImageView);	 
		 		 
		 ImageButton play_ImageButton = (ImageButton) findViewById(R.id.play_ImageButton);		 
		 ImageButton options_ImageButton = (ImageButton) findViewById(R.id.options_ImageButton);
		 ImageButton facebook_ImageButton = (ImageButton) findViewById(R.id.facebook_ImageButton);
		 ImageButton twitter_ImageButton = (ImageButton) findViewById(R.id.twitter_ImageButton);
		 
		 
		 
		 
		 title_ImageView.startAnimation(custom_anim); 
		 		 
		 play_ImageButton.startAnimation(move_from_left);		 
		 options_ImageButton.startAnimation(move_from_left);
		 facebook_ImageButton.startAnimation(move_from_left);
		 twitter_ImageButton.startAnimation(move_from_left);
		 
	 }
	 
	 private void openBrowser(String url){	    	
	    	Intent intent = new Intent(Intent.ACTION_VIEW, 
	    	Uri.parse("http://" + url));
	    	startActivity(intent);
	    }

	 @Override
	 public void onBackPressed() {
		 finish();
	 }
//	@Override
//	public void noAdAvailableListener() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onAdCached(AdType arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onAdError(String arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onSDKIntegrationError(String arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onSmartWallAdClosed() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onSmartWallAdShowing() {
//		// TODO Auto-generated method stub
//		
//	}    	 
	 
}
