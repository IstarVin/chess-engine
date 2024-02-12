class Coords {
    val row: Int
    val col: Int
    val pgn: String

    constructor(pgn: String) {
        if (!Utils.checkPGN(pgn)) throw InvalidPGNException()

        this.pgn = pgn
        row = 8 - pgn[1].digitToInt()
        col = pgn[0].minus('a')
    }

    constructor(row: Int, col: Int) {
        if (!Utils.checkDimension(row)) throw InvalidDimensionException("row: $row")
        if (!Utils.checkDimension(col)) throw InvalidDimensionException("column: $col")

        this.row = row
        this.col = col

        val letter = Constants.BOARD_LETTERS[col]
        val number = 8 - row

        this.pgn = "$letter$number"
    }

    companion object {
        fun make(row: Int, col: Int): Coords? {
            if (!Utils.checkDimension(row) || !Utils.checkDimension(col)) return null
            return Coords(row, col)
        }
    }
}