package io.scalac.minesweeper.api

import munit.{FunSuite, ScalaCheckSuite}
import org.scalacheck.Prop.*

abstract class BoardFactorySpec(boardFactory: BoardFactory)
    extends ScalaCheckSuite:
  val gen = Generators(boardFactory)
  import gen.*

  property("Should create board of given size") {
    forAll(sizeGen, hasMineGen) { (size, hasMine) =>
      val board = boardFactory.create(size, hasMine)
      assertEquals(board.allCoordinates.size, size)
    }
  }

  property("Should set mines correctly") {
    forAll(sizeGen, hasMineGen) { (size, hasMine) =>
      val board = boardFactory.create(size, hasMine)
      board.allCoordinates.foreach { c =>
        assertEquals(board.hasMine(c), hasMine(c))
      }
    }
  }
