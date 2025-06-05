package com.example.game

import scala.scalanative.unsafe._
import scala.scalanative.unsigned._

@extern
object GL {
  def glClear(mask: CUnsignedInt): Unit = extern
  def glClearColor(red: CFloat, green: CFloat, blue: CFloat, alpha: CFloat): Unit = extern
  def glViewport(x: CInt, y: CInt, width: CInt, height: CInt): Unit = extern
  def glEnable(cap: CUnsignedInt): Unit = extern
  def glDisable(cap: CUnsignedInt): Unit = extern
  def glBlendFunc(sfactor: CUnsignedInt, dfactor: CUnsignedInt): Unit = extern
  def glMultMatrixf(m: Ptr[Float]): Unit = extern


  def glBegin(mode: CUnsignedInt): Unit = extern
  def glEnd(): Unit = extern
  def glColor3f(r: CFloat, g: CFloat, b: CFloat): Unit = extern
  def glColor4f(r: CFloat, g: CFloat, b: CFloat, a: CFloat): Unit = extern
  def glVertex3f(x: CFloat, y: CFloat, z: CFloat): Unit = extern
  def glVertex2f(x: CFloat, y: CFloat): Unit = extern

  def glLineWidth(width: CFloat): Unit = extern

  def glMatrixMode(mode: CUnsignedInt): Unit = extern
  def glLoadIdentity(): Unit = extern
  def glOrtho(left: CDouble, right: CDouble, bottom: CDouble, top: CDouble, zNear: CDouble, zFar: CDouble): Unit = extern

  def glFrustum(left: CDouble, right: CDouble, bottom: CDouble, top: CDouble, zNear: CDouble, zFar: CDouble): Unit = extern
  def glPushMatrix(): Unit = extern
  def glPopMatrix(): Unit = extern
  def glTranslatef(x: CFloat, y: CFloat, z: CFloat): Unit = extern
  def glRotatef(angle: CFloat, x: CFloat, y: CFloat, z: CFloat): Unit = extern
}