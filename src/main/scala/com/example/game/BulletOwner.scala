package com.example.game

sealed trait BulletOwner
case object Player extends BulletOwner
case object Bot extends BulletOwner