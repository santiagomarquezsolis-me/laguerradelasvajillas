package es.zgames.laguerradelasvajillasfree.activities;

import es.zgames.laguerradelasvajillasfree.R;
import es.zgames.utils.AnimationsContainer;
import es.zgames.utils.FramesSequenceAnimation;
import es.zgames.utils.GlobalInformation;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

//import com.GVEumNRl.SCQTemZx128872.*;


public class LaGuerradelasVajillasSelectLanguageActivity extends Activity  {
	//Airpush airpush;
	private TextToSpeech talker;
	private FramesSequenceAnimation animSpain = null;
	private FramesSequenceAnimation animUK = null;
	
	@Override
	    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Screen Properties
		 //////////////////////////////////////////////////////////////////////////////////////
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				  				   WindowManager.LayoutParams.FLAG_FULLSCREEN);
		  
		 
		 
		 //setContentView(R.layout.select_language_layout);
		 setContentView(R.layout.select_language_layout);
//		 airpush = new Airpush(getApplicationContext());
//			airpush.startSmartWallAd(); //launch smart wall on App start
//			airpush.startPushNotification(true);
//			// start icon ad.
//			airpush.startIconAd();
//		 
		

		 //////////////////////////////////////////////////////////////////////////////////////
		 // Spain Flag Behaviour
		 //////////////////////////////////////////////////////////////////////////////////////

		 ImageView spanishFlag_ImageView = (ImageView) findViewById(R.id.spanishFlag_ImageView);
		 spanishFlag_ImageView.setOnClickListener(new OnClickListener() {
	    	  public void onClick(View v) {
	    		  GlobalInformation globalInformation = GlobalInformation.getInstance();
	    		  globalInformation.setLanguage("ES");
	    		  startActivity(new Intent(LaGuerradelasVajillasSelectLanguageActivity.this, LaGuerradelasVajillasMainActivity.class));
			}
	    	  
	      });
	     
	}  

	 
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {			
//			//use smart wall on app exit. 
//		//	airpush.startSmartWallAd();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
	
	 
	    public void say(String text2say){
	    	talker.speak(text2say, TextToSpeech.QUEUE_FLUSH, null);
	    }

		
	

		@Override
		public void onDestroy() {
			if (talker != null) {
				talker.stop();
				talker.shutdown();
			}

			super.onDestroy();
		}
	
	 
	 
}
