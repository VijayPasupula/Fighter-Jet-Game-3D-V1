package com.example.game

class FighterJet {
  var position = Vec3(0f, 0f, 0f)
  var yaw = 0f
  var pitch = 0f
  var roll = 0f
  var speed = 0.33f

  // Movement vector updated each frame
  var velocity = Vec3(0f, 0f, 0f)

  // Afterburner and airbrake states
  var afterburner = false
  var airbrake = false

  def forwardVector: Vec3 = {
    val forwardX = math.sin(yaw).toFloat * math.cos(pitch).toFloat
    val forwardY = -math.sin(pitch).toFloat
    val forwardZ = math.cos(yaw).toFloat * math.cos(pitch).toFloat
    Vec3(forwardX, forwardY, forwardZ).normalized
  }

  def update(input: JetInput, dt: Float): Unit = {
    // Integrate roll, yaw, and pitch
    roll += input.roll * dt * 2.2f
    yaw += (input.yaw + math.sin(roll).toFloat * 0.7f) * dt * 1.4f
    pitch += input.pitch * dt * 1.2f

    // Clamp pitch and roll for realism
    pitch = math.max(-1.2f, math.min(1.2f, pitch))
    roll = math.max(-1.6f, math.min(1.6f, roll))

    // Throttle/speed logic
    val baseSpeed = 0.45f
    var targetSpeed = baseSpeed + input.thrust * 0.35f
    if (input.afterburner) targetSpeed += 0.45f
    if (input.airbrake) targetSpeed -= 0.25f
    targetSpeed = math.max(0.12f, math.min(targetSpeed, 1.45f))
    speed += (targetSpeed - speed) * (if (input.afterburner) 0.25f else 0.13f)
    speed = math.max(0.01f, math.min(speed, 1.45f))

    // Move forward
    position = position + forwardVector * speed * dt
  }
}

case class JetInput(
  pitch: Float,     // +: nose down, -: nose up
  yaw: Float,       // +: right, -: left
  roll: Float,      // +: right, -: left
  thrust: Float,    // +: speed up, -: slow down (throttle)
  afterburner: Boolean = false,
  airbrake: Boolean = false
)