package es.zgames.textadventures.engine;

import java.util.HashMap;
import java.util.Vector;

public class Actor {
	private String name;	
	private String locationText;
	private String description;
	private String killable;
	private String killable_object;
	private String killable_message;
	private int killable_numberOfHits;
	private HashMap<String, String> actions;
	private HashMap<String, String> automaticActions;
	private Vector<String> randomMessages;
	private String locationId;
	private Vector<String> giveBehaviourList;
	private Vector<String> askedBehaviourList;
	private Vector<String> nameSynonimous;
	private String pushBehaviour;
	private String suicideBehaviour;
	private String followingBehaviour;	
	private String image;
	
	public String getFollowingBehaviour() {
		return followingBehaviour;
	}

	public void setFollowingBehaviour(String followingBehaviour) {
		this.followingBehaviour = followingBehaviour;
	}

	public String getSuicideBehaviour() {
		return suicideBehaviour;
	}

	public void setSuicideBehaviour(String suicideBehaviour) {
		this.suicideBehaviour = suicideBehaviour;
	}

	public String getPushBehaviour() {
		return pushBehaviour;
	}

	public void setPushBehaviour(String pushBehaviour) {
		this.pushBehaviour = pushBehaviour;
	}

	public void addGiveBehaviourId(String giveBehaviourId){
		if (!giveBehaviourId.equals("")){
			if (this.giveBehaviourList == null){
				this.giveBehaviourList = new Vector<String>();
			}
			giveBehaviourList.add(giveBehaviourId);
		}
	}
	
	public void addAskedBehaviourId(String askedBehaviourId){
		if (!askedBehaviourId.equals("")){
			if (this.askedBehaviourList == null){
				this.askedBehaviourList = new Vector<String>();
			}
			askedBehaviourList.add(askedBehaviourId);
		}
	}

	public Vector<String> getGiveBehaviourList(){
		return this.giveBehaviourList;
	}
	
	public Vector<String> getAskedBehaviourList(){
		return this.askedBehaviourList;
	}
		
	public Vector<String> getNameSynonimousList(){
		return this.nameSynonimous;
	}
	
	
	public void addNameSynonimous(String name){
		if (this.nameSynonimous == null){
			this.nameSynonimous = new Vector<String>();
		}
		this.nameSynonimous.add(name);
	}
	
	
	public Actor(){
	//	actions = new HashMap<String, String>();
		//randomMessages = new Vector<String>();
		//giveBehaviourList = new Vector<String>();
		
		//randomMessages.removeAllElements();
		//giveBehaviourList.removeAllElements();
	}
	
	public String getLocationId() {
		return locationId;
	}
	
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLocationText() {
		return locationText;
	}
	
	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public HashMap<String, String> getActions() {
		return actions;
	}
	
	public void setActions(HashMap<String, String> actions) {
		this.actions = actions;
	}
	
	public HashMap<String, String> getAutomaticActions() {
		return automaticActions;
	}
	
	public void setAutomaticActions(HashMap<String, String> automaticActions) {
		this.automaticActions = automaticActions;
	}
	
	
	public Vector<String> getRandomMessages() {
		return randomMessages;
	}
	
	public void setRandomMessages(Vector<String> randomMessages) {
		if (randomMessages!=null){
			if (this.randomMessages == null){
				randomMessages = new Vector<String>();
			}
			this.randomMessages = randomMessages;
		}
	}
	
	public void addAction(String action, String message){
		if (actions == null){
			actions = new HashMap<String, String>();
		}
		actions.put(action, message);
	}
	
	public void addAutomaticAction(String action, String message){
		if (automaticActions == null){
			this.automaticActions = new HashMap<String, String>();
		}
		this.automaticActions.put(action, message);
	}
	
	
	public String getActionMessage(String action){
		String aux = this.actions.get(action); 
		if (aux == null) { // Es decir, no hay respuesta programada cogemos una aleatoria
			int index = (int)(Math.random() * randomMessages.size()) % randomMessages.size();
			aux = getRandomMessage(index);
		}
		return aux;
	}
	
	
	public void addRandomMessage(String message){
		if (this.randomMessages == null){
			this.randomMessages = new Vector<String>();
		}
		this.randomMessages.add(message);
	}
	
	public String getRandomMessage(int index){		
		return this.randomMessages.get(index);
	}

	public String getRandomMessage(){		
		int index = (int)(Math.random() * randomMessages.size()) % randomMessages.size();		
		return this.randomMessages.get(index);
	}
	
	// Set actor image 
	public void setImage(String image){ 
		this.image = image; 
	}
	
	// Returns location image 
	public String getImage(){ 
		return image; 
	}

	public String getKillable(){
		return this.killable;
	}
	
	public void setKillable(String killable){
		this.killable = killable;
	}
	
	public String getKillable_object(){
		return this.killable_object;
	}
	
	public void setKillable_object(String killable_object){
		this.killable_object = killable_object;
	}
	
	public String getKillable_message(){
		return this.killable_message;
	}
	
