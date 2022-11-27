package io.scalac.minesweeper.cli.squared

import io.scalac.minesweeper.api.{Board, Coordinate}
import io.scalac.minesweeper.cli.CoordinateParser
import io.scalac.minesweeper.squared.SquaredCoordinateParser

class CliSquaredCoordinateParser(board: Board) extends CoordinateParser:
  val parser = SquaredCoordinateParser(board)
  override def parse(input: String): Option[Coordinate] =
    parser.parse(input)
