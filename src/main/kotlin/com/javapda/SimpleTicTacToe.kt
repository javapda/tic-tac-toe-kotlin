package com.javapda

import kotlin.math.abs

@SuppressWarnings("StatementWithEmptyBody")
class SimpleTicTacToe {
    private val gridWidth = 3
    private var gameData = "_".repeat(9)

    private fun playerWins(player: String, data: String = gameData): List<List<Int>> {
        require(isValidPlayer(player))
        val winningSequence = player.repeat(3)
        val result = mutableListOf<List<Int>>()

        // horizontals
        if (data.substring(0, 3) == winningSequence) result += listOf(0, 1, 2)
        if (data.substring(3, 6) == winningSequence) result += listOf(3, 4, 5)
        if (data.substring(6) == winningSequence) result += listOf(6, 7, 8)

        // verticals
        if (data.slice(listOf(0, 3, 6)) == winningSequence) result += listOf(0, 3, 6)
        if (data.slice(listOf(1, 4, 7)) == winningSequence) result += listOf(1, 4, 7)
        if (data.slice(listOf(2, 5, 8)) == winningSequence) result += listOf(2, 5, 8)

        // diagonals
        if (data.slice(listOf(0, 4, 8)) == winningSequence) result += listOf(0, 4, 8)
        if (data.slice(listOf(2, 4, 6)) == winningSequence) result += listOf(2, 4, 6)
        return result
    }

    private fun isValidPlayer(player: String): Boolean = player in "XO"

    private fun showState(data: String = gameData): String {
        val xCount = data.count { it == 'X' }
        val oCount = data.count { it == 'O' }
        val underscoreCount = data.count { it == '_' }
        val xWins = playerWins("X", data = data)
        val oWins = playerWins("O", data = data)
        if (false)
            println(
                """
            xCount:      $xCount
            oCount:      $oCount
            abs(diff):   ${abs(xCount - oCount)}
            data:        $data
        """.trimIndent()
            )
        return when {
            abs(xCount - oCount) > 1 || xWins.size > 1 || oWins.size > 1 || (xWins.size == 1 && oWins.size == 1) -> "Impossible"
            xWins.size == 1 -> "X wins"
            oWins.size == 1 -> "O wins"
            underscoreCount == 0 -> "Draw"
            else -> "Game not finished"
        }

    }

    private fun setCell(row: Int = 0, column: Int = 0, xOrO: String = "_", data: String = gameData): String {
        if (xOrO !in "XO_") throw IllegalArgumentException("bad string '$xOrO', should be one of X, O, _")
        val cellIndex = row * gridWidth + column // 0-8
        val newData = StringBuilder(data).replace(cellIndex, cellIndex + 1, xOrO).toString()
        if (false)
            println(
                """
            setCell ************
            row:         $row
            column:      $column
            cellIndex:   $cellIndex
            data:        $data
            gameData:    $gameData
            newData:     $newData
        """.trimIndent()
            )
        return newData
    }

    fun printGameGrid() {
        val (line1, line2, line3) = Regex("([OX_]{3})([OX_]{3})([OX_]{3})").find(gameData)!!.destructured
        println(
            """
            ---------
            | ${line1.toList().joinToString(" ")} |
            | ${line2.toList().joinToString(" ")} |
            | ${line3.toList().joinToString(" ")} |
            ---------
        """.trimIndent()
        )

    }

    fun newGame() {
        gameData = "_________"
        printGameGrid()
        var currentUser = "X"
        while (gameStillGoing()) {

            while (!userMove(currentUser)) {
                /* do nothing */
            }
            currentUser = if (currentUser == "X") "O" else "X"
        }
        println(showState())

    }

    private fun gameStillGoing(): Boolean = showState() == "Game not finished"

//    fun readUserInput() {
//        val inputData = readln() // "O_OXXO_XX"
//        val (line1, line2, line3) =
//            Regex("([OX_]{3})([OX_]{3})([OX_]{3})").find(inputData)!!.destructured
//        gameData = "$line1$line2$line3"
//        printGameGrid()
//    }

    fun test() {
        testIsValidPlayer()
        testPlayerWins()
        testShowState()
        testIsValidPlayerGridIndex()
        testCellValue()
        testIsCellOccupied()
        testSetCellData()
    }

