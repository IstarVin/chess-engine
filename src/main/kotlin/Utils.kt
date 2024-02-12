object Utils {
    fun checkPGN(pgn: String): Boolean {
        return !(pgn.length != 2 ||
                pgn[0] !in Constants.BOARD_LETTERS ||
                !pgn[1].isDigit() ||
                pgn[1].digitToInt().notWithinEight())
    }

    fun checkDimension(dimension: Int): Boolean = dimension in 0..7

    fun checkFEN(fen: String): FenCheckResult {
        val fenSplit = fen.split(' ')
        if (fenSplit.size != 6) return FenCheckResult.INVALID_FEN_SIZE

        // Board
        val boardSplit = fenSplit[0].split('/')
        if (boardSplit.size != 8) return FenCheckResult.INVALID_BOARD_SIZE

        for (row in boardSplit) {
            if (row.length > 8 || row.isEmpty()) return FenCheckResult.INVALID_BOARD_SIZE
            var numPieces = 0
            for (piece in row) {
                if (piece.isDigit()) {
                    if (piece.digitToInt().notWithinEight()) return FenCheckResult.INVALID_PIECE
                    numPieces += piece.digitToInt()
                } else {
                    if (piece !in Constants.PIECES_STRING) return FenCheckResult.INVALID_PIECE
                    numPieces += 1
                }
            }
            if (numPieces != 8) return FenCheckResult.INVALID_PIECE
        }

        // Turn
        if (fenSplit[1] !in "wb") return FenCheckResult.INVALID_TURN

        // Castleability
        if (fenSplit[2] != "-") {
            if (fenSplit[2].toList().distinct().size != fenSplit[2].length)
                return FenCheckResult.INVALID_CASTLEABILITY


            for (i in fenSplit[2]) {
                if (i !in "KQkq") return FenCheckResult.INVALID_CASTLEABILITY
            }
        }

        // EnPassant
        val enPassant = fenSplit[3]
        if (!(enPassant.length == 1 && enPassant[0] == '-') && !checkPGN(enPassant))
            return FenCheckResult.INVALID_ENPASSANT

        // Half Moves
        val halfMovesInt = fenSplit[4].toIntOrNull() ?: return FenCheckResult.INVALID_HALF_MOVES
        if (halfMovesInt < 0 || halfMovesInt > 50) return FenCheckResult.INVALID_HALF_MOVES

        // Full Moves
        fenSplit[5].toIntOrNull() ?: return FenCheckResult.INVALID_FULL_MOVES

        return FenCheckResult.OK
    }

    fun pgnMoveDecoder(pgn: String) {
        TODO()
    }

    fun Int.notWithinEight() = this < 1 || this > 8

    fun Array<Coords>.contains(coord: Coords): Boolean {
        for (i in this) {
            if (i.pgn == coord.pgn) return true
        }

        return false
    }

    fun String.toPGN(): Pair<Coords, Coords> {
        this.split(' ').let {
            return Pair(Coords(it[0]), Coords(it[1]))
        }
    }
}
