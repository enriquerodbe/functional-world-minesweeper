package io.scalac.minesweeper.cli

import io.scalac.minesweeper.api.Board

trait CoordinateParserFactory:
  def create(board: Board): CoordinateParser
