import Utils.toPGN

fun main() {
    val engine = ChessEngine("rnbqk2r/ppppp1pp/8/4Pp2/8/8/PPPP1PPP/RNBQK2R w KQkq f6 0 1")

    while (true) {
        println("\n${engine.currentTurn()} to move\n")
        println(engine.showBoard())
        print("Enter move: ")
        val moveString = readlnOrNull() ?: continue

        val (from, to) = moveString.toPGN()

        val result = engine.move(from, to)

        println(result)

        if (result == MoveResult.WHITE_WIN || result == MoveResult.BLACK_WIN) {
            println("Game over: $result")
            break
        }
    }

//    val engine = ChessEngine()
//    println(engine.showBoard())
}