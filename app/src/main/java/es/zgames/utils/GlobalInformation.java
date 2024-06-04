package es.zgames.utils;

import es.zgames.textadventures.engine.*;


public class GlobalInformation {
	private static GlobalInformation INSTANCE = new GlobalInformation();
	
	private String language = Actor.AdventureCodes.OPTIONS_LANGUAGE_SPANISH;
	private String imageType = Actor.AdventureCodes.OPTIONS_IMAGE_NORMAL;
	private String music = Actor.AdventureCodes.TRUE;
	private String autoSave = Actor.AdventureCodes.TRUE;
	private String withGraphics = Actor.AdventureCodes.TRUE;
	private String advanceLocation = Actor.AdventureCodes.TRUE;
	private String animatedImage = "";
	private String animatedText1 = "";
	private String animatedText2 = "";
	private String animatedText3 = "";
	
	
	private boolean image01_free = true;
	private boolean image02_free = true;
	private boolean image03_free = true;
	private boolean image04_free = true;
	private boolean image05_free = true;
	private boolean image06_free = true;
	private boolean image07_free = true;
	private boolean image08_free = true;
	
	
	private int numberOfMovesUntilDead = 5;
	private boolean areYouShoot = false;
	private boolean areYouSeen = false;
	
	private boolean caughtSelected = false;
	private boolean leaveSelected = false;
	private boolean giveSelected = false;
	
	private AdventureObject objectToGive = null;
	private Actor actorToTalk = null;
	
	private boolean pRobotsFuera = false;
	private boolean pIs_plectrum_cut = false;
	private boolean pDoorsLocked = false;
	private boolean pEngineIsBroken = true;
	private boolean pHyperspaceIsBroken = true;
	private boolean pInTheSpace = false;		
	private int pChrisCounter = 0;
	private int pEngineHit = 0;
	private int pHits = 0;
	private int pMerodeadores = 0;
	private int pSoldiers = 0;
	private int pJuan = 0;

	private boolean pDoorWestLocked; 
	private boolean pDoorEastLocked;
	private boolean pTractorRayEnabled;
	private boolean pAutodestroyEnabled;
	private int pHidden = 0;
	private int pEye;
	private int pPaca;
	private int pTotalShipsDestroyed;
	private int pCountDownForDestruction;
		
	private int maxMoves = 5;
	
	public void setRobotsFuera(boolean robotsFuera){
		pRobotsFuera = robotsFuera;
	}
	
	public boolean getRobotsFuera(){
		return pRobotsFuera;
	}
	
	public void setIsPlectrumCut(boolean isPlectrumCut){		
		pIs_plectrum_cut = isPlectrumCut;
	}
	
	public boolean getIsPlectrumCut(){
		return pIs_plectrum_cut;
	}
	
	public void setDoorsLocked(boolean isDoorsLocked){
		pDoorsLocked = isDoorsLocked;
	}
	
	public boolean getDoorsLocked(){
		return pDoorsLocked;
	}
	
	public void setEngineIsBroken(boolean engineIsBroken){
		pEngineIsBroken = engineIsBroken;
	}
	
	public boolean getEngineIsBroken(){
		return pEngineIsBroken;
	}
	
	public void setHyperspaceIsBroken(boolean hyperspaceIsBroken){
		pHyperspaceIsBroken = hyperspaceIsBroken;
	}
	
	public boolean getHyperspaceIsBroken(){
		return pHyperspaceIsBroken;
	}
	
	public void setInTheSpace(boolean inTheSpace){
		pInTheSpace = inTheSpace;
	}
	
	public boolean getInTheSpace(){
		return pInTheSpace;
	}
	
	public void setChrisCounter(int chrisCounter){
		pChrisCounter = chrisCounter;
	}
	
	public int getChrisCounter(){
		return pChrisCounter;
	}
	
	public void setEngineHit(int engineHit){
		pEngineHit = engineHit;
	}
	
	public int getEngineHit(){
		return pEngineHit;
	}
	
	public void setHits(int hits){
		pHits = hits;
	}
	
	public int getHits(){
		return pHits;
	}
	
	public void setMerodeadores(int merodeadores){
		pMerodeadores = merodeadores;
	}
	
	public int getMerodeadores(){
		return pMerodeadores;
	}
	
	public void setJuan(int juan){
		pJuan = juan;
	}
	
	public int getJuan(){
		return pJuan;
	}
	
	public void setSoldiers(int soldiers){
		pSoldiers = soldiers;
	}
	
	public int getSoldiers(){
		return pSoldiers;
	}
	
	public boolean isImage01_free() {
		return image01_free;
	}

	
	public int getNumberOfMovesUntilDead() {
		return numberOfMovesUntilDead;
	}

	public void setNumberOfMovesUntilDead(int numberOfMovesUntilDead) {
		this.numberOfMovesUntilDead = numberOfMovesUntilDead;
	}

	
	public boolean areYouShoot() {
		return areYouShoot;
	}

	public void setAreYouShoot(boolean areYouShoot) {
		this.areYouShoot = areYouShoot;
	}

	public boolean areYouSeen() {
		return areYouSeen;
	}

	public void setAreYouSeen(boolean areYouSeen) {
		this.areYouSeen = areYouSeen;
	}

	public int getMaxMoves() {
		return maxMoves;
	}

	public void setMaxMoves(int maxMoves) {
		this.maxMoves = maxMoves;
	}	

