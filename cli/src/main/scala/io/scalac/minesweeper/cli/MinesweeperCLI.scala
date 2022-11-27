package io.scalac.minesweeper.cli

import cats.effect.*
import cats.syntax.monad.*
import io.scalac.minesweeper.api.*
import io.scalac.minesweeper.cli.Movement.{Flag, Uncover}
import scala.collection.mutable
import scala.util.Random

class MinesweeperCLI(boardFactory: BoardFactory, movementParser: MovementParser)
    extends IOApp:
  val readSize: IO[Int] =
    for
      _ <- IO.print("Size: ")
      input <- IO.readLine
      size <-
        input.toIntOption.fold(IO.println("Invalid size") >> readSize)(IO.pure)
    yield size

  val readDifficulty: IO[Double] =
    for
      _ <- IO.print("Difficulty: ")
      input <- IO.readLine
      difficulty <-
        input.toDoubleOption.fold(
          IO.println("Invalid difficulty") >> readDifficulty
        )(IO.pure)
    yield difficulty

  def makeMove(board: Board, input: String): Either[String, Board] =
    movementParser.parse(input, board) match
      case Right(Uncover(coordinate)) => Right(board.uncover(coordinate))
      case Right(Flag(coordinate))    => Right(board.flag(coordinate))
      case Left(error)                => Left(error)

  def mainLoop(board: Board): IO[Board] =
    for
      _ <- IO.println(board.show)
      _ <- IO.print("Play: ")
      input <- IO.readLine
      newBoard <- makeMove(board, input).fold(IO.println(_).as(board), IO.pure)
    yield newBoard

  val game: IO[Unit] =
    for
      size <- readSize
      difficulty <- readDifficulty
      mines = mutable.Map.empty[Coordinate, Boolean]
      initialBoard = boardFactory.create(
        size,
        mines.getOrElseUpdate(_, Random.nextDouble() < difficulty)
      )
      finalBoard <-
        initialBoard.iterateWhileM(mainLoop)(_.state == Board.State.Playing)
      _ <- IO.println(s"You ${finalBoard.state.toString}!")
    yield ()

  override def run(args: List[String]): IO[ExitCode] =
    game.as(ExitCode.Success)
