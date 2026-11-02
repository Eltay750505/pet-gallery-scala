package com.petgallery.repository

import cats.effect.IO
import com.petgallery.domain.{Follow, UserId}

trait FollowRepository:
  def isFollowing(follower: UserId, followee: UserId): IO[Boolean]
  def add(follow: Follow): IO[Unit]
  def remove(follower: UserId, followee: UserId): IO[Unit]
