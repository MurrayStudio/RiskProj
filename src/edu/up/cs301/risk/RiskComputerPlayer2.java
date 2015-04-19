package edu.up.cs301.risk;

import android.util.Log;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.Tickable;

/**
 * A computer-version of a risk-player.
 * 
 * @authors Shamus Murray, Garrett Becker, Logan Mealy, Lucas Burns, John Will Bryan 
 * 
 * @version March 2015 
 * 
 * 
 */
public class RiskComputerPlayer2 extends GameComputerPlayer implements RiskPlayer, Tickable {
	//instance variables

	// the most recent game state, as given to us by the RiskLocalGame
	private RiskState state;

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

	protected void updateDisplay() {

		Log.i("player turn update", Integer.toString(state.getPlayerTurn()));

		//determines if it is the computers turn
		if (state.getPlayerTurn() == RiskState.PLAYER_TWO) {
			// starts the timer at the beginning of turn, ticking 20 times per second 
			getTimer().setInterval(500);
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

		if (state.getPlayerTurn() == RiskState.PLAYER_TWO && 
				this.state.getHaveTroopBeenPlaced(RiskState.PLAYER_TWO) == false) 
		{
			// Iterators
			int i;
			int j;
			int temp = 0;
			int largestCountry = 1;

			// cycles through countries, selecting the first one it owns
			outerLoop:
				for (i = 1; i < 17; i++) 
				{
					if (this.state.playerInControl(i) == RiskState.PLAYER_TWO)
					{	
						if (temp == 0)
						{
							temp = i;
						}

						for (j = i; j < 17; j++) 
						{
							if (this.state.playerInControl(j) == RiskState.PLAYER_TWO)
							{
								if (j == temp)
								{
									largestCountry = temp;
								}

								if (this.state.getPlayerTroopsInCountry(RiskState.PLAYER_TWO, j) == 1) {
									largestCountry = j;
									break outerLoop;
								}

								if (this.state.getPlayerTroopsInCountry(RiskState.PLAYER_TWO, j) >
								this.state.getPlayerTroopsInCountry(RiskState.PLAYER_TWO, i))
								{
									largestCountry = j;
									break outerLoop;
								}
							}
						}

						if (i == 17)
						{
							largestCountry = temp;
						}
					}
				}

			//sets place troops game action and sends to  the state
			RiskPlaceTroopAction placeAction = new RiskPlaceTroopAction(this, largestCountry, RiskState.PLAYER_TWO);
			game.sendAction(placeAction);
			this.state.setHaveTroopBeenPlacedToTrue(RiskState.PLAYER_TWO);

			int[] adjacentCountries = getAdjacentCountries(largestCountry);
			int weakestCountry = adjacentCountries[0];

			for (i = 1; i < adjacentCountries.length; i++) 
			{
				if (this.state.playerInControl(i) == RiskState.PLAYER_ONE &&
						this.state.getPlayerTroopsInCountry(RiskState.PLAYER_ONE, adjacentCountries[i]) < 
						this.state.getPlayerTroopsInCountry(RiskState.PLAYER_ONE, weakestCountry) && 
						adjacentCountries[i] != 0) 
				{
					weakestCountry = adjacentCountries[i];
				}
			}
			
			int attackCount = 0;
			
			while (this.state.getPlayerTroopsInCountry(RiskState.PLAYER_TWO, largestCountry) + 3 >
			this.state.getPlayerTroopsInCountry(RiskState.PLAYER_ONE, weakestCountry) &&
			this.state.getPlayerTroopsInCountry(RiskState.PLAYER_TWO, largestCountry) > 1 &&
			attackCount < 4) 
			{
				attackCount++;
				if (state.playerInControl(weakestCountry) != RiskState.PLAYER_TWO)
				{
					currentAction = new RiskAttackAction(this, largestCountry, weakestCountry);
					game.sendAction(currentAction);
				}
			}

			currentAction = new RiskEndTurnAction(this, playerNum);
			game.sendAction(currentAction);
		}
	}

	/**
	 * gets and returns a list of the countries adjacent to the given country
	 * 
	 * @param center
	 * 		the country to search around
	 * @return array of adjacent countries
	 */
	public int[] getAdjacentCountries(int center)
	{
		int[] adjacentCountries = new int[4];
		int index = 0;

		for (int i = 1; i < 17; i++) 
		{
			if (state.isTerritoryAdj(i, center) && state.playerInControl(i) == RiskState.PLAYER_ONE)
			{
				adjacentCountries[index] = i;
				index++;
			}
		}
		return adjacentCountries;	
	}
}