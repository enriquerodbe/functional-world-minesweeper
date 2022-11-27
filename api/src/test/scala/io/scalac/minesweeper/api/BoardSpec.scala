package io.scalac.minesweeper.api

import munit.ScalaCheckSuite
import org.scalacheck.Prop.*

abstract class BoardSpec(factory: BoardFactory) extends ScalaCheckSuite:
  private val gen = new Generators(factory)
  import gen.*

  property("All coordinates should start covered") {
    forAll(boardGen) { board =>
      board.allCoordinates.foreach { coordinate =>
        assertEquals(board.stateAt(coordinate), Coordinate.State.Covered)
      }
    }
  }

  property("Flagging should set coordinate as flagged") {
    forAll(boardGen) { board =>
      forAll(coordinateGen(board)) { coordinate =>
        val flaggedBoard = board.flag(coordinate)
        assertEquals(flaggedBoard.stateAt(coordinate), Coordinate.State.Flagged)
      }
    }
  }

  property("Flagging one coordinate should not affect other coordinates") {
    forAll(boardGen) { board =>
      forAll(coordinateGen(board)) { coordinate =>
        val updatedBoard = board.flag(coordinate)
        updatedBoard.allCoordinates
          .filterNot(_ == coordinate)
          .foreach { otherCoordinate =>
            assertEquals(
              updatedBoard.stateAt(otherCoordinate),
              board.stateAt(otherCoordinate)
            )
          }
      }
    }
  }

  property(
    "Uncovering any empty coordinate should set coordinate as uncovered"
  ) {
    forAll(boardGen(chanceOfMine = .1)) { board =>
      forAll(emptyCoordinateGen(board)) { coordinate =>
        assertEquals(
          board.uncover(coordinate).stateAt(coordinate),
          Coordinate.State.Uncovered
        )
      }
    }
  }

  property("Uncovering some empty coordinate should keep state as Playing") {
    forAll(boardGen(chanceOfMine = .1)) { board =>
      emptyCoordinates(board).size > 1 ==>
        forAll(emptyCoordinateGen(board)) { coordinate =>
          assertEquals(board.uncover(coordinate).state, Board.State.Playing)
        }
    }
  }

  property("Board state should start as Playing") {
    forAll(boardGen) { board =>
      assertEquals(board.state, Board.State.Playing)
    }
  }

  property("Uncovering any coordinate which has mine should lose") {
    forAll(boardGen(chanceOfMine = .8)) { board =>
      forAll(minedCoordinateGen(board)) { coordinate =>
        assertEquals(board.uncover(coordinate).state, Board.State.Lost)
      }
    }
  }

  property("Uncovering all empty coordinates should win") {
    forAll(boardGen) { board =>
      emptyCoordinates(board).nonEmpty ==> {
        val finalState =
          emptyCoordinates(board)
            .foldLeft(board)(_ uncover _)
            .state

        finalState == Board.State.Won
      }
    }
  }

  property("Uncovering flagged mine should not lose") {
    forAll(boardGen(chanceOfMine = .8)) { board =>
      forAll(minedCoordinateGen(board)) { coordinate =>
        assertEquals(
          board.flag(coordinate).uncover(coordinate).state,
          Board.State.Playing
        )
      }
    }
  }

  private def emptyCoordinates(board: Board): Seq[Coordinate] =
    board.allCoordinates.filterNot(board.hasMine)
