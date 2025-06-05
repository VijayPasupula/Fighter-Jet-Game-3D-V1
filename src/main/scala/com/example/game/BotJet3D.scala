package com.example.game

import scala.util.Random

class BotJet3D(var position: Vec3, var yaw: Float) {
  var pitch: Float = 0f
  var speed: Float = 0.22f + Random.nextFloat() * 0.09f
  var moveTimer: Float = 0f
  var targetYaw: Float = 0f
  var targetPitch: Float = 0f

  def update(dt: Float, playerPos: Vec3): Unit = {
    // Move slightly toward player, but with random wander
    val toPlayer = (playerPos - position).normalized
    moveTimer -= dt
    if (moveTimer <= 0f) {
      // Slightly randomize direction but bias toward player
      targetYaw = math.atan2(toPlayer.x, -toPlayer.z).toFloat + Random.between(-0.7, 0.7).toFloat
      targetPitch = math.asin(-toPlayer.y).toFloat + Random.between(-0.3, 0.3).toFloat
      moveTimer = 0.8f + Random.nextFloat() * 0.7f
    }
    // Interpolate to target yaw/pitch
    yaw += (targetYaw - yaw) * 0.02f
    pitch += (targetPitch - pitch) * 0.02f

    // Move forward
    val dir = forwardVector
    position = position + dir * speed * dt

    // Bounce off cube walls
    if (position.x < -0.98f || position.x > 0.98f) {
      yaw = math.Pi.toFloat - yaw
      position.x = math.max(math.min(position.x, 0.98f), -0.98f)
    }
    if (position.y < -0.98f || position.y > 0.98f) {
      pitch = -pitch
      position.y = math.max(math.min(position.y, 0.98f), -0.98f)
    }
    if (position.z < -0.98f || position.z > 0.98f) {
      yaw += math.Pi.toFloat
      position.z = math.max(math.min(position.z, 0.98f), -0.98f)
    }
  }

  def forwardVector: Vec3 = {
    val forwardX = math.sin(yaw).toFloat * math.cos(pitch).toFloat
    val forwardY = -math.sin(pitch).toFloat
    val forwardZ = math.cos(yaw).toFloat * math.cos(pitch).toFloat
    Vec3(forwardX, forwardY, forwardZ).normalized
  }
}