enum class Color {
    BLACK, WHITE;

    companion object {
        fun make(color: Char): Color {
            return when (color) {
                'w' -> WHITE
                'b' -> BLACK
                else -> throw InvalidColorException()
            }
        }
    }

    val toFen: Char
        get() = when (this) {
            WHITE -> 'w'
            BLACK -> 'b'
        }

    fun toEnemy(): Color {
        return when (this) {
            WHITE -> BLACK
            BLACK -> WHITE
        }
    }
}