	public void setImage01_free(boolean image01_free) {
		this.image01_free = image01_free;
	}

	public boolean isImage02_free() {
		return image02_free;
	}

	public void setImage02_free(boolean image02_free) {
		this.image02_free = image02_free;
	}

	public boolean isImage03_free() {
		return image03_free;
	}

	public void setImage03_free(boolean image03_free) {
		this.image03_free = image03_free;
	}

	public boolean isImage04_free() {
		return image04_free;
	}

	public void setImage04_free(boolean image04_free) {
		this.image04_free = image04_free;
	}

	public boolean isImage05_free() {
		return image05_free;
	}

	public void setImage05_free(boolean image05_free) {
		this.image05_free = image05_free;
	}

	public boolean isImage06_free() {
		return image06_free;
	}

	public void setImage06_free(boolean image06_free) {
		this.image06_free = image06_free;
	}

	public boolean isImage07_free() {
		return image07_free;
	}

	public void setImage07_free(boolean image07_free) {
		this.image07_free = image07_free;
	}

	public boolean isImage08_free() {
		return image08_free;
	}

	public void setImage08_free(boolean image08_free) {
		this.image08_free = image08_free;
	}
	
	private GlobalInformation(){
		
	}
	
	public static GlobalInformation getInstance(){
		return INSTANCE;
	}
	
	public String getLanguage(){
		return this.language;
	}
	
	public void setLanguage(String language){
		this.language = language;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getMusic() {
		return music;
	}

	public void setMusic(String music) {
		this.music = music;
	}

	public String getAutoSave() {
		return autoSave;
	}

	public void setAutoSave(String autoSave) {
		this.autoSave = autoSave;
	}

	public String getWithGraphics() {
		return withGraphics;
	}

	public void setWithGraphics(String withGraphics) {
		this.withGraphics = withGraphics;
	}

	public String getAnimatedImage() {
		return animatedImage;
	}

	public void setAnimatedImage(String animatedImage) {
		this.animatedImage = animatedImage;
	}

	public String getAnimatedText1() {
		return animatedText1;
	}

	public void setAnimatedText1(String animatedText1) {
		this.animatedText1 = animatedText1;
	}

	public String getAnimatedText2() {
		return animatedText2;
	}

	public void setAnimatedText2(String animatedText2) {
		this.animatedText2 = animatedText2;
	}

	public String getAnimatedText3() {
		return animatedText3;
	}

	public void setAnimatedText3(String animatedText3) {
		this.animatedText3 = animatedText3;
	}

	public String getAdvancedLocation(){
		return this.advanceLocation;
	}
	
	public void setAdvancedLocation(String advanceLocation){
		this.advanceLocation = advanceLocation;
	}

	public boolean getCaughtSelected(){
		return this.caughtSelected;
	}
	
	public boolean getLeaveSelected(){
		return this.leaveSelected;
	}
	
	public boolean getGiveSelected(){
		return this.giveSelected;
	}
	
	public void setCaughtSelected(boolean caughtSelected){
		this.caughtSelected = caughtSelected;		
	}
	
	public void setLeaveSelected(boolean leaveSelected){
		this.leaveSelected = leaveSelected;
	}
	
	public void setGiveSelected(boolean giveSelected){
		this.giveSelected = giveSelected;
	}
	
	public void setObjectToGive(AdventureObject object) {
		this.objectToGive = object;
	}
	
	public AdventureObject getObjectToGive(){
		return this.objectToGive;
	}
	
	
	public void setActorToTalk(Actor actor) {
		this.actorToTalk = actor;
	}
	
	public Actor getActorToTalk(){
		return this.actorToTalk;
	}


	public void setDoorWestLocked(boolean doorWestLocked){
		this.pDoorWestLocked = doorWestLocked;
	}
	
	public void setDoorEastLocked(boolean doorEastLocked){
		this.pDoorEastLocked = doorEastLocked;
	}
	
	public void setTractorRayEnabled(boolean tractorRayEnabled){
		this.pTractorRayEnabled = tractorRayEnabled;
	}
	
	public void setAutoDestroyEnabled(boolean autodestroyEnabled){
		this.pAutodestroyEnabled = autodestroyEnabled;
	}
	
	public void setHidden(int hidden){
		this.pHidden = hidden;
	}
	
	public void setEye(int eye){
		this.pEye = eye;
	}
	
	public void setPaca(int paca){
		this.pPaca = paca;
	}
	
	public void setTotalShipsDestroyed(int totalShipsDestroyed){
		this.pTotalShipsDestroyed = totalShipsDestroyed;
	}
	
	public void setCountDownForDestruction(int countDownForDestruction){
		this.pCountDownForDestruction = countDownForDestruction;
	}

	public boolean getDoorWestLocked(){
		return this.pDoorWestLocked;
	}
	public boolean getDoorEastLocked(){
		return this.pDoorEastLocked;
	}
	public boolean getTractorRayEnabled(){
		return this.pTractorRayEnabled;
	}
	public boolean getAutoDestroyEnabled(){
		return this.pAutodestroyEnabled;
	}
	public int getHidden(){
		return this.pHidden;
	}	
	public int getEye(){
		return this.pEye;
	}
	public int getPaca(){
		return this.pPaca;
	}
	public int getTotalShipsDestroyed(){
		return this.pTotalShipsDestroyed;
	}
	public int getCountDownForDestruction(){
		return this.pCountDownForDestruction;
	}



}


