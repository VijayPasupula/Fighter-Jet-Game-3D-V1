package com.example.game

object DialogUI {
  sealed trait DialogType
  case object StartDialog extends DialogType
  case object EndDialog extends DialogType

  case class DialogState(
    dialogType: DialogType,
    visible: Boolean,
    message: String,
    buttonText: String
  )

  // Button box in NDC coordinates (centered)
  case class ButtonBox(x1: Float, y1: Float, x2: Float, y2: Float)
  val buttonBox = ButtonBox(-0.25f, -0.05f, 0.25f, 0.10f)

  def drawDialog(dialog: DialogState): Unit = {
    // Black background overlay
    GL.glColor3f(0f, 0f, 0f)
    GL.glBegin(GLConsts.GL_QUADS)
    GL.glVertex2f(-1f, -1f)
    GL.glVertex2f(1f, -1f)
    GL.glVertex2f(1f, 1f)
    GL.glVertex2f(-1f, 1f)
    GL.glEnd()

    // Dialog box (light gray)
    GL.glColor3f(0.8f, 0.8f, 0.8f)
    GL.glBegin(GLConsts.GL_QUADS)
    GL.glVertex2f(-0.5f, -0.2f)
    GL.glVertex2f( 0.5f, -0.2f)
    GL.glVertex2f( 0.5f,  0.4f)
    GL.glVertex2f(-0.5f,  0.4f)
    GL.glEnd()

    // Button rectangle: blue for Start, green for OK
    dialog.dialogType match {
      case StartDialog =>
        GL.glColor3f(0.0f, 0.45f, 0.95f) // Blue
      case EndDialog =>
        GL.glColor3f(0.2f, 0.9f, 0.2f)   // Green
    }
    GL.glBegin(GLConsts.GL_QUADS)
    GL.glVertex2f(buttonBox.x1, buttonBox.y1)
    GL.glVertex2f(buttonBox.x2, buttonBox.y1)
    GL.glVertex2f(buttonBox.x2, buttonBox.y2)
    GL.glVertex2f(buttonBox.x1, buttonBox.y2)
    GL.glEnd()

    // Button border (dark)
    GL.glColor3f(0.1f, 0.1f, 0.1f)
    GL.glLineWidth(3f)
    GL.glBegin(GLConsts.GL_LINE_LOOP)
    GL.glVertex2f(buttonBox.x1, buttonBox.y1)
    GL.glVertex2f(buttonBox.x2, buttonBox.y1)
    GL.glVertex2f(buttonBox.x2, buttonBox.y2)
    GL.glVertex2f(buttonBox.x1, buttonBox.y2)
    GL.glEnd()
  }

  def isButtonClicked(mx: Float, my: Float): Boolean = {
    mx >= buttonBox.x1 && mx <= buttonBox.x2 && my >= buttonBox.y1 && my <= buttonBox.y2
  }
}