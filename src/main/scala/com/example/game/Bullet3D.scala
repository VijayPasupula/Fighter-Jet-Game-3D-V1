package com.example.game

case class Bullet3D(
  var position: Vec3,
  var direction: Vec3,
  val speed: Float = 1.5f,
  var alive: Boolean = true,
  owner: BulletOwner = Player
) {
  def update(dt: Float): Unit = {
    position = position + (direction * speed * dt)
    if (math.abs(position.x) > 1.1f || math.abs(position.y) > 1.1f || math.abs(position.z) > 1.1f) alive = false
  }
}