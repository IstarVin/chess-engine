import Utils.contains

class ChessEngine(
    private var gameState: GameState,
) {
    constructor(fen: String = Constants.STARTING_FEN) : this(GameState(fen))

    val history = History()

    fun move(from: Coords, to: Coords): MoveResult {
        if (gameState.board.isGridEmpty(from) ||
            gameState.board.isEnemy(from, gameState.currentTurn)
        ) return MoveResult.INVALID_MOVE

        val moves = calculateMoves(from)
        val piece = gameState.board.getPiece(from)!!

        if (!moves.contains(to)) return MoveResult.INVALID_MOVE

        history.add(gameState.getFen())

        if (gameState.board.isEnemy(to, gameState.currentTurn)) gameState.halfMoves = 0

        gameState.board.move(from, to)

        when (piece.pieceType) {
            PieceType.KING -> {
                if (from.col == 4 &&
                    (from.row == 0 || from.row == 7) &&
                    (to.col == 2 || to.col == 6)
                ) {
                    val fromCastledRookCol = if (to.col == 2) 0 else 7
                    val toCastleRookCol = if (to.col == 2) 3 else 5

                    gameState.board.move(
                        Coords(from.row, fromCastledRookCol),
                        Coords(from.row, toCastleRookCol)
                    )
                }
                when (piece.color) {
                    Color.WHITE -> {
                        gameState.castleability.whiteQueen = false
                        gameState.castleability.whiteKing = false
                    }

                    Color.BLACK -> {
                        gameState.castleability.blackQueen = false
                        gameState.castleability.blackKing = false
                    }
                }
            }

            PieceType.ROOK -> {
                if (gameState.castleability.blackQueen && from.col == 0 && from.row == 0) {
                    gameState.castleability.blackQueen = false
                } else if (gameState.castleability.blackKing && from.col == 7 && from.row == 0) {
                    gameState.castleability.blackKing = false
                } else if (gameState.castleability.whiteQueen && from.col == 0 && from.row == 7) {
                    gameState.castleability.whiteQueen = false
                } else if (gameState.castleability.whiteKing && from.col == 7 && from.row == 7) {
                    gameState.castleability.whiteKing = false
                }
            }
            // En Passant
            PieceType.PAWN -> {
                if (to.pgn == gameState.enPassant?.pgn) {
                    gameState.board.deletePiece(Coords(from.row, to.col))
                }

                gameState.enPassant = null

                if ((to.row == 3 || to.row == 4) && (from.row == 1 || from.row == 6)) {
                    val rowEnPassant = if (from.row == 1) 2 else 5
                    gameState.enPassant = Coords(rowEnPassant, from.col)
                }
                gameState.halfMoves = 0
            }

            else -> Unit
        }

        if (piece.color == Color.BLACK) gameState.fullMoves++

        gameState.halfMoves++

        if (gameState.halfMoves == 100) return MoveResult.DRAW

        gameState.nextTurn()

        if (gameState.isCurrentKingChecked()) {
            return if (gameState.noValidMoves(gameState.currentTurn))
                if (gameState.currentTurn == Color.WHITE)
                    MoveResult.BLACK_WIN else MoveResult.WHITE_WIN
            else {
                if (gameState.currentTurn == Color.WHITE)
                    MoveResult.WHITE_CHECKED else MoveResult.BLACK_CHECKED
            }
        } else {
            if (gameState.noValidMoves(gameState.currentTurn))
                return MoveResult.DRAW
        }

        return MoveResult.OK
    }

    fun move(from: String, to: String): MoveResult {
        if (!(Utils.checkPGN(from) || Utils.checkPGN(to))) return MoveResult.INVALID_MOVE

        return move(Coords(from), Coords(to))
    }

    fun undo() {
        history.undo(gameState.getFen())?.let { gameState = GameState(it) }
    }

    fun redo() {
        history.redo()?.let { gameState = GameState(it) }
    }

    val historyList: List<String>
        get() = history.historyList

    private fun calculateMoves(
        from: Coords,
    ): Array<Coords> {
        return MoveCalculator(gameState, from).validMoves()
    }

    fun calculateMovesP(from: Coords): Array<Coords> {
        return calculateMoves(from)
    }

    fun currentTurn(): Color {
        return gameState.currentTurn
    }

    fun showBoard(): String {
        return gameState.board.showBoard()
    }
}