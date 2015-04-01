package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A RiskAttackRollAction is the action to roll the dice for the attacker
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @author logan mealy, garrett becker, lucas burns, shamus murray, John Will Bryant
 * @version March 2015
 */
public class RiskAttackRollAction extends GameAction {
	
	// to satisfy the serializable interface
	private static final long serialVersionUID = 28062013L;

	//whether this move is a plus (true) or minus (false)
	private boolean attackRollTrue;
	
	/**
	 * Constructor for the RiskAttackRoll class.
	 * 
	 * @param source
	 *            the player making the move
	
	 */
	public RiskAttackRollAction(GamePlayer player, boolean attackRollTrue) {
		super(player);
		this.attackRollTrue = attackRollTrue;
		
	}
	
	public boolean attackRollTrue(){
		return attackRollTrue;
	}
	
	
	
	
	}
//class RiskMoveAction
