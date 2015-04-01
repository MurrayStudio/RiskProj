package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import android.util.Log;

/**
 * A class that represents the state of a game. 
 * 
 * @authors Shamus Murray, Garrett Becker, Logan Mealy, Lucas Burns, John Will Bryan 
 * 
 * @version March 2015 
 */
public class RiskLocalGame extends LocalGame implements RiskGame {

	

	// the game's state
	private RiskState gameState;
	
	/**
	 * can this player move
	 * 
	 * @return
	 * 		returns true or false depending on who's turn it is
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
		
			return true;
		}
		
	//makeMove
	
	/**
	 * send the updated state to a given player
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// this is a perfect-information game, so we'll make a
		// complete copy of the state to send to the player
		p.sendInfo(new RiskState(gameState));
		
	}//sendUpdatedSate
	
	/**
	 * Check if the game is over. It is over, return a string that tells
	 * who the winner(s), if any, are. If the game is not over, return null;
	 * 
	 * @return
	 * 		a message that tells who has won the game, or null if the
	 * 		game is not over
	 */
	@Override
	public String checkIfGameOver() {
		

		return null;
	}

}// class RiskLocalGame
