package edu.up.cs301.risk;

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
	}

	/**
	 * sets the troop value in the text view depending on the country selected
	 */
	protected void updateDisplay() {

		Log.i("player turn update", Integer.toString(state.getPlayerTurn()));
		
		if (state.getPlayerTurn() == playerID) {
			isItPlayerTurn = true;
		}
		
		if (isItPlayerTurn == true) {
			// start the timer, ticking 20 times per second
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

		// send the move-action to the game
		// game.sendAction(new RiskMoveTroopAction(this, false, 0));

		// currentAction = new RiskMoveTroopAction(this, false, 0);

		currentAction = new RiskEndTurnAction(this, playerID);

		if (currentAction instanceof RiskEndTurnAction) {
			game.sendAction(currentAction);
			getTimer().stop();
			
			Log.i("player turn b", Integer.toString(state.getPlayerTurn()));
			
			state.setPlayerTurn(RiskState.PLAYER_ONE);
			isItPlayerTurn = false;
			
			Log.i("player turn a", Integer.toString(state.getPlayerTurn()));
		}
	}

}
