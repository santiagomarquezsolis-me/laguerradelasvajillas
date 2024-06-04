package es.zgames.textadventures.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.Vector;
import es.zgames.utils.GlobalInformation;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class Adventure<V, K> {
	private  static Adventure<java.lang.Object, java.lang.Object> INSTANCE = new Adventure<java.lang.Object, java.lang.Object>();
	
	private Context context;
	private int actionsCounter = 0;
	private AdventureLocation currentLocation;
	private HashMap<String, AdventureLocation> locations;
	private HashMap<String, Description> descriptions;
	private HashMap<String, java.lang.Object> objects;
	private HashMap<String, Actor> actors;
	private HashMap<String, Behaviour> actorsBehaviours;
	private HashMap<String, Behaviour> objectsBehaviours;
	private HashMap<String, String> visitedLocations;
	
	private int scoreCounter = 0;
	private int badWordsCounter = 0;
	
	private String adventurePart;
	
	private Adventure(){
		
	}
	
	public static Adventure<?, ?> getInstance(){
		return INSTANCE;
	}
	
	public void initAdventure(Context context, String adventurePart){
		this.context = context;	
		this.actionsCounter = 0;
		this.scoreCounter = 0;
		this.badWordsCounter = 0;
		this.currentLocation = null;		
		this.locations = null;
		this.visitedLocations = new HashMap<String, String>();
		visitedLocations.put("001", "001");
		this.adventurePart = adventurePart;
	}
	
	public AdventureLocation getLocation(String locationId){
		return locations.getOrDefault(locationId, null);
	}
	
	public void loadLocations(int resourceId){
		try {
			// Connect with Resources in /res/raw folder
			InputStream inputStream = context.getResources().openRawResource(resourceId);
			
			InputStreamReader inputreader = new InputStreamReader(inputStream, "ISO-8859-1");
			
			BufferedReader buffreader = new BufferedReader(inputreader, 8192);
			
			String line = "";
			locations = new HashMap<String, AdventureLocation>();
			AdventureLocation location = null;
			// Process text line
			
			while (( line = buffreader.readLine()) != null) {    
				String identificator = line.substring(0, AdventureCodes.MAX_IDENTIFICATOR);
				if (identificator.equals (AdventureCodes.INIT_DEFINITION)) {
					location = new AdventureLocation();
				}
				if (identificator.equals (AdventureCodes.LOCATION_ID)) {

					location.setId(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1) );
				}
				if (identificator.equals (AdventureCodes.TYPE_ID)) {
					location.setType(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.TITLE_ID)) {
					location.setTitle(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.IMAGEN_ID)) {
					location.setImage(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.RETRO_IMAGEN_ID)) {
					location.setRetroImage(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.HELP_ID)) {
					location.setHelp(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.DESCRIPTION_ID)) {
					location.setDescription(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.MENSAGE_ID)) {
					location.addMessage(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.EXITS_ID)) {
					Exit exit = new Exit();
					StringTokenizer strTok = new StringTokenizer(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
					String directionName = strTok.nextToken();
					String shortDirectionName = strTok.nextToken();
					String finalLocation = strTok.nextToken();
					exit.setDirectionName(directionName);
					exit.setShortDirectionName(shortDirectionName);
					exit.setLeadsTo(finalLocation);
					assert location != null;
					location.addExit(exit);
				}
				if (identificator.equals (AdventureCodes.FLUID_ID)) {
					assert location != null;
					location.setHasFluid(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.FLUID_TYPE_ID)) {
					assert location != null;
					location.setFluidType(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.LIGHT_ID)) {
					assert location != null;
					location.setNeedLight(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.END_DEFINITION)) {
					assert location != null;
					locations.put(location.getId(), location);
				}
			}
			setCurrentLocation("001");
		} catch (IOException e) {                 
			System.out.println ("Error: " + e.getMessage());			             
		}		
	}



	public void loadDescriptions(int resourceId){
		try {
			// Connect with Resources in /res/raw folder
			InputStream inputStream = context.getResources().openRawResource(resourceId);
			InputStreamReader inputreader = new InputStreamReader(inputStream, "ISO-8859-1");  
			
			BufferedReader buffreader = new BufferedReader(inputreader, 8192);
			
			String line = "";
			descriptions = new HashMap<String, Description>();
			Description description = null;
			// Process text line
			
			while (( line = buffreader.readLine()) != null) {    
				String identificator = line.substring(0, AdventureCodes.MAX_IDENTIFICATOR);
				if (identificator.equals (AdventureCodes.INIT_DEFINITION)) {
					description = new Description();
				}
				if (identificator.equals (AdventureCodes.LOCATION_ID)) {
					description.setLocationId(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1) );
				}
				if (identificator.equals (AdventureCodes.NAME_ID)) {
					description.setName(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.DESCRIPTION_ID)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1),"@");
					description.setDescription(strTok.nextToken());
					if (strTok.countTokens()>0){
						description.setObjectReturned(AdventureCodes.FALSE);
						description.setReturnObject(strTok.nextToken());
						description.setAuxiliaryDescription(strTok.nextToken());
					}
				}
				if (identificator.equals (AdventureCodes.END_DEFINITION)) {
					assert description != null;
					descriptions.put(description.getLocationId() + "-" + description.getName(), description);
				}
			}
		} catch (IOException e) {                 
			System.out.println ("Error: " + e.getMessage());			             
		}		
	}

	public void loadObjects(int resourceId){
		try {
			GlobalInformation globalInformation = GlobalInformation.getInstance();
			// Connect with Resources in /res/raw folder
			InputStream inputStream = context.getResources().openRawResource(resourceId);
			InputStreamReader inputreader = new InputStreamReader(inputStream, "ISO-8859-1");  
			
			BufferedReader buffreader = new BufferedReader(inputreader, 8192);
			
			String line = "";
			objects = new HashMap<String, java.lang.Object>();
			AdventureObject<?, ?> object = null;
			// Process text line
			
			while (( line = buffreader.readLine()) != null) {    
				String identificator = line.substring(0, AdventureCodes.MAX_IDENTIFICATOR);
				if (identificator.equals (AdventureCodes.INIT_DEFINITION)) {
					object = new AdventureObject<java.lang.Object, java.lang.Object>();
				}
				if (identificator.equals (AdventureCodes.LOCATION_ID)) {
					object.setLocationId(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1) );
					if (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
						if (globalInformation.isImage01_free()){
							object.setImagePosition(1);
							globalInformation.setImage01_free(false);
						} else if (globalInformation.isImage02_free()){
							object.setImagePosition(2);
							globalInformation.setImage02_free(false);
						} else if (globalInformation.isImage03_free()){
							object.setImagePosition(3);
							globalInformation.setImage03_free(false);
						} else if (globalInformation.isImage04_free()){
							object.setImagePosition(4);
							globalInformation.setImage04_free(false);
						} else if (globalInformation.isImage05_free()){
							object.setImagePosition(5);
							globalInformation.setImage05_free(false);
						} else if (globalInformation.isImage06_free()){
							object.setImagePosition(6);
							globalInformation.setImage06_free(false);
						} else if (globalInformation.isImage07_free()){
							object.setImagePosition(7);
							globalInformation.setImage07_free(false);
						} else if (globalInformation.isImage08_free()){
							object.setImagePosition(8);
							globalInformation.setImage08_free(false);
						}
					}
				}
				if (identificator.equals (AdventureCodes.NAME_ID)) {
					object.setName(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.LONG_ID)) {
					object.setLongName(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.DESCRIPTION_ID)) {
					object.setDescription(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.TEXT_ID)) {
					object.setLocationText(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.FOOD_ID)) {
					object.setIsFood(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.DISTANCE_ID)) {
					object.setDistance(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.ARTICLE_ID)) {
					object.setArticle(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.LEVEL_ID)) {
					object.setChargeLevel(Integer.valueOf(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1)));
				}
				if (identificator.equals (AdventureCodes.STATUS_ID)) {
					object.setStatus(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.FLUID_ID)) {
					object.setContainsFluid(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.TREASURE_ID)) {
					object.setIsTreasure(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.FLUID_TYPE_ID)) {
					object.setFluidType(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}				
				if (identificator.equals (AdventureCodes.LIGHT_ID)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1),"@");
					object.setIsLightSource(strTok.nextToken());
					if (strTok.countTokens()>0){
						object.setLightLink(strTok.nextToken());
					}
				}
				if (identificator.equals (AdventureCodes.CONTAINER_ID)) {
					object.setIsContainer(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.SIZE_ID)) {
					object.setSize(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.SHOW_ID)) {
					object.setShow(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.IMAGEN_ID)) {
					object.setImage(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.END_DEFINITION)) {
					objects.put(object.getName(), object);					
				}
			}
		} catch (IOException e) {                 
			System.out.println ("Error: " + e.getMessage());			             
		}		
	}
	
	public void loadSystemMessage(int resourceId){
		try {
			// Connect with Resources in /res/raw folder
			InputStream inputStream = context.getResources().openRawResource(resourceId);
			InputStreamReader inputreader = new InputStreamReader(inputStream, "ISO-8859-1");  
			
			BufferedReader buffreader = new BufferedReader(inputreader, 8192);
			
			String line = "";
			String messageType = "";
			String messageData = "";
			// Process text line
			
			while (( line = buffreader.readLine()) != null) {    
				String identificator = line.substring(0, AdventureCodes.MAX_IDENTIFICATOR);
				if (identificator.equals (AdventureCodes.MESSAGE_ID)) {				
					StringTokenizer stringTokenizer = new StringTokenizer(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1), "=");
					messageType = stringTokenizer.nextToken().trim();
					messageData = stringTokenizer.nextToken();
					if (messageType.equals("MESSAGE_WRONG_DIRECTION")) SystemMessage.MESSAGE_WRONG_DIRECTION = messageData;
					if (messageType.equals("MESSAGE_QUIT")) SystemMessage.MESSAGE_QUIT = messageData;
					if (messageType.equals("MESSAGE_ARE_YOU_SURE")) SystemMessage.MESSAGE_ARE_YOU_SURE = messageData;
					if (messageType.equals("MESSAGE_NULL_SENTENCE")) SystemMessage.MESSAGE_NULL_SENTENCE = messageData;
					if (messageType.equals("MESSAGE_BADWORDS_WARNING")) SystemMessage.MESSAGE_BADWORDS_WARNING = messageData;
					if (messageType.equals("MESSAGE_BADWORDS_END")) SystemMessage.MESSAGE_BADWORDS_END = messageData;
					if (messageType.equals("MESSAGE_UNKOWN_ACTION")) SystemMessage.MESSAGE_UNKOWN_ACTION = messageData;
					if (messageType.equals("MESSAGE_UNKOWN_DESCRIPTION")) SystemMessage.MESSAGE_UNKOWN_DESCRIPTION = messageData;
					if (messageType.equals("MESSAGE_INVENTARY_EMPTY")) SystemMessage.MESSAGE_INVENTARY_EMPTY = messageData;
					if (messageType.equals("MESSAGE_INVENTARY_STRING")) SystemMessage.MESSAGE_INVENTARY_STRING = messageData;
					if (messageType.equals("MESSAGE_VISIBLE_EXITS")) SystemMessage.MESSAGE_VISIBLE_EXITS = messageData;
					if (messageType.equals("MESSAGE_VISIBLE_OBJECTS")) SystemMessage.MESSAGE_VISIBLE_OBJECTS = messageData;
					if (messageType.equals("MESSAGE_YOUR_TURN")) SystemMessage.MESSAGE_YOUR_TURN = messageData;
					if (messageType.equals("MESSAGE_AND")) SystemMessage.MESSAGE_AND = messageData;
					if (messageType.equals("MESSAGE_DOT")) SystemMessage.MESSAGE_DOT = messageData;
					if (messageType.equals("MESSAGE_HAS_CAUGHT")) SystemMessage.MESSAGE_HAS_CAUGHT = messageData;
					if (messageType.equals("MESSAGE_HAS_LEAVE")) SystemMessage.MESSAGE_HAS_LEAVE = messageData;
					if (messageType.equals("MESSAGE_CANT_CAUGHT_THAT")) SystemMessage.MESSAGE_CANT_CAUGHT_THAT = messageData;
					if (messageType.equals("MESSAGE_CANT_LEAVE_THAT")) SystemMessage.MESSAGE_CANT_LEAVE_THAT = messageData;
					if (messageType.equals("MESSAGE_DONT_HAVE_THAT")) SystemMessage.MESSAGE_DONT_HAVE_THAT = messageData;
					if (messageType.equals("MESSAGE_ALL")) SystemMessage.MESSAGE_ALL = messageData;
					if (messageType.equals("MESSAGE_CANT_DO_THAT")) SystemMessage.MESSAGE_CANT_DO_THAT = messageData;
					if (messageType.equals("MESSAGE_CANT_SEE_WATER")) SystemMessage.MESSAGE_CANT_SEE_WATER = messageData;
					if (messageType.equals("MESSAGE_CAN_YOU_PLAY_AGAIN")) SystemMessage.MESSAGE_CAN_YOU_PLAY_AGAIN = messageData;
					if (messageType.equals("MESSAGE_YES")) SystemMessage.MESSAGE_YES = messageData;
					if (messageType.equals("MESSAGE_THIS_ACTOR_IS_NOT_HERE")) SystemMessage.MESSAGE_THIS_ACTOR_IS_NOT_HERE = messageData;
					if (messageType.equals("MESSAGE_INSERT_ACTOR_NAME")) SystemMessage.MESSAGE_INSERT_ACTOR_NAME = messageData;
					if (messageType.equals("MESSAGE_ACTOR_WITHOUT_GIVE_BEHAVIOUR")) SystemMessage.MESSAGE_ACTOR_WITHOUT_GIVE_BEHAVIOUR = messageData;
					if (messageType.equals("MESSAGE_YOU_FILL")) SystemMessage.MESSAGE_YOU_FILL = messageData;
					if (messageType.equals("MESSAGE_NO_LIQUID_HERE")) SystemMessage.MESSAGE_NO_LIQUID_HERE = messageData;
					if (messageType.equals("MESSAGE_DRINK_LIQUID")) SystemMessage.MESSAGE_DRINK_LIQUID = messageData;
					if (messageType.equals("MESSAGE_OBJECT_HAS_NO_LIQUID")) SystemMessage.MESSAGE_OBJECT_HAS_NO_LIQUID = messageData;
					if (messageType.equals("MESSAGE_IS_VERY_DARK")) SystemMessage.MESSAGE_IS_VERY_DARK = messageData;
					if (messageType.equals("MESSAGE_NOTHING_TO_CAUGHT")) SystemMessage.MESSAGE_NOTHING_TO_CAUGHT = messageData;
					if (messageType.equals("MESSAGE_DONT_HAVE_THAT_OBJECT")) SystemMessage.MESSAGE_DONT_HAVE_THAT_OBJECT = messageData;
					if (messageType.equals("MESSAGE_DONT_HAVE_PUT_OBJECT_IN")) SystemMessage.MESSAGE_DONT_HAVE_PUT_OBJECT_IN = messageData;
					if (messageType.equals("MESSAGE_DONT_HAVE_QUIT_OBJECT_IN")) SystemMessage.MESSAGE_DONT_HAVE_QUIT_OBJECT_IN = messageData;
					if (messageType.equals("MESSAGE_HAVE_PUT_OBJECT_IN")) SystemMessage.MESSAGE_HAVE_PUT_OBJECT_IN = messageData;
					if (messageType.equals("MESSAGE_DONT_HAVE_THIS_OBJECT_IN_THIS_OTHER_OBJECT")) SystemMessage.MESSAGE_DONT_HAVE_THIS_OBJECT_IN_THIS_OTHER_OBJECT = messageData;
					if (messageType.equals("MESSAGE_DONT_QUIT_THIS_OBJECT_IN_THIS_OTHER_OBJECT")) SystemMessage.MESSAGE_DONT_QUIT_THIS_OBJECT_IN_THIS_OTHER_OBJECT = messageData;
					if (messageType.equals("MESSAGE_HAS_IN")) SystemMessage.MESSAGE_HAS_IN = messageData;
					if (messageType.equals("MESSAGE_HAS_QUIT_OBJECT_FROM_OTHER_OBJECT")) SystemMessage.MESSAGE_HAS_QUIT_OBJECT_FROM_OTHER_OBJECT = messageData;
					if (messageType.equals("MESSAGE_NOTHING")) SystemMessage.MESSAGE_NOTHING = messageData;
					if (messageType.equals("MESSAGE_HAS_TURNON")) SystemMessage.MESSAGE_HAS_TURNON = messageData;
					if (messageType.equals("MESSAGE_HAS_TURNOFF")) SystemMessage.MESSAGE_HAS_TURNOFF = messageData;
					if (messageType.equals("MESSAGE_TURNON_IMPOSSIBLE")) SystemMessage.MESSAGE_TURNON_IMPOSSIBLE = messageData;
					if (messageType.equals("MESSAGE_TURNOFF_IMPOSSIBLE")) SystemMessage.MESSAGE_TURNOFF_IMPOSSIBLE = messageData;
					if (messageType.equals("MESSAGE_TURNON_LINK_OBJECT")) SystemMessage.MESSAGE_TURNON_LINK_OBJECT = messageData;
					if (messageType.equals("MESSAGE_DRINK_FROM_WHERE")) SystemMessage.MESSAGE_DRINK_FROM_WHERE = messageData;
					if (messageType.equals("MESSAGE_UNKOWN_WHAT_DESCRIBE")) SystemMessage.MESSAGE_UNKOWN_WHAT_DESCRIBE = messageData;
					if (messageType.equals("MESSAGE_IMPOSSIBLE_KILL")) SystemMessage.MESSAGE_IMPOSSIBLE_KILL = messageData;
					if (messageType.equals("MESSAGE_NO_ACTORS_HERE")) SystemMessage.MESSAGE_NO_ACTORS_HERE = messageData;
					if (messageType.equals("MESSAGE_ACTOR_NOT_PUSH_BEHAVIOUR")) SystemMessage.MESSAGE_ACTOR_NOT_PUSH_BEHAVIOUR = messageData;
					if (messageType.equals("MESSAGE_AD")) SystemMessage.MESSAGE_AD = messageData;
					if (messageType.equals("MESSAGE_LISTEN_1")) SystemMessage.MESSAGE_LISTEN_1 = messageData;
					if (messageType.equals("MESSAGE_OBJECT_IS_FAR")) SystemMessage.MESSAGE_OBJECT_IS_FAR = messageData;
					if (messageType.equals("MESSAGE_DONT_BREAK_PLEASE")) SystemMessage.MESSAGE_DONT_BREAK_PLEASE = messageData;
					if (messageType.equals("MESSAGE_OBJECT_HAS_FALLDOWN")) SystemMessage.MESSAGE_OBJECT_HAS_FALLDOWN = messageData;
					if (messageType.equals("MESSAGE_NO_OPEN_OBJECTS")) SystemMessage.MESSAGE_NO_OPEN_OBJECTS = messageData;
					if (messageType.equals("MESSAGE_OBJECT_NOT_OPENABLE")) SystemMessage.MESSAGE_OBJECT_NOT_OPENABLE = messageData;
					if (messageType.equals("MESSAGE_TEXT_SIZE_CHANGED")) SystemMessage.MESSAGE_TEXT_SIZE_CHANGED = messageData;
					if (messageType.equals("MESSAGE_TOTAL_OBJECTS")) SystemMessage.MESSAGE_TOTAL_OBJECTS = messageData;
					if (messageType.equals("MESSAGE_NOT_ENOUGH_LIGHT")) SystemMessage.MESSAGE_NOT_ENOUGH_LIGHT = messageData;
					if (messageType.equals("MESSAGE_FOLLOWING_BEHAVIOUR_ACTIVATE")) SystemMessage.MESSAGE_FOLLOWING_BEHAVIOUR_ACTIVATE = messageData;
					if (messageType.equals("MESSAGE_CANT_CARRY_MORE_OBJECTS")) SystemMessage.MESSAGE_CANT_CARRY_MORE_OBJECTS = messageData;
					if (messageType.equals("MESSAGE_NEED_OBJECTS_FOR_THAT_DIRECTION")) SystemMessage.MESSAGE_NEED_OBJECTS_FOR_THAT_DIRECTION = messageData;
					if (messageType.equals("MESSAGE_NEED_OBJECTS_FOR_THAT_DIRECTION2")) SystemMessage.MESSAGE_NEED_OBJECTS_FOR_THAT_DIRECTION2 = messageData;
					if (messageType.equals("MESSAGE_THERE_ARENT_ANY_EXITS")) SystemMessage.MESSAGE_THERE_ARENT_ANY_EXITS = messageData;
					if (messageType.equals("MESSAGE_LEVEL_OF_CHARGE")) SystemMessage.MESSAGE_LEVEL_OF_CHARGE = messageData;
					if (messageType.equals("MESSAGE_CANT_SEE_SEEDS")) SystemMessage.MESSAGE_CANT_SEE_SEEDS= messageData;
					if (messageType.equals("MESSAGE_HAS_EMPTY_OBJECT")) SystemMessage.MESSAGE_HAS_EMPTY_OBJECT= messageData;
					if (messageType.equals("MESSAGE_IS_FILL_WITH")) SystemMessage.MESSAGE_IS_FILL_WITH= messageData;
					if (messageType.equals("MESSAGE_DONT_DRINK_THAT")) SystemMessage.MESSAGE_DONT_DRINK_THAT= messageData;
					if (messageType.equals("MESSAGE_OBJECT_IS_EMPTY_YET")) SystemMessage.MESSAGE_OBJECT_IS_EMPTY_YET= messageData;
					if (messageType.equals("MESSAGE_OBJECT_IS_HEAVY")) SystemMessage.MESSAGE_OBJECT_IS_HEAVY= messageData;
					if (messageType.equals("MESSAGE_COIN_IN_MACHINE")) SystemMessage.MESSAGE_COIN_IN_MACHINE= messageData;
					if (messageType.equals("MESSAGE_LAMP_RECHARGE")) SystemMessage.MESSAGE_LAMP_RECHARGE= messageData;
					if (messageType.equals("MESSAGE_PUT_WHERE"))SystemMessage.MESSAGE_PUT_WHERE= messageData;					
					if (messageType.equals("MESSAGE_OK")) SystemMessage.MESSAGE_OK = messageData;
					if (messageType.equals("MESSAGE_CONGRATULATIONS")) SystemMessage.MESSAGE_CONGRATULATIONS = messageData;		 
					if (messageType.equals("MESSAGE_YOU_DID_IT")) SystemMessage.MESSAGE_YOU_DID_IT = messageData;
					if (messageType.equals("MESSAGE_EAT")) SystemMessage.MESSAGE_EAT = messageData;
					if (messageType.equals("MESSAGE_CANT_EAT_THAT")) SystemMessage.MESSAGE_CANT_EAT_THAT = messageData;
					if (messageType.equals("MESSAGE_YOU_STAY_IN")) SystemMessage.MESSAGE_YOU_STAY_IN = messageData; 
					if (messageType.equals("MESSAGE_LOCATION_OPEN")) SystemMessage.MESSAGE_LOCATION_OPEN = messageData;
					if (messageType.equals("MESSAGE_OBJECT")) SystemMessage.MESSAGE_OBJECT= messageData;
					if (messageType.equals("MESSAGE_FULL")) SystemMessage.MESSAGE_FULL= messageData;
					if (messageType.equals("MESSAGE_EMPTY")) SystemMessage.MESSAGE_EMPTY= messageData;
					if (messageType.equals("MESSAGE_LAMP_WITH_PROBLEMS1")) SystemMessage.MESSAGE_LAMP_WITH_PROBLEMS1= messageData;
					if (messageType.equals("MESSAGE_LAMP_WITH_PROBLEMS2")) SystemMessage.MESSAGE_LAMP_WITH_PROBLEMS2= messageData;
					if (messageType.equals("MESSAGE_LAMP_WITH_PROBLEMS3")) SystemMessage.MESSAGE_LAMP_WITH_PROBLEMS3= messageData;
					if (messageType.equals("MESSAGE_PART2_LOCATION1_END")) SystemMessage.MESSAGE_PART2_LOCATION1_END= messageData;
					if (messageType.equals("MESSAGE_GAME_SAVED")) SystemMessage.MESSAGE_GAME_SAVED= messageData;
					if (messageType.equals("MESSAGE_GAME_LOADED")) SystemMessage.MESSAGE_GAME_LOADED= messageData;
					if (messageType.equals("MESSAGE_GAME_NOT_LOADED")) SystemMessage.MESSAGE_GAME_NOT_LOADED= messageData;
					if (messageType.equals("MESSAGE_ANDROID")) SystemMessage.MESSAGE_ANDROID= messageData;
					if (messageType.equals("MESSAGE_ANDROIDS")) SystemMessage.MESSAGE_ANDROIDS= messageData;
					if (messageType.equals("MESSAGE_ANDROIDS_DESCRIPTION")) SystemMessage.MESSAGE_ANDROIDS_DESCRIPTION = messageData;
					if (messageType.equals("MESSAGE_R3D2_NEW_LOCATION_TEXT")) SystemMessage.MESSAGE_R3D2_NEW_LOCATION_TEXT = messageData;
					if (messageType.equals("MESSAGE_TAMBOR")) SystemMessage.MESSAGE_TAMBOR = messageData;
					if (messageType.equals("MESSAGE_SWIN_IN_TIME")) SystemMessage.MESSAGE_SWIN_IN_TIME = messageData;
					if (messageType.equals("MESSAGE_OBI_QUESTION"))  SystemMessage.MESSAGE_OBI_QUESTION = messageData;
					if (messageType.equals("MESSAGE_C2P2_OBI"))  SystemMessage.MESSAGE_C2P2_OBI = messageData;
					if (messageType.equals("MESSAGE_OBI_SAID_WE_NEED_A_PILOT"))  SystemMessage.MESSAGE_OBI_SAID_WE_NEED_A_PILOT = messageData;
					if (messageType.equals("MESSAGE_C2P2_SAID_WAIT_OUTSIDE"))  SystemMessage.MESSAGE_C2P2_SAID_WAIT_OUTSIDE= messageData;
					if (messageType.equals("MESSAGE_KILL_IMPOSSIBLE"))  SystemMessage.MESSAGE_KILL_IMPOSSIBLE= messageData;
					if (messageType.equals("MESSAGE_USING_SELFPEELER_TAKE_PLECTRUM"))  SystemMessage.MESSAGE_USING_SELFPEELER_TAKE_PLECTRUM= messageData;
					if (messageType.equals("MESSAGE_NOT_USING_SELFPEELER_TAKE_PLECTRUM"))  SystemMessage.MESSAGE_NOT_USING_SELFPEELER_TAKE_PLECTRUM= messageData;
					if (messageType.equals("MESSAGE_CANT_CARRY_MORE_OBJECTS_BECAUSE_I_HAVE"))  SystemMessage.MESSAGE_CANT_CARRY_MORE_OBJECTS_BECAUSE_I_HAVE = messageData;
					if (messageType.equals("MESSAGE_BESUGINA_SAID_YOU_CANT_PASS"))SystemMessage.MESSAGE_BESUGINA_SAID_YOU_CANT_PASS = messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_SAID_HELLO_CHRIS1"))SystemMessage.MESSAGE_JUANSOLO_SAID_HELLO_CHRIS1 = messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_SAID_HELLO_CHRIS2"))SystemMessage.MESSAGE_JUANSOLO_SAID_HELLO_CHRIS2 = messageData;
					if (messageType.equals("MESSAGE_NOT_FUCK_IS_POSSIBLE"))SystemMessage.MESSAGE_NOT_FUCK_IS_POSSIBLE = messageData;
					if (messageType.equals("MESSAGE_NOT_FUCK_IS_POSSIBLE2"))SystemMessage.MESSAGE_NOT_FUCK_IS_POSSIBLE2 = messageData;
					if (messageType.equals("MESSAGE_OBI_WE_MUST_HYPERSPACE"))SystemMessage.MESSAGE_OBI_WE_MUST_HYPERSPACE = messageData;
					if (messageType.equals("MESSAGE_TWO_BATTLESHIPS"))SystemMessage.MESSAGE_TWO_BATTLESHIPS = messageData;
					if (messageType.equals("MESSAGE_TWO_BATTLESHIPS2"))SystemMessage.MESSAGE_TWO_BATTLESHIPS2 = messageData;
					if (messageType.equals("MESSAGE_TWO_BATTLESHIPS3"))SystemMessage.MESSAGE_TWO_BATTLESHIPS3 = messageData;
					if (messageType.equals("MESSAGE_TWO_BATTLESHIPS4"))SystemMessage.MESSAGE_TWO_BATTLESHIPS4 = messageData;
					if (messageType.equals("MESSAGE_TWO_BATTLESHIPS5"))SystemMessage.MESSAGE_TWO_BATTLESHIPS5 = messageData;
					if (messageType.equals("MESSAGE_NOTHING_TO_FIX"))SystemMessage.MESSAGE_NOTHING_TO_FIX = messageData;
					if (messageType.equals("MESSAGE_NOTHING_TO_FIX2"))SystemMessage.MESSAGE_NOTHING_TO_FIX2 = messageData;
					if (messageType.equals("MESSAGE_HYPERSPACE_FIXED1"))SystemMessage.MESSAGE_HYPERSPACE_FIXED1 = messageData;
					if (messageType.equals("MESSAGE_HYPERSPACE_FIXED2"))SystemMessage.MESSAGE_HYPERSPACE_FIXED2 = messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_SAID_R3D2"))SystemMessage.MESSAGE_JUANSOLO_SAID_R3D2 = messageData;
					if (messageType.equals("MESSAGE_C2P2_SAID_BAD_SHIP"))SystemMessage.MESSAGE_C2P2_SAID_BAD_SHIP = messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_SAID_NOT_R3D2"))SystemMessage.MESSAGE_JUANSOLO_SAID_NOT_R3D2 = messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_CLOSE_DOORS"))SystemMessage.MESSAGE_JUANSOLO_CLOSE_DOORS = messageData;
					if (messageType.equals("MESSAGE_IMPOSSIBLE_LEAVE_SPACESHIP"))SystemMessage.MESSAGE_IMPOSSIBLE_LEAVE_SPACESHIP = messageData;
					if (messageType.equals("MESSAGE_JUAN_NOT_LIKE_DESERT"))SystemMessage.MESSAGE_JUAN_NOT_LIKE_DESERT= messageData;
					if (messageType.equals("MESSAGE_OBI_SAID_WE_NEED_A_JUAN"))SystemMessage.MESSAGE_OBI_SAID_WE_NEED_A_JUAN= messageData;
					if (messageType.equals("MESSAGE_JUAN_IS_WAITING"))SystemMessage.MESSAGE_JUAN_IS_WAITING= messageData;
					if (messageType.equals("MESSAGE_FIND_SPACESHIP"))SystemMessage.MESSAGE_FIND_SPACESHIP = messageData;
					if (messageType.equals("MESSAGE_FIND_SPACESHIP2"))SystemMessage.MESSAGE_FIND_SPACESHIP2 = messageData;
					if (messageType.equals("MESSAGE_TAKEOFF_IMPOSSIBLE"))SystemMessage.MESSAGE_TAKEOFF_IMPOSSIBLE= messageData;
					if (messageType.equals("MESSAGE_ENGINE_IS_BROKEN1"))SystemMessage.MESSAGE_ENGINE_IS_BROKEN1= messageData;
					if (messageType.equals("MESSAGE_ENGINE_IS_BROKEN2"))SystemMessage.MESSAGE_ENGINE_IS_BROKEN2= messageData;
					if (messageType.equals("MESSAGE_ENGINE_IS_BROKEN3"))SystemMessage.MESSAGE_ENGINE_IS_BROKEN3= messageData;
					if (messageType.equals("MESSAGE_ENGINE_IS_WORKING_NOW"))SystemMessage.MESSAGE_ENGINE_IS_WORKING_NOW= messageData;
					if (messageType.equals("MESSAGE_HALCON_IS_FLYING"))SystemMessage.MESSAGE_HALCON_IS_FLYING= messageData;
					if (messageType.equals("MESSAGE_PACA_AND_DARTHWATER"))SystemMessage.MESSAGE_PACA_AND_DARTHWATER= messageData;
					if (messageType.equals("MESSAGE_OBI_WE_MUST_TAKEOFF"))SystemMessage.MESSAGE_OBI_WE_MUST_TAKEOFF= messageData;
					if (messageType.equals("MESSAGE_HYPERSPACE_IMPOSSIBLE"))SystemMessage.MESSAGE_HYPERSPACE_IMPOSSIBLE= messageData;
					if (messageType.equals("MESSAGE_HYPERSPACE_IMPOSSIBLE2"))SystemMessage.MESSAGE_HYPERSPACE_IMPOSSIBLE2= messageData;
					if (messageType.equals("MESSAGE_HYPERSPACE_IMPOSSIBLE3"))SystemMessage.MESSAGE_HYPERSPACE_IMPOSSIBLE3= messageData;
					if (messageType.equals("MESSAGE_HYPERSPACE_IMPOSSIBLE4"))SystemMessage.MESSAGE_HYPERSPACE_IMPOSSIBLE4= messageData;
					if (messageType.equals("MESSAGE_HYPERSPACE_IS_BROKEN1"))SystemMessage.MESSAGE_HYPERSPACE_IS_BROKEN1= messageData;
					if (messageType.equals("MESSAGE_HYPERSPACE_IS_BROKEN2"))SystemMessage.MESSAGE_HYPERSPACE_IS_BROKEN2= messageData;
					if (messageType.equals("MESSAGE_HYPERSPACE_IS_BROKEN3"))SystemMessage.MESSAGE_HYPERSPACE_IS_BROKEN3= messageData;
					if (messageType.equals("MESSAGE_HYPERSPACE_IS_BROKEN4"))SystemMessage.MESSAGE_HYPERSPACE_IS_BROKEN4= messageData;
					if (messageType.equals("MESSAGE_MERODEADORES_FEAR_OBI"))SystemMessage.MESSAGE_MERODEADORES_FEAR_OBI= messageData;
					if (messageType.equals("MESSAGE_MERODEADORES_ARE_HERE1"))SystemMessage.MESSAGE_MERODEADORES_ARE_HERE1= messageData;
					if (messageType.equals("MESSAGE_MERODEADORES_ARE_HERE2"))SystemMessage.MESSAGE_MERODEADORES_ARE_HERE2= messageData;
					if (messageType.equals("MESSAGE_MERODEADORES_ARE_HERE3"))SystemMessage.MESSAGE_MERODEADORES_ARE_HERE3= messageData;
					if (messageType.equals("MESSAGE_MERODEADORES_ARE_HERE4"))SystemMessage.MESSAGE_MERODEADORES_ARE_HERE4= messageData;
					if (messageType.equals("MESSAGE_ARRIVEN_PRINGOSA_STAR1"))SystemMessage.MESSAGE_ARRIVEN_PRINGOSA_STAR1= messageData;
					if (messageType.equals("MESSAGE_ARRIVEN_PRINGOSA_STAR2"))SystemMessage.MESSAGE_ARRIVEN_PRINGOSA_STAR2= messageData;
					if (messageType.equals("MESSAGE_ARRIVEN_PRINGOSA_STAR3"))SystemMessage.MESSAGE_ARRIVEN_PRINGOSA_STAR3= messageData;
					if (messageType.equals("MESSAGE_SHOOT_NOTHING"))SystemMessage.MESSAGE_SHOOT_NOTHING= messageData;
					if (messageType.equals("MESSAGE_SHOOT_WITHOUT_MUNITION"))SystemMessage.MESSAGE_SHOOT_WITHOUT_MUNITION= messageData;
					if (messageType.equals("MESSAGE_SHOOT_NO_GUN"))SystemMessage.MESSAGE_SHOOT_NO_GUN= messageData;
					if (messageType.equals("MESSAGE_SHOOT_SAPOIDE"))SystemMessage.MESSAGE_SHOOT_SAPOIDE= messageData;
					if (messageType.equals("MESSAGE_SHOOT_NOT_POSSIBLE"))SystemMessage.MESSAGE_SHOOT_NOT_POSSIBLE= messageData;
					if (messageType.equals("MESSAGE_SHOOT_CHRIS_NOT_POSSIBLE"))SystemMessage.MESSAGE_SHOOT_CHRIS_NOT_POSSIBLE= messageData;
					if (messageType.equals("MESSAGE_SHOT_BLUE_BUG"))SystemMessage.MESSAGE_SHOT_BLUE_BUG= messageData;
					if (messageType.equals("MESSAGE_SHOT_DEPENDIENTE"))SystemMessage.MESSAGE_SHOT_DEPENDIENTE= messageData;
					if (messageType.equals("MESSAGE_MERODEADORES_FEAR_SHOOT"))SystemMessage.MESSAGE_MERODEADORES_FEAR_SHOOT= messageData;
					if (messageType.equals("MESSAGE_DEPENDIENTE_SAID_YOU_CANT_PASS"))SystemMessage.MESSAGE_DEPENDIENTE_SAID_YOU_CANT_PASS= messageData;
					if (messageType.equals("MESSAGE_TURBOLASER"))SystemMessage.MESSAGE_TURBOLASER= messageData;
					if (messageType.equals("MESSAGE_NOTHING_TO_ACTIVATE"))SystemMessage.MESSAGE_NOTHING_TO_ACTIVATE= messageData;
					if (messageType.equals("MESSAGE_NOTHING_TO_ACTIVATE2"))SystemMessage.MESSAGE_NOTHING_TO_ACTIVATE2= messageData;
					if (messageType.equals("MESSAGE_SOLDIERS_ARE_HERE1"))SystemMessage.MESSAGE_SOLDIERS_ARE_HERE1= messageData;
					if (messageType.equals("MESSAGE_SOLDIERS_ARE_HERE2"))SystemMessage.MESSAGE_SOLDIERS_ARE_HERE2= messageData;
					if (messageType.equals("MESSAGE_OBJECT_INSIDE_OTHER_OBJECT_IMPOSSIBLE_TO_LEAVE"))SystemMessage.MESSAGE_OBJECT_INSIDE_OTHER_OBJECT_IMPOSSIBLE_TO_LEAVE= messageData;
					if (messageType.equals("MESSAGE_GUN_LOADED"))SystemMessage.MESSAGE_GUN_LOADED= messageData;
					if (messageType.equals("MESSAGE_GUN_UNLOADED"))SystemMessage.MESSAGE_GUN_UNLOADED= messageData;
					if (messageType.equals("MESSAGE_TRACTORAY_IS_ENABLE1"))SystemMessage.MESSAGE_TRACTORAY_IS_ENABLE1= messageData;
					if (messageType.equals("MESSAGE_TRACTORAY_IS_ENABLE2"))SystemMessage.MESSAGE_TRACTORAY_IS_ENABLE2= messageData;
					if (messageType.equals("MESSAGE_TRACTORAY_IS_ENABLE3"))SystemMessage.MESSAGE_TRACTORAY_IS_ENABLE3= messageData;
					if (messageType.equals("MESSAGE_TRACTORAY_IS_ENABLE4"))SystemMessage.MESSAGE_TRACTORAY_IS_ENABLE4= messageData;
					if (messageType.equals("MESSAGE_TRACTORAY_IS_ENABLE5"))SystemMessage.MESSAGE_TRACTORAY_IS_ENABLE5= messageData;
					if (messageType.equals("MESSAGE_C2P2_HAS_FEAR"))SystemMessage.MESSAGE_C2P2_HAS_FEAR= messageData;
					if (messageType.equals("MESSAGE_C2P2_HAS_FEAR2"))SystemMessage.MESSAGE_C2P2_HAS_FEAR2= messageData;
					if (messageType.equals("MESSAGE_C2P2_HAS_FEAR3"))SystemMessage.MESSAGE_C2P2_HAS_FEAR3= messageData;
					if (messageType.equals("MESSAGE_C2P2_HAS_FEAR4"))SystemMessage.MESSAGE_C2P2_HAS_FEAR4= messageData;
					if (messageType.equals("MESSAGE_C2P2_HAS_FEAR5"))SystemMessage.MESSAGE_C2P2_HAS_FEAR5= messageData;
					if (messageType.equals("MESSAGE_JAIL_EAST_LOCKED"))SystemMessage.MESSAGE_JAIL_EAST_LOCKED= messageData;
					if (messageType.equals("MESSAGE_JAIL_WEST_LOCKED"))SystemMessage.MESSAGE_JAIL_WEST_LOCKED= messageData;
					if (messageType.equals("MESSAGE_SHOT_EYE"))SystemMessage.MESSAGE_SHOT_EYE= messageData;
					if (messageType.equals("MESSAGE_C2P2_AND_EYE1"))SystemMessage.MESSAGE_C2P2_AND_EYE1= messageData; 
					if (messageType.equals("MESSAGE_C2P2_AND_EYE2"))SystemMessage.MESSAGE_C2P2_AND_EYE2= messageData;
					if (messageType.equals("MESSAGE_KILL_DARTH_WATER_FIRST"))SystemMessage.MESSAGE_KILL_DARTH_WATER_FIRST= messageData;
					if (messageType.equals("MESSAGE_OBI_AND_DARTHWATER"))SystemMessage.MESSAGE_OBI_AND_DARTHWATER= messageData;
					if (messageType.equals("MESSAGE_TOILET_OBI_CONVERSATION1"))SystemMessage.MESSAGE_TOILET_OBI_CONVERSATION1= messageData;
					if (messageType.equals("MESSAGE_TOILET_OBI_CONVERSATION2"))SystemMessage.MESSAGE_TOILET_OBI_CONVERSATION2= messageData;
					if (messageType.equals("MESSAGE_NO_MORE_PIS"))SystemMessage.MESSAGE_NO_MORE_PIS=messageData;
					if (messageType.equals("MESSAGE_NOT_BE_A_PIG"))SystemMessage.MESSAGE_NOT_BE_A_PIG=messageData;
					if (messageType.equals("MESSAGE_YOU_ARE_PISSING1"))SystemMessage.MESSAGE_YOU_ARE_PISSING1=messageData;
					if (messageType.equals("MESSAGE_YOU_ARE_PISSING2"))SystemMessage.MESSAGE_YOU_ARE_PISSING2=messageData;
					if (messageType.equals("MESSAGE_TROOPERS_BLOCK_EXIT"))SystemMessage.MESSAGE_TROOPERS_BLOCK_EXIT=messageData;
					if (messageType.equals("MESSAGE_TROOPERS_ARE_COMING1")) SystemMessage.MESSAGE_TROOPERS_ARE_COMING1=messageData;
					if (messageType.equals("MESSAGE_TROOPERS_ARE_COMING2")) SystemMessage.MESSAGE_TROOPERS_ARE_COMING2=messageData;
					if (messageType.equals("MESSAGE_NO_CONTROLPANEL_HERE")) SystemMessage.MESSAGE_NO_CONTROLPANEL_HERE=messageData;
					if (messageType.equals("MESSAGE_NO_PLACE_TO_HIDE")) SystemMessage.MESSAGE_NO_PLACE_TO_HIDE=messageData;
					if (messageType.equals("MESSAGE_YOU_ARE_HIDE1")) SystemMessage.MESSAGE_YOU_ARE_HIDE1=messageData;
					if (messageType.equals("MESSAGE_YOU_ARE_HIDE2")) SystemMessage.MESSAGE_YOU_ARE_HIDE2=messageData;
					if (messageType.equals("MESSAGE_YOU_ARE_HIDE3")) SystemMessage.MESSAGE_YOU_ARE_HIDE3=messageData;
					if (messageType.equals("MESSAGE_YOU_ARE_HIDE4")) SystemMessage.MESSAGE_YOU_ARE_HIDE4=messageData;
					if (messageType.equals("MESSAGE_TROOPERS_NOT_BLOCK_EXIT1")) SystemMessage.MESSAGE_TROOPERS_NOT_BLOCK_EXIT1=messageData;
					if (messageType.equals("MESSAGE_TROOPERS_NOT_BLOCK_EXIT2")) SystemMessage.MESSAGE_TROOPERS_NOT_BLOCK_EXIT2=messageData;
					if (messageType.equals("MESSAGE_TROOPERS_NOT_BLOCK_EXIT3")) SystemMessage.MESSAGE_TROOPERS_NOT_BLOCK_EXIT3=messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_FIGHTING1")) SystemMessage.MESSAGE_JUANSOLO_FIGHTING1=messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_FIGHTING2")) SystemMessage.MESSAGE_JUANSOLO_FIGHTING2=messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_FIGHTING3")) SystemMessage.MESSAGE_JUANSOLO_FIGHTING3=messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_FIGHTING4")) SystemMessage.MESSAGE_JUANSOLO_FIGHTING4=messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_FIGHTING5")) SystemMessage.MESSAGE_JUANSOLO_FIGHTING5=messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_FIGHTING6")) SystemMessage.MESSAGE_JUANSOLO_FIGHTING6=messageData;
					if (messageType.equals("MESSAGE_TAKEOFF_IMPOSSIBLE2")) SystemMessage.MESSAGE_TAKEOFF_IMPOSSIBLE2=messageData;
					if (messageType.equals("MESSAGE_YOU_ARE_IN_SPACE_NOW")) SystemMessage.MESSAGE_YOU_ARE_IN_SPACE_NOW=messageData;
					if (messageType.equals("MESSAGE_TRACTORRAY_IS_DISABLED1")) SystemMessage.MESSAGE_TRACTORRAY_IS_DISABLED1=messageData;
					if (messageType.equals("MESSAGE_TRACTORRAY_IS_DISABLED2")) SystemMessage.MESSAGE_TRACTORRAY_IS_DISABLED2=messageData;
					if (messageType.equals("MESSAGE_TRACTORRAY_IS_DISABLED3")) SystemMessage.MESSAGE_TRACTORRAY_IS_DISABLED3=messageData;
					if (messageType.equals("MESSAGE_TRACTORRAY_IS_DISABLED4")) SystemMessage.MESSAGE_TRACTORRAY_IS_DISABLED4=messageData;
					if (messageType.equals("MESSAGE_HALCON_IS_FLYING2")) SystemMessage.MESSAGE_HALCON_IS_FLYING2=messageData;
					if (messageType.equals("MESSAGE_HALCON_IS_FLYING3")) SystemMessage.MESSAGE_HALCON_IS_FLYING3=messageData;
					if (messageType.equals("MESSAGE_ACTIVATE_TURBOLASER")) SystemMessage.MESSAGE_ACTIVATE_TURBOLASER=messageData;
					if (messageType.equals("MESSAGE_RUNAWAY")) SystemMessage.MESSAGE_RUNAWAY=messageData;
					if (messageType.equals("MESSAGE_RUNAWAY2")) SystemMessage.MESSAGE_RUNAWAY2=messageData;
					if (messageType.equals("MESSAGE_RUNAWAY3")) SystemMessage.MESSAGE_RUNAWAY3=messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_PACA1")) SystemMessage.MESSAGE_JUANSOLO_PACA1=messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_PACA2")) SystemMessage.MESSAGE_JUANSOLO_PACA2=messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_PACA3")) SystemMessage.MESSAGE_JUANSOLO_PACA3=messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_PACA4")) SystemMessage.MESSAGE_JUANSOLO_PACA4=messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_RETURN1")) SystemMessage.MESSAGE_JUANSOLO_RETURN1=messageData;
					if (messageType.equals("MESSAGE_JUANSOLO_RETURN2")) SystemMessage.MESSAGE_JUANSOLO_RETURN2=messageData;					
					if (messageType.equals("MESSAGE_COMBAT_MODE")) SystemMessage.MESSAGE_COMBAT_MODE=messageData;
					if (messageType.equals("MESSAGE_COMBAT_DARTH_01")) SystemMessage.MESSAGE_COMBAT_DARTH_01=messageData;
					if (messageType.equals("MESSAGE_COMBAT_DARTH_02")) SystemMessage.MESSAGE_COMBAT_DARTH_02=messageData;
					if (messageType.equals("MESSAGE_COMBAT_DARTH_03")) SystemMessage.MESSAGE_COMBAT_DARTH_03=messageData;
					if (messageType.equals("MESSAGE_COMBAT_DARTH_04")) SystemMessage.MESSAGE_COMBAT_DARTH_04=messageData;
					if (messageType.equals("MESSAGE_COMBAT_DARTH_05")) SystemMessage.MESSAGE_COMBAT_DARTH_05=messageData;
					if (messageType.equals("MESSAGE_COMBAT_DARTH_06")) SystemMessage.MESSAGE_COMBAT_DARTH_06=messageData;
					if (messageType.equals("MESSAGE_OBI_DEATH1")) SystemMessage.MESSAGE_OBI_DEATH1=messageData;
					if (messageType.equals("MESSAGE_OBI_DEATH2")) SystemMessage.MESSAGE_OBI_DEATH2=messageData;
					if (messageType.equals("MESSAGE_OBI_DEATH3")) SystemMessage.MESSAGE_OBI_DEATH3=messageData;
					if (messageType.equals("MESSAGE_BOT_BLOCK_EXIT")) SystemMessage.MESSAGE_BOT_BLOCK_EXIT=messageData;
					if (messageType.equals("MESSAGE_R3D2_BOT1")) SystemMessage.MESSAGE_R3D2_BOT1=messageData;
					if (messageType.equals("MESSAGE_R3D2_BOT2")) SystemMessage.MESSAGE_R3D2_BOT2=messageData;
					if (messageType.equals("MESSAGE_R3D2_BOT3")) SystemMessage.MESSAGE_R3D2_BOT3=messageData;
					if (messageType.equals("MESSAGE_R3D2_BOT4")) SystemMessage.MESSAGE_R3D2_BOT4=messageData;
					if (messageType.equals("MESSAGE_R3D2_BOT5")) SystemMessage.MESSAGE_R3D2_BOT5=messageData;
					if (messageType.equals("MESSAGE_COMBAT_BOT_01")) SystemMessage.MESSAGE_COMBAT_BOT_01=messageData;
					if (messageType.equals("MESSAGE_COMBAT_BOT_02")) SystemMessage.MESSAGE_COMBAT_BOT_02=messageData;
					if (messageType.equals("MESSAGE_COMBAT_BOT_03")) SystemMessage.MESSAGE_COMBAT_BOT_03=messageData;
					if (messageType.equals("MESSAGE_COMBAT_BOT_04")) SystemMessage.MESSAGE_COMBAT_BOT_04=messageData;
					if (messageType.equals("MESSAGE_COMBAT_BOT_05")) SystemMessage.MESSAGE_COMBAT_BOT_05=messageData;
					if (messageType.equals("MESSAGE_COMBAT_BOT_06")) SystemMessage.MESSAGE_COMBAT_BOT_06=messageData;
					if (messageType.equals("MESSAGE_BOT_DEATH1")) SystemMessage.MESSAGE_BOT_DEATH1=messageData;
					if (messageType.equals("MESSAGE_BOT_DEATH2")) SystemMessage.MESSAGE_BOT_DEATH2=messageData;
					if (messageType.equals("MESSAGE_BOT_DEATH3")) SystemMessage.MESSAGE_BOT_DEATH3=messageData;
					if (messageType.equals("MESSAGE_BOT_DEATH4")) SystemMessage.MESSAGE_BOT_DEATH4=messageData;
					if (messageType.equals("MESSAGE_JAILS_OPEN1")) SystemMessage.MESSAGE_JAILS_OPEN1=messageData;
					if (messageType.equals("MESSAGE_JAILS_OPEN2")) SystemMessage.MESSAGE_JAILS_OPEN2=messageData;
					if (messageType.equals("MESSAGE_HELPING_JUAN1")) SystemMessage.MESSAGE_HELPING_JUAN1=messageData;
					if (messageType.equals("MESSAGE_HELPING_JUAN2")) SystemMessage.MESSAGE_HELPING_JUAN2=messageData;
					if (messageType.equals("MESSAGE_HELPING_JUAN3")) SystemMessage.MESSAGE_HELPING_JUAN3=messageData;
					if (messageType.equals("MESSAGE_RESCUE_PACA1")) SystemMessage.MESSAGE_RESCUE_PACA1=messageData;
					if (messageType.equals("MESSAGE_RESCUE_PACA2")) SystemMessage.MESSAGE_RESCUE_PACA2=messageData;
					if (messageType.equals("MESSAGE_RESCUE_PACA3")) SystemMessage.MESSAGE_RESCUE_PACA3=messageData;
					if (messageType.equals("MESSAGE_RESCUE_PACA4")) SystemMessage.MESSAGE_RESCUE_PACA4=messageData;
					if (messageType.equals("MESSAGE_RESCUE_PACA5")) SystemMessage.MESSAGE_RESCUE_PACA5=messageData;
					if (messageType.equals("MESSAGE_RESCUE_PACA6")) SystemMessage.MESSAGE_RESCUE_PACA6=messageData;
					if (messageType.equals("MESSAGE_RESCUE_PACA7")) SystemMessage.MESSAGE_RESCUE_PACA7=messageData;
					if (messageType.equals("MESSAGE_PACA_IS_HERE")) SystemMessage.MESSAGE_PACA_IS_HERE=messageData;
					if (messageType.equals("MESSAGE_R3D2_CONTROL_CENTER1")) SystemMessage.MESSAGE_R3D2_CONTROL_CENTER1=messageData;
					if (messageType.equals("MESSAGE_R3D2_CONTROL_CENTER2")) SystemMessage.MESSAGE_R3D2_CONTROL_CENTER2=messageData;
					if (messageType.equals("MESSAGE_R3D2_CONTROL_CENTER3")) SystemMessage.MESSAGE_R3D2_CONTROL_CENTER3=messageData;
					if (messageType.equals("MESSAGE_R3D2_CONTROL_CENTER4")) SystemMessage.MESSAGE_R3D2_CONTROL_CENTER4=messageData;
					if (messageType.equals("MESSAGE_AUTODESTRUCTION_CODE_INVALID")) SystemMessage.MESSAGE_AUTODESTRUCTION_CODE_INVALID=messageData;
					if (messageType.equals("MESSAGE_TRACTORRAY_CODE_INVALID")) SystemMessage.MESSAGE_TRACTORRAY_CODE_INVALID=messageData;
					if (messageType.equals("MESSAGE_AUTODESTRUCTION_CODE_VALID")) SystemMessage.MESSAGE_AUTODESTRUCTION_CODE_VALID=messageData;
					if (messageType.equals("MESSAGE_TRACTORRAY_CODE_VALID")) SystemMessage.MESSAGE_TRACTORRAY_CODE_VALID=messageData;
					if (messageType.equals("MESSAGE_WIN_BATTLE1")) SystemMessage.MESSAGE_WIN_BATTLE1=messageData;
					if (messageType.equals("MESSAGE_WIN_BATTLE2")) SystemMessage.MESSAGE_WIN_BATTLE2=messageData;
					if (messageType.equals("MESSAGE_WIN_BATTLE3")) SystemMessage.MESSAGE_WIN_BATTLE3=messageData;
					if (messageType.equals("MESSAGE_IMPOSSIBLE_ACTIVATE_TURBOLASER")) SystemMessage.MESSAGE_IMPOSSIBLE_ACTIVATE_TURBOLASER=messageData;
					if (messageType.equals("MESSAGE_TROOPERS_ARE_COMMING1")) SystemMessage.MESSAGE_TROOPERS_ARE_COMMING1=messageData;
					if (messageType.equals("MESSAGE_TROOPERS_ARE_COMMING2")) SystemMessage.MESSAGE_TROOPERS_ARE_COMMING2=messageData;
					if (messageType.equals("MESSAGE_STAR_IS_DESTROYED1")) SystemMessage.MESSAGE_STAR_IS_DESTROYED1=messageData;
					if (messageType.equals("MESSAGE_STAR_IS_DESTROYED2")) SystemMessage.MESSAGE_STAR_IS_DESTROYED2=messageData;
					if (messageType.equals("MESSAGE_STAR_IS_DESTROYED3")) SystemMessage.MESSAGE_STAR_IS_DESTROYED3=messageData;
					if (messageType.equals("MESSAGE_STAR_IS_DESTROYED4")) SystemMessage.MESSAGE_STAR_IS_DESTROYED4=messageData;
					if (messageType.equals("MESSAGE_STAR_IS_DESTROYED5")) SystemMessage.MESSAGE_STAR_IS_DESTROYED5=messageData;
					if (messageType.equals("MESSAGE_STAR_IS_DESTROYED6")) SystemMessage.MESSAGE_STAR_IS_DESTROYED6=messageData;
					if (messageType.equals("MESSAGE_STAR_IS_DESTROYED7")) SystemMessage.MESSAGE_STAR_IS_DESTROYED7=messageData;
					if (messageType.equals("MESSAGE_STAR_IS_DESTROYED8")) SystemMessage.MESSAGE_STAR_IS_DESTROYED8=messageData;
					if (messageType.equals("MESSAGE_AUTODESTRUCTION_15SEC1")) SystemMessage.MESSAGE_AUTODESTRUCTION_15SEC1=messageData;
					if (messageType.equals("MESSAGE_AUTODESTRUCTION_15SEC2")) SystemMessage.MESSAGE_AUTODESTRUCTION_15SEC2=messageData;
					if (messageType.equals("MESSAGE_AUTODESTRUCTION_15SEC3")) SystemMessage.MESSAGE_AUTODESTRUCTION_15SEC3=messageData;
					if (messageType.equals("MESSAGE_AUTODESTRUCTION_15SEC4")) SystemMessage.MESSAGE_AUTODESTRUCTION_15SEC4=messageData;
					if (messageType.equals("MESSAGE_GO_HOME1")) SystemMessage.MESSAGE_GO_HOME1=messageData;
					if (messageType.equals("MESSAGE_GO_HOME2")) SystemMessage.MESSAGE_GO_HOME2=messageData;
					if (messageType.equals("MESSAGE_VICTORY_JUAN")) SystemMessage.MESSAGE_VICTORY_JUAN=messageData;
					if (messageType.equals("MESSAGE_VICTORY_JUAN2")) SystemMessage.MESSAGE_VICTORY_JUAN2=messageData;
					if (messageType.equals("MESSAGE_VICTORY_PACA")) SystemMessage.MESSAGE_VICTORY_PACA=messageData;
					if (messageType.equals("MESSAGE_VICTORY_C2P2")) SystemMessage.MESSAGE_VICTORY_C2P2=messageData;
					
					if (messageType.equals("OBJECT_SELFPEELER")) SystemMessage.OBJECT_SELFPEELER = messageData;					
					if (messageType.equals("OBJECT_OILCAN")) SystemMessage.OBJECT_OILCAN = messageData;
					if (messageType.equals("OBJECT_MUNITION")) SystemMessage.OBJECT_MUNITION = messageData;
					if (messageType.equals("OBJECT_GUN")) SystemMessage.OBJECT_GUN = messageData;
					if (messageType.equals("OBJECT_CREDITS")) SystemMessage.OBJECT_CREDITS = messageData;
					if (messageType.equals("OBJECT_CUBE")) SystemMessage.OBJECT_CUBE = messageData;
					if (messageType.equals("OBJECT_WATER")) SystemMessage.OBJECT_WATER= messageData;
					if (messageType.equals("OBJECT_OIL")) SystemMessage.OBJECT_OIL= messageData;
					if (messageType.equals("OBJECT_PLECTRUM")) SystemMessage.OBJECT_PLECTRUM= messageData;
					if (messageType.equals("OBJECT_CARD")) SystemMessage.OBJECT_CARD= messageData;
					if (messageType.equals("OBJECT_BURGER")) SystemMessage.OBJECT_BURGER= messageData;
					if (messageType.equals("OBJECT_LASERSWORD")) SystemMessage.OBJECT_LASERSWORD= messageData;
					if (messageType.equals("OBJECT_RANURA")) SystemMessage.OBJECT_RANURA= messageData;
					if (messageType.equals("ACTOR_C2P2")) SystemMessage.ACTOR_C2P2= messageData;
					if (messageType.equals("ACTOR_R3D2")) SystemMessage.ACTOR_R3D2= messageData;
					if (messageType.equals("ACTOR_OBI")) SystemMessage.ACTOR_OBI= messageData;
					if (messageType.equals("ACTOR_JUAN")) SystemMessage.ACTOR_JUAN = messageData;
					if (messageType.equals("ACTOR_CHEQUEVACA")) SystemMessage.ACTOR_CHEQUEVACA = messageData;
					if (messageType.equals("ACTOR_SAPOIDE")) SystemMessage.ACTOR_SAPOIDE = messageData;
					if (messageType.equals("ACTOR_BLUE_BUG")) SystemMessage.ACTOR_BLUE_BUG= messageData;
					if (messageType.equals("ACTOR_JAMONEANO")) SystemMessage.ACTOR_JAMONEANO= messageData;
					if (messageType.equals("ACTOR_CHAVALA")) SystemMessage.ACTOR_CHAVALA= messageData;
					if (messageType.equals("ACTOR_DEPENDIENTE")) SystemMessage.ACTOR_DEPENDIENTE= messageData;
					if (messageType.equals("ACTOR_EYE")) SystemMessage.ACTOR_EYE= messageData;
					if (messageType.equals("ACTOR_DARTHWATER")) SystemMessage.ACTOR_DARTHWATER= messageData;
					if (messageType.equals("ACTOR_BOT")) SystemMessage.ACTOR_BOT= messageData;
					if (messageType.equals("ACTOR_PACA")) SystemMessage.ACTOR_PACA= messageData;
				}
			}
		} catch (IOException e) {                 
			System.out.println ("Error: " + e.getMessage());			             
		}		
	}
	
	public void loadActors(int resourceId){
		try {
			// Connect with Resources in /res/raw folder
			InputStream inputStream = context.getResources().openRawResource(resourceId);
			InputStreamReader inputreader = new InputStreamReader(inputStream, "ISO-8859-1");  
			
			BufferedReader buffreader = new BufferedReader(inputreader, 8192);
			
			String line = "";
			actors = new HashMap<String, Actor>();
			Actor actor= null;
			// Process text line
			
			while (( line = buffreader.readLine()) != null) {    
				String identificator = line.substring(0, AdventureCodes.MAX_IDENTIFICATOR);
				if (identificator.equals (AdventureCodes.INIT_DEFINITION)) {
					actor = new Actor();
				}
				if (identificator.equals (AdventureCodes.LOCATION_ID)) {					
					actor.setLocationId(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1) );
				}
				if (identificator.equals (AdventureCodes.IMAGEN_ID)) {
					actor.setImage(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.NAME_ID)) {
					actor.setName(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.TEXT_ID)) {
					actor.setLocationText(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}				
				if (identificator.equals (AdventureCodes.DESCRIPTION_ID)) {
					actor.setDescription(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.ACTION_ID)) {					
					StringTokenizer strTok = new StringTokenizer(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1), "@");
					String actionCommand = strTok.nextToken();
					String actionResponse = strTok.nextToken();
					actor.addAction(actionCommand, actionResponse);
				}
				if (identificator.equals (AdventureCodes.KILL_ID)) {					
					StringTokenizer strTok = new StringTokenizer(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1), "@");
					String killable = strTok.nextToken();
					actor.setKillable(killable);
					if (killable.equals(AdventureCodes.TRUE)){
						String killable_object = strTok.nextToken();
						String killable_message = strTok.nextToken();
						String killable_numberOfHits = strTok.nextToken();
						actor.setKillable_object(killable_object);
						actor.setKillable_message(killable_message);
						actor.setKillable_numberOfHits(Integer.valueOf(killable_numberOfHits));
					}
					
				}
				if (identificator.equals (AdventureCodes.AUTOMATIC_ACTION_ID)) {					
					//StringTokenizer strTok = new StringTokenizer(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1), "@");
					//String actionCommand = strTok.nextToken();					
					//actor.addAutomaticAction(actionCommand, actionCommand);
					String actionCommand = line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1);
					actor.addAutomaticAction(actionCommand,actionCommand);
					
				}
				if (identificator.equals (AdventureCodes.SYNONIMOUS_ID)) {					
					actor.addNameSynonimous(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.RANDOMIZE_ID)) {					
					actor.addRandomMessage(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.ASKED_ID)) {					
					actor.addAskedBehaviourId(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.GIVE_ID)) {					
					actor.addGiveBehaviourId(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.PUSH_ID)) {					
					actor.setPushBehaviour(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.SUICIDE_ID)) {					
					actor.setSuicideBehaviour(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.FOLLOW_ID)) {					
					actor.setFollowingBehaviour(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.END_DEFINITION)) {
					actors.put(actor.getName(), actor);					
				}
			}
			
		} catch (IOException e) {                 
			System.out.println ("Error: " + e.getMessage());			             
		}		
	}
	
	public void loadActorsBehaviours(int resourceId){
		try {
			// Connect with Resources in /res/raw folder
			InputStream inputStream = context.getResources().openRawResource(resourceId);
			InputStreamReader inputreader = new InputStreamReader(inputStream, "ISO-8859-1");  
			
			BufferedReader buffreader = new BufferedReader(inputreader, 8192);
			
			String line = "";
			actorsBehaviours = new HashMap<String, Behaviour>();
			Behaviour behaviour = null;
			// Process text line
			
			while (( line = buffreader.readLine()) != null) {    
				String identificator = line.substring(0, AdventureCodes.MAX_IDENTIFICATOR);
				if (identificator.equals (AdventureCodes.INIT_DEFINITION)) {
					behaviour = new Behaviour();
				}
				if (identificator.equals (AdventureCodes.BEHAVIOUR_ID)) {					
					behaviour.setBehaviourId(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1) );
				}
				if (identificator.equals (AdventureCodes.NAME_ID)) {
					behaviour.setName(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.LOCATION_ID)) {
					behaviour.setMoveActorTo(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}				
				if (identificator.equals (AdventureCodes.MOVE_PLAYER_ID)) {
					behaviour.setMovePlayerTo(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.OBJECTS_NEEDED_ID)) {					
					StringTokenizer strTok = new StringTokenizer(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1), "@");
					String objectName = strTok.nextToken();
					String objectMenssage = strTok.nextToken();
					behaviour.addObjectsNeeded(objectName, objectMenssage);
				}
				if (identificator.equals (AdventureCodes.OBJECTS_RETURNED_ID)) {					
					StringTokenizer strTok = new StringTokenizer(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1), "@");
					String objectName = strTok.nextToken();
					String objectMenssage = strTok.nextToken();
					behaviour.addObjectsReturned(objectName, objectMenssage);
				}
				if (identificator.equals (AdventureCodes.DESCRIPTION_ID)) {
					behaviour.setDescription(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.ACTIVE_ID)) {
					behaviour.setActive(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.TYPE_ID)) {
					behaviour.setType(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1));
				}
				if (identificator.equals (AdventureCodes.RANDOMIZE_ID)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(AdventureCodes.MAX_IDENTIFICATOR + 1), "@");					
					behaviour.setIsRandomizeBehaviour(strTok.nextToken());
					if (strTok.countTokens()>0){
						behaviour.setRandomizeMaxNumber(strTok.nextToken());
					}
				}
				if (identificator.equals (AdventureCodes.END_DEFINITION)) {
					actorsBehaviours.put(behaviour.getBehaviourId(), behaviour);					
				}
			}
			
		} catch (IOException e) {                 
			System.out.println ("Error: " + e.getMessage());			             
		}		
	}
	
	
	public void increaseActionsCounter() {
		this.actionsCounter++;
	}
	
	public int getActionsCounter(){
		return this.actionsCounter;
	}

	public int getScoreCounter(){
		return this.scoreCounter;
	}

	
	public AdventureLocation getCurrentLocation(){
		return this.currentLocation;
	}

	public void setCurrentLocation(String id){
		this.currentLocation = locations.get(id);		
	}
	
	public void printLocations(){
		for (int i=0; i<locations.size(); i++){
			//locations.get(i).print();
		}
	}
	
	public int move(String direction){
		// Preguntamos a la localidad actual si se puede mover a esa direccion
		// Si se puede mover cambiamos la localidad actual por la nueva
		// Si no se puede mover devolvemos un codigo de error

		if (direction.equals("SUBE")) direction = "SUBIR";
		if (direction.equals("BAJA")) direction = "BAJAR";
		
		
		String locationId = currentLocation.getConnectId(direction); 
		if (locationId.equals("") ){
			// No hay conexion con la direccion pasada como parametro
			return AdventureCodes.MOVE_ACTION_FAIL;
		} else {
			if (!isLocationVisited(locationId)){
				visitedLocations.put(locationId, locationId);
			}
			setCurrentLocation(locationId);
			return AdventureCodes.MOVE_ACTION_OK;
		}				
	}
	
	public String executeActorsBehaviours(){
		String aux = "";
		
		Iterator<?> it = actors.entrySet().iterator();
		Behaviour behaviour;
		while (it.hasNext()){ // Recorremos la lista de Actores
			Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			Actor actor = (Actor)e.getValue();
			if (!actor.getLocationId().equals(AdventureCodes.OBJECT_DESTROY)) { // Es decir, si el actor est� creado			   
				if ((!actor.getSuicideBehaviour().equals("")) && 
				    (currentLocation.getId().equals(actor.getLocationId()))){ // Tiene sucidio definido y estoy en la localidad
					behaviour = actorsBehaviours.get(actor.getSuicideBehaviour());
					if (behaviour.getRandomizeMaxNumber()==0){
						aux = aux + behaviour.getDescription();
						actor.setLocationId(behaviour.getMoveActorTo());
					} else {
						actorsBehaviours.get(actor.getSuicideBehaviour()).setRandomizeMaxNumber(String.valueOf(Integer.valueOf(behaviour.getRandomizeMaxNumber())-1));
					}
				}
				if (!actor.getFollowingBehaviour().equals("")) { // Tiene seguimiento habilitado
					behaviour = actorsBehaviours.get(actor.getFollowingBehaviour());
					if (behaviour.getActive().equals(AdventureCodes.TRUE)){
						actor.setLocationId(currentLocation.getId());
					}
				}
			}
		}
		return aux;
	}
	
	public int getBadWordsCounter(){
		return this.badWordsCounter;
	}
	
	public void increaseBadWordsCounter(){
		this.badWordsCounter++;
	}
	
	public String getDescription(String objectName){		
		try {
			// Si no hay luz y no llevamos la fuente de luz encendida no se ve un carajo
			if (currentLocation.getNeedLight().equals(AdventureCodes.TRUE) &&
				(isObjectWithLightHereAndTurnOn().equals(AdventureCodes.FALSE))) {
				return SystemMessage.MESSAGE_IS_VERY_DARK;
			} else {
				if (objectName.equals("")) {
					return SystemMessage.MESSAGE_UNKOWN_WHAT_DESCRIBE;
				} else {
					if (objects.containsKey(objectName)) { // Es un Objeto Normal				
						AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
						String aux = "";
						if ((currentLocation.getId().equals(object.getLocationId()) || 
							(object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)))
						   ){
							if (object.getIsContainer().equals(AdventureCodes.TRUE)){
								aux = object.getContainerObjectAsString() + "\n";
							}
							if (object.getChargeLevel()>=0){
								aux = aux + SystemMessage.MESSAGE_LEVEL_OF_CHARGE + object.getChargeLevel();
							}
							if (object.getFluidType()!=null){
								if ((object.getFluidType().equals("1")) && 
									(object.getStatus().equals(AdventureCodes.OBJECT_STATE_FULL))){
									aux = aux + " " + SystemMessage.MESSAGE_IS_FILL_WITH + " " + SystemMessage.OBJECT_WATER.toLowerCase() + SystemMessage.MESSAGE_DOT;
								} else if ((object.getFluidType().equals("2")) && 
										   (object.getStatus().equals(AdventureCodes.OBJECT_STATE_FULL))){
									aux = aux + " " + SystemMessage.MESSAGE_IS_FILL_WITH + " " + SystemMessage.OBJECT_OIL.toLowerCase() + SystemMessage.MESSAGE_DOT;
								}
							}
							return object.getDescription() + aux;
						} else {
							return SystemMessage.MESSAGE_UNKOWN_DESCRIPTION;
						}
					} else if (actors.containsKey(objectName)) { // Es un personaje
						Actor actor = actors.get(objectName); 
						if (currentLocation.getId().equals(actor.getLocationId())) {
							return actor.getDescription();
						} else {
							return SystemMessage.MESSAGE_UNKOWN_DESCRIPTION;
						}
						
				    } else { // Forma parte de las descripciones del paisaje o un sin�nimo	
				    	String aux = "";				    	
				    	
				    	if (getSynonimous(objectName).length()>0) { 
				    		return getSynonimous(objectName);
				    	} else {
				    		return getOthers(objectName);
				    	}	
				    	
				    }				    
				}
			}
		} catch (Exception e){
			return SystemMessage.MESSAGE_UNKOWN_DESCRIPTION;
		}
	}

	private String getSynonimous(String objectName){
		String aux = "";
		Iterator<?> it = actors.entrySet().iterator();				    	
		while (it.hasNext()){
			 Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			 Actor actor = (Actor)e.getValue();
			 if (actor.getNameSynonimousList()!=null){
				 if (actor.getNameSynonimousList().contains(objectName.toUpperCase())){
					 aux = aux + " " + actor.getDescription();			  					 
				 }
			 }
		}
		return aux;
	}
	
	private String getOthers(String objectName){
		Description description = descriptions.get(currentLocation.getId() + "-" + objectName);
		if (currentLocation.getId().equals(description.getLocationId())) {
			if (description.getReturnObject().equals("")) { // La descripcion no devuelve objetos
				return description.getDescription();
			} else {
				if (description.getObjectReturned().equals(AdventureCodes.FALSE)){ // Es decir todavia no hemos devuelto el objeto del mensaje									
					((AdventureObject<?,?>)objects.get(description.getReturnObject())).setLocationId(currentLocation.getId());
					description.setObjectReturned(AdventureCodes.TRUE);
					return description.getDescription();
				} else {
					return description.getAuxiliaryDescription();
				}
			}
		} else {
			return SystemMessage.MESSAGE_UNKOWN_DESCRIPTION;
		}
	}
	
	public String kill(String actorName){
		if (isActorHere(actorName)){
			Actor actor = getActor(actorName);
			if (actor.getKillable().equals(AdventureCodes.FALSE)){
				return SystemMessage.MESSAGE_IMPOSSIBLE_KILL;
			} else {				
				if ((isObjectTaken(actor.getKillable_object())) ||
					(actor.getKillable_object().equals("HAND"))
					){
					actor.setLocationId(AdventureCodes.OBJECT_DESTROY);
					return actor.getKillable_message();
				} else {
					return SystemMessage.MESSAGE_KILL_IMPOSSIBLE;
				}				
			} 
		} else {
			return SystemMessage.MESSAGE_IMPOSSIBLE_KILL;
		}
	}
	
	public String ad(String actorName){		
		return SystemMessage.MESSAGE_AD;
	}
	
	public String push(String actorName){
		String aux = "";
		if (actorName.equals("")){
			aux = SystemMessage.MESSAGE_INSERT_ACTOR_NAME;
		} else {
			Actor actor = actors.get(actorName);
			if (actor == null) { // Hemos indicado un nombre pero no est� definido en la BBDD
				aux = SystemMessage.MESSAGE_THIS_ACTOR_IS_NOT_HERE;
				aux = aux.replace("*", actorName.toLowerCase());
			} else {
				if (!actor.getLocationId().equals(currentLocation.getId())) { // El Actor est� definido en BBDD pero no est� aqu�
					aux = SystemMessage.MESSAGE_THIS_ACTOR_IS_NOT_HERE;
					aux = aux.replace("*", actorName.toLowerCase());
				} else { // El Actor est� aqu�
					// Verificamos si tiene definido comportamiento de empujar
					if (actor.getPushBehaviour().equals("")){ // No tienen definido este comportamiento se devuelve mensaje estandar.
						aux = SystemMessage.MESSAGE_ACTOR_NOT_PUSH_BEHAVIOUR;
						aux = aux.replace("*", actorName.toLowerCase());
					} else { // Tiene definido comportamiento de empujar.
						Behaviour behaviour = actorsBehaviours.get(actor.getPushBehaviour());
						actors.get(actor.getName()).setLocationId(behaviour.getMoveActorTo()); 
						aux = behaviour.getDescription();
						
					}
				}
			}
		}
		return aux;
	}
	
	public String getCurrentLocationObjects(){		
		String aux = SystemMessage.MESSAGE_VISIBLE_OBJECTS;
		Iterator<?> it = objects.entrySet().iterator();
		String objectsString = "";
		String objectText = "";
		int index = 0;
		while (it.hasNext()){
			Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
			if (object.getLocationId().equals(currentLocation.getId())){
				if (!object.getLocationText().equals("")){
					objectText = object.getLocationText();
				} else {
					objectText = object.getArticle() + " " + object.getNameWithStatus().toLowerCase();
				}
				if (object.getShow().equals(AdventureCodes.TRUE)){
					if (getTotalLocationObjects() == 1) {
						objectsString = "- " + objectText + SystemMessage.MESSAGE_DOT;
					} else {
						//if (index == (getTotalLocationObjects() - 1)){
						//	objectsString = objectsString + SystemMessage.MESSAGE_AND + " " + objectText + SystemMessage.MESSAGE_DOT;
						//} else {
							objectsString = "- " + objectText + SystemMessage.MESSAGE_DOT + "\n" + objectsString;
						//}					
					}
					index++;
				}
			}			
		}
		if (index > 0){
			return aux + "\n" + objectsString;
		} else {
			return objectsString;
		}
	}

	public void savePart1(){
		try {  
	      File dir = new File(Environment.getExternalStorageDirectory() + "/vajillas");
	      	      
	      if(!dir.exists()){
	    	  dir.mkdir();
	      }
	      GlobalInformation globalInformation = GlobalInformation.getInstance();
	      
	      File file = new File(dir, "save1.db");
	      FileWriter writer = new FileWriter(file);
	      writer.append("LOCATIONID=").append(currentLocation.getId()).append("\n");
	      writer.append("TOTALACTIONS=").append(String.valueOf(actionsCounter)).append("\n");
	      writer.append("ROBOTSFUERA=").append(String.valueOf(globalInformation.getRobotsFuera())).append("\n");
	      writer.append("IS_PLECTRUM_CUT=").append(String.valueOf(globalInformation.getIsPlectrumCut())).append("\n");
	      writer.append("DOORSLOCKED=").append(String.valueOf(globalInformation.getDoorsLocked())).append("\n");
	      writer.append("ENGINEISBROKEN=").append(String.valueOf(globalInformation.getEngineIsBroken())).append("\n");
	      writer.append("HYPERSPACEISBROKEN=").append(String.valueOf(globalInformation.getHyperspaceIsBroken())).append("\n");
	      writer.append("INTHESPACE=").append(String.valueOf(globalInformation.getInTheSpace())).append("\n");
	      writer.append("CHRISCOUNTER=").append(String.valueOf(globalInformation.getChrisCounter())).append("\n");
	      writer.append("ENGINEHIT=").append(String.valueOf(globalInformation.getEngineHit())).append("\n");
	      writer.append("HITS=").append(String.valueOf(globalInformation.getHits())).append("\n");
	      writer.append("MERODEADORES=").append(String.valueOf(globalInformation.getMerodeadores())).append("\n");
	      writer.append("SOLDIERS=").append(String.valueOf(globalInformation.getSoldiers())).append("\n");
	      writer.append("JUAN=").append(String.valueOf(globalInformation.getJuan())).append("\n");

	      writer.append(SystemMessage.OBJECT_SELFPEELER + "=" + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_SELFPEELER)).getLocationId() + "@"
	                                                          + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_SELFPEELER)).getStatus()  + "@"
	    		  										      + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_SELFPEELER)).getImagePosition() + "\n");

	      writer.append(SystemMessage.OBJECT_OILCAN + "=" + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_OILCAN)).getLocationId() + "@"
	                                                      + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_OILCAN)).getStatus()  + "@"
	                                                      + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_OILCAN)).getImagePosition() + "\n");
	      
	      writer.append(SystemMessage.OBJECT_MUNITION + "=" + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_MUNITION)).getLocationId() + "@"
	                                                        + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_MUNITION)).getStatus() + "@"
	                                                        + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition() + "\n");
	      
	      writer.append(SystemMessage.OBJECT_GUN + "=" + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_GUN)).getLocationId() + "@"
				                                       + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_GUN)).getStatus() + "@"
				                                       + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_GUN)).getImagePosition() + "\n");
	      
	      writer.append(SystemMessage.OBJECT_CREDITS + "=" + ((AdventureObject<?, ?>)  objects.get(SystemMessage.OBJECT_CREDITS)).getLocationId() + "@"
	    		  										   + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_CREDITS)).getStatus() + "@"
	    		  										   + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition() + "\n");
	      
	      writer.append(SystemMessage.OBJECT_CUBE + "=" + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_CUBE)).getLocationId() + "@"
				  								        + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_CUBE)).getStatus()  + "@"
				                                        + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_CUBE)).getImagePosition() + "\n");
	      
	      writer.append(SystemMessage.OBJECT_LASERSWORD + "=" + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getLocationId() + "@"
														      + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getStatus()  + "@"
														      + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getImagePosition() + "\n");

	      writer.append(SystemMessage.OBJECT_PLECTRUM + "=" + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_PLECTRUM)).getLocationId() + "@"
											  				+ ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_PLECTRUM)).getStatus()  + "@"
				                                            + ((AdventureObject<?, ?>) objects.get(SystemMessage.OBJECT_PLECTRUM)).getImagePosition() + "\n");

	      
	      writer.append(SystemMessage.ACTOR_OBI).append("=").append(actors.get(SystemMessage.ACTOR_OBI).getLocationId()).append("@").append(actorsBehaviours.get(actors.get(SystemMessage.ACTOR_OBI).getFollowingBehaviour()).getActive()).append("\n");
	      writer.append(SystemMessage.ACTOR_C2P2).append("=").append(actors.get(SystemMessage.ACTOR_C2P2).getLocationId()).append("@").append(actorsBehaviours.get(actors.get(SystemMessage.ACTOR_C2P2).getFollowingBehaviour()).getActive()).append("\n");
	      writer.append(SystemMessage.ACTOR_R3D2).append("=").append(actors.get(SystemMessage.ACTOR_R3D2).getLocationId()).append("@").append(actorsBehaviours.get(actors.get(SystemMessage.ACTOR_R3D2).getFollowingBehaviour()).getActive()).append("\n");
	      writer.append(SystemMessage.ACTOR_JUAN).append("=").append(actors.get(SystemMessage.ACTOR_JUAN).getLocationId()).append("@").append(actorsBehaviours.get(actors.get(SystemMessage.ACTOR_JUAN).getFollowingBehaviour()).getActive()).append("\n");
	      writer.append(SystemMessage.ACTOR_CHEQUEVACA).append("=").append(actors.get(SystemMessage.ACTOR_CHEQUEVACA).getLocationId()).append("@").append(actorsBehaviours.get(actors.get(SystemMessage.ACTOR_CHEQUEVACA).getFollowingBehaviour()).getActive()).append("\n");
	      writer.append(SystemMessage.ACTOR_BLUE_BUG).append("=").append(actors.get(SystemMessage.ACTOR_BLUE_BUG).getLocationId()).append("@").append("\n");
	      writer.append(SystemMessage.ACTOR_SAPOIDE).append("=").append(actors.get(SystemMessage.ACTOR_SAPOIDE).getLocationId()).append("@").append("\n");
	      writer.append(SystemMessage.ACTOR_DEPENDIENTE).append("=").append(actors.get(SystemMessage.ACTOR_DEPENDIENTE).getLocationId()).append("@").append("\n");
	      
	      writer.flush();
	      writer.close();
	      
	     
	     }
	     catch(IOException e)
	     {
	      e.printStackTrace();
	     
	     }	
	}
	
	public void savePart2(){
		try {  
			GlobalInformation globalInformation = GlobalInformation.getInstance();
	      File dir = new File(Environment.getExternalStorageDirectory() + "/vajillas");
	      	      
	      if(!dir.exists()){
	    	  dir.mkdir();
	      }
	      	      
	      File file = new File(dir, "save2.db");
	      FileWriter writer = new FileWriter(file);
	      writer.append("LOCATIONID=" + currentLocation.getId() + "\n");
	      writer.append("TOTALACTIONS=" + actionsCounter + "\n");
	      writer.append("DOORWESTLOCKED=" + globalInformation.getDoorWestLocked()+ "\n");
	      writer.append("DOOREASTLOCKED=" + globalInformation.getDoorEastLocked()+ "\n");
	      writer.append("TRACTORRAYENABLED=" + globalInformation.getTractorRayEnabled()+ "\n");
	      writer.append("AUTODESTROYEDENABLED=" + globalInformation.getAutoDestroyEnabled() + "\n");
	      writer.append("INTHESPACE=" + globalInformation.getInTheSpace() + "\n");					 		    
	 	  writer.append("SOLDIERS=" + globalInformation.getSoldiers() + "\n");					 		        
	 	  writer.append("HIDDEN=" + globalInformation.getHidden()+ "\n");
	 	  writer.append("JUAN=" + globalInformation.getJuan() + "\n");		      
	 	  writer.append("EYE=" + globalInformation.getEye() + "\n");
	 	  writer.append("PACA2=" + globalInformation.getPaca() + "\n");
	 	  writer.append("TOTALSHIPSDESTROYED=" + globalInformation.getTotalShipsDestroyed() + "\n");
	 	  writer.append("COUNTDOWNFORDESTRUCTION=" + globalInformation.getCountDownForDestruction() + "\n");

	      writer.append(SystemMessage.OBJECT_MUNITION + "=" + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getLocationId() + "@"
	                                                        + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getStatus()  + "@"
	    		  										    + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition() + "\n");
	      
	      writer.append(SystemMessage.OBJECT_GUN + "=" + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).getLocationId() + "@"
				                                       + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).getStatus() + "@"
				                                       + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).getImagePosition() + "\n");
	      
	      writer.append(SystemMessage.OBJECT_CREDITS + "=" + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).getLocationId() + "@"
	    		  										   + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).getStatus() + "@"
	    		  										   + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition() + "\n");
	      
	      writer.append(SystemMessage.OBJECT_LASERSWORD + "=" + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getLocationId() + "@"
				                                              + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getStatus()  + "@"
				                                              + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getImagePosition() + "\n");

	      writer.append(SystemMessage.OBJECT_BURGER + "=" + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).getLocationId() + "@"
				                                          + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).getStatus()  + "@"
				                                          + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).getImagePosition() + "\n");

	      writer.append(SystemMessage.OBJECT_CARD + "=" + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).getLocationId() + "@"
				                                        + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).getStatus()  + "@"
				                                        + ((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).getImagePosition() + "\n");

	      
	      writer.append(SystemMessage.ACTOR_OBI + "=" + actors.get(SystemMessage.ACTOR_OBI).getLocationId() + "@" + actorsBehaviours.get(actors.get(SystemMessage.ACTOR_OBI).getFollowingBehaviour()).getActive() + "\n");
	      writer.append(SystemMessage.ACTOR_C2P2 + "=" + actors.get(SystemMessage.ACTOR_C2P2).getLocationId() + "@" + actorsBehaviours.get(actors.get(SystemMessage.ACTOR_C2P2).getFollowingBehaviour()).getActive() + "\n");
	      writer.append(SystemMessage.ACTOR_R3D2 + "=" + actors.get(SystemMessage.ACTOR_R3D2).getLocationId() + "@" + actorsBehaviours.get(actors.get(SystemMessage.ACTOR_R3D2).getFollowingBehaviour()).getActive() + "\n");
	      writer.append(SystemMessage.ACTOR_JUAN + "=" + actors.get(SystemMessage.ACTOR_JUAN).getLocationId() + "@" + actorsBehaviours.get(actors.get(SystemMessage.ACTOR_JUAN).getFollowingBehaviour()).getActive() + "\n");
	      writer.append(SystemMessage.ACTOR_CHEQUEVACA + "=" + actors.get(SystemMessage.ACTOR_CHEQUEVACA).getLocationId() + "@" + actorsBehaviours.get(actors.get(SystemMessage.ACTOR_CHEQUEVACA).getFollowingBehaviour()).getActive() + "\n");
	      writer.append(SystemMessage.ACTOR_PACA + "=" + actors.get(SystemMessage.ACTOR_PACA).getLocationId() + "@" + actorsBehaviours.get(actors.get(SystemMessage.ACTOR_PACA).getFollowingBehaviour()).getActive() + "\n");
	      writer.append(SystemMessage.ACTOR_EYE + "=" + actors.get(SystemMessage.ACTOR_EYE).getLocationId()  + "@"+ "\n");
	      writer.append(SystemMessage.ACTOR_DARTHWATER + "=" + actors.get(SystemMessage.ACTOR_DARTHWATER).getLocationId()  + "@" + "\n");
	      writer.append(SystemMessage.ACTOR_BOT + "=" + actors.get(SystemMessage.ACTOR_BOT).getLocationId() + "@" + "\n");
	   
	      
	      
	      writer.flush();
	      writer.close();
	      
	     
	     }
	     catch(IOException e)
	     {
	      e.printStackTrace();
	     
	     }
	    
	   

	
	}

	public boolean loadPart1(){
		try {
			// Connect with Resources in /res/raw folder
			File dir = new File(Environment.getExternalStorageDirectory() + "/vajillas/save1.db");
			
			FileReader inputreader = new FileReader(dir);  
			
			BufferedReader buffreader = new BufferedReader(inputreader, 8192);
			
			String line = "";
			// Process text line
			GlobalInformation globalInformation = GlobalInformation.getInstance();
			while (( line = buffreader.readLine()) != null) {    
				StringTokenizer key = new StringTokenizer(line,"=");				
				String identificator = key.nextToken();
				if (identificator.equals ("LOCATIONID")) {
					currentLocation = locations.get(line.substring(identificator.length() + 1));
				}				
				if (identificator.equals ("TOTALACTIONS")) {
					actionsCounter = Integer.valueOf(line.substring(identificator.length() + 1));
				}				
				if (identificator.equals ("ROBOTSFUERA")) {
					globalInformation.setRobotsFuera(Boolean.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("IS_PLECTRUM_CUT")) {
					globalInformation.setIsPlectrumCut(Boolean.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("DOORSLOCKED")) {
					globalInformation.setDoorsLocked(Boolean.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("ENGINEISBROKEN")) {
					globalInformation.setEngineIsBroken(Boolean.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("HYPERSPACEISBROKEN")) {
					globalInformation.setHyperspaceIsBroken(Boolean.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("INTHESPACE")) {
					globalInformation.setInTheSpace(Boolean.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("CHRISCOUNTER")) {
					globalInformation.setChrisCounter(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("ENGINEHIT")) {
					globalInformation.setEngineHit(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("HITS")) {
					globalInformation.setHits(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("MERODEADORES")) {
					globalInformation.setMerodeadores(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("SOLDIERS")) {
					globalInformation.setSoldiers(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("JUAN")) {
					globalInformation.setJuan(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
			
				if (identificator.equals (SystemMessage.OBJECT_SELFPEELER)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_SELFPEELER)).setLocationId(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_SELFPEELER)).setStatus(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_SELFPEELER)).setImagePosition(Integer.valueOf(strTok.nextToken()));
					if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_SELFPEELER)).getImagePosition()==1){
						globalInformation.setImage01_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_SELFPEELER)).getImagePosition()==2){
						globalInformation.setImage02_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_SELFPEELER)).getImagePosition()==3){
						globalInformation.setImage03_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_SELFPEELER)).getImagePosition()==4){
						globalInformation.setImage04_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_SELFPEELER)).getImagePosition()==5){
						globalInformation.setImage05_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_SELFPEELER)).getImagePosition()==6){
						globalInformation.setImage06_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_SELFPEELER)).getImagePosition()==7){
						globalInformation.setImage07_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_SELFPEELER)).getImagePosition()==8){
						globalInformation.setImage08_free(false);
					}
				}
				if (identificator.equals (SystemMessage.OBJECT_OILCAN )) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_OILCAN)).setLocationId(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_OILCAN)).setStatus(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_OILCAN)).setImagePosition(Integer.valueOf(strTok.nextToken()));
					if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_OILCAN)).getImagePosition()==1){
						globalInformation.setImage01_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_OILCAN)).getImagePosition()==2){
						globalInformation.setImage02_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_OILCAN)).getImagePosition()==3){
						globalInformation.setImage03_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_OILCAN)).getImagePosition()==4){
						globalInformation.setImage04_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_OILCAN)).getImagePosition()==5){
						globalInformation.setImage05_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_OILCAN)).getImagePosition()==6){
						globalInformation.setImage06_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_OILCAN)).getImagePosition()==7){
						globalInformation.setImage07_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_OILCAN)).getImagePosition()==8){
						globalInformation.setImage08_free(false);
					}
				}
				if (identificator.equals (SystemMessage.OBJECT_MUNITION)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).setLocationId(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).setStatus(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).setImagePosition(Integer.valueOf(strTok.nextToken()));
					if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==1){
						globalInformation.setImage01_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==2){
						globalInformation.setImage02_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==3){
						globalInformation.setImage03_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==4){
						globalInformation.setImage04_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==5){
						globalInformation.setImage05_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==6){
						globalInformation.setImage06_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==7){
						globalInformation.setImage07_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==8){
						globalInformation.setImage08_free(false);
					}
				}
				if (identificator.equals (SystemMessage.OBJECT_GUN)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_GUN)).setLocationId(strTok.nextToken());
					((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_GUN)).setStatus(strTok.nextToken());
					((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_GUN)).setImagePosition(Integer.valueOf(strTok.nextToken()));
					if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==1){
						globalInformation.setImage01_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==2){
						globalInformation.setImage02_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==3){
						globalInformation.setImage03_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==4){
						globalInformation.setImage04_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==5){
						globalInformation.setImage05_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==6){
						globalInformation.setImage06_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==7){
						globalInformation.setImage07_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==8){
						globalInformation.setImage08_free(false);
					}
				}
				if (identificator.equals (SystemMessage.OBJECT_CREDITS)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CREDITS)).setLocationId(strTok.nextToken());
					((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CREDITS)).setStatus(strTok.nextToken());
					((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CREDITS)).setImagePosition(Integer.valueOf(strTok.nextToken()));
					if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==1){
						globalInformation.setImage01_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==2){
						globalInformation.setImage02_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==3){
						globalInformation.setImage03_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==4){
						globalInformation.setImage04_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==5){
						globalInformation.setImage05_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==6){
						globalInformation.setImage06_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==7){
						globalInformation.setImage07_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==8){
						globalInformation.setImage08_free(false);
					}
				}	
				if (identificator.equals (SystemMessage.OBJECT_CUBE)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CUBE)).setLocationId(strTok.nextToken());
					((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CUBE)).setStatus(strTok.nextToken());
					((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CUBE)).setImagePosition(Integer.valueOf(strTok.nextToken()));
					if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CUBE)).getImagePosition()==1){
						globalInformation.setImage01_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CUBE)).getImagePosition()==2){
						globalInformation.setImage02_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CUBE)).getImagePosition()==3){
						globalInformation.setImage03_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CUBE)).getImagePosition()==4){
						globalInformation.setImage04_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CUBE)).getImagePosition()==5){
						globalInformation.setImage05_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CUBE)).getImagePosition()==6){
						globalInformation.setImage06_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CUBE)).getImagePosition()==7){
						globalInformation.setImage07_free(false);
					} else if (((AdventureObject<?,?>)objects.get(SystemMessage.OBJECT_CUBE)).getImagePosition()==8){
						globalInformation.setImage08_free(false);
					}
				}
				
				if (identificator.equals (SystemMessage.ACTOR_OBI)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_OBI).setLocationId(strTok.nextToken());
					actorsBehaviours.get(actors.get(SystemMessage.ACTOR_OBI).getFollowingBehaviour()).setActive(strTok.nextToken());
				}		
				if (identificator.equals (SystemMessage.ACTOR_C2P2)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_C2P2).setLocationId(strTok.nextToken());
					actorsBehaviours.get(actors.get(SystemMessage.ACTOR_C2P2).getFollowingBehaviour()).setActive(strTok.nextToken());
				}	
				if (identificator.equals (SystemMessage.ACTOR_R3D2)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_R3D2).setLocationId(strTok.nextToken());
					actorsBehaviours.get(actors.get(SystemMessage.ACTOR_R3D2).getFollowingBehaviour()).setActive(strTok.nextToken());
				}	
				if (identificator.equals (SystemMessage.ACTOR_JUAN)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_JUAN).setLocationId(strTok.nextToken());
					actorsBehaviours.get(actors.get(SystemMessage.ACTOR_JUAN).getFollowingBehaviour()).setActive(strTok.nextToken());
				}	
				if (identificator.equals (SystemMessage.ACTOR_CHEQUEVACA)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_CHEQUEVACA).setLocationId(strTok.nextToken());
					actorsBehaviours.get(actors.get(SystemMessage.ACTOR_CHEQUEVACA).getFollowingBehaviour()).setActive(strTok.nextToken());
				}	
				if (identificator.equals (SystemMessage.ACTOR_BLUE_BUG)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_BLUE_BUG).setLocationId(strTok.nextToken());
					
				}	
				if (identificator.equals (SystemMessage.ACTOR_SAPOIDE)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_SAPOIDE).setLocationId(strTok.nextToken());					
				}	
				if (identificator.equals (SystemMessage.ACTOR_DEPENDIENTE)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_DEPENDIENTE).setLocationId(strTok.nextToken());					
				}	
			}
			buffreader.close();
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	public boolean loadPart2(){
		try {
			// Connect with Resources in /res/raw folder
			File dir = new File(Environment.getExternalStorageDirectory() + "/vajillas/save2.db");
			
			FileReader inputreader = new FileReader(dir);  
			
			BufferedReader buffreader = new BufferedReader(inputreader, 8192);
			
			String line = "";
			// Process text line
			GlobalInformation globalInformation = GlobalInformation.getInstance();
			while (( line = buffreader.readLine()) != null) {    
				StringTokenizer key = new StringTokenizer(line,"=");
				String identificator = key.nextToken();
				Log.d("AVENTURA", identificator);
				if (identificator.equals ("LOCATIONID")) {
					currentLocation = locations.get(line.substring(identificator.length() + 1));
				}
				if (identificator.equals ("TOTALACTIONS")) {
					actionsCounter = Integer.valueOf(line.substring(identificator.length() + 1));
				}
				if (identificator.equals ("DOORWESTLOCKED")) {
					globalInformation.setDoorWestLocked(Boolean.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("DOOREASTLOCKED")) {
					globalInformation.setDoorEastLocked(Boolean.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("TRACTORRAYENABLED")) {
					globalInformation.setTractorRayEnabled(Boolean.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("AUTODESTROYEDENABLED")) {
					globalInformation.setAutoDestroyEnabled(Boolean.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("INTHESPACE")) {
					globalInformation.setInTheSpace(Boolean.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("SOLDIERS")) {
					globalInformation.setSoldiers(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("HIDDEN")) {
					globalInformation.setHidden(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("JUAN")) {
					globalInformation.setJuan(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("EYE")) {
					globalInformation.setEye(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("PACA2")) {
					globalInformation.setPaca(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("TOTALSHIPSDESTROYED")) {
					globalInformation.setTotalShipsDestroyed(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
				if (identificator.equals ("COUNTDOWNFORDESTRUCTION")) {
					globalInformation.setCountDownForDestruction(Integer.valueOf(line.substring(identificator.length() + 1)));
				}
				
				
				if (identificator.equals (SystemMessage.OBJECT_LASERSWORD)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).setLocationId(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).setStatus(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).setImagePosition(Integer.valueOf(strTok.nextToken()));
					if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getImagePosition()==1){
						globalInformation.setImage01_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getImagePosition()==2){
						globalInformation.setImage02_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getImagePosition()==3){
						globalInformation.setImage03_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getImagePosition()==4){
						globalInformation.setImage04_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getImagePosition()==5){
						globalInformation.setImage05_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getImagePosition()==6){
						globalInformation.setImage06_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getImagePosition()==7){
						globalInformation.setImage07_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_LASERSWORD)).getImagePosition()==8){
						globalInformation.setImage08_free(false);
					}
				}
				
				if (identificator.equals (SystemMessage.OBJECT_MUNITION)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).setLocationId(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).setStatus(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).setImagePosition(Integer.valueOf(strTok.nextToken()));
					if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==1){
						globalInformation.setImage01_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==2){
						globalInformation.setImage02_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==3){
						globalInformation.setImage03_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==4){
						globalInformation.setImage04_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==5){
						globalInformation.setImage05_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==6){
						globalInformation.setImage06_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==7){
						globalInformation.setImage07_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_MUNITION)).getImagePosition()==8){
						globalInformation.setImage08_free(false);
					}
				}
				
				if (identificator.equals (SystemMessage.OBJECT_GUN )) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).setLocationId(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).setStatus(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).setImagePosition(Integer.valueOf(strTok.nextToken()));
					if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==1){
						globalInformation.setImage01_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==2){
						globalInformation.setImage02_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==3){
						globalInformation.setImage03_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==4){
						globalInformation.setImage04_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==5){
						globalInformation.setImage05_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==6){
						globalInformation.setImage06_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==7){
						globalInformation.setImage07_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_GUN)).getImagePosition()==8){
						globalInformation.setImage08_free(false);
					}
				}
				
				if (identificator.equals (SystemMessage.OBJECT_CREDITS)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).setLocationId(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).setStatus(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).setImagePosition(Integer.valueOf(strTok.nextToken()));
					if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==1){
						globalInformation.setImage01_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==2){
						globalInformation.setImage02_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==3){
						globalInformation.setImage03_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==4){
						globalInformation.setImage04_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==5){
						globalInformation.setImage05_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==6){
						globalInformation.setImage06_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==7){
						globalInformation.setImage07_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CREDITS)).getImagePosition()==8){
						globalInformation.setImage08_free(false);
					}
				}	
				if (identificator.equals (SystemMessage.OBJECT_BURGER)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).setLocationId(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).setStatus(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).setImagePosition(Integer.valueOf(strTok.nextToken()));
					if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).getImagePosition()==1){
						globalInformation.setImage01_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).getImagePosition()==2){
						globalInformation.setImage02_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).getImagePosition()==3){
						globalInformation.setImage03_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).getImagePosition()==4){
						globalInformation.setImage04_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).getImagePosition()==5){
						globalInformation.setImage05_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).getImagePosition()==6){
						globalInformation.setImage06_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).getImagePosition()==7){
						globalInformation.setImage07_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_BURGER)).getImagePosition()==8){
						globalInformation.setImage08_free(false);
					}
				}
				if (identificator.equals (SystemMessage.OBJECT_CARD)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).setLocationId(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).setStatus(strTok.nextToken());
					((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).setImagePosition(Integer.valueOf(strTok.nextToken()));
					if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).getImagePosition()==1){
						globalInformation.setImage01_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).getImagePosition()==2){
						globalInformation.setImage02_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).getImagePosition()==3){
						globalInformation.setImage03_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).getImagePosition()==4){
						globalInformation.setImage04_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).getImagePosition()==5){
						globalInformation.setImage05_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).getImagePosition()==6){
						globalInformation.setImage06_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).getImagePosition()==7){
						globalInformation.setImage07_free(false);
					} else if (((AdventureObject<?,?>) objects.get(SystemMessage.OBJECT_CARD)).getImagePosition()==8){
						globalInformation.setImage08_free(false);
					}
				}
		
				if (identificator.equals (SystemMessage.ACTOR_OBI)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_OBI).setLocationId(strTok.nextToken());
					actorsBehaviours.get(actors.get(SystemMessage.ACTOR_OBI).getFollowingBehaviour()).setActive(strTok.nextToken());
				}		
				if (identificator.equals (SystemMessage.ACTOR_C2P2)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_C2P2).setLocationId(strTok.nextToken());
					actorsBehaviours.get(actors.get(SystemMessage.ACTOR_C2P2).getFollowingBehaviour()).setActive(strTok.nextToken());
				}	
				if (identificator.equals (SystemMessage.ACTOR_R3D2)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_R3D2).setLocationId(strTok.nextToken());
					actorsBehaviours.get(actors.get(SystemMessage.ACTOR_R3D2).getFollowingBehaviour()).setActive(strTok.nextToken());
				}	
				if (identificator.equals (SystemMessage.ACTOR_JUAN)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_JUAN).setLocationId(strTok.nextToken());
					actorsBehaviours.get(actors.get(SystemMessage.ACTOR_JUAN).getFollowingBehaviour()).setActive(strTok.nextToken());
				}	
				if (identificator.equals (SystemMessage.ACTOR_CHEQUEVACA)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_CHEQUEVACA).setLocationId(strTok.nextToken());
					actorsBehaviours.get(actors.get(SystemMessage.ACTOR_CHEQUEVACA).getFollowingBehaviour()).setActive(strTok.nextToken());
				}	
				if (identificator.equals (SystemMessage.ACTOR_DARTHWATER)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_DARTHWATER).setLocationId(strTok.nextToken());					
				}	
				if (identificator.equals (SystemMessage.ACTOR_BOT)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_BOT).setLocationId(strTok.nextToken());					
				}	
				if (identificator.equals (SystemMessage.ACTOR_EYE)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_EYE).setLocationId(strTok.nextToken());					
				}	
				if (identificator.equals (SystemMessage.ACTOR_PACA)) {
					StringTokenizer strTok = new StringTokenizer(line.substring(identificator.length() + 1),"@");
					actors.get(SystemMessage.ACTOR_PACA).setLocationId(strTok.nextToken());
					actorsBehaviours.get(actors.get(SystemMessage.ACTOR_PACA).getFollowingBehaviour()).setActive(strTok.nextToken());
				}
				
						
			}
			buffreader.close();
			return true;
		} catch (Exception e){
			return false;
		}
	}
	

	
	
	public String getCurrentLocationActors(){		
		String aux = "";
		Iterator<?> it = actors.entrySet().iterator();
		String actorsString = "";
		int index = 0;
		while (it.hasNext()){
			Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			Actor actor = (Actor)e.getValue();
			if (actor.getLocationId().equals(currentLocation.getId())){
				if (getTotalLocationActors() == 1) {
					actorsString = "- " + actor.getLocationText();
				} else {
					//if (index == (getTotalLocationActors() - 1)){
					//	actorsString = actorsString + SystemMessage.MESSAGE_AND + " " + actor.getLocationText();
					//} else {
						actorsString = "- " + actor.getLocationText() + "\n" + actorsString;
					//}					
				}
				index++;
			}
			
		}
		if (index > 0){
			aux = "Aqu� est�n:\n";
			return aux + actorsString;
		} else {
			return actorsString;
		}
	}
	
	
	public String getCurrentLocationExits(){
		String aux = SystemMessage.MESSAGE_VISIBLE_EXITS;
		String exitsString = "";
		Vector<Exit> exits = currentLocation.getExits();
		if (exits.size() == 1) {
			exitsString = exits.get(0).getDirectionName() + SystemMessage.MESSAGE_DOT;
			return aux + exitsString;
		} else {
			for (int index=0; index<exits.size(); index++){
				Exit exit = exits.get(index);
				if (index == (exits.size()-1)){
					exitsString = exitsString  + SystemMessage.MESSAGE_AND + " " + exit.getDirectionName() + SystemMessage.MESSAGE_DOT;
				} else {
					exitsString = exit.getDirectionName() + ", " + exitsString;
				}				
			}
			return aux + exitsString;
		}
	}

	public String getInventary(){
		String aux = SystemMessage.MESSAGE_INVENTARY_STRING;
		Iterator<?> it = objects.entrySet().iterator();
		String objectsString = "";
		int index = 0;
		while (it.hasNext()){
			Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
			if (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
				if (getTotalObjectsTaken() == 1) {
					objectsString = object.getArticle().toLowerCase() + " " + object.getNameWithStatus().toLowerCase() + SystemMessage.MESSAGE_DOT;
				} else {
					if (index == (getTotalObjectsTaken() - 1)){
						objectsString = objectsString + SystemMessage.MESSAGE_AND + " " + object.getArticle().toLowerCase() + " " +object.getNameWithStatus().toLowerCase() + SystemMessage.MESSAGE_DOT;
					} else {
						objectsString = object.getArticle().toLowerCase() + " " + object.getNameWithStatus().toLowerCase() + ", " + objectsString;
					}					
				}
				index++;
			}
			
		}
		if (index > 0){
			return aux + objectsString;
		} else {
			return aux = aux + SystemMessage.MESSAGE_INVENTARY_EMPTY;		
		}
		
	}
	
	public boolean isObjectTaken(String objectName){
		boolean aux = false;
		AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
		if (object != null){
			if (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
				aux = true;
			}
		}
		return aux;
	}
	
	public boolean isObjectInsideOther(String objectName){
		boolean aux = false;
		AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
		if (object != null){
			if (object.getLocationId().equals(AdventureCodes.OBJECT_IN_OTHER_OBJECT)){
				aux = true;
			}
		}
		return aux;
	}
	
	@SuppressWarnings("unchecked")
	public String catchObject(String objectName){		
		GlobalInformation globalInformation = GlobalInformation.getInstance();
		if (getTotalObjectsTaken() == AdventureCodes.MAX_OBJECTS) {
			return SystemMessage.MESSAGE_CANT_CARRY_MORE_OBJECTS;
		} else if (isObjectTaken(objectName)) {
			return SystemMessage.MESSAGE_CANT_CARRY_MORE_OBJECTS_BECAUSE_I_HAVE;
		} else {
			if (objectName.equals(SystemMessage.MESSAGE_ALL)) {
				String hasCatch = SystemMessage.MESSAGE_HAS_CAUGHT;
				String aux = "";
				int index = 0; 
				int total = getTotalLocationObjects();
				if (total > 0) {	
					Iterator<?> it = objects.entrySet().iterator();
					while (it.hasNext()){
						Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
						AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
						
							if (object.getLocationId().equals(currentLocation.getId())){
								if ((object.getDistance().equals(AdventureCodes.NEAR) &&
										(object.getSize()<10))){	
									if (total == 1){
										aux = object.getArticle().toLowerCase() + 
											   " " + object.getName().toLowerCase() + SystemMessage.MESSAGE_DOT;
									} else if (index == total ) {
										aux = aux + SystemMessage.MESSAGE_AND + " " + object.getArticle().toLowerCase() + 
											   " " +  object.getName().toLowerCase() + 
									   SystemMessage.MESSAGE_DOT;
								} else {
									aux = aux + object.getArticle().toLowerCase() + 
											   " " +  object.getName().toLowerCase() + ", ";									   
								}
							
								object.setLocationId(AdventureCodes.OBJECT_TAKEN);
								
								if (globalInformation.isImage01_free()){
									object.setImagePosition(1);
									globalInformation.setImage01_free(false);
								} else if (globalInformation.isImage02_free()){
									object.setImagePosition(2);
									globalInformation.setImage02_free(false);
								} else if (globalInformation.isImage03_free()){
									object.setImagePosition(3);
									globalInformation.setImage03_free(false);
								} else if (globalInformation.isImage04_free()){
									object.setImagePosition(4);
									globalInformation.setImage04_free(false);
								} else if (globalInformation.isImage05_free()){
									object.setImagePosition(5);
									globalInformation.setImage05_free(false);
								} else if (globalInformation.isImage06_free()){
									object.setImagePosition(6);
									globalInformation.setImage06_free(false);
								} else if (globalInformation.isImage07_free()){
									object.setImagePosition(7);
									globalInformation.setImage07_free(false);
								} else if (globalInformation.isImage08_free()){
									object.setImagePosition(8);
									globalInformation.setImage08_free(false);
								}
								
							} else if (object.getDistance().equals(AdventureCodes.FAR)) {
								aux = aux + SystemMessage.MESSAGE_OBJECT_IS_FAR;
								aux = aux.replace("*", object.getArticle().toLowerCase() + " " + object.getName().toLowerCase());
							} 
						}

						index++;
					}
					if (aux.equals("")) { // Es decir, no hay nada que coger
						return hasCatch + ": " + SystemMessage.MESSAGE_NOTHING;
					} else { 
						return hasCatch + " " + aux;
					}
				} else {
					return SystemMessage.MESSAGE_CANT_CAUGHT_THAT;
				}
			} else {
				AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
				if (object == null) {
					return SystemMessage.MESSAGE_CANT_CAUGHT_THAT;
				} else if (object.getSize()>=10) {
					String aux = SystemMessage.MESSAGE_OBJECT_IS_HEAVY;
					aux = aux.replace("*", object.getArticle().toLowerCase() + " " + object.getName().toLowerCase());			
					return aux;
				} else {
					if (object.getLocationId().equals(currentLocation.getId())){
						if (object.getDistance().equals(AdventureCodes.NEAR)){
								object.setLocationId(AdventureCodes.OBJECT_TAKEN);
								if (globalInformation.isImage01_free()){
									object.setImagePosition(1);
									globalInformation.setImage01_free(false);
								} else if (globalInformation.isImage02_free()){
									object.setImagePosition(2);
									globalInformation.setImage02_free(false);
								} else if (globalInformation.isImage03_free()){
									object.setImagePosition(3);
									globalInformation.setImage03_free(false);
								} else if (globalInformation.isImage04_free()){
									object.setImagePosition(4);
									globalInformation.setImage04_free(false);
								} else if (globalInformation.isImage05_free()){
									object.setImagePosition(5);
									globalInformation.setImage05_free(false);
								} else if (globalInformation.isImage06_free()){
									object.setImagePosition(6);
									globalInformation.setImage06_free(false);
								} else if (globalInformation.isImage07_free()){
									object.setImagePosition(7);
									globalInformation.setImage07_free(false);
								} else if (globalInformation.isImage08_free()){
									object.setImagePosition(8);
									globalInformation.setImage08_free(false);
								}
								return SystemMessage.MESSAGE_HAS_CAUGHT + " " +
								   object.getArticle().toLowerCase() + 
								   " " + 
								   object.getLongName().toLowerCase() + 
								   SystemMessage.MESSAGE_DOT;
							
						
						} else {
							String aux = SystemMessage.MESSAGE_OBJECT_IS_FAR;
							aux = aux.replace("*", object.getArticle().toLowerCase() + " " + object.getName().toLowerCase());
							return aux;
						}
						
					} else {
						return SystemMessage.MESSAGE_CANT_CAUGHT_THAT;
					}			
				}
			
			}
		}
	}
	
	public String eat(String objectName){
		String aux = "";
		if (objects.containsKey(objectName)){
			if (isObjectTaken(objectName)){
				if (((AdventureObject<?,?>) objects.get(objectName)).getIsFood().equals(AdventureCodes.TRUE)){
					((AdventureObject<?,?>) objects.get(objectName)).setLocationId(AdventureCodes.OBJECT_EATEN);
					aux = SystemMessage.MESSAGE_EAT;
					aux = aux.replace("*", ((AdventureObject<?,?>) objects.get(objectName)).getArticle().toLowerCase() + " " +
							((AdventureObject<?,?>) objects.get(objectName)).getLongName().toLowerCase());
							
				} else {
					aux = SystemMessage.MESSAGE_CANT_EAT_THAT;
				}
			} else {
				aux = SystemMessage.MESSAGE_DONT_HAVE_THAT;
			}
		} else {
			aux = SystemMessage.MESSAGE_CANT_DO_THAT;
		}
		
		return aux;
	}
	
	public String leaveObject(String objectName){
		GlobalInformation globalInformation = GlobalInformation.getInstance();
		if (objectName.equals(SystemMessage.MESSAGE_ALL)) {
			String hasLeave = SystemMessage.MESSAGE_HAS_LEAVE;
			String aux = "";
			int index = 0; 
			int total = getTotalObjectsTaken();
			if (total > 0) {
				Iterator<?> it = objects.entrySet().iterator();
				while (it.hasNext()){
					Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
					AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
					if ((((AdventureObject<?,?>) object).getLocationId().equals(AdventureCodes.OBJECT_TAKEN)) ||
					    (object.getLocationId().equals(AdventureCodes.OBJECT_IN_OTHER_OBJECT))){
						if (total == 1)
						{
							aux = object.getArticle().toLowerCase() +
									   " " +
							   object.getName().toLowerCase() + SystemMessage.MESSAGE_DOT;
						} else if (index == total ) {
							aux = aux + SystemMessage.MESSAGE_AND + " " + object.getArticle().toLowerCase() +
									   " " +
							   object.getName().toLowerCase() +
							   SystemMessage.MESSAGE_DOT;
						} else {
							aux = aux + object.getArticle().toLowerCase() +
									   " " +
							   object.getName().toLowerCase() + ", ";

						}
						if (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
							object.setLocationId(currentLocation.getId());
						}
						if (object.getImagePosition()==1){
							globalInformation.setImage01_free(true);
						}if (object.getImagePosition()==2){
							globalInformation.setImage02_free(true);
						}if (object.getImagePosition()==3){
							globalInformation.setImage03_free(true);
						}if (object.getImagePosition()==4){
							globalInformation.setImage04_free(true);
						}if (object.getImagePosition()==5){
							globalInformation.setImage05_free(true);
						}if (object.getImagePosition()==6){
							globalInformation.setImage06_free(true);
						}if (object.getImagePosition()==7){
							globalInformation.setImage07_free(true);
						}if (object.getImagePosition()==8){
							globalInformation.setImage08_free(true);
						}
						object.setImagePosition(0);
					}
					index++;
				}
				return hasLeave + aux;
			} else {
				return SystemMessage.MESSAGE_INVENTARY_STRING + " " + SystemMessage.MESSAGE_INVENTARY_EMPTY;
			}
		} else {
			AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
			if (object == null) {
				return SystemMessage.MESSAGE_CANT_LEAVE_THAT;
			} else {
				if (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
					object.setLocationId(currentLocation.getId());
					if (object.getImagePosition()==1){
						globalInformation.setImage01_free(true);
					}if (object.getImagePosition()==2){
						globalInformation.setImage02_free(true);
					}if (object.getImagePosition()==3){
						globalInformation.setImage03_free(true);
					}if (object.getImagePosition()==4){
						globalInformation.setImage04_free(true);
					}if (object.getImagePosition()==5){
						globalInformation.setImage05_free(true);
					}if (object.getImagePosition()==6){
						globalInformation.setImage06_free(true);
					}if (object.getImagePosition()==7){
						globalInformation.setImage07_free(true);
					}if (object.getImagePosition()==8){
						globalInformation.setImage08_free(true);
					}

					return SystemMessage.MESSAGE_HAS_LEAVE + 
						   object.getArticle().toLowerCase() + 
						   " " + 
						   objectName.toLowerCase() + 
						   SystemMessage.MESSAGE_DOT;
				} else if (object.getLocationId().equals(AdventureCodes.OBJECT_IN_OTHER_OBJECT)) {
					String aux = SystemMessage.MESSAGE_OBJECT_INSIDE_OTHER_OBJECT_IMPOSSIBLE_TO_LEAVE;
					aux = aux.replace("*", object.getArticle() + " " + object.getLongName());					
					return aux;
				} else {
					return SystemMessage.MESSAGE_DONT_HAVE_THAT;
				}			
			}
		}
	}

	public String shake(String objectName){
		String aux = "";		
		aux = SystemMessage.MESSAGE_CANT_DO_THAT;
		return aux;
	}
	
	public String checkAdventureConditions(){
		String aux = "";
		
		return aux;
	}
	
	public String open(String objectName){
		String aux = "";
		if (!currentLocation.getId().equals("005") && (adventurePart.equals("PART1"))){
			if (objects.containsKey(objectName) && (isObjectTaken(objectName)==true)){
				aux = SystemMessage.MESSAGE_OBJECT_NOT_OPENABLE;
				aux = aux.replace("*", ((AdventureObject<?,?>) objects.get(objectName)).getArticle().toLowerCase() + " " + ((AdventureObject<?,?>) objects.get(objectName)).getName().toLowerCase() );
			} else {
				aux = SystemMessage.MESSAGE_NO_OPEN_OBJECTS;
			}
		} else {
			if (objects.containsKey(objectName) && (isObjectTaken(objectName)==true)) {
				aux = SystemMessage.MESSAGE_OBJECT_NOT_OPENABLE;
				aux = aux.replace("*",   ((AdventureObject<?,?>) objects.get(objectName)).getArticle().toLowerCase() + " " + ((AdventureObject<?,?>) objects.get(objectName)).getName().toLowerCase() );
			} else {			
				aux = SystemMessage.MESSAGE_NO_OPEN_OBJECTS;				
			}
		}
		return aux;
	}
	
	public String fallDownObject(String objectName){
		String aux = "";
		if (objectName.equals(SystemMessage.MESSAGE_ALL)) {			
			Iterator<?> it = objects.entrySet().iterator();
			while (it.hasNext()){
				Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
				AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
				if (object.getLocationId().equals("025") && (object.getDistance().equals(AdventureCodes.NEAR))){						
					object.setLocationId("023");
					object.setDistance(AdventureCodes.NEAR);
					object.setLocationText("");						
					aux = aux + "\n" + SystemMessage.MESSAGE_OBJECT_HAS_FALLDOWN;
					aux = aux.replace("*", object.getArticle().toLowerCase() + " " + object.getName().toLowerCase());
				}				
			}
			return aux;
		} else {
			AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
			if (object == null) {
				return SystemMessage.MESSAGE_CANT_LEAVE_THAT;
			} else {
				if (object.getLocationId().equals("025")){
					object.setLocationId("023");
					object.setDistance(AdventureCodes.NEAR);
					object.setLocationText("");	
					aux = SystemMessage.MESSAGE_OBJECT_HAS_FALLDOWN;
					aux = aux.replace("*", object.getArticle().toLowerCase() + " " + object.getName().toLowerCase());					
					return aux;
				} else {
					return SystemMessage.MESSAGE_DONT_HAVE_THAT;
				}			
			}
		}
	}
	public int getTotalLocationObjects(){
		if (objects != null){
			Iterator<?> it = objects.entrySet().iterator();
			int index = 0;
			while (it.hasNext()){
				Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
				AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
				if (object.getLocationId().equals(currentLocation.getId())){
					index++;
				}			
			}
			return index;
		} else {
			return 0;
		}
	}
	
	public int getTotalLocationActors(){
		if (actors != null){
			Iterator<?> it = actors.entrySet().iterator();
			int index = 0;
			while (it.hasNext()){
				Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
				Actor actor = (Actor)e.getValue();
				if (actor.getLocationId().equals(currentLocation.getId())){
					index++;
				}			
			}
			return index;
		} else {
			return 0;
		}
	}
	
	public int getTotalObjectsTaken(){
		if (objects != null){
			Iterator<?> it = objects.entrySet().iterator();
			int index = 0;
			while (it.hasNext()){
				Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
				AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
				if (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
					index++;
				}			
			}
			return index;
		} else {
			return 0;
		}
	}

	public String say(String actorName, String action){
		String aux = "";
		if (actorName.equals("")){
			aux = SystemMessage.MESSAGE_INSERT_ACTOR_NAME;
		} else if (getTotalLocationActors() == 0) { // Si no hay actores no podemos hablar con ellos
			aux = SystemMessage.MESSAGE_THIS_ACTOR_IS_NOT_HERE;
			aux = aux.replace("*", actorName);
		} else { // Hay actores
			Actor actor = actors.get(actorName);
			if (actor == null) { // Hemos indicado un nombre pero no est� definido en la BBDD
				aux = SystemMessage.MESSAGE_THIS_ACTOR_IS_NOT_HERE;
				aux = aux.replace("*", actorName);
			} else {
				if (!actor.getLocationId().equals(currentLocation.getId())) {
					aux = SystemMessage.MESSAGE_THIS_ACTOR_IS_NOT_HERE;
					aux = aux.replace("*", actorName);						
				} else {
					aux = actor.getActionMessage(action);						
				}
			}				
		}		
		return aux;
	}
	
	public String give(String actorName, String objectName){
		String aux = "";
		if (getTotalLocationActors() == 0) { // Si no hay actores no podemos hablar con ellos
			aux = SystemMessage.MESSAGE_THIS_ACTOR_IS_NOT_HERE;
			aux = aux.replace("*", actorName);
		} else { // Hay actores
			if (actorName.equals("")){ // No hemos dicho a quien se lo queremos dar
				aux = SystemMessage.MESSAGE_INSERT_ACTOR_NAME;
			} else {
				Actor actor = actors.get(actorName);
				if (actor == null) { // Hemos indicado un nombre pero no est� definido en la BBDD
					aux = SystemMessage.MESSAGE_THIS_ACTOR_IS_NOT_HERE;
					aux = aux.replace("*", actorName.toLowerCase());
				} else {
					if (!actor.getLocationId().equals(currentLocation.getId())) { // El Actor est� definido en BBDD pero no est� aqu�
						aux = SystemMessage.MESSAGE_THIS_ACTOR_IS_NOT_HERE;
						aux = aux.replace("*", actorName.toLowerCase());
					} else {
						if (isObjectTaken(objectName)==true) { // Verificamos que llevemos cogido el objeto que queremos dar
							// Verificamos si est� definido el comportamiento dar para el actor						
							Vector<String> giveBehaviourList = actor.getGiveBehaviourList();
							if (giveBehaviourList == null) { // No ha definido comportamientos para dar
								aux = SystemMessage.MESSAGE_ACTOR_WITHOUT_GIVE_BEHAVIOUR;
								actorName = actorName.substring(0, 1).toUpperCase() + actorName.substring(1, actorName.length()).toLowerCase();
								aux = aux.replace("*", actorName);
							} else {
								// Recorremos la lista de comportamientos para ver si hay
								// una que se ajuste al objeto se�alado
								String giveBehaviourId = "";
								Behaviour behaviour;
								for (int index=0; index<giveBehaviourList.size(); index++){
									giveBehaviourId = giveBehaviourList.get(index);
									behaviour = actorsBehaviours.get(giveBehaviourId);
									if (behaviour.isObjectNeeded(objectName)){
										// El comportamiento esta asociado al objeto que llevamos.
										// - Mostramos el mensaje asociado al objeto requerido.
										// - Movemos el objeto que llevamos a objeto destruido.
										// - Verificamos si la acci�n lleva como resultado la devolucion de otro objeto 
										//   en cuyo caso lo ponemos en la localidad actual.
										// - Si hay que mover al actor lo movemos.
										aux = behaviour.getObjectNeededMessage(objectName);
										GlobalInformation globalInformation = GlobalInformation.getInstance();
										((AdventureObject<?,?>) objects.get(objectName)).setLocationId(AdventureCodes.OBJECT_DESTROY);
										if (((AdventureObject<?,?>) objects.get(objectName)).getImagePosition()==1){
											globalInformation.setImage01_free(true);
										}if (((AdventureObject<?,?>) objects.get(objectName)).getImagePosition()==2){
											globalInformation.setImage02_free(true);
										}if (((AdventureObject<?,?>) objects.get(objectName)).getImagePosition()==3){
											globalInformation.setImage03_free(true);
										}if (((AdventureObject<?,?>) objects.get(objectName)).getImagePosition()==4){
											globalInformation.setImage04_free(true);
										}if (((AdventureObject<?,?>) objects.get(objectName)).getImagePosition()==5){
											globalInformation.setImage05_free(true);
										}if (((AdventureObject<?,?>) objects.get(objectName)).getImagePosition()==6){
											globalInformation.setImage06_free(true);
										}if (((AdventureObject<?,?>) objects.get(objectName)).getImagePosition()==7){
											globalInformation.setImage07_free(true);
										}
										((AdventureObject<?,?>) objects.get(objectName)).setImagePosition(0);
										
										HashMap<String, String> objectsReturnedList = behaviour.getObjectsReturned();
										
										Iterator<?> it = objectsReturnedList.entrySet().iterator();
										 
										String objectNameAux;
										while (it.hasNext()){
											Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
											objectNameAux = (String)e.getKey();
											aux = aux + "\n" + (String)e.getValue(); // Anexamos los mensajes para los diferentes objetos.
											((AdventureObject<?,?>) objects.get(objectNameAux)).setLocationId(currentLocation.getId());
											
										}
										
										if (behaviour.getMoveActorTo().equals("PLAYER")){
											actor.setLocationId(currentLocation.getId());
										} else {
											actor.setLocationId(behaviour.getMoveActorTo());
										}
										
									
									}
								}
								if (aux.equals("")){ 
									// Al llegar hasta aqu� significa que aunque hay comportamientos definidos
									// ninguno se corresponde con el objeto que queremos dar.									
									aux = SystemMessage.MESSAGE_ACTOR_WITHOUT_GIVE_BEHAVIOUR;
									aux = aux.replace("*", actorName);
								}
							}
						} else { // El objeto que quiero dar no lo llevo cogido
							aux = SystemMessage.MESSAGE_DONT_HAVE_THAT;
						}
					}
				}
			}	
		}		
		return aux;
	}
	
	public String fillObject(String objectName){
		String aux = "";
		if (isObjectTaken(objectName)==true){
			AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
			if (object.getContainsFluid().equals(AdventureCodes.TRUE)){
				if (currentLocation.getHasFluid().equals(AdventureCodes.TRUE)){					
					object.setStatus(SystemMessage.MESSAGE_FULL);
					object.setFluidType(currentLocation.getFluidType());
					aux = SystemMessage.MESSAGE_YOU_FILL + object.getArticle().toLowerCase() + " " + objectName.toLowerCase() + SystemMessage.MESSAGE_DOT;
				} else {
					aux = SystemMessage.MESSAGE_NO_LIQUID_HERE;
				}
			} else {
				aux = SystemMessage.MESSAGE_CANT_DO_THAT;
			}
		} else {
			aux = SystemMessage.MESSAGE_CANT_DO_THAT;
		}
		return aux;
	}
	
	public String emptyObject(String objectName){
		String aux = "";
		if (isObjectTaken(objectName)){
			AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
			assert object != null;
			if (object.getContainsFluid().equals(AdventureCodes.TRUE)){
				if (object.getStatus().equals(AdventureCodes.OBJECT_STATE_FULL)) {
					object.setStatus(SystemMessage.MESSAGE_EMPTY);
					aux = SystemMessage.MESSAGE_HAS_EMPTY_OBJECT;
				} else {
					aux = SystemMessage.MESSAGE_OBJECT_IS_EMPTY_YET;	
				}
			} else {
				aux = SystemMessage.MESSAGE_CANT_DO_THAT;
			}
		} else {
			aux = SystemMessage.MESSAGE_CANT_DO_THAT;
		}
		return aux;
	}
	
	public String drink(String objectName){
		String aux = "";
		if (isObjectTaken(objectName)){
			AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
			assert object != null;
			if (object.getContainsFluid().equals(AdventureCodes.TRUE)){
				if (object.getStatus().equals(AdventureCodes.OBJECT_STATE_FULL)){					
					if (object.getFluidType().equals("1")) {
						object.setStatus(AdventureCodes.OBJECT_STATE_EMPTY);
						aux = SystemMessage.MESSAGE_DRINK_LIQUID;
					} else {
						aux = SystemMessage.MESSAGE_DONT_DRINK_THAT;
					}
				} else { 
					aux = SystemMessage.MESSAGE_OBJECT_HAS_NO_LIQUID;	
				}
								
			} else {
				aux = SystemMessage.MESSAGE_CANT_DO_THAT;
			}
		} else {
			aux = SystemMessage.MESSAGE_DRINK_FROM_WHERE;
		}
		return aux;
	}

	public String put(String objectName1, String objectName2){
		// objectName1 es lo que quiero poner
		// objectName2 es donde quiero ponerlo
		String aux = "";
		
		// Primero comprobamos que los objetos son objetos reales
		AdventureObject<?, ?> object1 = (AdventureObject<?, ?>) objects.get(objectName1);
		AdventureObject<?, ?> object2 = (AdventureObject<?, ?>) objects.get(objectName2);
		
		if ((object1 == null) || (object2 == null)) {
			aux = SystemMessage.MESSAGE_DONT_HAVE_THAT;
		
		} else {
			if (object1.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
				if (object2.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
					if (object2.getIsContainer().equals(AdventureCodes.TRUE)){
						if (object1.getSize()<object2.getSize()) { // Solo puedo meter objetos que sean mas peque�os que otro dado
							object2.addContainerObject(object1);
							object1.setLocationId(AdventureCodes.OBJECT_IN_OTHER_OBJECT);
							aux = SystemMessage.MESSAGE_HAVE_PUT_OBJECT_IN;
							aux = aux.replace("*", object1.getArticle().toLowerCase() + " " + object1.getName().toLowerCase());
							aux = aux.replace("#", object2.getArticle().toLowerCase() + " " + object2.getName().toLowerCase());
						} else {
							aux = SystemMessage.MESSAGE_DONT_HAVE_THIS_OBJECT_IN_THIS_OTHER_OBJECT;
							aux = aux.replace("*", object1.getArticle().toLowerCase() + " " + object1.getName().toLowerCase());
							aux = aux.replace("#", object2.getArticle().toLowerCase() + " " + object2.getName().toLowerCase());
						}
					} else { // El objeto destino no es contenedor
						aux = SystemMessage.MESSAGE_DONT_HAVE_PUT_OBJECT_IN;
						aux = aux.replace("*", object2.getArticle().toLowerCase() + " " + object2.getName().toLowerCase()) + SystemMessage.MESSAGE_DOT;
					}
				} else { // No llevamos el segundo de los objetos
					aux = SystemMessage.MESSAGE_DONT_HAVE_THAT_OBJECT;
					aux = aux.replace("*", object2.getArticle().toLowerCase() + " " + object2.getName().toLowerCase());
				}
			} else { // No llevamos el primero de los objectos.
				aux = SystemMessage.MESSAGE_DONT_HAVE_THAT_OBJECT;
				aux = aux.replace("*", object1.getArticle().toLowerCase() + " " + object1.getName().toLowerCase());
			}
		}
		return aux;
	}

	public String quit(String objectName1, String objectName2){
		// objectName1 es lo que quiero sacar
		// objectName2 es donde quiero sacarlo
		String aux = "";
				
		// Primero comprobamos que los objetos son objetos reales
		AdventureObject<?, ?> object1 = (AdventureObject<?, ?>) objects.get(objectName1);
		AdventureObject<?, ?> object2 = (AdventureObject<?, ?>) objects.get(objectName2);
				
		if ((object1 == null) && (object2 == null)) {
			aux = SystemMessage.MESSAGE_DONT_HAVE_THAT;
		} else if ((object1 != null) && (object2 == null)) {
			aux = SystemMessage.MESSAGE_CANT_DO_THAT;
		} else {
			if (object1.getLocationId().equals(AdventureCodes.OBJECT_IN_OTHER_OBJECT)){
				if (object2.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
					if (object2.getIsContainer().equals(AdventureCodes.TRUE)){						
						if (object2.getContainerObject(object1.getName()) != null) { // El objeto esta dentro
							object1.setLocationId(AdventureCodes.OBJECT_TAKEN);
							object2.removeObjectFromContainer(object1.getName());
							if ((object2.getIsLightSource().equals(AdventureCodes.TRUE)) && // Estamos sacando un objeto de otro y este otro necesita al primero para funcionar como fuente de luz 
							    (object2.getLightLink().equals(objectName1))){
								object2.setStatus(AdventureCodes.OBJECT_STATE_TURNOFF);
							}
							aux = SystemMessage.MESSAGE_HAS_QUIT_OBJECT_FROM_OTHER_OBJECT;
							aux = aux.replace("*", object1.getArticle().toLowerCase() + " " + object1.getName().toLowerCase());
							aux = aux.replace("#", object2.getArticle().toLowerCase() + " " + object2.getName().toLowerCase());
						}
					} else { // El objeto destino no es contenedor
						aux = SystemMessage.MESSAGE_DONT_HAVE_QUIT_OBJECT_IN;
						aux = aux.replace("*", object2.getArticle().toLowerCase() + " " + object2.getName().toLowerCase()) + SystemMessage.MESSAGE_DOT;
					}
				} else { // No llevamos el segundo de los objetos
					aux = SystemMessage.MESSAGE_DONT_HAVE_THAT_OBJECT;
					aux = aux.replace("*", object2.getArticle().toLowerCase() + " " + object2.getName().toLowerCase());
				}
			} else { // No llevamos el primero de los objectos.
				if (object1.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)) {
					aux = SystemMessage.MESSAGE_CANT_DO_THAT;
				} else {
					aux = SystemMessage.MESSAGE_DONT_HAVE_THAT_OBJECT;
					aux = aux.replace("*", object1.getArticle().toLowerCase() + " " + object1.getName().toLowerCase());
				}
			}
		}
		return aux;
	}
	
	
	public boolean isBehaviourId(String behaviourId){
		boolean aux = false;
		if (actorsBehaviours !=null){
			if (actorsBehaviours.containsKey(behaviourId)){
				aux = true;
			}
		}
		return aux;
	}
	
	public Behaviour getBehaviour(String behaviourId){
		return actorsBehaviours.get(behaviourId);
	}
	
	public String turnOn(String objectName){
		String aux = "";
		// Primero comprobamos que el objeto es un objeto real
		
		AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
		
		if (object == null) {
			aux = SystemMessage.MESSAGE_DONT_HAVE_THAT;
		} else {
			if (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
				if (object.getIsLightSource().equals(AdventureCodes.TRUE)){
					if (((AdventureObject<?,?>) objects.get(object.getLightLink())).getLocationId().equals(AdventureCodes.OBJECT_IN_OTHER_OBJECT)){
						object.setStatus(AdventureCodes.OBJECT_STATE_TURNON);
						aux = SystemMessage.MESSAGE_HAS_TURNON;
						aux = aux.replace("*", object.getArticle().toLowerCase() + " " + object.getName().toLowerCase());
					} else {
						aux = SystemMessage.MESSAGE_TURNON_LINK_OBJECT;
						aux = aux.replace("*", object.getArticle().toLowerCase() + " " + object.getName().toLowerCase());
					}
				} else {
					aux = SystemMessage.MESSAGE_TURNON_IMPOSSIBLE;
					aux = aux.replace("*", object.getArticle().toLowerCase() + " " + object.getName().toLowerCase());
				}
			} else {
				aux = SystemMessage.MESSAGE_DONT_HAVE_THAT;
			}
		}
		return aux;		
	}
	
	public String turnOff(String objectName){
		String aux = "";
		// Primero comprobamos que el objeto es un objeto real
		
		AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
		if (object == null) {
			aux = SystemMessage.MESSAGE_DONT_HAVE_THAT;
		} else {
			if (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
				if (object.getIsLightSource().equals(AdventureCodes.TRUE)){
					object.setStatus(AdventureCodes.OBJECT_STATE_TURNOFF);
					aux = SystemMessage.MESSAGE_HAS_TURNOFF;
					aux = aux.replace("*", object.getArticle().toLowerCase() + " " + object.getName().toLowerCase());
				} else {
					aux = SystemMessage.MESSAGE_TURNOFF_IMPOSSIBLE;
					aux = aux.replace("*", object.getArticle().toLowerCase() + " " + object.getName().toLowerCase());
				}
			} else {
				aux = SystemMessage.MESSAGE_DONT_HAVE_THAT;
			}
		}
		return aux;		
	}
	
	public String isObjectWithLightTaken(){
		String aux = AdventureCodes.FALSE;
		Iterator<?> it = objects.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
			if ((object.getIsLightSource().equals(AdventureCodes.TRUE)) && 
			    (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN))) {
				aux = AdventureCodes.TRUE;
			}
		}		
		return aux;
	}
	
	public String isObjectWithLightTakenAndTurnOn(){
		String aux = AdventureCodes.FALSE;
		Iterator<?> it = objects.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
			if ((object.getIsLightSource().equals(AdventureCodes.TRUE)) && 
			    (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)) &&
			    (object.getStatus().equals(AdventureCodes.OBJECT_STATE_TURNON))) {
				aux = AdventureCodes.TRUE;
			}
		}		
		return aux;
	}
	
	public String isObjectWithLightHereAndTurnOn(){
		String aux = AdventureCodes.FALSE;
		Iterator<?> it = objects.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
			if ((object.getIsLightSource().equals(AdventureCodes.TRUE)) && 
			    ((object.getLocationId().equals(currentLocation.getId())) || 
			     (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN))) &&
			    (object.getStatus().equals(AdventureCodes.OBJECT_STATE_TURNON))) {
				aux = AdventureCodes.TRUE;
			}
		}		
		return aux;
	}
	

	
	public boolean isLocationVisited(String locationId){
		return visitedLocations.containsKey(locationId);
	}
	
	public boolean isObjectNear(String objectName){
		boolean aux = true;
		if (objects.containsKey(objectName)){				 
			if (((AdventureObject<?,?>) objects.get(objectName)).getDistance().equals(AdventureCodes.FAR)){
				aux = false;
			}
		}
		return aux;
	}
	
	public boolean isObjectDestroyed(String objectName){
		boolean aux = false;
		if (objects.containsKey(objectName)){				 
			if (((AdventureObject<?,?>) objects.get(objectName)).getLocationId().equals(AdventureCodes.OBJECT_DESTROY)){
				aux = true;
			}
		}
		return aux;
	}
	
	public void setActorVisible(String actorName, String locationId){
		if (actors.containsKey(actorName)){
			Actor actor = actors.get(actorName);
			assert actor != null; // To avoid nullpointer exception
			actor.setLocationId(locationId);
		}
	}
	
	public void setObjectVisible(String objectName, String locationId){
		if (objects.containsKey(objectName)){
			AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
			object.setLocationId(locationId);
			if (!locationId.equals(AdventureCodes.OBJECT_TAKEN)){
				GlobalInformation globalInformation = GlobalInformation.getInstance();
				if (object.getImagePosition()==1){
					globalInformation.setImage01_free(true);
				} else if (object.getImagePosition()==2){
					globalInformation.setImage02_free(true);
				} else if (object.getImagePosition()==3){
					globalInformation.setImage03_free(true);
				} else if (object.getImagePosition()==4){
					globalInformation.setImage04_free(true);
				} else if (object.getImagePosition()==5){
					globalInformation.setImage05_free(true);
				} else if (object.getImagePosition()==6){
					globalInformation.setImage06_free(true);
				} else if (object.getImagePosition()==7){
					globalInformation.setImage07_free(true);
				} else if (object.getImagePosition()==8){
					globalInformation.setImage08_free(true);
				}
				object.setImagePosition(0);
			}
		}
	}
	
	public boolean isActorHere(String actorName){
		boolean aux = false;
		
		if (actors.containsKey(actorName)){
			Actor actor = actors.get(actorName);
			if (actor.getLocationId().equals(currentLocation.getId())){
				aux = true;
			}
		}
		return aux;
	}

	public boolean isObjectHere(String objectName){
		boolean aux = false;
		if (objects.containsKey(objectName)){
			AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
			if (object.getLocationId().equals(currentLocation.getId())){
				aux = true;
			}
		}
		return aux;
	}
	
	
	
	public String activateFollowingBehaviour(String actorName){
		String aux = "";
		if (actors.containsKey(actorName) == true){
			if (isActorHere(actorName) == true){
				Behaviour behaviour = actorsBehaviours.get(actors.get(actorName).getFollowingBehaviour());
				behaviour.setActive(AdventureCodes.TRUE);
				//aux = SystemMessage.MESSAGE_FOLLOWING_BEHAVIOUR_ACTIVATE;
				aux = behaviour.getDescription();
				aux = aux.replace("*", actorName);
			}
		} else {
			aux = SystemMessage.MESSAGE_THIS_ACTOR_IS_NOT_HERE;
		}
		return aux;
	}

	public String desactivateFollowingBehaviour(String actorName){
		String aux = "";
		if (actors.containsKey(actorName) == true){
			if (isActorHere(actorName) == true){
				Behaviour behaviour = actorsBehaviours.get(actors.get(actorName).getFollowingBehaviour());
				behaviour.setActive(AdventureCodes.FALSE);
				//aux = SystemMessage.MESSAGE_FOLLOWING_BEHAVIOUR_ACTIVATE;
				aux = behaviour.getDescription();
				aux = aux.replace("*", actorName);
			}
		} else {
			aux = SystemMessage.MESSAGE_THIS_ACTOR_IS_NOT_HERE;
		}
		return aux;
	}

	public Actor getActor(String actorName){
		if (actors.containsKey(actorName)){
			return actors.get(actorName);
		} else {
			return null;
		}
	}
	
	public AdventureObject<?, ?> getObject(String objectName){
		if (objects.containsKey(objectName)){
			return (AdventureObject<?, ?>) objects.get(objectName);
		} else {
			return null;
		}
	}

	public HashMap<String, java.lang.Object> getObjects(){
		return this.objects;	
	}
	
	public HashMap<String, java.lang.Object> getObjectsTaken(){
		HashMap<String, java.lang.Object> objectsTaken = new HashMap<String, java.lang.Object>();
		Iterator<?> it = objects.entrySet().iterator();	    
		while (it.hasNext()){ // Recorremos la lista de Actores
			Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
			if (object.getLocationId().equals(AdventureCodes.OBJECT_TAKEN)){
				objectsTaken.put(object.getName(),object);				
			}
		}
		
		return objectsTaken;	
	}

	public HashMap<String, Actor> getLocationActors(){
		HashMap<String, Actor> locationActors = new HashMap<String, Actor>();
		Iterator<?> it = actors.entrySet().iterator();	    
		while (it.hasNext()){ // Recorremos la lista de Actores
			Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			Actor actor = (Actor)e.getValue();
			if (actor.getLocationId().equals(currentLocation.getId())){
				locationActors.put(actor.getName(),actor);				
			}
		}
		
		return locationActors;	
	}

	
	public HashMap<String, java.lang.Object> getLocationObjects(){
		HashMap<String, java.lang.Object> objectsTaken = new HashMap<String, java.lang.Object>();
		Iterator<?> it = objects.entrySet().iterator();	    
		while (it.hasNext()){ // Recorremos la lista de Actores
			Map.Entry<K, V> e = (Map.Entry<K, V>)it.next();
			AdventureObject<?, ?> object = (AdventureObject<?, ?>)e.getValue();
			if (object.getLocationId().equals(currentLocation.getId())){
				objectsTaken.put(object.getName(),object);				
			}
		}
		
		return objectsTaken;	
	}
	
	
	public String getObjectStatus(String objectName){
		if (objects.containsKey(objectName)){
			AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
			return object.getStatus().toUpperCase();
		} else {
			return "";
		}
	}

	
	public void decrementEnergy(String objectName){
		if (objects.containsKey(objectName)){
			AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
			if (object.getChargeLevel()>0) {
				object.setChargeLevel(object.getChargeLevel() - 1);
			} else {
				turnOff(objectName);
			}
		}
	}
	
	public void incrementEnergy(String objectName){
		if (objects.containsKey(objectName)){
			AdventureObject<?, ?> object = (AdventureObject<?, ?>) objects.get(objectName);
			object.setChargeLevel(1000);
		}
	}
}

