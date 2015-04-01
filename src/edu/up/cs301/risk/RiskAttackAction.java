package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A RiskAttackAction is an action to involving troops of one player
 * attacking a country of another player
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will Bryant
 * @version March 2015
 */
public class RiskAttackAction extends GameAction {
	
	// to satisfy the serializable interface
	private static final long serialVersionUID = 28062013L;

	//whether this move is a plus (true) or minus (false)
	private boolean attackTrue;
	
	/**
	 * Constructor for the RiskAttackAction class.
	 * 
	 * @param source
	 *            the player making the move
	
	 */
	public RiskAttackAction(GamePlayer player, boolean attackTrue) {
		super(player);
		this.attackTrue=attackTrue;
	}
	
	public boolean attackTrue(){
		return attackTrue;
	}
	
	
	
	
	}
//class RiskMoveAction
