package com.example.game

import scala.scalanative.unsafe._
import scala.scalanative.unsigned._
import scala.scalanative.libc.stdlib

object SDLConsts {
  val SDL_GL_CONTEXT_MAJOR_VERSION: CInt = 17
  val SDL_GL_CONTEXT_MINOR_VERSION: CInt = 18
  val SDL_WINDOW_OPENGL: CUnsignedInt = 0x00000002.toUInt
  val SDL_INIT_VIDEO: CUnsignedInt = 0x00000020.toUInt
  val SDL_QUIT: CUnsignedInt = 0x100.toUInt
  val SDL_KEYDOWN: CUnsignedInt = 0x300.toUInt
  val SDL_KEYUP: CUnsignedInt = 0x301.toUInt
  val SDL_WINDOWPOS_CENTERED: CInt = 0x2FFF0000
  val SDL_WINDOW_RESIZABLE: CUnsignedInt = 0x00000020.toUInt
}

@extern
object SDL {
  type SDL_Window = CStruct0
  type SDL_Event = CStruct0

  def SDL_Init(flags: CUnsignedInt): CInt = extern
  def SDL_Quit(): Unit = extern
  def SDL_CreateWindow(title: CString, x: CInt, y: CInt, w: CInt, h: CInt, flags: CUnsignedInt): Ptr[SDL_Window] = extern
  def SDL_DestroyWindow(window: Ptr[SDL_Window]): Unit = extern

  def SDL_PollEvent(event: Ptr[SDL_Event]): CInt = extern

  def SDL_GL_SetAttribute(attr: CInt, value: CInt): CInt = extern
  def SDL_GL_CreateContext(window: Ptr[SDL_Window]): Ptr[Byte] = extern
  def SDL_GL_SwapWindow(window: Ptr[SDL_Window]): Unit = extern
}

@extern
object SDLError {
  @name("SDL_GetError")
  def SDL_GetError(): CString = extern
}

object GameWindow {
  val WINDOW_WIDTH = 800
  val WINDOW_HEIGHT = 800

  // Key scancodes
  val SC_W = 26
  val SC_A = 4
  val SC_S = 22
  val SC_D = 7
  val SC_SPACE = 44
  val SC_ESC = 41

  // Flight sim controls
  val SC_Q = 20
  val SC_E = 8
  val SC_UP = 82
  val SC_DOWN = 81
  val SC_LEFT = 80
  val SC_RIGHT = 79
  val SC_SHIFT = 225
  val SC_CTRL = 224
  val SC_Z = 29
  val SC_X = 27
  val SC_PLUS = 46   // Usually '=' key
  val SC_MINUS = 45
  val SC_F = 9
  val SC_B = 5

  def clamp(value: Float, min: Float, max: Float): Float =
    Math.max(min, Math.min(max, value)).toFloat

