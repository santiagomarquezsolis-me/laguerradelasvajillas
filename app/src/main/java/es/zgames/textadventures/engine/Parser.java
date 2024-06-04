package es.zgames.textadventures.engine;

import java.util.HashMap;
import java.util.StringTokenizer;

import es.zgames.utils.GlobalInformation;

public class Parser {
	private HashMap<String, Integer> vocabulary;
	private String actionVerb;
	private String actionObject1;
	private String actionObject2;
	private String actionActor;
	
	
	public void initParser(){
		actionVerb = "";
		loadVocabulary();
	}
	
	private void loadVocabulary(){
		vocabulary = new HashMap<String, Integer>();
		
		// De 0 a 11 son palabras de movimiento
		vocabulary.put("S", 0);	
		vocabulary.put("SUR", 0);
		vocabulary.put("E", 1);	
		vocabulary.put("ESTE", 1);	
		vocabulary.put("O", 2);	
		vocabulary.put("OESTE", 2);	
		vocabulary.put("NE", 3);	
		vocabulary.put("NORESTE", 3);	
		vocabulary.put("NO", 4);	
		vocabulary.put("NOROESTE", 4);	
		vocabulary.put("SE", 5);	
		vocabulary.put("SURESTE", 5);	
		vocabulary.put("SO", 6);	
		vocabulary.put("SUROESTE", 6);	
		vocabulary.put("N",	7);	
		vocabulary.put("NORTE", 7);
		vocabulary.put("ENTRAR", 8);
		vocabulary.put("ENTRA", 8);
		vocabulary.put("ENTRO", 8);
		vocabulary.put("SALIR",	8);
		vocabulary.put("SALGO",	8);
		vocabulary.put("BAJAR",	9);
		vocabulary.put("BAJA",	9);
		vocabulary.put("ABAJO",	9);
		vocabulary.put("SUBIR",	10);
		vocabulary.put("SUBE", 10);
		vocabulary.put("ARRIBA",10);
		vocabulary.put("XYZZY",11);
		vocabulary.put("RECOLECTOR",11);
		
		// De 12 a 15 son palabras para acabar la aventura
		vocabulary.put("FIN", ParserCodes.START_END_ACTIONS_WORDS);		
		vocabulary.put("RETIRADA", ParserCodes.START_END_ACTIONS_WORDS);
		vocabulary.put("ABANDONAR", ParserCodes.START_END_ACTIONS_WORDS);
		vocabulary.put("RINDO", ParserCodes.START_END_ACTIONS_WORDS);
		vocabulary.put("RENDIRME", ParserCodes.START_END_ACTIONS_WORDS);
		vocabulary.put("ACABAR", ParserCodes.START_END_ACTIONS_WORDS);
		vocabulary.put("FINALIZAR", ParserCodes.START_END_ACTIONS_WORDS);
		vocabulary.put("CONCLUIR", ParserCodes.START_END_ACTIONS_WORDS);
		vocabulary.put("TERMINAR", ParserCodes.START_END_ACTIONS_WORDS);
		vocabulary.put("RENUNCIAR", ParserCodes.START_END_ACTIONS_WORDS);
					
		// De 16 a 20 son insultos
		vocabulary.put("IDIOTA", 16);		
		vocabulary.put("SUBNORMAL", 16);
		vocabulary.put("GILIPOLLAS", 16);
		vocabulary.put("MARICA", 16);
		vocabulary.put("MARICON", 16);
		vocabulary.put("MARICONA", 16);
		vocabulary.put("MARICONAZO", 16);
		vocabulary.put("PUTA", 16);
		vocabulary.put("CABRON", 16);
		vocabulary.put("CABRONAZO", 16);
		vocabulary.put("IMBECIL", 16);
		vocabulary.put("DESGRACIAD", 16);
		vocabulary.put("ZORRA", 16);
		vocabulary.put("CAGAR", 16);
		vocabulary.put("PEDO", 16);
		vocabulary.put("PIS", 16);
		vocabulary.put("CACA", 16);
		vocabulary.put("CAPULLO", 16);		
				
		// De 21 a 25 son para pedir descripciones
		vocabulary.put("DESCRIBIR", 21);
		vocabulary.put("EXAMINA", 21);
		vocabulary.put("EXAMINAR", 21);
		vocabulary.put("DESCRIBE", 21);
		vocabulary.put("DES", 21);
		vocabulary.put("EX", 21);
		
		// De 26 a 30 son para coger / dejar objetos 
		vocabulary.put("COGER", 26);
		vocabulary.put("COGE", 26);
		vocabulary.put("AGARRA", 26);
		vocabulary.put("AGARRAR", 26);
		vocabulary.put("PILLA", 28);
		vocabulary.put("PILLAR", 28);
		vocabulary.put("DEJA", 28);
		vocabulary.put("DEJAR", 28);
		vocabulary.put("SOLTAR", 28);
		vocabulary.put("SUELTA", 28);
		
		// De 31 a 35 son para inventario
		vocabulary.put("INVENTARIO", 31);
		vocabulary.put("INVEN", 31);
		vocabulary.put("I", 31);
			
		// De 36 a 40 son para salidas
		vocabulary.put("SALIDAS", 36);
		vocabulary.put("SALIDA", 36);
			
		// De 41 a 45 son para mirar
		vocabulary.put("MIRAR", 41);
		vocabulary.put("VER", 41);
		vocabulary.put("VE", 41);
		vocabulary.put("MIRA", 41);
		vocabulary.put("M", 41);
			
		// De 46 a 50 son para llenar
		vocabulary.put("LLENAR", 46);
		vocabulary.put("LLENA", 46);
		
		// De 51 a 55 son para hablar
		vocabulary.put("DECIR", 51);
		vocabulary.put("HABLAR", 51);
		
		// De 56 a 60 son para la ayuda
		vocabulary.put("ALLUDA", 56);
		vocabulary.put("ALLUDAR", 56);
		vocabulary.put("ALLUDAME", 56);
		vocabulary.put("SOCORRO", 56);
		vocabulary.put("SOCORREME", 56);
			
		// De 61 a 65 son para dar
		vocabulary.put("DAR", 61);
		vocabulary.put("DA", 61);
		vocabulary.put("ENTREGA", 61);
		vocabulary.put("ENTREGAR", 61);
				
		// De 66 a 70 son para beber
		vocabulary.put("BEBER", 66);
		vocabulary.put("BEBE", 66);
		vocabulary.put("BEBERSE", 66);
		vocabulary.put("TRAGAR", 66);
		vocabulary.put("TRAGA", 66);
		vocabulary.put("TRAGARSE", 66);
			
		// De 71 a 75 son para poner
		vocabulary.put("PONER", 71);
		vocabulary.put("PONE", 71);
		vocabulary.put("METER", 71);
		vocabulary.put("METE", 71);
		vocabulary.put("INTRODUCIR", 71);
		vocabulary.put("INTRODUCE", 71);
	
		// De 76 a 80 son para quitar
		vocabulary.put("QUITAR", 76);
		vocabulary.put("SACAR", 76);
		vocabulary.put("QUITA", 76);
		vocabulary.put("SACA", 76);
		
		// De 81 a 85 son para encender
		vocabulary.put("ENCENDER", 81);
		vocabulary.put("ENCIENDE", 81);
	
		// De 86 a 90 son para apagar
		vocabulary.put("APAGAR", 86);
		vocabulary.put("APAGA", 86);
		
		// De 86 a 90 son para apagar
		vocabulary.put("APAGAR", 86);
		vocabulary.put("APAGA", 86);
		
		// De 91 a 95 son para matar
		vocabulary.put("MATAR", 91);
		vocabulary.put("MATA", 91);
		vocabulary.put("ATACAR", 91);
		vocabulary.put("ATACA", 91);
		vocabulary.put("LUCHAR", 91);
		vocabulary.put("LUCHA", 91);		
		vocabulary.put("ASESINAR", 91);
		vocabulary.put("ASESINA", 91);
		vocabulary.put("PEGAR", 91);
		vocabulary.put("GOLPEA", 91);
		vocabulary.put("GOLPEAR", 91);
	
		// De 96 a 100 son para empujar
		vocabulary.put("EMPUJAR", 96);
		vocabulary.put("EMPUJA", 96);
	
		// De 101 a 105 son para escuchar / oir
		vocabulary.put("ESCUCHAR", 101);
		vocabulary.put("ESCUCHA", 101);
		vocabulary.put("OIR", 101);
		vocabulary.put("OYE", 101);
		vocabulary.put("OID", 101);
		
		// 106 Huevo de Pascua para AD
		vocabulary.put("AD", 106);
	
		vocabulary.put("MOVER", ParserCodes.START_SHAKE_ACTIONS_WORDS + 1);
		vocabulary.put("MUEVE", ParserCodes.START_SHAKE_ACTIONS_WORDS + 1);
		vocabulary.put("SACUDE", ParserCodes.START_SHAKE_ACTIONS_WORDS + 1);
		vocabulary.put("SACUDIR", ParserCodes.START_SHAKE_ACTIONS_WORDS + 1);
		vocabulary.put("AGITAR", ParserCodes.START_SHAKE_ACTIONS_WORDS + 1);
		vocabulary.put("AGITA", ParserCodes.START_SHAKE_ACTIONS_WORDS + 1);
			
		vocabulary.put("ABRIR", ParserCodes.START_OPEN_ACTIONS_WORDS + 1);
		vocabulary.put("ABRE", ParserCodes.START_OPEN_ACTIONS_WORDS + 1);
			
		vocabulary.put("USAR", ParserCodes.START_USE_ACTIONS_WORDS + 1);
		vocabulary.put("USA", ParserCodes.START_USE_ACTIONS_WORDS + 1);
			
		vocabulary.put("REGAR", ParserCodes.START_WATER_ACTIONS_WORDS + 1);
		vocabulary.put("RIEGA", ParserCodes.START_WATER_ACTIONS_WORDS + 1);
			
		vocabulary.put("VACIAR", ParserCodes.START_EMPTY_ACTIONS_WORDS + 1);
		vocabulary.put("VACIA", ParserCodes.START_EMPTY_ACTIONS_WORDS + 1);
		vocabulary.put("VERTER", ParserCodes.START_EMPTY_ACTIONS_WORDS + 1);
		vocabulary.put("VIERTE", ParserCodes.START_EMPTY_ACTIONS_WORDS + 1);
			
		vocabulary.put("ACEITAR", ParserCodes.START_OIL_ACTIONS_WORDS + 1);
		vocabulary.put("ACEITA", ParserCodes.START_OIL_ACTIONS_WORDS + 1);
		vocabulary.put("ENGRASAR", ParserCodes.START_OIL_ACTIONS_WORDS + 1);
		vocabulary.put("ENGRASA", ParserCodes.START_OIL_ACTIONS_WORDS + 1);
		vocabulary.put("LUBRICAR", ParserCodes.START_OIL_ACTIONS_WORDS + 1);
		vocabulary.put("LUBRICA", ParserCodes.START_OIL_ACTIONS_WORDS + 1);
		
		vocabulary.put("NADAR", ParserCodes.START_SWIN_ACTIONS_WORDS + 1);
			
		vocabulary.put("DESENCADENAR", ParserCodes.START_UNCHAIN_ACTIONS_WORDS + 1);
		vocabulary.put("DESENCADENA", ParserCodes.START_UNCHAIN_ACTIONS_WORDS + 1);
		vocabulary.put("DESATAR", ParserCodes.START_UNCHAIN_ACTIONS_WORDS + 1);
		vocabulary.put("DESATA", ParserCodes.START_UNCHAIN_ACTIONS_WORDS + 1);
			
		vocabulary.put("SOPLAR", ParserCodes.START_BLOW_ACTIONS_WORDS + 1);
		vocabulary.put("SOPLA", ParserCodes.START_BLOW_ACTIONS_WORDS + 1);
			
		vocabulary.put("COMER", ParserCodes.START_EAT_ACTIONS_WORDS + 1);
		vocabulary.put("COME", ParserCodes.START_EAT_ACTIONS_WORDS + 1);
		vocabulary.put("ZAMPATE", ParserCodes.START_EAT_ACTIONS_WORDS + 1);
		vocabulary.put("ZAMPAR", ParserCodes.START_EAT_ACTIONS_WORDS + 1);
		vocabulary.put("DEVORAR", ParserCodes.START_EAT_ACTIONS_WORDS + 1);
		vocabulary.put("DEVORA", ParserCodes.START_EAT_ACTIONS_WORDS + 1);
			
		vocabulary.put("DISPARAR", ParserCodes.START_SHOOT_ACTIONS_WORDS + 1);
		vocabulary.put("DISPARA", ParserCodes.START_SHOOT_ACTIONS_WORDS + 1);
			
		vocabulary.put("LANZAR", ParserCodes.START_THROW_ACTIONS_WORDS + 1);
		vocabulary.put("LANZA", ParserCodes.START_THROW_ACTIONS_WORDS + 1);
		vocabulary.put("TIRAR", ParserCodes.START_THROW_ACTIONS_WORDS + 1);
		vocabulary.put("TIRA", ParserCodes.START_THROW_ACTIONS_WORDS + 1);
			
		vocabulary.put("DESPEGAR", ParserCodes.START_TAKEOFF_ACTIONS_WORDS + 1);
		vocabulary.put("DESPEGA", ParserCodes.START_TAKEOFF_ACTIONS_WORDS + 1);
		
		vocabulary.put("SALTAR", ParserCodes.START_JUMP_ACTIONS_WORDS + 1);
		vocabulary.put("SALTA", ParserCodes.START_JUMP_ACTIONS_WORDS + 1);
		vocabulary.put("PASAR", ParserCodes.START_JUMP_ACTIONS_WORDS + 1);
		vocabulary.put("PASA", ParserCodes.START_JUMP_ACTIONS_WORDS + 1);
		
		vocabulary.put("ARREGLAR", ParserCodes.START_FIX_ACTIONS_WORDS + 1);
		vocabulary.put("ARREGLA", ParserCodes.START_FIX_ACTIONS_WORDS + 1);
		vocabulary.put("REPARAR", ParserCodes.START_FIX_ACTIONS_WORDS + 1);
		vocabulary.put("REPARA", ParserCodes.START_FIX_ACTIONS_WORDS + 1);
		
		vocabulary.put("ROMPER", ParserCodes.START_BREAK_ACTIONS_WORDS + 1);
		vocabulary.put("ROMPE", ParserCodes.START_BREAK_ACTIONS_WORDS + 1);
		vocabulary.put("DESTROZAR", ParserCodes.START_BREAK_ACTIONS_WORDS + 1);
		vocabulary.put("DESTROZA", ParserCodes.START_BREAK_ACTIONS_WORDS + 1);
		
		vocabulary.put("SAVE", ParserCodes.START_SAVE_ACTIONS_WORDS + 1);
		vocabulary.put("GUARDAR", ParserCodes.START_SAVE_ACTIONS_WORDS + 1);
		vocabulary.put("SALVAR", ParserCodes.START_SAVE_ACTIONS_WORDS + 1);
		vocabulary.put("GRABAR", ParserCodes.START_SAVE_ACTIONS_WORDS + 1);
			
		vocabulary.put("LOAD", ParserCodes.START_LOAD_ACTIONS_WORDS + 1);
		vocabulary.put("CARGAR", ParserCodes.START_LOAD_ACTIONS_WORDS + 1);
		vocabulary.put("CARGA", ParserCodes.START_LOAD_ACTIONS_WORDS + 1);
		
		//vocabulary.put("CARGAR", ParserCodes.START_CARGAR_ACTIONS_WORDS + 1);
		
		vocabulary.put("FOLLAR", ParserCodes.START_FUCK_ACTIONS_WORDS + 1);
		
		vocabulary.put("PANEL", ParserCodes.START_PANEL_ACTIONS_WORDS + 1);
		
		vocabulary.put("ACTIVAR", ParserCodes.START_ACTIVATE_ACTIONS_WORDS + 1);
		vocabulary.put("CONECTAR", ParserCodes.START_ACTIVATE_ACTIONS_WORDS + 1);
		vocabulary.put("ACTIVA", ParserCodes.START_ACTIVATE_ACTIONS_WORDS + 1);
		vocabulary.put("CONECTA", ParserCodes.START_ACTIVATE_ACTIONS_WORDS + 1);
		
		vocabulary.put("MEAR", ParserCodes.START_PISS_ACTIONS_WORDS + 1);
		vocabulary.put("MEA", ParserCodes.START_PISS_ACTIONS_WORDS + 1);
		vocabulary.put("CAGAR", ParserCodes.START_PISS_ACTIONS_WORDS + 1);
		vocabulary.put("CAGA", ParserCodes.START_PISS_ACTIONS_WORDS + 1);
		vocabulary.put("ORINAR", ParserCodes.START_PISS_ACTIONS_WORDS + 1);
		vocabulary.put("ORINA", ParserCodes.START_PISS_ACTIONS_WORDS + 1);
		vocabulary.put("MICCIONAR", ParserCodes.START_PISS_ACTIONS_WORDS + 1);
		vocabulary.put("MICCIONA", ParserCodes.START_PISS_ACTIONS_WORDS + 1);
		
		vocabulary.put("ESCONDERSE", ParserCodes.START_HIDE_ACTIONS_WORDS + 1);
		vocabulary.put("ESCONDERTE", ParserCodes.START_HIDE_ACTIONS_WORDS + 1);
		vocabulary.put("ESCONDETE", ParserCodes.START_HIDE_ACTIONS_WORDS + 1);
		vocabulary.put("ESCONDE", ParserCodes.START_HIDE_ACTIONS_WORDS + 1);		
		vocabulary.put("ESCONDER", ParserCodes.START_HIDE_ACTIONS_WORDS + 1);
		
		// Del 1000 en adelante son objetos
		vocabulary.put("BOTELLA", ParserCodes.START_OBJECTS_WORDS + 1);
				
		// Del 2000 en adelante son nombres
		vocabulary.put("BESUGINA", ParserCodes.START_NAMES_WORDS + 1);
		vocabulary.put("CHRIS", ParserCodes.START_NAMES_WORDS + 1);
		vocabulary.put("JUANSOLO", ParserCodes.START_NAMES_WORDS + 2);
		vocabulary.put("OBI", ParserCodes.START_NAMES_WORDS + 3);
	
	}
	
