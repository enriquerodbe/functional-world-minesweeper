# Functional World Minesweeper

Example project used for the Functional World presentation on high-quality Scala code.

## Project structure

This is a [sbt](https://www.scala-sbt.org/) multi-module project with the following modules:

1. `api` - Contains only interfaces and specifications about the expected behavior of any
   implementation of the game.
2. `squared` - An implementation for the classic Minesweeper in 2D.

## Development environment

This project is built using sbt for Scala on the JVM. Both sbt and the JDK are needed in order to be
able to build and run the code.
The file [.tool-versions](.tool-versions) contains the exact versions used during the development of
this project.

It is recommended to use [asdf](https://asdf-vm.com) such that just doing `asdf install` will
install and provide the correct tools versions.

## SBT commands

You can expect all the usual sbt commands to work, for example `compile`, `test`, or `api/compile`.

There are some sbt command alias provided as well:

- `checkFormat` - Check all Scala files to verify their formatting.
- `lint` - Compile with strict flags enabled plus wartremover checks.
- `testCoverage` - Enable coverage, run test, generate a report, and verify that minimum code
  coverage is reached.
- `verify` - Run all the above sequentially.

## Tools

Here's a list of the tools used in the project. Some of them are sbt plugins that add their own
commands. See the corresponding documentation to find more about how they work:

- [Scalafmt](https://scalameta.org/scalafmt) - Code formatter
- [sbt-tpolecat](https://github.com/typelevel/sbt-tpolecat) - Strict scalac options
- [WartRemover](https://www.wartremover.org) - Static linter
- [sbt-scoverage](https://github.com/scoverage/sbt-scoverage) - Code coverage
