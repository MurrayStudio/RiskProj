package edu.up.cs301.risk;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import android.util.Log;

/**
 * A class that represents the state of a game.
 * 
 * @authors Shamus Murray, Garrett Becker, Logan Mealy, Lucas Burns, John Will
 *          Bryan
 * 
 * @version March 2015
 */
public class RiskLocalGame extends LocalGame implements RiskGame {

	// the game's state
	private RiskState gameState;
	private int playerID;

	int defendmax1;
	int defendmax2;
	int attackmax1;
	int attackmax2;
	int maxAttackDie2;

	int defenddie1;
	int defenddie2;
	int attackdie1;
	int attackdie2;
	int attackdie3;
	int tempdie1;

	int defendTroops;
	int attackTroops;

	int attackcountryID;
	int defendcountryID;

	/**
	 * can this player move
	 * 
	 * @return returns true or false depending on who's turn it is
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

		if (action instanceof RiskEndTurnAction) {
			
			if (gameState.getPlayerTurn() == RiskState.PLAYER_ONE) {
				gameState.setPlayerTurn(RiskState.PLAYER_TWO);
				return true;
			}
			else{
				gameState.setPlayerTurn(RiskState.PLAYER_ONE);
				return true;
			}
		}

		if (action instanceof RiskPlaceTroopAction) {

			RiskPlaceTroopAction placeAction = (RiskPlaceTroopAction) action;
			playerID = placeAction.getPlayerID();
			int countrySelected = placeAction.getCountryID();

			if (gameState.playerInControl(countrySelected) == playerID) {
				gameState.assignUnits(playerID, countrySelected);
				gameState.setHaveTroopBeenPlacedToTrue(playerID);

				return true;
			} else {
				return false;
			}
		}

		if (action instanceof RiskAttackAction) {

			// cast so that we Java knows it's a CounterMoveAction
			RiskAttackAction cma = (RiskAttackAction) action;

			// Update the counter values based upon the action
			// int result = gameState.getCounter() + (cma.isPlus() ? 1 : -1);
			// gameState.setCounter(result);

			playerID = gameState.getPlayerTurn();// gets player id
			attackcountryID = cma.getAttackCountryID();
			defendcountryID = cma.getDefendCountryID();

			if (playerID == gameState.PLAYER_ONE) {
				defendTroops = gameState.getPlayerTroopsInCountry(
						gameState.PLAYER_TWO, defendcountryID);
				attackTroops = gameState.getPlayerTroopsInCountry(
						gameState.PLAYER_ONE, attackcountryID);

			} else {
				defendTroops = gameState.getPlayerTroopsInCountry(
						gameState.PLAYER_ONE, defendcountryID);
				attackTroops = gameState.getPlayerTroopsInCountry(
						gameState.PLAYER_TWO, attackcountryID);
			}

			if (attackTroops <= 1) {
				return false;
			} else {
				if (gameState.isTerritoryAdj(attackcountryID, defendcountryID)) {
					// get dice information based on number of troops attacker
					// wants to attack with
					if (attackTroops >= 4) {

						gameState.setAttackDieOne();
						gameState.setAttackDieTwo();
						gameState.setAttackDieThree();

						gameState.setDefendDieOne();
						gameState.setDefendDieTwo();

						defenddie1 = 0;
						defenddie2 = 0;

						attackdie1 = gameState.getAttackDieOne();
						attackdie2 = gameState.getAttackDieTwo();
						attackdie3 = gameState.getAttackDieThree();

						// middle value of three dice
						if (attackdie1 >= attackdie2) {
							if (attackdie1 >= attackdie3) {

								if (attackdie2 >= attackdie3) {

									maxAttackDie2 = attackdie2;

								} else {
									maxAttackDie2 = attackdie3;

								}
							} else {

								maxAttackDie2 = attackdie1;

							}
						} else if (attackdie2 >= attackdie3) {

							if (attackdie1 >= attackdie3) {
								maxAttackDie2 = attackdie1;

							}

							else {

								maxAttackDie2 = attackdie3;
							}

						} else {

							if (attackdie1 >= attackdie2) {

								maxAttackDie2 = attackdie1;
							} else {

								maxAttackDie2 = attackdie2;
							}
						}

						if (defendTroops <= 2) {
							defenddie1 = gameState.getDefendDieOne();
							defendmax1 = Math.max(defenddie2, defenddie1);
							gameState.sethighestdefendroll(defendmax1);
						} else if (defendTroops > 2) {
							defenddie1 = gameState.getDefendDieOne();
							defenddie2 = gameState.getDefendDieTwo();
							defendmax1 = Math.max(defenddie2, defenddie1);
							defendmax2 = Math.min(defenddie2, defenddie1);
							gameState.sethighestdefendroll(defendmax1);
							gameState.set2ndhighestdefendroll(defendmax2);
						}
						tempdie1 = Math.max(attackdie1, attackdie2);// max
																	// of
																	// first
																	// two

						attackmax1 = Math.max(tempdie1, attackdie3);// gets
																	// the
																	// greatest
																	// value
																	// of
																	// attackers
																	// dice

						gameState.sethighestattackroll(attackmax1);// highest
																	// roll for
																	// attacker
						gameState.set2ndhighestattackroll(maxAttackDie2);// 2nd
																			// highest

						// determines winner of attack
						if (attackmax1 > defendmax1) {
							gameState.attackWon(playerID, defendcountryID);
						} else {
							gameState.attackLost(playerID, attackcountryID);
						}
						if (maxAttackDie2 > defendmax2) {
							gameState.attackWon(playerID, defendcountryID);
						} else {
							gameState.attackLost(playerID, attackcountryID);
						}

						// attacker

						// add gameState to set2ndhighestattackroll
						return true;

					} else if (attackTroops == 3) {
						gameState.setAttackDieOne();
						gameState.setAttackDieTwo();

						gameState.setDefendDieOne();
						gameState.setDefendDieTwo();

						defenddie1 = 0;
						defenddie2 = 0;

						if (defendTroops <= 2) {
							defenddie1 = gameState.getDefendDieOne();
							defendmax1 = Math.max(defenddie2, defenddie1);
							gameState.sethighestdefendroll(defendmax1);

						} else if (defendTroops > 2) {
							defenddie1 = gameState.getDefendDieOne();
							defenddie2 = gameState.getDefendDieTwo();
							defendmax1 = Math.max(defenddie2, defenddie1);// highest
							defendmax2 = Math.min(defenddie2, defenddie1);// second
																			// highest
							gameState.sethighestdefendroll(defendmax1);
							gameState.set2ndhighestdefendroll(defendmax2);
						}

						attackdie1 = gameState.getAttackDieOne();
						attackdie2 = gameState.getAttackDieTwo();
						attackmax1 = Math.max(attackdie1, attackdie2);
						attackmax2 = Math.min(attackdie2, attackdie1);

						gameState.sethighestattackroll(attackmax1);
						gameState.set2ndhighestattackroll(attackmax2);

						if (attackmax1 > defendmax1) {
							gameState.attackWon(playerID, defendcountryID);
						} else {
							gameState.attackLost(playerID, attackcountryID);
						}
						if (attackmax2 > defendmax2) {
							gameState.attackWon(playerID, defendcountryID);
						} else {
							gameState.attackLost(playerID, attackcountryID);
						}

						return true;
					} else if (attackTroops == 2) {
						gameState.setAttackDieOne();
						attackdie1 = gameState.getAttackDieOne();
						defenddie1 = 0;
						defenddie2 = 0;

						if (defendTroops <= 2) {
							defenddie1 = gameState.getDefendDieOne();
							defendmax1 = Math.max(defenddie2, defenddie1);
							gameState.sethighestdefendroll(defendmax1);

						} else if (defendTroops > 2) {
							defenddie1 = gameState.getDefendDieOne();
							defenddie2 = gameState.getDefendDieTwo();
							defendmax1 = Math.max(defenddie2, defenddie1);// highest
							defendmax2 = Math.min(defenddie2, defenddie1);// second
																			// highest
							gameState.sethighestdefendroll(defendmax1);
							gameState.set2ndhighestdefendroll(defendmax2);
						}

						gameState.sethighestattackroll(attackdie1);

						if (attackdie1 > defendmax1) {
							gameState.attackWon(playerID, defendcountryID);
						} else {
							gameState.attackLost(playerID, attackcountryID);
						}
						return true;
					}
				}
			}

			// denote that this was a legal/successful move
			return true;
		} else {
			// denote that this was an illegal move
			return false;
		}

	}

	// makeMove

	/**
	 * send the updated state to a given player
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		// this is a perfect-information game, so we'll make a
		// complete copy of the state to send to the player
		p.sendInfo(new RiskState(gameState));

	}// sendUpdatedSate

	/**
	 * Check if the game is over. It is over, return a string that tells who the
	 * winner(s), if any, are. If the game is not over, return null;
	 * 
	 * @return a message that tells who has won the game, or null if the game is
	 *         not over
	 */
	@Override
	public String checkIfGameOver() {

		return null;
	}

}// class RiskLocalGame