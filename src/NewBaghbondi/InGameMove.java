package NewBaghbondi;

import javafx.scene.Group;
import javafx.stage.Stage;

public class InGameMove {

    Turn turn = new Turn(Turn.TurnType.GOAT_TURN);
    Position[][] board;
    Group pieceGroup;
    Stage boardStage;
    GameOverWorks gameOverWorks;
    InGameMove(Position[][] board, Group pieceGroup, Stage boardStage) {
        this.board = board;
        this.pieceGroup = pieceGroup;
        this.boardStage = boardStage;
        gameOverWorks = new GameOverWorks(boardStage, board);
    }

    public boolean makeMove(Piece piece) {
        System.out.println("-----Called makePiece() on mouse-----");
        int newHorizontal = pixelToBoard(piece.getLayoutX());
        int oldHorizontal = pixelToBoard(piece.getOldHorizontal());
        int newVertical = pixelToBoard(piece.getLayoutY());
        int oldVertical = pixelToBoard(piece.getOldVertical());
        System.out.println("Turn of :: " + turn.getTurn());
        MoveResult result = tryMove(piece, newHorizontal, newVertical);

        if (turn.getTurn() == Turn.TurnType.GOAT_TURN) {
            if (piece.getPieceType() == PieceTypeEnum.TIGER) {
                piece.abortMove();
                return false;
            }

        } else if (turn.getTurn() == Turn.TurnType.TIGER_TURN) {
            if (piece.getPieceType() == PieceTypeEnum.GOAT) {
                piece.abortMove();
                return false;
            }

        }

        // if (endTigerGame(piece)) boardStage.setScene(gameOverScene(false));
        if (gameOverWorks.goatWinCase(piece)) return false;

        boolean movementMade = false;

        switch (result.getType()) {
            case NONE:
                System.out.println("MoveResult : None ");
                //if (endTigerGame(piece)) boardStage.setScene(gameOverScene(false));
                piece.abortMove();
                break;
            //break
            case NORMAL:
                System.out.println("MoveResult : Normal ");
                piece.move(newHorizontal, newVertical);
                System.out.println("Normal move from:: " + oldHorizontal + ", " + oldVertical);
                board[oldHorizontal][oldVertical].setPiece(null);
                board[newHorizontal][newVertical].setPiece(piece);
                System.out.println("Normal move: preturn " + turn.getTurn());
                //    if (endTigerGame(piece)) boardStage.setScene(gameOverScene(false));
                turn.changeTurn();
                //  turnTy = (turnTy+1)%2;
                System.out.println("Normal move: turn " + turn.getTurn());
                movementMade = true;
                break;
            case KILL:
                System.out.println("MoveResult : Kill ");
                piece.move(newHorizontal, newVertical);
                board[oldHorizontal][oldVertical].setPiece(null);
                board[newHorizontal][newVertical].setPiece(piece);
                Piece killedPiece = result.getPiece();
                board[pixelToBoard(killedPiece.getOldHorizontal())][pixelToBoard(killedPiece.getOldVertical())].setPiece(null);
                pieceGroup.getChildren().remove(killedPiece);
                //    if (endTigerGame(piece)) boardStage.setScene(gameOverScene(false));
                turn.changeTurn();
                gameOverWorks.killGoat();
                //turnTy = (turnTy+1)%2;
                movementMade = true;
                break;
        }

        if (gameOverWorks.tigerWinCase()) return false;
        return movementMade;
    }

    private int pixelToBoard(double pixel) {
        return (int) pixel / (StageCreator.positionSize * 2);
    }

