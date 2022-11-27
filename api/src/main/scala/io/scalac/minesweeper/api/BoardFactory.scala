package io.scalac.minesweeper.api

trait BoardFactory:
  def create(size: Int, hasMine: Coordinate => Boolean): Board
