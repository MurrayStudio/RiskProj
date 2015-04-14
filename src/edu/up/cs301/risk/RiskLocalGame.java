package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import android.util.Log;

/**
 * A class that represents the state of a game.
 * 
 * @authors Shamus Murray, Garrett Becker, Logan Mealy, Lucas Burns, John Will
 *          Bryan
 * 
 * @version March 2015
 */
public class RiskLocalGame extends LocalGame implements RiskGame {

	// the game's state
	private RiskState gameState;
	private int playerID;
	
	//The max amounts for the attack and defender die
	int defendMax1;
	int defendMax2;
	int attackMax1;
	int attackMax2;
	int maxAttackDie2;
	
	//All the die for attacking and defending
	int defendDie1;
	int defendDie2;
	int attackDie1;
	int attackDie2;
	int attackDie3;
	int tempDie;

	//Variables to use for checking the number of troops in each country
	int defendTroops;
	int attackTroops;
	//Country ids for attacking/defending
	int attackCountryID;
	int defendCountryID;

	/**
	 * can this player move
	 * 
	 * @return returns true or false depending on who's turn it is
	 */
	@Override
	public boolean canMove(int playerIdx) {
		return true;
	}

	/**
	 * This ctor should be called when a new risk game is started
	 */
	public RiskLocalGame() {
		// initialize the game state
		this.gameState = new RiskState();
	}

	/**
	 * The only type of GameAction that should be sent is RiskMoveAction
	 */
	@Override
	public boolean makeMove(GameAction action) {
		//Checking for an end turn action
		if (action instanceof RiskEndTurnAction) {
			//checking to see if it's the right player's turn
			//And then gives access to be able to end the turn
			if (gameState.getPlayerTurn() == RiskState.PLAYER_ONE) {
				gameState.setPlayerTurn(RiskState.PLAYER_TWO);
				gameState.setHaveTroopBeenPlacedToFalse(100);
				return true;
			} else {
				gameState.setPlayerTurn(RiskState.PLAYER_ONE);
				gameState.setHaveTroopBeenPlacedToFalse(200);
				return true;
			}
		}
		//If the action is a place troop action
		if (action instanceof RiskPlaceTroopAction) {

			RiskPlaceTroopAction placeAction = (RiskPlaceTroopAction) action;
			playerID = placeAction.getPlayerID();
			int countrySelected = placeAction.getCountryID();
			//Checks if the country selected to place troops is owned by the current player
			//And then assign the troops to that country
			if (gameState.playerInControl(countrySelected) == playerID) {
				gameState.assignUnits(playerID, countrySelected);
				gameState.setHaveTroopBeenPlacedToTrue(playerID);

				return true;
			} else {
				return false;
			}
		}
		//Checks if the action is a surrender action
		if (action instanceof RiskSurrenderAction) {
			//Then surrenders the game and assigns the victor
			RiskSurrenderAction surrenderAction = (RiskSurrenderAction) action;
			playerID = surrenderAction.getPlayerID();
			gameState.setSurrenderPlayerTrue(playerID);

				return true;
		}
		//Checks if the action is a moving action
		if(action instanceof RiskMoveTroopAction){
			//Gets the countries and the player ids for the movement action
			RiskMoveTroopAction moveAction = (RiskMoveTroopAction) action;
			playerID = moveAction.getPlayerID();
			int countrySelected1 = moveAction.getCountry1();
			int countrySelected2 = moveAction.getCountry2();
			//Checks to make sure the move is legal
			if(gameState.playerInControl(countrySelected1) == playerID && gameState.playerInControl(countrySelected2) == playerID 
					&& gameState.getPlayerTroopsInCountry(playerID, countrySelected1)>1 && gameState.isTerritoryAdj(countrySelected1, countrySelected2)){
					//This assigns the troop to the country moved to and decrements a troop from the 
					//country moved from
					gameState.attackLost(playerID, countrySelected1);
					gameState.gainTroop(playerID, countrySelected2);
					return true;
				}
			
			else{
			return false;
			}
		}
		//Check to see if the action is an attack action
		if (action instanceof RiskAttackAction) 
		{

			// cast so that we Java knows it's a CounterMoveAction
			RiskAttackAction cma = (RiskAttackAction) action;

			playerID = gameState.getPlayerTurn();// gets player id
			attackCountryID = cma.getAttackCountryID();
			defendCountryID = cma.getDefendCountryID();
			
			//Initialize all the die to 0
			defendMax1 = 0;
			defendMax2 = 0;
			attackMax1 = 0;
			attackMax2 = 0;

			defendDie1 = 0;
			defendDie2 = 0;
			attackDie1 = 0;
			attackDie2 = 0;
			attackDie3 = 0;
			tempDie = 0;
			
			//Check to see who's turn it is
			if (playerID == gameState.PLAYER_ONE) 
			{
				defendTroops = gameState.getPlayerTroopsInCountry(
						gameState.PLAYER_TWO, defendCountryID);
				attackTroops = gameState.getPlayerTroopsInCountry(
						gameState.PLAYER_ONE, attackCountryID);

			} 
			else 
			{
				defendTroops = gameState.getPlayerTroopsInCountry(
						gameState.PLAYER_ONE, defendCountryID);
				attackTroops = gameState.getPlayerTroopsInCountry(
						gameState.PLAYER_TWO, attackCountryID);
			}

			// If the attacker is trying to use one or fewer troops to attack
			if (attackTroops <= 1) 
			{
				// return that the move is invalid
				return false;
			} 
			else 
			{
				if (gameState.isTerritoryAdj(attackCountryID, defendCountryID) && gameState.playerInControl(attackCountryID) != gameState.playerInControl(defendCountryID)) 
				{
					// Roll the attack dice based on number of attacking troops (2, 3, 4+)
					attackRoll(attackTroops);

					// Roll the defense dice based on number of defending troops (1, 2+)
					defendRoll(defendTroops);

					// FIGHT TO THE DEATH
					// (determine which player lost the battle)
					FIGHT(playerID, defendCountryID, attackCountryID);

					// return that the move was valid
					return true;
				}
			}
			// denote that this was a legal/successful move
			return true;
		} 
		else
		{
			// denote that this was an illegal move
			return false;
		}

	}
	
