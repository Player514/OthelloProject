import java.util.ArrayList;

/**
 * A simple OthelloAI-implementation. The method to decide the next move just
 * returns the first legal move that it finds. 
 * @author Mai Ajspur
 * @version 9.2.2018
 */
public class OneDeepAI implements IOthelloAI{

	/**
	 * Returns first legal move
	 */

	/**
	public Position CreateTree(GameState s){
		
	}
	*/
	 
	 
	public Position decideMove(GameState s){
		
		ArrayList<Position> moves = s.legalMoves();
		ArrayList<GameState> gamestates = new ArrayList<GameState>(moves.size());
		int topscoreindex = -1;
		int topscore = -500;
		for (int i = 0; i < moves.size(); i++) {
			gamestates.add(new GameState(s.getBoard(),1));
			gamestates.get(i).insertToken(moves.get(i))	;
//			System.out.print(gamestates.get(i).dumbScore());
			if(gamestates.get(i).dumbScore() > topscore){
				topscore = gamestates.get(i).dumbScore(); 
				topscoreindex = i;
				//not the best that this gets called twice instead of called and stored only worst case happens every time with average = O log(n)
			}
		}
//		System.out.println();
		
		
//		System.out.println(moves.size());
//		System.out.println(gamestates.size());
//		System.out.println("index =" + topscoreindex);
		if ( topscoreindex  >= 0)
			return moves.get(topscoreindex);
		else
			return new Position(-1,-1);
	}
	
}
