package com.petgallery.repository

import cats.effect.{IO, Ref}
import com.petgallery.domain.{Follow, UserId, unfollow, isFollowing as followExists}

class InMemoryFollowRepository(follows: Ref[IO, List[Follow]]) extends FollowRepository:

  override def isFollowing(follower: UserId, followee: UserId): IO[Boolean] =
    follows.get.map(list => followExists(list, follower, followee))

  override def add(follow: Follow): IO[Unit] =
    follows.update(list => list.appended(follow))

  override def remove(follower: UserId, followee: UserId): IO[Unit] =
    follows.update(list => unfollow(list, follower, followee))

object InMemoryFollowRepository:
  def create: IO[InMemoryFollowRepository] =
    Ref.of[IO, List[Follow]](List.empty).map(ref => InMemoryFollowRepository(ref))
