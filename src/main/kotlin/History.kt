class History {
    val historyList = mutableListOf<String>()
    private var currentIndex = -1

    fun add(fen: String) {
        if (currentIndex != historyList.size - 1 && fen != historyList[currentIndex + 1]) {
            for (i in currentIndex + 1..<historyList.size)
                historyList.removeAt(i)
        }

        currentIndex++
        historyList.add(fen)
    }

    fun getCurrentIndex(): String {
        return historyList[currentIndex]
    }

    fun undo(currentFen: String, step: Int = 1): String? {
        if (currentIndex == historyList.size - 1) currentFen.let {
            historyList.add(it)
            currentIndex += 1
        }

        return historyList.getOrNull(currentIndex - step)?.also { currentIndex-- }
//            .also { println("$historyList: $currentIndex") }
    }

    fun redo(step: Int = 1) =
        historyList.getOrNull(currentIndex + step)?.also { currentIndex++ }
}