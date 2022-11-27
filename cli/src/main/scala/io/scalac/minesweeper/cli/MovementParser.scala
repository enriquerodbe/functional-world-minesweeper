package io.scalac.minesweeper.cli

import io.scalac.minesweeper.api.Board
import io.scalac.minesweeper.cli.Movement.{Flag, Uncover}
import scala.util.matching.Regex

class MovementParser(coordinateParser: CoordinateParser):
  private val uncover = "u"
  private val flag = "f"
  val regex: Regex = """^([uf]) (.+)$""".r

  def parse(input: String, board: Board): Either[String, Movement] =
    input match
      case regex(movement, coordinateString) =>
        val maybeCoordinate =
          coordinateParser.parse(coordinateString, board)
        (movement, maybeCoordinate) match
          case (`uncover`, Some(coordinate)) => Right(Uncover(coordinate))
          case (`flag`, Some(coordinate))    => Right(Flag(coordinate))
          case _                             => Left("Invalid coordinate")
      case _ => Left("Invalid input")
