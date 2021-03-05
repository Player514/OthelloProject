import java.util.ArrayList;


/**
 * A simple OthelloAI-implementation. The method to decide the next move just
 * returns the first legal move that it finds.
 * @author Mai Ajspur
 * @version 9.2.2018
 */



public class OthelloAI100 implements IOthelloAI{


  private int max_depth = 8;


	public Position decideMove(GameState s){
    System.out.println("----------------------------------------------");
    printBoard(s);

		ArrayList<Position> moves = actions(s);
		if ( !moves.isEmpty() ){

			int max_score = Integer.MIN_VALUE;
      Position best_position = new Position(-1,-1);
      for (Position a : moves){
        int candidate_score = minValue(result(s, a), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        if (candidate_score>max_score){
          max_score = candidate_score;
          best_position = a;
        }
      }
      System.out.printf("%d %d\n", best_position.col, best_position.row);
      System.out.println(max_score);
      return best_position;
    }
		else
			return new Position(-1,-1);
	}

  //Returns available actions
  private ArrayList<Position> actions(GameState s){
    return s.legalMoves();
  }

  //Simulates one move from GameState s
  private GameState result(GameState s, Position place){
    GameState s1 = new GameState(s.getBoard(), s.getPlayerInTurn());
    s1.insertToken(place);
    return s1;
  }

  //Tests if we have searched sufficiently deeply
  private boolean cutoffTest(int depth){
    if (depth >= max_depth) return true;
    else return false;
  }

  // Returns the MAX value of a move
  private int maxValue(GameState s, int alpha, int beta, int depth){
    if (cutoffTest(depth)) return evaluate(s);
    int v = Integer.MIN_VALUE;
    for (Position a : actions(s)){
      v = Math.max(v, minValue(result(s, a), alpha, beta, depth + 1));
      if (v >= beta) return v;
      alpha = Math.max(alpha, v);
    }
    return v;
  }

  //Returns the MIN value of a move
  private int minValue(GameState s, int alpha, int beta, int depth){
    if (cutoffTest(depth)) return evaluate(s);
    int v = Integer.MAX_VALUE;
    for (Position a : actions(s)){
      v = Math.min(v, maxValue(result(s, a), alpha, beta, depth + 1));
      if (v <= alpha) return v;
      beta = Math.min(beta, v);
    }
    return v;
  }

  //Evaluates the minimax-value of game state
  private int evaluate(GameState s){
    // System.out.print(s.countTokens()[0]);
    // System.out.print(" ");
    // System.out.print(s.countTokens()[1]);
    // System.out.print(" ");
    // System.out.println(s.countTokens()[1] - s.countTokens()[0]);
    return s.countTokens()[1] - s.countTokens()[0];
  }

  private void printBoard(GameState s){
    int[][] board = s.getBoard();
    for (int i = 0; i<=7; i++){
      for (int j = 0; j<=7; j++){
        if (board[j][i] == 0) System.out.print(". ");
        if (board[j][i] == 1) System.out.print("B ");
        if (board[j][i] == 2) System.out.print("W ");
      }
      System.out.println();
    }
    System.out.println();
  }

}
