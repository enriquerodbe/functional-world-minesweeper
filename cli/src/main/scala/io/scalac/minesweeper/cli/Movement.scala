package io.scalac.minesweeper.cli

import io.scalac.minesweeper.api.Coordinate

enum Movement:
  def coordinate: Coordinate

  case Uncover(coordinate: Coordinate) extends Movement
  case Flag(coordinate: Coordinate) extends Movement
