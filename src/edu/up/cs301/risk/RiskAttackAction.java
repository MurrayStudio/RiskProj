package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A RiskAttackAction is an action to involving troops of one player attacking a
 * country of another player
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will
 *         Bryant
 * @version March 2015
 */
public class RiskAttackAction extends GameAction {

	private boolean attackTrue;
	private int attackcountryID;
	private int defendcountryID;

	/**
	 * Constructor for the RiskAttackAction class.
	 * 
	 * @param source
	 *            the player making the move
	 */
	public RiskAttackAction(GamePlayer player, boolean attackTrue,
			int attackcountryID, int defendcountryID) {
		super(player);
		this.attackTrue = attackTrue;
		this.attackcountryID = attackcountryID;
		this.defendcountryID = defendcountryID;
	}

	public boolean attackTrue() {
		return attackTrue;
	}

	public int getAttackCountryID() {
		return attackcountryID;
	}

	public int getDefendCountryID() {
		return defendcountryID;
	}
}
// class RiskMoveAction
