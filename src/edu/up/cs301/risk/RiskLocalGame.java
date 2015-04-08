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
	private String tf;

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

		if (action instanceof RiskPlaceTroopAction) {

			RiskPlaceTroopAction placeAction = (RiskPlaceTroopAction) action;
			playerID = placeAction.getPlayerID();
			int countrySelected = placeAction.getCountryID();

			if (gameState.playerInControl(countrySelected) == playerID) {
				gameState.assignUnits(playerID, countrySelected);
				
				if (this.gameState.getHaveTroopBeenPlaced() == true) {
					tf = "t";
				} else {
					tf = "f";
				}

				Log.i("troop placed state?", tf);
				
				gameState.setHaveTroopBeenPlacedToTrue();
				
				if (this.gameState.getHaveTroopBeenPlaced() == true) {
					tf = "t";
				} else {
					tf = "f";
				}

				Log.i("troop placed state?", tf);
				
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
			int attackcountryID = cma.getAttackCountryID();
			int defendcountryID = cma.getDefendCountryID();
			int defendTroops;
			int attackTroops;
			if (playerID == gameState.PLAYER_ONE) {
				defendTroops = gameState.getPlayerTroopsInCountry(
						defendcountryID, gameState.PLAYER_TWO);
				attackTroops = gameState.getPlayerTroopsInCountry(
						attackcountryID, gameState.PLAYER_ONE);

			} else {
				defendTroops = gameState.getPlayerTroopsInCountry(
						defendcountryID, gameState.PLAYER_ONE);
				attackTroops = gameState.getPlayerTroopsInCountry(
						attackcountryID, gameState.PLAYER_TWO);
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

						int defenddie1 = 0;
						int defenddie2 = 0;

						int attackdie1 = gameState.getAttackDieOne();
						int attackdie2 = gameState.getAttackDieTwo();
						int attackdie3 = gameState.getAttackDieThree();

						int maxAttackDie2; // middle value of three dice
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
							int defendmax1 = Math.max(defenddie2, defenddie1);
							gameState.sethighestdefendroll(defendmax1);
						} else if (defendTroops > 2) {
							defenddie1 = gameState.getDefendDieOne();
							defenddie2 = gameState.getDefendDieTwo();
							int defendmax1 = Math.max(defenddie2, defenddie1);
							int defendmax2 = Math.min(defenddie2, defenddie1);
							gameState.sethighestdefendroll(defendmax1);
							gameState.set2ndhighestdefendroll(defendmax2);
						}
						int tempdie1 = Math.max(attackdie1, attackdie2);// max
																		// of
																		// first
																		// two

						int attackmax1 = Math.max(tempdie1, attackdie3);// gets
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
																			// roll
																			// for
																			// attacker

						// add gameState to set2ndhighestattackroll
						return true;

					} else if (attackTroops == 3) {
						gameState.setAttackDieOne();
						gameState.setAttackDieTwo();

						gameState.setDefendDieOne();
						gameState.setDefendDieTwo();

						int defenddie1 = 0;
						int defenddie2 = 0;

						if (defendTroops <= 2) {
							defenddie1 = gameState.getDefendDieOne();
							int defendmax1 = Math.max(defenddie2, defenddie1);
							gameState.sethighestdefendroll(defendmax1);

						} else if (defendTroops > 2) {
							defenddie1 = gameState.getDefendDieOne();
							defenddie2 = gameState.getDefendDieTwo();
							int defendmax1 = Math.max(defenddie2, defenddie1);// highest
							int defendmax2 = Math.min(defenddie2, defenddie1);// second
																				// highest
							gameState.sethighestdefendroll(defendmax1);
							gameState.set2ndhighestdefendroll(defendmax2);
						}

						int attackdie1 = gameState.getAttackDieOne();
						int attackdie2 = gameState.getAttackDieTwo();
						int attackmax1 = Math.max(attackdie1, attackdie2);
						int attackmax2 = Math.min(attackdie2, attackdie1);

						gameState.sethighestattackroll(attackmax1);
						gameState.set2ndhighestattackroll(attackmax2);

						return true;
					} else if (attackTroops == 2) {
						gameState.setAttackDieOne();
						int attackdie1 = gameState.getAttackDieOne();
						int defenddie1 = 0;
						int defenddie2 = 0;

						if (defendTroops <= 2) {
							defenddie1 = gameState.getDefendDieOne();
							int defendmax1 = Math.max(defenddie2, defenddie1);
							gameState.sethighestdefendroll(defendmax1);

						} else if (defendTroops > 2) {
							defenddie1 = gameState.getDefendDieOne();
							defenddie2 = gameState.getDefendDieTwo();
							int defendmax1 = Math.max(defenddie2, defenddie1);// highest
							int defendmax2 = Math.min(defenddie2, defenddie1);// second
																				// highest
							gameState.sethighestdefendroll(defendmax1);
							gameState.set2ndhighestdefendroll(defendmax2);
						}

						gameState.sethighestattackroll(attackdie1);
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
