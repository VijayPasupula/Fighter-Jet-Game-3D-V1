package com.example.game

object JetRenderer3D {
  def drawJet(
    center: Vec3, yaw: Float, pitch: Float, roll: Float,
    color: (Float, Float, Float), isPlayer: Boolean, dummy: Float
  ): Unit = {
    GL.glPushMatrix()
    GL.glTranslatef(center.x, center.y, center.z)
    GL.glRotatef(math.toDegrees(yaw).toFloat, 0, 1, 0)
    GL.glRotatef(math.toDegrees(pitch).toFloat, 1, 0, 0)
    GL.glRotatef(math.toDegrees(roll).toFloat, 0, 0, 1)

    val fuselageLength = 0.25f
    val fuselageWidth  = 0.08f
    val fuselageHeight = 0.06f

    // 3D Fuselage (prismatic body - triangle sandwich)
    val nose   = (0f, 0f,  fuselageLength)
    val rearL  = (-fuselageWidth, -fuselageHeight, -fuselageLength * 0.7f)
    val rearR  = ( fuselageWidth, -fuselageHeight, -fuselageLength * 0.7f)
    val rearT  = (0f, fuselageHeight, -fuselageLength * 0.7f)

    // Bottom triangle
    GL.glBegin(GLConsts.GL_TRIANGLES)
    GL.glColor3f(color._1, color._2, color._3)
    GL.glVertex3f(nose._1, nose._2, nose._3)
    GL.glVertex3f(rearL._1, rearL._2, rearL._3)
    GL.glVertex3f(rearR._1, rearR._2, rearR._3)
    GL.glEnd()

    // Left side
    GL.glBegin(GLConsts.GL_TRIANGLES)
    GL.glColor3f(color._1 * 0.9f, color._2 * 0.9f, color._3 * 0.7f)
    GL.glVertex3f(nose._1, nose._2, nose._3)
    GL.glVertex3f(rearL._1, rearL._2, rearL._3)
    GL.glVertex3f(rearT._1, rearT._2, rearT._3)
    GL.glEnd()

    // Right side
    GL.glBegin(GLConsts.GL_TRIANGLES)
    GL.glColor3f(color._1 * 0.7f, color._2 * 0.7f, color._3 * 0.9f)
    GL.glVertex3f(nose._1, nose._2, nose._3)
    GL.glVertex3f(rearT._1, rearT._2, rearT._3)
    GL.glVertex3f(rearR._1, rearR._2, rearR._3)
    GL.glEnd()

    // Back face
    GL.glBegin(GLConsts.GL_TRIANGLES)
    GL.glColor3f(color._1 * 0.7f, color._2 * 0.7f, color._3 * 0.7f)
    GL.glVertex3f(rearT._1, rearT._2, rearT._3)
    GL.glVertex3f(rearL._1, rearL._2, rearL._3)
    GL.glVertex3f(rearR._1, rearR._2, rearR._3)
    GL.glEnd()

    // Wings (flat, wide triangles)
    GL.glBegin(GLConsts.GL_TRIANGLES)
    GL.glColor3f(color._1 * 0.9f, color._2 * 0.9f, color._3)
    GL.glVertex3f(0f, 0f, fuselageLength * 0.1f)
    GL.glVertex3f(-fuselageWidth * 2.2f, 0f, -fuselageLength * 0.4f)
    GL.glVertex3f(fuselageWidth * 2.2f, 0f, -fuselageLength * 0.4f)
    GL.glEnd()

    // Vertical tail
    GL.glBegin(GLConsts.GL_TRIANGLES)
    GL.glColor3f(0.5f, 0.5f, 0.9f)
    GL.glVertex3f(0f, 0f, -fuselageLength * 0.2f)
    GL.glVertex3f(0f, fuselageHeight * 2f, -fuselageLength * 0.4f)
    GL.glVertex3f(0f, 0f, -fuselageLength * 0.7f)
    GL.glEnd()

    // Cockpit highlight (always visible)
    GL.glBegin(GLConsts.GL_QUADS)
    GL.glColor3f(0.8f, 0.8f, 1f)
    GL.glVertex3f(-0.025f, 0.02f, fuselageLength * 0.5f)
    GL.glVertex3f(0.025f, 0.02f, fuselageLength * 0.5f)
    GL.glVertex3f(0.025f, 0.02f, fuselageLength * 0.2f)
    GL.glVertex3f(-0.025f, 0.02f, fuselageLength * 0.2f)
    GL.glEnd()

    // 3D weapon system: a gray cylinder under the jet
    drawGunCylinder(fuselageWidth, fuselageHeight, fuselageLength)

    GL.glPopMatrix()
  }

  // Draws a simple cylinder under the jet as the weapon system
  def drawGunCylinder(fuselageWidth: Float, fuselageHeight: Float, fuselageLength: Float): Unit = {
    val radius = 0.015f
    val length = 0.11f
    val slices = 16

    GL.glPushMatrix()
    // Position: slightly below the nose, aligned to jet's Z
    GL.glTranslatef(0f, -fuselageHeight - 0.018f, fuselageLength * 0.38f)
    // Cylinder axis = Z direction

    // Draw side of cylinder
    GL.glBegin(GLConsts.GL_QUADS)
    GL.glColor3f(0.3f, 0.3f, 0.3f)
    for (i <- 0 until slices) {
      val theta0 = (2 * Math.PI * i) / slices
      val theta1 = (2 * Math.PI * (i + 1)) / slices
      val x0 = (radius * Math.cos(theta0)).toFloat
      val y0 = (radius * Math.sin(theta0)).toFloat
      val x1 = (radius * Math.cos(theta1)).toFloat
      val y1 = (radius * Math.sin(theta1)).toFloat
      // Quad for side
      GL.glVertex3f(x0, y0, 0f)
      GL.glVertex3f(x1, y1, 0f)
      GL.glVertex3f(x1, y1, length)
      GL.glVertex3f(x0, y0, length)
    }
    GL.glEnd()

    // Draw front face (circle)
    GL.glBegin(GLConsts.GL_TRIANGLE_FAN)
    GL.glColor3f(0.5f, 0.5f, 0.5f)
    GL.glVertex3f(0f, 0f, length)
    for (i <- 0 to slices) {
      val theta = (2 * Math.PI * i) / slices
      val x = (radius * Math.cos(theta)).toFloat
      val y = (radius * Math.sin(theta)).toFloat
      GL.glVertex3f(x, y, length)
    }
    GL.glEnd()

    // Draw back face (circle)
    GL.glBegin(GLConsts.GL_TRIANGLE_FAN)
    GL.glColor3f(0.4f, 0.4f, 0.4f)
    GL.glVertex3f(0f, 0f, 0f)
    for (i <- 0 to slices) {
      val theta = (2 * Math.PI * i) / slices
      val x = (radius * Math.cos(theta)).toFloat
      val y = (radius * Math.sin(theta)).toFloat
      GL.glVertex3f(x, y, 0f)
    }
    GL.glEnd()

    GL.glPopMatrix()
  }
}