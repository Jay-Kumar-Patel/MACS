import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FollowChess{

    private List<List<Character>> board;

    private List<Character> whitePieceCaptureOrder;
    private List<Character> blackPieceCaptureOrder;
    private List<Character> tempwhitePieceCaptureOrder;
    private List<Character> tempblackPieceCaptureOrder;

    //This variable ensure that when we call applyMoveSequence first time the first move must be from white piece
    private int applyMoveSequence = 0;

    //This variable ensure that alternate move will be applied.
    private boolean lastMovePieceColor = true;

    private int blackKing = 0;
    private int whiteKing = 0;
    private final char[] blackPieces = {'R', 'N', 'B', 'Q', 'K', 'P'};
    private final char[] whitePieces = {'r', 'n', 'b', 'q', 'k', 'p'};
    private final char[] blankSpace = {'.'};


    /**
     * Constructor which initialize the board and captureOrder list
     */
    public FollowChess(){
        board = new ArrayList<>();
        whitePieceCaptureOrder = new ArrayList<>();
        blackPieceCaptureOrder = new ArrayList<>();
        tempwhitePieceCaptureOrder = new ArrayList<>();
        tempblackPieceCaptureOrder = new ArrayList<>();
    }

    /**
     * takes a board structure as an input
     * @param boardStream
     * @return if board has valid elements or not for example any other input then chess peices. 
     */
    boolean loadBoard( BufferedReader boardStream){
        try {
            if (boardStream == null || !boardStream.ready()) {
                return false;
            }
    
            if (create2DArray(boardStream) && checkValid2DArray()) {
                if(blackKing == 1 && whiteKing == 1){
                    blackKing = 0;
                    whiteKing = 0;
                    return true;
                }
            }
            
            return false;
        } 
        catch (Exception e) {
            return false;
        }
    }

    /**
     * This method takes care of the printing the current chessboard. 
     * @param outstream This is the printWriter Variable.
     * @return It returns true when everything is good or else false.
     */
    boolean printBoard( PrintWriter outstream){

        if (outstream == null) {
            return false;
        }

        if(board == null || board.isEmpty() || board.size() == 0){
            outstream.flush();
            outstream.close();
    
            return true;
        }
    
        try {
            for (List < Character > currRow: board) {
                String currRowString = "";
                for (Character currChar: currRow) {
                    currRowString += currChar;
                }
                outstream.print(currRowString + "\n");
            }
    
            outstream.flush();
            outstream.close();
    
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method takes input as the sequence of the moves and checks different criteria such as follows.
     * It checks if the player doing the moves is checkMate or not. 
     * If not then moves the pieces to the desired input.
     * If every move is applied successfully then the move board is copied to the original board. 
     * 
     * @param moveStream This is the input stream in which sequence is present and passed on to the method. 
     * @return If all sequences are executed correctly then returns true or else false. 
     */
    boolean applyMoveSequence( BufferedReader moveStream ){
        try {
        
            if(board == null || board.isEmpty() || board.size() == 0){
                tempblackPieceCaptureOrder.clear();
                tempwhitePieceCaptureOrder.clear();
                return false;
            }
            
            if (moveStream == null || !moveStream.ready()) {
                tempblackPieceCaptureOrder.clear();
                tempwhitePieceCaptureOrder.clear();
                return false;
            }
    
            List<List<Character>> tempBoard = board;
    
            int boardRowSize = board.size();
            int boardColSize = board.get(0).size();

            boolean isFirstCharacter = true;
            boolean lastPieceColor = true;

            String nextLine;
            while ((nextLine = moveStream.readLine()) != null) {

                if(nextLine == null || nextLine.isEmpty() || nextLine.length() < 7 || nextLine.length() > 7){
                    tempblackPieceCaptureOrder.clear();
                    tempwhitePieceCaptureOrder.clear();
                    return false;
                }

                int startCol = nextLine.charAt(0) - 'a';
                int startRow = boardRowSize - Integer.parseInt(String.valueOf(nextLine.charAt(2)));
                int endCol = nextLine.charAt(4) - 'a';
                int endRow = boardRowSize - Integer.parseInt(String.valueOf(nextLine.charAt(6)));

                if (startCol >= 0 && startCol < boardColSize && startRow >= 0 && startRow < boardRowSize && endCol >= 0 && endCol < boardColSize && endRow >= 0 && endRow < boardRowSize) {
                    Character currCharToMove = board.get(startRow).get(startCol);
                    boolean pieceColor = isWhiteOrBlackPiece(currCharToMove);

                    if(isFirstCharacter){
                        if((applyMoveSequence == 0 && !isWhiteOrBlackPiece(currCharToMove)) || (applyMoveSequence != 0 && (pieceColor == lastMovePieceColor))){
                            tempblackPieceCaptureOrder.clear();
                            tempwhitePieceCaptureOrder.clear();
                            return false;
                        }
                    }

                    int player = -1;
                    if(pieceColor){
                        player = 0;
                    }
                    else{
                        player = 1;
                    }

                    if (currCharToMove != '.') {
                        if(inCheck(player) == false){
                            if(isMovePossible(currCharToMove, startRow, startCol, endRow, endCol)){
                                tempBoard.get(startRow).set(startCol, '.');
                                tempBoard.get(endRow).set(endCol, currCharToMove);

                                if(isFirstCharacter){
                                    isFirstCharacter = false;
                                }
                                lastPieceColor = pieceColor;
                            }
                            else{
                                tempblackPieceCaptureOrder.clear();
                                tempwhitePieceCaptureOrder.clear();
                                return false;
                            }
                        }
                        else{
                            if((player == 0 && currCharToMove.equals('k')) || (player == 1 && currCharToMove.equals('K'))){
                                if(isMovePossible(currCharToMove, startRow, startCol, endRow, endCol)){
                                    tempBoard.get(startRow).set(startCol, '.');
                                    tempBoard.get(endRow).set(endCol, currCharToMove);

                                    if(isFirstCharacter){
                                        isFirstCharacter = false;
                                    }
                                    lastPieceColor = pieceColor;
                                }
                                else{
                                    tempblackPieceCaptureOrder.clear();
                                    tempwhitePieceCaptureOrder.clear();
                                    return false;
                                }
                            }
                            else{
                                tempblackPieceCaptureOrder.clear();
                                tempwhitePieceCaptureOrder.clear();
                                return false;
                            }
                        }
                    } 
                    else {
                        tempblackPieceCaptureOrder.clear();
                        tempwhitePieceCaptureOrder.clear();
                        return false;
                    }
                }
                else{
                    tempblackPieceCaptureOrder.clear();
                    tempwhitePieceCaptureOrder.clear();
                    return false;
                }
            }


            board = tempBoard;
            for (Character order: tempblackPieceCaptureOrder) {
                blackPieceCaptureOrder.add(order);
            }
            for (Character order: tempwhitePieceCaptureOrder) {
                whitePieceCaptureOrder.add(order);
            }
            tempblackPieceCaptureOrder.clear();
            tempwhitePieceCaptureOrder.clear();

            applyMoveSequence++;
            lastMovePieceColor = lastPieceColor;

            return true;
    
        } catch (Exception e) {
            tempblackPieceCaptureOrder.clear();
            tempwhitePieceCaptureOrder.clear();
            return false;
        }
    }

    /**
     * This method takes care of the movement.
     * @param piece The piece which is being moved. 
     * @param startRow The starting row of the piece to map the piece in the board.
     * @param startCol The starting column of the piece to map the piece in the board. 
     * @param endRow The end row is the desired movement of the piece. 
     * @param endCol The end column is the desired mvement of the piece.
     * @return
     */
    private boolean isMovePossible(Character piece, int startRow, int startCol, int endRow, int endCol){

        if(piece == 'P' || piece == 'p'){
            return canPawnMove(piece, startRow, startCol, endRow, endCol, true);
        }
        else if(piece == 'N' || piece == 'n'){
            return canKnightMove(piece, startRow, startCol, endRow, endCol, true);
        }
        else if(piece == 'B' || piece == 'b'){
            return canPieceMoveDiagonally(piece, startRow, startCol, endRow, endCol, true);
        }
        else if(piece == 'R' || piece == 'r'){
            return canPieceMoveHoriVerti(piece, startRow, startCol, endRow, endCol, true);
        }
        else if(piece == 'Q' || piece == 'q'){
            return canPieceMoveHoriVerti(piece, startRow, startCol, endRow, endCol, true) || canPieceMoveDiagonally(piece, startRow, startCol, endRow, endCol, true);
        }
        else if(piece == 'K' || piece == 'k'){
            return canPieceMoveDiagonally(piece, startRow, startCol, endRow, endCol, true) || canPieceMoveHoriVerti(piece, startRow, startCol, endRow, endCol, true);
        }
        else{
            return false;
        }
    }

    /**
     * This method ensures if the knight can move or not in the knight movement according to chess. 
     * @param piece The piece which is the knight. 
     * @param startRow The starting row of the knight.
     * @param startCol The starting column of the knight. 
     * @param endRow The desired row the knight wants to move. 
     * @param endCol The desired column the knight wants to move. 
     * @param canCaptureOrder Takes care of the pieces killed will be recorded or not. 
     * @return If it can move then true and false. 
     */
    private boolean canKnightMove(Character piece, int startRow, int startCol, int endRow, int endCol, boolean canCaptureOrder){

        boolean pieceColor = isWhiteOrBlackPiece(piece);

        if ((endRow == startRow - 2 && endCol == startCol + 1) 
        || (endRow == startRow - 1 && endCol == startCol + 2) 
        || (endRow == startRow + 1 && endCol == startCol + 2) 
        || (endRow == startRow + 2 && endCol == startCol + 1) 
        || (endRow == startRow + 2 && endCol == startCol - 1) 
        || (endRow == startRow + 1 && endCol == startCol - 2) 
        || (endRow == startRow - 1 && endCol == startCol - 2) 
        || (endRow == startRow - 2 && endCol == startCol - 1)) {

            
            Character pieceAtDestination = board.get(endRow).get(endCol);
            boolean pieceColorAtDestination = isWhiteOrBlackPiece(pieceAtDestination);

            if (pieceAtDestination.equals('.')) {
                return true;
            }

            if(pieceColor != pieceColorAtDestination){
                if (canCaptureOrder) {
                    if (pieceColor) {
                        tempwhitePieceCaptureOrder.add(board.get(endRow).get(endCol));
                    } else {
                        tempblackPieceCaptureOrder.add(board.get(endRow).get(endCol));
                    }
                }
                return true;
            }
        }

        return false;
    }

    /**
     * This method ensures if the Pawn can move or not in the pawn movement according to chess. 
     * @param piece The piece which is the pawn. 
     * @param startRow The starting row of the pawn.
     * @param startCol The starting column of the pawn. 
     * @param endRow The desired row the pawn wants to move. 
     * @param endCol The desired column the pawn wants to move. 
     * @param canCaptureOrder Takes care of the pieces killed will be recorded or not. 
     * @return If it can move then true and false. 
     */
    private boolean canPawnMove(Character piece, int startRow, int startCol, int endRow, int endCol, boolean canCaptureOrder){
        boolean pieceColor = isWhiteOrBlackPiece(piece);
        Character pieceAtDestination = board.get(endRow).get(endCol);
        boolean pieceColorAtDestination = isWhiteOrBlackPiece(pieceAtDestination);

        if (pieceColor) {
            if (endRow == startRow - 1 && endCol == startCol && pieceAtDestination.equals('.')){
                return true;
            } 
            
            if((endRow == startRow - 1 && endCol == startCol - 1 && !pieceColorAtDestination) 
            || (endRow == startRow - 1 && endCol == startCol + 1 && !pieceColorAtDestination)) {
                
                if(canCaptureOrder){
                    if (pieceColor) {
                        tempwhitePieceCaptureOrder.add(board.get(endRow).get(endCol));
                    } 
                    else {
                        tempblackPieceCaptureOrder.add(board.get(endRow).get(endCol));
                    }
                }
                return true;
            }
        } else {
            if (endRow == startRow + 1 && endCol == startCol && pieceAtDestination.equals('.')){
                return true;
            }
            
            if ((endRow == startRow + 1 && endCol == startCol - 1 && !pieceColorAtDestination) 
            || (endRow == startRow + 1 && endCol == startCol + 1 && !pieceColorAtDestination)) {
               
                if(canCaptureOrder){
                    if (pieceColor) {
                        tempwhitePieceCaptureOrder.add(board.get(endRow).get(endCol));
                    } 
                    else {
                        tempblackPieceCaptureOrder.add(board.get(endRow).get(endCol));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if the piece can be moved diagonally or not. 
     * @param piece Piece which is being move. 
     * @param startRow The starting row of the piece.
     * @param startCol The starting column of the piece. 
     * @param endRow The desired row the piece wants to move. 
     * @param endCol The desired column the piece wants to move. 
     * @param canCaptureOrder Takes care of the pieces killed will be recorded or not. 
     * @return If it can move then true and false. 
     */
    private boolean canPieceMoveDiagonally(Character piece, int startRow, int startCol, int endRow, int endCol, boolean canCaptureOrder){

        String direction = "";

        if (endCol < startCol && endRow < startRow) {
            direction = "TopLeft";
        } else if (endCol > startCol && endRow < startRow) {
            direction = "TopRight";
        } else if (endCol < startCol && endRow > startRow) {
            direction = "BottomLeft";
        } else if (endCol > startCol && endRow > startRow) {
            direction = "BottomRight";
        } else {
            return false;
        }

        int boardRowSize = board.size();
        int boardColSize = board.get(0).size();

        if(direction.equals("TopLeft") && (startRow-1 < 0 || startCol-1 < 0)){
            return false;
        }
        else if(direction.equals("TopRight") && (startRow-1 < 0 || startCol+1 >= boardColSize)){
            return false;
        }
        else if(direction.equals("BottomLeft") && (startRow+1 >= boardRowSize || startCol-1 < 0)){
            return false;
        }
        else if(direction.equals("BottomRight") && (startRow+1 >= boardRowSize ||  startCol+1 >= boardColSize)){
            return false;
        }
        else{
            //Do Nothing
        }

        int i = -1;
        int j = -1;

        if (!direction.isEmpty()) {
            switch (direction) {
                case "TopLeft":
                    j = startCol - 1;
                    for (i = startRow - 1; i > endRow; i--) {
                        if (i<0 || j<0 || board.get(i).get(j) != '.') {
                            return false;
                        }
                        j--;
                    }
                    break;

                case "TopRight":
                    j = startCol + 1;
                    for (i = startRow - 1; i > endRow; i--) {
                        if (i<0 || j>= boardColSize || board.get(i).get(j) != '.') {
                            return false;
                        }
                        j++;
                    }
                    break;

                case "BottomLeft":
                    j = startCol - 1;
                    for (i = startRow + 1; i < endRow; i++) {
                        if (i>=boardRowSize || j<0 || board.get(i).get(j) != '.') {
                            return false;
                        }
                        j--;
                    }
                    break;

                case "BottomRight":
                    j = startCol + 1;
                    for (i = startRow + 1; i < endRow; i++) {
                        if (i>=boardRowSize || j>= boardColSize || board.get(i).get(j) != '.') {
                            return false;
                        }
                        j++;
                    }
                    break;

                default:
                    return false;
            }

            boolean canContinue = false;

            if(direction.equals("TopLeft") && (startRow-1 == endRow && startCol-1 == endCol)){
                canContinue = true;
            }
            else if(direction.equals("TopRight") && (startRow-1 == endRow && startCol+1 == endCol)){
                canContinue = true;
            }
            else if(direction.equals("BottomLeft") && (startRow+1 == endRow && startCol-1 == endCol)){
                canContinue = true;
            }
            else if(direction.equals("BottomRight") && (startRow+1 == endRow &&  startCol+1 == endCol)){
                canContinue = true;
            }
            else{
                return false;
            }

            if(canContinue){
                if (board.get(endRow).get(endCol) == '.') {
                    return true;
                }
    
                boolean pieceColor = isWhiteOrBlackPiece(piece);
                boolean pieceColorAtDestination = isWhiteOrBlackPiece(board.get(endRow).get(endCol));
    
    
                if (pieceColor != pieceColorAtDestination) {
                    if (canCaptureOrder) {
                        if (pieceColor) {
                            tempwhitePieceCaptureOrder.add(board.get(endRow).get(endCol));
                        } else {
                            tempblackPieceCaptureOrder.add(board.get(endRow).get(endCol));
                        }
                    }
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * This method checks if the piece can be moved horizontally and vertically or not. 
     * @param piece Piece which is being move. 
     * @param startRow The starting row of the piece.
     * @param startCol The starting column of the piece. 
     * @param endRow The desired row the piece wants to move. 
     * @param endCol The desired column the piece wants to move. 
     * @param canCaptureOrder Takes care of the pieces killed will be recorded or not. 
     * @return If it can move then true and false. 
     */
    private boolean canPieceMoveHoriVerti(Character piece, int startRow, int startCol, int endRow, int endCol, boolean canCaptureOrder){

        String direction = "";

        if (endCol == startCol && endRow < startRow) {
            direction = "Top";
        } else if (endCol > startCol && endRow == startRow) {
            direction = "Right";
        } else if (endCol == startCol && endRow > startRow) {
            direction = "Bottom";
        } else if (endCol < startCol && endRow == startRow) {
            direction = "Left";
        } else {
            return false;
        }


        int boardRowSize = board.size();
        int boardColSize = board.get(0).size();

        if(direction.equals("Top") && startRow-1 < 0){
            return false;
        }
        else if(direction.equals("Right") && startCol+1 >= boardColSize){
            return false;
        }
        else if(direction.equals("Bottom") && startRow+1 >= boardRowSize){
            return false;
        }
        else if(direction.equals("Left") && startCol-1 < 0){
            return false;
        }
        else{
            //Do Nothing
        }

        if (!direction.isEmpty()) {
            switch (direction) {
                case "Top":
                    for (int i = startRow - 1; i > endRow; i--) {
                        if (i<0 || board.get(i).get(endCol) != '.') {
                            return false;
                        }
                    }
                    break;

                case "Right":
                    for (int i = startCol + 1; i < endCol; i++) {
                        if (i>=boardColSize || board.get(endRow).get(i) != '.') {
                            return false;
                        }
                    }
                    break;

                case "Bottom":
                    for (int i = startRow + 1; i < endRow; i++) {
                        if (i>=boardRowSize || board.get(i).get(endCol) != '.') {
                            return false;
                        }
                    }
                    break;

                case "Left":
                    for (int i = startCol - 1; i > endCol; i--) {
                        if (i<0 || board.get(endRow).get(i) != '.') {
                            return false;
                        }
                    }
                    break;

                default:
                    return false;
            }

            boolean canContinue = false;

            if(direction.equals("Top") && startRow-1 == endRow){
                canContinue = true;
            }
            else if(direction.equals("Right") && startCol+1 == endCol){
                canContinue = true;
            }
            else if(direction.equals("Bottom") && startRow+1 == endRow){
                canContinue = true;
            }
            else if(direction.equals("Left") && startCol-1 == endCol){
                canContinue = true;
            }
            else{
                return false;
            }


            if(canContinue){
                if (board.get(endRow).get(endCol) == '.') {
                    return true;
                }
    
                boolean pieceColor = isWhiteOrBlackPiece(piece);
                boolean pieceColorAtDestination = isWhiteOrBlackPiece(board.get(endRow).get(endCol));
    
                if (pieceColor != pieceColorAtDestination) {
                    if (canCaptureOrder) {
                        if (pieceColor) {
                            tempwhitePieceCaptureOrder.add(board.get(endRow).get(endCol));
                        } else {
                            tempblackPieceCaptureOrder.add(board.get(endRow).get(endCol));
                        }
                    }
                    return true;
                }
            }
            
        }

        return false;
    }

    

    /**
     * 
     * @param player Player = 0 (White) and Player = 1 (Black)
     * @return list of that respective player.
     */
    List<Character> captureOrder( int player ){
        List<Character> ansList = new ArrayList<>();
        if(player == 0){
            ansList.addAll(whitePieceCaptureOrder);
        }
        else{
            ansList.addAll(blackPieceCaptureOrder);
        }
        return ansList;
    }



    /**
     * 
     * @param player Player = 0 (White) and Player = 1 (Black)
     * @return true if that player's king is check and mate by any of the opponent's piece
     */
    boolean inCheck( int player ){

        if(board == null || board.isEmpty() || board.size() == 0){
            return false;
        }

        if(player == 0){

            int kingRow = -1;
            int kingCol = -1;
            List<Map.Entry<Map.Entry<Integer,Integer>, Character>> opponentPositions = new ArrayList<>();

            for(int i=0;i<board.size();i++){
                for(int j=0;j<board.get(0).size();j++){

                    Character currChar = board.get(i).get(j);

                    if(currChar.equals('k')){
                        kingRow = i;
                        kingCol = j;
                        continue;
                    }

                    boolean currPieceColor = isWhiteOrBlackPiece(board.get(i).get(j));

                    if(!currPieceColor){
                        Map.Entry<Integer, Integer> rowCol = new AbstractMap.SimpleEntry<>(i, j);
                        opponentPositions.add(new  AbstractMap.SimpleEntry<>(rowCol, currChar));
                    }
                }
            }

            boolean ans = false;

            if(kingRow != -1 && kingCol != -1){

                for(Map.Entry<Map.Entry<Integer,Integer>, Character> currEntry : opponentPositions){

                    Map.Entry<Integer, Integer> rowCol = currEntry.getKey();
                    char piece = currEntry.getValue();
                    int startRow = rowCol.getKey();
                    int startCol = rowCol.getValue();

                    switch (piece) {
                        case 'P':
                            ans = ans || canPawnMove(piece,startRow, startCol, kingRow, kingCol, false);
                            break;
                        
                        case 'N':
                            ans = ans || canKnightMove(piece, startRow, startCol, kingRow, kingCol, false);
                            break;

                        case 'B':
                            ans = ans || canPieceMoveDiagonally(piece, startRow, startCol, kingRow, kingCol, false);
                            break;

                        case 'R':
                            ans = ans || canPieceMoveHoriVerti(piece, startRow, startCol, kingRow, kingCol, false);
                            break;

                        case 'Q':
                        case 'K':
                            ans = ans || canPieceMoveDiagonally(piece, startRow, startCol, kingRow, kingCol, false) || canPieceMoveHoriVerti(piece, startRow, startCol, kingRow, kingCol, false);
                            break;
                        default:
                            return false;
                    }
            
                }
            }

            return ans;
        }
        else if(player == 1){

            int kingRow = -1;
            int kingCol = -1;
            List<Map.Entry<Map.Entry<Integer,Integer>, Character>> opponentPositions = new ArrayList<>();

            for(int i=0;i<board.size();i++){
                for(int j=0;j<board.get(0).size();j++){

                    Character currChar = board.get(i).get(j);

                    if(currChar.equals('.')){
                        continue;
                    }

                    if(currChar.equals('K')){
                        kingRow = i;
                        kingCol = j;
                        continue;
                    }

                    boolean currPieceColor = isWhiteOrBlackPiece(board.get(i).get(j));

                    if(currPieceColor){
                        Map.Entry<Integer, Integer> rowCol = new AbstractMap.SimpleEntry<>(i, j);
                        opponentPositions.add(new  AbstractMap.SimpleEntry<>(rowCol, currChar));
                    }
                }
            }

            boolean ans = false;

            if(kingRow != -1 && kingCol != -1){

                for(Map.Entry<Map.Entry<Integer,Integer>, Character> currEntry : opponentPositions){
                
                    Map.Entry<Integer, Integer> rowCol = currEntry.getKey();
                    char piece = currEntry.getValue();
                    int startRow = rowCol.getKey();
                    int startCol = rowCol.getValue();

                    switch (piece) {
                        case 'p':
                            ans = ans || canPawnMove(piece,startRow, startCol, kingRow, kingCol, false);
                            break;
                        
                        case 'n':
                            ans = ans || canKnightMove(piece, startRow, startCol, kingRow, kingCol, false);
                            break;

                        case 'b':
                            ans = ans || canPieceMoveDiagonally(piece, startRow, startCol, kingRow, kingCol, false);
                            break;

                        case 'r':
                            ans = ans || canPieceMoveHoriVerti(piece, startRow, startCol, kingRow, kingCol, false);
                            break;

                        case 'q':
                        case 'k':
                            ans = ans || canPieceMoveDiagonally(piece, startRow, startCol, kingRow, kingCol, false) || canPieceMoveHoriVerti(piece, startRow, startCol, kingRow, kingCol, false);
                            break;
                    
                        default:
                            return false;
                    }
                }
            }

            return ans;
        }
        else{
            return false;
        }
    }



    /**
     * 
     * @param boardPosition It is the input in which start row and start col are mentioned
     * @return true if that piece move atleast one step in any direction.
     */
    boolean pieceCanMove( String boardPosition ){

        if(boardPosition == null || boardPosition.isEmpty() || boardPosition.length() != 3){
            return false;
        }

        int boardRowSize = board.size();
        int boardColSize = board.get(0).size();

        int col = boardPosition.charAt(0) - 'a';
        int row = boardRowSize - Integer.parseInt(String.valueOf(boardPosition.charAt(2)));

        Character piece = board.get(row).get(col);

        if(piece == '.'){
            return false;
        }

        if(piece == 'P'){
            boolean ans = false;
            if(row + 1 < boardRowSize){
                ans =  ans || canPawnMove(piece, row, col, row+1, col, false);
                if(col+1 < boardColSize){
                    ans = ans || canPawnMove(piece, row, col, row+1, col+1, false);
                }
                if(col-1 >= 0){
                    ans = ans || canPawnMove(piece, row, col, row+1, col-1, false);
                }
            }
            return ans;
        }
        else if(piece == 'p'){
            boolean ans = false;
            if(row - 1 >= 0){
                ans =  ans || canPawnMove(piece, row, col, row-1, col, false);
                if(col+1 < boardColSize){
                    ans = ans || canPawnMove(piece, row, col, row-1, col+1, false);
                }
                if(col-1 >= 0){
                    ans = ans || canPawnMove(piece, row, col, row-1, col-1, false);
                }
            }
            return ans;
        }
        else if(piece == 'N' || piece == 'n'){
            boolean ans = false;
            if(row-2 >= 0 && col+1 < boardColSize){
                ans =  ans || canKnightMove(piece, row, col, row-2, col+1, false);
            }
            if(row-1 >=0 && col+2 < boardColSize){
                ans = ans || canKnightMove(piece, row, col, row-1, col+2, false);
            }
            if(row+1 < boardRowSize && col+2 < boardColSize){
                ans = ans || canKnightMove(piece, row, col, row+1, col+2, false);
            }
            if(row+2 < boardRowSize && col+1 < boardColSize){
                ans = ans || canKnightMove(piece, row, col, row+2, col+1, false);
            }
            if(row+2 < boardRowSize && col-1 >= 0){
                ans = ans || canKnightMove(piece, row, col, row+2, col-1, false);
            }
            if(row+1 < boardRowSize && col-2 >= 0){
                ans = ans || canKnightMove(piece, row, col, row+1, col-2, false);
            }
            if(row-1 >= 0 && col-2 >= 0){
                ans = ans || canKnightMove(piece, row, col, row-1, col-2, false);
            }
            if(row-2 >= 0 && col-1 >= 0){
                ans = ans || canKnightMove(piece, row, col, row-2, col-1, false);
            }
            return ans;
        }
        else if(piece == 'B' || piece == 'b'){
            boolean ans = false;
            if(row-1 >= 0 && col+1 < boardColSize){
                ans =  ans || canPieceMoveDiagonally(piece, row, col, row-1, col+1, false);
            }
            if(row+1 < boardRowSize && col+1 < boardColSize){
                ans =  ans || canPieceMoveDiagonally(piece, row, col, row+1, col+1, false);
            }
            if(row+1 < boardRowSize && col-1 >= 0){
                ans =  ans || canPieceMoveDiagonally(piece, row, col, row+1, col-1, false);
            }
            if(row-1 >= 0 && col-1 >= 0){
                ans =  ans || canPieceMoveDiagonally(piece, row, col, row-1, col-1, false);
            }
            return ans;
        }
        else if(piece == 'R' || piece == 'r'){
            boolean ans = false;
            if(row-1 >= 0){
                ans =  ans || canPieceMoveHoriVerti(piece, row, col, row-1, col, false);
            }
            if(col+1 < boardColSize){
                ans =  ans || canPieceMoveHoriVerti(piece, row, col, row, col+1, false);
            }
            if(row+1 < boardRowSize){
                ans =  ans || canPieceMoveHoriVerti(piece, row, col, row+1, col, false);
            }
            if(col-1 >= 0){
                ans =  ans || canPieceMoveHoriVerti(piece, row, col, row, col-1, false);
            }
            return ans;
        }
        else if(piece == 'Q' || piece == 'q' || piece == 'K' || piece == 'k'){
            boolean ans = false;
            if(row-1 >= 0 && col+1 < boardColSize){
                ans =  ans || canPieceMoveDiagonally(piece, row, col, row-1, col+1, false);
            }
            if(row+1 < boardRowSize && col+1 < boardColSize){
                ans =  ans || canPieceMoveDiagonally(piece, row, col, row+1, col+1, false);
            }
            if(row+1 < boardRowSize && col-1 >= 0){
                ans =  ans || canPieceMoveDiagonally(piece, row, col, row+1, col-1, false);
            }
            if(row-1 >= 0 && col-1 >= 0){
                ans =  ans || canPieceMoveDiagonally(piece, row, col, row-1, col-1, false);
            }
            if(row-1 >= 0){
                ans =  ans || canPieceMoveHoriVerti(piece, row, col, row-1, col, false);
            }
            if(col+1 < boardColSize){
                ans =  ans || canPieceMoveHoriVerti(piece, row, col, row, col+1, false);
            }
            if(row+1 < boardRowSize){
                ans =  ans || canPieceMoveHoriVerti(piece, row, col, row+1, col, false);
            }
            if(col-1 >= 0){
                ans =  ans || canPieceMoveHoriVerti(piece, row, col, row, col-1, false);
            }
            return ans;
        }
        else{
            return false;
        }
    }


    /**
     * 
     * @param piece It is the piece which we want to check that it is white or black colored piece.
     * @return true if white piece, else return false for black piece.
     */
    private boolean isWhiteOrBlackPiece(Character piece){
        //true = White Piece and false = Black Piece
        if(Character.isUpperCase(piece)){
            return false;
        }
        else{
            return true;
        }
    }


    /**
     * This function is used to check that each row of that 2D list must be same
     * @return true if all row had same length else return false.
     */
    private boolean checkValid2DArray(){
        int validRowSize = -1;

        for (int i = 0; i < board.size(); i++) {
            if (board.get(i).size() > 1) {

                if (validRowSize == -1) {
                    validRowSize = board.get(i).size();
                } else {
                    if (validRowSize != board.get(i).size()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


    /**
     * 
     * @param boardStream It is the input from which we create our 2D Character list
     * @return true if all the pieces present in the input are valid
     */
    private boolean create2DArray(BufferedReader boardStream)
    {
        try {
            String nextLine;
            while ((nextLine = boardStream.readLine()) != null) {
    
                if (nextLine.equals("")) {
                    continue;
                }
    
                ArrayList<Character> currLine = new ArrayList<>();
                for (int i = 0; i < nextLine.length(); i++) {
    
                    Character currChar = nextLine.charAt(i);
    
                    if (!checkValidPieces(currChar)) {
                        return false;
                    }
    
                    currLine.add(currChar);
    
                    if(currChar.equals('K')){
                        blackKing++;
                    }
                    
                    if(currChar.equals('k')){
                        whiteKing++;
                    }
                }
                board.add(currLine);
            }
            return true;
        } 
        catch (IOException io) {
            return false;
        }
    }


    /**
     * This function used to check piece is valid or not. All characters must be from black and white pieces.
     * @param piece It is the piece which is to be checked
     * @return
     */
    private boolean checkValidPieces(Character piece){

        boolean isValid = false;

        for (int i = 0; i < blackPieces.length; i++) {
            if (piece == blackPieces[i]) {
                return true;
            }
        }

        for (int i = 0; i < whitePieces.length; i++) {
            if (piece == whitePieces[i]) {
                return true;
            }
        }

        if (piece == blankSpace[0]) {
            return true;
        }

        return isValid;
    }

}