package com.petgallery.domain

import com.petgallery.domain.Visibility.{FollowersOnly, Private, Public}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.Instant
import java.util.UUID

class ModelSpec extends AnyFlatSpec with Matchers:

  private val owner = UserId(UUID.randomUUID())
  private val stranger = UserId(UUID.randomUUID())
  private val follower = UserId(UUID.randomUUID())

  private val follows = List(Follow(follower, owner, Instant.now()))

  "canView" should "allow the owner to view any photo" in {
    val photo = buildPhoto(Private)

    canView(photo, owner, viewerFollowsOwner = false) shouldBe true
  }

  it should "allow anyone to view a public photo" in {
    val photo = buildPhoto(Public)

    canView(photo, stranger, viewerFollowsOwner = false) shouldBe true
  }

  it should "allow a follower to view a followers-only photo" in {
    val photo = buildPhoto(FollowersOnly)

    canView(photo, follower, viewerFollowsOwner = true) shouldBe true
  }

  it should "not allow a stranger to view a followers-only photo" in {
    val photo = buildPhoto(FollowersOnly)

    canView(photo, stranger, viewerFollowsOwner = false) shouldBe false
  }

  it should "not allow a follower to view a private photo" in {
    val photo = buildPhoto(Private)

    canView(photo, follower, viewerFollowsOwner = true) shouldBe false
  }

  "isFollowing" should "return true when the follower follows the followee" in {
    isFollowing(follows, follower, owner) shouldBe true
  }

  it should "return false for an empty list" in {
    isFollowing(List.empty, follower, owner) shouldBe false
  }

  it should "return false when the follow goes the other way" in {
    isFollowing(follows, owner, follower) shouldBe false
  }

  it should "return false when the follower follows someone else" in {
    isFollowing(follows, follower, stranger) shouldBe false
  }

  it should "return false when someone else follows the followee" in {
    isFollowing(follows, stranger, owner) shouldBe false
  }

  "feed" should "include the viewer's own private photo" in {
    val photo = buildPhoto(Private)

    feed(owner, List(photo), follows) shouldBe List(photo)
  }

  it should "include a followers-only photo of someone the viewer follows" in {
    val photo = buildPhoto(FollowersOnly)

    feed(follower, List(photo), follows) shouldBe List(photo)
  }

  it should "not include a public photo of someone the viewer does not follow" in {
    val photo = buildPhoto(Public)

    feed(stranger, List(photo), follows) shouldBe List()
  }

  it should "show newer photos first" in {
    val oldest = buildPhoto(Public, Instant.parse("2026-11-01T10:00:00Z"))
    val middle = buildPhoto(Public, Instant.parse("2026-11-01T11:00:00Z"))
    val newest = buildPhoto(Public, Instant.parse("2026-11-01T12:00:00Z"))

    feed(follower, List(middle, oldest, newest), follows) shouldBe List(newest, middle, oldest)
  }

  "Ids" should "be equal when they wrap the same UUID" in {
    val uuid = UUID.randomUUID()

    val firstUser = UserId(uuid)
    val secondUser = UserId(uuid)
    val thirdUser = UserId(UUID.randomUUID())

    firstUser shouldBe secondUser
    firstUser should not be thirdUser
  }

  "Photo.copy" should "change only the given field" in {
    val photo = buildPhoto(Private)
    val newPhoto = photo.copy(visibility = Public)

    newPhoto should not be photo
    newPhoto.visibility shouldBe Public
    newPhoto.copy(visibility = Private) shouldBe photo
  }

  private def buildPhoto(visibility: Visibility, createdAt: Instant = Instant.now()): Photo =
    Photo(PhotoId(UUID.randomUUID()), owner, Some("My cat"), "cat.jpg", visibility, createdAt)