    private fun testSetCellData() {
        fun req(expected: String, actual: String) {
            require(expected == actual) { "testSetCellData: expected=$expected, but found $actual" }
        }

        req("X_X_O_X__", setCell(3 - 1, 1 - 1, "X", data = "X_X_O____"))
        req("XXXOO_OX_", setCell(1 - 1, 1 - 1, "X", data = "_XXOO_OX_"))
        req("_XXOO_OXX", setCell(3 - 1, 3 - 1, "X", data = "_XXOO_OX_"))
        req("_XXOOXOX_", setCell(2 - 1, 3 - 1, "X", data = "_XXOO_OX_"))
    }

    private fun testCellValue() {
        require(cellValue(0, 0, "X________") == 'X')
        require(cellValue(0, 1, "_X_______") == 'X')
        require(cellValue(2, 2, "_X______O") == 'O')
    }


    private fun testIsCellOccupied() {
        require(isCellOccupied(0, 0, "XX_______"))
        require(isCellOccupied(0, 1, "_O_______"))
        require(isCellOccupied(1, 1, "____X____"))
    }

    private fun testIsValidPlayerGridIndex() {
        require(isValidPlayerGridIndex(1))
        require(isValidPlayerGridIndex(2))
        require(isValidPlayerGridIndex(3))
        require(!isValidPlayerGridIndex(0))
        require(!isValidPlayerGridIndex(4))
    }

    private fun testShowState() {
        fun req(expected: String, actual: String) {
            require(expected == actual) { "testShowState: expected=$expected, but found $actual" }
        }
        req("X wins", showState("XXXOO____"))
        req("X wins", showState("XXXOO__O_"))
        req("O wins", showState("XOOOXOXXO"))
        req("Draw", showState("XOXOOXXXO"))
        req("Game not finished", showState("XO_OOX_X_"))
        req("Impossible", showState("XO_XO_XOX"))
        req("Impossible", showState("_O_X__X_X"))
        req("Impossible", showState("_OOOO_X_X"))
    }

    private fun testPlayerWins() {
        // horizontal wins
        require(playerWins("X", "XXX______").size == 1)
        require(playerWins("X", "___XXX___").size == 1)
        require(playerWins("X", "______XXX").size == 1)
        // vertical wins
        require(playerWins("X", "X__X__X__").size == 1)
        require(playerWins("X", "_X__X__X_").size == 1)
        require(playerWins("X", "__X__X__X").size == 1)
        // diagonal wins
        require(playerWins("X", "X___X___X").size == 1)
        require(playerWins("X", "__X_X_X__").size == 1)

        // impossible
        require(playerWins("X", "XX_XX_XX_").size == 2)
        require(playerWins("O", "OXOOXOOXO").size == 2)
    }

    private fun testIsValidPlayer() {
        require(isValidPlayer("X"))
        require(isValidPlayer("O"))
        require(!isValidPlayer("_"))
        require(!isValidPlayer(" "))
    }

//    fun printState() {
//        println(showState())
//    }

    class InvalidPlayerGridIndexException() : Exception("Coordinates should be from 1 to 3!")
    class CellAlreadyOccupiedException() : Exception("This cell is occupied! Choose another one!")

    fun userMove(xOrO: String): Boolean {
        return try {
            print("> ")
            val (x, y) = readln().split("\\s+".toRegex()).map(String::toInt)
            if (!isValidPlayerGridIndex(x) || !isValidPlayerGridIndex(y)) throw InvalidPlayerGridIndexException()
            if (isCellOccupied(x - 1, y - 1)) throw CellAlreadyOccupiedException()
            if (false)
                println(
                    """
            x:               $x
            y:               $y
            valid x:         ${isValidPlayerGridIndex(x)}
            valid y:         ${isValidPlayerGridIndex(y)}
            isCellOccupied:  ${isCellOccupied(x - 1, y - 1)}
            cellValue:       ${cellValue(x - 1, y - 1)}
        """.trimIndent()
                )
            gameData = setCell(x - 1, y - 1, xOrO)
            printGameGrid()
            true
        } catch (err: NumberFormatException) {
            println("You should enter numbers!")
            false
        } catch (err: Exception) {
            println(err.message)
            false
        }

    }

    private fun isValidPlayerGridIndex(num: Int): Boolean = num in 1..3
    private fun cellValue(x: Int, y: Int, data: String = gameData): Char = data[x * 3 + y]
    private fun isCellOccupied(x: Int, y: Int, data: String = gameData): Boolean = data[x * 3 + y] in "XO"

}

fun main() {
    with(SimpleTicTacToe()) {
        test()
        newGame()
    }
}