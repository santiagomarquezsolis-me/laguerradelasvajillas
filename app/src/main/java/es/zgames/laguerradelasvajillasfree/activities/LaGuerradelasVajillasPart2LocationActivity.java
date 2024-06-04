package es.zgames.laguerradelasvajillasfree.activities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import es.zgames.laguerradelasvajillasfree.R;
import es.zgames.textadventures.engine.*;
import es.zgames.textadventures.engine.AdventureObject;
import es.zgames.utils.AnimationsContainer;
import es.zgames.utils.FramesSequenceAnimation;
import es.zgames.utils.GlobalInformation;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class LaGuerradelasVajillasPart2LocationActivity<V, K> extends LaGuerradelasVajillasActivity implements OnInitListener {
	static final int DIALOG_END_ID = 1;
	static final int DIALOG_WIN_ID = 2;
	static final int DIALOG_CAUGHT_ID = 3;
	static final int DIALOG_TALK_ID = 4;
	static final int DIALOG_DISK_ID = 5;
	static final int DIALOG_GIVE_ID = 6;
	static final int DIALOG_TALK_ROOM_ID = 7;
	static final int DIALOG_PANEL_ID = 8;
	static final int DIALOG_COMBAT_ID = 9;
	static final int DIALOG_COMBAT2_ID = 10;
	static final int DIALOG_COMBAT3_ID = 11;
	
	static final String CODE_AUTODESTROYED_ON = "8745";
	static final String CODE_TRACTORRAY_OFF = "9125";
	
	private AdventureLocation currentLocation = null;
	private AdventureLocation previousLocation = null;
	private Adventure<?, ?> part2_Adventure = null;
	private Parser parser = null;
	private int textSize = 15;
	private boolean languageSupported = true;
	
	private Dialog dialogObjects = null;
	private Dialog dialogMessages = null;
	private Dialog dialogLoadSave = null;
	private Dialog dialogTalk = null;
	private Dialog dialogTalkRoom = null;
	private Dialog dialogGive = null;
	private Dialog dialogCombat = null;
	private Dialog dialogPanel = null;
	
	
	private boolean doorWestLocked = true;
	private boolean doorEastLocked = true;
	private boolean tractorRayEnabled = true;
	private boolean autodestroyEnabled = false;
	
	private boolean inTheSpace = false;
	
	private TextToSpeech talker;
	
	
	private int soldiers = -1;
	private int hidden = 0;
	private int juan = 0;
	private int eye = 0;
	private int paca = 0;
	private int shipPosition[];
	private int totalShipsDestroyed = 0;
	private int countDownForDestruction = 10;
	
	private GlobalInformation globalInformation = GlobalInformation.getInstance();

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
			showDialog(DIALOG_END_ID);
			
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
		 
		
		 if (globalInformation.getAdvancedLocation().equals(AdventureCodes.TRUE)){
			 setContentView(R.layout.spanish_location_advance_layout);
		 } else {
			 setContentView(R.layout.spanish_location_layout);
		 }	 
		 
		 
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Init Talker
		 //////////////////////////////////////////////////////////////////////////////////////

		 
		 try {
			 talker = new TextToSpeech(this, this);
			 
		     if (globalInformation.getLanguage().equals(AdventureCodes.OPTIONS_LANGUAGE_ENGLISH)){		    	 
		    	 int result = talker.setLanguage(Locale.US);
		    	 if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
		    		 languageSupported = false;
		    		 Log.e("TTS", "This Language is not supported");
		    	 }
		     }
		 } catch (Exception e){
		   	 System.out.println("Error = " + e.getMessage());
		 }
		 
		 
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Init Parser
		 //////////////////////////////////////////////////////////////////////////////////////
		
		 parser = new Parser();
		 parser.initParser();
		 
		 //////////////////////////////////////////////////////////////////////////////////////
		 // Init Adventure
		 //////////////////////////////////////////////////////////////////////////////////////
				
		 part2_Adventure = Adventure.getInstance();
		 
		 updateScreen();
		 
		 //////////////////////////////////////////////////////////////////////////////////////
		 // HighText Button Behaviour
		 //////////////////////////////////////////////////////////////////////////////////////
		 		 
		 ImageButton highText_Button = (ImageButton) findViewById(R.id.highText_Button);
		 highText_Button.setOnClickListener(new OnClickListener() {
			 public void onClick(View v) {		
				 if (textSize < AdventureCodes.MAX_TEXT_SIZE){
					 textSize = textSize + 5;
				 } else {
					 textSize = AdventureCodes.MIN_TEXT_SIZE;
				 }
				 addLine(SystemMessage.MESSAGE_TEXT_SIZE_CHANGED, Color.GREEN);
			 }
		 });
		 
				
		//////////////////////////////////////////////////////////////////////////////////////
		// Map ImagenButton Behaviour
		//////////////////////////////////////////////////////////////////////////////////////
		
		ImageButton map_ImageButton = (ImageButton) findViewById(R.id.map_ImageButton);
		map_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {		
				if (languageSupported){
					talker.stop();
				}
				startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasPart2MapActivity.class));				 				


			}
		});
		
		//////////////////////////////////////////////////////////////////////////////////////
		// Inventary ImagenButton Behaviour
		//////////////////////////////////////////////////////////////////////////////////////
		
		ImageButton inventary_ImageButton = (ImageButton) findViewById(R.id.inventary_ImageButton);
		inventary_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				if (languageSupported){
					talker.stop();
				}
				startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasInventoryActivity.class));				 				

			}
		});
		
	
		
		if (globalInformation.getAdvancedLocation().equals(AdventureCodes.TRUE)){
			//////////////////////////////////////////////////////////////////////////////////////
			// North ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////

			ImageButton north_ImageButton = (ImageButton) findViewById(R.id.north_ImageButton);
			north_ImageButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {	
					parser.parse("N");
					part2_Adventure.increaseActionsCounter();
					executeMoveActions();					 				
		 		}
			});
		
			//////////////////////////////////////////////////////////////////////////////////////
			// South ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton south_ImageButton = (ImageButton) findViewById(R.id.south_ImageButton);
			south_ImageButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {	
				parser.parse("S");
				part2_Adventure.increaseActionsCounter();
				executeMoveActions();					 				
				}
			});
		
			//////////////////////////////////////////////////////////////////////////////////////
			// East ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton east_ImageButton = (ImageButton) findViewById(R.id.east_ImageButton);
			east_ImageButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {	
				parser.parse("E");
				part2_Adventure.increaseActionsCounter();
				executeMoveActions();					 				
				}
			});
			
			//////////////////////////////////////////////////////////////////////////////////////
			// West ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton west_ImageButton = (ImageButton) findViewById(R.id.west_ImageButton);
			west_ImageButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {	
					parser.parse("O");
					part2_Adventure.increaseActionsCounter();
					executeMoveActions();					 				
				}
			});
			
			//////////////////////////////////////////////////////////////////////////////////////
			// Up ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton up_ImageButton = (ImageButton) findViewById(R.id.up_ImageButton);
			up_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				parser.parse("SUBIR");
				part2_Adventure.increaseActionsCounter();
				executeMoveActions();					 				
			}
			});
			
			//////////////////////////////////////////////////////////////////////////////////////
			// Down ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton down_ImageButton = (ImageButton) findViewById(R.id.down_ImageButton);
			down_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
			parser.parse("BAJAR");
			part2_Adventure.increaseActionsCounter();
			executeMoveActions();					 				
			}
			});
			
			//////////////////////////////////////////////////////////////////////////////////////
			// Glass ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton glass_ImageButton = (ImageButton) findViewById(R.id.glass_ImageButton);
			glass_ImageButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {	
					 EditText userCommand_EditText = (EditText) findViewById(R.id.userCommand_EditText);
					 userCommand_EditText.setText("");
					 part2_Adventure.increaseActionsCounter();
					 userCommand_EditText.append("Examina ");
					 
				}
			});
			
			//////////////////////////////////////////////////////////////////////////////////////
			// Hand ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton caught_ImageButton = (ImageButton) findViewById(R.id.caught_ImageButton);
			caught_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				part2_Adventure.increaseActionsCounter();
				showDialog(DIALOG_CAUGHT_ID);
				
			}
			});
			
			//////////////////////////////////////////////////////////////////////////////////////
			// Talk ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton talk_ImageButton = (ImageButton) findViewById(R.id.talk_ImageButton);
			talk_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				part2_Adventure.increaseActionsCounter();
				showDialog(DIALOG_TALK_ID);
			
			}
			});
			
			
			
			

			//////////////////////////////////////////////////////////////////////////////////////
			// Disk ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton disk_ImageButton = (ImageButton) findViewById(R.id.disk_ImageButton);
			disk_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				part2_Adventure.increaseActionsCounter();
				showDialog(DIALOG_DISK_ID);			
			}
			});
			
			
		} else {
			//////////////////////////////////////////////////////////////////////////////////////
			// Exits ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////

			ImageButton exits_ImageButton = (ImageButton) findViewById(R.id.exits_ImageButton);
			exits_ImageButton .setOnClickListener(new OnClickListener() {
				public void onClick(View v) {					
					addLine(part2_Adventure.getCurrentLocationExits(), Color.WHITE);				 				
		 		}
			});
			
		}
		
		if (languageSupported == true) {
			//////////////////////////////////////////////////////////////////////////////////////
			// Exits ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton sound_ImageButton = (ImageButton) findViewById(R.id.sound_ImageButton);
			sound_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				say(part2_Adventure.getCurrentLocation().getDescription());	 				
			}
			});
		 } else {
			 ImageButton sound_ImageButton = (ImageButton) findViewById(R.id.sound_ImageButton);
			 sound_ImageButton.setVisibility(View.INVISIBLE);
			 
		 }
		
		//////////////////////////////////////////////////////////////////////////////////////
		// User Command Button Behaviour
		//////////////////////////////////////////////////////////////////////////////////////
		 
		ImageButton userCommand_ImageButton = (ImageButton) findViewById(R.id.userCommand_ImageButton);
		userCommand_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				talker.stop();
				int action_type = ParserCodes.UNKNOW_ACTION;

				 EditText userCommand_EditText = (EditText) findViewById(R.id.userCommand_EditText);
				 String userSentence = userCommand_EditText.getText().toString();		
				 addLine(userSentence, Color.YELLOW);
				 int counter = parser.getTotalLogicSentence(userSentence);
				
				 if (counter > 0) {
					 for (int index=0; index<counter; index++){
						 // Increase Action Counter
						 part2_Adventure.increaseActionsCounter();
						
						 action_type = parser.parse(parser.getLogicSentence(userSentence, index));
					 						 
						 switch (action_type) {
					 		case ParserCodes.NULL_ACTION: {		
					 			addLine (SystemMessage.MESSAGE_NULL_SENTENCE, Color.WHITE);
					 			if (part2_Adventure.isActorHere(SystemMessage.ACTOR_EYE)){
					 				eye++;					 			
					 			}
					 			if (hidden==0) soldiers++;
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			if (eye>=5){
					 				if (languageSupported){
										talker.stop();
									}
				 					 part2_Adventure.setCurrentLocation("104");
				 					 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
				 				}
					 			if (soldiers>=5){
					 				if (languageSupported){
										talker.stop();
									}
					 				 part2_Adventure.setCurrentLocation("100");
					 				 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
					 			}
					 			break;
					 		}
					 		case ParserCodes.SWIN_ACTION: {
					 			addLine (SystemMessage.MESSAGE_SWIN_IN_TIME, Color.WHITE);
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			break;
					 		}
					 		case ParserCodes.MOVE_ACTION: {
					 			executeMoveActions();					 			
					 			break;				 		
					 		}
					 		case ParserCodes.PANEL_ACTION: {
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			if (currentLocation.getId().equals("013")){
					 				if ((autodestroyEnabled == false) || (tractorRayEnabled == true)) {
					 					showDialog(DIALOG_PANEL_ID);
					 				} else {
					 					addLine (SystemMessage.MESSAGE_R3D2_CONTROL_CENTER4, Color.WHITE);
					 				}
					 			} else {
					 				addLine (SystemMessage.MESSAGE_NO_CONTROLPANEL_HERE, Color.WHITE);
					 			}
					 			break;				 		
					 		}
					 		case ParserCodes.HIDE_ACTION: {
					 			if (currentLocation.getId().equals("006")){
					 				if (hidden==0){
					 					addLine (SystemMessage.MESSAGE_YOU_ARE_HIDE1, Color.WHITE);
					 					addLine (SystemMessage.MESSAGE_YOU_ARE_HIDE2, Color.WHITE);
					 					addLine (SystemMessage.MESSAGE_YOU_ARE_HIDE3, Color.WHITE);
					 					hidden = 1;
					 					soldiers=-1;
					 				} else {
					 					addLine (SystemMessage.MESSAGE_YOU_ARE_HIDE4, Color.WHITE);
					 				}
					 			} else {
					 				addLine (SystemMessage.MESSAGE_NO_PLACE_TO_HIDE, Color.WHITE);
					 			}
					 			break;				 		
					 		}
					 		case ParserCodes.JUMP_ACTION: {
					 			if ((currentLocation.getId().equals("001")) &&
					 				(parser.getActionObject1().equals("HIPERESPACIO"))){
					 				if (part2_Adventure.isActorHere(SystemMessage.ACTOR_R3D2) == false){
					 					addLine (SystemMessage.MESSAGE_HYPERSPACE_IMPOSSIBLE, Color.WHITE);
					 				} else {	
					 					if (inTheSpace==true){
					 						if (totalShipsDestroyed<3) {
					 							addLine (SystemMessage.MESSAGE_HYPERSPACE_IMPOSSIBLE2, Color.WHITE);
					 							addLine (SystemMessage.MESSAGE_HYPERSPACE_IMPOSSIBLE3, Color.WHITE);
					 						} else {
					 							if (languageSupported){
					 								talker.stop();
					 							}
					 							startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasEndGameActivity.class));
					 						}
					 					} else {
					 						addLine (SystemMessage.MESSAGE_HYPERSPACE_IMPOSSIBLE4, Color.WHITE);
					 					}
						 			}
					 			} else {
					 				addLine (SystemMessage.MESSAGE_FIND_SPACESHIP2, Color.WHITE);
					 			}
					 			break;				 						 		
					 		}
					 		case ParserCodes.FIX_ACTION: {					 			
					 			addLine (SystemMessage.MESSAGE_NOTHING_TO_FIX, Color.WHITE);					 			
					 			break;
					 		}
					 		case ParserCodes.TAKEOFF_ACTION: {
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			if (currentLocation.getId().equals("001")){
					 				if (part2_Adventure.isActorHere(SystemMessage.ACTOR_R3D2) == false){
					 					// Sin R3D2 no se puede despegar
					 					addLine (SystemMessage.MESSAGE_TAKEOFF_IMPOSSIBLE, Color.WHITE);
					 				} else {
					 					if (tractorRayEnabled == true){
					 						if (part2_Adventure.isActorHere(SystemMessage.ACTOR_JUAN)){
					 							addLine (SystemMessage.MESSAGE_TRACTORAY_IS_ENABLE1, Color.WHITE);
					 						} else {
					 							addLine (SystemMessage.MESSAGE_TRACTORAY_IS_ENABLE5, Color.WHITE);
					 						}
					 						
					 					} else {
					 						if (part2_Adventure.isActorHere(SystemMessage.ACTOR_PACA) == false){
					 							addLine (SystemMessage.MESSAGE_TAKEOFF_IMPOSSIBLE2, Color.WHITE);
					 						} else {
					 							if (autodestroyEnabled==false){
					 								if (languageSupported){
					 									talker.stop();
					 								}
					 								 part2_Adventure.setCurrentLocation("112");
									 				 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
									 	
					 							} else {
						 							addLine (SystemMessage.MESSAGE_HALCON_IS_FLYING2, Color.WHITE);
						 							addLine (SystemMessage.MESSAGE_HALCON_IS_FLYING3, Color.WHITE);
						 							int resId = getResources().getIdentifier("part1_location_022e", "drawable", getPackageName());
						 							ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
						 							location_ImageView.setImageResource(resId);
						 							AdventureLocation location = part2_Adventure.getLocation("001");
						 							location.setImage("part1_location_022e");
						 							inTheSpace=true;
						 						}
					 						}
					 					}
					 				}
					 			} else {
					 				addLine (SystemMessage.MESSAGE_FIND_SPACESHIP, Color.WHITE);
					 			}
					 			break;				 		
					 		}
					 		case ParserCodes.BLOW_ACTION: {
					 			addLine(SystemMessage.MESSAGE_OK,Color.WHITE);					 			
					 			break;
					 		}
					 		case ParserCodes.BREAK_ACTION: {
					 			addLine (SystemMessage.MESSAGE_DONT_BREAK_PLEASE, Color.WHITE);
					 			break;
					 		}
					 		case ParserCodes.SAVE_ACTION:{
					 			globalInformation.setDoorWestLocked(doorWestLocked);
					 		    globalInformation.setDoorEastLocked(doorEastLocked);
					 		    globalInformation.setTractorRayEnabled(tractorRayEnabled);
					 		    globalInformation.setAutoDestroyEnabled(autodestroyEnabled);
					 		    globalInformation.setInTheSpace(inTheSpace);					 		    
					 		    globalInformation.setSoldiers(soldiers);					 		        
					 		    globalInformation.setHidden(hidden);
					 		    globalInformation.setJuan(juan);
					 		    globalInformation.setEye(eye);
					 		    globalInformation.setPaca(paca);
					 		    globalInformation.setTotalShipsDestroyed(totalShipsDestroyed);
					 		    globalInformation.setCountDownForDestruction(countDownForDestruction);
					 		    
					 			part2_Adventure.savePart2();
					 			addLine(SystemMessage.MESSAGE_GAME_SAVED,Color.GREEN);
					 			break;
					 		}
					 		case ParserCodes.LOAD_ACTION:{
					 			if (part2_Adventure.loadPart2()==true){
					 				doorWestLocked = globalInformation.getDoorWestLocked();
					 				doorEastLocked = globalInformation.getDoorEastLocked();
					 				tractorRayEnabled = globalInformation.getTractorRayEnabled();
					 				autodestroyEnabled = globalInformation.getAutoDestroyEnabled();
					 				inTheSpace = globalInformation.getInTheSpace();
					 				soldiers = globalInformation.getSoldiers();
					 				hidden = globalInformation.getHidden();
					 				juan = globalInformation.getJuan();
					 				if ((juan>0) && 
					 					(!currentLocation.getId().equals("001")) &&
					 					(!currentLocation.getId().equals("002")) &&
					 					(!currentLocation.getId().equals("003")) &&
					 					(!currentLocation.getId().equals("004")) &&
					 					(!currentLocation.getId().equals("005")) &&
					 					(!currentLocation.getId().equals("006"))
					 				   ){
					 					part2_Adventure.getActor(SystemMessage.ACTOR_JUAN).setLocationText(SystemMessage.MESSAGE_JUANSOLO_FIGHTING3);
					 				}
					 				eye = globalInformation.getEye();
					 				paca = globalInformation.getPaca();
					 				if (paca>0){
					 					part2_Adventure.getActor(SystemMessage.ACTOR_PACA).setLocationText(SystemMessage.MESSAGE_PACA_IS_HERE);
					 				}
					 				totalShipsDestroyed = globalInformation.getTotalShipsDestroyed();
					 				if (inTheSpace==true){
					 					if (totalShipsDestroyed>=3){
					 						part2_Adventure.getLocation("001").setImage("part1_location_022f");
					 					} else {
					 						part2_Adventure.getLocation("001").setImage("part1_location_022e");
					 					}
					 				}
					 				countDownForDestruction = globalInformation.getCountDownForDestruction();
					 				updateScreen();
					 				addLine(SystemMessage.MESSAGE_GAME_LOADED, Color.GREEN);
					 			} else {
					 				addLine(SystemMessage.MESSAGE_GAME_NOT_LOADED, Color.GREEN);
					 			}
					 			addLine("", Color.WHITE);
					 			
					 			break;
					 		}
					 		case ParserCodes.BADWORDS_ACTION: {
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			if (part2_Adventure.getBadWordsCounter() == 0){
					 				addLine(SystemMessage.MESSAGE_BADWORDS_WARNING, Color.WHITE);
					 				part2_Adventure.increaseBadWordsCounter();
					 			} else {
					 				addLine(SystemMessage.MESSAGE_BADWORDS_END, Color.WHITE);	
					 			}
					 			if (part2_Adventure.isActorHere(SystemMessage.ACTOR_EYE)){
					 				eye++;					 			
					 			}
					 			if (hidden==0) soldiers++;
					 			if (eye>=5){
					 				if (languageSupported){
										talker.stop();
									}
				 					 part2_Adventure.setCurrentLocation("104");
				 					 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
				 				}
					 			if (soldiers>=5){
					 				if (languageSupported){
										talker.stop();
									}
					 				 part2_Adventure.setCurrentLocation("100");
					 				 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
					 			}
					 			break;
					 		}
					 		case ParserCodes.DESCRIBE_ACTION: {
					 			String object = parser.getActionObject1();
					 			addLine(part2_Adventure.getDescription(object), Color.WHITE);
					 			if (part2_Adventure.isActorHere(SystemMessage.ACTOR_EYE)){
					 				eye++;					 			
					 			}
					 			if (hidden==0) soldiers++;
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			if (eye>=5){
					 				if (languageSupported){
										talker.stop();
									}
				 					 part2_Adventure.setCurrentLocation("104");
				 					 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
				 				}
					 			if (soldiers>=5){
					 				if (languageSupported){
										talker.stop();
									}
					 				 part2_Adventure.setCurrentLocation("100");
					 				 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
					 			}
					 			break;		
					 		}
					 		case ParserCodes.EMPTY_ACTION:{
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine(part2_Adventure.emptyObject(parser.getActionObject1()),Color.WHITE);
					 		
					 			break;
					 		}
					 		case ParserCodes.INVENTARY_ACTION: {
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine(part2_Adventure.getInventary(), Color.WHITE);
					 			addLine(SystemMessage.MESSAGE_TOTAL_OBJECTS + part2_Adventure.getTotalObjectsTaken(), Color.WHITE);
					 			break;								
					 		}
					 		case ParserCodes.EXITS_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine(part2_Adventure.getCurrentLocationExits(), Color.WHITE);	
					 			if (part2_Adventure.isActorHere(SystemMessage.ACTOR_EYE)){
					 				eye++;					 			
					 			}
					 			if (hidden==0) soldiers++;
					 			break;								
					 		}
					 		case ParserCodes.LOOK_ACTION: {	
					 			updateScreen();			
					 			
					 			break;								
					 		}
					 		case ParserCodes.CATCH_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine(part2_Adventure.catchObject(parser.getActionObject1()), Color.WHITE);					 			
					 			break;								
					 		}
					 		case ParserCodes.LEAVE_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine(part2_Adventure.leaveObject(parser.getActionObject1()), Color.WHITE);
					 			if (currentLocation.getId().equals("025")){ // Estoy en lo alto del �rbol.
					 														// Habr�a que mejorar esto en un posible motor
					 				
					 				addLine(part2_Adventure.fallDownObject(parser.getActionObject1()), Color.WHITE);
					 			}
					 			break;								
					 		}
					 		case ParserCodes.SHAKE_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine(SystemMessage.MESSAGE_CANT_DO_THAT, Color.WHITE);
					 			break;
					 		}
					 		case ParserCodes.OPEN_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine(part2_Adventure.open(parser.getActionObject1()), Color.WHITE);
					 			
					 			break;								
					 		}
					 		
					 		case ParserCodes.END_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			showDialog(DIALOG_END_ID);
					 			break;
					 		}	
					 		case ParserCodes.FILL_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine(part2_Adventure.fillObject(parser.getActionObject1()),Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.PISS_ACTION: {
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			if (currentLocation.getId().equals("016")){
					 				if (part2_Adventure.getObject(SystemMessage.OBJECT_CARD).getLocationId().equals(AdventureCodes.OBJECT_UNDEFINED)){
					 					addLine(SystemMessage.MESSAGE_YOU_ARE_PISSING1,Color.WHITE);
					 					addLine(SystemMessage.MESSAGE_YOU_ARE_PISSING2,Color.WHITE);
					 					part2_Adventure.getObject(SystemMessage.OBJECT_CARD).setLocationId("016");
					 				} else {
					 					addLine(SystemMessage.MESSAGE_NO_MORE_PIS,Color.WHITE);
					 				}
					 			} else {
					 				addLine(SystemMessage.MESSAGE_NOT_BE_A_PIG,Color.WHITE);
					 			}
					 			break;
					 		}	
					 		case ParserCodes.DRINK_ACTION: {
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine(part2_Adventure.drink(parser.getActionObject1()),Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.PUT_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			if (currentLocation.getId().equals("022")){
					 				if ((parser.getActionObject1().equals(SystemMessage.OBJECT_CARD)) &&
							 			(parser.getActionObject2().equals(SystemMessage.OBJECT_RANURA)) &&
							 			(part2_Adventure.isObjectTaken(SystemMessage.OBJECT_CARD))){
					 					
					 					if (part2_Adventure.isActorHere(SystemMessage.ACTOR_EYE)){
					 						if (languageSupported){
					 							talker.stop();
					 						}
					 						part2_Adventure.setCurrentLocation("105");
					 						startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 					} else {
					 						addLine (SystemMessage.MESSAGE_JAILS_OPEN1, Color.WHITE);					 					
					 						addLine (SystemMessage.MESSAGE_JAILS_OPEN2, Color.WHITE);
					 						part2_Adventure.getObject(SystemMessage.OBJECT_CARD).setLocationId(AdventureCodes.OBJECT_DESTROY);
					 						doorEastLocked=false;
					 						doorWestLocked=false;
					 					}
					 				}
					 			} else {
						 			
						 			addLine(part2_Adventure.put(parser.getActionObject1(),parser.getActionObject2()),Color.WHITE);
						 			if ((parser.getActionObject1().equals(SystemMessage.OBJECT_MUNITION)) &&
						 			    (parser.getActionObject2().equals(SystemMessage.OBJECT_GUN)) && 
						 			    (part2_Adventure.getObject(SystemMessage.OBJECT_MUNITION).getLocationId().equals(AdventureCodes.OBJECT_IN_OTHER_OBJECT)) &&
						 			    (part2_Adventure.getObject(SystemMessage.OBJECT_GUN).getLocationId().equals(AdventureCodes.OBJECT_TAKEN))
						 			    ){
						 				part2_Adventure.getObject(SystemMessage.OBJECT_GUN).setStatus(SystemMessage.MESSAGE_GUN_LOADED);
						 			}
					 			}
					 			
					 			break;
					 		}	
					 		case ParserCodes.QUIT_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine(part2_Adventure.quit(parser.getActionObject1(), parser.getActionObject2()),Color.WHITE);
					 			if ((parser.getActionObject1().equals(SystemMessage.OBJECT_MUNITION)) &&
						 			    (parser.getActionObject2().equals(SystemMessage.OBJECT_GUN)) && 
						 			    (part2_Adventure.getObject(SystemMessage.OBJECT_MUNITION).getLocationId().equals(AdventureCodes.OBJECT_TAKEN)) &&
						 			    (part2_Adventure.getObject(SystemMessage.OBJECT_GUN).getLocationId().equals(AdventureCodes.OBJECT_TAKEN))
						 			    ){
						 				part2_Adventure.getObject(SystemMessage.OBJECT_GUN).setStatus(SystemMessage.MESSAGE_GUN_UNLOADED);
						 			}
					 			if (currentLocation.getNeedLight().equals(AdventureCodes.TRUE)){
					 				updateScreen();
					 			}
					 			break;
					 		}	
					 		case ParserCodes.HELP_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine (part2_Adventure.getCurrentLocation().getHelp(), Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.TURNON_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine (part2_Adventure.turnOn(parser.getActionObject1()), Color.WHITE);
					 			if (currentLocation.getNeedLight().equals(AdventureCodes.TRUE)){
					 				updateScreen();
					 			}
					 			break;
					 		}	
					 		case ParserCodes.TURNOFF_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									} 
					 				part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine (part2_Adventure.turnOff(parser.getActionObject1()), Color.WHITE);
					 			if (currentLocation.getNeedLight().equals(AdventureCodes.TRUE)){
					 				updateScreen();
					 			}
					 			break;
					 		}	
					 		case ParserCodes.KILL_ACTION: {	
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			if ((currentLocation.getId().equals("015")) && 
					 			    (parser.getActionActor().equals(SystemMessage.ACTOR_DARTHWATER))){
					 				if (globalInformation.getAdvancedLocation().equals(AdventureCodes.TRUE)){
					 					if (part2_Adventure.isObjectTaken(SystemMessage.OBJECT_LASERSWORD)){
					 						addLine (SystemMessage.MESSAGE_COMBAT_MODE, Color.WHITE);
					 						showDialog(DIALOG_COMBAT_ID);
					 					} else {
					 						if (languageSupported){
					 							talker.stop();
					 						}
					 						part2_Adventure.setCurrentLocation("101");
					 						startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 					}
					 				} else {
					 					
					 				}
					 			} else if ((currentLocation.getId().equals("018")) && 
						 			    (parser.getActionActor().equals(SystemMessage.ACTOR_BOT))){
						 				if (globalInformation.getAdvancedLocation().equals(AdventureCodes.TRUE)){
						 						addLine (SystemMessage.MESSAGE_COMBAT_MODE, Color.WHITE);
						 						showDialog(DIALOG_COMBAT2_ID);
						 				} else {
						 					
						 				}	
					 				
					 			} else {
						 			addLine (part2_Adventure.kill(parser.getActionActor()), Color.WHITE);
					 			}
					 			break;
					 		}	
					 		case ParserCodes.AD_ACTION: {
					 			if (autodestroyEnabled){
					 				countDownForDestruction--;
					 			}
					 			if ((countDownForDestruction<=0) && (inTheSpace==false)){
					 				if (languageSupported){
										talker.stop();
									}
									 part2_Adventure.setCurrentLocation("111");
									 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
								
								}
					 			addLine (part2_Adventure.ad(parser.getActionActor()), Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.PUSH_ACTION: {	
					 			addLine (part2_Adventure.push(parser.getActionActor()), Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.EAT_ACTION: {	
					 			addLine (part2_Adventure.eat(parser.getActionObject1()), Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.SAY_ACTION: {	
					 			//Una acci�n de decir puede llevar asociado un comportamiento en el actor
					 			String sayReturn = part2_Adventure.say(parser.getActionActor(), parser.getActionObject1());
					 			if (part2_Adventure.isBehaviourId(sayReturn)){
					 				Behaviour behaviour = part2_Adventure.getBehaviour(sayReturn);
					 				if (!behaviour.getMovePlayerTo().equals("")){ // Tengo que mover al usuario a una localidad
					 					part2_Adventure.setCurrentLocation(behaviour.getMovePlayerTo());
					 					if (part2_Adventure.getCurrentLocation().getType().equals(AdventureCodes.LOCATION_TYPE_DEAD)){
					 						if (languageSupported){
					 							talker.stop();
					 						}
							 				startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));				 				
							 			} else {
							 				updateScreen();
							 			}
							 			break;	
					 				}
					 				if (behaviour.getType().equals(AdventureCodes.BEHAVIOR_TYPE_FOLLOW)){
					 					addLine(part2_Adventure.activateFollowingBehaviour(parser.getActionActor()), Color.WHITE);
					 				}
					 				
					 				if (behaviour.getObjectsReturned().size()>0){ // El comportamiento devuelve objetos
					 					HashMap<String, String> objectsReturned = behaviour.getObjectsReturned();
					 					Iterator<?> it = objectsReturned.entrySet().iterator();					 					
					 					while (it.hasNext()){ // Recorremos la lista de Actores
					 						Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
					 						String object = (String)e.getKey();
					 						String message = (String)e.getValue();
					 						part2_Adventure.setObjectVisible(object, currentLocation.getId());
					 						addLine(message,Color.WHITE);
					 					}
					 							
					 				}
					 			} else {
					 				addLine(sayReturn, Color.WHITE);
					 			}
					 			break;
					 		}	
					 		case ParserCodes.FUCK_ACTION: {
					 			if (part2_Adventure.getTotalLocationActors()>0){
					 				addLine(SystemMessage.MESSAGE_NOT_FUCK_IS_POSSIBLE, Color.WHITE);
					 			} else {
					 				addLine(SystemMessage.MESSAGE_NOT_FUCK_IS_POSSIBLE2, Color.WHITE);
					 			}
					 			break;
					 		}
					 		case ParserCodes.GIVE_ACTION: {	
					 			//Una acci�n de decir puede llevar asociado un comportamiento en el actor
					 			String sayReturn = part2_Adventure.give(parser.getActionActor(), parser.getActionObject1());
					 			if (part2_Adventure.isBehaviourId(sayReturn)){
					 				Behaviour behaviour = part2_Adventure.getBehaviour(sayReturn);
					 				
					 			} else {
					 				if (((parser.getActionActor().equals(SystemMessage.ACTOR_C2P2) || 
					 					      (parser.getActionActor().equals(SystemMessage.ACTOR_R3D2)))) && 
					 					     (parser.getActionObject1().equals(SystemMessage.OBJECT_OILCAN))){
					 						 part2_Adventure.activateFollowingBehaviour(SystemMessage.ACTOR_C2P2);
					 						int resId = getResources().getIdentifier("part1_location_004b", "drawable", getPackageName());
						 					ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
						 					location_ImageView.setImageResource(resId);
						 					
					 				}
					 				if ((parser.getActionActor().equals(SystemMessage.ACTOR_JUAN)) &&					 					       
					 					(parser.getActionObject1().equals(SystemMessage.OBJECT_CREDITS))){
					 						 part2_Adventure.activateFollowingBehaviour(SystemMessage.ACTOR_JUAN);
					 				}
					 					
					 				addLine(sayReturn, Color.WHITE);
					 			}
					 			break;
					 		}	
					 		case ParserCodes.SHOOT_ACTION:{
					 			if (part2_Adventure.isObjectTaken(SystemMessage.OBJECT_GUN)){
					 				if (part2_Adventure.isObjectInsideOther(SystemMessage.OBJECT_MUNITION)){
					 					if (parser.getActionObject1().equals("")){
					 						addLine(SystemMessage.MESSAGE_SHOOT_NOTHING,Color.WHITE);
					 					} else {
					 						if  ((parser.getActionObject1().equals(SystemMessage.ACTOR_EYE)) && 
						 							 (part2_Adventure.isActorHere(SystemMessage.ACTOR_EYE))){
					 							part2_Adventure.setActorVisible(SystemMessage.ACTOR_EYE, AdventureCodes.OBJECT_DESTROY);
					 							currentLocation.setImage("part2_location_022b");
					 							addLine(SystemMessage.MESSAGE_SHOT_EYE, Color.WHITE);
					 						} else  if  (((parser.getActionObject1().equals(SystemMessage.ACTOR_OBI)) ||
					 							  (parser.getActionObject1().equals("CABALLERO")) ||
					 							  (parser.getActionObject1().equals("MAESTRO")))&& 
					 							 (part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI))){
					 							 // Estas muerto
					 							if (languageSupported){
					 								talker.stop();
					 							}
					 							 part2_Adventure.setCurrentLocation("106");
					 							 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 						} else if ((parser.getActionObject1().equals(SystemMessage.ACTOR_JUAN))&& 
				 									   (part2_Adventure.isActorHere(SystemMessage.ACTOR_JUAN))){
					 							if (languageSupported){
					 								talker.stop();
					 							}
					 							part2_Adventure.setCurrentLocation("107");
					 							 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 						} else if (((parser.getActionObject1().equals(SystemMessage.ACTOR_C2P2)) || 
					 								    (parser.getActionObject1().equals(SystemMessage.ACTOR_R3D2)) || 
					 								    (parser.getActionObject1().equals("ROBOT")) || 
					 								    (parser.getActionObject1().equals("ROBOTS"))) && 
					 								   (part2_Adventure.isActorHere(SystemMessage.ACTOR_C2P2))){
					 							if (languageSupported){
					 								talker.stop();
					 							} 
					 							part2_Adventure.setCurrentLocation("108");
					 							 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 						} else if ((parser.getActionObject1().equals("SOLDADOS")) ||
					 								    (parser.getActionObject1().equals("SOLDADO")) ||
					 								    (parser.getActionObject1().equals("TROPAS"))) {
					 							if ((currentLocation.getId().equals("007"))){
					 								addLine(SystemMessage.MESSAGE_HELPING_JUAN1, Color.WHITE);
					 								if (!part2_Adventure.isActorHere(SystemMessage.ACTOR_PACA)){
					 									addLine(SystemMessage.MESSAGE_HELPING_JUAN2, Color.WHITE);
					 								} 
					 								if (tractorRayEnabled == true){
					 									addLine(SystemMessage.MESSAGE_HELPING_JUAN3, Color.WHITE);
					 								}
						 							 						 				
					 							} else {
					 								addLine(SystemMessage.MESSAGE_SHOOT_NOT_POSSIBLE, Color.WHITE);
					 							}					 							
					 						} else {
					 							addLine(SystemMessage.MESSAGE_SHOOT_NOT_POSSIBLE, Color.WHITE);
					 						}
					 					}
					 				} else {
					 					addLine(SystemMessage.MESSAGE_SHOOT_WITHOUT_MUNITION,Color.WHITE);
					 				}
					 			} else {
					 				addLine(SystemMessage.MESSAGE_SHOOT_NO_GUN,Color.WHITE);
					 			}
					 			
					 			break;
					 		}
					 		case ParserCodes.ACTIVATE_ACTION:{
					 			if ((currentLocation.getId().equals("005")) && 
					 				((parser.getActionObject1().equals(SystemMessage.MESSAGE_TURBOLASER)) || 
					 				 (parser.getActionObject1().equals("TURBOLASERES")))) {
					 				if (tractorRayEnabled == true){
					 					addLine(SystemMessage.MESSAGE_IMPOSSIBLE_ACTIVATE_TURBOLASER,Color.WHITE);
					 				} else {
					 					if (totalShipsDestroyed>=3){
					 						addLine(SystemMessage.MESSAGE_GO_HOME1,Color.WHITE);
					 					} else {
					 						showDialog(DIALOG_COMBAT3_ID);
					 					}
					 				}
					 			} else {
					 				addLine (SystemMessage.MESSAGE_NOTHING_TO_ACTIVATE, Color.WHITE);
					 			}
					 			break;
					 		}	
					 		case ParserCodes.UNKNOW_ACTION: {
					 			addLine (SystemMessage.MESSAGE_UNKOWN_ACTION, Color.WHITE);
					 			break;
					 		}
					 		case ParserCodes.LISTEN_ACTION: {	
					 			addLine(SystemMessage.MESSAGE_LISTEN_1, Color.WHITE);
					 			break;
					 		}
					 		case ParserCodes.OBJECT_WORD: {	
					 			addLine(part2_Adventure.catchObject(parser.getActionObject1()), Color.WHITE);						
					 			break;								
					 		}		 
						 }
					 }
				 } else {
						addLine (SystemMessage.MESSAGE_UNKOWN_ACTION, Color.WHITE);
				 }
				 userCommand_EditText.setText("");
				 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				 imm.hideSoftInputFromWindow(userCommand_EditText.getWindowToken(),0); 		 
			}
			
			
		});
		 
		//////////////////////////////////////////////////////////////////////////////////////
		// Location ImageView Button Behaviour
		//////////////////////////////////////////////////////////////////////////////////////
		 
		ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);		
		//int resId = getResources().getIdentifier(currentLocation.getImage(), "drawable", getPackageName());
		//location_ImageView.setImageResource(resId);		
		location_ImageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateScreen();
				//String userSentence = currentLocation.getDescription();				 
				//clearTextArea();
				//addLine(userSentence); 		 
			}
		});
		 
