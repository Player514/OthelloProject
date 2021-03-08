import java.util.ArrayList;

/**
 * A Simple AI that evaluates the next possible move, 
 * by counting the number of resulting tokens in each color.
 * @author Kristian Noergaard Jensen
 * @version 23.2.2021
 */
public class SimpleAI implements IOthelloAI {
    
    public Position decideMove(GameState s) {
        double bestRatio = Double.NEGATIVE_INFINITY;
        Position bestPosition = null;
        ArrayList<Position> legalMoves = s.legalMoves();
        for (Position move : legalMoves) {
            GameState s_prime = new GameState(s.getBoard(), s.getPlayerInTurn());
            boolean result = s_prime.insertToken(move);
            if (result) {
                if (s_prime.isFinished()) {
                    return move;
                } else {
                    int[] tokens = s_prime.countTokens();
                    double ratio = (double) tokens[s.getPlayerInTurn()]/tokens[(1-s.getPlayerInTurn())];
                    if (ratio > bestRatio) {
                        bestRatio = ratio;
                        bestPosition = move;
                    }
                }
            } else {
                System.out.println("What the hell happened here?!?!");
            }
        }
        return bestPosition;
    }
} 