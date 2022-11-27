package io.scalac.minesweeper.api

trait Coordinate

object Coordinate:
  sealed trait State:
    def uncover: State
    def flag: State

  object State:
    case object Covered extends State:
      override def uncover: State = Uncovered
      override def flag: State = Flagged

    case object Uncovered extends State:
      override def uncover: State = Uncovered
      override def flag: State = Uncovered

    case object Flagged extends State:
      override def uncover: State = Flagged
      override def flag: State = Covered
