package edu.up.cs301.risk;

import edu.up.cs301.game.Game;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * @authors Shamus Murray, Garrett Becker, Logan Mealy, Lucas Burns, John Will Bryan 
 * 
 * Risk game state that handles updating information and sending it to players. 
 * 
 * @version March 2015
 */

public class RiskState extends GameState {

	// to satisfy the Serializable interface
	private static final long serialVersionUID = -5109179064333136954L;

	private int playerTurn; // keeps track of who's turn

	protected Game game; // the game object

	protected String[] allPlayerNames; // list of all player names, in ID order
	private boolean gameOver = false; // whether the game is over

	// stores number of troops in each of the 16 countries
	public int[] playerOneTroops;

	// stores number of troops in each of the 16 countries for player 2
	public int[] playerTwoTroops;

	// prints victor name
	private String victor;
	
	// have the 3 troops been placed
	private boolean haveTroopsBeenPlaced;

	// variables to store the value of each of the die
	private int attack1die;
	private int attack2die;
	private int attack3die;
	private int defend1die;
	private int defend2die;

	private static int[] countryList; // array of countries on the board
	private final static int COUNTRY_LENGTH = 16; // length of array

	// countryConstants
	public static int RUSSIA = 0;
	public static int ICELAND = 1;
	public static int ITALY = 2;
	public static int SWEDEN = 3;
	public static int ATLANTIS = 4;
	public static int HOGWARTS = 5;
	public static int NARNIA = 6;
	public static int GERMANY = 7;
	public static int MORDOR = 8;
	public static int GONDOR = 9;
	public static int SHIRE = 10;
	public static int ROHAN = 11;
	public static int BULGARIA = 12;
	public static int ISRAEL = 13;
	public static int SWITZERLAND = 14;
	public static int UKRAINE = 15;

	// player ID numbers
	public final static int PLAYER_ONE = 100;
	public final static int PLAYER_TWO = 200;

	/**
	 * Constructor
	 * 
	 * places one troop per country.
	 * 
	 * player one will have control over the top 2 rows player two will have
	 * control over the bottom 2 rows
	 * 
	 */
	public RiskState() {
		playerOneTroops = new int[COUNTRY_LENGTH];
		int i;

		// loop through and place troop in every country
		// in top two rows of map
		for (i = 0; i < COUNTRY_LENGTH - 1; i++) {
			if (i <= 7) {
				playerOneTroops[i] = 2;
			} else {
				playerOneTroops[i] = 0;
			}
		}
		playerTwoTroops = new int[COUNTRY_LENGTH];
		int y;

		// loop through and place troop in every country
		// in bottom 2 rows of map
		for (y = 0; y < COUNTRY_LENGTH - 1; y++) {
			if (y > 7) {
				playerTwoTroops[i] = 2;
			}
			if (y <= 7) {
				playerTwoTroops[i] = 0;
			}
		}
	}

	/**
	 * RiskState()
	 * 
	 * creates a copy of the game state to prevent cheating
	 *
	 */
	public RiskState(RiskState orig) {
		this.playerTwoTroops = new int[orig.playerTwoTroops.length];
		for (int y = 0; y < orig.playerOneTroops.length; ++y) {
			this.playerTwoTroops[y] = orig.playerTwoTroops[y];
		}
	}

	/**
	 * attackWon()
	 * 
	 * When attacking country wins subtract one troop from the losing country
	 * 
	 * @param playerIDAttacking
	 *            : Id of the player attacking
	 * @param countryIDDefendFrom
	 *
	 * 
	 */
	public void attackWon(int playerIDAttacking, int countryIDDefendFrom) {

		if (playerIDAttacking == PLAYER_ONE) {
			// if player1 wins subtract one from player 2 in their country
			playerTwoTroops[countryIDDefendFrom] = playerTwoTroops[countryIDDefendFrom] - 1;
		} else {
			// else player 2 wins and subtract one from player 1 in their
			// country
			playerOneTroops[countryIDDefendFrom] = playerOneTroops[countryIDDefendFrom] - 1;
		}

	}

