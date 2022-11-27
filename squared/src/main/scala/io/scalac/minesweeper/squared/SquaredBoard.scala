package io.scalac.minesweeper.squared

import io.scalac.minesweeper.api.*

class SquaredBoard(size: Int, _hasMine: Coordinate => Boolean) extends Board:
  override def uncover(coordinate: Coordinate): Board =
    new SquaredBoard(size, _hasMine):
      override def stateAt(_coordinate: Coordinate): Coordinate.State =
        if SquaredBoard.this.stateAt(_coordinate) == Coordinate.State.Flagged
        then Coordinate.State.Flagged
        else Coordinate.State.Uncovered

      override def state: Board.State =
        if hasMine(coordinate) then
          if stateAt(coordinate) == Coordinate.State.Flagged
          then SquaredBoard.this.state
          else Board.State.Lost
        else if (won()) Board.State.Won
        else Board.State.Playing

  private def won(): Boolean =
    allCoordinates
      .filter(!hasMine(_))
      .forall(stateAt(_) == Coordinate.State.Uncovered)

  override def flag(coordinate: Coordinate): Board =
    new SquaredBoard(size, _hasMine):
      override def stateAt(_coordinate: Coordinate): Coordinate.State =
        if (_coordinate == coordinate) Coordinate.State.Flagged
        else SquaredBoard.this.stateAt(_coordinate)

  override def allCoordinates: Seq[Coordinate] =
    Seq.tabulate(size)(SquaredCoordinate(_, 0))

  override def hasMine(coordinate: Coordinate): Boolean =
    _hasMine(coordinate)

  override def stateAt(coordinate: Coordinate): Coordinate.State =
    Coordinate.State.Covered

  override def state: Board.State =
    Board.State.Playing
