package com.petgallery

import cats.effect.{IO, IOApp}
import com.petgallery.domain.*

import java.time.Instant
import java.util.UUID

object Main extends IOApp.Simple:

  override def run: IO[Unit] =
    val owner = User(UserId(UUID.randomUUID()), "alice", "alice@example.com", None, Instant.now())
    val viewer = UserId(UUID.randomUUID())
    val photo = Photo(PhotoId(UUID.randomUUID()), owner.id, Some("My cat"), "cat.jpg", Visibility.FollowersOnly, Instant.now())

    for
      _ <- IO.println(owner)
      _ <- IO.println(photo)
      _ <- IO.println(s"Owner can view: ${canView(photo, owner.id, viewerFollowsOwner = false)}")
      _ <- IO.println(s"Stranger can view: ${canView(photo, viewer, viewerFollowsOwner = false)}")
    yield ()