	/**
	 * attackLost()
	 * 
	 * When attacking country loses, subtract one troop from the attacking
	 * country
	 * 
	 * @param playerIDAttacking
	 *            : ID of the player attacking
	 * @param countryIDAttackFrom
	 *            : ID of the country the player is playing from
	 * 
	 * 
	 */
	public void attackLost(int playerIDAttacking, int countryIDAttackFrom) {
		if (playerIDAttacking == PLAYER_TWO) {

			// if Player2 loses subtract one troop from player2's country
			playerTwoTroops[countryIDAttackFrom] = playerTwoTroops[countryIDAttackFrom] - 1;
		} else {

			// else Player1 loses subtract one troop from player1's country
			playerOneTroops[countryIDAttackFrom] = playerOneTroops[countryIDAttackFrom] - 1;
		}

	}

	/**
	 * getPlayerInControl()
	 * 
	 * returns the player in control of a given country
	 * 
	 * @param countryCode
	 *            : code of the country being tested
	 */
	public int getPlayerInControl(int countryCode) {

		if (playerOneTroops[countryCode] >= 1) {
			// player one has more than 1 troop
			// so player one is in control
			return PLAYER_ONE;
		} else {

			// player two has more than 1 troop
			// so player two is in control
			return PLAYER_TWO;

		}
	}

	/**
	 * setAttackDieOne()
	 * 
	 * sets integer value for the first attack die
	 * 
	 */
	public void setAttackDieOne() {

		// randomly assigns an integer between 1 and 6
		this.attack1die = (int) Math.ceil(Math.random() * 6);
	}

	/**
	 * setAttackDieTwo()
	 * 
	 * sets integer value for the second attack die
	 * 
	 */
	public void setAttackDieTwo() {

		// randomly assigns an integer between 1 and 6
		this.attack2die = (int) Math.ceil(Math.random() * 6);
	}

	/**
	 * setAttackDieThree()
	 * 
	 * sets integer value for the third attack die
	 * 
	 */
	public void setAttackDieThree() {

		// randomly assigns an integer between 1 and 6
		this.attack3die = (int) Math.ceil(Math.random() * 6);
	}

	/**
	 * setDefendDieOne()
	 * 
	 * sets integer value for the first defend die
	 * 
	 */
	public void setDefendDieOne() {

		// randomly assigns an integer between 1 and 6
		this.defend1die = (int) Math.ceil(Math.random() * 6);
	}

	/**
	 * setDefendDieTwo()
	 * 
	 * sets integer value for the second defend die
	 * 
	 */
	public void setDefendDieTwo() {

		// randomly assigns an integer between 1 and 6
		this.defend2die = (int) Math.ceil(Math.random() * 6);
	}

	/**
	 * getAttackDieOne()
	 * 
	 * @return the int value for the first attack die
	 * 
	 */
	public int getAttackDieOne() {
		return attack1die;
	}

	/**
	 * getAttackDieTwo()
	 * 
	 * @return the int value for the second attack die
	 * 
	 */
	public int getAttackDieTwo() {
		return attack2die;
	}

	/**
	 * getAttackDieThree()
	 * 
	 * @return the int value for the third attack die
	 * 
	 */
	public int getAttackDieThree() {
		return attack3die;
	}

	/**
	 * getDefendDieOne()
	 * 
	 * @return the int value for the first defend die
	 * 
	 */
	public int getDefendDieOne() {
		return defend1die;
	}

	/**
	 * getAttackDieTwo()
	 * 
	 * @return the int value for the second defend die
	 * 
	 */
	public int getDefendDieTwo() {
		return defend2die;
	}

