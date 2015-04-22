package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * This action ends the turn of the current player
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will
 *         Bryant
 * @version April 2015
 */
public class RiskEndTurnAction extends GameAction {

	// to satisfy the serializable interface
	private static final long serialVersionUID = 28062013L;

	private int playerID;

	/**
	 * Constructor for the RiskEndTurnAction class.
	 * 
	 * @param source
	 *            the player making the move
	 */
	public RiskEndTurnAction(GamePlayer player,
			int playerID) {
		super(player);
		this.playerID = playerID;

	}

	public int getPlayerID() {
		return this.playerID;
	}

}
