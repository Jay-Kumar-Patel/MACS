import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;


public class A1 {
    public static void main(String[] args) {

        BufferedReader bufferedReader = null;

        try {
            FollowChess followChess = new FollowChess();
            followChess.loadBoard(new BufferedReader(new StringReader("RNBQKBNR\nPPPPPPPP\n........\n........\n........\n........\npppppppp\nrnbqkbnr")));
            followChess.applyMoveSequence(new BufferedReader(new StringReader("e 2 e 3\ng 8 f 6\nd 2 d 3\ne 7 e 6\ne 3 e 4\ne 6 e 5")));
            followChess.captureOrder(0);
            followChess.captureOrder(1);

        } catch (Exception e) {
            return;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                return;
            }
        }
    }
}