	/**
	 * defendRoll handles the rolling and setting of defense dice
	 * 
	 * @param defendTroops
	 */
	public void defendRoll(int defendTroops)
	{
		// Roll one die by default
		gameState.setDefendDieOne();
		
		// If the defender has 1 troop in the country
		if (defendTroops < 2)
		{
			// Get the die roll
			defendDie1 = gameState.getDefendDieOne();
			
			// Set max die to the only die rolled
			defendMax1 = defendDie1;	
		}
		// If the defender has more than one troop in the country
		else if (defendTroops >= 2)
		{
			// Roll the second die
			gameState.setDefendDieTwo();

			// Retrieve both dice values
			defendDie1 = gameState.getDefendDieOne();
			defendDie2 = gameState.getDefendDieTwo();
			
			// Set the max die to the greater of the two
			defendMax1 = Math.max(defendDie2, defendDie1);
			// Set the mid die to the lesser of the two
			defendMax2 = Math.min(defendDie2, defendDie1);
		}
	}

	/**
	 * attackRoll handles the rolling and setting of attack dice
	 * 
	 * @param attackTroops
	 */
	public void attackRoll(int attackTroops)
	{
		if (attackTroops >= 4) 
		{
			// Roll all attack dice
			gameState.setAttackDieOne();
			gameState.setAttackDieTwo();
			gameState.setAttackDieThree();

			// Retreive all attack dice
			attackDie1 = gameState.getAttackDieOne();
			attackDie2 = gameState.getAttackDieTwo();
			attackDie3 = gameState.getAttackDieThree();

			// Find the max of all three attack dice, set it in the gameState
			attackMax1 = Math.max(attackDie1, Math.max(attackDie2, attackDie3));

			// Find the middle of all three attack dice, set it in the gameState
			attackMax2 = Math.max(Math.max(Math.min(attackDie1, attackDie2), Math.min(attackDie2, attackDie3)), 
					(Math.max(Math.min(attackDie2, attackDie3), Math.min(attackDie1, attackDie3))));
		}
		else if (attackTroops == 3)
		{
			// Only roll two dice for attacker
			gameState.setAttackDieOne();
			gameState.setAttackDieTwo();

			// Get results of roll
			attackDie1 = gameState.getAttackDieOne();
			attackDie2 = gameState.getAttackDieTwo();
			
			// Set max and mid dice for attacker
			attackMax1 = Math.max(attackDie1, attackDie2);
			attackMax2 = Math.min(attackDie2, attackDie1);
		}
		else if (attackTroops == 2) 
		{
			// Roll one die for attacker
			gameState.setAttackDieOne();
			
			// Retrieve the one attacking die
			attackDie1 = gameState.getAttackDieOne();
			
			// Set the max die to the one available
			attackMax1 = attackDie1;
		}
	}


	/**
	 * FIGHT determines whose troops to decrease/who won the battle
	 * 
	 * @param playerID
	 * @param defendCountryID
	 * @param attackCountryID
	 */
	public void FIGHT (int playerID, int defendCountryID, int attackCountryID)
	{
		// If the attackers highest die is higher than the defenders highest die
		if (attackMax1 > defendMax1) 
		{
			// Attacker wins
			gameState.attackWon(playerID, defendCountryID, attackCountryID);
		} 
		// If the attackers highest die is NOT higher than the defenders highest die (includes ties)
		else
		{
			// Defender wins
			gameState.attackLost(playerID, attackCountryID);
		}

		// If the middle attack and defend dice are not 0 (meaning they have been rolled)
		if (attackMax2 != 0 && defendMax2 != 0)
		{
			// If the attackers middle die is higher than the defenders middle (lowest of two) die
			if (attackMax2 > defendMax2) 
			{
				// Attacker wins
				gameState.attackWon(playerID, defendCountryID, attackCountryID);
			}
			// If the attackers middle die is NOT higher than the defenders middle (lowest of two) die
			else 
			{
				// Defender wins
				gameState.attackLost(playerID, attackCountryID);
			}
		}
	}


	// makeMove

	/**
	 * send the updated state to a given player
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// this is a perfect-information game, so we'll make a
		// complete copy of the state to send to the player
		p.sendInfo(new RiskState(gameState));

	}// sendUpdatedSate

	/**
	 * Check if the game is over. It is over, return a string that tells who the
	 * winner(s), if any, are. If the game is not over, return null;
	 * 
	 * @return a message that tells who has won the game, or null if the game is
	 *         not over
	 */
	@Override
	public String checkIfGameOver() {
		return null;
	}

}// class RiskLocalGame