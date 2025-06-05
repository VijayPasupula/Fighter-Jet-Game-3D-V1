package com.example.game

import scala.scalanative.unsafe._
import scala.math._

object GL3D {
  def setupChaseCameraView(width: Int, height: Int, jetPos: Vec3, jetYaw: Float, jetPitch: Float): Unit = {
    val aspect = width.toFloat / height.toFloat
    GL.glMatrixMode(GLConsts.GL_PROJECTION)
    GL.glLoadIdentity()
    GLU.gluPerspective(60.0f, aspect, 0.1f, 50.0f)
    GL.glMatrixMode(GLConsts.GL_MODELVIEW)
    GL.glLoadIdentity()

    val distanceBehind = 1.5f // units behind, positive value
    val heightAbove = 0.7f    // units above

    // Calculate the FORWARD vector of the jet
    val yawRad = jetYaw.toDouble
    val pitchRad = jetPitch.toDouble

    val forwardX = (sin(yawRad) * cos(pitchRad)).toFloat
    val forwardY = (-sin(pitchRad)).toFloat
    val forwardZ = (cos(yawRad) * cos(pitchRad)).toFloat

    // Camera is BEHIND the jet (subtract forward vector, not add)
    val camX = jetPos.x - forwardX * distanceBehind
    val camY = jetPos.y + heightAbove
    val camZ = jetPos.z - forwardZ * distanceBehind

    GLU.gluLookAt(
      camX, camY, camZ,           // camera position (behind jet)
      jetPos.x, jetPos.y, jetPos.z, // look at jet
      0f, 1f, 0f                  // up vector
    )
  }
}