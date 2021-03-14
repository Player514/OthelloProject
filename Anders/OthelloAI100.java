import java.util.ArrayList;


public class OthelloAI100 implements IOthelloAI{


  private int max_depth = 6;


	public Position decideMove(GameState s){


    //We need to know who we are so we know what to maximize
    int me = s.getPlayerInTurn();

    //System.out.println(me);

    //System.out.println("----------------------------------------------");
    //printBoard(s);

		ArrayList<Position> moves = actions(s);

		if ( !moves.isEmpty() ){

			int max_score = Integer.MIN_VALUE;
      Position best_position = new Position(-1,-1);

      //We iterate through our available moves and find the one with the highest
      //utility as evaluated by the minimax algorithm
      for (Position a : moves){
        int candidate_score = minValue(result(s, a), Integer.MIN_VALUE, Integer.MAX_VALUE, 1, me);
        if (candidate_score>max_score){
          max_score = candidate_score;
          best_position = a;
        }
      }
      //System.out.printf("%d %d\n", best_position.col, best_position.row);
      //System.out.println(max_score);
      return best_position;
    }
		else
			return new Position(-1,-1);
	}

  //Renaming legalmoves to actions to stay consistent with the book terminology
  private ArrayList<Position> actions(GameState s){
    return s.legalMoves();
  }

  //Simulates one move called place from GameState s
  private GameState result(GameState s, Position place){
    GameState s1 = new GameState(s.getBoard(), s.getPlayerInTurn());
    s1.insertToken(place);
    return s1;
  }

  //Tests if we have searched sufficiently deeply by using a fixed depth cutoff
  private boolean cutoffTest(int depth){
    if (depth >= max_depth) return true;
    else return false;
  }

  // Returns the MAX value of a move
  private int maxValue(GameState s, int alpha, int beta, int depth, int me){
    //If the game has finished, we use one evaluation function. If not, we use another
    if (s.isFinished()) return evaluateEnd(s, me);
    if (cutoffTest(depth)) return evaluate(s, me);
    int v = Integer.MIN_VALUE;

    //Alpha beta pruning of game tree
    for (Position a : actions(s)){
      v = Math.max(v, minValue(result(s, a), alpha, beta, depth + 1, me));
      if (v >= beta) return v;
      alpha = Math.max(alpha, v);
    }
    return v;
  }

  //Returns the MIN value of a move
  private int minValue(GameState s, int alpha, int beta, int depth, int me){
    if (s.isFinished()) return evaluateEnd(s, me);
    if (cutoffTest(depth)) return evaluate(s, me);
    int v = Integer.MAX_VALUE;


    for (Position a : actions(s)){
      v = Math.min(v, maxValue(result(s, a), alpha, beta, depth + 1, me));
      if (v <= alpha) return v;
      beta = Math.min(beta, v);
    }
    return v;
  }



  //This function counts the number of corner squares per player. This is a
  //very good place to place a token
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
  //4 squares diagonally adjacent to the corner squares. You don't want to put a token here
  private int[] countXSquares(GameState s){
    int[] XSquares = {0,0};
    int[][] board = s.getBoard();
    if (board[1][1] != 0) XSquares[board[1][1] - 1] += 1;
    if (board[1][6] != 0) XSquares[board[1][6] - 1] += 1;
    if (board[6][1] != 0) XSquares[board[6][1] - 1] += 1;
    if (board[6][6] != 0) XSquares[board[6][6] - 1] += 1;
    return XSquares;
  }


  //This is the evaluation function for the case where minimax did not reach the
  //end of the game. It is a linear combination of number of X squares per player,
  //number of corner squares per player and number of available moves per player
  private int evaluate(GameState s, int me){
    int my_number_of_moves;
    int opponent_number_of_moves;
    int[] corners = countCorners(s);
    int[] XSquares = countXSquares(s);
    int totalScore = 0;

    //We find the number of available moves per player
    if (s.getPlayerInTurn() == me){
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

    //Corner squares are strategically very good squares, so they are rated 10,
    //i.e. 1 corner square is as good as 10 available moves
    if (me == 2){
      totalScore -= 10*corners[0];
      totalScore += 10*corners[1];
    }
    else{
      totalScore += 10*corners[0];
      totalScore -= 10*corners[1];
    }


    //"X-squares" are strategically bad squares, so they are rated -5
    if (me == 2){
      totalScore += 5*XSquares[0];
      totalScore -= 5*XSquares[1];
    }
    else{
      totalScore -= 5*XSquares[0];
      totalScore += 5*XSquares[1];
    }

    return totalScore;
  }


  //If MiniMax gets all the way to the end state, we evaluate a win as infinity,
  //and a loss as negative infinity. A draw is 0
  private int evaluateEnd(GameState s, int me){
    if (me == 2){
      if (s.countTokens()[1] > s.countTokens()[0]) return Integer.MAX_VALUE - 1;
      if (s.countTokens()[1] < s.countTokens()[0]) return Integer.MIN_VALUE + 1;
      else return 0;
    }
    else{
      if (s.countTokens()[0] > s.countTokens()[1]) return Integer.MAX_VALUE - 1;
      if (s.countTokens()[0] < s.countTokens()[1]) return Integer.MIN_VALUE + 1;
      else return 0;
    }

  }

  //Used for debugging during development
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
