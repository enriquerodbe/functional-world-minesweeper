package io.scalac.minesweeper.squared

import io.scalac.minesweeper.api.{BoardFactory, Coordinate}

class SquaredBoardFactory extends BoardFactory:
  override def create(size: Int, hasMine: Coordinate => Boolean): SquaredBoard =
    SquaredBoard()
