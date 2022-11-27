package io.scalac.minesweeper.cli.squared

import io.scalac.minesweeper.cli.MinesweeperCLI
import io.scalac.minesweeper.squared.SquaredBoardFactory

object Main
    extends MinesweeperCLI(
      SquaredBoardFactory,
      CliSquaredCoordinateParserFactory
    )
