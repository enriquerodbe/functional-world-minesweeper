package io.scalac.minesweeper.squared

import cats.parse.Numbers.*
import cats.parse.Parser
import cats.parse.Parser.*
import cats.parse.Rfc5234.*
import cats.syntax.apply.*
import io.scalac.minesweeper.api.Board

class SquaredCoordinateParser(board: Board):
  private val nonNegNum: Parser[Int] =
    bigInt.map(_.toInt).filter(_ >= 0)

  private val parser: Parser[SquaredCoordinate] =
    (nonNegNum <* sp.rep, nonNegNum)
      .mapN(SquaredCoordinate.validated(_, _, board.size))
      .flatMap {
        case Some(coordinate) => pure(coordinate)
        case None             => fail
      }

  def parse(input: String): Option[SquaredCoordinate] =
    parser.parseAll(input).toOption
