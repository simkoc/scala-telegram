import xerial.sbt.Sonatype.sonatype01
name :=  "scala-telegram"
organization := "de.halcony"
ThisBuild / description := "An implementation of process handling and threading I found using repeatedly myself"
ThisBuild / versionScheme := Some("semver-spec")
Global / onChangedBuildSource := ReloadOnSourceChanges
enablePlugins(JavaAppPackaging)
lazy val scala213 = "2.13.14"
lazy val scala306 = "3.6.3"

scalaVersion := scala306

crossScalaVersions := List(scala213, scala306)

libraryDependencies ++= Seq(
  "org.scalatest"                 %% "scalatest"               % "3.2.19" % Test,
  "org.wvlet.airframe"            %% "airframe-log"            % "2025.1.8",
  "org.slf4j"                     %  "slf4j-nop"               % "2.0.17",
  "com.softwaremill.sttp.client4" %% "core"                    % "4.0.0-RC2",
)

ThisBuild / resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.mavenCentral,
  "Apache public" at "https://repository.apache.org/content/groups/public/"
)

scalacOptions ++= Seq(
  "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
  "-encoding", "utf-8",                // Specify character encoding used by source files.
  "-explaintypes",                     // Explain type errors in more detail.
  "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials",            // Existential types (besides wildcard types) can be written and inferred
  "-language:experimental.macros",     // Allow macro definition (besides implementation and application)
  "-language:higherKinds",             // Allow higher-kinded types
  "-language:implicitConversions",     // Allow definition of implicit functions called views
  "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
  //"-Xcheckinit",                       // Wrap field accessors to throw an exception on uninitialized access.
  // "-Xfatal-warnings",                  // Fail the compilation if there are any warnings.
  //"-Xlint:adapted-args",               // Warn if an argument list is modified to match the receiver.
  //"-Xlint:constant",                   // Evaluation of a constant arithmetic expression results in an error.
  //"-Xlint:delayedinit-select",         // Selecting member of DelayedInit.
  //"-Xlint:doc-detached",               // A Scaladoc comment appears to be detached from its element.
  //"-Xlint:inaccessible",               // Warn about inaccessible types in method signatures.
  //"-Xlint:infer-any",                  // Warn when a type argument is inferred to be `Any`.
  //"-Xlint:missing-interpolator",       // A string literal appears to be missing an interpolator id.
  //"-Xlint:option-implicit",            // Option.apply used implicit view.
  //"-Xlint:package-object-classes",     // Class or object defined in package object.
  //"-Xlint:poly-implicit-overload",     // Parameterized overloaded implicit methods are not visible as view bounds.
  //"-Xlint:private-shadow",             // A private field (or class parameter) shadows a superclass field.
  //"-Xlint:stars-align",                // Pattern sequence wildcard must align with sequence component.
  //"-Xlint:type-parameter-shadow",      // A local type parameter shadows a type already in scope.
  //"-Ywarn-dead-code",                  // Warn when dead code is identified.
  //"-Ywarn-extra-implicit",             // Warn when more than one implicit parameter section is defined.
  //"-Xlint:nullary-override",           // Warn when non-nullary def f() overrides nullary def f.
  //"-Xlint:nullary-unit",               // Warn when nullary methods return Unit.
  //"-Ywarn-numeric-widen",              // Warn when numerics are widened.
  //"-Ywarn-unused:implicits",           // Warn if an implicit parameter is unused.
  //"-Ywarn-unused:imports",             // Warn if an import selector is not referenced.
  //"-Ywarn-unused:locals",              // Warn if a local definition is unused.
  //"-Ywarn-unused:params",              // Warn if a value parameter is unused.
  //"-Ywarn-unused:patvars",             // Warn if a variable bound in a pattern is unused.
  //"-Ywarn-unused:privates",            // Warn if a private member is unused.
  //"-Ywarn-value-discard"               // Warn when non-Unit expression results are unused.
)

compile / javacOptions ++= Seq("-Xlint:all", "-Xlint:-cast", "-g")
Test / fork :=  false
testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

checkstyleConfigLocation := CheckstyleConfigLocation.File("config/checkstyle/google_checks.xml")
checkstyleSeverityLevel := Some(CheckstyleSeverityLevel.Info)

ThisBuild / sonatypeCredentialHost := sonatype01
// this is required for sonatype sync requirements
sonatypeProfileName := "de.halcony"
// this is required for sonatype sync requirements
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/simkoc/scala-processes"),
    "scm:git@github.com:simkoc/scala-processes.git"
  )
)
// this is required for sonatype sync requirements
ThisBuild / developers := List(
  Developer(
    id   = "simkoc",
    name = "Simon Koch",
    email = "ossrh@halcony.de",
    url = url("https://github.com/simkoc/")
  )
)
// this is required for sonatype sync requirements
ThisBuild / licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))
// this is required for sonatype sync requirements
ThisBuild / homepage := Some(url("https://github.com/simkoc/scala-processes"))


// below is pretty much cargo cult/fuzzing....
import ReleaseTransformations._
releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseVersionBump := sbtrelease.Version.Bump.Bugfix
publishTo := sonatypePublishToBundle.value
releaseProcess := Seq[ReleaseStep](
  runClean,
  runTest,
  inquireVersions,
  setReleaseVersion,
  commitReleaseVersion,
  publishArtifacts,
  releaseStepCommand("publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges,
)