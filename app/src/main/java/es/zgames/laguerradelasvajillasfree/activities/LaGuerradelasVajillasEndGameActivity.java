package es.zgames.laguerradelasvajillasfree.activities;

import es.zgames.laguerradelasvajillasfree.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class LaGuerradelasVajillasEndGameActivity extends LaGuerradelasVajillasActivity {
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Screen Properties
		 //////////////////////////////////////////////////////////////////////////////////////
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				  				   WindowManager.LayoutParams.FLAG_FULLSCREEN);
		  
		 setContentView(R.layout.spanish_end_adventure_layout);
		
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Play Behaviour
		 //////////////////////////////////////////////////////////////////////////////////////

		 TextView textView = (TextView) findViewById(R.id.TextView16);
		 textView.setOnClickListener(new OnClickListener() {
	    	 public void onClick(View v) {
	    		  startActivity(new Intent(LaGuerradelasVajillasEndGameActivity.this, LaGuerradelasVajillasMainActivity.class));
			}
	    	  
	      });
	    		  
	   
		 ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
		 imageView1.setOnClickListener(new OnClickListener() {
	    	 public void onClick(View v) {
	    		  startActivity(new Intent(LaGuerradelasVajillasEndGameActivity.this, LaGuerradelasVajillasMainActivity.class));
			}
	    	  
	      });
	 	}
	 
	 
}
