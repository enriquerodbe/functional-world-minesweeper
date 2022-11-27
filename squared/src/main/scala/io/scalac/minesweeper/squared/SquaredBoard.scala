package io.scalac.minesweeper.squared

import io.scalac.minesweeper.api.*

class SquaredBoard extends Board:
  override def uncover(coordinate: Coordinate): Board =
    new SquaredBoard:
      override def stateAt(_coordinate: Coordinate): Coordinate.State =
        Coordinate.State.Uncovered

      override def state: Board.State =
        Board.State.Won

  override def flag(coordinate: Coordinate): Board =
    new SquaredBoard:
      override def stateAt(_coordinate: Coordinate): Coordinate.State =
        Coordinate.State.Flagged

  override def allCoordinates: Seq[Coordinate] =
    Seq(SquaredCoordinate(0, 0))

  override def hasMine(coordinate: Coordinate): Boolean =
    false

  override def stateAt(coordinate: Coordinate): Coordinate.State =
    Coordinate.State.Covered

  override def state: Board.State =
    Board.State.Playing
