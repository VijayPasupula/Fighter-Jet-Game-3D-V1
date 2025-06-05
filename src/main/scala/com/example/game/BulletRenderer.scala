package com.example.game

import scala.math._

object BulletRenderer {
  // Draws a sphere at position (x, y, z) with the given radius
  def drawSphere(center: Vec3, radius: Float, color: (Float, Float, Float)): Unit = {
    val lats = 10
    val longs = 10
    GL.glPushMatrix()
    GL.glTranslatef(center.x, center.y, center.z)
    GL.glColor3f(color._1, color._2, color._3)
    for (i <- 0 until lats) {
      val lat0 = Pi * (-0.5 + i.toDouble / lats)
      val z0 = sin(lat0) * radius
      val zr0 = cos(lat0) * radius
      val lat1 = Pi * (-0.5 + (i + 1).toDouble / lats)
      val z1 = sin(lat1) * radius
      val zr1 = cos(lat1) * radius

      GL.glBegin(GLConsts.GL_QUAD_STRIP)
      for (j <- 0 to longs) {
        val lng = 2 * Pi * j.toDouble / longs
        val x = cos(lng)
        val y = sin(lng)
        GL.glVertex3f((x * zr0).toFloat, (y * zr0).toFloat, z0.toFloat)
        GL.glVertex3f((x * zr1).toFloat, (y * zr1).toFloat, z1.toFloat)
      }
      GL.glEnd()
    }
    GL.glPopMatrix()
  }
}