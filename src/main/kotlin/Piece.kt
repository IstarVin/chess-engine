enum class PieceType {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN;

    companion object {
        fun determine(charPiece: Char): PieceType =
            when (charPiece.lowercase()) {
                "p" -> PAWN
                "k" -> KING
                "q" -> QUEEN
                "r" -> ROOK
                "b" -> BISHOP
                "n" -> KNIGHT
                else -> throw InvalidPieceTypeException()
            }
    }
}

data class Piece(val pieceType: PieceType, val color: Color) {
    fun isSame(other: Piece): Boolean {
        return this.color == other.color && this.pieceType == other.pieceType
    }

    fun isSameButEnemy(other: Piece): Boolean {
        return this.color != other.color && this.pieceType == other.pieceType
    }

    val char: Char
        get() = when (pieceType) {
            PieceType.KING -> if (color == Color.WHITE) 'K' else 'k'
            PieceType.QUEEN -> if (color == Color.WHITE) 'Q' else 'q'
            PieceType.ROOK -> if (color == Color.WHITE) 'R' else 'r'
            PieceType.BISHOP -> if (color == Color.WHITE) 'B' else 'b'
            PieceType.KNIGHT -> if (color == Color.WHITE) 'N' else 'n'
            PieceType.PAWN -> if (color == Color.WHITE) 'P' else 'p'
        }
}

//enum class Piece(val pieceType: PieceType, val color: Color) {
//    BLACK_KING(PieceType.KING, Color.BLACK),
//    BLACK_QUEEN(PieceType.QUEEN, Color.BLACK),
//    BLACK_ROOK(PieceType.ROOK, Color.BLACK),
//    BLACK_BISHOP(PieceType.BISHOP, Color.BLACK),
//    BLACK_KNIGHT(PieceType.KNIGHT, Color.BLACK),
//    BLACK_PAWN(PieceType.PAWN, Color.BLACK),
//    WHITE_KING(PieceType.KING, Color.WHITE),
//    WHITE_QUEEN(PieceType.QUEEN, Color.WHITE),
//    WHITE_ROOK(PieceType.ROOK, Color.WHITE),
//    WHITE_BISHOP(PieceType.BISHOP, Color.WHITE),
//    WHITE_KNIGHT(PieceType.KNIGHT, Color.WHITE),
//    WHITE_PAWN(PieceType.PAWN, Color.WHITE);
//
//    companion object {
//        fun make(pieceType: PieceType, color: Color): Piece {
//            return when (pieceType) {
//                PieceType.KING -> if (color == Color.WHITE) WHITE_KING else BLACK_KING
//                PieceType.QUEEN -> if (color == Color.WHITE) WHITE_QUEEN else BLACK_QUEEN
//                PieceType.ROOK -> if (color == Color.WHITE) WHITE_ROOK else BLACK_ROOK
//                PieceType.BISHOP -> if (color == Color.WHITE) WHITE_BISHOP else BLACK_BISHOP
//                PieceType.KNIGHT -> if (color == Color.WHITE) WHITE_KNIGHT else BLACK_KNIGHT
//                PieceType.PAWN -> if (color == Color.WHITE) WHITE_PAWN else BLACK_PAWN
//            }
//        }
//    }
//}