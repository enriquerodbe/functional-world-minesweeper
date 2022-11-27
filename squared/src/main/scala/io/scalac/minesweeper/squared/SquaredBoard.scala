package io.scalac.minesweeper.squared

import io.scalac.minesweeper.api.*

class SquaredBoard extends Board:
  override def uncover(coordinate: Coordinate): Board =
    new SquaredBoard:
      override def stateAt(_coordinate: Coordinate): Coordinate.State =
        if SquaredBoard.this.stateAt(_coordinate) == Coordinate.State.Flagged
        then Coordinate.State.Flagged
        else Coordinate.State.Uncovered

      override def state: Board.State =
        if hasMine(coordinate) then
          if stateAt(coordinate) == Coordinate.State.Flagged
          then SquaredBoard.this.state
          else Board.State.Lost
        else Board.State.Won

  override def flag(coordinate: Coordinate): Board =
    new SquaredBoard:
      override def stateAt(_coordinate: Coordinate): Coordinate.State =
        Coordinate.State.Flagged

  override def allCoordinates: Seq[Coordinate] =
    Seq.tabulate(5)(SquaredCoordinate(_, 0))

  override def hasMine(coordinate: Coordinate): Boolean =
    coordinate.hashCode() % 2 == 0

  override def stateAt(coordinate: Coordinate): Coordinate.State =
    Coordinate.State.Covered

  override def state: Board.State =
    Board.State.Playing
