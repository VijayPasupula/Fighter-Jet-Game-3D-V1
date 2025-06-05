package com.example.game

import scala.scalanative.unsafe._

object GLU {
  def gluPerspective(fovy: Float, aspect: Float, zNear: Float, zFar: Float): Unit = {
    val fH = math.tan(math.toRadians(fovy / 2.0)).toFloat * zNear
    val fW = fH * aspect
    GL.glFrustum(-fW, fW, -fH, fH, zNear, zFar)
  }

  def gluLookAt(eyeX: Float, eyeY: Float, eyeZ: Float,
                centerX: Float, centerY: Float, centerZ: Float,
                upX: Float, upY: Float, upZ: Float): Unit = {
    val fx = centerX - eyeX
    val fy = centerY - eyeY
    val fz = centerZ - eyeZ
    val rlf = 1.0f / math.sqrt(fx*fx + fy*fy + fz*fz).toFloat
    val fvx = fx * rlf
    val fvy = fy * rlf
    val fvz = fz * rlf

    val sx = fvy * upZ - fvz * upY
    val sy = fvz * upX - fvx * upZ
    val sz = fvx * upY - fvy * upX
    val rls = 1.0f / math.sqrt(sx*sx + sy*sy + sz*sz).toFloat
    val svx = sx * rls
    val svy = sy * rls
    val svz = sz * rls

    val ux = svy * fvz - svz * fvy
    val uy = svz * fvx - svx * fvz
    val uz = svx * fvy - svy * fvx

    val m = Array[Float](
      svx, ux, -fvx, 0f,
      svy, uy, -fvy, 0f,
      svz, uz, -fvz, 0f,
      0f,  0f,   0f, 1f
    )
    val arr = stackalloc[Float](16)
    var i = 0
    while (i < 16) { arr(i) = m(i); i += 1 }
    GL.glMultMatrixf(arr)
    GL.glTranslatef(-eyeX, -eyeY, -eyeZ)
  }
}