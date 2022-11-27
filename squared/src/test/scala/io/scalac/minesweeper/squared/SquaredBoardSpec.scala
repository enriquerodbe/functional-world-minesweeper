package io.scalac.minesweeper.squared

import io.scalac.minesweeper.api.{Board, BoardSpec, Coordinate}
import munit.FunSuite

class SquaredBoardSpec extends BoardSpec(SquaredBoardFactory):
  test("Should contain all coordinates") {
    val board = SquaredBoard(9, _ => false)
    val coordinates = for
      x <- 0 until 3
      y <- 0 until 3
    yield SquaredCoordinate(x, y)

    board.allCoordinates.foreach(c => assert(coordinates.contains(c)))
    coordinates.foreach(c => assert(board.allCoordinates.contains(c)))
  }

  test("Should show correctly") {
    val size: Int = 9

    def hasMine(coordinate: Coordinate): Boolean =
      coordinate match
        case SquaredCoordinate(x, _) if x == 0 => true
        case _                                 => false

    val toUncover =
      Seq.tabulate(3)(SquaredCoordinate(1, _))
    val toFlag =
      Seq.tabulate(3)(SquaredCoordinate(0, _))

    val initialBoard: Board = SquaredBoard(size, hasMine)

    val uncovered =
      toUncover.foldLeft(initialBoard)(_ uncover _)

    val flagged =
      toFlag.foldLeft(uncovered)(_ flag _)

    val expected =
      """F | F | F
        |2 | 3 | 2
        |+ | + | +
        |""".stripMargin

    assertNoDiff(flagged.show, expected)
  }

  private val boardSize = 9
  private val lastIndex = SquaredCoordinate.maxIndex(boardSize)
  private val extremes = Seq(0, lastIndex)
  private val middle = 1 until lastIndex
  private val board = SquaredBoard(boardSize, _ => false)

  test("Corners should have 3 neighbors") {
    product(extremes, extremes)
      .map(board.neighbors(_).size)
      .foreach(assertEquals(_, 3))
  }

  test("Borders should have 5 neighbors") {
    (product(middle, extremes) ++ product(extremes, middle))
      .map(board.neighbors(_).size)
      .foreach(assertEquals(_, 5))
  }

  test("Internal should have 8 neighbors") {
    product(middle, middle)
      .map(board.neighbors(_).size)
      .foreach(assertEquals(_, 8))
  }

  private def product(xs: Seq[Int], ys: Seq[Int]): Seq[SquaredCoordinate] =
    for
      x <- xs
      y <- ys
    yield SquaredCoordinate(x, y)