    private MoveResult tryMove(Piece piece, int newHorizontal, int newVertical) {

        if (newVertical > 4 || newHorizontal > 4) {
            System.out.println("Out of board selection");
            return new MoveResult((MoveType.NONE));
        }
        if ((board[newHorizontal][newVertical] == null)) {
            System.out.println("null Board for" + newHorizontal + ", " + newVertical);
            return new MoveResult(MoveType.NONE);
        }
        if (board[newHorizontal][newVertical].hasPiece()) {
            // System.out.println(endTigerGame(piece));

            System.out.println("HAS PIECE, NO MOVE" + newHorizontal + ", " + newVertical);
            return new MoveResult(MoveType.NONE);
        }
        int oldHorizontal = pixelToBoard(piece.getOldHorizontal());
        int oldVertical = pixelToBoard(piece.getOldVertical());
        System.out.println("Try move to(" + newHorizontal + ", " + newVertical + ") from (" + oldHorizontal + "," + oldVertical + ")");
        System.out.println("New Hz - Old Hz = " + (newHorizontal - oldHorizontal) + "\nNew Vr - Old Vr = " + (newVertical - oldVertical));

        double line = Math.sqrt((newVertical - oldVertical) * (newVertical - oldVertical) + (newHorizontal - oldHorizontal) * (newHorizontal - oldHorizontal));
        double value = Math.sqrt(2);
        double killValue = 2 * Math.sqrt(2);

        if (oldVertical == 0 || oldVertical == 4) {
            if (oldHorizontal == 0 || oldHorizontal == 4) {
                if (line == 2.0 || line == value) {
                    //  System.out.println(endTigerGame(piece));
                    //  if (endTigerGame(piece)) boardStage.setScene(gameOverScene(false));
                    return new MoveResult(MoveType.NORMAL);
                }
            } else if (oldHorizontal == 2) {
                if ((Math.abs(newHorizontal - oldHorizontal) == 2.0) || line == 1.0) {
                    //    System.out.println(endTigerGame(piece));
                    //      if (endTigerGame(piece)) boardStage.setScene(gameOverScene(false));
                    return new MoveResult(MoveType.NORMAL);
                }
            }
        } else {
            if (line == 1.0) {
                // System.out.println(endTigerGame(piece));
                // if (endTigerGame(piece)) boardStage.setScene(gameOverScene(false));
                return new MoveResult(MoveType.NORMAL);
            } else if (line == value) {
                if ((oldVertical == 2) && (oldHorizontal == 2)) {
                    //   System.out.println(endTigerGame(piece));
                    //    if (endTigerGame(piece)) boardStage.setScene(gameOverScene(false));
                    return new MoveResult(MoveType.NORMAL);
                } else if ((oldVertical == 1) || (oldVertical == 3)) {
                    if ((oldHorizontal == 1) || (oldHorizontal == 3)) {
                        if ((newHorizontal == 2) && (newVertical == 2)) {
                            //            System.out.println(endTigerGame(piece));
                            //            if (endTigerGame(piece)) boardStage.setScene(gameOverScene(false));
                            return new MoveResult(MoveType.NORMAL);
                        } else if (newHorizontal == 0 || newHorizontal == 4) {
                            //           System.out.println(endTigerGame(piece));
                            //           if (endTigerGame(piece)) boardStage.setScene(gameOverScene(false));
                            return new MoveResult(MoveType.NORMAL);
                        }
                    }
                }
            }
        }

        ///System.out.println(piece.getPieceTypeEnum());
        if (piece.getPieceTypeEnum() == PieceTypeEnum.TIGER) {
            if (oldVertical == 0 || oldVertical == 4) {
                if (line == 4) {
                    int killedX = oldHorizontal + (newHorizontal - oldHorizontal) / 2;
                    int killedY = oldVertical + (newVertical - oldVertical) / 2;
                    ///System.out.println("Killed");

                    if (board[killedX][killedY].hasPiece()) {
                        return new MoveResult(MoveType.KILL, board[killedX][killedY].getPiece());
                    }
                }
            }
            if (line == 2 || line == killValue) {
                int killedX = oldHorizontal + (newHorizontal - oldHorizontal) / 2;
                int killedY = oldVertical + (newVertical - oldVertical) / 2;
                ///System.out.println("Killed");

                if (board[killedX][killedY].hasPiece()) {
                    return new MoveResult(MoveType.KILL, board[killedX][killedY].getPiece());
                }
            } else {
                System.out.println("Goat can't kill");
            }
        }
        return new MoveResult(MoveType.NONE);
    }
}