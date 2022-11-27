package io.scalac.minesweeper.api

import org.scalacheck.Gen.*
import org.scalacheck.{Cogen, Gen}
import scala.math.max

class Generators(boardFactory: BoardFactory):
  def boardGen(chanceOfMine: Double): Gen[Board] =
    for {
      size <- sizeGen
      hasMine <- hasMineGen(prob(chanceOfMine))
    } yield boardFactory.create(size, hasMine)

  val sizeGen: Gen[Int] =
    sized(size => choose(1, max(1, size)))

  def hasMineGen(boolGen: Gen[Boolean]): Gen[Coordinate => Boolean] =
    function1(boolGen)(coordinateCogen)

  private val coordinateCogen: Cogen[Coordinate] =
    Cogen[Coordinate] { (c: Coordinate) => c.hashCode().longValue() }

  val boardGen: Gen[Board] =
    double.flatMap(boardGen(_))

  val hasMineGen: Gen[Coordinate => Boolean] =
    hasMineGen(oneOf(true, false))

  def coordinateGen(board: Board): Gen[Coordinate] =
    oneOf(board.allCoordinates)

  def emptyCoordinateGen(board: Board): Gen[Coordinate] =
    coordinateGen(board).filterNot(board.hasMine)

  def minedCoordinateGen(board: Board): Gen[Coordinate] =
    coordinateGen(board).filter(board.hasMine)
