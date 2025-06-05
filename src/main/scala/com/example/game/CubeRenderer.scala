package com.example.game

object CubeRenderer {
  def drawCube(): Unit = {
    // Draw only the bottom face (ground, green) for debug
    GL.glBegin(GLConsts.GL_QUADS)
    GL.glColor4f(0.15f, 0.4f, 0.18f, 1f)
    GL.glVertex3f(-1f, -1f, -1f)
    GL.glVertex3f( 1f, -1f, -1f)
    GL.glVertex3f( 1f, -1f,  1f)
    GL.glVertex3f(-1f, -1f,  1f)
    GL.glEnd()
  }
}