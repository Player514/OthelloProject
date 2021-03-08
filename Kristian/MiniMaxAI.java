import java.util.ArrayList;

/**
 * A Simple AI that evaluates the next possible move, 
 * by counting the number of resulting tokens in each color.
 * @author Kristian Noergaard Jensen
 * @version 02.03.2021
 */

public class MiniMaxAI implements IOthelloAI {
    public Position decideMove(GameState s) {
        ArrayList<Position> legalMoves = s.legalMoves();
        
        ArrayList<Pair<Double, Position>> moves = new ArrayList<Pair<Double, Position>>();
        legalMoves.parallelStream().forEach(move -> {
            GameState s_prime = new GameState(s.getBoard(), s.getPlayerInTurn());
            boolean result = s_prime.insertToken(move);
            if (result) {
                double val = MinValue(s_prime, 10, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                System.out.println(val);
                Pair<Double, Position> v = new Pair<Double, Position>(val, move);
                moves.add(v);
            } else {
                System.out.println("Help here!");
            }
        });
        Position bestMove = null;
        Double v = Double.MIN_VALUE;
        for (Pair<Double, Position> p : moves) {
            if (p.getFst() > v) {
                bestMove = p.getSnd();
                v = p.getFst();
            } 
        }
        return bestMove;
    }

    private Double MinValue(GameState s, Integer depth, Double alpha, Double beta) {
        // System.out.println("Calling Min");
        if (s.isFinished() || depth == 0) {
            return Utility(s);
        }

        double v = 10;
        ArrayList<Position> moves = s.legalMoves();
        for (Position move : moves) {
            GameState s_prime = new GameState(s.getBoard(), s.getPlayerInTurn());
            boolean result = s_prime.insertToken(move);
            if (result) {
                v = Math.min(v, MaxValue(s_prime, depth - 1, alpha, beta));
                beta = Math.min(v, beta);
                if (beta <= alpha) break;
            } else {
                System.out.println("What the hell??");
            }
        }
        return v;
    }

    private Double MaxValue(GameState s, Integer depth, Double alpha, Double beta) {
        // System.out.println("Calling Max");
        if (s.isFinished() || depth == 0) {
            return Utility(s);
        }

        double v = -10;
        ArrayList<Position> moves = s.legalMoves();
        for (Position move : moves) {
            GameState s_prime = new GameState(s.getBoard(), s.getPlayerInTurn());
            boolean result = s_prime.insertToken(move);
            if (result) {
                v = Math.max(v, MinValue(s_prime, depth - 1, alpha, beta));
                alpha = Math.max(v, alpha);
                if (alpha >= beta) break;
            } else {
                System.out.println("What the hell??");
            }
        }
        return v;
    }

    private Double Utility(GameState s) {
        // int[] tokens = s.countTokens();
        // if (tokens[0] > tokens[1]) {
        //     return 1.0;
        // } else if (tokens[1] > tokens[0]) {
        //     return -1.0;
        // } else {
        //     return 0.0;
        // }
        int[] tokens = s.countTokens();
        double ratio = (double) tokens[1]/tokens[0];
        return ratio;
    }
}


class Pair<T, S> {
    T fst;
    S snd;
  
    public Pair(T fst, S snd) {
      this.fst = fst;
      this.snd = snd;
    }
  
    T getFst() { return fst; }
  
    S getSnd() { return snd; }
  
    public String toString() { return "(" + getFst() + "," + getSnd() + ")"; }
  }