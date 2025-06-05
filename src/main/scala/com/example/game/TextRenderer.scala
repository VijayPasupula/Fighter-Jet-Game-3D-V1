package com.example.game

// Dummy text renderer (replace with your own if you have better)
object TextRenderer {
  // Draws text at (x, y) in NDC, with given scale and color (r,g,b)
  def drawText(text: String, x: Float, y: Float, scale: Float, color: (Float,Float,Float)): Unit = {
    // For simplicity, replace this with your own OpenGL text rendering.
    // This is just a stub.
    // You can use a bitmap font, or SDL_ttf, or any text method.
    // Here, we don't actually render text!
    ()
  }
}