//		 if (globalInformation.getMusic().equals(AdventureCodes.TRUE)){
//			 MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.music01);
// 	    	 mp.setLooping(true);
// 	    	 mp.start();
// 	     }
		startAnimatingMainPicture();
	 }
	 

	public void executeMoveActions() {
		if (languageSupported){
			talker.stop();
		}
		
		if (autodestroyEnabled) countDownForDestruction--;
		if ((countDownForDestruction<=0) && (inTheSpace==false)){
			if (languageSupported){
				talker.stop();
			}
			 part2_Adventure.setCurrentLocation("111");
			 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
		
		}
		
		if (eye>=5){
			if (languageSupported){
				talker.stop();
			}
			 part2_Adventure.setCurrentLocation("104");
			 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
		} 
		if (soldiers>=5){
			if (languageSupported){
				talker.stop();
			}
			 part2_Adventure.setCurrentLocation("100");
			 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
		} 
		if ((currentLocation.getId().equals("002")) &&
				((parser.getActionVerb().equals("S")) || (parser.getActionVerb().equals("SUR"))) &&
				(hidden == 0)) {
				soldiers++;
			addLine (SystemMessage.MESSAGE_TROOPERS_BLOCK_EXIT, Color.WHITE);
		} else if ((currentLocation.getId().equals("002")) &&
			((parser.getActionVerb().equals("S")) || (parser.getActionVerb().equals("SUR"))) &&
			(hidden == 1)) {
			addLine (SystemMessage.MESSAGE_TROOPERS_NOT_BLOCK_EXIT1, Color.WHITE);
			addLine (SystemMessage.MESSAGE_TROOPERS_NOT_BLOCK_EXIT2, Color.WHITE);
			addLine (SystemMessage.MESSAGE_TROOPERS_NOT_BLOCK_EXIT3, Color.WHITE);
			Behaviour behaviour = part2_Adventure.getBehaviour(part2_Adventure.getActor(SystemMessage.ACTOR_CHEQUEVACA).getFollowingBehaviour());
			behaviour.setActive(AdventureCodes.FALSE);
			part2_Adventure.getActor(SystemMessage.ACTOR_CHEQUEVACA).setLocationId("001");
			hidden = 2;
			soldiers=-1;
		} else if ((currentLocation.getId().equals("002")) &&
				((parser.getActionVerb().equals("S")) || (parser.getActionVerb().equals("SUR"))) &&
				(inTheSpace == true)) {
			addLine (SystemMessage.MESSAGE_YOU_ARE_IN_SPACE_NOW, Color.WHITE);
		} else if ((currentLocation.getId().equals("002")) &&
				((parser.getActionVerb().equals("S")) || (parser.getActionVerb().equals("SUR"))) &&
				(tractorRayEnabled==false) &&
				(part2_Adventure.isActorHere(SystemMessage.ACTOR_PACA) == true)) {
			addLine (SystemMessage.MESSAGE_RUNAWAY, Color.WHITE);
		} else if ((currentLocation.getId().equals("015")) &&
			((parser.getActionVerb().equals("O")) || (parser.getActionVerb().equals("OESTE"))) && 
			(part2_Adventure.isActorHere(SystemMessage.ACTOR_DARTHWATER)== true)) {
			addLine (SystemMessage.MESSAGE_KILL_DARTH_WATER_FIRST, Color.WHITE);
		} else if ((currentLocation.getId().equals("018")) &&
				  (part2_Adventure.isActorHere(SystemMessage.ACTOR_BOT))) {
			addLine (SystemMessage.MESSAGE_BOT_BLOCK_EXIT, Color.WHITE);
		} else if ((currentLocation.getId().equals("022")) &&
				  
			((parser.getActionVerb().equals("E")) || (parser.getActionVerb().equals("ESTE"))) && 
			(doorEastLocked == true)) {
			addLine (SystemMessage.MESSAGE_JAIL_EAST_LOCKED, Color.WHITE);
		} else if ((currentLocation.getId().equals("022")) &&
				((parser.getActionVerb().equals("O"))||(parser.getActionVerb().equals("OESTE"))) && 
				(doorWestLocked == true)) {
				addLine (SystemMessage.MESSAGE_JAIL_WEST_LOCKED, Color.WHITE);
				if (part2_Adventure.isActorHere(SystemMessage.ACTOR_EYE)){
					eye++;
				}
		} else {
			int result = part2_Adventure.move(parser.getActionVerb());
			if (hidden==0) soldiers++;
			
			
		

			if (result == AdventureCodes.MOVE_ACTION_FAIL) {					 			
				addLine(SystemMessage.MESSAGE_WRONG_DIRECTION, Color.WHITE);				
			} else if (result == AdventureCodes.MOVE_ACTION_FAIL_BY_OBJECT) {
				addLine(SystemMessage.MESSAGE_NEED_OBJECTS_FOR_THAT_DIRECTION, Color.WHITE);
			} else {				
				updateScreen();				
			}
		}
		
	}
	
	public void addLine (String text, int color){
		if (!text.equals("")){
			String line = "> " + text;
		 
		 // Add New Line Text
		 LinearLayout description_LinearLayout = (LinearLayout) findViewById(R.id.description_LinearLayout);
		 TextView description_TextView = new TextView(getApplicationContext());
		 description_TextView.setTextSize(textSize);		 
		 description_TextView.setText(line);
		 description_TextView.setTextColor(color);	
		 description_LinearLayout.addView(description_TextView);
		 
	 
		 TextView actionsValue_TextView = (TextView) findViewById(R.id.actionsValue_TextView);
		 actionsValue_TextView.setText(String.valueOf(part2_Adventure.getActionsCounter()));
		 
		 ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);
		 scrollView.fullScroll(ScrollView.FOCUS_DOWN);
		 
		 if (languageSupported==true){
			 say(text);
		 }
		}
		 
	 }
	 
	 public void clearTextArea (){		 
		 LinearLayout description_LinearLayout = (LinearLayout) findViewById(R.id.description_LinearLayout);
		 description_LinearLayout.removeAllViews();		 
	 }
	 
	 public void updateScreen(){		 
		 clearTextArea();
		 previousLocation = currentLocation;
		 currentLocation = part2_Adventure.getCurrentLocation();
		 ImageButton north_ImageButton = null; 
		 ImageButton south_ImageButton = null;
		 ImageButton east_ImageButton = null;
		 ImageButton west_ImageButton = null;
		 ImageButton up_ImageButton = null;	 
		 ImageButton down_ImageButton = null;
		 
		 
		 if (globalInformation.getAdvancedLocation().equals(AdventureCodes.TRUE)){
			 north_ImageButton = (ImageButton) findViewById(R.id.north_ImageButton);
			 north_ImageButton.setImageResource(R.drawable.ic_north);
			 south_ImageButton = (ImageButton) findViewById(R.id.south_ImageButton);
			 south_ImageButton.setImageResource(R.drawable.ic_south);
			 east_ImageButton = (ImageButton) findViewById(R.id.east_ImageButton);
			 east_ImageButton.setImageResource(R.drawable.ic_east);
			 west_ImageButton = (ImageButton) findViewById(R.id.west_ImageButton);
			 west_ImageButton.setImageResource(R.drawable.ic_west);
			 up_ImageButton = (ImageButton) findViewById(R.id.up_ImageButton);
			 up_ImageButton.setImageResource(R.drawable.ic_up);
			 down_ImageButton = (ImageButton) findViewById(R.id.down_ImageButton);
			 down_ImageButton.setImageResource(R.drawable.ic_down);
		 }
		 
		 
		 Vector<String> messages = currentLocation.getMessages();
		 if (messages.size()>0){
			 for (int index=0; index<messages.size(); index++){
				 addLine(messages.get(index), Color.WHITE);
			 }
		 }
		 TextView location_TextView = (TextView) findViewById(R.id.location_TextView);
		 location_TextView.setText(currentLocation.getTitle());
		 
		 TextView actionsValue_TextView = (TextView) findViewById(R.id.actionsValue_TextView);
		 actionsValue_TextView.setText(String.valueOf(part2_Adventure.getActionsCounter()));
		 	
		 
		 if (currentLocation == previousLocation) {
			 if (currentLocation.getNeedLight().equals(AdventureCodes.FALSE)){
				 if (globalInformation.getWithGraphics().equals(AdventureCodes.TRUE)){						
				 ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);		
				 if (globalInformation.getImageType().equals(AdventureCodes.OPTIONS_IMAGE_NORMAL)){
					 int resId = getResources().getIdentifier(currentLocation.getImage(), "drawable", getPackageName());
					 location_ImageView.setImageResource(resId);
				 } else {
					 int resId = getResources().getIdentifier(currentLocation.getRetroImage(), "drawable", getPackageName());
					 location_ImageView.setImageResource(resId);
				 }
				 }
				 addLine(SystemMessage.MESSAGE_YOU_STAY_IN + currentLocation.getDescription(), Color.WHITE);
			 } else { // La localidad necesita luz.
				 if (part2_Adventure.isObjectWithLightTakenAndTurnOn().equals(AdventureCodes.FALSE)){
					 if (globalInformation.getWithGraphics().equals(AdventureCodes.TRUE)){
							
					 ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
					 // Es una peque;a el usar una localidad fija con la imagen, si hago algun dia un
					 // editor lo cambiaremos
					 int resId = getResources().getIdentifier("part1_location_999", "drawable", getPackageName());
					 location_ImageView.setImageResource(resId);	
					 }
					 addLine(SystemMessage.MESSAGE_IS_VERY_DARK, Color.WHITE);
				 } else {
					 if (globalInformation.getWithGraphics().equals(AdventureCodes.TRUE)){
							
					 ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
					 if (globalInformation.getImageType().equals(AdventureCodes.OPTIONS_IMAGE_NORMAL)){
						 int resId = getResources().getIdentifier(currentLocation.getImage(), "drawable", getPackageName());
						 location_ImageView.setImageResource(resId);
					 } else {
						 int resId = getResources().getIdentifier(currentLocation.getRetroImage(), "drawable", getPackageName());
						 location_ImageView.setImageResource(resId);
					 }
					 }
					 addLine(currentLocation.getDescription(), Color.WHITE);				
				 }
			 }
		 } else {
			 if (currentLocation.getNeedLight().equals(AdventureCodes.FALSE)){
				 if (globalInformation.getWithGraphics().equals(AdventureCodes.TRUE)){
						
				 ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);		
				 if (globalInformation.getImageType().equals(AdventureCodes.OPTIONS_IMAGE_NORMAL)){
					 int resId = getResources().getIdentifier(currentLocation.getImage(), "drawable", getPackageName());
					 location_ImageView.setImageResource(resId);
				 } else {
					 int resId = getResources().getIdentifier(currentLocation.getRetroImage(), "drawable", getPackageName());
					 location_ImageView.setImageResource(resId);
				 }
				 }
				 addLine(currentLocation.getDescription(), Color.WHITE);
			 } else { // La localidad necesita luz.
				 if (part2_Adventure.isObjectWithLightTakenAndTurnOn().equals(AdventureCodes.FALSE)){
					 if (globalInformation.getWithGraphics().equals(AdventureCodes.TRUE)){
							
					 ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
					 // Es una peque;a el usar una localidad fija con la imagen, si hago algun dia un
					 // editor lo cambiaremos
					 int resId = getResources().getIdentifier("part1_location_999", "drawable", getPackageName());
					 location_ImageView.setImageResource(resId);	
					 }
					 addLine(SystemMessage.MESSAGE_IS_VERY_DARK, Color.WHITE);
				 } else {
					 if (globalInformation.getWithGraphics().equals(AdventureCodes.TRUE)){
							
					 ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
					 if (globalInformation.getImageType().equals(AdventureCodes.OPTIONS_IMAGE_NORMAL)){
						 int resId = getResources().getIdentifier(currentLocation.getImage(), "drawable", getPackageName());
						 location_ImageView.setImageResource(resId);
					 } else {
						 int resId = getResources().getIdentifier(currentLocation.getRetroImage(), "drawable", getPackageName());
						 location_ImageView.setImageResource(resId);
					 }
					 }
					 addLine(currentLocation.getDescription(), Color.WHITE);				
				 }
			 }
		 }
		 
		 // Si hay objetos en la localidad, muestralos a continuaci�n de la descripci�n
		 if ( ((part2_Adventure.getCurrentLocation().getNeedLight().equals(AdventureCodes.TRUE)) && 
			   (part2_Adventure.isObjectWithLightTakenAndTurnOn().equals(AdventureCodes.TRUE))) || 
			  (part2_Adventure.getCurrentLocation().getNeedLight().equals(AdventureCodes.FALSE))){ 
			 if (part2_Adventure.getTotalLocationObjects() > 0)  {
				 if (!part2_Adventure.getCurrentLocationObjects().equals("")){
					 addLine(part2_Adventure.getCurrentLocationObjects(), Color.GREEN);
				 }
			 }			
		 }

		 // Si los actores tienen comportamientos ejecutarlos			 
		 addLine(part2_Adventure.executeActorsBehaviours(), Color.WHITE);

		 // Si hay actores en la localidad, muestralos a continuaci�n de los objetos
		 addLine(part2_Adventure.getCurrentLocationActors(), Color.RED);

			if ((countDownForDestruction==2) && (inTheSpace==false) && (autodestroyEnabled == true)){
				addLine(SystemMessage.MESSAGE_AUTODESTRUCTION_15SEC3,Color.WHITE);
				addLine(SystemMessage.MESSAGE_AUTODESTRUCTION_15SEC4,Color.WHITE);
			}
			if ((countDownForDestruction==8) && (inTheSpace==false) && (autodestroyEnabled == true)){
				addLine(SystemMessage.MESSAGE_AUTODESTRUCTION_15SEC1,Color.WHITE);
				addLine(SystemMessage.MESSAGE_AUTODESTRUCTION_15SEC2,Color.WHITE);
			}
			
			if ((countDownForDestruction<=0) && (inTheSpace==false) && (autodestroyEnabled == true)){
				if (languageSupported){
					talker.stop();
				}
				part2_Adventure.setCurrentLocation("111");
				 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
			
			}
		 
		 if (part2_Adventure.isActorHere(SystemMessage.ACTOR_EYE)){
				eye++;					 			
			}
		
		 if (hidden==0) soldiers++;
	
		 if (currentLocation.getId().equals("001")){
			 if (tractorRayEnabled == true) { 
				 if (part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI)){
					 addLine(SystemMessage.MESSAGE_TRACTORAY_IS_ENABLE2, Color.WHITE);
				 }
				 addLine(SystemMessage.MESSAGE_TRACTORAY_IS_ENABLE3, Color.WHITE);
				 addLine(SystemMessage.MESSAGE_TRACTORAY_IS_ENABLE4, Color.WHITE);
				 if (hidden == 0) {
						addLine (SystemMessage.MESSAGE_TROOPERS_ARE_COMING1, Color.WHITE);
						addLine (SystemMessage.MESSAGE_TROOPERS_ARE_COMING2, Color.WHITE);
						soldiers=0;
				 }
			 } else {
				 if ((part2_Adventure.isActorHere(SystemMessage.ACTOR_PACA)) && (inTheSpace == false)){
					 addLine(SystemMessage.MESSAGE_RUNAWAY2, Color.WHITE);
				 }
				 if ((!part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI)) && (inTheSpace == false)){
					 addLine(SystemMessage.MESSAGE_RUNAWAY3, Color.WHITE);
				 }
			 }
			 if (totalShipsDestroyed>=3){
				 addLine (SystemMessage.MESSAGE_VICTORY_JUAN, Color.WHITE);
				 addLine (SystemMessage.MESSAGE_VICTORY_PACA, Color.WHITE);
				 addLine (SystemMessage.MESSAGE_VICTORY_C2P2, Color.WHITE);
				 addLine (SystemMessage.MESSAGE_VICTORY_JUAN2, Color.WHITE);
			 }
			
			 
		 }
		 
		 if (currentLocation.getId().equals("002")){
			 if ((part2_Adventure.isActorHere(SystemMessage.ACTOR_PACA)) && 
						(tractorRayEnabled == false)){
				if (paca<100) paca=100;
			}
			
			if ((part2_Adventure.isActorHere(SystemMessage.ACTOR_PACA)) && 
				(tractorRayEnabled == false) &&
				(paca == 100)){
				paca=150;
				addLine (SystemMessage.MESSAGE_JUANSOLO_RETURN1, Color.WHITE);
				addLine (SystemMessage.MESSAGE_JUANSOLO_RETURN2, Color.WHITE);
				part2_Adventure.getActor(SystemMessage.ACTOR_JUAN).setLocationId("001");
				part2_Adventure.getActor(SystemMessage.ACTOR_JUAN).setLocationText("Juansolo");
			}
		 }
		 
		  
		 if (currentLocation.getId().equals("004")){
			if (soldiers>=0){
				addLine(SystemMessage.MESSAGE_TROOPERS_ARE_COMMING1, Color.WHITE);
			}
		 }
		 
		 if (currentLocation.getId().equals("005")){
				if (soldiers>=0){
					addLine(SystemMessage.MESSAGE_TROOPERS_ARE_COMMING2, Color.WHITE);
				}
				if ((inTheSpace == true) && (totalShipsDestroyed<3)){
					addLine(SystemMessage.MESSAGE_ACTIVATE_TURBOLASER, Color.WHITE);
				}
		 }
		 
		 if (currentLocation.getId().equals("006")){
				if (soldiers>=0){
					addLine(SystemMessage.MESSAGE_TROOPERS_ARE_COMMING1, Color.WHITE);
				}
		 }
			 
		 if (currentLocation.getId().equals("007")){
			 if (juan==0){
				 addLine (SystemMessage.MESSAGE_JUANSOLO_FIGHTING1, Color.WHITE);
				 addLine (SystemMessage.MESSAGE_JUANSOLO_FIGHTING2, Color.WHITE);
				 part2_Adventure.getActor(SystemMessage.ACTOR_JUAN).setLocationText(SystemMessage.MESSAGE_JUANSOLO_FIGHTING3);
				 part2_Adventure.getBehaviour(part2_Adventure.getActor(SystemMessage.ACTOR_JUAN).getFollowingBehaviour()).setActive(AdventureCodes.FALSE);
				 juan++;
			 } else {
				 if (autodestroyEnabled==false){
					 addLine (SystemMessage.MESSAGE_JUANSOLO_FIGHTING1, Color.WHITE);
					 addLine (SystemMessage.MESSAGE_JUANSOLO_FIGHTING4, Color.WHITE);
				 } else {
					 addLine (SystemMessage.MESSAGE_JUANSOLO_FIGHTING5, Color.WHITE);
					 if (!part2_Adventure.isActorHere(SystemMessage.ACTOR_PACA)){
						 addLine (SystemMessage.MESSAGE_JUANSOLO_FIGHTING6, Color.WHITE);
					 } 
				 }
				 if (tractorRayEnabled==true){
					 addLine (SystemMessage.MESSAGE_JUANSOLO_PACA4, Color.WHITE);
				 } else {
					 if (part2_Adventure.isActorHere(SystemMessage.ACTOR_PACA)){
						 addLine (SystemMessage.MESSAGE_JUANSOLO_PACA1, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_JUANSOLO_PACA2, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_JUANSOLO_PACA3, Color.WHITE);
					 }
				 }
			 }
			 
		 }
		 		
		 if (currentLocation.getId().equals("013")){
			 if ((tractorRayEnabled == true) || (autodestroyEnabled == false)) {
			    addLine (SystemMessage.MESSAGE_R3D2_CONTROL_CENTER1, Color.WHITE);
			 	addLine (SystemMessage.MESSAGE_R3D2_CONTROL_CENTER2, Color.WHITE);
			 	addLine (SystemMessage.MESSAGE_R3D2_CONTROL_CENTER3, Color.WHITE);
			 }
			 
		 }
		
		 if (currentLocation.getId().equals("014")){
			 if ((part2_Adventure.isActorHere(SystemMessage.ACTOR_C2P2)) &&
				 (part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI))){ 
					 addLine (SystemMessage.MESSAGE_C2P2_HAS_FEAR, Color.WHITE);
			 } else {
				 int number = (int)(Math.random() * 2) % 2;
				 if (number <=1){
					 addLine (SystemMessage.MESSAGE_C2P2_HAS_FEAR4, Color.WHITE);
					 addLine (SystemMessage.MESSAGE_C2P2_HAS_FEAR5, Color.WHITE);
				 }
			 }
		 }

		 if (currentLocation.getId().equals("015")){
			 if (((part2_Adventure.isActorHere(SystemMessage.ACTOR_C2P2))) &&
				 ((part2_Adventure.isActorHere(SystemMessage.ACTOR_DARTHWATER)))){ 
					 addLine (SystemMessage.MESSAGE_C2P2_HAS_FEAR2, Color.WHITE);
			 } else  if (((part2_Adventure.isActorHere(SystemMessage.ACTOR_C2P2))) &&
					 (!(part2_Adventure.isActorHere(SystemMessage.ACTOR_DARTHWATER)))){ 
				 addLine (SystemMessage.MESSAGE_C2P2_HAS_FEAR3, Color.WHITE);
			 }
			 if ((part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI))){
				 addLine (SystemMessage.MESSAGE_OBI_AND_DARTHWATER, Color.WHITE);
			 }
			 if (((part2_Adventure.isActorHere(SystemMessage.ACTOR_PACA))) &&
					 ((part2_Adventure.isActorHere(SystemMessage.ACTOR_DARTHWATER)))){ 
					 addLine (SystemMessage.MESSAGE_PACA_AND_DARTHWATER, Color.WHITE);
			 }
		 }
		 

		 if (currentLocation.getId().equals("016")){
			 if (((part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI))) &&
				 ((part2_Adventure.getObject(SystemMessage.OBJECT_CARD).getLocationId().equals(AdventureCodes.OBJECT_UNDEFINED) ))){ 
					 addLine (SystemMessage.MESSAGE_TOILET_OBI_CONVERSATION1, Color.WHITE);
			 }  else if ((!(part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI))) &&
					 ((part2_Adventure.getObject(SystemMessage.OBJECT_CARD).getLocationId().equals(AdventureCodes.OBJECT_UNDEFINED) ))){ 
				 addLine (SystemMessage.MESSAGE_TOILET_OBI_CONVERSATION2, Color.WHITE);		
			 }
			
		 }
		 
		 if (currentLocation.getId().equals("018")){
			 if ((part2_Adventure.isActorHere(SystemMessage.ACTOR_BOT))){ 
					 addLine (SystemMessage.MESSAGE_R3D2_BOT1, Color.WHITE);
					 addLine (SystemMessage.MESSAGE_R3D2_BOT2, Color.WHITE);
					 addLine (SystemMessage.MESSAGE_R3D2_BOT3, Color.WHITE);
					 addLine (SystemMessage.MESSAGE_R3D2_BOT4, Color.WHITE);
					 addLine (SystemMessage.MESSAGE_R3D2_BOT5, Color.WHITE);
			 }  else if ((!(part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI))) &&
					 ((part2_Adventure.getObject(SystemMessage.OBJECT_CARD).getLocationId().equals(AdventureCodes.OBJECT_UNDEFINED) ))){ 
				 addLine (SystemMessage.MESSAGE_TOILET_OBI_CONVERSATION2, Color.WHITE);		
			 }
			
		 }
		 
		 // Comportamiento particular para la localidad 010
		 if ((currentLocation.getId().equals("022"))){
			 if ((part2_Adventure.isActorHere(SystemMessage.ACTOR_C2P2)) &&
			 	 (part2_Adventure.isActorHere(SystemMessage.ACTOR_EYE))){
			  	addLine (SystemMessage.MESSAGE_C2P2_AND_EYE1, Color.WHITE);		
			  	eye++;
			 }  else if ((part2_Adventure.isActorHere(SystemMessage.ACTOR_C2P2)) &&
					     (!part2_Adventure.isActorHere(SystemMessage.ACTOR_EYE))) {
				 	if (!part2_Adventure.isActorHere(SystemMessage.ACTOR_PACA)){
				 		addLine (SystemMessage.MESSAGE_C2P2_AND_EYE2, Color.WHITE);
				 	} 
			 }  
		 }
		 
		 // Comportamiento particular para la localidad 010
		 if ((currentLocation.getId().equals("025"))){			 
			 if (paca == 0){
				 paca++;
				 if (part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI)){
					 addLine (SystemMessage.MESSAGE_RESCUE_PACA1, Color.WHITE);
					 addLine (SystemMessage.MESSAGE_RESCUE_PACA3, Color.WHITE);
				 } else {
					 addLine (SystemMessage.MESSAGE_RESCUE_PACA2, Color.WHITE);
					 addLine (SystemMessage.MESSAGE_RESCUE_PACA4, Color.WHITE);
					 addLine (SystemMessage.MESSAGE_RESCUE_PACA5, Color.WHITE);
				 }
				 addLine (SystemMessage.MESSAGE_RESCUE_PACA6, Color.WHITE);				 
				 part2_Adventure.getBehaviour(part2_Adventure.getActor(SystemMessage.ACTOR_PACA).getFollowingBehaviour()).setActive(AdventureCodes.TRUE);
				 part2_Adventure.getActor(SystemMessage.ACTOR_PACA).setLocationText(SystemMessage.MESSAGE_PACA_IS_HERE);
			 } else { 
				 
				 addLine (SystemMessage.MESSAGE_RESCUE_PACA7, Color.WHITE);
			 }
		 }
		 
		
		 // Actualizamos los gr�ficos asociados a las salidas disponibles
		 if (globalInformation.getAdvancedLocation().equals(AdventureCodes.TRUE)){
			 Vector<Exit> exits = currentLocation.getExits();
			 for (int i=0; i<exits.size();i++){
				 Exit exit = exits.get(i);
				 if (exit.getShortDirectionName().equals("N")){				 
					 north_ImageButton.setImageResource(R.drawable.ic_north_select);
				 } else if (exit.getShortDirectionName().equals("S")){				 
					 south_ImageButton.setImageResource(R.drawable.ic_south_select);
				 } else if (exit.getShortDirectionName().equals("E")){				
					 east_ImageButton.setImageResource(R.drawable.ic_east_select);
				 } else if (exit.getShortDirectionName().equals("O")){				
					 west_ImageButton.setImageResource(R.drawable.ic_west_select);
				 } else if (exit.getShortDirectionName().equals("BA")){
					 down_ImageButton.setImageResource(R.drawable.ic_down_select);
				 } else if (exit.getShortDirectionName().equals("SU")){
					 up_ImageButton.setImageResource(R.drawable.ic_up_select);
				 }
			 }
		 }
		 startAnimatingMainPicture();
	 }
 
	  
	 public void setUserInterfaceState(boolean state){
		 ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
		 ImageButton highText_Button = (ImageButton) findViewById(R.id.highText_Button);
		 
		 location_ImageView.setEnabled(state);
		 highText_Button.setEnabled(state);
	 }
	 
	 
	 @Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog);
		if (languageSupported) {
			talker.stop();
		}
		
		if (id == DIALOG_DISK_ID) {
			 ImageView load_ImageView = (ImageView) dialogLoadSave.findViewById(R.id.load_ImageView);			 
			 load_ImageView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
						if (part2_Adventure.loadPart2()==true){
							doorWestLocked = globalInformation.getDoorWestLocked();
			 				doorEastLocked = globalInformation.getDoorEastLocked();
			 				tractorRayEnabled = globalInformation.getTractorRayEnabled();
			 				autodestroyEnabled = globalInformation.getAutoDestroyEnabled();
			 				inTheSpace = globalInformation.getInTheSpace();
			 				soldiers = globalInformation.getSoldiers();
			 				hidden = globalInformation.getHidden();
			 				juan = globalInformation.getJuan();
			 				if ((juan>0) && 
				 					(!currentLocation.getId().equals("001")) &&
				 					(!currentLocation.getId().equals("002")) &&
				 					(!currentLocation.getId().equals("003")) &&
				 					(!currentLocation.getId().equals("004")) &&
				 					(!currentLocation.getId().equals("005")) &&
				 					(!currentLocation.getId().equals("006"))
				 				   ){
				 					part2_Adventure.getActor(SystemMessage.ACTOR_JUAN).setLocationText(SystemMessage.MESSAGE_JUANSOLO_FIGHTING3);
				 			}
			 				eye = globalInformation.getEye();
			 				paca = globalInformation.getPaca();	
			 				if (paca>0){
			 					part2_Adventure.getActor(SystemMessage.ACTOR_PACA).setLocationText(SystemMessage.MESSAGE_PACA_IS_HERE);
			 				}
			 				totalShipsDestroyed = globalInformation.getTotalShipsDestroyed();
			 				if (inTheSpace==true){
			 					if (totalShipsDestroyed>=3){
			 						part2_Adventure.getLocation("001").setImage("part1_location_022f");
			 					} else {
			 						part2_Adventure.getLocation("001").setImage("part1_location_022e");
			 					}
			 				}
			 				countDownForDestruction = globalInformation.getCountDownForDestruction();
			 				updateScreen();
			 				addLine(SystemMessage.MESSAGE_GAME_LOADED, Color.GREEN);
			 			} else {
			 				addLine(SystemMessage.MESSAGE_GAME_NOT_LOADED, Color.GREEN);
			 			}
			 			addLine("", Color.WHITE);
					 dialogLoadSave.cancel();
			     }
			  });
			 
			 ImageView save_ImageView = (ImageView) dialogLoadSave.findViewById(R.id.save_ImageView);			 
			 save_ImageView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 globalInformation.setDoorWestLocked(doorWestLocked);
					 globalInformation.setDoorEastLocked(doorEastLocked);
			 		 globalInformation.setTractorRayEnabled(tractorRayEnabled);
			 		 globalInformation.setAutoDestroyEnabled(autodestroyEnabled);
			 		 globalInformation.setInTheSpace(inTheSpace);					 		    
			 		 globalInformation.setSoldiers(soldiers);					 		        
			 		 globalInformation.setHidden(hidden);
			 		 globalInformation.setJuan(juan);
			 		 globalInformation.setEye(eye);
			 		 globalInformation.setPaca(paca);
			 		 globalInformation.setTotalShipsDestroyed(totalShipsDestroyed);
			 		 globalInformation.setCountDownForDestruction(countDownForDestruction);
			 		 part2_Adventure.savePart2();
			 		 addLine(SystemMessage.MESSAGE_GAME_SAVED,Color.GREEN);					 
					 dialogLoadSave.cancel();
			     }
			  });
			 
		} else  if (id == DIALOG_CAUGHT_ID){
			globalInformation.setCaughtSelected(false);
			globalInformation.setLeaveSelected(false);
			globalInformation.setGiveSelected(false);
			 
			setAllImageObjectsInvisible();
			 
			ImageView give_ImageView = (ImageView) dialogObjects.findViewById(R.id.give_ImageView);
			give_ImageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					showGiveObjects();
			     }
			  });
			 
			 ImageView caught_ImageView = (ImageView) dialogObjects.findViewById(R.id.caught_ImageView);
			 caught_ImageView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 showCaughtObjects();				    	
			     }
			  });
			 			 
			 ImageView leave_ImageView = (ImageView) dialogObjects.findViewById(R.id.leave_ImageView);
			 leave_ImageView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 showLeaveObjects();				    	
			     }
			  });
			 
			
		} else if (id == DIALOG_TALK_ID){
			 showActors(dialog, DIALOG_TALK_ID);
		} else if (id == DIALOG_TALK_ROOM_ID){
			 TextView actor_TextView = (TextView) dialogTalkRoom.findViewById(R.id.actor_TextView);
			 TextView response_TextView = (TextView) dialogTalkRoom.findViewById(R.id.response_TextView);
			 response_TextView.setTextColor(Color.GREEN);
			 response_TextView.setText(globalInformation.getActorToTalk().getRandomMessage().toString());
			 ImageView actor_ImageView = (ImageView) dialogTalkRoom.findViewById(R.id.actor_ImageView);
			 actor_TextView.setText(globalInformation.getActorToTalk().getName().toString()+ ":");
			 int resId = getResources().getIdentifier(globalInformation.getActorToTalk().getImage(), "drawable", getPackageName());
			 actor_ImageView.setImageResource(resId);
			 Actor actor = globalInformation.getActorToTalk();
			 LinearLayout questions_LinearLayout = (LinearLayout) dialogTalkRoom.findViewById(R.id.questions_LinearLayout);
			 questions_LinearLayout.removeAllViews();
			 Iterator<?> it = actor.getAutomaticActions().entrySet().iterator();
			 int index = 0;
			 while (it.hasNext()){ // Recorremos la lista de Actores
					Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
					String question = (String)e.getKey();
					
					StringTokenizer strTok = new StringTokenizer(question, "@");
					
					String location = strTok.nextToken();
					question = strTok.nextToken();
					
					if (location.equals(currentLocation.getId())){
					LinearLayout question_LinearLayout = new LinearLayout(getApplicationContext());
					
					TextView question_TextView = new TextView(getApplicationContext());
					ImageView question_ImageView = new ImageView(getApplicationContext());
					question_LinearLayout.setId(index);
					question_ImageView.setImageResource(R.drawable.ic_talk_mini);
					question_ImageView.setPadding(10, 0, 0, 0);
					question_TextView.setText(question);
					question_TextView.setTextSize(20);
					question_TextView.setTextColor(Color.WHITE);
					question_TextView.setPadding(10, 0, 0, 0);
					
					question_LinearLayout.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {							 
							 LinearLayout aux = (LinearLayout)v;
							 TextView sentence = (TextView)aux.getChildAt(1);
							 TextView response_TextView = (TextView) dialogTalkRoom.findViewById(R.id.response_TextView);
							 
							 String sayReturn = part2_Adventure.say(globalInformation.getActorToTalk().getName(), sentence.getText().toString().toUpperCase());
							 if (part2_Adventure.isBehaviourId(sayReturn)){
								 Behaviour behaviour = part2_Adventure.getBehaviour(sayReturn);
					 				if (!behaviour.getMovePlayerTo().equals("")){ // Tengo que mover al usuario a una localidad
					 					part2_Adventure.setCurrentLocation(behaviour.getMovePlayerTo());
					 					if (part2_Adventure.getCurrentLocation().getType().equals(AdventureCodes.LOCATION_TYPE_DEAD)){
					 						if (languageSupported){
					 							talker.stop();
					 						}
					 						startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));				 				
							 			} else {
							 				updateScreen();
							 			}							 			
					 				}
					 				if (behaviour.getType().equals(AdventureCodes.BEHAVIOR_TYPE_FOLLOW)){
					 					response_TextView.setText(part2_Adventure.activateFollowingBehaviour(globalInformation.getActorToTalk().getName()));
					 				}
					 				
					 				if (behaviour.getObjectsReturned().size()>0){ // El comportamiento devuelve objetos
					 					HashMap<String, String> objectsReturned = behaviour.getObjectsReturned();
					 					Iterator<?> it = objectsReturned.entrySet().iterator();					 					
					 					while (it.hasNext()){ // Recorremos la lista de Actores
					 						Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
					 						String object = (String)e.getKey();
					 						String message = (String)e.getValue();
					 						part2_Adventure.setObjectVisible(object, currentLocation.getId());
					 						addLine(message,Color.WHITE);
					 					}					 							
					 				}
					 				
					 				// Algunas consideraciones para OBI
					 				if ((globalInformation.getActorToTalk().getName().equals(SystemMessage.ACTOR_OBI)) &&
					 					(behaviour.getName().equals("OBI_NOS_SIGUE"))
					 					){
					 					
					 					behaviour.setObjectsReturned(new HashMap<String,String>());
					 					behaviour.setDescription("Ya, tio, con una vez que me lo digas vale. No te voy a dar otra cachiporra.");
					 				}
					 				
					 				
					 			} else {
					 				 response_TextView.setText(sayReturn);
									 response_TextView.setTextColor(Color.GREEN);
					 			}
							 
							 
							 aux.removeAllViews();
					     }
					  });
					 
					
					question_LinearLayout.addView(question_ImageView);
					question_LinearLayout.addView(question_TextView);
					questions_LinearLayout.addView(question_LinearLayout);
					index++;
			 }
			 }
			 
				

				 
			 
			 
		} else if (id == DIALOG_GIVE_ID){
			 TextView object_TextView = (TextView) dialogGive.findViewById(R.id.object_TextView);
			 ImageView object_ImageView = (ImageView) dialogGive.findViewById(R.id.object_ImageView);
			 object_TextView.setText(globalInformation.getObjectToGive().getName().toString());
			 int resId = getResources().getIdentifier(globalInformation.getObjectToGive().getImage(), "drawable", getPackageName());
			 object_ImageView.setImageResource(resId);

			 showActors(dialog, DIALOG_GIVE_ID);
		}
	 }
	 
	 protected Dialog onCreateDialog(int id) {
		 
		 
		 if (id == DIALOG_END_ID){
			 dialogMessages = new Dialog(this);
			 
			 dialogMessages.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			 dialogMessages.setContentView(R.layout.close_custom_dialog_layout);
			 //dialog.setTitle("Atencion!!");
			 dialogMessages.setCancelable(true);
			 
			 TextView dialogLine1_TextView = (TextView) dialogMessages.findViewById(R.id.dialogLine1_TextView);
			 TextView dialogLine2_TextView = (TextView) dialogMessages.findViewById(R.id.dialogLine2_TextView);
			 ImageButton cancelImageButton = (ImageButton) dialogMessages.findViewById(R.id.cancel_ImageButton);
			 ImageButton okImageButton = (ImageButton) dialogMessages.findViewById(R.id.ok_ImageButton);
			
			 dialogLine1_TextView.setText(SystemMessage.MESSAGE_QUIT);		 
			 dialogLine2_TextView.setText(SystemMessage.MESSAGE_ARE_YOU_SURE);
		 
			 okImageButton.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 if (languageSupported){
							talker.stop();
						}
					  startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasMainActivity.class));
				    	
			     }
			  });
			 
			 cancelImageButton.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 dialogMessages.cancel();
				 }
			 });
			 return dialogMessages;
		 } else if (id == DIALOG_WIN_ID){
			 dialogMessages = new Dialog(this);
			 dialogMessages.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			 dialogMessages.setContentView(R.layout.close_custom_dialog_layout);
			 //dialog.setTitle("Atencion!!");
			 dialogMessages.setCancelable(true);
			 
			 TextView dialogLine1_TextView = (TextView) dialogMessages.findViewById(R.id.dialogLine1_TextView);
			 TextView dialogLine2_TextView = (TextView) dialogMessages.findViewById(R.id.dialogLine2_TextView);
			 ImageButton cancelImageButton = (ImageButton) dialogMessages.findViewById(R.id.cancel_ImageButton);
			 ImageButton okImageButton = (ImageButton) dialogMessages.findViewById(R.id.ok_ImageButton);
			
			 cancelImageButton.setVisibility(View.INVISIBLE);
			 dialogLine1_TextView.setText(SystemMessage.MESSAGE_CONGRATULATIONS);		 
			 dialogLine2_TextView.setText(SystemMessage.MESSAGE_YOU_DID_IT);
			 
			 okImageButton.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 if (languageSupported){
							talker.stop();
						}
					  startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasMainActivity.class));
				    	
			     }
			  });

			 return dialogMessages;
		 } else if (id == DIALOG_CAUGHT_ID){
			 dialogObjects = new Dialog(this);
			 
			 dialogObjects.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			 dialogObjects.setContentView(R.layout.select_object_custom_dialog_layout);
			 //dialog.setTitle("Atencion!!");			 
			 dialogObjects.setCancelable(true);
			 
			 return dialogObjects;
		 } else if (id == DIALOG_DISK_ID){
			 dialogLoadSave = new Dialog(this);
			 
			 dialogLoadSave.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			 dialogLoadSave.setContentView(R.layout.load_save_custom_dialog_layout);
			 //dialog.setTitle("Atencion!!");			 
			 dialogLoadSave.setCancelable(true);
			 
			
		
			 return dialogLoadSave;
		 } else if (id == DIALOG_TALK_ID){
			 dialogTalk = new Dialog(this);
			 
			 dialogTalk.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			 dialogTalk.setContentView(R.layout.talk_custom_dialog_layout);
			 //dialog.setTitle("Atencion!!");			 
			 dialogTalk.setCancelable(true);
		
			
			 
			 return dialogTalk;
		 } else if (id == DIALOG_GIVE_ID){			 
			 dialogGive = new Dialog(this);
			 
			 dialogGive.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			 dialogGive.setContentView(R.layout.give_custom_dialog_layout);
			 //dialog.setTitle("Atencion!!");			 
			 dialogGive.setCancelable(true);
			 
			 
			 return dialogGive;			 
		 } else if (id == DIALOG_TALK_ROOM_ID){			 
			 dialogTalkRoom = new Dialog(this);
			 
			 dialogTalkRoom.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			 dialogTalkRoom.setContentView(R.layout.talk_room_custom_dialog_layout);
			 //dialog.setTitle("Atencion!!");			 
			 dialogTalkRoom.setCancelable(true);
			 
			 
			 return dialogTalkRoom;		
		 } else if (id == DIALOG_COMBAT_ID){			 
			 dialogCombat = new Dialog(this);
			 
			 dialogCombat.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			 dialogCombat.setContentView(R.layout.combat_custom_dialog_layout);
			 //dialog.setTitle("Atencion!!");			 
			 dialogCombat.setCancelable(false);
			 ProgressBar darthwater_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.darthwater_ProgressBar);
			 darthwater_ProgressBar.setProgress(darthwater_ProgressBar.getProgress() - (part2_Adventure.getTotalObjectsTaken() * 2));
			 
			 Button attack_Button = (Button) dialogCombat.findViewById(R.id.attack_Button);
			 attack_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 int number1 = 0;
					 int number2 = 0;
					 int number3 = 0;
					 int number4 = 0;
					 
					 int resId = 0;
					 ProgressBar darthwater_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.darthwater_ProgressBar);
					 ProgressBar martin_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.martin_ProgressBar);
					 TextView percenWater_TextView = (TextView ) dialogCombat.findViewById(R.id.percenWater_TextView);
					 TextView percenMartin_TextView = (TextView ) dialogCombat.findViewById(R.id.percenMartin_TextView);
					 TextView combatDescription_TextView = (TextView ) dialogCombat.findViewById(R.id.combatDescription_TextView);
					 
					 ImageView water01_ImageView = (ImageView) dialogCombat.findViewById(R.id.water01_ImageView);
					 number1 = (int)(Math.random() * 6) % 6;
					 if (number1 == 0) number1 = 1; 
					 if (number1 == 1) resId = getResources().getIdentifier("dado01", "drawable", getPackageName());  
				     if (number1 == 2) resId = getResources().getIdentifier("dado02", "drawable", getPackageName());
				     if (number1 == 3) resId = getResources().getIdentifier("dado03", "drawable", getPackageName());
				     if (number1 == 4) resId = getResources().getIdentifier("dado04", "drawable", getPackageName());
				     if (number1 == 5) resId = getResources().getIdentifier("dado05", "drawable", getPackageName());
				     if (number1 == 6) resId = getResources().getIdentifier("dado06", "drawable", getPackageName());
				     water01_ImageView.setImageResource(resId);
				     darthwater_ProgressBar.setProgress(darthwater_ProgressBar.getProgress() - (number1 ));	
				     percenWater_TextView.setText(darthwater_ProgressBar.getProgress() + "%");
				     
				     ImageView water02_ImageView = (ImageView) dialogCombat.findViewById(R.id.water02_ImageView);
					 number2 = (int)(Math.random() * 6) % 6;
					 if (number2 == 0) number2 = 1;
					 if (number2 == 1) resId = getResources().getIdentifier("dado01", "drawable", getPackageName()); 
				     if (number2 == 2) resId = getResources().getIdentifier("dado02", "drawable", getPackageName());
				     if (number2 == 3) resId = getResources().getIdentifier("dado03", "drawable", getPackageName());
				     if (number2 == 4) resId = getResources().getIdentifier("dado04", "drawable", getPackageName());
				     if (number2 == 5) resId = getResources().getIdentifier("dado05", "drawable", getPackageName());
				     if (number2 == 6) resId = getResources().getIdentifier("dado06", "drawable", getPackageName());
				     water02_ImageView.setImageResource(resId);
				     darthwater_ProgressBar.setProgress(darthwater_ProgressBar.getProgress() - (number2 ));
				     percenWater_TextView.setText(darthwater_ProgressBar.getProgress() + "%");
				     				     
					 ImageView martin01_ImageView = (ImageView) dialogCombat.findViewById(R.id.martin01_ImageView);
					 number3 = (int)(Math.random() * 6) % 6;
					 if (number3 == 0) number3 = 1;
					 if (number3 == 1) resId = getResources().getIdentifier("dado01", "drawable", getPackageName()); 
				     if (number3 == 2) resId = getResources().getIdentifier("dado02", "drawable", getPackageName());
				     if (number3 == 3) resId = getResources().getIdentifier("dado03", "drawable", getPackageName());
				     if (number3 == 4) resId = getResources().getIdentifier("dado04", "drawable", getPackageName());
				     if (number3 == 5) resId = getResources().getIdentifier("dado05", "drawable", getPackageName());
				     if (number3 == 6) resId = getResources().getIdentifier("dado06", "drawable", getPackageName());
				     martin01_ImageView.setImageResource(resId);				     
				     martin_ProgressBar.setProgress(martin_ProgressBar.getProgress() - (number3 ));
				     percenMartin_TextView.setText(martin_ProgressBar.getProgress() + "%");
				     
					 ImageView martin02_ImageView = (ImageView) dialogCombat.findViewById(R.id.martin02_ImageView);
					 number4 = (int)(Math.random() * 6) % 6;
					 if (number4 == 0) number4 = 1;
					 if (number4 == 1) resId = getResources().getIdentifier("dado01", "drawable", getPackageName()); 
				     if (number4 == 2) resId = getResources().getIdentifier("dado02", "drawable", getPackageName());
				     if (number4 == 3) resId = getResources().getIdentifier("dado03", "drawable", getPackageName());
				     if (number4 == 4) resId = getResources().getIdentifier("dado04", "drawable", getPackageName());
				     if (number4 == 5) resId = getResources().getIdentifier("dado05", "drawable", getPackageName());
				     if (number4 == 6) resId = getResources().getIdentifier("dado06", "drawable", getPackageName());
				     martin02_ImageView.setImageResource(resId);				     
				     martin_ProgressBar.setProgress(martin_ProgressBar.getProgress() - (number4 ));
				     percenMartin_TextView.setText(martin_ProgressBar.getProgress() + "%");
				     
				     int magicNumber = (int)(Math.random() * 5) % 5;
				     if (magicNumber == 0) combatDescription_TextView.setText(SystemMessage.MESSAGE_COMBAT_DARTH_01);
				     if (magicNumber == 1) combatDescription_TextView.setText(SystemMessage.MESSAGE_COMBAT_DARTH_02);
				     if (magicNumber == 2) combatDescription_TextView.setText(SystemMessage.MESSAGE_COMBAT_DARTH_03);
				     if (magicNumber == 3) combatDescription_TextView.setText(SystemMessage.MESSAGE_COMBAT_DARTH_04);
				     if (magicNumber == 4) combatDescription_TextView.setText(SystemMessage.MESSAGE_COMBAT_DARTH_05);
				     if (magicNumber == 5) combatDescription_TextView.setText(SystemMessage.MESSAGE_COMBAT_DARTH_06);
				     if ((darthwater_ProgressBar.getProgress()<=0) || 
				         (martin_ProgressBar.getProgress()<=0)){
				    	 if (darthwater_ProgressBar.getProgress()>=martin_ProgressBar.getProgress()){
				    		 if (languageSupported){
									talker.stop();
								}
				    		 part2_Adventure.setCurrentLocation("102");
				    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
				    	 } else {
				    		 addLine(SystemMessage.MESSAGE_OBI_DEATH1, Color.WHITE);
			 				 addLine(SystemMessage.MESSAGE_OBI_DEATH2, Color.WHITE);
			 				 addLine(SystemMessage.MESSAGE_OBI_DEATH3, Color.WHITE);
			 				 part2_Adventure.getActor(SystemMessage.ACTOR_OBI).setLocationId(AdventureCodes.OBJECT_DESTROY);
			 				 part2_Adventure.getActor(SystemMessage.ACTOR_DARTHWATER).setLocationId(AdventureCodes.OBJECT_DESTROY);
			 				 part2_Adventure.getLocation("015").setImage("part2_location_015b");
				    		 dialogCombat.cancel();
				    	 }
				     }
				    	
			     }
			  });
			 return dialogCombat;	
		 } else if (id == DIALOG_COMBAT2_ID){			 
			 dialogCombat = new Dialog(this);
			 
			 dialogCombat.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			 dialogCombat.setContentView(R.layout.combat2_custom_dialog_layout);
			 //dialog.setTitle("Atencion!!");			 
			 dialogCombat.setCancelable(false);
			 ProgressBar darthwater_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.darthwater_ProgressBar);
			 darthwater_ProgressBar.setProgress(darthwater_ProgressBar.getProgress() - (part2_Adventure.getTotalObjectsTaken() * 2));

			 Button attack_Button = (Button) dialogCombat.findViewById(R.id.attack_Button);
			 attack_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 int number1 = 0;
					 int number2 = 0;
					 int number3 = 0;
					 int number4 = 0;
					 
					 int resId = 0;
					 ProgressBar darthwater_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.darthwater_ProgressBar);
					 ProgressBar martin_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.martin_ProgressBar);
					 TextView percenWater_TextView = (TextView ) dialogCombat.findViewById(R.id.percenWater_TextView);
					 TextView percenMartin_TextView = (TextView ) dialogCombat.findViewById(R.id.percenMartin_TextView);
					 TextView combatDescription_TextView = (TextView ) dialogCombat.findViewById(R.id.combatDescription_TextView);
					 
					 ImageView water01_ImageView = (ImageView) dialogCombat.findViewById(R.id.water01_ImageView);
					 number1 = (int)(Math.random() * 6) % 6;
					 if (number1 == 0) number1 = 1; 
					 if (number1 == 1) resId = getResources().getIdentifier("dado01", "drawable", getPackageName());  
				     if (number1 == 2) resId = getResources().getIdentifier("dado02", "drawable", getPackageName());
				     if (number1 == 3) resId = getResources().getIdentifier("dado03", "drawable", getPackageName());
				     if (number1 == 4) resId = getResources().getIdentifier("dado04", "drawable", getPackageName());
				     if (number1 == 5) resId = getResources().getIdentifier("dado05", "drawable", getPackageName());
				     if (number1 == 6) resId = getResources().getIdentifier("dado06", "drawable", getPackageName());
				     water01_ImageView.setImageResource(resId);
				     darthwater_ProgressBar.setProgress(darthwater_ProgressBar.getProgress() - (number1 ));	
				     percenWater_TextView.setText(darthwater_ProgressBar.getProgress() + "%");
				     
				     ImageView water02_ImageView = (ImageView) dialogCombat.findViewById(R.id.water02_ImageView);
					 number2 = (int)(Math.random() * 6) % 6;
					 if (number2 == 0) number2 = 1;
					 if (number2 == 1) resId = getResources().getIdentifier("dado01", "drawable", getPackageName()); 
				     if (number2 == 2) resId = getResources().getIdentifier("dado02", "drawable", getPackageName());
				     if (number2 == 3) resId = getResources().getIdentifier("dado03", "drawable", getPackageName());
				     if (number2 == 4) resId = getResources().getIdentifier("dado04", "drawable", getPackageName());
				     if (number2 == 5) resId = getResources().getIdentifier("dado05", "drawable", getPackageName());
				     if (number2 == 6) resId = getResources().getIdentifier("dado06", "drawable", getPackageName());
				     water02_ImageView.setImageResource(resId);
				     darthwater_ProgressBar.setProgress(darthwater_ProgressBar.getProgress() - (number2 ));
				     percenWater_TextView.setText(darthwater_ProgressBar.getProgress() + "%");
				     				     
					 ImageView martin01_ImageView = (ImageView) dialogCombat.findViewById(R.id.martin01_ImageView);
					 number3 = (int)(Math.random() * 6) % 6;
					 if (number3 == 0) number3 = 1;
					 if (number3 == 1) resId = getResources().getIdentifier("dado01", "drawable", getPackageName()); 
				     if (number3 == 2) resId = getResources().getIdentifier("dado02", "drawable", getPackageName());
				     if (number3 == 3) resId = getResources().getIdentifier("dado03", "drawable", getPackageName());
				     if (number3 == 4) resId = getResources().getIdentifier("dado04", "drawable", getPackageName());
				     if (number3 == 5) resId = getResources().getIdentifier("dado05", "drawable", getPackageName());
				     if (number3 == 6) resId = getResources().getIdentifier("dado06", "drawable", getPackageName());
				     martin01_ImageView.setImageResource(resId);				     
				     martin_ProgressBar.setProgress(martin_ProgressBar.getProgress() - (number3 ));
				     percenMartin_TextView.setText(martin_ProgressBar.getProgress() + "%");
				     
					 ImageView martin02_ImageView = (ImageView) dialogCombat.findViewById(R.id.martin02_ImageView);
					 number4 = (int)(Math.random() * 6) % 6;
					 if (number4 == 0) number4 = 1;
					 if (number4 == 1) resId = getResources().getIdentifier("dado01", "drawable", getPackageName()); 
				     if (number4 == 2) resId = getResources().getIdentifier("dado02", "drawable", getPackageName());
				     if (number4 == 3) resId = getResources().getIdentifier("dado03", "drawable", getPackageName());
				     if (number4 == 4) resId = getResources().getIdentifier("dado04", "drawable", getPackageName());
				     if (number4 == 5) resId = getResources().getIdentifier("dado05", "drawable", getPackageName());
				     if (number4 == 6) resId = getResources().getIdentifier("dado06", "drawable", getPackageName());
				     martin02_ImageView.setImageResource(resId);				     
				     martin_ProgressBar.setProgress(martin_ProgressBar.getProgress() - (number4 ));
				     percenMartin_TextView.setText(martin_ProgressBar.getProgress() + "%");
				     
				     int magicNumber = (int)(Math.random() * 6) % 6;
				     if (magicNumber == 0) combatDescription_TextView.setText(SystemMessage.MESSAGE_COMBAT_BOT_01);
				     if (magicNumber == 1) combatDescription_TextView.setText(SystemMessage.MESSAGE_COMBAT_BOT_02);
				     if (magicNumber == 2) combatDescription_TextView.setText(SystemMessage.MESSAGE_COMBAT_BOT_03);
				     if (magicNumber == 3) combatDescription_TextView.setText(SystemMessage.MESSAGE_COMBAT_BOT_04);
				     if (magicNumber == 4) combatDescription_TextView.setText(SystemMessage.MESSAGE_COMBAT_BOT_05);
				     if (magicNumber == 5) combatDescription_TextView.setText(SystemMessage.MESSAGE_COMBAT_BOT_06);
				     if ((darthwater_ProgressBar.getProgress()<=0) || 
				         (martin_ProgressBar.getProgress()<=0)){
				    	 if (darthwater_ProgressBar.getProgress()>=martin_ProgressBar.getProgress()){
				    		 if (languageSupported){
									talker.stop();
								}
				    		 part2_Adventure.setCurrentLocation("103");
				    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
				    	 } else {
				    		 addLine(SystemMessage.MESSAGE_BOT_DEATH1, Color.WHITE);
			 				 addLine(SystemMessage.MESSAGE_BOT_DEATH2, Color.WHITE);
			 				 addLine(SystemMessage.MESSAGE_BOT_DEATH3, Color.WHITE);			 				
			 				 addLine(SystemMessage.MESSAGE_BOT_DEATH4, Color.WHITE);
			 				 part2_Adventure.getActor(SystemMessage.ACTOR_BOT).setLocationId(AdventureCodes.OBJECT_DESTROY);
			 				 
				    		 dialogCombat.cancel();
				    	 }
				     }
				    	
			     }
			  });
			 return dialogCombat;
		 } else if (id == DIALOG_COMBAT3_ID){			 
			 dialogCombat = new Dialog(this);
			 
			 dialogCombat.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			 dialogCombat.setContentView(R.layout.combat3_custom_dialog_layout);
			 //dialog.setTitle("Atencion!!");			 
			 dialogCombat.setCancelable(false);
			 
			 TextView AA_TextView = (TextView) dialogCombat.findViewById(R.id.AA_TextView);
			 TextView AB_TextView = (TextView) dialogCombat.findViewById(R.id.AB_TextView);
			 TextView AC_TextView = (TextView) dialogCombat.findViewById(R.id.AC_TextView);
			 TextView AD_TextView = (TextView) dialogCombat.findViewById(R.id.AD_TextView);
			 TextView AE_TextView = (TextView) dialogCombat.findViewById(R.id.AE_TextView);
			 
			 TextView BA_TextView = (TextView) dialogCombat.findViewById(R.id.BA_TextView);
			 TextView BB_TextView = (TextView) dialogCombat.findViewById(R.id.BB_TextView);
			 TextView BC_TextView = (TextView) dialogCombat.findViewById(R.id.BC_TextView);
			 TextView BD_TextView = (TextView) dialogCombat.findViewById(R.id.BD_TextView);
			 TextView BE_TextView = (TextView) dialogCombat.findViewById(R.id.BE_TextView);
			 
			 TextView CA_TextView = (TextView) dialogCombat.findViewById(R.id.CA_TextView);
			 TextView CB_TextView = (TextView) dialogCombat.findViewById(R.id.CB_TextView);
			 TextView CC_TextView = (TextView) dialogCombat.findViewById(R.id.CC_TextView);
			 TextView CD_TextView = (TextView) dialogCombat.findViewById(R.id.CD_TextView);
			 TextView CE_TextView = (TextView) dialogCombat.findViewById(R.id.CE_TextView);
			 
			 TextView DA_TextView = (TextView) dialogCombat.findViewById(R.id.DA_TextView);
			 TextView DB_TextView = (TextView) dialogCombat.findViewById(R.id.DB_TextView);
			 TextView DC_TextView = (TextView) dialogCombat.findViewById(R.id.DC_TextView);
			 TextView DD_TextView = (TextView) dialogCombat.findViewById(R.id.DD_TextView);
			 TextView DE_TextView = (TextView) dialogCombat.findViewById(R.id.DE_TextView);
			 
			 TextView EA_TextView = (TextView) dialogCombat.findViewById(R.id.EA_TextView);
			 TextView EB_TextView = (TextView) dialogCombat.findViewById(R.id.EB_TextView);
			 TextView EC_TextView = (TextView) dialogCombat.findViewById(R.id.EC_TextView);
			 TextView ED_TextView = (TextView) dialogCombat.findViewById(R.id.ED_TextView);
			 TextView EE_TextView = (TextView) dialogCombat.findViewById(R.id.EE_TextView);
			 
			 TextView FA_TextView = (TextView) dialogCombat.findViewById(R.id.FA_TextView);
			 TextView FB_TextView = (TextView) dialogCombat.findViewById(R.id.FB_TextView);
			 TextView FC_TextView = (TextView) dialogCombat.findViewById(R.id.FC_TextView);
			 TextView FD_TextView = (TextView) dialogCombat.findViewById(R.id.FD_TextView);
			 TextView FE_TextView = (TextView) dialogCombat.findViewById(R.id.FE_TextView);
			 
			 ImageView radar_ImageView = (ImageView) dialogCombat.findViewById(R.id.radar_ImageView); 
			 FramesSequenceAnimation anim = AnimationsContainer.getInstance().createRadarAnim(radar_ImageView); 
		     anim.start(); 
		     
			 shipPosition = new int[30];
			 for (int i=0; i<30; i++){
				 shipPosition[i] = 0;
			 }
			 int contador = 0;
			 while (contador<5){
				 int position = (int)(Math.random() * 30) % 30;
				 if (shipPosition[position]==0){
					 shipPosition[position]=1;
					 contador++;
				 } 
			 }
			 
			 /// PRIMERA FILA 
			 
			 AA_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView AA_TextView = (TextView) dialogCombat.findViewById(R.id.AA_TextView);
					
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);
					 int resId = 0;
					 if (shipPosition[0]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }

					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	
					   
					 AA_TextView.setBackgroundResource(resId);
					 AA_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }
			     }
			  });
			 AB_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView AB_TextView = (TextView) dialogCombat.findViewById(R.id.AB_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[1]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 AB_TextView.setBackgroundResource(resId);
					 AB_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 AC_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView AC_TextView = (TextView) dialogCombat.findViewById(R.id.AC_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[2]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 AC_TextView.setBackgroundResource(resId);
					 AC_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 AD_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView AD_TextView = (TextView) dialogCombat.findViewById(R.id.AD_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[3]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 AD_TextView.setBackgroundResource(resId);
					 AD_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 AE_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView AE_TextView = (TextView) dialogCombat.findViewById(R.id.AE_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[4]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 AE_TextView.setBackgroundResource(resId);
					 AE_TextView.setText("");
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 
 /// SEGUNDA FILA 
			 
			 BA_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView BA_TextView = (TextView) dialogCombat.findViewById(R.id.BA_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[5]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 BA_TextView.setBackgroundResource(resId);
					 BA_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 BB_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView BB_TextView = (TextView) dialogCombat.findViewById(R.id.BB_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[6]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 BB_TextView.setBackgroundResource(resId);
					 BB_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 BC_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView BC_TextView = (TextView) dialogCombat.findViewById(R.id.BC_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[7]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 BC_TextView.setBackgroundResource(resId);
					 BC_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 BD_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView BD_TextView = (TextView) dialogCombat.findViewById(R.id.BD_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[8]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 BD_TextView.setBackgroundResource(resId);
					 BD_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 BE_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView BE_TextView = (TextView) dialogCombat.findViewById(R.id.BE_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[9]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 BE_TextView.setBackgroundResource(resId);
					 BE_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 
 /// TERCERA FILA 
			 
			 CA_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView CA_TextView = (TextView) dialogCombat.findViewById(R.id.CA_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[10]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 CA_TextView.setBackgroundResource(resId);
					 CA_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 CB_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView CB_TextView = (TextView) dialogCombat.findViewById(R.id.CB_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[11]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 CB_TextView.setBackgroundResource(resId);
					 CB_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 CC_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView CC_TextView = (TextView) dialogCombat.findViewById(R.id.CC_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[12]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 CC_TextView.setBackgroundResource(resId);
					 CC_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 CD_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView CD_TextView = (TextView) dialogCombat.findViewById(R.id.CD_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[13]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 CD_TextView.setBackgroundResource(resId);
					 CD_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 CE_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView CE_TextView = (TextView) dialogCombat.findViewById(R.id.CE_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[14]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 CE_TextView.setBackgroundResource(resId);
					 CE_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

				 }
			  });
			 
 /// CUARTA FILA 
			 
			 DA_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView DA_TextView = (TextView) dialogCombat.findViewById(R.id.DA_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[15]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 DA_TextView.setBackgroundResource(resId);
					 DA_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 DB_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView DB_TextView = (TextView) dialogCombat.findViewById(R.id.DB_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[16]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 DB_TextView.setBackgroundResource(resId);
					 DB_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 DC_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView DC_TextView = (TextView) dialogCombat.findViewById(R.id.DC_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[17]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 DC_TextView.setBackgroundResource(resId);
					 DC_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 DD_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView DD_TextView = (TextView) dialogCombat.findViewById(R.id.DD_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[18]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 DD_TextView.setBackgroundResource(resId);
					 DD_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 DE_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView DE_TextView = (TextView) dialogCombat.findViewById(R.id.DE_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[19]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));
					 DE_TextView.setBackgroundResource(resId);
					 DE_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 
 /// QUINTA FILA 
			 
			 EA_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView EA_TextView = (TextView) dialogCombat.findViewById(R.id.EA_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[20]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 EA_TextView.setBackgroundResource(resId);
					 EA_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 EB_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView EB_TextView = (TextView) dialogCombat.findViewById(R.id.EB_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[21]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 EB_TextView.setBackgroundResource(resId);
					 EB_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 EC_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView EC_TextView = (TextView) dialogCombat.findViewById(R.id.EC_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[22]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 EC_TextView.setBackgroundResource(resId);
					 EC_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 ED_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView ED_TextView = (TextView) dialogCombat.findViewById(R.id.ED_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[23]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 ED_TextView.setBackgroundResource(resId);
					 ED_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 EE_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView EE_TextView = (TextView) dialogCombat.findViewById(R.id.EE_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[24]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 EE_TextView.setBackgroundResource(resId);
					 EE_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 
 /// SEXTA FILA 
			 
			 FA_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView FA_TextView = (TextView) dialogCombat.findViewById(R.id.FA_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[25]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 FA_TextView.setBackgroundResource(resId);
					 FA_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 FB_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView FB_TextView = (TextView) dialogCombat.findViewById(R.id.FB_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[26]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 FB_TextView.setBackgroundResource(resId);
					 FB_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 FC_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView FC_TextView = (TextView) dialogCombat.findViewById(R.id.FC_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[27]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 FC_TextView.setBackgroundResource(resId);
					 FC_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 FD_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView FD_TextView = (TextView) dialogCombat.findViewById(R.id.FD_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[28]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 FD_TextView.setBackgroundResource(resId);
					 FD_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 FE_TextView.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView FE_TextView = (TextView) dialogCombat.findViewById(R.id.FE_TextView);
					 ProgressBar halcon_ProgressBar = (ProgressBar) dialogCombat.findViewById(R.id.halcon_ProgressBar);

					 int resId = 0;
					 if (shipPosition[29]==1){
						 resId = getResources().getIdentifier("boton_mini_green", "drawable", getPackageName());
						 totalShipsDestroyed++;
					 } else {
						 resId = getResources().getIdentifier("boton_mini_red", "drawable", getPackageName());
					 }
					 
					 TextView number_TextView = (TextView) dialogCombat.findViewById(R.id.number_TextView);
					 if (totalShipsDestroyed==0){
						 number_TextView.setText("Faltan 3 cazas por destruir.");
					 } else if (totalShipsDestroyed==1){
						 number_TextView.setText("Faltan 2 cazas por destruir.");
					 } else if (totalShipsDestroyed==2){
						 number_TextView.setText("Faltan 1 caza por destruir.");
					 } else if (totalShipsDestroyed==3){
						 number_TextView.setText("Faltan 0 cazas por destruir.");
					 }
					 
					 int number1 = (int)(Math.random() * 8) % 8;
					 int number2 = (int)(Math.random() * 8) % 8;
						
					 halcon_ProgressBar.setProgress(halcon_ProgressBar.getProgress() - (number1 + number2 ));	

					 FE_TextView.setBackgroundResource(resId);
					 FE_TextView.setText("");
					 
					 if (halcon_ProgressBar.getProgress()<=0){
						 if (languageSupported){
								talker.stop();
							}
						 part2_Adventure.setCurrentLocation("110");
			    		 startActivity(new Intent(LaGuerradelasVajillasPart2LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 } else if (totalShipsDestroyed==3) {
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED6, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED7, Color.WHITE);
						 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED8, Color.WHITE);
						 part2_Adventure.getLocation("001").setImage("part1_location_022f");
						 dialogCombat.cancel();
					 }

			     }
			  });
			 return dialogCombat;		 
		 } else if (id == DIALOG_PANEL_ID){
			 dialogPanel = new Dialog(this);
			 
			 dialogPanel.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			 dialogPanel.setContentView(R.layout.controlpanel_custom_dialog_layout);
			 //dialog.setTitle("Atencion!!");			 
			 dialogPanel.setCancelable(true);
			 
			 	 
			 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
			 
			 Button zero_Button = (Button) dialogPanel.findViewById(R.id.Zero_Button);
			 Button one_Button = (Button) dialogPanel.findViewById(R.id.One_Button);
			 Button two_Button = (Button) dialogPanel.findViewById(R.id.Two_Button);
			 Button three_Button = (Button) dialogPanel.findViewById(R.id.Three_Button);
			 Button four_Button = (Button) dialogPanel.findViewById(R.id.Four_Button);
			 Button five_Button = (Button) dialogPanel.findViewById(R.id.Five_Button);
			 Button six_Button = (Button) dialogPanel.findViewById(R.id.Six_Button);
			 Button seven_Button = (Button) dialogPanel.findViewById(R.id.Seven_Button);
			 Button eight_Button = (Button) dialogPanel.findViewById(R.id.Eight_Button);
			 Button nine_Button = (Button) dialogPanel.findViewById(R.id.Nine_Button);
			 Button enter_Button = (Button) dialogPanel.findViewById(R.id.Enter_Button);
			 Button clear_Button = (Button) dialogPanel.findViewById(R.id.Clear_Button);
			 CheckBox autoDestruction_CheckBox = (CheckBox) dialogPanel.findViewById(R.id.autoDestruction_CheckBox);
			 CheckBox tractorRay_CheckBox = (CheckBox) dialogPanel.findViewById(R.id.tractorRay_CheckBox );
			 
			 if (autodestroyEnabled==true){
				 autoDestruction_CheckBox.setEnabled(false);
			 }
			 if (tractorRayEnabled==false){
				 tractorRay_CheckBox.setEnabled(false);
			 }
		
			 
			 zero_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
					 if (display_TextView.getText().length()<5){ 
						 display_TextView.setText(display_TextView.getText() + "0");
					 }
			     }
			  });
			 
			 one_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
					 if (display_TextView.getText().length()<5){
						 display_TextView.setText(display_TextView.getText() + "1");
					 }
			     }
			  });
			 
			 two_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
					 if (display_TextView.getText().length()<5){
						 display_TextView.setText(display_TextView.getText() + "2");
					 }
			     }
			  });
			 
			 three_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
					 if (display_TextView.getText().length()<5){
						 display_TextView.setText(display_TextView.getText() + "3");
					 }
			     }
			  });
			 
			 four_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
					 if (display_TextView.getText().length()<5){
						 display_TextView.setText(display_TextView.getText() + "4");
					 }
			     }
			  });
			 
			 five_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
					 if (display_TextView.getText().length()<5){
						 display_TextView.setText(display_TextView.getText() + "5");
					 }
			     }
			  });
			 
			 six_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
					 if (display_TextView.getText().length()<5){
						 display_TextView.setText(display_TextView.getText() + "6");
					 }
			     }
			  });
			 
			 seven_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
					 if (display_TextView.getText().length()<5){
						 display_TextView.setText(display_TextView.getText() + "7");
					 }
			     }
			  });
			 
			 eight_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
					 if (display_TextView.getText().length()<5){
						 display_TextView.setText(display_TextView.getText() + "8");
					 }
			     }
			  });
			 
			 nine_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
					 if (display_TextView.getText().length()<5){
						 display_TextView.setText(display_TextView.getText() + "9");
					 }
			     }
			  });
			 
			 autoDestruction_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
					if (arg1 == true){
						CheckBox tractorRay_CheckBox = (CheckBox) dialogPanel.findViewById(R.id.tractorRay_CheckBox );
						tractorRay_CheckBox.setChecked(false);
					}
					
				}
			 });
			 
			 tractorRay_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					

					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1 == true){
							CheckBox autoDestruction_CheckBox = (CheckBox) dialogPanel.findViewById(R.id.autoDestruction_CheckBox );
							autoDestruction_CheckBox.setChecked(false);
						}
						
					}
				 });
			 
			 enter_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
					 TextView actor_TextView = (TextView) dialogPanel.findViewById(R.id.actor_TextView);
					 CheckBox autoDestruction_CheckBox = (CheckBox) dialogPanel.findViewById(R.id.autoDestruction_CheckBox);
					 CheckBox tractorRay_CheckBox = (CheckBox) dialogPanel.findViewById(R.id.tractorRay_CheckBox );
					 
					 if (autoDestruction_CheckBox.isChecked()){
						 if ((display_TextView.getText().toString().trim().equals(CODE_AUTODESTROYED_ON))) {
							 autodestroyEnabled = true;
							 actor_TextView.setText(SystemMessage.MESSAGE_AUTODESTRUCTION_CODE_VALID);
							 autoDestruction_CheckBox.setEnabled(false);
							 autoDestruction_CheckBox.setChecked(false);
							 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED1, Color.WHITE);
							 if (part2_Adventure.isActorHere(SystemMessage.ACTOR_PACA)){
								 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED5, Color.WHITE);
								 if (part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI)){
									 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED4, Color.WHITE);
								 }
							 } else {
								 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED2, Color.WHITE);
								 if (part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI)){
									 addLine (SystemMessage.MESSAGE_STAR_IS_DESTROYED3, Color.WHITE);
								 }	 
							 }
						 } else {
							 actor_TextView.setText(SystemMessage.MESSAGE_AUTODESTRUCTION_CODE_INVALID);
						 }
					 }
					 if (tractorRay_CheckBox.isChecked()){
						 if ((display_TextView.getText().toString().trim().equals(CODE_TRACTORRAY_OFF))){
							 tractorRayEnabled = false;
							 actor_TextView.setText(SystemMessage.MESSAGE_TRACTORRAY_CODE_VALID);
							 tractorRay_CheckBox.setEnabled(false);
							 tractorRay_CheckBox.setChecked(false);
							 addLine (SystemMessage.MESSAGE_TRACTORRAY_IS_DISABLED1, Color.WHITE);
							 if (part2_Adventure.isActorHere(SystemMessage.ACTOR_PACA)){
								 addLine (SystemMessage.MESSAGE_TRACTORRAY_IS_DISABLED2, Color.WHITE);
								 if (part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI)){
									 addLine (SystemMessage.MESSAGE_TRACTORRAY_IS_DISABLED3, Color.WHITE);
								 }
							 } else {								 
								 if (part2_Adventure.isActorHere(SystemMessage.ACTOR_OBI)){
									 addLine (SystemMessage.MESSAGE_TRACTORRAY_IS_DISABLED4, Color.WHITE);
								 }	 
							 }
						 } else {
							 actor_TextView.setText(SystemMessage.MESSAGE_TRACTORRAY_CODE_INVALID);
						 }
					 }
			     }
			  });
			 
			 clear_Button.setOnClickListener(new OnClickListener() {
				 public void onClick(View v) {
					 TextView display_TextView = (TextView) dialogPanel.findViewById(R.id.display_TextView);
					
					 display_TextView.setText(" ");
			     }
			  });
			 
			 
			 return dialogPanel;
			 
		 } else {
			 return null;
		 }
			 
		 
		 		 
		 
		}

	 private void makeAction(int actionType){
		 TextView aux_TextView = null;
		 
		 if (actionType == 1){
			 aux_TextView = (TextView) dialogObjects.findViewById(R.id.object1_TextView);
		 } else if (actionType == 2){
			 aux_TextView = (TextView) dialogObjects.findViewById(R.id.object2_TextView);
	 	 } else if (actionType == 3){
	 		aux_TextView = (TextView) dialogObjects.findViewById(R.id.object3_TextView);
		 } else if (actionType == 4){
			 aux_TextView = (TextView) dialogObjects.findViewById(R.id.object4_TextView);
		 } else if (actionType == 5){
			 aux_TextView = (TextView) dialogObjects.findViewById(R.id.object5_TextView);
		 } else if (actionType == 6){
			 aux_TextView = (TextView) dialogObjects.findViewById(R.id.object6_TextView);
		 }
			 
		 if (globalInformation.getCaughtSelected()==true){			
			 part2_Adventure.increaseActionsCounter();
			 addLine(part2_Adventure.catchObject(aux_TextView.getText().toString()), Color.WHITE);				
		 	 showCaughtObjects();
		 } else if (globalInformation.getLeaveSelected()==true){
			 part2_Adventure.increaseActionsCounter();
			 addLine(part2_Adventure.leaveObject(aux_TextView.getText().toString()), Color.WHITE);
			 showLeaveObjects();
		 } else if (globalInformation.getGiveSelected()==true){
			 //object_ImageView = (ImageView) dialogGive.findViewById(R.id.object_ImageView);
			 //object_TextView = (TextView) dialogGive.findViewById(R.id.object_TextView);
			 globalInformation.setObjectToGive(part2_Adventure.getObject(aux_TextView.getText().toString()));
			 showDialog(DIALOG_GIVE_ID); 
		 }
			 
		 
	 }
	 
	 private void makeGive(int actionType){
		 TextView aux_TextView = null;
		 
		 if (actionType == 1){
			 aux_TextView = (TextView) dialogGive.findViewById(R.id.actor1_TextView);
		 } else if (actionType == 2){
			 aux_TextView = (TextView) dialogGive.findViewById(R.id.actor2_TextView);
	 	 } else if (actionType == 3){
	 		aux_TextView = (TextView) dialogGive.findViewById(R.id.actor3_TextView);
		 } else if (actionType == 4){
			 aux_TextView = (TextView) dialogGive.findViewById(R.id.actor4_TextView);
		 } else if (actionType == 5){
			 aux_TextView = (TextView) dialogGive.findViewById(R.id.actor5_TextView);
		 } else if (actionType == 6){
			 aux_TextView = (TextView) dialogGive.findViewById(R.id.actor6_TextView);
		 }
		 
		 
		 String sayReturn = part2_Adventure.give(aux_TextView.getText().toString(), globalInformation.getObjectToGive().getName());
		 if (((aux_TextView.getText().toString().equals(SystemMessage.ACTOR_C2P2) || 
		      (aux_TextView.getText().toString().equals(SystemMessage.ACTOR_R3D2)))) && 
		     (globalInformation.getObjectToGive().getName().equals(SystemMessage.OBJECT_OILCAN))){
			 part2_Adventure.activateFollowingBehaviour(SystemMessage.ACTOR_C2P2);	 
				int resId = getResources().getIdentifier("part1_location_004b", "drawable", getPackageName());
					ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
					location_ImageView.setImageResource(resId);
		 }
		 
		 if ((aux_TextView.getText().toString().equals(SystemMessage.ACTOR_JUAN)) && 
			     (globalInformation.getObjectToGive().getName().equals(SystemMessage.OBJECT_CREDITS))){
				 part2_Adventure.activateFollowingBehaviour(SystemMessage.ACTOR_JUAN);	 
		}
		
		 addLine(sayReturn, Color.WHITE);

		 dialogGive.cancel();
		 dialogObjects.cancel();
	 }
	 
	 
	 private void makeTalk(int actionType){
		 TextView aux_TextView = null;
		 
		 if (actionType == 1){
			 aux_TextView = (TextView) dialogTalk.findViewById(R.id.actor1_TextView);
		 } else if (actionType == 2){
			 aux_TextView = (TextView) dialogTalk.findViewById(R.id.actor2_TextView);
	 	 } else if (actionType == 3){
	 		aux_TextView = (TextView) dialogTalk.findViewById(R.id.actor3_TextView);
		 } else if (actionType == 4){
			 aux_TextView = (TextView) dialogTalk.findViewById(R.id.actor4_TextView);
		 } else if (actionType == 5){
			 aux_TextView = (TextView) dialogTalk.findViewById(R.id.actor5_TextView);
		 } else if (actionType == 6){
			 aux_TextView = (TextView) dialogTalk.findViewById(R.id.actor6_TextView);
		 }
		 
		 globalInformation.setActorToTalk(part2_Adventure.getActor(aux_TextView.getText().toString()));
		 showDialog(DIALOG_TALK_ROOM_ID);
		 
	 }
	 
	 
	 
	 
	 private void showGiveObjects(){
		 setAllImageObjectsInvisible();
		 
		 globalInformation.setGiveSelected(true);
		 globalInformation.setCaughtSelected(false);
		 globalInformation.setLeaveSelected(false);
		 
		 ImageView give_ImageView = (ImageView) dialogObjects.findViewById(R.id.give_ImageView);
		 give_ImageView.setImageResource(R.drawable.give_select_text);
		 
		 ImageView caught_ImageView = (ImageView) dialogObjects.findViewById(R.id.caught_ImageView);
		 caught_ImageView.setImageResource(R.drawable.caught_text);
		 
		 ImageView leave_ImageView = (ImageView) dialogObjects.findViewById(R.id.leave_ImageView);
		 leave_ImageView.setImageResource(R.drawable.leave_text);
		 
		 if (part2_Adventure.getTotalObjectsTaken() == 0) {
			 
			 ImageView object1_ImageView = (ImageView) dialogObjects.findViewById(R.id.object1_ImageView);
			 object1_ImageView.setVisibility(View.VISIBLE);
			 object1_ImageView.setEnabled(false);
			 object1_ImageView.setImageResource(R.drawable.inventary_empty);
			 TextView object1_TextView = (TextView) dialogObjects.findViewById(R.id.object1_TextView);
			 object1_TextView.setVisibility(View.VISIBLE);
			 object1_TextView.setText(SystemMessage.MESSAGE_INVENTARY_EMPTY);
			 
			 
		 } else {			 
			 HashMap<String, Object> objects = part2_Adventure.getObjectsTaken();			 
		     Iterator<?> it = objects.entrySet().iterator();
		     int pos = 1;
		     while (it.hasNext()){ // Recorremos la lista de Actores
				Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
				AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
				
				int resId = getResources().getIdentifier(object.getImage(), "drawable", getPackageName());
				if (pos==1){
					ImageView object1_ImageView = (ImageView) dialogObjects.findViewById(R.id.object1_ImageView);
					object1_ImageView.setVisibility(View.VISIBLE);
					object1_ImageView.setEnabled(true);					
					TextView object1_TextView = (TextView) dialogObjects.findViewById(R.id.object1_TextView);
					object1_TextView.setVisibility(View.VISIBLE);					
					 
					object1_ImageView.setImageResource(resId);
					object1_TextView.setText(object.getName());	
					object1_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {		
							 makeAction(1);							 
					     }
					});
										
				} else if (pos==2) {
					ImageView object2_ImageView = (ImageView) dialogObjects.findViewById(R.id.object2_ImageView);
					object2_ImageView.setVisibility(View.VISIBLE);					
					TextView object2_TextView = (TextView) dialogObjects.findViewById(R.id.object2_TextView);
					object2_TextView.setVisibility(View.VISIBLE);					
					 
					object2_ImageView.setImageResource(resId);
					object2_TextView.setText(object.getName());
					
					object2_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(2);						
					     }
					});
				} else if (pos==3) {
					ImageView object3_ImageView = (ImageView) dialogObjects.findViewById(R.id.object3_ImageView);
					object3_ImageView.setVisibility(View.VISIBLE);
					object3_ImageView.setImageResource(R.drawable.inventary_empty);
					TextView object3_TextView = (TextView) dialogObjects.findViewById(R.id.object3_TextView);
					object3_TextView.setVisibility(View.VISIBLE);
					object3_TextView.setText(SystemMessage.MESSAGE_INVENTARY_EMPTY);
					 
					object3_ImageView.setImageResource(resId);
					object3_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(3);
						 }
					});
					object3_TextView.setText(object.getName());
				} else if (pos==4) {
					ImageView object4_ImageView = (ImageView) dialogObjects.findViewById(R.id.object4_ImageView);
					object4_ImageView.setVisibility(View.VISIBLE);
					object4_ImageView.setImageResource(R.drawable.inventary_empty);
					TextView object4_TextView = (TextView) dialogObjects.findViewById(R.id.object4_TextView);
					object4_TextView.setVisibility(View.VISIBLE);
					object4_TextView.setText(SystemMessage.MESSAGE_INVENTARY_EMPTY);
					 
					object4_ImageView.setImageResource(resId);
					object4_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(4);							 
					     }
					});
					object4_TextView.setText(object.getName());
				} else if (pos==5) {
					ImageView object5_ImageView = (ImageView) dialogObjects.findViewById(R.id.object5_ImageView);
					object5_ImageView.setVisibility(View.VISIBLE);
					object5_ImageView.setImageResource(R.drawable.inventary_empty);
					TextView object5_TextView = (TextView) dialogObjects.findViewById(R.id.object5_TextView);
					object5_TextView.setVisibility(View.VISIBLE);
					object5_TextView.setText(SystemMessage.MESSAGE_INVENTARY_EMPTY);
					 
					object5_ImageView.setImageResource(resId);
					object5_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(5);							 
					     }
					});
					object5_TextView.setText(object.getName());
				} else if (pos==6) {
					ImageView object6_ImageView = (ImageView) dialogObjects.findViewById(R.id.object6_ImageView);
					object6_ImageView.setVisibility(View.VISIBLE);
					object6_ImageView.setImageResource(R.drawable.inventary_empty);
					TextView object6_TextView = (TextView) dialogObjects.findViewById(R.id.object6_TextView);
					object6_TextView.setVisibility(View.VISIBLE);
					object6_TextView.setText(SystemMessage.MESSAGE_INVENTARY_EMPTY);
					 
					object6_ImageView.setImageResource(resId);
					object6_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(6);							 
					     }
					});
					object6_TextView.setText(object.getName());
				}
				pos++;	
		    }
		 }
	 }
	 
	 private void showCaughtObjects(){
		 setAllImageObjectsInvisible();
		 
		 
		 globalInformation.setGiveSelected(false);
		 globalInformation.setCaughtSelected(true);
		 globalInformation.setLeaveSelected(false);
		 
		 ImageView give_ImageView = (ImageView) dialogObjects.findViewById(R.id.give_ImageView);
		 give_ImageView.setImageResource(R.drawable.give_text);
		 
		 ImageView caught_ImageView = (ImageView) dialogObjects.findViewById(R.id.caught_ImageView);
		 caught_ImageView.setImageResource(R.drawable.caught_select_text);
		 
		 ImageView leave_ImageView = (ImageView) dialogObjects.findViewById(R.id.leave_ImageView);
		 leave_ImageView.setImageResource(R.drawable.leave_text);
		 
		 if (part2_Adventure.getTotalLocationObjects() == 0) {
			 
			 ImageView object1_ImageView = (ImageView) dialogObjects.findViewById(R.id.object1_ImageView);
			 object1_ImageView.setVisibility(View.VISIBLE);
			 object1_ImageView.setEnabled(false);
			 object1_ImageView.setImageResource(R.drawable.inventary_empty);
			 TextView object1_TextView = (TextView) dialogObjects.findViewById(R.id.object1_TextView);
			 object1_TextView.setVisibility(View.VISIBLE);
			 object1_TextView.setText(SystemMessage.MESSAGE_INVENTARY_EMPTY);
		 } else {			 
			 HashMap<String, Object> objects = part2_Adventure.getLocationObjects();			 
		     Iterator<?> it = objects.entrySet().iterator();
		     int pos = 1;
		     while (it.hasNext()){ // Recorremos la lista de Actores
				Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
				AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
				int resId = getResources().getIdentifier(object.getImage(), "drawable", getPackageName());
				if (pos==1){
					ImageView object1_ImageView = (ImageView) dialogObjects.findViewById(R.id.object1_ImageView);
					object1_ImageView.setVisibility(View.VISIBLE);
					object1_ImageView.setEnabled(true);					
					TextView object1_TextView = (TextView) dialogObjects.findViewById(R.id.object1_TextView);
					object1_TextView.setVisibility(View.VISIBLE);					
					 
					object1_ImageView.setImageResource(resId);
					object1_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(1);			    	
					     }
					  });					
					object1_TextView.setText(object.getName());		
								 
					 
					
				} else if (pos==2) {
					ImageView object2_ImageView = (ImageView) dialogObjects.findViewById(R.id.object2_ImageView);
					object2_ImageView.setVisibility(View.VISIBLE);
					TextView object2_TextView = (TextView) dialogObjects.findViewById(R.id.object2_TextView);
					object2_TextView.setVisibility(View.VISIBLE);					
					 
					object2_ImageView.setImageResource(resId);
					object2_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(2);			    	
					     }
					  });
					object2_TextView.setText(object.getName());
					
				} else if (pos==3) {
					ImageView object3_ImageView = (ImageView) dialogObjects.findViewById(R.id.object3_ImageView);
					object3_ImageView.setVisibility(View.VISIBLE);
					TextView object3_TextView = (TextView) dialogObjects.findViewById(R.id.object3_TextView);
					object3_TextView.setVisibility(View.VISIBLE);
					 
					object3_ImageView.setImageResource(resId);
					object3_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(3);			    	
					     }
					  });
					
					object3_TextView.setText(object.getName());
				} else if (pos==4) {
					ImageView object4_ImageView = (ImageView) dialogObjects.findViewById(R.id.object4_ImageView);
					object4_ImageView.setVisibility(View.VISIBLE);
					TextView object4_TextView = (TextView) dialogObjects.findViewById(R.id.object4_TextView);
					object4_TextView.setVisibility(View.VISIBLE);
					 
					object4_ImageView.setImageResource(resId);
					object4_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(4);			    	
					     }
					  });
					
					object4_TextView.setText(object.getName());
				} else if (pos==5) {
					ImageView object5_ImageView = (ImageView) dialogObjects.findViewById(R.id.object5_ImageView);
					object5_ImageView.setVisibility(View.VISIBLE);
					TextView object5_TextView = (TextView) dialogObjects.findViewById(R.id.object5_TextView);
					object5_TextView.setVisibility(View.VISIBLE);
					 
					object5_ImageView.setImageResource(resId);
					object5_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(5);			    	
					     }
					  });
					
					object5_TextView.setText(object.getName());
				} else if (pos==6) {
					ImageView object6_ImageView = (ImageView) dialogObjects.findViewById(R.id.object6_ImageView);
					object6_ImageView.setVisibility(View.VISIBLE);
					TextView object6_TextView = (TextView) dialogObjects.findViewById(R.id.object6_TextView);
					object6_TextView.setVisibility(View.VISIBLE);
					 
					object6_ImageView.setImageResource(resId);
					object6_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(6);			    	
					     }
					  });
					
					object6_TextView.setText(object.getName());
				}
				pos++;	
		     }
		 }
	 }
	 
	 private void showLeaveObjects(){
		 setAllImageObjectsInvisible();
		 
		 globalInformation.setGiveSelected(false);
		 globalInformation.setCaughtSelected(false);
		 globalInformation.setLeaveSelected(true);
		 
		 ImageView give_ImageView = (ImageView) dialogObjects.findViewById(R.id.give_ImageView);
		 give_ImageView.setImageResource(R.drawable.give_text);
		 
		 ImageView caught_ImageView = (ImageView) dialogObjects.findViewById(R.id.caught_ImageView);
		 caught_ImageView.setImageResource(R.drawable.caught_text);
		 
		 ImageView leave_ImageView = (ImageView) dialogObjects.findViewById(R.id.leave_ImageView);
		 leave_ImageView.setImageResource(R.drawable.leave_select_text);
		 
		 if (part2_Adventure.getTotalObjectsTaken() == 0) {
			 
			 ImageView object1_ImageView = (ImageView) dialogObjects.findViewById(R.id.object1_ImageView);
			 object1_ImageView.setVisibility(View.VISIBLE);
			 object1_ImageView.setEnabled(false);
			 object1_ImageView.setImageResource(R.drawable.inventary_empty);
			 TextView object1_TextView = (TextView) dialogObjects.findViewById(R.id.object1_TextView);
			 object1_TextView.setVisibility(View.VISIBLE);
			 object1_TextView.setText(SystemMessage.MESSAGE_INVENTARY_EMPTY);
			 
		 } else {			 
			 HashMap<String, Object> objects = part2_Adventure.getObjectsTaken();			 
		     Iterator<?> it = objects.entrySet().iterator();
		     int pos = 1;
		     while (it.hasNext()){ // Recorremos la lista de Actores
				Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
				AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
				int resId = getResources().getIdentifier(object.getImage(), "drawable", getPackageName());
				if (pos==1){
					ImageView object1_ImageView = (ImageView) dialogObjects.findViewById(R.id.object1_ImageView);
					object1_ImageView.setVisibility(View.VISIBLE);
					object1_ImageView.setEnabled(true);
					TextView object1_TextView = (TextView) dialogObjects.findViewById(R.id.object1_TextView);
					object1_TextView.setVisibility(View.VISIBLE);
					 
					object1_ImageView.setImageResource(resId);
					object1_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(1);			    	
					     }
					  });
					object1_TextView.setText(object.getName());						
				} else if (pos==2) {
					ImageView object2_ImageView = (ImageView) dialogObjects.findViewById(R.id.object2_ImageView);
					object2_ImageView.setVisibility(View.VISIBLE);
					TextView object2_TextView = (TextView) dialogObjects.findViewById(R.id.object2_TextView);
					object2_TextView.setVisibility(View.VISIBLE);
					 
					object2_ImageView.setImageResource(resId);
					object2_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(2);			    	
					     }
					  });
					object2_TextView.setText(object.getName());
				} else if (pos==3) {
					ImageView object3_ImageView = (ImageView) dialogObjects.findViewById(R.id.object3_ImageView);
					object3_ImageView.setVisibility(View.VISIBLE);
					TextView object3_TextView = (TextView) dialogObjects.findViewById(R.id.object3_TextView);
					object3_TextView.setVisibility(View.VISIBLE);
					 
					object3_ImageView.setImageResource(resId);
					object3_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(3);			    	
					     }
					  });
					object3_TextView.setText(object.getName());
				} else if (pos==4) {
					ImageView object4_ImageView = (ImageView) dialogObjects.findViewById(R.id.object4_ImageView);
					object4_ImageView.setVisibility(View.VISIBLE);
					TextView object4_TextView = (TextView) dialogObjects.findViewById(R.id.object4_TextView);
					object4_TextView.setVisibility(View.VISIBLE);
					 
					object4_ImageView.setImageResource(resId);
					object4_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(4);			    	
					     }
					  });
					object4_TextView.setText(object.getName());
				} else if (pos==5) {
					ImageView object5_ImageView = (ImageView) dialogObjects.findViewById(R.id.object5_ImageView);
					object5_ImageView.setVisibility(View.VISIBLE);
					TextView object5_TextView = (TextView) dialogObjects.findViewById(R.id.object5_TextView);
					object5_TextView.setVisibility(View.VISIBLE);
					 
					object5_ImageView.setImageResource(resId);
					object5_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(5);			    	
					     }
					  });
					object5_TextView.setText(object.getName());
				} else if (pos==6) {
					ImageView object6_ImageView = (ImageView) dialogObjects.findViewById(R.id.object6_ImageView);
					object6_ImageView.setVisibility(View.VISIBLE);
					TextView object6_TextView = (TextView) dialogObjects.findViewById(R.id.object6_TextView);
					object6_TextView.setVisibility(View.VISIBLE);
					 
					object6_ImageView.setImageResource(resId);
					object6_ImageView.setOnClickListener(new OnClickListener() {
						 public void onClick(View v) {
							 makeAction(6);			    	
					     }
					  });
					object6_TextView.setText(object.getName());
				}
				pos++;	
		    }
			 
		 }  
		 
	 }
	 
	 private void setAllImageObjectsInvisible(){
		 
		 ImageView give_ImageView = (ImageView) dialogObjects.findViewById(R.id.give_ImageView);
		 give_ImageView.setImageResource(R.drawable.give_text);
		 
		 ImageView caught_ImageView = (ImageView) dialogObjects.findViewById(R.id.caught_ImageView);
		 caught_ImageView.setImageResource(R.drawable.caught_text);
		 
		 ImageView leave_ImageView = (ImageView) dialogObjects.findViewById(R.id.leave_ImageView);
		 leave_ImageView.setImageResource(R.drawable.leave_text);
		 
		 ImageView object1_ImageView = (ImageView) dialogObjects.findViewById(R.id.object1_ImageView);
		 object1_ImageView.setVisibility(View.INVISIBLE);
		 TextView object1_TextView = (TextView) dialogObjects.findViewById(R.id.object1_TextView);
		 object1_TextView.setVisibility(View.INVISIBLE);
		 
		 ImageView object2_ImageView = (ImageView) dialogObjects.findViewById(R.id.object2_ImageView);
		 object2_ImageView.setVisibility(View.INVISIBLE);
		 TextView object2_TextView = (TextView) dialogObjects.findViewById(R.id.object2_TextView);
		 object2_TextView.setVisibility(View.INVISIBLE);
		 
		 ImageView object3_ImageView = (ImageView) dialogObjects.findViewById(R.id.object3_ImageView);
		 object3_ImageView.setVisibility(View.INVISIBLE);
		 TextView object3_TextView = (TextView) dialogObjects.findViewById(R.id.object3_TextView);
		 object3_TextView.setVisibility(View.INVISIBLE);
		 
		 ImageView object4_ImageView = (ImageView) dialogObjects.findViewById(R.id.object4_ImageView);
		 object4_ImageView.setVisibility(View.INVISIBLE);
		 TextView object4_TextView = (TextView) dialogObjects.findViewById(R.id.object4_TextView);
		 object4_TextView.setVisibility(View.INVISIBLE);
		 
		 ImageView object5_ImageView = (ImageView) dialogObjects.findViewById(R.id.object5_ImageView);
		 object5_ImageView.setVisibility(View.INVISIBLE);
		 TextView object5_TextView = (TextView) dialogObjects.findViewById(R.id.object5_TextView);
		 object5_TextView.setVisibility(View.INVISIBLE);
		 
		 ImageView object6_ImageView = (ImageView) dialogObjects.findViewById(R.id.object6_ImageView);
		 object6_ImageView.setVisibility(View.INVISIBLE);
		 TextView object6_TextView = (TextView) dialogObjects.findViewById(R.id.object6_TextView);
		 object6_TextView.setVisibility(View.INVISIBLE);
		 
	 }
	 
	 private void setAllImageActorsInvisible(Dialog dialog){
		 
		 ImageView actor1_ImageView = (ImageView) dialog.findViewById(R.id.actor1_ImageView);
		 actor1_ImageView.setVisibility(View.INVISIBLE);
		 TextView actor1_TextView = (TextView) dialog.findViewById(R.id.actor1_TextView);
		 actor1_TextView.setVisibility(View.INVISIBLE);
		 
		 ImageView actor2_ImageView = (ImageView) dialog.findViewById(R.id.actor2_ImageView);
		 actor2_ImageView.setVisibility(View.INVISIBLE);
		 TextView actor2_TextView = (TextView) dialog.findViewById(R.id.actor2_TextView);
		 actor2_TextView.setVisibility(View.INVISIBLE);
		 
		 ImageView actor3_ImageView = (ImageView) dialog.findViewById(R.id.actor3_ImageView);
		 actor3_ImageView.setVisibility(View.INVISIBLE);
		 TextView actor3_TextView = (TextView) dialog.findViewById(R.id.actor3_TextView);
		 actor3_TextView.setVisibility(View.INVISIBLE);
		 
		 ImageView actor4_ImageView = (ImageView) dialog.findViewById(R.id.actor4_ImageView);
		 actor4_ImageView.setVisibility(View.INVISIBLE);
		 TextView actor4_TextView = (TextView) dialog.findViewById(R.id.actor4_TextView);
		 actor4_TextView.setVisibility(View.INVISIBLE);
		 
		 ImageView actor5_ImageView = (ImageView) dialog.findViewById(R.id.actor5_ImageView);
		 actor5_ImageView.setVisibility(View.INVISIBLE);
		 TextView actor5_TextView = (TextView) dialog.findViewById(R.id.actor5_TextView);
		 actor5_TextView.setVisibility(View.INVISIBLE);
		 
		 ImageView actor6_ImageView = (ImageView) dialog.findViewById(R.id.actor6_ImageView);
		 actor6_ImageView.setVisibility(View.INVISIBLE);
		 TextView actor6_TextView = (TextView) dialog.findViewById(R.id.actor6_TextView);
		 actor6_TextView.setVisibility(View.INVISIBLE);
		 
	 }

	 
	 private void showActors(Dialog dialog, int dialogType){
		 setAllImageActorsInvisible(dialog);
		 
		 if (part2_Adventure.getTotalLocationActors() == 0) {
			 
			 ImageView object1_ImageView = (ImageView) dialog.findViewById(R.id.actor1_ImageView);
			 object1_ImageView.setVisibility(View.VISIBLE);
			 object1_ImageView.setEnabled(false);
			 object1_ImageView.setImageResource(R.drawable.inventary_empty);
			 TextView object1_TextView = (TextView) dialog.findViewById(R.id.actor1_TextView);
			 object1_TextView.setVisibility(View.VISIBLE);
			 object1_TextView.setText(SystemMessage.MESSAGE_NO_ACTORS_HERE);
			 
			 
		 } else {		
			 
			 HashMap<String, Actor> actors = part2_Adventure.getLocationActors() ;			 
		     Iterator<?> it = actors.entrySet().iterator();
		     int pos = 1;
		     while (it.hasNext()){ // Recorremos la lista de Actores
				Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
				Actor actor = (Actor)e.getValue();
				int resId = getResources().getIdentifier(actor.getImage(), "drawable", getPackageName());
				if (pos==1){
					ImageView actor1_ImageView = (ImageView) dialog.findViewById(R.id.actor1_ImageView);
					actor1_ImageView.setVisibility(View.VISIBLE);
					actor1_ImageView.setEnabled(true);
					TextView actor1_TextView = (TextView) dialog.findViewById(R.id.actor1_TextView);
					actor1_TextView.setVisibility(View.VISIBLE);
					 
					actor1_ImageView.setImageResource(resId);
					actor1_TextView.setText(actor.getName());	
					if (dialogType == DIALOG_GIVE_ID){
						actor1_ImageView.setOnClickListener(new OnClickListener() {
							 public void onClick(View v) {
								 makeGive(1);			    	
						     }
						  });
					} else if (dialogType == DIALOG_TALK_ID){
						actor1_ImageView.setOnClickListener(new OnClickListener() {
							 public void onClick(View v) {
								 makeTalk(1);			    	
						     }
						  });
					}
				} else if (pos==2) {
					ImageView actor2_ImageView = (ImageView) dialog.findViewById(R.id.actor2_ImageView);
					actor2_ImageView.setVisibility(View.VISIBLE);
					TextView actor2_TextView = (TextView) dialog.findViewById(R.id.actor2_TextView);
					actor2_TextView.setVisibility(View.VISIBLE);
					 
					actor2_ImageView.setImageResource(resId);
					actor2_TextView.setText(actor.getName());
					if (dialogType == DIALOG_GIVE_ID){
						actor2_ImageView.setOnClickListener(new OnClickListener() {
							 public void onClick(View v) {
								 makeGive(2);			    	
						     }
						  });
					} else if (dialogType == DIALOG_TALK_ID){
						actor2_ImageView.setOnClickListener(new OnClickListener() {
							 public void onClick(View v) {
								 makeTalk(2);			    	
						     }
						  });
					}
				} else if (pos==3) {
					ImageView actor3_ImageView = (ImageView) dialog.findViewById(R.id.actor3_ImageView);
					actor3_ImageView.setVisibility(View.VISIBLE);
					TextView actor3_TextView = (TextView) dialog.findViewById(R.id.actor3_TextView);
					actor3_TextView.setVisibility(View.VISIBLE);
					 
					actor3_ImageView.setImageResource(resId);
					actor3_TextView.setText(actor.getName());
					if (dialogType == DIALOG_GIVE_ID){
						actor3_ImageView.setOnClickListener(new OnClickListener() {
							 public void onClick(View v) {
								 makeGive(3);			    	
						     }
						  });
					} else if (dialogType == DIALOG_TALK_ID){
						actor3_ImageView.setOnClickListener(new OnClickListener() {
							 public void onClick(View v) {
								 makeTalk(3);			    	
						     }
						  });
					}
				} else if (pos==4) {
					ImageView actor4_ImageView = (ImageView) dialog.findViewById(R.id.actor4_ImageView);
					actor4_ImageView.setVisibility(View.VISIBLE);
					TextView actor4_TextView = (TextView) dialog.findViewById(R.id.actor4_TextView);
					actor4_TextView.setVisibility(View.VISIBLE);
					 
					actor4_ImageView.setImageResource(resId);
					actor4_TextView.setText(actor.getName());
					if (dialogType == DIALOG_GIVE_ID){
						actor4_ImageView.setOnClickListener(new OnClickListener() {
							 public void onClick(View v) {
								 makeGive(4);			    	
						     }
						  });
					} else if (dialogType == DIALOG_TALK_ID){
						actor4_ImageView.setOnClickListener(new OnClickListener() {
							 public void onClick(View v) {
								 makeTalk(4);			    	
						     }
						  });
					}
				} else if (pos==5) {
					ImageView actor5_ImageView = (ImageView) dialog.findViewById(R.id.actor5_ImageView);
					actor5_ImageView.setVisibility(View.VISIBLE);
					TextView actor5_TextView = (TextView) dialog.findViewById(R.id.actor5_TextView);
					actor5_TextView.setVisibility(View.VISIBLE);
					 
					actor5_ImageView.setImageResource(resId);
					actor5_TextView.setText(actor.getName());
					if (dialogType == DIALOG_GIVE_ID){
						actor5_ImageView.setOnClickListener(new OnClickListener() {
							 public void onClick(View v) {
								 makeGive(5);			    	
						     }
						  });
					} else if (dialogType == DIALOG_TALK_ID){
						actor5_ImageView.setOnClickListener(new OnClickListener() {
							 public void onClick(View v) {
								 makeTalk(5);			    	
						     }
						  });
					}
				} else if (pos==6) {
					ImageView actor6_ImageView = (ImageView) dialog.findViewById(R.id.actor6_ImageView);
					actor6_ImageView.setVisibility(View.VISIBLE);
					TextView actor6_TextView = (TextView) dialog.findViewById(R.id.actor6_TextView);
					actor6_TextView.setVisibility(View.VISIBLE);
					 
					actor6_ImageView.setImageResource(resId);
					actor6_TextView.setText(actor.getName());
					if (dialogType == DIALOG_GIVE_ID){
						actor6_ImageView.setOnClickListener(new OnClickListener() {
							 public void onClick(View v) {
								 makeGive(6);			    	
						     }
						  });
					} else if (dialogType == DIALOG_TALK_ID){
						actor6_ImageView.setOnClickListener(new OnClickListener() {
							 public void onClick(View v) {
								 makeTalk(6);			    	
						     }
						  });
					}
				}
				pos++;	
		    }
		 }
	 }
	 
	 private void startAnimatingMainPicture() {
		 Animation move_from_left = AnimationUtils.loadAnimation(this, R.anim.move_from_left);
		 
		 ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);		 
		 		 
		 location_ImageView.startAnimation(move_from_left);
		 
		 
	 }
	 

	  public void say(String text2say){    	    	
	    	talker.speak(text2say, TextToSpeech.QUEUE_ADD, null);    		
	    	
	    }

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onDestroy() {
		// Don't forget to shutdown tts!
		if (talker != null) {
			talker.stop();
			talker.shutdown();
		}
		super.onDestroy();
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
