package io.scalac.minesweeper.squared

import io.scalac.minesweeper.api.Generators
import munit.ScalaCheckSuite
import org.scalacheck.Gen.*
import org.scalacheck.Prop.*

class SquaredCoordinateParserSpec extends ScalaCheckSuite:
  val gen = new Generators(SquaredBoardFactory)
  import gen.*

  property("Parse valid coordinate") {
    forAll(boardGen) { board =>
      val max = SquaredCoordinate.maxIndex(board.size)
      forAll(choose(0, max), choose(0, max)) { (x, y) =>
        val obtained =
          SquaredCoordinateParser.parse(s"${x.toString} ${y.toString}", board)
        assertEquals(obtained, Some(SquaredCoordinate(x, y)))
      }
    }
  }

  property("Return None for coordinates out of bounds") {
    forAll(boardGen) { board =>
      val outOfBoundsGen = posNum[Int].filter(_ >= board.size)
      forAll(outOfBoundsGen, outOfBoundsGen) { (x, y) =>
        val obtained =
          SquaredCoordinateParser.parse(s"${x.toString} ${y.toString}", board)
        assertEquals(obtained, None)
      }
    }
  }

  property("Return None for invalid coordinates") {
    forAll(boardGen, alphaNumStr) { (board, str) =>
      val obtained = SquaredCoordinateParser.parse(str, board)
      assertEquals(obtained, None)
    }
  }
