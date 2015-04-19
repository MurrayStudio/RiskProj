package edu.up.cs301.risk;

import android.util.Log;
import edu.up.cs301.game.Game;
import edu.up.cs301.game.infoMsg.GameState;

/**
 * @authors Shamus Murray, Garrett Becker, Logan Mealy, Lucas Burns, John Will
 *          Bryan
 * 
 *          Risk game state that handles updating information and sending it to
 *          players.
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
	public int[] playerOneTroops = new int[17];

	// stores number of troops in each of the 16 countries for player 2
	public int[] playerTwoTroops = new int[17];

	// prints victor name
	private int victor;

	// have the 3 troops been placed
	private boolean haveTroopsBeenPlacedPlayer1;
	private boolean haveTroopsBeenPlacedPlayer2;

	// booleans for if player1 or player2 surrenders
	private boolean surrenderPlayer1;
	private boolean surrenderPlayer2;

	// variables to store the value of each of the die
	private int attack1die;
	private int attack2die;
	private int attack3die;
	private int defend1die;
	private int defend2die;

	private static int[] countryList; // array of countries on the board
	private final static int COUNTRY_LENGTH = 17; // length of array

	// countryConstants
	final public static int RUSSIA = 1;
	final public static int ICELAND = 2;
	final public static int ITALY = 3;
	final public static int SWEDEN = 4;
	final public static int ATLANTIS = 5;
	final public static int HOGWARTS = 6;
	final public static int NARNIA = 7;
	final public static int GERMANY = 8;
	final public static int MORDOR = 9;
	final public static int GONDOR = 10;
	final public static int SHIRE = 11;
	final public static int ROHAN = 12;
	final public static int BULGARIA = 13;
	final public static int ISRAEL = 14;
	final public static int SWITZERLAND = 15;
	final public static int UKRAINE = 16;

	// player ID numbers
	public final static int PLAYER_ONE = 0;
	public final static int PLAYER_TWO = 1;

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
		for (i = 1; i < COUNTRY_LENGTH; i++) {
			if (i <= 8) {
				playerOneTroops[i] = 3;
			} else {
				playerOneTroops[i] = 0;
			}
		}
		playerTwoTroops = new int[COUNTRY_LENGTH];
		int y;

		// loop through and place troop in every country
		// in bottom 2 rows of map
		for (y = 1; y < COUNTRY_LENGTH; y++) {
			if (y > 8) {
				playerTwoTroops[y] = 3;
			}
			if (y <= 8) {
				playerTwoTroops[y] = 0;
			}
		}

		playerTurn = PLAYER_ONE;

		// initialize boolean variables
		haveTroopsBeenPlacedPlayer1 = false;
		haveTroopsBeenPlacedPlayer2 = false;
		surrenderPlayer1 = false;
		surrenderPlayer2 = false;
	}

	/**
	 * RiskState()
	 * 
	 * creates a copy of the game state to prevent cheating
	 *
	 */
	public RiskState(RiskState orig) {

		this.playerOneTroops = new int[orig.playerOneTroops.length];
		for (int y = 0; y < orig.playerOneTroops.length; ++y) {
			this.playerOneTroops[y] = orig.playerOneTroops[y];
		}

		this.playerTwoTroops = new int[orig.playerTwoTroops.length];
		for (int y = 0; y < orig.playerTwoTroops.length; ++y) {
			this.playerTwoTroops[y] = orig.playerTwoTroops[y];
		}

		this.haveTroopsBeenPlacedPlayer1 = orig.haveTroopsBeenPlacedPlayer1;
		this.haveTroopsBeenPlacedPlayer2 = orig.haveTroopsBeenPlacedPlayer2;
		this.surrenderPlayer1 = orig.surrenderPlayer1;
		this.surrenderPlayer2 = orig.surrenderPlayer2;
		this.playerTurn = orig.playerTurn;

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
	/**
	 * attackWon()
	 * 
	 * When attacking country wins subtract one troop from the losing country
	 * 
	 * @param playerIDAttacking
	 *            : Id of the player attacking
	 * @param countryIDDefendFrom
	 *            ID of the defenders country
	 * @param countryIDAttackFrom
	 *            ID of the attackers country
	 */
	public void attackWon(int playerIDAttacking, int countryIDDefendFrom,
			int countryIDAttackFrom) {

		// P1 won the attack
		if (playerIDAttacking == PLAYER_ONE) {
			// P2 would have at least 1 troop left (still owned by P2)
			if (playerTwoTroops[countryIDDefendFrom] - 1 > 0) {
				// Decrement P2 troops by 1
				playerTwoTroops[countryIDDefendFrom] = playerTwoTroops[countryIDDefendFrom] - 1;
			}
			// P2 would have 0 troops left (now owned by P1)
			else {
				// Remove P2 from the country
				playerTwoTroops[countryIDDefendFrom] = 0;
				// Take one troop from P1s winning country
				playerOneTroops[countryIDAttackFrom] = playerOneTroops[countryIDAttackFrom] - 1;
				// Add that troop to the country P1 won
				playerOneTroops[countryIDDefendFrom] = 1;

			}
		}
		// P2 won the attack
		else {
			if (playerOneTroops[countryIDDefendFrom] - 1 > 0) {

				playerOneTroops[countryIDDefendFrom] = playerOneTroops[countryIDDefendFrom] - 1;
			} else {
				// Remove P1 from the country
				playerOneTroops[countryIDDefendFrom] = 0;
				// Take one troops from P2s winning country
				playerTwoTroops[countryIDAttackFrom] = playerTwoTroops[countryIDAttackFrom] - 1;
				// Add that troop to the country P2 won
				playerTwoTroops[countryIDDefendFrom] = 1;
			}
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
			if (playerTwoTroops[countryIDAttackFrom] - 1 > 0) {
				// if Player2 loses subtract one troop from player2's country
				playerTwoTroops[countryIDAttackFrom] = playerTwoTroops[countryIDAttackFrom] - 1;
			} else {
				playerTwoTroops[countryIDAttackFrom] = 0;
			}
		} else {
			if (playerOneTroops[countryIDAttackFrom] > 0) {
				// else Player1 loses subtract one troop from player1's country
				playerOneTroops[countryIDAttackFrom] = playerOneTroops[countryIDAttackFrom] - 1;
			} else {
				playerOneTroops[countryIDAttackFrom] = 0;
			}
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
		if (country1 == 1 || country1 == 5 || country1 == 9 || country1 == 13) {
			if (country1 + 4 == country2 || country1 - 4 == country2
					|| country1 + 1 == country2) {
				return true;
			}

		} else if (country2 == 1 || country2 == 5 || country2 == 9
				|| country2 == 13) {
			if (country2 + 4 == country1 || country2 - 4 == country1
					|| country2 + 1 == country1) {
				return true;
			}

		} else if (country1 == 4 || country1 == 8 || country1 == 12
				|| country1 == 16) {
			if (country1 + 4 == country2 || country1 - 4 == country2
					|| country1 - 1 == country2) {
				return true;
			}

		} else if (country2 == 4 || country2 == 8 || country2 == 12
				|| country2 == 16) {
			if (country2 + 4 == country1 || country2 - 4 == country1
					|| country2 - 1 == country1) {
				return true;
			}

		}

		else if (country1 + 1 == country2 || country1 - 1 == country2
				|| country1 + 4 == country2 || country1 - 4 == country2) {

			// return true if country1 is above, below or left or right of
			// country2
			return true;

		} else {
			// return false if country2 is NOT above/below/left/right of
			// country2
			return false;
		}
		return false;

	}

	/**
	 * setVictor sets the name of the victor to PLAYER
	 *
	 * @param PLAYER
	 *            : name of the player that wins
	 * 
	 */
	public void setVictor(int playerID) {

		victor = playerID;
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
		if (victor == PLAYER_ONE) {
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

		int i;
		int count1 = 0;
		int count2 = 0;

		// check # of countries owned by player
		for (i = 1; i < COUNTRY_LENGTH; i++) {
			if (playerInControl(i) == PLAYER_ONE) {
				count1++;
			}
			if (playerInControl(i) == PLAYER_TWO) {
				count2++;
			}
		}

		if (playerId == PLAYER_ONE) {
			if (count1 >= 9 && count1 < 11) {
				playerOneTroops[countryCode] = playerOneTroops[countryCode] + 4;
			} else if (count1 >= 12 && count1 < 14) {
				playerOneTroops[countryCode] = playerOneTroops[countryCode] + 5;
			} else if (count1 >= 15) {
				playerOneTroops[countryCode] = playerOneTroops[countryCode] + 6;
			} else {
				playerOneTroops[countryCode] = playerOneTroops[countryCode] + 3;
			}
		} else {
			if (count2 >= 9 && count2 < 11) {
				playerTwoTroops[countryCode] = playerTwoTroops[countryCode] + 4;
			} else if (count2 >= 12 && count2 < 14) {
				playerTwoTroops[countryCode] = playerTwoTroops[countryCode] + 5;
			} else if (count2 >= 15) {
				playerTwoTroops[countryCode] = playerTwoTroops[countryCode] + 6;
			} else {
				playerTwoTroops[countryCode] = playerTwoTroops[countryCode] + 3;
			}
		}

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
	 * setSurrenderPlayerTrue
	 *
	 * @param the
	 *            player that surrendered
	 */
	public void setSurrenderPlayerTrue(int playerID) {
		if (playerID == PLAYER_ONE) {
			surrenderPlayer1 = true;
		} else {
			surrenderPlayer2 = true;
		}
	}

	/**
	 * getSurrenderPlayerTrue
	 *
	 * @param the
	 *            player to check if surrendered
	 * @return return if player has surrendered
	 */
	public boolean getSurrenderPlayerTrue(int playerID) {
		if (playerID == PLAYER_ONE) {
			return surrenderPlayer1;
		} else {
			return surrenderPlayer2;
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
	 * winnerCheck
	 *
	 * checks if a player owns all countries
	 * 
	 * @return player that won, returns 3 if no player has won
	 */
	public int winnerCheck() {
		int count1 = 0;
		int count2 = 0;
		int i;
		for (i = 1; i < COUNTRY_LENGTH; i++) {
			if (playerOneTroops[i] >= 1) {
				count1++;
			}
			if (playerTwoTroops[i] >= 1) {
				count2++;
			}
		}
		// if player one has control of all 16 countries
		if (count1 == 16) {
			return PLAYER_ONE;
		}
		// if player two has control of all 16 countries
		if (count2 == 16) {
			return PLAYER_TWO;
		}
		return 3;
	}

	/**
	 * setHaveTroopBeenPlayedToFalse
	 *
	 * sets troops Placed back to false
	 */
	public void setHaveTroopBeenPlacedToFalse(int playerID) {
		if (playerID == PLAYER_ONE) {
			haveTroopsBeenPlacedPlayer1 = false;
		} else {
			haveTroopsBeenPlacedPlayer2 = false;
		}
	}

	/**
	 * setHaveTroopBeenPlayedToTrue
	 *
	 * sets troops Placed back to True
	 */
	public void setHaveTroopBeenPlacedToTrue(int playerID) {
		if (playerID == PLAYER_ONE) {
			haveTroopsBeenPlacedPlayer1 = true;
		} else {
			haveTroopsBeenPlacedPlayer2 = true;
		}
	}

	/**
	 * getHaveTroopBeenPlayed
	 *
	 * @return haveTroopsBeenPlaced boolean
	 */
	public boolean getHaveTroopBeenPlaced(int playerID) {
		if (playerID == PLAYER_ONE) {
			return haveTroopsBeenPlacedPlayer1;
		} else {
			return haveTroopsBeenPlacedPlayer2;
		}
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
	 * gainTroop
	 * 
	 * add a troop to a country
	 *
	 * @param playerID
	 *            who is gaining troop
	 * @param country
	 *            to get troop
	 */
	public void gainTroop(int playerID, int countryMoveTo) {
		if (playerID == PLAYER_TWO) {
			playerTwoTroops[countryMoveTo] = playerTwoTroops[countryMoveTo] + 1;
		} else {
			playerOneTroops[countryMoveTo] = playerOneTroops[countryMoveTo] + 1;
		}
	}
}