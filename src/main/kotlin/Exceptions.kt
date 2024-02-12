class InvalidFenException(message: String) : Exception("Invalid FEN: $message")
class InvalidPGNException : Exception("Invalid PGN")
class InvalidColorException : Exception("Invalid color inputted")
class InvalidPieceTypeException : Exception("Invalid piece type")
class InvalidDimensionException(dimensionType: String) : Exception("Invalid $dimensionType")
class KingNotFoundException : Exception("King not found")
class InvalidMoveException(message: String) : Exception("Invalid move: $message")