	public int getTotalLogicSentence(String sentence){
		String aux = cleanSentence(sentence);
		int value = 1;
		if (aux.length() > 0){
			if (aux.charAt(0) == ','){
				value = 0;
			} else {
				value = value + counter(aux, ',');
			}
		} 
		
		return value;
	}
	
	public String getLogicSentence(String sentence, int index){
		String aux = cleanSentence(sentence);
		String logicSentences[] = aux.split(",");
		
		String newSentence = "";		
		if (index <= (logicSentences.length - 1)){
			newSentence  = logicSentences[index];
		}
		
		return newSentence ;		
	}
	
	public int parse (String sentence) {
		// END_ACTION = fin, acabar, terminar, quitar, finalizar
		
		
		if (sentence.equals("")){
			return ParserCodes.NULL_ACTION;
		} if (sentence.toUpperCase().equals("FEE FIE FOE FOO")) {
			return ParserCodes.FEEFIEFOEFOO_ACTION;
		} else {
			// 1. Limpiamos las variables de consulta
			actionActor = "";
			actionVerb = "";
			actionObject1 = "";
			actionObject2 = "";
			// 2. Transforma la sentencia de entrada y la limpiamos
			//String aux = cleanSentence (sentence);
			String aux = sentence;
			System.out.println("Sentence: " + aux);	
			// 3. Devuelve el tipo de acci�n encontrada
			if (isMoveAction(aux) == true) {
				return ParserCodes.MOVE_ACTION;
			} else if (isEndAction(aux) == true) {
				return ParserCodes.END_ACTION;
			} else if (isBadWordsAction(aux) == true) {
				return ParserCodes.BADWORDS_ACTION;
			} else if (isDescribeAction(aux) == true) {
				return ParserCodes.DESCRIBE_ACTION;
			} else if (isInventaryAction(aux) == true) {
				return ParserCodes.INVENTARY_ACTION;
			} else if (isExitsAction(aux) == true) {
				return ParserCodes.EXITS_ACTION;	
			} else if (isLookAction(aux) == true) {
				return ParserCodes.LOOK_ACTION;		
			} else if (isCatchAction(aux) == true) {
				return ParserCodes.CATCH_ACTION;
			} else if (isLeaveAction(aux) == true) {
				return ParserCodes.LEAVE_ACTION;
			} else if (isFillAction(aux) == true) {
				return ParserCodes.FILL_ACTION;
			} else if (isSayAction(aux) == true) {
				return ParserCodes.SAY_ACTION;
			} else if (isGiveAction(aux) == true) {
				return ParserCodes.GIVE_ACTION;		
			} else if (isDrinkAction(aux) == true) {
				return ParserCodes.DRINK_ACTION;
			} else if (isPutAction(aux) == true) {
				return ParserCodes.PUT_ACTION;
			} else if (isQuitAction(aux) == true) {
				return ParserCodes.QUIT_ACTION;
			} else if (isThrowAction(aux) == true) {
				return ParserCodes.THROW_ACTION;			
			} else if (isTurnOnAction(aux) == true) {
				return ParserCodes.TURNON_ACTION;
			} else if (isTurnOffAction(aux) == true) {
				return ParserCodes.TURNOFF_ACTION;
			} else if (isKillAction(aux) == true) {
				return ParserCodes.KILL_ACTION;
			} else if (isOilAction(aux) == true) {
				return ParserCodes.OIL_ACTION;
			} else if (isUseAction(aux) == true) {
				return ParserCodes.USE_ACTION;
			} else if (isShakeAction(aux) == true) {
				return ParserCodes.SHAKE_ACTION;
			} else if (isPushAction(aux) == true) {
				return ParserCodes.PUSH_ACTION;			
			} else if (isOpenAction(aux) == true) {
				return ParserCodes.OPEN_ACTION;
			} else if (isSaveAction(aux) == true) {
				return ParserCodes.SAVE_ACTION;		
			} else if (isLoadAction(aux) == true) {
				return ParserCodes.LOAD_ACTION;			
			} else if (isEmptyAction(aux) == true) {
				return ParserCodes.EMPTY_ACTION;		
			} else if (isWaterAction(aux) == true) {
				return ParserCodes.WATER_ACTION;
			} else if (isSwinAction(aux) == true) {
				return ParserCodes.SWIN_ACTION;	
			} else if (isEatAction(aux) == true) {
				return ParserCodes.EAT_ACTION;			
			} else if (isListenAction(aux) == true) {
				return ParserCodes.LISTEN_ACTION;	
			} else if (isBlowAction(aux) == true) {
				return ParserCodes.BLOW_ACTION;	
			} else if (isAdAction(aux) == true) {
				return ParserCodes.AD_ACTION;			
			} else if (isHelpAction(aux) == true) {
				return ParserCodes.HELP_ACTION;
			} else if (isUnChainAction(aux) == true) {
				return ParserCodes.UNCHAIN_ACTION;
			} else if (isObjectWord(aux) == true) {
				return ParserCodes.OBJECT_WORD;
			} else if (isFuckAction (aux) == true) {
				return ParserCodes.FUCK_ACTION;				
			} else if (isShootAction (aux) == true) {
				return ParserCodes.SHOOT_ACTION;			
			} else if (isTakeOffAction (aux) == true) {
				return ParserCodes.TAKEOFF_ACTION;		
			} else if (isJumpAction (aux) == true) {
				return ParserCodes.JUMP_ACTION;	
			} else if (isFixAction (aux) == true) {
				return ParserCodes.FIX_ACTION;	
			} else if (isPanelAction (aux) == true) {
				return ParserCodes.PANEL_ACTION;	
			} else if (isActivateAction (aux) == true) {
				return ParserCodes.ACTIVATE_ACTION;
			} else if (isPissAction (aux) == true) {
				return ParserCodes.PISS_ACTION;
			} else if (isHideAction (aux) == true) {
				return ParserCodes.HIDE_ACTION;			
			} else if (isBreakAction (aux) == true) {
				return ParserCodes.BREAK_ACTION;
			} else {
				return ParserCodes.UNKNOW_ACTION;
			}
		}
	}

