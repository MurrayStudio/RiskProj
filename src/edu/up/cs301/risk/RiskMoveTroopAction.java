package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A RiskMoveTroopAction is an action that moves any number of troops from one
 * country to another
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will
 *         Bryant
 * @version March 2015
 */
public class RiskMoveTroopAction extends GameAction {

	// to satisfy the serializable interface
	private static final long serialVersionUID = 28062013L;

	// whether this move is a plus (true) or minus (false)
	private boolean moveTroopTrue;

	private int countryID;

	/**
	 * Constructor for the RiskMoveTroopAction class.
	 * 
	 * @param source
	 *            the player making the move
	 */
	public RiskMoveTroopAction(GamePlayer player, boolean moveTroopTrue,
			int countryID) {
		super(player);
		this.moveTroopTrue = moveTroopTrue;
		this.countryID = countryID;
	}

	public boolean moveTroopTrue() {
		return moveTroopTrue;
	}

}
// class RiskMoveAction
