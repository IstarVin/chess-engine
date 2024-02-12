class GameState(fen: String = Constants.STARTING_FEN) {
    val board: Board
    private var turn: Color
    val castleability: Castleability
    var enPassant: Coords?
    var halfMoves: Int
    var fullMoves: Int

    init {
        val checkFenResult = Utils.checkFEN(fen)
        if (checkFenResult != FenCheckResult.OK) throw InvalidFenException(checkFenResult.toString())

        // Decode FEN
        val splitFen = fen.split(' ')

        board = Board(splitFen[0])
        turn = Color.make(splitFen[1][0])
        castleability = Castleability(splitFen[2])
        enPassant = if (splitFen[3] != "-") Coords(splitFen[3]) else null
        halfMoves = splitFen[4].toInt()
        fullMoves = splitFen[5].toInt()
    }

    fun isCurrentKingChecked(): Boolean {
        return isKingChecked(turn)
    }

    fun isKingChecked(color: Color): Boolean {
        val kingCoords = board.findKing(color)

        for (pieceType in PieceType.entries) {
            if (pieceType == PieceType.KING) continue

            val piece = Piece(pieceType, color)
            val moveCalculator = MoveCalculator(this, kingCoords, piece)
            when (pieceType) {
                PieceType.PAWN -> moveCalculator.pawn()
                PieceType.KNIGHT -> moveCalculator.knight()
                PieceType.BISHOP -> moveCalculator.bishop()
                PieceType.ROOK -> moveCalculator.rook()
                PieceType.QUEEN -> moveCalculator.queen()
                else -> throw InvalidPieceTypeException()
            }

            for (move in moveCalculator.partialMoves()) {
                board.getPiece(move)?.let {
                    if (piece.isSameButEnemy(it)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun noValidMoves(color: Color): Boolean {
        for (ally in board.findAllPieceWithColor(color)) {
            val moveCalculator = MoveCalculator(this, ally)
            if (moveCalculator.validMoves().isNotEmpty()) return false
        }
        return true
    }

    fun getFen(): String {
        return "${board.toFen} ${turn.toFen} ${castleability.toFen} " +
                "${enPassant?.pgn ?: "-"} $halfMoves $fullMoves"
    }

    fun copy(): GameState {
        return GameState(getFen())
    }

    fun nextTurn() {
        turn = turn.toEnemy()
    }

    val currentTurn: Color
        get() = turn

}