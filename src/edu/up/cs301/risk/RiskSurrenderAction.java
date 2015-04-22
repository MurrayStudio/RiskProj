package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A RiskSurrenderAction is an action that is called for a player to end the
 * game
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will
 *         Brian
 * @version April 2015
 */
public class RiskSurrenderAction extends GameAction {

	// to satisfy the serializable interface
	private static final long serialVersionUID = 28062013L;

	// whether this move is a plus (true) or minus (false)
	private int playerID;

	/**
	 * Constructor for the RiskSurrenderAction class.
	 * 
	 * @param source
	 *            the player making the move
	 */
	public RiskSurrenderAction(GamePlayer player,
			int playerID) {
		super(player);
		this.playerID = playerID;

	}
	
	public int getPlayerID() {
		return playerID;
	}

}
// class RiskMoveAction
