package io.scalac.minesweeper.squared

import io.scalac.minesweeper.api.{Board, Coordinate}
import scala.util.matching.Regex

object SquaredCoordinateParser:
  val regex: Regex = """(\d+) (\d+)""".r

  def parse(input: String, board: Board): Option[Coordinate] =
    input match
      case regex(x, y) =>
        for
          validatedX <- x.toIntOption
          validatedY <- y.toIntOption
          coordinate <- SquaredCoordinate.validated(
            validatedX,
            validatedY,
            board.size
          )
        yield coordinate
      case _ =>
        None
