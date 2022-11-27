package io.scalac.minesweeper.squared

import io.scalac.minesweeper.api.Generators
import munit.ScalaCheckSuite
import org.scalacheck.Gen.*
import org.scalacheck.Prop.*

class SquaredCoordinateParserSpec extends ScalaCheckSuite:
  val gen = Generators(SquaredBoardFactory)

  import gen.*

  property("Parse valid coordinate") {
    forAll(boardGen) { board =>
      val max = SquaredCoordinate.maxIndex(board.size)
      val parser = SquaredCoordinateParser(board)
      forAll(choose(0, max), choose(0, max)) { (x, y) =>
        val obtained = parser.parse(s"${x.toString} ${y.toString}")
        assertEquals(obtained, Some(SquaredCoordinate(x, y)))
      }
    }
  }

  property("Return None for coordinates out of bounds") {
    forAll(boardGen) { board =>
      val parser = SquaredCoordinateParser(board)
      val maxIndex = SquaredCoordinate.maxIndex(board.size)

      val indexOutOfBoundsGen = posNum[Int].filter(_ > maxIndex)
      val validIndexGen = choose(0, maxIndex)

      forAll(indexOutOfBoundsGen, validIndexGen) {
        (indexOutOfBounds, validIndex) =>
          val xOutOfBounds =
            parser.parse(s"${indexOutOfBounds.toString} ${validIndex.toString}")
          assertEquals(xOutOfBounds, None)

          val yOutOfBounds =
            parser.parse(s"${validIndex.toString} ${indexOutOfBounds.toString}")
          assertEquals(yOutOfBounds, None)
      }
    }
  }

  property("Return None for invalid coordinates") {
    forAll(boardGen, alphaNumStr) { (board, str) =>
      val parser = SquaredCoordinateParser(board)
      assertEquals(parser.parse(str), None)
    }
  }
