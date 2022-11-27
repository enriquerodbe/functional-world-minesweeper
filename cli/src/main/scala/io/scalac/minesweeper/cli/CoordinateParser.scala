package io.scalac.minesweeper.cli

import io.scalac.minesweeper.api.Coordinate

trait CoordinateParser:
  def parse(input: String): Option[Coordinate]
