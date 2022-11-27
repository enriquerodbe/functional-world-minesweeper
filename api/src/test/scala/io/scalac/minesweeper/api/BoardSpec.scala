package io.scalac.minesweeper.api

import munit.FunSuite

abstract class BoardSpec(factory: BoardFactory) extends FunSuite:
  test("All coordinates should start covered") {
    val board = createBoard()

    board.allCoordinates
      .map(board.stateAt)
      .foreach(assertEquals(_, Coordinate.State.Covered))
  }

  test("Flagging should set coordinate as flagged") {
    val board = createBoard()

    board.allCoordinates
      .map(coordinate => board.flag(coordinate).stateAt(coordinate))
      .foreach(assertEquals(_, Coordinate.State.Flagged))
  }

  test("Flagging one coordinate should not affect other coordinates") {
    val board = createBoard()

    board.allCoordinates
      .map(coordinate => (coordinate, board.flag(coordinate)))
      .foreach { case (coordinate, updatedBoard) =>
        updatedBoard.allCoordinates
          .filterNot(_ == coordinate)
          .foreach { c =>
            assertEquals(updatedBoard.stateAt(c), board.stateAt(c))
          }
      }
  }

  test("Uncovering any empty coordinate should set coordinate as uncovered") {
    val board = createBoard()

    coordinatesWithoutMine(board)
      .map(coordinate => board.uncover(coordinate).stateAt(coordinate))
      .foreach(assertEquals(_, Coordinate.State.Uncovered))
  }

  test("Uncovering some empty coordinate should keep state as Playing") {
    val board = createBoard()

    coordinatesWithoutMine(board)
      .map(coordinate => board.uncover(coordinate).state)
      .foreach(assertEquals(_, Board.State.Playing))
  }

  test("Board state should start as Playing") {
    val board = createBoard()

    assertEquals(board.state, Board.State.Playing)
  }

  test("Uncovering any coordinate which has mine should lose") {
    val board = createBoard()

    coordinatesWithMine(board)
      .map(coordinate => board.uncover(coordinate).state)
      .foreach(assertEquals(_, Board.State.Lost))
  }

  test("Uncovering all empty coordinates should win") {
    val board = createBoard()

    val finalState =
      coordinatesWithoutMine(board)
        .foldLeft(board)(_ uncover _)
        .state

    assertEquals(finalState, Board.State.Won)
  }

  test("Uncovering flagged mine should not lose") {
    val board = createBoard()

    coordinatesWithMine(board)
      .map(coordinate => board.flag(coordinate).uncover(coordinate).state)
      .foreach(assertEquals(_, Board.State.Playing))
  }

  private def createBoard(): Board =
    factory.create(25, _.hashCode() % 2 == 0)

  private def coordinatesWithMine(board: Board): Seq[Coordinate] =
    filterCoordinates(board, board.hasMine)

  private def coordinatesWithoutMine(board: Board): Seq[Coordinate] =
    filterCoordinates(board, !board.hasMine(_))

  private def filterCoordinates(
      board: Board,
      predicate: Coordinate => Boolean
  ): Seq[Coordinate] =
    board.allCoordinates.filter(predicate)
