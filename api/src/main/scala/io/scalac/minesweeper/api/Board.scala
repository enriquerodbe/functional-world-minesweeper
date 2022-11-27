package io.scalac.minesweeper.api

trait Board:
  def uncover(coordinate: Coordinate): Board

  def flag(coordinate: Coordinate): Board

  def allCoordinates: Seq[Coordinate]

  def hasMine(coordinate: Coordinate): Boolean

  def stateAt(coordinate: Coordinate): Coordinate.State

  def state: Board.State

  def isPlaying: Boolean = state == Board.State.Playing

  def show: String

  def size: Int

object Board:
  enum State:
    case Playing, Lost, Won