	public void setKillable_message(String killable_message){
		this.killable_message=killable_message;
	}
	
	public int getKillable_numberOfHits(){
		return this.killable_numberOfHits;
	}
	
	public void setKillable_numberOfHits(int killable_numberOfHits){
		this.killable_numberOfHits = killable_numberOfHits;
	}

    public static class AdventureCodes {
        public static final int MAX_OBJECTS = 7;
        public static final int MAX_IDENTIFICATOR = 4;
        public static final int MAX_TEXT_SIZE = 35;
        public static final int MIN_TEXT_SIZE = 15;
        public static final String LOCATION_ID = "LOCA";
        public static final String TYPE_ID = "TIPO";
        public static final String TITLE_ID = "TITU";
        public static final String IMAGEN_ID = "IMAG";
        public static final String RETRO_IMAGEN_ID = "RETR";
        public static final String HELP_ID = "AYUD";
        public static final String NAME_ID = "NAME";
        public static final String DESCRIPTION_ID = "DESC";
        public static final String EXITS_ID = "SALI";
        public static final String ARTICLE_ID = "ARTI";
        public static final String STATUS_ID = "STAT";
        public static final String MENSAGE_ID = "MENS";
        public static final String LEVEL_ID = "LEVE";
        public static final String TEXT_ID = "TEXT";
        public static final String FOOD_ID = "FOOD";
        public static final String TREASURE_ID = "TREA";
        public static final String AUTOMATIC_ACTION_ID = "AUTO";
        public static final String ACTION_ID = "ACTI";
        public static final String ACTIVE_ID = "ACTI";
        public static final String LONG_ID = "LONG";
        public static final String SYNONIMOUS_ID = "SYNO";
        public static final String BEHAVIOUR_ID = "IDEN";
        public static final String MOVE_PLAYER_ID = "MOVE";
        public static final String ASKED_ID = "ASKE";
        public static final String GIVE_ID = "GIVE";
        public static final String FLUID_ID = "FLUI";
        public static final String FLUID_TYPE_ID = "TFLU";
        public static final String DISTANCE_ID = "DIST";
        public static final String LIGHT_ID = "LIGH";
        public static final String CONTAINER_ID = "CONT";
        public static final String SHOW_ID = "SHOW";
        public static final String SIZE_ID = "SIZE";
        public static final String PUSH_ID = "PUSH";
        public static final String KILL_ID = "KILL";
        public static final String SUICIDE_ID = "SUIC";
        public static final String MESSAGE_ID = "MESS";
        public static final String FOLLOW_ID = "FOLL";
        public static final String RANDOMIZE_ID = "RAND";
        public static final String OBJECTS_NEEDED_ID = "NEED";
        public static final String OBJECTS_RETURNED_ID = "RETU";
        public static final String INIT_DEFINITION = "####";
        public static final String END_DEFINITION = "////";
        public static final String PLAYER = "PLAYER";

        public static final String LOCATION_TYPE_NORMAL = "NORMAL";
        public static final String LOCATION_TYPE_ANIMATED = "ANIMADA";
        public static final String LOCATION_TYPE_DEAD = "MUERTE";
        public static final int MOVE_ACTION_OK = 0;
        public static final int MOVE_ACTION_FAIL = 1;
        public static final int MOVE_ACTION_FAIL_BY_OBJECT = 2;
        public static final int MOVE_ACTION_FAIL_BY_MAGIC_WORD = 3;

        public static final String OBJECT_UNDEFINED = "250";
        public static final String OBJECT_HAS_ACTOR = "251";
        public static final String OBJECT_EATEN = "252";
        public static final String OBJECT_IN_OTHER_OBJECT = "253";
        public static final String OBJECT_TAKEN = "254";
        public static final String OBJECT_DESTROY = "255";

        public static final String OBJECT_STATE_EMPTY = "VACIA";
        public static final String OBJECT_STATE_FULL = "LLENA";
        public static final String OBJECT_STATE_TURNON = "ENCENDIDA";
        public static final String OBJECT_STATE_TURNOFF = "APAGADA";


        public static final String TRUE = "TRUE";
        public static final String FALSE = "FALSE";
        public static final String NEAR = "NEAR";
        public static final String FAR = "FAR";

        public static final String OPTIONS_LANGUAGE_SPANISH = "ES";
        public static final String OPTIONS_LANGUAGE_ENGLISH = "EN";
        public static final String OPTIONS_IMAGE_NORMAL = "NORMAL";
        public static final String OPTIONS_IMAGE_RETRO = "RETRO";
        public static final String OPTIONS_PASSWORD_01 = "TIMACUS";

        public static final String BEHAVIOR_TYPE_INSULT = "INSULT";
        public static final String BEHAVIOR_TYPE_GIVE = "GIVE";
        public static final String BEHAVIOR_TYPE_PUSH = "PUSH";
        public static final String BEHAVIOR_TYPE_SUICIDE = "SUICIDE";
        public static final String BEHAVIOR_TYPE_FOLLOW = "FOLLOW";

    }
}


