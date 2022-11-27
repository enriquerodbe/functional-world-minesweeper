lazy val commonSettings = Seq(
  organization := "io.scalac",
  scalaVersion := "3.2.1",
  wartremoverErrors ++= Warts.unsafe.diff(Seq(Wart.Any)),
  coverageFailOnMinimum := true,
  coverageMinimumStmtTotal := 100,
  coverageMinimumBranchTotal := 100
)

val catsEffect = "org.typelevel" %% "cats-effect" % "3.4.1"
val catsParse = "org.typelevel" %% "cats-parse" % "0.3.8"
val munit = "org.scalameta" %% "munit" % "0.7.29" % Test
val munitScalaCheck = "org.scalameta" %% "munit-scalacheck" % "0.7.29" % Test

lazy val api =
  project
    .in(file("api"))
    .settings(commonSettings)
    .settings(
      name := "minesweeper-api",
      libraryDependencies ++= Seq(munit, munitScalaCheck)
    )

lazy val squared =
  project
    .in(file("squared"))
    .settings(commonSettings)
    .dependsOn(api % "test->test;compile->compile")
    .settings(
      name := "minesweeper-squared",
      libraryDependencies += catsParse
    )

lazy val cli =
  project
    .in(file("cli"))
    .settings(commonSettings)
    .dependsOn(api % "test->test;compile->compile")
    .settings(
      name := "minesweeper-cli",
      libraryDependencies ++= Seq(catsEffect, catsParse)
    )

lazy val `cli-squared` =
  project
    .in(file("cli-squared"))
    .settings(commonSettings)
    .dependsOn(cli, squared)
    .settings(
      name := "minesweeper-cli-squared"
    )

addCommandAlias("checkFormat", "scalafmtSbtCheck; scalafmtCheckAll")
addCommandAlias("lint", "compile")
addCommandAlias("testCoverage", "coverage; test; squared/coverageReport")
addCommandAlias("mutationTest", "project squared; stryker")
addCommandAlias("verify", "checkFormat; lint; testCoverage; mutationTest")
