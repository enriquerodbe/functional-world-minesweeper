package io.scalac.minesweeper.cli

import cats.effect.*
import cats.syntax.monad.*
import io.scalac.minesweeper.api.*
import io.scalac.minesweeper.cli.Movement.{Flag, Uncover}
import scala.collection.mutable
import scala.util.Random

class MinesweeperCLI(
    boardFactory: BoardFactory,
    coordinateParserFactory: CoordinateParserFactory
) extends IOApp:
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

  def makeMove(
      board: Board,
      input: String,
      movementParser: MovementParser
  ): Either[String, Board] =
    movementParser.parse(input) match
      case Some((Uncover, coordinate)) => Right(board.uncover(coordinate))
      case Some((Flag, coordinate))    => Right(board.flag(coordinate))
      case None                        => Left("Invalid input")

  def mainLoop(board: Board, movementParser: MovementParser): IO[Board] =
    for
      _ <- IO.println(board.show)
      _ <- IO.print("Play: ")
      input <- IO.readLine
      newBoard <- makeMove(board, input, movementParser).fold(
        IO.println(_).as(board),
        IO.pure
      )
    yield newBoard

  val game: IO[Unit] =
    for
      size <- readSize
      difficulty <- readDifficulty
      initialBoard = generateBoard(size, difficulty)
      movementParser = MovementParser(initialBoard, coordinateParserFactory)
      finalBoard <-
        initialBoard.iterateWhileM(mainLoop(_, movementParser))(_.isPlaying)
      _ <- IO.println(s"You ${finalBoard.state.toString}!")
    yield ()

  def generateBoard(size: Int, difficulty: Double): Board =
    val mines = mutable.Map.empty[Coordinate, Boolean]
    val hasMine = mines.getOrElseUpdate(_, Random.nextDouble() < difficulty)
    boardFactory.create(size, hasMine)

  override def run(args: List[String]): IO[ExitCode] =
    game.as(ExitCode.Success)
