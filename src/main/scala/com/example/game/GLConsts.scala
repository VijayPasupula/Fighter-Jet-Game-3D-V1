package com.example.game

import scala.scalanative.unsafe.CUnsignedInt
import scala.scalanative.unsigned._

object GLConsts {
  val GL_COLOR_BUFFER_BIT: CUnsignedInt = 0x00004000.toUInt
  val GL_DEPTH_BUFFER_BIT: CUnsignedInt = 0x00000100.toUInt
  val GL_DEPTH_TEST: CUnsignedInt = 0x0B71.toUInt
  val GL_TRIANGLES: CUnsignedInt = 0x0004.toUInt
  val GL_LINES: CUnsignedInt = 0x0001.toUInt
  val GL_QUADS: CUnsignedInt = 0x0007.toUInt
  val GL_LINE_LOOP: CUnsignedInt = 0x0002.toUInt
  val GL_TRIANGLE_FAN: CUnsignedInt = 0x0006.toUInt

  val GL_PROJECTION: CUnsignedInt = 0x1701.toUInt
  val GL_MODELVIEW: CUnsignedInt = 0x1700.toUInt
  val GL_QUAD_STRIP: CUnsignedInt = 0x0008.toUInt
}