	/**
	 * isTeritoryAdj
	 *
	 * @param country1
	 *            : id of the first country
	 * @param country2
	 *            : id of the second country
	 * 
	 * @return true if two countries are adjacent false if two countries are not
	 *         adjacent
	 * 
	 */
	public boolean isTerritoryAdj(int country1, int country2) {
		if (country1 + 1 == country2 || country1 - 1 == country2
				|| country1 + 4 == country2 || country1 - 4 == country2) {

			// return true if country1 is above, below or left or right of
			// country2
			return true;

		} else {
			// return false if country2 is NOT above/below/left/right of
			// country2
			return false;
		}

	}

	/**
	 * setVictor sets the name of the victor to PLAYER
	 *
	 * @param PLAYER
	 *            : name of the player that wins
	 * 
	 */
	public void setVictor(String PLAYER) {

		victor = PLAYER;
	}

	/**
	 * getVictor()
	 *
	 * returns the name of the victor
	 *
	 * @return a string that shows what player won
	 * 
	 */
	public String getVictor() {

		// 100 is the playerID for playerOne
		if (victor.equals("100")) {
			return "PLAYER 1 WINS!";
		} else {
			return "PLAYER 2 WINS!";
		}
	}

	/**
	 * assignUnits
	 *
	 * assigns 3 more troops to the player in the given Country
	 *
	 * @param playerId
	 *            : id of the player
	 * @param countryCode
	 *
	 * 
	 */
	public void assignUnits(int playerId, int countryCode) {

		if (playerId == PLAYER_ONE) {
			playerOneTroops[countryCode] = playerOneTroops[countryCode] + 3;
		} else {
			playerTwoTroops[countryCode] = playerTwoTroops[countryCode] + 3;
		}
		
		haveTroopsBeenPlaced = true;
		
	}

	/**
	 * playerInControl
	 *
	 * @param countryCode
	 *            : id of the country
	 * @return the player that is in control of the given country
	 */
	public int playerInControl(int countryCode) {
		if (playerOneTroops[countryCode] >= 1) {
			return PLAYER_ONE;
		} else {
			return PLAYER_TWO;
		}
	}

	/**
	 * getPlayerTroopsInCountry
	 *
	 * gets the number of troops in the country for the given player
	 *
	 *
	 * @param playerID
	 *            : id of the player
	 * @param countryCode
	 *
	 * @return the number of troops in the country for the given player
	 */
	public int getPlayerTroopsInCountry(int playerID, int countryCode) {
		if (playerID == PLAYER_ONE) {
			return playerOneTroops[countryCode];
		} else {
			return playerTwoTroops[countryCode];
		}
	}

	/**
	 * setGameToBeOver
	 *
	 * Set gameOver boolean to true
	 */
	public void setGameToBeOver() {
		gameOver = true;
	}

	/**
	 * getGameToBeOver
	 *
	 * @return gameOver boolean
	 */
	public boolean getIsGameOver() {
		return gameOver;
	}
	
	/**
	 * setHaveTroopBeenPlayedToFalse
	 *
	 * sets troops Placed back to false
	 */
	public void setHaveTroopBeenPlayedToFalse(){
		haveTroopsBeenPlaced = false;
	}
	
	/**
	 * getHaveTroopBeenPlayed
	 *
	 * @return haveTroopsBeenPlaced boolean
	 */
	public boolean getHaveTroopBeenPlayed(){
		return haveTroopsBeenPlaced;
	}

	/**
	 * setPlayerTurn
	 * 
	 * @param playerID
	 *            : id of the player
	 *
	 *            Set the playerTurn int to ID of passed in player
	 */
	public void setPlayerTurn(int PlayerID) {
		playerTurn = PlayerID;
	}

	/**
	 * getPlayerTurn
	 *
	 * @return current player turn
	 */
	public int getPlayerTurn() {
		return playerTurn;
	}

	/**
	 * init()
	 *
	 * store ID's/constants of countries in array countrylist from the state
	 *
	 */
	private void init() {

		int i;
		RiskState.countryList = new int[RiskState.COUNTRY_LENGTH];
		for (i = 0; i < RiskState.COUNTRY_LENGTH; i++)
			RiskState.countryList[i] = i;
	}
}
