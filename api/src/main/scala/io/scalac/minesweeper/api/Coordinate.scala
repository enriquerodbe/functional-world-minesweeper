package io.scalac.minesweeper.api

trait Coordinate

object Coordinate:
  enum State:
    case Uncovered, Covered, Flagged
