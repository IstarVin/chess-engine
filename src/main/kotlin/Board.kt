class Board(boardFen: String = Constants.BOARD_STARTING_FEN) {
    private val boardTable = Array(Constants.ROW_SIZE) { Array<Char?>(Constants.COL_SIZE) { null } }

    init {
        // Decode Board FEN
        val boardFenSplit = boardFen.split('/')

        for ((row, rowContent) in boardFenSplit.withIndex()) {
            var column = 0
            rowContent.forEach { piece ->
                if (piece.isDigit()) {
                    column += piece.digitToInt()
                } else {
                    boardTable[row][column++] = piece
                }
            }
        }
    }

    fun move(from: Coords, to: Coords) {
        val piece = getPiece(from) ?: throw InvalidMoveException("No piece at $from")
        boardTable[from.row][from.col] = null
        boardTable[to.row][to.col] = piece.char
    }

    fun getPiece(from: Coords): Piece? {
        val charPiece = boardTable[from.row][from.col] ?: return null

        val color = if (charPiece.isUpperCase()) Color.make('w') else Color.make('b')
        val pieceType = PieceType.determine(charPiece)

        return Piece(pieceType, color)
    }

    fun isGridEmpty(from: Coords): Boolean {
        return getPiece(from) == null
    }

    fun isEnemy(from: Coords, color: Color): Boolean {
        val piece = getPiece(from)
        return if (piece == null) false else piece.color != color
    }

    fun findKing(color: Color): Coords {
        for (row in boardTable.indices) {
            for (col in boardTable[row].indices) {
                val piece = getPiece(Coords(row, col)) ?: continue
                if (piece.pieceType == PieceType.KING && piece.color == color) return Coords(
                    row,
                    col
                )
            }
        }
        throw KingNotFoundException()
    }

    fun findAllPieceWithColor(color: Color): Array<Coords> {
        val coords = mutableListOf<Coords>()
        for (row in boardTable.indices) {
            for (col in boardTable[row].indices) {
                val piece = getPiece(Coords(row, col)) ?: continue
                if (piece.color == color) coords += Coords(row, col)
            }
        }
        return coords.toTypedArray()
    }

    fun showBoard(): String {
        // show board with labels
        var board = ""
        for ((row, rowContent) in boardTable.withIndex()) {
            board += "${8 - row} "
            for (cell in rowContent) {
                board += cell ?: "-"
                board += " "
            }
            board += "\n"
        }
        board += "  a b c d e f g h"
        return board
    }

    fun deletePiece(coords: Coords) {
        boardTable[coords.row][coords.col] = null
    }

    val toFen: String
        get() = boardTable.joinToString(separator = "/") { row ->
            var emptyCount = 0
            row.joinToString(separator = "") { cell ->
                if (cell == null) {
                    emptyCount++
                    ""
                } else {
                    if (emptyCount > 0) {
                        val emptyString = emptyCount.toString()
                        emptyCount = 0
                        emptyString + cell.toString()
                    } else {
                        cell.toString()
                    }
                }
            } + if (emptyCount > 0) emptyCount.toString() else ""
        }
}