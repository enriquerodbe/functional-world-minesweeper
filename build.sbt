lazy val commonSettings = Seq(
  organization := "io.scalac",
  scalaVersion := "3.2.1",
  wartremoverErrors ++= Warts.unsafe,
  coverageFailOnMinimum := true,
  coverageMinimumStmtTotal := 100,
  coverageMinimumBranchTotal := 100
)

lazy val api =
  project
    .in(file("api"))
    .settings(commonSettings)
    .settings(
      name := "minesweeper-api"
    )

lazy val squared =
  project
    .in(file("squared"))
    .settings(commonSettings)
    .dependsOn(api % "test->test;compile->compile")
    .settings(
      name := "minesweeper-squared"
    )

addCommandAlias("checkFormat", ";scalafmtSbtCheck ;scalafmtCheckAll")
addCommandAlias("lint", "compile")
addCommandAlias("testCoverage", ";coverage ;test ;coverageReport")
addCommandAlias("verify", ";checkFormat ;lint ;testCoverage")