  def main(args: Array[String]): Unit = {
    println("[DEBUG] Starting Jet Fighter Game 3D...")

    Zone { implicit z =>
      if (SDL.SDL_Init(SDLConsts.SDL_INIT_VIDEO) != 0) {
        println("[ERROR] SDL_Init failed: " + fromCString(SDLError.SDL_GetError()))
        stdlib.exit(1)
      }

      SDL.SDL_GL_SetAttribute(SDLConsts.SDL_GL_CONTEXT_MAJOR_VERSION, 2)
      SDL.SDL_GL_SetAttribute(SDLConsts.SDL_GL_CONTEXT_MINOR_VERSION, 1)

      val window = SDL.SDL_CreateWindow(
        toCString("Jet Fighter Game 3D"),
        SDLConsts.SDL_WINDOWPOS_CENTERED, SDLConsts.SDL_WINDOWPOS_CENTERED,
        WINDOW_WIDTH, WINDOW_HEIGHT,
        SDLConsts.SDL_WINDOW_RESIZABLE | SDLConsts.SDL_WINDOW_OPENGL
      )
      if (window == null) {
        println("[ERROR] SDL_CreateWindow failed: " + fromCString(SDLError.SDL_GetError()))
        SDL.SDL_Quit()
        stdlib.exit(1)
      }

      val glContext = SDL.SDL_GL_CreateContext(window)
      if (glContext == null) {
        println("[ERROR] SDL_GL_CreateContext failed: " + fromCString(SDLError.SDL_GetError()))
        SDL.SDL_DestroyWindow(window)
        SDL.SDL_Quit()
        stdlib.exit(1)
      }

      val eventRaw = stackalloc[Byte](56)
      var running = true

      // Controls state
      var wPressed, aPressed, sPressed, dPressed = false
      var spacePressed = false
      var qPressed, ePressed = false
      var upPressed, downPressed, leftPressed, rightPressed = false
      var shiftPressed, ctrlPressed = false
      var zPressed, xPressed = false
      var plusPressed, minusPressed = false
      var afterburnerToggled = false
      var airbrakeToggled = false

      // Player jet
      val playerJet = new FighterJet()
      playerJet.position = Vec3(0f, 0f, 0.7f)
      playerJet.yaw = 0f
      playerJet.pitch = 0f
      playerJet.roll = 0f

      // Bot jet
      val botJet = new BotJet3D(Vec3(0.6f, 0.6f, -0.7f), math.Pi.toFloat / 2)

      // Bullets
      var bullets = List.empty[Bullet3D]
      var playerCanFire = true
      var playerFireCooldown = 0f

      // Bot firing AI
      var botFireCooldown = 0f
      val botFireInterval = 0.6f

      val frameDelayMs = 16
      var lastTime = System.currentTimeMillis()

      var playerAlive = true
      var botAlive = true

      while (running) {
        val currentTime = System.currentTimeMillis()
        val dt = ((currentTime - lastTime).toFloat / 1000f).max(0.016f).min(0.1f)
        lastTime = currentTime

        // --- Event Polling ---
        while (SDL.SDL_PollEvent(eventRaw.asInstanceOf[Ptr[SDL.SDL_Event]]) != 0) {
          val eventType = !(eventRaw.asInstanceOf[Ptr[CUnsignedInt]])
          if (eventType == SDLConsts.SDL_QUIT) {
            running = false
          }
          else if (eventType == SDLConsts.SDL_KEYDOWN || eventType == SDLConsts.SDL_KEYUP) {
            val keysymBase = eventRaw + 16
            val scancode = (!(keysymBase.asInstanceOf[Ptr[CUnsignedInt]])).toInt
            val pressed = eventType == SDLConsts.SDL_KEYDOWN
            scancode match {
              case SC_W     => wPressed = pressed
              case SC_A     => aPressed = pressed
              case SC_S     => sPressed = pressed
              case SC_D     => dPressed = pressed
              case SC_UP    => upPressed = pressed
              case SC_DOWN  => downPressed = pressed
              case SC_LEFT  => leftPressed = pressed
              case SC_RIGHT => rightPressed = pressed
              case SC_Q     => qPressed = pressed
              case SC_E     => ePressed = pressed
              case SC_SHIFT => shiftPressed = pressed
              case SC_CTRL  => ctrlPressed = pressed
              case SC_Z     => zPressed = pressed
              case SC_X     => xPressed = pressed
              case SC_PLUS  => plusPressed = pressed
              case SC_MINUS => minusPressed = pressed
              case SC_F     => if (pressed) afterburnerToggled = !afterburnerToggled
              case SC_B     => if (pressed) airbrakeToggled = !airbrakeToggled
              case SC_SPACE => spacePressed = pressed
              case SC_ESC   => if (pressed) running = false
              case _        =>
            }
          }
        }

        // --- Player movement ---
        if (playerAlive) {
          val pitchInput =
            (if (wPressed || upPressed) -1f else 0f) +
            (if (sPressed || downPressed) 1f else 0f)
          val rollInput =
            (if (aPressed || leftPressed) -1f else 0f) +
            (if (dPressed || rightPressed) 1f else 0f)
          val yawInput =
            (if (qPressed) -1f else 0f) +
            (if (ePressed) 1f else 0f)
          val throttleInput =
            (if (shiftPressed || zPressed || plusPressed) 1f else 0f) +
            (if (ctrlPressed || xPressed || minusPressed) -1f else 0f)

          val jetInput = JetInput(
            pitch = pitchInput,
            yaw = yawInput,
            roll = rollInput,
            thrust = throttleInput,
            afterburner = afterburnerToggled || shiftPressed,
            airbrake = airbrakeToggled || ctrlPressed
          )
          playerJet.update(jetInput, dt)
          playerJet.position.x = clamp(playerJet.position.x, -0.95f, 0.95f)
          playerJet.position.y = clamp(playerJet.position.y, -0.95f, 0.95f)
          playerJet.position.z = clamp(playerJet.position.z, -0.95f, 0.95f)
        }

        // --- Player fire bullets with cooldown ---
        if (playerAlive && spacePressed && playerCanFire) {
          val bulletDir = playerJet.forwardVector
          val bulletPos = playerJet.position + bulletDir * 0.13f
          bullets = Bullet3D(bulletPos, bulletDir, 2.0f, alive = true, owner = Player) :: bullets
          playerCanFire = false
          playerFireCooldown = 0.25f
        }
        if (!playerCanFire) {
          playerFireCooldown -= dt
          if (playerFireCooldown <= 0f) playerCanFire = true
        }

        // --- Update bullets ---
        bullets.foreach(_.update(dt))
        bullets = bullets.filter(_.alive)

        // --- Update bot ---
        if (botAlive) botJet.update(dt, playerJet.position)

        // --- Bot AI: fire at player ---
        if (botAlive && playerAlive) {
          botFireCooldown -= dt
          if (botFireCooldown <= 0f) {
            val toPlayer = (playerJet.position - botJet.position).normalized
            val botFacing = botJet.forwardVector
            val angleDiff = math.acos(botFacing.dot(toPlayer).max(-1f).min(1f)).abs
            val fireThreshold = math.Pi / 5
            if (angleDiff < fireThreshold) {
              val bulletDir = botJet.forwardVector
              val bulletPos = botJet.position + bulletDir * 0.13f
              bullets = Bullet3D(bulletPos, bulletDir, 2.0f, alive = true, owner = Bot) :: bullets
              botFireCooldown = botFireInterval
            }
          }
        }

        // --- Bullet-bot collision ---
        if (botAlive) {
          bullets.find { b =>
            b.owner == Player && b.position.distance(botJet.position) < 0.07f
          }.foreach { hit =>
            botAlive = false
            hit.alive = false
            println("[DEBUG] Bot hit! You win!")
          }
        }

        // --- Bullet-player collision ---
        if (playerAlive) {
          bullets.find { b =>
            b.owner == Bot && b.position.distance(playerJet.position) < 0.07f
          }.foreach { hit =>
            playerAlive = false
            hit.alive = false
            println("[DEBUG] Player hit by bot bullet! Game Over!")
          }
        }

        // --- Bot Jet - Player Jet collision ---
        if (playerAlive && botAlive) {
          if (playerJet.position.distance(botJet.position) < 0.11f) {
            playerAlive = false
            botAlive = false
            println("[DEBUG] Player and Bot jets collided! Both eliminated!")
          }
        }

        // --- Camera (chase cam) ---
        GL3D.setupChaseCameraView(
          WINDOW_WIDTH,
          WINDOW_HEIGHT,
          playerJet.position,
          playerJet.yaw,
          playerJet.pitch
        )

        GL.glClearColor(0f, 0.0f, 0.15f, 1f)
        GL.glClear(GLConsts.GL_COLOR_BUFFER_BIT | GLConsts.GL_DEPTH_BUFFER_BIT)
        GL.glEnable(GLConsts.GL_DEPTH_TEST)

        // --- Draw cube environment ---
        CubeRenderer.drawCube()

        // --- Draw bot jet ---
        if (botAlive)
          JetRenderer3D.drawJet(botJet.position, botJet.yaw, botJet.pitch, 0f, (0.8f, 0.2f, 0.2f), false, 0f)

        // --- Draw player jet ---
        if (playerAlive)
          JetRenderer3D.drawJet(playerJet.position, playerJet.yaw, playerJet.pitch, 0f, (0.2f, 0.8f, 0.2f), true, 0f)

        // --- Draw bullets (as spheres) ---
        bullets.foreach { bul =>
          val color = bul.owner match {
            case Player => (1f, 0.9f, 0.2f)
            case Bot    => (1f, 0.4f, 0.4f)
          }
          BulletRenderer.drawSphere(bul.position, 0.04f, color)
        }

        SDL.SDL_GL_SwapWindow(window)
        Thread.sleep(frameDelayMs)
      }

      SDL.SDL_DestroyWindow(window)
      SDL.SDL_Quit()
    }
  }
}
