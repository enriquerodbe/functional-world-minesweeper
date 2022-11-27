package io.scalac.minesweeper.cli.squared

import io.scalac.minesweeper.cli.MovementParser
import io.scalac.minesweeper.squared.SquaredCoordinateParser

object SquaredMovementParser
    extends MovementParser(SquaredCoordinateParser.parse(_, _))
