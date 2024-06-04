package es.zgames.laguerradelasvajillasfree.activities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import es.zgames.laguerradelasvajillasfree.R;
import es.zgames.textadventures.engine.*;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LaGuerradelasVajillasPart1LocationActivity<V, K> extends LaGuerradelasVajillasActivity implements OnInitListener {
	static final int DIALOG_END_ID = 1;
	static final int DIALOG_WIN_ID = 2;
	static final int DIALOG_CAUGHT_ID = 3;
	static final int DIALOG_TALK_ID = 4;
	static final int DIALOG_DISK_ID = 5;
	static final int DIALOG_GIVE_ID = 6;
	static final int DIALOG_TALK_ROOM_ID = 7;
	
	private AdventureLocation currentLocation = null;
	private AdventureLocation previousLocation = null;
	private Adventure<?, ?> part1_Adventure = null;
	private Parser parser = null;
	private int textSize = 15;
	
	private Dialog dialogObjects = null;
	private Dialog dialogMessages = null;
	private Dialog dialogLoadSave = null;
	private Dialog dialogTalk = null;
	private Dialog dialogTalkRoom = null;
	private Dialog dialogGive = null;

	private boolean robotsFuera = false;
	private boolean is_plectrum_cut = false;
	private boolean doorsLocked = false;
	private boolean engineIsBroken = true;
	private boolean hyperspaceIsBroken = true;
	private boolean inTheSpace = false;
		
	private int chrisCounter = 0;
	private int engineHit = 0;
	private int hits = 0;
	private int merodeadores = 0;
	private int soldiers = 0;
	private int juan = 0;
	private boolean languageSupported = true;
	
	private TextToSpeech talker;
	
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
				
		 part1_Adventure = Adventure.getInstance();
		 
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
				startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasPart1MapActivity.class));				 				

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
				startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasInventoryActivity.class));				 				

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
					part1_Adventure.increaseActionsCounter();
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
				part1_Adventure.increaseActionsCounter();
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
				part1_Adventure.increaseActionsCounter();
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
					part1_Adventure.increaseActionsCounter();
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
					 part1_Adventure.increaseActionsCounter();
					 userCommand_EditText.append("Examina ");
					 
				}
			});
			
			//////////////////////////////////////////////////////////////////////////////////////
			// Hand ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton caught_ImageButton = (ImageButton) findViewById(R.id.caught_ImageButton);
			caught_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				part1_Adventure.increaseActionsCounter();
				showDialog(DIALOG_CAUGHT_ID);
				
			}
			});
			
			//////////////////////////////////////////////////////////////////////////////////////
			// Talk ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton talk_ImageButton = (ImageButton) findViewById(R.id.talk_ImageButton);
			talk_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				part1_Adventure.increaseActionsCounter();
				showDialog(DIALOG_TALK_ID);
			
			}
			});

			//////////////////////////////////////////////////////////////////////////////////////
			// Disk ImagenButton Behaviour
			//////////////////////////////////////////////////////////////////////////////////////
			
			ImageButton disk_ImageButton = (ImageButton) findViewById(R.id.disk_ImageButton);
			disk_ImageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				part1_Adventure.increaseActionsCounter();
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
					addLine(part1_Adventure.getCurrentLocationExits(), Color.WHITE);				 				
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
				say(part1_Adventure.getCurrentLocation().getDescription());	 				
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
						 part1_Adventure.increaseActionsCounter();
						
						 action_type = parser.parse(parser.getLogicSentence(userSentence, index));
					 						 
						 switch (action_type) {
					 		case ParserCodes.NULL_ACTION: {		
					 			addLine (SystemMessage.MESSAGE_NULL_SENTENCE, Color.WHITE);					
					 			break;
					 		}
					 		case ParserCodes.SWIN_ACTION: {
					 			addLine (SystemMessage.MESSAGE_SWIN_IN_TIME, Color.WHITE);					 			
					 			break;
					 		}
					 		case ParserCodes.MOVE_ACTION: {
					 			executeMoveActions();					 			
					 			break;				 		
					 		}
					 		case ParserCodes.JUMP_ACTION: {
					 			if ((currentLocation.getId().equals("022")) &&
					 				(parser.getActionObject1().equals("HIPERESPACIO"))){
					 				if (part1_Adventure.isActorHere(SystemMessage.ACTOR_R3D2) == false){
					 					addLine (SystemMessage.MESSAGE_HYPERSPACE_IMPOSSIBLE, Color.WHITE);
					 				} else {
					 					if (hyperspaceIsBroken == true){
					 						addLine (SystemMessage.MESSAGE_HYPERSPACE_IS_BROKEN1, Color.WHITE);					 						
					 						addLine (SystemMessage.MESSAGE_HYPERSPACE_IS_BROKEN2, Color.WHITE);
					 						addLine (SystemMessage.MESSAGE_HYPERSPACE_IS_BROKEN3, Color.WHITE);
					 						addLine (SystemMessage.MESSAGE_HYPERSPACE_IS_BROKEN4, Color.WHITE);
					 					} else {
					 						showDialog(DIALOG_WIN_ID);						 					
					 					}
					 				}
					 			} else {
					 				addLine (SystemMessage.MESSAGE_FIND_SPACESHIP2, Color.WHITE);
					 			}
					 			break;				 						 		
					 		}
					 		case ParserCodes.ACTIVATE_ACTION:{
					 			if ((currentLocation.getId().equals("026")) && 
					 				(parser.getActionObject1().equals(SystemMessage.MESSAGE_TURBOLASER))) {
					 				addLine (SystemMessage.MESSAGE_NOTHING_TO_ACTIVATE2, Color.WHITE);
					 			} else {
					 				addLine (SystemMessage.MESSAGE_NOTHING_TO_ACTIVATE, Color.WHITE);
					 			}
					 			break;
					 		}					 		
					 		case ParserCodes.FIX_ACTION: {
					 			if (currentLocation.getId().equals("023")){
					 				if (inTheSpace == false) {
					 					addLine (SystemMessage.MESSAGE_NOTHING_TO_FIX, Color.WHITE);
					 				} else {
					 					if (parser.getActionObject1().equals("HIPERESPACIO")){
					 						if (hyperspaceIsBroken == true){
					 							addLine (SystemMessage.MESSAGE_HYPERSPACE_FIXED1, Color.WHITE);
					 							addLine (SystemMessage.MESSAGE_HYPERSPACE_FIXED2, Color.WHITE);
					 							hyperspaceIsBroken = false;
					 						} else {
					 							addLine (SystemMessage.MESSAGE_NOTHING_TO_FIX2, Color.WHITE);
					 						}
					 					} else {
					 						addLine (SystemMessage.MESSAGE_NOTHING_TO_FIX, Color.WHITE);
					 					}
					 				}
					 				
					 			} else {
					 				addLine (SystemMessage.MESSAGE_NOTHING_TO_FIX, Color.WHITE);
					 			}
					 			break;
					 		}
					 		case ParserCodes.PISS_ACTION: {
					 			addLine(SystemMessage.MESSAGE_NOT_BE_A_PIG,Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.TAKEOFF_ACTION: {
					 			if (currentLocation.getId().equals("022")){
					 				if (part1_Adventure.isActorHere(SystemMessage.ACTOR_R3D2) == false){
					 					addLine (SystemMessage.MESSAGE_TAKEOFF_IMPOSSIBLE, Color.WHITE);
					 				} else {
					 					if (engineIsBroken == true){
					 						addLine (SystemMessage.MESSAGE_ENGINE_IS_BROKEN1, Color.WHITE);					 						
					 						addLine (SystemMessage.MESSAGE_ENGINE_IS_BROKEN3, Color.WHITE);
					 					} else {
					 						addLine (SystemMessage.MESSAGE_HALCON_IS_FLYING, Color.WHITE);
					 						int resId = getResources().getIdentifier("part1_location_022b", "drawable", getPackageName());
						 					ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
						 					location_ImageView.setImageResource(resId);
						 					AdventureLocation location = part1_Adventure.getLocation("022");
						 					location.setImage("part1_location_022b");
						 					inTheSpace=true;
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
					 		case ParserCodes.SHOOT_ACTION:{
					 			if (part1_Adventure.isObjectTaken(SystemMessage.OBJECT_GUN)){
					 				if (part1_Adventure.isObjectInsideOther(SystemMessage.OBJECT_MUNITION)){
					 					if (parser.getActionObject1().equals("")){
					 						addLine(SystemMessage.MESSAGE_SHOOT_NOTHING,Color.WHITE);
					 					} else {
					 						if  ((parser.getActionObject1().equals(SystemMessage.ACTOR_DEPENDIENTE)) && 
						 							 (part1_Adventure.isActorHere(SystemMessage.ACTOR_DEPENDIENTE))){
					 							part1_Adventure.setActorVisible(SystemMessage.ACTOR_DEPENDIENTE, AdventureCodes.OBJECT_DESTROY);
					 							addLine(SystemMessage.MESSAGE_SHOT_DEPENDIENTE, Color.WHITE);
					 						} else  if  (((parser.getActionObject1().equals(SystemMessage.ACTOR_OBI)) ||
					 							  (parser.getActionObject1().equals("CABALLERO")) ||
					 							  (parser.getActionObject1().equals("MAESTRO")))&& 
					 							 (part1_Adventure.isActorHere(SystemMessage.ACTOR_OBI))){
					 							 // Estas muerto
					 							if (languageSupported){
					 								talker.stop();
					 							}
					 							 part1_Adventure.setCurrentLocation("100");
					 							 startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 						} else if  ((parser.getActionObject1().equals(SystemMessage.ACTOR_BLUE_BUG)) && 
							 							 (part1_Adventure.isActorHere(SystemMessage.ACTOR_BLUE_BUG))){
					 							part1_Adventure.setActorVisible(SystemMessage.ACTOR_BLUE_BUG, AdventureCodes.OBJECT_DESTROY);					 							
					 							part1_Adventure.getLocation("012").setImage("part1_location_012");
					 							addLine(SystemMessage.MESSAGE_SHOT_BLUE_BUG, Color.WHITE);
					 						} else if ((parser.getActionObject1().equals(SystemMessage.ACTOR_JAMONEANO)) && 
						 							(part1_Adventure.isActorHere(SystemMessage.ACTOR_JAMONEANO))){
					 							 // Estas muerto
					 							if (languageSupported){
					 								talker.stop();
					 							}
					 							 part1_Adventure.setCurrentLocation("103");
					 							 startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 						} else if ((parser.getActionObject1().equals(SystemMessage.ACTOR_JUAN))&& 
				 									   (part1_Adventure.isActorHere(SystemMessage.ACTOR_JUAN))){
					 							if (languageSupported){
					 								talker.stop();
					 							} 
					 							part1_Adventure.setCurrentLocation("101");
					 							 startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 						} else if (((parser.getActionObject1().equals(SystemMessage.ACTOR_C2P2)) || 
					 								    (parser.getActionObject1().equals(SystemMessage.ACTOR_R3D2)) || 
					 								    (parser.getActionObject1().equals("ROBOT")) || 
					 								    (parser.getActionObject1().equals("ROBOTS"))) && 
					 								   (part1_Adventure.isActorHere(SystemMessage.ACTOR_C2P2))){
					 							if (languageSupported){
					 								talker.stop();
					 							} 
					 							part1_Adventure.setCurrentLocation("102");
					 							 startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
					 						} else if (((parser.getActionObject1().equals(SystemMessage.ACTOR_SAPOIDE)) || 
					 								    (parser.getActionObject1().equals("SAPO"))&& 
		 									   		   (part1_Adventure.isActorHere(SystemMessage.ACTOR_SAPOIDE)))){
					 							addLine(SystemMessage.MESSAGE_SHOOT_SAPOIDE, Color.WHITE);
					 						} else if ((parser.getActionObject1().equals("MERODEADORES")) || 
					 								   (parser.getActionObject1().equals("MERODEADOR"))&& 
		 									   		    ((currentLocation.getId().equals("004")) || 
		 									   		    (currentLocation.getId().equals("008")))) {
					 							merodeadores=0;
					 							addLine(SystemMessage.MESSAGE_MERODEADORES_FEAR_SHOOT, Color.WHITE);
					 						} else if (((parser.getActionObject1().equals(SystemMessage.ACTOR_CHAVALA)) ||
					 								   (parser.getActionObject1().equals("BESUGINA")) ||
					 								   (parser.getActionObject1().equals("CHRIS")))	&& 
					 									   (part1_Adventure.isActorHere(SystemMessage.ACTOR_CHAVALA))){
					 							addLine(SystemMessage.MESSAGE_SHOOT_CHRIS_NOT_POSSIBLE, Color.WHITE);
					 						} else if ((parser.getActionObject1().equals("SOLDADOS")) ||
					 								    (parser.getActionObject1().equals("SOLDADO")) ||
					 								    (parser.getActionObject1().equals("TROPAS"))) {
					 							if ((currentLocation.getId().equals("016")) || (soldiers >= 3)){
					 								if (languageSupported){
					 									talker.stop();
					 								}
					 								part1_Adventure.setCurrentLocation("105");
						 							 startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));						 				
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
					 		case ParserCodes.SAVE_ACTION:{
					 			globalInformation.setRobotsFuera(robotsFuera);
					 		    globalInformation.setIsPlectrumCut(is_plectrum_cut);
					 		    globalInformation.setDoorsLocked(doorsLocked);
					 		    globalInformation.setEngineIsBroken(engineIsBroken);
					 		    globalInformation.setHyperspaceIsBroken(hyperspaceIsBroken);
					 		    globalInformation.setInTheSpace(inTheSpace);	  		
					 		    globalInformation.setChrisCounter(chrisCounter);
					 		    globalInformation.setEngineHit(engineHit);
					 		    globalInformation.setHits(hits);
					 		    globalInformation.setMerodeadores(merodeadores);
					 		    globalInformation.setSoldiers(soldiers);
					 		    globalInformation.setJuan(juan);
					 			part1_Adventure.savePart1();
					 			addLine(SystemMessage.MESSAGE_GAME_SAVED,Color.GREEN);
					 			break;
					 		}
					 		case ParserCodes.LOAD_ACTION:{
					 			if (part1_Adventure.loadPart1()==true){
					 				robotsFuera = globalInformation.getRobotsFuera();
					 				is_plectrum_cut = globalInformation.getIsPlectrumCut();
					 				doorsLocked = globalInformation.getDoorsLocked();
					 				engineIsBroken = globalInformation.getEngineIsBroken();
					 				hyperspaceIsBroken = globalInformation.getHyperspaceIsBroken();
					 				inTheSpace = globalInformation.getInTheSpace();	  		
					 				chrisCounter = globalInformation.getChrisCounter();
					 				engineHit = globalInformation.getEngineHit();
					 				hits = globalInformation.getHits();
					 				merodeadores = globalInformation.getMerodeadores();
					 				soldiers = globalInformation.getSoldiers();
					 				juan = globalInformation.getJuan();
					 				if ((part1_Adventure.getCurrentLocation().getId().equals("014")) &&
					 					(!part1_Adventure.isActorHere(SystemMessage.ACTOR_SAPOIDE))){
				 						part1_Adventure.getLocation("014").setImage("part1_location_014b");
				 					} 
					 				updateScreen();
					 				
					 				addLine(SystemMessage.MESSAGE_GAME_LOADED, Color.GREEN);
					 			} else {
					 				addLine(SystemMessage.MESSAGE_GAME_NOT_LOADED, Color.GREEN);
					 			}
					 			addLine("", Color.WHITE);
					 			
					 			break;
					 		}
					 		case ParserCodes.BREAK_ACTION: {
					 			addLine (SystemMessage.MESSAGE_DONT_BREAK_PLEASE, Color.WHITE);
					 			break;
					 		}
					 		case ParserCodes.BADWORDS_ACTION: {
					 			if (part1_Adventure.getBadWordsCounter() == 0){
					 				addLine(SystemMessage.MESSAGE_BADWORDS_WARNING, Color.WHITE);
					 				part1_Adventure.increaseBadWordsCounter();
					 			} else {
					 				addLine(SystemMessage.MESSAGE_BADWORDS_END, Color.WHITE);	
					 			}
					 			break;
					 		}
					 		case ParserCodes.DESCRIBE_ACTION: {
					 			String object = parser.getActionObject1();
					 			
					 			if (((currentLocation.getId().equals("008")) || 
					 				(currentLocation.getId().equals("004"))) && 
					 			    (merodeadores > 0) && 
					 			    ((object.equals ("MERODEADORES") ||
					 			     (object.equals ("MERODEADOR"))))) {
					 				addLine ("Que feos son los merodeadores", Color.WHITE);
					 				
					 			} else {
					 				addLine(part1_Adventure.getDescription(object), Color.WHITE);	
					 			}
					 			if ((part1_Adventure.isActorHere(SystemMessage.ACTOR_R3D2)) &&
					 			    ((object.equals(SystemMessage.ACTOR_R3D2)) ||
					 			     (object.equals(SystemMessage.MESSAGE_ANDROID)) ||
					 			     (object.equals(SystemMessage.MESSAGE_ANDROIDS) ||
					 			     (object.equals(SystemMessage.MESSAGE_TAMBOR))))
					 			   ){					 				
					 				part1_Adventure.getActor("R3D2").setLocationText(SystemMessage.MESSAGE_R3D2_NEW_LOCATION_TEXT);
					 			}
					 			

					 			if ((currentLocation.getId().equals("016"))) {
					 				 
					 				 if (soldiers == 3){
					 					addLine (SystemMessage.MESSAGE_SOLDIERS_ARE_HERE2, Color.WHITE);					 
					 				 } 
					 				 soldiers++;		 				 
					 			 }
					 			
					 			 
					 			break;		
					 		}
					 		case ParserCodes.EMPTY_ACTION:{
					 			addLine(part1_Adventure.emptyObject(parser.getActionObject1()),Color.WHITE);
					 			break;
					 		}
					 		case ParserCodes.INVENTARY_ACTION: {
					 			addLine(part1_Adventure.getInventary(), Color.WHITE);
					 			addLine(SystemMessage.MESSAGE_TOTAL_OBJECTS + part1_Adventure.getTotalObjectsTaken(), Color.WHITE);
					 			break;								
					 		}
					 		case ParserCodes.EXITS_ACTION: {	
					 			addLine(part1_Adventure.getCurrentLocationExits(), Color.WHITE);					
					 			break;								
					 		}
					 		case ParserCodes.LOOK_ACTION: {	
					 			updateScreen();				
					 			break;								
					 		}
					 		case ParserCodes.CATCH_ACTION: {	
					 			if ((currentLocation.getId().equals("006")) &&
					 				(is_plectrum_cut == false)){
					 				if (parser.getActionObject1().equals(SystemMessage.OBJECT_PLECTRUM)){
					 					if (part1_Adventure.isObjectTaken(SystemMessage.OBJECT_SELFPEELER)){
					 						addLine(SystemMessage.MESSAGE_USING_SELFPEELER_TAKE_PLECTRUM, Color.WHITE);
					 						is_plectrum_cut = true;
					 						AdventureObject<?, ?> object = part1_Adventure.getObject(SystemMessage.OBJECT_PLECTRUM);
					 						object.setLocationText("");
					 					} else {
					 						addLine(SystemMessage.MESSAGE_NOT_USING_SELFPEELER_TAKE_PLECTRUM, Color.WHITE);
					 					}
					 				} else {
					 					addLine(part1_Adventure.catchObject(parser.getActionObject1()), Color.WHITE);
									}
					 			} else{
					 				addLine(part1_Adventure.catchObject(parser.getActionObject1()), Color.WHITE);
					 			}
					 			break;								
					 		}
					 		case ParserCodes.LEAVE_ACTION: {	
					 			addLine(part1_Adventure.leaveObject(parser.getActionObject1()), Color.WHITE);
					 			if (currentLocation.getId().equals("025")){ // Estoy en lo alto del �rbol.
					 														// Habr�a que mejorar esto en un posible motor
					 				
					 				addLine(part1_Adventure.fallDownObject(parser.getActionObject1()), Color.WHITE);
					 			}
					 			break;								
					 		}
					 		case ParserCodes.SHAKE_ACTION: {					 			
					 			addLine(SystemMessage.MESSAGE_CANT_DO_THAT, Color.WHITE);
					 			break;
					 		}
					 		case ParserCodes.OPEN_ACTION: {	
					 			addLine(part1_Adventure.open(parser.getActionObject1()), Color.WHITE);
					 			
					 			break;								
					 		}
					 		
					 		case ParserCodes.END_ACTION: {	
					 			showDialog(DIALOG_END_ID);
					 			break;
					 		}	
					 		case ParserCodes.FILL_ACTION: {	
					 			addLine(part1_Adventure.fillObject(parser.getActionObject1()),Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.DRINK_ACTION: {	
					 			addLine(part1_Adventure.drink(parser.getActionObject1()),Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.PUT_ACTION: {	
					 			addLine(part1_Adventure.put(parser.getActionObject1(),parser.getActionObject2()),Color.WHITE);
					 			if ((parser.getActionObject1().equals(SystemMessage.OBJECT_MUNITION)) &&
					 			    (parser.getActionObject2().equals(SystemMessage.OBJECT_GUN)) && 
					 			    (part1_Adventure.getObject(SystemMessage.OBJECT_MUNITION).getLocationId().equals(AdventureCodes.OBJECT_IN_OTHER_OBJECT)) &&
					 			    (part1_Adventure.getObject(SystemMessage.OBJECT_GUN).getLocationId().equals(AdventureCodes.OBJECT_TAKEN))
					 			    ){
					 				part1_Adventure.getObject(SystemMessage.OBJECT_GUN).setStatus(SystemMessage.MESSAGE_GUN_LOADED);
					 			}
					 			break;
					 		}	
					 		case ParserCodes.QUIT_ACTION: {	
					 			addLine(part1_Adventure.quit(parser.getActionObject1(), parser.getActionObject2()),Color.WHITE);
					 			if ((parser.getActionObject1().equals(SystemMessage.OBJECT_MUNITION)) &&
						 			    (parser.getActionObject2().equals(SystemMessage.OBJECT_GUN)) && 
						 			    (part1_Adventure.getObject(SystemMessage.OBJECT_MUNITION).getLocationId().equals(AdventureCodes.OBJECT_TAKEN)) &&
						 			    (part1_Adventure.getObject(SystemMessage.OBJECT_GUN).getLocationId().equals(AdventureCodes.OBJECT_TAKEN))
						 			    ){
						 				part1_Adventure.getObject(SystemMessage.OBJECT_GUN).setStatus(SystemMessage.MESSAGE_GUN_UNLOADED);
						 			}
					 			if (currentLocation.getNeedLight().equals(AdventureCodes.TRUE)){
					 				updateScreen();
					 			}
					 			break;
					 		}	
					 		case ParserCodes.HELP_ACTION: {	
					 			addLine (part1_Adventure.getCurrentLocation().getHelp(), Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.TURNON_ACTION: {	
					 			addLine (part1_Adventure.turnOn(parser.getActionObject1()), Color.WHITE);
					 			if (currentLocation.getNeedLight().equals(AdventureCodes.TRUE)){
					 				updateScreen();
					 			}
					 			break;
					 		}	
					 		case ParserCodes.TURNOFF_ACTION: {	
					 			addLine (part1_Adventure.turnOff(parser.getActionObject1()), Color.WHITE);
					 			if (currentLocation.getNeedLight().equals(AdventureCodes.TRUE)){
					 				updateScreen();
					 			}
					 			break;
					 		}	
					 		case ParserCodes.KILL_ACTION: {	
					 			if ((currentLocation.getId().equals("025")) && 
					 			    ((parser.getActionActor().equals("MOTOR")) || 
					 			     (parser.getActionActor().equals("MOTORES")))
					 			    ){
					 				if (engineHit==0){
					 					addLine (SystemMessage.MESSAGE_ENGINE_IS_WORKING_NOW, Color.WHITE);
					 					engineHit++;
					 				} else {
					 					addLine (SystemMessage.MESSAGE_ENGINE_IS_BROKEN2, Color.WHITE);
					 				}
					 				engineIsBroken = false;
					 				
					 			} else {
						 			addLine (part1_Adventure.kill(parser.getActionActor()), Color.WHITE);
						 			if (currentLocation.getId().equals("014")){
						 				if (!part1_Adventure.isActorHere(SystemMessage.ACTOR_SAPOIDE)){					 			
						 					part1_Adventure.setActorVisible(SystemMessage.ACTOR_JUAN, "014");
						 					part1_Adventure.setActorVisible(SystemMessage.ACTOR_CHEQUEVACA, "014");
						 					AdventureLocation location = part1_Adventure.getLocation("014");
						 					location.setImage("part1_location_014b");
						 					int resId = getResources().getIdentifier("part1_location_014b", "drawable", getPackageName());
						 					ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
						 					location_ImageView.setImageResource(resId);	
						 				}
						 			}
					 			}
					 			break;
					 		}	
					 		case ParserCodes.AD_ACTION: {	
					 			addLine (part1_Adventure.ad(parser.getActionActor()), Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.PUSH_ACTION: {	
					 			addLine (part1_Adventure.push(parser.getActionActor()), Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.EAT_ACTION: {	
					 			addLine (part1_Adventure.eat(parser.getActionObject1()), Color.WHITE);
					 			break;
					 		}	
					 		case ParserCodes.SAY_ACTION: {	
					 			//Una acci�n de decir puede llevar asociado un comportamiento en el actor
					 			String sayReturn = part1_Adventure.say(parser.getActionActor(), parser.getActionObject1());
					 			if (part1_Adventure.isBehaviourId(sayReturn)){
					 				Behaviour behaviour = part1_Adventure.getBehaviour(sayReturn);
					 				if (!behaviour.getMovePlayerTo().equals("")){ // Tengo que mover al usuario a una localidad
					 					part1_Adventure.setCurrentLocation(behaviour.getMovePlayerTo());
					 					if (part1_Adventure.getCurrentLocation().getType().equals(AdventureCodes.LOCATION_TYPE_DEAD)){
					 						if (languageSupported){
					 							talker.stop();
					 						}
							 				startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));				 				
							 			} else {
							 				updateScreen();
							 			}
							 			break;	
					 				}
					 				if (behaviour.getType().equals(AdventureCodes.BEHAVIOR_TYPE_FOLLOW)){
					 					addLine(part1_Adventure.activateFollowingBehaviour(parser.getActionActor()), Color.WHITE);
					 				}
					 				
					 				if (behaviour.getObjectsReturned().size()>0){ // El comportamiento devuelve objetos
					 					HashMap<String, String> objectsReturned = behaviour.getObjectsReturned();
					 					Iterator<?> it = objectsReturned.entrySet().iterator();					 					
					 					while (it.hasNext()){ // Recorremos la lista de Actores
					 						Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
					 						String object = (String)e.getKey();
					 						String message = (String)e.getValue();
					 						part1_Adventure.setObjectVisible(object, currentLocation.getId());
					 						addLine(message,Color.WHITE);
					 					}
					 							
					 				}
					 			} else {
					 				addLine(sayReturn, Color.WHITE);
					 			}
					 			break;
					 		}	
					 		case ParserCodes.FUCK_ACTION: {
					 			if (part1_Adventure.getTotalLocationActors()>0){
					 				addLine(SystemMessage.MESSAGE_NOT_FUCK_IS_POSSIBLE, Color.WHITE);
					 			} else {
					 				addLine(SystemMessage.MESSAGE_NOT_FUCK_IS_POSSIBLE2, Color.WHITE);
					 			}
					 			break;
					 		}
					 		case ParserCodes.GIVE_ACTION: {	
					 			//Una acci�n de decir puede llevar asociado un comportamiento en el actor
					 			String sayReturn = part1_Adventure.give(parser.getActionActor(), parser.getActionObject1());
					 			if (part1_Adventure.isBehaviourId(sayReturn)){
					 				Behaviour behaviour = part1_Adventure.getBehaviour(sayReturn);
					 				
					 			} else {
					 				if (((parser.getActionActor().equals(SystemMessage.ACTOR_C2P2) || 
					 					      (parser.getActionActor().equals(SystemMessage.ACTOR_R3D2)))) && 
					 					     (parser.getActionObject1().equals(SystemMessage.OBJECT_OILCAN))){
					 						 part1_Adventure.activateFollowingBehaviour(SystemMessage.ACTOR_C2P2);
					 						int resId = getResources().getIdentifier("part1_location_004b", "drawable", getPackageName());
						 					ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
						 					location_ImageView.setImageResource(resId);
						 					
					 				}
					 				if ((parser.getActionActor().equals(SystemMessage.ACTOR_JUAN)) &&					 					       
					 					(parser.getActionObject1().equals(SystemMessage.OBJECT_CREDITS))){
					 						 part1_Adventure.activateFollowingBehaviour(SystemMessage.ACTOR_JUAN);
					 				}
					 					
					 				addLine(sayReturn, Color.WHITE);
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
					 			addLine(part1_Adventure.catchObject(parser.getActionObject1()), Color.WHITE);						
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
		if (currentLocation.getId().equals("015")) {
			if ((part1_Adventure.isActorHere(SystemMessage.ACTOR_DEPENDIENTE)) && 
			    (parser.getActionVerb().equals("S"))){
				addLine(SystemMessage.MESSAGE_DEPENDIENTE_SAID_YOU_CANT_PASS, Color.WHITE);
			} else {
				int result = part1_Adventure.move(parser.getActionVerb());
				 
				// Si hemos llegado a una localidad de muerte nos movemos a ella.
				if (part1_Adventure.getCurrentLocation().getType().equals(AdventureCodes.LOCATION_TYPE_DEAD)){
					if (languageSupported){
						talker.stop();
					}
					startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));				 				
				} else if (result == AdventureCodes.MOVE_ACTION_FAIL) {					 			
					addLine(SystemMessage.MESSAGE_WRONG_DIRECTION, Color.WHITE);				
				} else if (result == AdventureCodes.MOVE_ACTION_FAIL_BY_OBJECT) {
					addLine(SystemMessage.MESSAGE_NEED_OBJECTS_FOR_THAT_DIRECTION, Color.WHITE);
				} else {				
					updateScreen();				
				}
			}
			
		} else if (currentLocation.getId().equals("019")) {
			if ((!part1_Adventure.isActorHere(SystemMessage.ACTOR_JUAN)) && 
			    (parser.getActionVerb().equals("E"))){
				addLine(SystemMessage.MESSAGE_BESUGINA_SAID_YOU_CANT_PASS, Color.WHITE);
			} else {
				int result = part1_Adventure.move(parser.getActionVerb());
				 
				// Si hemos llegado a una localidad de muerte nos movemos a ella.
				if (part1_Adventure.getCurrentLocation().getType().equals(AdventureCodes.LOCATION_TYPE_DEAD)){
					if (languageSupported){
						talker.stop();
					}
					startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));				 				
				} else if (result == AdventureCodes.MOVE_ACTION_FAIL) {					 			
					addLine(SystemMessage.MESSAGE_WRONG_DIRECTION, Color.WHITE);				
				} else if (result == AdventureCodes.MOVE_ACTION_FAIL_BY_OBJECT) {
					addLine(SystemMessage.MESSAGE_NEED_OBJECTS_FOR_THAT_DIRECTION, Color.WHITE);
				} else {				
					updateScreen();				
				}
			}
			
		} else if (currentLocation.getId().equals("021")) { 
			if ((doorsLocked == true) && 	
				(parser.getActionVerb().equals("S"))){
				addLine(SystemMessage.MESSAGE_IMPOSSIBLE_LEAVE_SPACESHIP, Color.WHITE);
			} else {				
				int result = part1_Adventure.move(parser.getActionVerb());
				 
				// Si hemos llegado a una localidad de muerte nos movemos a ella.
				if (part1_Adventure.getCurrentLocation().getType().equals(AdventureCodes.LOCATION_TYPE_DEAD)){
					if (languageSupported){
						talker.stop();
					}
					startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));				 				
				} else if (result == AdventureCodes.MOVE_ACTION_FAIL) {					 			
					addLine(SystemMessage.MESSAGE_WRONG_DIRECTION, Color.WHITE);				
				} else if (result == AdventureCodes.MOVE_ACTION_FAIL_BY_OBJECT) {
					addLine(SystemMessage.MESSAGE_NEED_OBJECTS_FOR_THAT_DIRECTION, Color.WHITE);
				} else {				
					updateScreen();				
				}			
			}
		
		} else {
			int result = part1_Adventure.move(parser.getActionVerb());
			 
			// Si hemos llegado a una localidad de muerte nos movemos a ella.
			if (part1_Adventure.getCurrentLocation().getType().equals(AdventureCodes.LOCATION_TYPE_DEAD)){
				if (languageSupported){
					talker.stop();
				}
				startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));				 				
			} else if (result == AdventureCodes.MOVE_ACTION_FAIL) {					 			
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
		 actionsValue_TextView.setText(String.valueOf(part1_Adventure.getActionsCounter()));
		 
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
		 currentLocation = part1_Adventure.getCurrentLocation();
		 ImageButton north_ImageButton = null; 
		 ImageButton south_ImageButton = null;
		 ImageButton east_ImageButton = null;
		 ImageButton west_ImageButton = null;
		 if (globalInformation.getAdvancedLocation().equals(AdventureCodes.TRUE)){
			 north_ImageButton = (ImageButton) findViewById(R.id.north_ImageButton);
			 north_ImageButton.setImageResource(R.drawable.ic_north);
			 south_ImageButton = (ImageButton) findViewById(R.id.south_ImageButton);
			 south_ImageButton.setImageResource(R.drawable.ic_south);
			 east_ImageButton = (ImageButton) findViewById(R.id.east_ImageButton);
			 east_ImageButton.setImageResource(R.drawable.ic_east);
			 west_ImageButton = (ImageButton) findViewById(R.id.west_ImageButton);
			 west_ImageButton.setImageResource(R.drawable.ic_west);
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
		 actionsValue_TextView.setText(String.valueOf(part1_Adventure.getActionsCounter()));
		 	
		 
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
				 if (part1_Adventure.isObjectWithLightTakenAndTurnOn().equals(AdventureCodes.FALSE)){
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
				 if (part1_Adventure.isObjectWithLightTakenAndTurnOn().equals(AdventureCodes.FALSE)){
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
		 if ( ((part1_Adventure.getCurrentLocation().getNeedLight().equals(AdventureCodes.TRUE)) && 
			   (part1_Adventure.isObjectWithLightTakenAndTurnOn().equals(AdventureCodes.TRUE))) || 
			  (part1_Adventure.getCurrentLocation().getNeedLight().equals(AdventureCodes.FALSE))){ 
			 if (part1_Adventure.getTotalLocationObjects() > 0)  {
				 if (!part1_Adventure.getCurrentLocationObjects().equals("")){
					 addLine(part1_Adventure.getCurrentLocationObjects(), Color.GREEN);
				 }
			 }			
		 }

		 // Si los actores tienen comportamientos ejecutarlos			 
		 addLine(part1_Adventure.executeActorsBehaviours(), Color.WHITE);

		 // Si hay actores en la localidad, muestralos a continuaci�n de los objetos
		 addLine(part1_Adventure.getCurrentLocationActors(), Color.RED);

		 if ( (part1_Adventure.isActorHere(SystemMessage.ACTOR_C2P2)) && 
				  (part1_Adventure.isActorHere(SystemMessage.ACTOR_OBI)) &&
				  ( part1_Adventure.getBehaviour("004").getActive().equals(AdventureCodes.FALSE))
				) {
				 addLine (SystemMessage.MESSAGE_C2P2_OBI, Color.WHITE);
		 }

		 
		 if (currentLocation.getId().equals("002")){
			 if ((part1_Adventure.isActorHere(SystemMessage.ACTOR_C2P2))) {
				 Actor c2p2 = part1_Adventure.getActor(SystemMessage.ACTOR_C2P2);
				 addLine (c2p2.getRandomMessage(), Color.WHITE);				 
			 }
		 }
		 
		 if ((currentLocation.getId().equals("004")) || 
			 (currentLocation.getId().equals("008"))) {
			 if (part1_Adventure.isActorHere(SystemMessage.ACTOR_OBI)){
				 addLine (SystemMessage.MESSAGE_MERODEADORES_FEAR_OBI, Color.WHITE); 
			 } else {
				 if (merodeadores >= 1){
					 addLine (SystemMessage.MESSAGE_MERODEADORES_ARE_HERE4, Color.WHITE);
				 } 
				 if (merodeadores == 2){
					 addLine (SystemMessage.MESSAGE_MERODEADORES_ARE_HERE1, Color.WHITE);					 
				 } else if (merodeadores == 4) {
					 addLine (SystemMessage.MESSAGE_MERODEADORES_ARE_HERE2, Color.WHITE);
				 } else if (merodeadores == 5) {
					 addLine (SystemMessage.MESSAGE_MERODEADORES_ARE_HERE3, Color.WHITE);
				 } else if (merodeadores == 7){
					 // Estas muerto
					 if (languageSupported){
							talker.stop();
						}
					 part1_Adventure.setCurrentLocation("099");
					 startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
				 }
				 merodeadores++;
			 }
			 
		 }
		 
		 if (currentLocation.getId().equals("005")){
			 Behaviour obi = part1_Adventure.getBehaviour("004");
			 if (obi.getActive().equals(AdventureCodes.FALSE)){			 				 
				 addLine (SystemMessage.MESSAGE_OBI_QUESTION, Color.WHITE);
			 } 
		 }
		 
		 if (currentLocation.getId().equals("007")){
			 if ((part1_Adventure.isActorHere(SystemMessage.ACTOR_C2P2))) {
				 Actor c2p2 = part1_Adventure.getActor(SystemMessage.ACTOR_C2P2);
				 addLine (c2p2.getRandomMessage(), Color.WHITE);				 
			 }
		 }
		 
		 
		
		 if (currentLocation.getId().equals("008")){
			 if ((part1_Adventure.isActorHere(SystemMessage.ACTOR_C2P2))) {
				 Actor c2p2 = part1_Adventure.getActor(SystemMessage.ACTOR_C2P2);
				 addLine (c2p2.getRandomMessage(), Color.WHITE);				 
			 }			 
		 }
		
		 if (currentLocation.getId().equals("009")){
			 if ((part1_Adventure.isActorHere(SystemMessage.ACTOR_JUAN))) {
				 part1_Adventure.getActor(SystemMessage.ACTOR_JUAN).setLocationId("014");
				 part1_Adventure.getActor(SystemMessage.ACTOR_CHEQUEVACA).setLocationId("014");;
				 part1_Adventure.getBehaviour(part1_Adventure.getActor(SystemMessage.ACTOR_JUAN).getFollowingBehaviour()).setActive(AdventureCodes.FALSE);
				 addLine (SystemMessage.MESSAGE_JUAN_NOT_LIKE_DESERT, Color.WHITE);
				 juan++;
				 
			 }			 
		 }
		

		 // Comportamiento particular para la localidad 010
		 if (currentLocation.getId().equals("010")){
			 if ((part1_Adventure.isActorHere(SystemMessage.ACTOR_OBI)) && 
			     (!part1_Adventure.isActorHere(SystemMessage.ACTOR_JUAN))) {
				 if (juan==0){
					 addLine (SystemMessage.MESSAGE_OBI_SAID_WE_NEED_A_PILOT, Color.WHITE);
				 } else {
					 addLine (SystemMessage.MESSAGE_OBI_SAID_WE_NEED_A_JUAN, Color.WHITE);
				 }
			 }
			 
			 if ((part1_Adventure.isActorHere(SystemMessage.ACTOR_C2P2))) {
				 Actor c2p2 = part1_Adventure.getActor(SystemMessage.ACTOR_C2P2);
				 addLine (c2p2.getRandomMessage(), Color.WHITE);				 
			 }			
		 }
		 
		 
		 if ((currentLocation.getId().equals("011")) || 
		     (currentLocation.getId().equals("012"))){
			 robotsFuera = false;
			 part1_Adventure.activateFollowingBehaviour(SystemMessage.ACTOR_C2P2);
			 part1_Adventure.activateFollowingBehaviour(SystemMessage.ACTOR_R3D2);

		 }
		 
		 if (currentLocation.getId().equals("012")){
			 if ((part1_Adventure.isActorHere(SystemMessage.ACTOR_BLUE_BUG))) {
				part1_Adventure.getLocation("012").setImage("part1_location_012b"); 
			 } else {
				part1_Adventure.getLocation("012").setImage("part1_location_012");
			 }
		 }
		
		 
		 // Comportamiento particular para la localidad 13 - Los Robots esperan fuera
		 if (currentLocation.getId().equals("013")) { 
				 
				if  (part1_Adventure.isActorHere(SystemMessage.ACTOR_C2P2)){
						part1_Adventure.desactivateFollowingBehaviour(SystemMessage.ACTOR_C2P2);
						part1_Adventure.setActorVisible(SystemMessage.ACTOR_R3D2, "012");
						part1_Adventure.setActorVisible(SystemMessage.ACTOR_C2P2, "012");
				
				
					if (robotsFuera == false){
					 addLine (SystemMessage.MESSAGE_C2P2_SAID_WAIT_OUTSIDE, Color.WHITE);				 				 
					 robotsFuera = true;
					}	
				}
		 }

		 // Comportamiento particular para la localidad 14 - Los Robots esperan fuera
		 if (currentLocation.getId().equals("014")) { 
			 if (juan>0){
					part1_Adventure.getBehaviour(part1_Adventure.getActor(SystemMessage.ACTOR_JUAN).getFollowingBehaviour()).setActive(AdventureCodes.TRUE);
					addLine (SystemMessage.MESSAGE_JUAN_IS_WAITING, Color.WHITE);
					
				}
			
				 
				if  (part1_Adventure.isActorHere(SystemMessage.ACTOR_C2P2)){
						part1_Adventure.desactivateFollowingBehaviour(SystemMessage.ACTOR_C2P2);
						part1_Adventure.setActorVisible(SystemMessage.ACTOR_R3D2, "011");
						part1_Adventure.setActorVisible(SystemMessage.ACTOR_C2P2, "011");
				
					
					if (robotsFuera == false){
					 addLine (SystemMessage.MESSAGE_C2P2_SAID_WAIT_OUTSIDE, Color.WHITE);				 				 
					 robotsFuera = true;
					}	
				}
		 }

		 if ((soldiers > 3) && (soldiers <=7)) {
			 addLine (SystemMessage.MESSAGE_SOLDIERS_ARE_HERE1, Color.WHITE);
			 soldiers++;	
		 } else if (soldiers > 7) {
			 // Estas muerto
			 if (languageSupported){
					talker.stop();
				}
			 part1_Adventure.setCurrentLocation("104");
			 startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));
	 	 }
		 
		 // Comportamiento particular para la localidad 16 - Soldados
		 if ((currentLocation.getId().equals("016"))) {
			 
			 if (soldiers == 3){
				addLine (SystemMessage.MESSAGE_SOLDIERS_ARE_HERE2, Color.WHITE);					 
			 } 
			 soldiers++;		 				 
		 }
		 
		 // Comportamiento particular para la localidad 19 - JuanSolo saluda a Chris
		 if (currentLocation.getId().equals("019")) { 
			 if (part1_Adventure.isActorHere(SystemMessage.ACTOR_JUAN)){
				 if (chrisCounter == 0) {
					 addLine (SystemMessage.MESSAGE_JUANSOLO_SAID_HELLO_CHRIS1, Color.WHITE);
				 } else if (chrisCounter == 1) {
					 addLine (SystemMessage.MESSAGE_JUANSOLO_SAID_HELLO_CHRIS2, Color.WHITE);
				 }
			 }
			 
		 }

		 // Comportamiento particular para la localidad 20 - JuanSolo comenta estado de los robots
		 if (currentLocation.getId().equals("020")) { 
			 if (part1_Adventure.isActorHere(SystemMessage.ACTOR_JUAN)){
				 if (part1_Adventure.isActorHere(SystemMessage.ACTOR_R3D2)) {
					 addLine (SystemMessage.MESSAGE_JUANSOLO_SAID_R3D2, Color.WHITE);
					 addLine (SystemMessage.MESSAGE_C2P2_SAID_BAD_SHIP, Color.WHITE);
				 } else {
					 addLine (SystemMessage.MESSAGE_JUANSOLO_SAID_NOT_R3D2, Color.WHITE);
				 }
			 }			 
		 }

		 // Comportamiento particular para la localidad 21 - JuanSolo cierra la rampa
		 if (currentLocation.getId().equals("021")) { 
			 if ((part1_Adventure.isActorHere(SystemMessage.ACTOR_JUAN)) &&
				 (part1_Adventure.isActorHere(SystemMessage.ACTOR_OBI)) &&
				 (part1_Adventure.isActorHere(SystemMessage.ACTOR_C2P2))) {
				 addLine (SystemMessage.MESSAGE_JUANSOLO_CLOSE_DOORS, Color.WHITE);
				 Actor juanSolo = part1_Adventure.getActor(SystemMessage.ACTOR_JUAN);
				 juanSolo.setLocationId("022");
				 Behaviour behaviour = part1_Adventure.getBehaviour(juanSolo.getFollowingBehaviour());
				 behaviour.setActive(AdventureCodes.FALSE);
				 doorsLocked = true;			 
			 }			 
		 }
		 
		 // Comportamiento particular para la localidad 22
		 if (currentLocation.getId().equals("022")) { 
			 if (inTheSpace == false){
				 addLine (SystemMessage.MESSAGE_OBI_WE_MUST_TAKEOFF, Color.WHITE);
			 } else {
				 addLine (SystemMessage.MESSAGE_OBI_WE_MUST_HYPERSPACE, Color.WHITE);				
				 int resId = getResources().getIdentifier("part1_location_022c", "drawable", getPackageName());
				 ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
				 location_ImageView.setImageResource(resId);
				 AdventureLocation location = part1_Adventure.getLocation("022");
				 location.setImage("part1_location_022c");
			 }
		 }

		 if (inTheSpace == true){
			 addLine (SystemMessage.MESSAGE_TWO_BATTLESHIPS, Color.WHITE);
			 hits++;
			 if (hits>9){
				 addLine (SystemMessage.MESSAGE_TWO_BATTLESHIPS5, Color.WHITE);
			 } else if (hits>7) {
				 addLine (SystemMessage.MESSAGE_TWO_BATTLESHIPS4, Color.WHITE);
			 } else if (hits>3) {
				 addLine (SystemMessage.MESSAGE_TWO_BATTLESHIPS3, Color.WHITE);
			 } else if (hits>1) {
				 addLine (SystemMessage.MESSAGE_TWO_BATTLESHIPS2, Color.WHITE);
			 }
		 }

		 if (globalInformation.getAdvancedLocation().equals(AdventureCodes.TRUE)){
			 // Actualizamos los gr�ficos asociados a las salidas disponibles
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
				 }
			 }
		 }
		 startAnimatingMainPicture();
	 }
 
	  
	 public void setUserInterfaceState(boolean state){
		 ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
		 Button highText_Button = (Button) findViewById(R.id.highText_Button);
		 
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
						if (part1_Adventure.loadPart1()==true){
			 				robotsFuera = globalInformation.getRobotsFuera();
			 				is_plectrum_cut = globalInformation.getIsPlectrumCut();
			 				doorsLocked = globalInformation.getDoorsLocked();
			 				engineIsBroken = globalInformation.getEngineIsBroken();
			 				hyperspaceIsBroken = globalInformation.getHyperspaceIsBroken();
			 				inTheSpace = globalInformation.getInTheSpace();	  		
			 				chrisCounter = globalInformation.getChrisCounter();
			 				engineHit = globalInformation.getEngineHit();
			 				hits = globalInformation.getHits();
			 				merodeadores = globalInformation.getMerodeadores();
			 				soldiers = globalInformation.getSoldiers();
			 				juan = globalInformation.getJuan();
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
					 globalInformation.setRobotsFuera(robotsFuera);
					 globalInformation.setIsPlectrumCut(is_plectrum_cut);
					 globalInformation.setDoorsLocked(doorsLocked);
			 		 globalInformation.setEngineIsBroken(engineIsBroken);
			 		 globalInformation.setHyperspaceIsBroken(hyperspaceIsBroken);
			 		 globalInformation.setInTheSpace(inTheSpace);	  		
			 		 globalInformation.setChrisCounter(chrisCounter);
			 		 globalInformation.setEngineHit(engineHit);
			 		 globalInformation.setHits(hits);
			 		 globalInformation.setMerodeadores(merodeadores);
			 		 globalInformation.setSoldiers(soldiers);
			 		 globalInformation.setJuan(juan);
			 		 part1_Adventure.savePart1();
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
							 
							 String sayReturn = part1_Adventure.say(globalInformation.getActorToTalk().getName(), sentence.getText().toString().toUpperCase());
							 if (part1_Adventure.isBehaviourId(sayReturn)){
								 Behaviour behaviour = part1_Adventure.getBehaviour(sayReturn);
					 				if (!behaviour.getMovePlayerTo().equals("")){ // Tengo que mover al usuario a una localidad
					 					part1_Adventure.setCurrentLocation(behaviour.getMovePlayerTo());
					 					if (part1_Adventure.getCurrentLocation().getType().equals(AdventureCodes.LOCATION_TYPE_DEAD)){
					 						if (languageSupported){
					 							talker.stop();
					 						}
							 				startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasDeadLocationActivity.class));				 				
							 			} else {
							 				updateScreen();
							 			}							 			
					 				}
					 				if (behaviour.getType().equals(AdventureCodes.BEHAVIOR_TYPE_FOLLOW)){
					 					response_TextView.setText(part1_Adventure.activateFollowingBehaviour(globalInformation.getActorToTalk().getName()));
					 				}
					 				
					 				if (behaviour.getObjectsReturned().size()>0){ // El comportamiento devuelve objetos
					 					HashMap<String, String> objectsReturned = behaviour.getObjectsReturned();
					 					Iterator<?> it = objectsReturned.entrySet().iterator();					 					
					 					while (it.hasNext()){ // Recorremos la lista de Actores
					 						Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
					 						String object = (String)e.getKey();
					 						String message = (String)e.getValue();
					 						part1_Adventure.setObjectVisible(object, currentLocation.getId());
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
					  startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasMainActivity.class));
				    	
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
			 dialogMessages.setContentView(R.layout.end_custom_dialog_layout);
			 //dialog.setTitle("Atencion!!");
			 dialogMessages.setCancelable(false);
			 
			 TextView dialogLine1_TextView = (TextView) dialogMessages.findViewById(R.id.dialogLine1_TextView);
			 TextView dialogLine2_TextView = (TextView) dialogMessages.findViewById(R.id.dialogLine2_TextView);
			 ImageView image_ImageView = (ImageView) dialogMessages.findViewById(R.id.image_ImageView);
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
					  startActivity(new Intent(LaGuerradelasVajillasPart1LocationActivity.this, LaGuerradelasVajillasMainActivity.class));
				    	
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
			 part1_Adventure.increaseActionsCounter();
			 if ((currentLocation.getId().equals("006")) &&
		 	  	 (is_plectrum_cut == false)){
		 				if (aux_TextView.getText().toString().equals(SystemMessage.OBJECT_PLECTRUM)){
		 					if (part1_Adventure.isObjectTaken(SystemMessage.OBJECT_SELFPEELER)){
		 						addLine(SystemMessage.MESSAGE_USING_SELFPEELER_TAKE_PLECTRUM, Color.WHITE);
		 						is_plectrum_cut = true;
		 						AdventureObject<?, ?> object = part1_Adventure.getObject(SystemMessage.OBJECT_PLECTRUM);
		 						object.setLocationText("");
		 					} else {
		 						addLine(SystemMessage.MESSAGE_NOT_USING_SELFPEELER_TAKE_PLECTRUM, Color.WHITE);
		 					}
		 				} else {
		 					addLine(part1_Adventure.catchObject(parser.getActionObject1()), Color.WHITE);
						}
		 				dialogObjects.cancel();
		 			} else{
		 				
		 				addLine(part1_Adventure.catchObject(aux_TextView.getText().toString()), Color.WHITE);				
		 				showCaughtObjects();
		 			}
			 
		 } else if (globalInformation.getLeaveSelected()==true){
			 part1_Adventure.increaseActionsCounter();
			 addLine(part1_Adventure.leaveObject(aux_TextView.getText().toString()), Color.WHITE);
			 showLeaveObjects();
		 } else if (globalInformation.getGiveSelected()==true){
			 //object_ImageView = (ImageView) dialogGive.findViewById(R.id.object_ImageView);
			 //object_TextView = (TextView) dialogGive.findViewById(R.id.object_TextView);
			 globalInformation.setObjectToGive(part1_Adventure.getObject(aux_TextView.getText().toString()));
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
		 
		 
		 String sayReturn = part1_Adventure.give(aux_TextView.getText().toString(), globalInformation.getObjectToGive().getName());
		 if (((aux_TextView.getText().toString().equals(SystemMessage.ACTOR_C2P2) || 
		      (aux_TextView.getText().toString().equals(SystemMessage.ACTOR_R3D2)))) && 
		     (globalInformation.getObjectToGive().getName().equals(SystemMessage.OBJECT_OILCAN))){
			 part1_Adventure.activateFollowingBehaviour(SystemMessage.ACTOR_C2P2);	 
				int resId = getResources().getIdentifier("part1_location_004b", "drawable", getPackageName());
					ImageView location_ImageView = (ImageView) findViewById(R.id.location_ImageView);
					location_ImageView.setImageResource(resId);
		 }
		 
		 if ((aux_TextView.getText().toString().equals(SystemMessage.ACTOR_JUAN)) && 
			     (globalInformation.getObjectToGive().getName().equals(SystemMessage.OBJECT_CREDITS))){
				 part1_Adventure.activateFollowingBehaviour(SystemMessage.ACTOR_JUAN);	 
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
		 
		 globalInformation.setActorToTalk(part1_Adventure.getActor(aux_TextView.getText().toString()));
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
		 
		 if (part1_Adventure.getTotalObjectsTaken() == 0) {
			 
			 ImageView object1_ImageView = (ImageView) dialogObjects.findViewById(R.id.object1_ImageView);
			 object1_ImageView.setVisibility(View.VISIBLE);
			 object1_ImageView.setEnabled(false);
			 object1_ImageView.setImageResource(R.drawable.inventary_empty);
			 TextView object1_TextView = (TextView) dialogObjects.findViewById(R.id.object1_TextView);
			 object1_TextView.setVisibility(View.VISIBLE);
			 object1_TextView.setText(SystemMessage.MESSAGE_INVENTARY_EMPTY);
			 
			 
		 } else {			 
			 HashMap<String, Object> objects = part1_Adventure.getObjectsTaken();			 
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
		 
		 if (part1_Adventure.getTotalLocationObjects() == 0) {
			 
			 ImageView object1_ImageView = (ImageView) dialogObjects.findViewById(R.id.object1_ImageView);
			 object1_ImageView.setVisibility(View.VISIBLE);
			 object1_ImageView.setEnabled(false);
			 object1_ImageView.setImageResource(R.drawable.inventary_empty);
			 TextView object1_TextView = (TextView) dialogObjects.findViewById(R.id.object1_TextView);
			 object1_TextView.setVisibility(View.VISIBLE);
			 object1_TextView.setText(SystemMessage.MESSAGE_INVENTARY_EMPTY);
		 } else {			 
			 HashMap<String, Object> objects = part1_Adventure.getLocationObjects();			 
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
		 
		 if (part1_Adventure.getTotalObjectsTaken() == 0) {
			 
			 ImageView object1_ImageView = (ImageView) dialogObjects.findViewById(R.id.object1_ImageView);
			 object1_ImageView.setVisibility(View.VISIBLE);
			 object1_ImageView.setEnabled(false);
			 object1_ImageView.setImageResource(R.drawable.inventary_empty);
			 TextView object1_TextView = (TextView) dialogObjects.findViewById(R.id.object1_TextView);
			 object1_TextView.setVisibility(View.VISIBLE);
			 object1_TextView.setText(SystemMessage.MESSAGE_INVENTARY_EMPTY);
			 
		 } else {			 
			 HashMap<String, Object> objects = part1_Adventure.getObjectsTaken();			 
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
		 
		 if (part1_Adventure.getTotalLocationActors() == 0) {
			 
			 ImageView object1_ImageView = (ImageView) dialog.findViewById(R.id.actor1_ImageView);
			 object1_ImageView.setVisibility(View.VISIBLE);
			 object1_ImageView.setEnabled(false);
			 object1_ImageView.setImageResource(R.drawable.inventary_empty);
			 TextView object1_TextView = (TextView) dialog.findViewById(R.id.actor1_TextView);
			 object1_TextView.setVisibility(View.VISIBLE);
			 object1_TextView.setText(SystemMessage.MESSAGE_NO_ACTORS_HERE);
			 
			 
		 } else {		
			 
			 HashMap<String, Actor> actors = part1_Adventure.getLocationActors() ;			 
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

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	 public void say(String text2say){    	    	
	    	talker.speak(text2say, TextToSpeech.QUEUE_ADD, null);    		
	    	
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
