package io.scalac.minesweeper.squared

import io.scalac.minesweeper.api.Coordinate

final case class SquaredCoordinate(x: Int, y: Int) extends Coordinate

object SquaredCoordinate:
  def validated(x: Int, y: Int, boardSize: Int): Option[SquaredCoordinate] =
    val max = SquaredCoordinate.maxIndex(boardSize)
    if (x >= 0 && x <= max && y >= 0 && y <= max) Some(SquaredCoordinate(x, y))
    else None

  def maxIndex(boardSize: Int): Int =
    math.sqrt(boardSize.toDouble).round.toInt - 1
