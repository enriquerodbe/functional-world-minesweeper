package io.scalac.minesweeper.cli.squared

import io.scalac.minesweeper.api.Board
import io.scalac.minesweeper.cli.{CoordinateParser, CoordinateParserFactory}

object CliSquaredCoordinateParserFactory extends CoordinateParserFactory:
  override def create(board: Board): CoordinateParser =
    CliSquaredCoordinateParser(board)
