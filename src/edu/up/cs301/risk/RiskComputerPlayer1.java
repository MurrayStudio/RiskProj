package edu.up.cs301.risk;

import java.util.Random;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.util.Tickable;

/**
 * A computer-version of a risk-player.
 * 
 * @authors Shamus Murray, Garrett Becker, Logan Mealy, Lucas Burns, John Will
 *          Bryan
 * 
 * @version March 2015
 * 
 */
public class RiskComputerPlayer1 extends GameComputerPlayer implements
		RiskPlayer, Tickable {

	private int playerID;
	private boolean isItPlayerTurn;
	// the android activity that we are running
	private GameMainActivity myActivity;

	// the most recent game state, as given to us by the RiskLocalGame
	private RiskState state;

	private GameAction currentAction;

	private double actionRandomizer;

	private int countrySelectedIndexId;
	private int countrySelectedIndexId2;

	private boolean troopsPlaced;

	private Random rand = new Random();

	/**
	 * Constructor for objects of class CounterComputerPlayer1
	 * 
	 * @param name
	 *            the player's name
	 */
	public RiskComputerPlayer1(String name, int playerID) {
		// invoke superclass constructor
		super(name);
		this.playerID = playerID;
		troopsPlaced = false;
	}

	/**
	 * sets the troop value in the text view depending on the country selected
	 */
	protected void updateDisplay() {

		Log.i("player turn update", Integer.toString(state.getPlayerTurn()));

		if (state.getPlayerTurn() == RiskState.PLAYER_TWO) {
			// start the timer, ticking 20 times per second
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

	/*	*//**
	 * Controls the actions that the dumb AI makes
	 */
	/*
	 * protected void compAction1() {
	 * 
	 * // send the move-action to the game game.sendAction(new
	 * RiskMoveTroopAction(this, false, 0)); }
	 */

	/**
	 * callback method: the timer ticked, make a random move
	 */
	protected void timerTicked() {
		if (state.getPlayerTurn() == RiskState.PLAYER_TWO) {
			// place troops first
			if (this.state.getHaveTroopBeenPlaced(200) == false) {
			int i;
			// cycles through countries, selecting the first one it owns
			for (i = 1; i < 17; i++) {
				if (this.state.playerInControl(i) == 200
						&& this.state.getPlayerTroopsInCountry(200, i) >= 1) {
					countrySelectedIndexId = i;	
					break;
				}
				}
			RiskPlaceTroopAction placeAction = new RiskPlaceTroopAction(
					this, i, 200);
			game.sendAction(placeAction);
			}

			actionRandomizer = Math.random();

			if (actionRandomizer <= 0.4) {
				// attack
				int i;
				// cycles through countries, selecting the first one it owns
				for (i = 1; i < 17; i++) {
					if (this.state.playerInControl(i) == 200
							&& this.state.getPlayerTroopsInCountry(200, i) >= 2) {
						countrySelectedIndexId = i;
						int y;
						// cycles through countries, selecting the first one it
						// doesn't own
						for (y = 1; y < 17; y++) {
							if (this.state.playerInControl(y) == 100
									&& this.state.getPlayerTroopsInCountry(100,
											y) >= 1
									&& this.state.isTerritoryAdj(i, y) == true) {
								countrySelectedIndexId2 = y;
								break;
							}
						}
						break;
					}
				}
				// send the attack action to the game
				currentAction = new RiskAttackAction(this,
						countrySelectedIndexId, countrySelectedIndexId2);
			}
			
			//move troops
			if (actionRandomizer >= 0.4 && actionRandomizer <= 0.6) {
				int i;
				// cycles through countries, selecting the first one it owns
				for (i = 1; i < 17; i++) {
					if (this.state.playerInControl(i) == 200
							&& this.state.getPlayerTroopsInCountry(200, i) >= 2) {
						countrySelectedIndexId = i;
						int y;
						// cycles through countries, selecting the first one it
						// doesn't own
						for (y = 1; y < 17; y++) {
							if (this.state.playerInControl(y) == 200
									&& this.state.getPlayerTroopsInCountry(200,
											y) >= 1
									&& this.state.isTerritoryAdj(i, y) == true) {
								countrySelectedIndexId2 = y;
								break;
							}
						}
						break;
					}
				}
				currentAction = new RiskMoveTroopAction(this, RiskState.PLAYER_TWO, countrySelectedIndexId, countrySelectedIndexId2);
			}
			
			if (actionRandomizer >= 0.6 && actionRandomizer <= 0.9) {
				// send the end turn action to the game
				currentAction = new RiskEndTurnAction(this, playerID);
			}
			if (actionRandomizer >= 0.9 && actionRandomizer <= 1.0) {
				// send the end turn action to the game
				currentAction = new RiskSurrenderAction(this, playerID);
			}

			if (currentAction instanceof RiskEndTurnAction || currentAction instanceof RiskSurrenderAction) {
				game.sendAction(currentAction);
				getTimer().stop();
				getTimer().reset();

				// state.setPlayerTurn(RiskState.PLAYER_ONE);
				isItPlayerTurn = false;
			} else {
				game.sendAction(currentAction);
			}
		}
	}
	

}