	private int counter(String word, char character){
		int total = 0;
		for (int index=0; index<word.length(); index++){
			if (word.charAt(index) == character){
				total++;
			}
		}
		return total;
	}
	
	private String cleanSentence(String sentence){
		// 1. Transforma la sentencia de entrada:
		//		- todo a mayusculas
		//		- elimina caracteres en blanco por delante y por detras de la sentencia
		// 		- elimina EL, LA, LO, LOS, LAS, UN, UNO, UNA, UNOS, UNAS, HACIA, IR, MOVER, A
		//		- cada palabra solo coje los 5 primeros caracteres
		
		StringTokenizer stringTokenizer = new StringTokenizer(sentence);
		String newSentence = "";
		GlobalInformation globalInformation = GlobalInformation.getInstance();
		
		if (globalInformation.getLanguage().equals(AdventureCodes.OPTIONS_LANGUAGE_SPANISH)){
			while (stringTokenizer.hasMoreElements()){
				String token = stringTokenizer.nextToken();
				token = token.trim().toUpperCase();			
				
				if ((!token.equals("EL")) &&
					(!token.equals("LA")) &&
					(!token.equals("AL")) &&
					(!token.equals("EL")) &&
					(!token.equals("LAS")) &&
					(!token.equals("DENTRO")) &&
					(!token.equals("FUERA")) &&
					(!token.equals("LO")) &&
					(!token.equals("LOS")) &&
					(!token.equals("A")) &&
					(!token.equals("EN")) &&
					(!token.equals("UN")) &&				
					(!token.equals("UNO")) &&
					(!token.equals("UNOS")) &&
					(!token.equals("UNA")) &&
					(!token.equals("UNAS")) &&
					(!token.equals("ME")) &&
					(!token.equals("DE")) &&
					(!token.equals("TE")) &&
					(!token.equals("HACIA")) &&
					(!token.equals("DON")) &&
					(!token.equals("DO�A")) &&
					(!token.equals("VAMOS")) &&
					(!token.equals("MUY")) &&
					(!token.equals("MUEVETE")) &&
					(!token.equals("IR")) &&
					(!token.equals("VE")) &&
					(!token.equals("SERAS")) &&
					(!token.equals("DESPL"))) {
					newSentence = newSentence + " " + token;
					if (token.equals("Y")){
						token = token.replaceAll("Y", ",");
					}
				}			
			}
			newSentence = newSentence.trim().toUpperCase();
			newSentence = newSentence.replaceAll("Y LUEGO", ",");
			newSentence = newSentence.replaceAll("Y DESPUES", ",");
			newSentence = newSentence.replaceAll("LUEGO", ",");
			newSentence = newSentence.replaceAll("DESPUES", ",");
			newSentence = newSentence.replaceAll("AYUDA","ALLUDA");
			newSentence = newSentence.replaceAll("AYUDAME","ALLUDAME");
			newSentence = newSentence.replaceAll("AYUDAR","ALLUDAR");
			newSentence = newSentence.replaceAll("SAPOIDE","SAPO");
			newSentence = newSentence.replaceAll("JUAN","JUANSOLO");		
			newSentence = newSentence.replaceAll("ANDROIDES","C2P2");
			newSentence = newSentence.replaceAll("ANDROIDE","C2P2");
			newSentence = newSentence.replaceAll("ROBOT","C2P2");
			newSentence = newSentence.replaceAll("ROBOTS","C2P2");
			newSentence = newSentence.replaceAll("CACHIPORRALUZ","CACHIPORRA");
			newSentence = newSentence.replaceAll("PRINCESA","PACA");
			newSentence = newSentence.replaceAll("GUANTEBOT","GUANTE");
			newSentence = newSentence.replaceAll("ESCONDEROS","ESCONDERTE");
			newSentence = newSentence.replaceAll("JUANSOLO","JUAN");
			
			newSentence = newSentence.replace(';', ',');
			newSentence = newSentence.replace(':', ',');
			newSentence = newSentence.replace('.', ',');
			return newSentence;
		} else {
			while (stringTokenizer.hasMoreElements()){
				String token = stringTokenizer.nextToken();
				token = token.trim().toUpperCase();			
				
				if ((!token.equals("THE")) &&
					(!token.equals("INSIDE")) &&
					(!token.equals("A")) &&
					(!token.equals("ME")) &&
					(!token.equals("DON")) &&
					(!token.equals("GO")) &&
					(!token.equals("VERY")) &&
					(!token.equals("BE"))) {
					if (token.equals("AND")){
						token = token.replaceAll("AND", ",");
					}
					newSentence = newSentence + " " + token;
				}			
			}
			newSentence = newSentence.trim().toUpperCase();
			newSentence = newSentence.replaceAll("AND THEN", ",");
			newSentence = newSentence.replaceAll("AND LATER", ",");
			newSentence = newSentence.replaceAll("LATER", ",");
			newSentence = newSentence.replaceAll("BEFORE", ",");
			
			if ((!newSentence.equals("HELP")) && 
				(!newSentence.equals("HELP ME"))
			   ) {				
				newSentence = newSentence.replace(';', ',');
				newSentence = newSentence.replace(':', ',');
				newSentence = newSentence.replace('.', ',');
				}
			return newSentence;
			
		}
	}
	
