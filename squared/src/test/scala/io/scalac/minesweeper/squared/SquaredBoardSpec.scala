package io.scalac.minesweeper.squared

import io.scalac.minesweeper.api.BoardSpec

class SquaredBoardSpec extends BoardSpec(SquaredBoardFactory()):
  test("Should contain all coordinates") {
    val board = new SquaredBoard(9, _ => false)
    val coordinates = for {
      x <- 0 until 3
      y <- 0 until 3
    } yield SquaredCoordinate(x, y)

    board.allCoordinates.foreach(c => assert(coordinates.contains(c)))
    coordinates.foreach(c => assert(board.allCoordinates.contains(c)))
  }
