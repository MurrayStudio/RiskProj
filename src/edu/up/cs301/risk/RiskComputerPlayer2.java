package edu.up.cs301.risk;

import android.util.Log;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.Tickable;

/**
 * A computer-version of a risk-player.
 * Smart AI
 * 
 * @authors Shamus Murray, Garrett Becker, Logan Mealy, Lucas Burns, John Will Bryan 
 * 
 * @version April 2015 
 * 
 * 
 */
public class RiskComputerPlayer2 extends GameComputerPlayer implements RiskPlayer, Tickable {
	//instance variables

	//the most recent game state, as given to us by the RiskLocalGame
	private RiskState state;

	//current game action
	private GameAction currentAction;

	/**
	 * Constructor for objects of class CounterComputerPlayer1
	 * 
	 * @param name
	 * 		the player's name
	 */
	public RiskComputerPlayer2(String name) {
		// invoke superclass constructor
		super(name);
	}
	/**
	 * updates display based on whose turn it is
	 */
	protected void updateDisplay() {

		Log.i("player turn update", Integer.toString(state.getPlayerTurn()));

		//determines if it is the computers turn
		if (state.getPlayerTurn() == RiskState.PLAYER_TWO) {
			// starts the timer at the beginning of turn, ticking 20 times per second 
			getTimer().setInterval(200);
			getTimer().start();
		}
	}

	/**
	 * callback method--game's state has changed
	 * 
	 * @param info
	 *            the information (presumably containing the game's state)
	 */
	@Override
	protected void receiveInfo(GameInfo info) {
		// ignore the message if it's not a RiskState message
		if (!(info instanceof RiskState))
			return;

		this.state = (RiskState) info;
		updateDisplay();
	}

	/**
	 * callback method: the timer ticked, makes a random move
	 */
	protected void timerTicked() 
	{
		// Iterators, placeholders
		int i;
		int j;
		int temp = 0;
		int largestCountry = 1;

		// If it is the computers turn
		if (state.getPlayerTurn() == RiskState.PLAYER_TWO)
		{
			// cycles through countries
			outerLoop:
				for (i = 1; i < 17; i++) 
				{
					// If the country is owned by the computer
					if (this.state.playerInControl(i) == RiskState.PLAYER_TWO)
					{	
						// If the temp hasn't been touched yet
						if (temp == 0)
						{
							// Make temp the first country that the computer owns
							temp = i;
						}

						// Cycles through all countries after the first one the computer owns
						for (j = i; j < 17; j++) 
						{
							// If the computer owns the country to be compared
							if (this.state.playerInControl(j) == RiskState.PLAYER_TWO)
							{
								// If the country to be compared is the first country the computer owns
								if (j == temp)
								{
									// Set the largest to that country
									largestCountry = temp;
								}

								// If the computer has one troop in a country
								if (this.state.getPlayerTroopsInCountry(RiskState.PLAYER_TWO, j) == 1) 
								{
									// Choose to fortify that country
									largestCountry = j;
									break outerLoop;
								}

								// If the country j has more troops than the one its being compared to, and has an adjacent opponent country
								if (this.state.getPlayerTroopsInCountry(RiskState.PLAYER_TWO, j) >
								this.state.getPlayerTroopsInCountry(RiskState.PLAYER_TWO, i) && 
								hasAdjacentOpponent(j))
								{
									largestCountry = j;
									break outerLoop;
								}
							}
						}

						// If we've made it to the last country, go back to the temp country
						if (i == 17)
						{
							largestCountry = temp;
						}
					}
				}

		// If AI hasnt placed troops yet this turn
		if (this.state.getHaveTroopBeenPlaced(RiskState.PLAYER_TWO) == false) 
		{
			// Start the turn counter (for managing number of actions per turn)
			state.startTurn();

			// Send the place troops action and mark that troops have been placed
			RiskPlaceTroopAction placeAction = new RiskPlaceTroopAction(this, largestCountry, RiskState.PLAYER_TWO);
			game.sendAction(placeAction);
			this.state.setHaveTroopBeenPlacedToTrue(200);
		}

		// Run through the countries again
		for (i = 1; i < 17; i++)
		{
			// If the country has more than 3 troops in it and has no adjecent enemy country
			if (this.state.getPlayerTroopsInCountry(RiskState.PLAYER_TWO, i) > 3 &&
					!hasAdjacentOpponent(i))
			{
				// Get a list of its adjacent allies
				int[] adjacentAllies = getAdjacentAllies(i);

				// Check each adjacent ally
				for (int k = 0; k < adjacentAllies.length; k++) 
				{
					// If the index is not 0/null and has an adjacent opponent
					if (adjacentAllies[k] != 0 && hasAdjacentOpponent(adjacentAllies[k]))
					{
						// Send the movetroops action
						currentAction = new RiskMoveTroopAction(this, RiskState.PLAYER_TWO, i, adjacentAllies[k]);
						game.sendAction(currentAction);
					}
				}
			}
		}

		// Get a list of the country in use's adjacent enemies
		int[] adjacentEnemies = getAdjacentEnemies(largestCountry);

		// If the array is nonempty
		if (adjacentEnemies[0] != 0)
		{
			// Set the weakest country around the largest to the first in the array by default
			int weakestCountry = adjacentEnemies[0];

			for (i = 1; i < adjacentEnemies.length; i++) 
			{
				// If the number of troops in the next surrounding country is less than in the active surrounding country
				// and the country index is not 0/null
				if (this.state.getPlayerTroopsInCountry(RiskState.PLAYER_ONE, adjacentEnemies[i]) < 
						this.state.getPlayerTroopsInCountry(RiskState.PLAYER_ONE, weakestCountry) && 
						adjacentEnemies[i] != 0) 
				{
					// Set the country to be attacked to the i index of the array
					weakestCountry = adjacentEnemies[i];
				}
			}

			// If the number of troops in the active center country is greater than
			// the number in the weakest country, and the largest can attack
			if (this.state.getPlayerTroopsInCountry(RiskState.PLAYER_TWO, largestCountry) >
			this.state.getPlayerTroopsInCountry(RiskState.PLAYER_ONE, weakestCountry) &&
			this.state.getPlayerTroopsInCountry(RiskState.PLAYER_TWO, largestCountry) > 1) 
			{
				// And if the other player controls the country to be attacked
				if (this.state.playerInControl(weakestCountry) == RiskState.PLAYER_ONE)
				{
					// Send the attack action
					currentAction = new RiskAttackAction(this, largestCountry, weakestCountry);
					game.sendAction(currentAction);
				}
			}
		}

		// Increment the number of actions taken this turn for the AI
		this.state.turnCountUp();

		// Once the AI performs 4 actions, stop its turn
		if (this.state.getTurnCount() >= 4)
		{
			// Send the end turn action
			currentAction = new RiskEndTurnAction(this, playerNum);
			game.sendAction(currentAction);
		}
		}
	}