	private boolean isMoveAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();

			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_MOVE_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_MOVE_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){			
			return aux;
		}
	}
	
	private boolean isEndAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();

			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_END_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_END_ACTIONS_WORDS)) {
				actionVerb = sentence;
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isBadWordsAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_BADWORDS_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_BADWORDS_ACTIONS_WORDS)) {
				actionVerb = sentence;
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isDescribeAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_DESCRIBE_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_DESCRIBE_ACTIONS_WORDS)) {
				actionVerb = sentence;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isCatchAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_CATCH_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_CATCH_ACTIONS_WORDS)) {
				actionVerb = sentence;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}				
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isLeaveAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_LEAVE_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_LEAVE_ACTIONS_WORDS)) {
				actionVerb = sentence;
				actionObject1 = stringTokenizer.nextToken();				
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isInventaryAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_INVENTARY_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_INVENTARY_ACTIONS_WORDS)) {
				actionVerb = sentence;				
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isAdAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_AD_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_AD_ACTIONS_WORDS)) {
				actionVerb = token;				
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}


	private boolean isExitsAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_EXITS_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_EXITS_ACTIONS_WORDS)) {
				actionVerb = sentence;				
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}

	private boolean isTakeOffAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_TAKEOFF_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_TAKEOFF_ACTIONS_WORDS)) {
				actionVerb = sentence;				
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isJumpAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_JUMP_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_JUMP_ACTIONS_WORDS)) {
				actionVerb = sentence;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}				
			
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isShootAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_SHOOT_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_SHOOT_ACTIONS_WORDS)) {
				actionVerb = sentence;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}				
			
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	private boolean isFixAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_FIX_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_FIX_ACTIONS_WORDS)) {
				actionVerb = sentence;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}				
			
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isPanelAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_PANEL_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_PANEL_ACTIONS_WORDS)) {
				actionVerb = sentence;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}				
			
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isActivateAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_ACTIVATE_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_ACTIVATE_ACTIONS_WORDS)) {
				actionVerb = sentence;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}				
			
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isPissAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_PISS_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_PISS_ACTIONS_WORDS)) {
				actionVerb = sentence;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}				
			
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isHideAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_HIDE_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_HIDE_ACTIONS_WORDS)) {
				actionVerb = sentence;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}				
			
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isBreakAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_BREAK_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_BREAK_ACTIONS_WORDS)) {
				actionVerb = sentence;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}				
			
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isHelpAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_HELP_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_HELP_ACTIONS_WORDS)) {
				actionVerb = token;				
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isLookAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_LOOK_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_LOOK_ACTIONS_WORDS)) {
				actionVerb = token;				
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isGiveAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_GIVE_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_GIVE_ACTIONS_WORDS)) {
				actionVerb = token;		
				actionActor = stringTokenizer.nextToken();
				actionObject1 = stringTokenizer.nextToken();
				
				// Checking para verificar dar xxx a yyy o dar a yyyy xxxx
				int actionActorVocabularyPosition = -1;
				int actionObject1VocabularyPosition = -1;
				
				try {
					actionActorVocabularyPosition = vocabulary.get(actionActor);
				} catch (Exception e) {
					// lo ignoramos para evitar el null pointer si la palabra no est� en el vocabulario.
				}
				try {
					actionObject1VocabularyPosition = vocabulary.get(actionObject1);
				} catch (Exception e){
					// lo ignoramos para evitar el null pointer si la palabra no est� en el vocabulario.
				}
				
				if ((actionActorVocabularyPosition != -1) && (actionObject1VocabularyPosition != -1)) {
					// Solo evaluamos en el caso de que ambas palabras esten en el vocabulario sino no hacemos nada
					// por tanto las variables de posicion tienen que tener valor diferente de -1
					if (
						((actionActorVocabularyPosition >= ParserCodes.START_OBJECTS_WORDS) &&
					     (actionActorVocabularyPosition <= ParserCodes.END_OBJECTS_WORDS)) &&
					    ((actionObject1VocabularyPosition >= ParserCodes.START_NAMES_WORDS) &&
						 (actionObject1VocabularyPosition <= ParserCodes.START_NAMES_WORDS))
					   ) {
						String temp = actionActor;
						actionActor = actionObject1;
						actionObject1 = temp;
					}
				}
				
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isObjectWord(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_OBJECTS_WORDS) && 
				(value <= ParserCodes.END_OBJECTS_WORDS)) {
				this.actionObject1 = token;				
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isFillAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_FILL_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_FILL_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isDrinkAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_DRINK_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_DRINK_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isEatAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_EAT_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_EAT_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isKillAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_KILL_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_KILL_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionActor = stringTokenizer.nextToken();
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isPushAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_PUSH_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_PUSH_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionActor = stringTokenizer.nextToken();
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isPutAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_PUT_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_PUT_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()==1){
					actionObject1 = stringTokenizer.nextToken();
				} else if (stringTokenizer.countTokens()==2){
					actionObject1 = stringTokenizer.nextToken();
					actionObject2 = stringTokenizer.nextToken();
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isShakeAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_SHAKE_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_SHAKE_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();					
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isQuitAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_QUIT_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_QUIT_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()==1){
					actionObject1 = stringTokenizer.nextToken();
				} else if (stringTokenizer.countTokens()==2){
					actionObject1 = stringTokenizer.nextToken();
					actionObject2 = stringTokenizer.nextToken();					
				} else {
					actionObject1 = "";
					actionObject2 = "";
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isTurnOnAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_TURNON_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_TURNON_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();					
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isTurnOffAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_TURNOFF_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_TURNOFF_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();				
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isOpenAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_OPEN_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_OPEN_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();					
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isThrowAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_THROW_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_THROW_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();					
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isUseAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_USE_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_USE_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();					
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isListenAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_LISTEN_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_LISTEN_ACTIONS_WORDS)) {
				actionVerb = token;
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isSaveAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_SAVE_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_SAVE_ACTIONS_WORDS)) {
				actionVerb = token;
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isLoadAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_LOAD_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_LOAD_ACTIONS_WORDS)) {
				actionVerb = token;
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	private boolean isWaterAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_WATER_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_WATER_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();					
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isBlowAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_BLOW_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_BLOW_ACTIONS_WORDS)) {
				actionVerb = token;
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isEmptyAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_EMPTY_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_EMPTY_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();					
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isUnChainAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_UNCHAIN_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_UNCHAIN_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionActor = stringTokenizer.nextToken();					
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isFuckAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_FUCK_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_FUCK_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionActor = stringTokenizer.nextToken();					
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isCargarAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_CARGAR_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_CARGAR_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();					
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isOilAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_OIL_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_OIL_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();					
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	

	private boolean isSwinAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_SWIN_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_SWIN_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					actionObject1 = stringTokenizer.nextToken();					
				}
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	private boolean isSayAction(String sentence){
		boolean aux = false;
		try {
			StringTokenizer stringTokenizer = new StringTokenizer(sentence);
			String token = stringTokenizer.nextToken();
			String temp = "";
			int value = vocabulary.get(token);
			if ((value >= ParserCodes.START_SAY_ACTIONS_WORDS) && 
				(value <= ParserCodes.END_SAY_ACTIONS_WORDS)) {
				actionVerb = token;
				if (stringTokenizer.countTokens()>0){
					temp = sentence.replace(actionVerb, "").trim(); // Con esto eliminamos el verbo de la frase
					if (temp.charAt(0) ==  '\"'){
						actionActor = "";
						actionObject1 = temp;
					} else {
						actionActor = stringTokenizer.nextToken();
						actionObject1 = temp.replace(actionActor, "").trim(); // Con esto eliminamos al actor de la frase
						actionObject1 = actionObject1.replace("\"", "");
						actionObject1 = actionObject1.replace("Y", "");
						actionObject1 = cleanSentence(actionObject1);
					}
				}
				
				aux = true;
			}
			return aux;
		} catch (Exception e){
			return aux;
		}
	}
	
	public String getActionVerb(){
		return this.actionVerb;
	}
	
	public String getActionObject1(){
		return this.actionObject1;
	}
	
	public String getActionObject2(){
		return this.actionObject2;
	}
	
	public String getActionActor(){
		return this.actionActor;
	}
	
}

