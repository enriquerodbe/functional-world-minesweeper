package io.scalac.minesweeper.cli

import cats.parse.Parser
import cats.parse.Parser.*
import cats.parse.Rfc5234.sp
import io.scalac.minesweeper.api.{Board, Coordinate}

class MovementParser(coordinateParser: CoordinateParser):
  private val uncover: Parser[Movement] =
    (string("uncover") | char('u')).as(Movement.Uncover)

  private val flag: Parser[Movement] =
    (string("flag") | char('f')).as(Movement.Flag)

  private val movement: Parser[Movement] =
    uncover | flag

  private val coordinate: Parser[Coordinate] =
    anyChar
      .repAs[String]
      .map(coordinateParser.parse)
      .flatMap {
        case Some(coordinate) => pure(coordinate)
        case None             => fail
      }

  private val parser: Parser[(Movement, Coordinate)] =
    (movement <* sp.rep) ~ coordinate

  def parse(input: String): Option[(Movement, Coordinate)] =
    parser.parseAll(input).toOption

object MovementParser:
  def apply(
      board: Board,
      coordinateParserFactory: CoordinateParserFactory
  ): MovementParser =
    new MovementParser(coordinateParserFactory.create(board))
