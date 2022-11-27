package io.scalac.minesweeper.squared

import io.scalac.minesweeper.api.*

class SquaredBoard(override val size: Int, _hasMine: Coordinate => Boolean)
    extends Board:
  previous =>

  override def uncover(coordinate: Coordinate): Board =
    new SquaredBoard(size, _hasMine):
      override def stateAt(_coordinate: Coordinate): Coordinate.State =
        val previousState = previous.stateAt(_coordinate)
        if _coordinate == coordinate then previousState.uncover
        else previousState

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
          .filterNot(hasMine)
          .forall(stateAt(_) == Coordinate.State.Uncovered)

  override def flag(coordinate: Coordinate): Board =
    new SquaredBoard(size, _hasMine):
      override def stateAt(_coordinate: Coordinate): Coordinate.State =
        val previousState = previous.stateAt(_coordinate)
        if _coordinate == coordinate then previousState.flag
        else previousState

  override lazy val allCoordinates: Seq[SquaredCoordinate] =
    val maxIndex = SquaredCoordinate.maxIndex(size) + 1
    (0 until size).map(n => SquaredCoordinate(n / maxIndex, n % maxIndex))

  override def hasMine(coordinate: Coordinate): Boolean =
    _hasMine(coordinate)

  override def stateAt(coordinate: Coordinate): Coordinate.State =
    Coordinate.State.Covered

  override def state: Board.State =
    Board.State.Playing

  override val show: String =
    allCoordinates
      .groupBy(_.x)
      .toSeq
      .sortBy(_._1)
      .map(_._2)
      .map(_.sortBy(_.y))
      .map(_.map(showCoordinate))
      .map(_.mkString(" | "))
      .mkString("\n")

  private def showCoordinate(coordinate: SquaredCoordinate): String =
    stateAt(coordinate) match
      case Coordinate.State.Covered => "+"
      case Coordinate.State.Uncovered =>
        neighbors(coordinate).count(hasMine).toString
      case Coordinate.State.Flagged => "F"

  def neighbors(coordinate: SquaredCoordinate): Seq[SquaredCoordinate] =
    Seq(
      SquaredCoordinate.validated(coordinate.x - 1, coordinate.y - 1, size),
      SquaredCoordinate.validated(coordinate.x - 1, coordinate.y, size),
      SquaredCoordinate.validated(coordinate.x - 1, coordinate.y + 1, size),
      SquaredCoordinate.validated(coordinate.x, coordinate.y - 1, size),
      SquaredCoordinate.validated(coordinate.x, coordinate.y + 1, size),
      SquaredCoordinate.validated(coordinate.x + 1, coordinate.y - 1, size),
      SquaredCoordinate.validated(coordinate.x + 1, coordinate.y, size),
      SquaredCoordinate.validated(coordinate.x + 1, coordinate.y + 1, size)
    ).flatten