	/**
	 * gets and returns a list of the enemies adjacent to the given country
	 * 
	 * @param center
	 * 		the country to search around
	 * @return array of adjacent enemies
	 */
	public int[] getAdjacentEnemies(int center)
	{
		// Initialize an array for the 4 possible adjacent countries
		int[] adjacentCountries = new int[4];
		int index = 0;
		
		// Iterate through all countries
		for (int i = 1; i < 17; i++) 
		{
			// If i is adjacent to the country at the center and is controlled by the first player
			if (state.isTerritoryAdj(i, center) && state.playerInControl(i) == RiskState.PLAYER_ONE)
			{
				// Add it to the array and increment the index for the array
				adjacentCountries[index] = i;
				index++;
			}
		}
		return adjacentCountries;	
	}

	/**
	 * gets and returns a list of the allies adjacent to the given country
	 * 
	 * @param center
	 * 		the country to search around
	 * @return array of adjacent allies
	 */
	public int[] getAdjacentAllies(int center)
	{
		// Initialize an array for the 4 possible adjacent countries
				int[] adjacentCountries = new int[4];
				int index = 0;
				
				// Iterate through all countries
				for (int i = 1; i < 17; i++) 
				{
					// If i is adjacent to the country at the center and is controlled by the first player
					if (state.isTerritoryAdj(i, center) && state.playerInControl(i) == RiskState.PLAYER_ONE)
					{
						// Add it to the array and increment the index for the array
						adjacentCountries[index] = i;
						index++;
					}
				}
				return adjacentCountries;	
	}

	/**
	 * tells if the given country has any attackable countries around it
	 * 
	 * @param center
	 * 		country to search around
	 * @return boolean of whether there are valid attack countries
	 */
	public boolean hasAdjacentOpponent(int center)
	{
		// Iterate through all countries
		for (int i = 1; i < 17; i++) 
		{
			// If i is adjacent to the country passed in and is controlled by the enemy
			if (state.isTerritoryAdj(i, center) && state.playerInControl(i) == RiskState.PLAYER_ONE)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * tells if the given country has any friendly countries around it
	 * 
	 * @param center
	 * 		country to search around
	 * @return boolean of whether there are valid ally countries
	 */
	public boolean hasAdjacentAlly(int center)
	{
		// Iterate through all countries
		for (int i = 1; i < 17; i++) 
		{
			// If i is adjacent to the country passed in and is controlled by the AI
			if (state.isTerritoryAdj(i, center) && state.playerInControl(i) == RiskState.PLAYER_TWO)
			{
				return true;
			}
		}

		return false;
	}
}