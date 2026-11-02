package com.petgallery.repository

import cats.effect.unsafe.implicits.global
import com.petgallery.domain.{Follow, UserId}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.Instant
import java.util.UUID

class InMemoryFollowRepositorySpec extends AnyFlatSpec with Matchers:

  private val at = Instant.now()

  private val owner = UserId(UUID.randomUUID())
  private val stranger = UserId(UUID.randomUUID())
  private val follower = UserId(UUID.randomUUID())

  "InMemoryFollowRepository" should "start empty" in {
    val result =
      for
        repo <- InMemoryFollowRepository.create
        following <- repo.isFollowing(follower, owner)
      yield following

    result.unsafeRunSync() shouldBe false
  }

  it should "report a follow after it is added" in {
    val result =
      for
        repo <- InMemoryFollowRepository.create
        _ <- repo.add(Follow(follower, owner, at))
        following <- repo.isFollowing(follower, owner)
      yield following

    result.unsafeRunSync() shouldBe true
  }

  it should "not report a follow going the other way" in {
    val result =
      for
        repo <- InMemoryFollowRepository.create
        _ <- repo.add(Follow(follower, owner, at))
        following <- repo.isFollowing(owner, follower)
      yield following

    result.unsafeRunSync() shouldBe false
  }

  it should "not report a follow after it is removed" in {
    val result =
      for
        repo <- InMemoryFollowRepository.create
        _ <- repo.add(Follow(follower, owner, at))
        _ <- repo.remove(follower, owner)
        following <- repo.isFollowing(follower, owner)
      yield following

    result.unsafeRunSync() shouldBe false
  }

  it should "keep other follows when one is removed" in {
    val result =
      for
        repo <- InMemoryFollowRepository.create
        _ <- repo.add(Follow(follower, owner, at))
        _ <- repo.add(Follow(stranger, owner, at))
        _ <- repo.remove(follower, owner)
        following <- repo.isFollowing(stranger, owner)
      yield following

    result.unsafeRunSync() shouldBe true
  }

  it should "do nothing when removing a follow that does not exist" in {
    val result =
      for
        repo <- InMemoryFollowRepository.create
        _ <- repo.add(Follow(follower, owner, at))
        _ <- repo.remove(stranger, owner)
        following <- repo.isFollowing(follower, owner)
      yield following

    result.unsafeRunSync() shouldBe true
  }

  it should "keep repositories independent" in {
    val result =
      for
        first <- InMemoryFollowRepository.create
        second <- InMemoryFollowRepository.create
        _ <- first.add(Follow(follower, owner, at))
        following <- second.isFollowing(follower, owner)
      yield following

    result.unsafeRunSync() shouldBe false
  }
