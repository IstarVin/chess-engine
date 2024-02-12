class Castleability(castleabilityFen: String) {
    var blackKing: Boolean = false
    var blackQueen: Boolean = false
    var whiteKing: Boolean = false
    var whiteQueen: Boolean = false

    init {
        for (c in castleabilityFen) {
            when (c) {
                'K' -> whiteKing = true
                'Q' -> whiteQueen = true
                'k' -> blackKing = true
                'q' -> blackQueen = true
            }
        }
    }

    val toFen: String
        get() {
            var result = ""
            if (whiteKing) result += "K"
            if (whiteQueen) result += "Q"
            if (blackKing) result += "k"
            if (blackQueen) result += "q"
            return if (result == "") "-" else result
        }
}