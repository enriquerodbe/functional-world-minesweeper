package io.scalac.minesweeper.squared

import io.scalac.minesweeper.api.*

class SquaredBoard(size: Int, _hasMine: Coordinate => Boolean) extends Board:
  previous =>

  override def uncover(coordinate: Coordinate): Board =
    new SquaredBoard(size, _hasMine):
      override def stateAt(_coordinate: Coordinate): Coordinate.State =
        if previous.stateAt(_coordinate) == Coordinate.State.Flagged
        then Coordinate.State.Flagged
        else Coordinate.State.Uncovered

      override def state: Board.State =
        if hasMine(coordinate) then
          if stateAt(coordinate) == Coordinate.State.Flagged
          then previous.state
          else Board.State.Lost
        else if won()
        then Board.State.Won
        else Board.State.Playing

  private def won(): Boolean =
    allCoordinates
      .filter(!hasMine(_))
      .forall(stateAt(_) == Coordinate.State.Uncovered)

  override def flag(coordinate: Coordinate): Board =
    new SquaredBoard(size, _hasMine):
      override def stateAt(_coordinate: Coordinate): Coordinate.State =
        if _coordinate == coordinate
        then Coordinate.State.Flagged
        else previous.stateAt(_coordinate)

  override lazy val allCoordinates: Seq[SquaredCoordinate] =
    val sqrt = math.sqrt(size.toDouble).round.toInt
    (0 until size).map(n => SquaredCoordinate(n / sqrt, n % sqrt))

  override def hasMine(coordinate: Coordinate): Boolean =
    _hasMine(coordinate)

  override def stateAt(coordinate: Coordinate): Coordinate.State =
    Coordinate.State.Covered

  override def state: Board.State =
    Board.State.Playing
