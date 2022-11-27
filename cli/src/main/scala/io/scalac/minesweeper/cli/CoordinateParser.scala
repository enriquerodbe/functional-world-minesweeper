package io.scalac.minesweeper.cli

import io.scalac.minesweeper.api.{Board, Coordinate}

trait CoordinateParser:
  def parse(input: String, board: Board): Option[Coordinate]
