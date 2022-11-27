package io.scalac.minesweeper.api

import munit.FunSuite

abstract class BoardFactorySpec(create: () => BoardFactory) extends FunSuite:
  test("Should create board of given size") {
    val board = create().create(5, _ => false)
    assertEquals(board.allCoordinates.size, 5)
  }

  test("Should set mines correctly") {
    val board = create().create(5, _.hashCode() % 2 == 0)
    board.allCoordinates.foreach(c =>
      assertEquals(c.hashCode() % 2 == 0, board.hasMine(c))
    )
  }
