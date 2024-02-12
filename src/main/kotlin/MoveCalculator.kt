import kotlin.math.absoluteValue

class MoveCalculator(
    private val gameState: GameState,
    private val from: Coords,
    dummyPiece: Piece? = null
) {
    private val moves = mutableListOf<Coords>()
    private val piece = dummyPiece ?: gameState.board.getPiece(from)!!

    fun validMoves(): Array<Coords> {
        when (gameState.board.getPiece(from)!!.pieceType) {
            PieceType.KING -> king()
            PieceType.QUEEN -> queen()
            PieceType.ROOK -> rook()
            PieceType.BISHOP -> bishop()
            PieceType.KNIGHT -> knight()
            PieceType.PAWN -> pawn()
        }

        return validateMoves()
    }

    private fun validateMoves(): Array<Coords> {
        val validMoves = mutableListOf<Coords>()
        for (move in moves) {
            val dummyGameState = gameState.copy()
            dummyGameState.board.move(from, move)
            if (!dummyGameState.isKingChecked(piece.color))
                validMoves += move
        }
        return validMoves.toTypedArray()
    }

    fun partialMoves(): Array<Coords> {
        return moves.toTypedArray()
    }

    fun pawn() {
        val direction = if (piece.color == Color.WHITE) -1 else 1

        Coords.make(from.row + direction, from.col)?.addMove()

        if (from.row == if (piece.color == Color.WHITE) 6 else 1) {
            // Double Forward
            Coords.make(from.row + direction * 2, from.col)?.addMove()
        }

        val attackLeft = Coords.make(from.row + direction, from.col + 1)
        val attackRight = Coords.make(from.row + direction, from.col - 1)

        attackLeft?.addAttack()
        attackRight?.addAttack()

        gameState.enPassant?.let {
            if (attackRight?.pgn == it.pgn) moves.add(attackRight)
            if (attackLeft?.pgn == it.pgn) moves.add(attackLeft)
        }
    }

    fun knight() {
        val row = from.row
        val col = from.col

        val rowSteps = arrayOf(-2, -1, 1, 2)
        val colSteps = arrayOf(-2, -1, 1, 2)

        for (i in rowSteps) {
            for (j in colSteps) {
                if (i.absoluteValue == j.absoluteValue) continue

                Coords.make(row + i, col + j)?.addMoveAttack()
            }
        }
    }

    fun bishop(steps: Int = 7) {
        val rowSteps = arrayOf(-1, 1, -1, 1)
        val colSteps = arrayOf(-1, 1, 1, -1)
        calculateLinearMoves(from, piece.color, rowSteps, colSteps, steps)
    }

    fun rook(steps: Int = 7) {
        val rowSteps = arrayOf(-1, 0, 1, 0)
        val colSteps = arrayOf(0, 1, 0, -1)
        calculateLinearMoves(from, piece.color, rowSteps, colSteps, steps)
    }

    fun queen(steps: Int = 7) {
        bishop(steps)
        rook(steps)
    }

    fun king() {
        val row = if (piece.color == Color.WHITE) 7 else 0

        queen(1)

        val kingCoords = gameState.board.findKing(piece.color)

        if (kingCoords.col == 4 && kingCoords.row == row) {
            if (isKingSideCastleAvailable(row)) {
                moves.add(Coords(row, 6))
            }
            if (isQueenSideCastleAvailable(row)) {
                moves.add(Coords(row, 2))
            }
        }
    }

    private fun isKingSideCastleAvailable(row: Int): Boolean {
        val kingCastleability =
            if (piece.color == Color.WHITE) gameState.castleability.whiteKing
            else gameState.castleability.blackKing
        return isColRangeEmpty(5, 6, row) && kingCastleability
    }

    private fun isQueenSideCastleAvailable(row: Int): Boolean {
        val queenCastleability =
            if (piece.color == Color.WHITE) gameState.castleability.whiteQueen
            else gameState.castleability.blackQueen
        return isColRangeEmpty(1, 3, row) && queenCastleability
    }

    private fun isColRangeEmpty(fromCol: Int, toCol: Int, row: Int): Boolean {
        for (col in fromCol..toCol) {
            if (!gameState.board.isGridEmpty(Coords(row, col))) return false
        }

        return true
    }

    private fun Coords.addMove() {
        if (gameState.board.isGridEmpty(this)) moves.add(this)
    }

    private fun Coords.addAttack() {
        if (gameState.board.isEnemy(this, piece.color)) moves.add(this)
    }

    private fun Coords.addMoveAttack() {
        this.addAttack()
        this.addMove()
    }

    private fun calculateLinearMoves(
        from: Coords,
        pieceColor: Color,
        rowSteps: Array<Int>,
        colSteps: Array<Int>,
        steps: Int = 7
    ) {
        val row = from.row
        val col = from.col

        for (i in rowSteps.indices) {
            for (step in 1..steps) {
                val moveRow = row + rowSteps[i] * step
                val moveCol = col + colSteps[i] * step
                if (!Utils.checkDimension(moveRow) || !Utils.checkDimension(moveCol)) break

                val move = Coords(moveRow, moveCol)
                if (gameState.board.isGridEmpty(move)) {
                    moves.add(move)
                } else if (gameState.board.isEnemy(move, pieceColor)) {
                    moves.add(move)
                    break
                } else {
                    break
                }
            }
        }
    }
}