import java.util.ArrayList;


public class OthelloAI100 implements IOthelloAI{


  private int max_depth = 7;


	public Position decideMove(GameState s){
    System.out.println("----------------------------------------------");
    printBoard(s);

		ArrayList<Position> moves = actions(s);
    // System.out.println(moves.size());
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
      //System.out.printf("%d %d\n", best_position.col, best_position.row);
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
    if (s.isFinished()) return evaluateEnd(s);
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
    if (s.isFinished()) return evaluateEnd(s);
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
  //Evaluates simply by the number of stones belonging to the AI compared to the opponent
  private int evaluateNaive(GameState s){
    // System.out.print(s.countTokens()[0]);
    // System.out.print(" ");
    // System.out.print(s.countTokens()[1]);
    // System.out.print(" ");
    // System.out.println(s.countTokens()[1] - s.countTokens()[0]);
    return s.countTokens()[1] - s.countTokens()[0];
  }



  //This function counts the number of corner squares per player
  private int[] countCorners(GameState s){
    int[] corners = {0,0};
    int[][] board = s.getBoard();
    if (board[0][0] != 0) corners[board[0][0] - 1] += 1;
    if (board[0][7] != 0) corners[board[0][7] - 1] += 1;
    if (board[7][0] != 0) corners[board[7][0] - 1] += 1;
    if (board[7][7] != 0) corners[board[7][7] - 1] += 1;
    return corners;
  }


  //This function counts the number of "X-squares" for each player, which are the
  //4 squares diagonally next to the corner squares. You don't want to put a token here
  private int[] countXSquares(GameState s){
    int[] XSquares = {0,0};
    int[][] board = s.getBoard();
    if (board[1][1] != 0) XSquares[board[1][1] - 1] += 1;
    if (board[1][6] != 0) XSquares[board[1][6] - 1] += 1;
    if (board[6][1] != 0) XSquares[board[6][1] - 1] += 1;
    if (board[6][6] != 0) XSquares[board[6][6] - 1] += 1;
    return XSquares;
  }



  private int evaluate(GameState s){
    int my_number_of_moves;
    int opponent_number_of_moves;
    int[] corners = countCorners(s);
    int[] XSquares = countXSquares(s);
    int totalScore = 0;
    if (s.getPlayerInTurn() == 2){
      my_number_of_moves = s.legalMoves().size();
      s.changePlayer();
      opponent_number_of_moves = s.legalMoves().size();
      s.changePlayer();
    }
    else {
      opponent_number_of_moves = s.legalMoves().size();
      s.changePlayer();
      my_number_of_moves = s.legalMoves().size();
      s.changePlayer();
    }
    totalScore += (my_number_of_moves - opponent_number_of_moves);

    //Corner squares are strategically very good squares, so they are rated 10
    totalScore -= 10*corners[0];
    totalScore += 10*corners[1];

    //"X-squares" are strategically bad squares, so they are rated at 5
    totalScore += 5*XSquares[0];
    totalScore -= 5*XSquares[1];
    return totalScore;
  }


  //If MiniMax gets all the way to the end state, we evaluate a win as infinity,
  //and a loss as negative infinity.
  //A draw is 0
  private int evaluateEnd(GameState s){
    if (s.countTokens()[1] > s.countTokens()[0]) return Integer.MAX_VALUE - 1;
    if (s.countTokens()[1] < s.countTokens()[0]) return Integer.MIN_VALUE + 1;
    else return 0;
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
