package es.zgames.laguerradelasvajillasfree.activities;

import es.zgames.laguerradelasvajillasfree.R;
import es.zgames.textadventures.engine.Adventure;
import es.zgames.textadventures.engine.SystemMessage;
import es.zgames.utils.GlobalInformation;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class LaGuerradelasVajillasSelectAdventurePartActivity extends LaGuerradelasVajillasActivity {
	private Adventure<?, ?> adventure = null;

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Screen Properties
		 //////////////////////////////////////////////////////////////////////////////////////
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				  				   WindowManager.LayoutParams.FLAG_FULLSCREEN);
		  
//		 StartAppSDK.init(this, "106842815", "208738605", true);
			
		 setContentView(R.layout.spanish_select_adventure_part_layout);
		      
	
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Part 1 Behaviour
		 //////////////////////////////////////////////////////////////////////////////////////

		 ImageButton playPart1_ImageButton = (ImageButton) findViewById(R.id.playPart1_ImageButton);
		 playPart1_ImageButton.setOnClickListener(new OnClickListener() {
	    	 public void onClick(View v) {
	    		 GlobalInformation globalInformation = GlobalInformation.getInstance();
	    		 adventure = Adventure.getInstance();
	    		 adventure.initAdventure(getApplicationContext(), "PART1");
	    		 globalInformation.setImage01_free(true);
	    		 globalInformation.setImage02_free(true);
	    		 globalInformation.setImage03_free(true);
	    		 globalInformation.setImage04_free(true);
	    		 globalInformation.setImage05_free(true);
	    		 globalInformation.setImage06_free(true);
	    		 globalInformation.setImage07_free(true);
	    		 globalInformation.setImage08_free(true);
	    					 
	    		 adventure.loadLocations(R.raw.part1_spanish_locations_data);
	    		 adventure.loadDescriptions(R.raw.part1_spanish_descriptions_data);
	    		 adventure.loadObjects(R.raw.part1_spanish_objects_data);
	    		 adventure.loadActors(R.raw.part1_spanish_actors_data);
	    		 adventure.loadActorsBehaviours(R.raw.part1_spanish_actors_behavior_data);
	    		 adventure.loadSystemMessage(R.raw.spanish_system_messages);
	    		
	    		  startActivity(new Intent(LaGuerradelasVajillasSelectAdventurePartActivity.this, 
	    				  			       LaGuerradelasVajillasIntroAdventureActivity.class));
	    	 }
	    	  
	      });
		 
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Part 2 Behaviour
		 //////////////////////////////////////////////////////////////////////////////////////

		 ImageButton playPart2_ImageButton = (ImageButton) findViewById(R.id.playPart2_ImageButton);
		 playPart2_ImageButton.setOnClickListener(new OnClickListener() {
	    	 public void onClick(View v) {
	    		 showDialog(0);	 
			}
	    	  
	      });
	    		  
	   
	 	}
	 
	 protected Dialog onCreateDialog(int id) {
		 final Dialog dialog = new Dialog(this);
		 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		 
		 dialog.setContentView(R.layout.spanish_introduce_password_dialog_layout);
		 
		 //dialog.setTitle("Atencion!!");
		 dialog.setCancelable(true);
		 
		 ImageButton cancelImageButton = (ImageButton) dialog.findViewById(R.id.cancel_ImageButton);
		 cancelImageButton.setOnClickListener(new OnClickListener() {
			 public void onClick(View v) {
				 dialog.cancel();
		     }
		  });

		 ImageButton okImageButton = (ImageButton) dialog.findViewById(R.id.ok_ImageButton);
		 okImageButton.setOnClickListener(new OnClickListener() {
			 public void onClick(View v) {
				 
				 EditText password_EditText = (EditText) dialog.findViewById(R.id.password_EditText);
				 GlobalInformation globalInformation = GlobalInformation.getInstance();
				 
				 if (password_EditText.getText().toString().toUpperCase().equals("GEORGE SPIELBERG")) {
								 
					 adventure = Adventure.getInstance();
			    		
					 adventure.initAdventure(getApplicationContext(), "PART2");
					 globalInformation.setImage01_free(true);
		    		 globalInformation.setImage02_free(true);
		    		 globalInformation.setImage03_free(true);
		    		 globalInformation.setImage04_free(true);
		    		 globalInformation.setImage05_free(true);
		    		 globalInformation.setImage06_free(true);
		    		 globalInformation.setImage07_free(true);
		    		 globalInformation.setImage08_free(true);

		    		 adventure.loadLocations(R.raw.part2_spanish_locations_data);
		    		 adventure.loadDescriptions(R.raw.part2_spanish_descriptions_data);
		    		 adventure.loadObjects(R.raw.part2_spanish_objects_data);
					 adventure.loadActors(R.raw.part2_spanish_actors_data);
					 adventure.loadActorsBehaviours(R.raw.part2_spanish_actors_behavior_data);
		    		 adventure.loadSystemMessage(R.raw.spanish_system_messages);
					
					 globalInformation.setAnimatedImage("part1_location_022d");
					 globalInformation.setAnimatedText1(SystemMessage.MESSAGE_ARRIVEN_PRINGOSA_STAR1);
					 globalInformation.setAnimatedText2(SystemMessage.MESSAGE_ARRIVEN_PRINGOSA_STAR2);
					 globalInformation.setAnimatedText3(SystemMessage.MESSAGE_ARRIVEN_PRINGOSA_STAR3);
					 startActivity(new Intent(LaGuerradelasVajillasSelectAdventurePartActivity.this, 
							  				   LaGuerradelasVajillasAnimatedLocationActivity.class));
				 } else {
					 TextView errorMessage_TextView = (TextView) dialog.findViewById(R.id.errorMessage_TextView);
					 errorMessage_TextView.setText("Contrase�a incorrecta.");						
					 
				 }
		     }
		  });

		 
		  return dialog;
		}
	 
	 @Override
	 public void onBackPressed() {
		 finish();
	 }
	 
	 @Override
	 public void onResume() {
	     super.onResume();
	 }

	 @Override
	 public void onPause() {
	     super.onPause();
	 }
}
