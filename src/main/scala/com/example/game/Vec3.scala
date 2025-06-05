package com.example.game

case class Vec3(var x: Float, var y: Float, var z: Float) {
  def +(that: Vec3): Vec3 = Vec3(this.x + that.x, this.y + that.y, this.z + that.z)
  def -(that: Vec3): Vec3 = Vec3(this.x - that.x, this.y - that.y, this.z - that.z)
  def *(scalar: Float): Vec3 = Vec3(this.x * scalar, this.y * scalar, this.z * scalar)
  def length: Float = math.sqrt(x * x + y * y + z * z).toFloat
  def normalized: Vec3 = {
    val len = length
    if (len == 0f) Vec3(0, 0, 0) else Vec3(x / len, y / len, z / len)
  }
  def dot(v: Vec3): Float = x * v.x + y * v.y + z * v.z
  def distance(v: Vec3): Float = (this - v).length
}