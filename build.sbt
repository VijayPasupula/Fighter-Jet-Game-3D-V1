// Project Name and Version
name := "Fighter-Jet-3D"
version := "0.1.0-SNAPSHOT"
organization := "com.example"

// Scala Version
ThisBuild / scalaVersion := "2.13.14"

// Define the version for Scala Native Graphics libraries
val scalaNativeGraphicsV = "0.2"

lazy val root = (project in file("."))
  .enablePlugins(ScalaNativePlugin)
  .settings(
    // Main class for the native executable
    Compile / mainClass := Some("com.example.game.GameWindow"),

    // General linking configuration for optimized binaries
    nativeConfig ~= { c =>
      c.withLTO(scala.scalanative.build.LTO.none) // thin
       .withMode(scala.scalanative.build.Mode.debug) // releaseFast
       .withGC(scala.scalanative.build.GC.immix) // commix
       .withLinkingOptions(c.linkingOptions ++ List("-lSDL2", "-lGL", "-lGLU"))
    }

  )