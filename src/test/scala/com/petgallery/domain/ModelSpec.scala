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

  "Ids" should "be equal when they wrap the same UUID" in {
    val uuid = UUID.randomUUID()

    val firstUser = UserId(uuid)
    val secondUser = UserId(uuid)
    val thirdUser = UserId(UUID.randomUUID())

    firstUser == secondUser shouldBe true
    firstUser == thirdUser shouldBe false
    secondUser == thirdUser shouldBe false
  }

  "Photo.copy" should "change only the given field" in {
    val photo = buildPhoto(Private)
    val newPhoto = photo.copy(visibility = Visibility.Public)

    photo == newPhoto shouldBe false
    newPhoto.visibility == Public shouldBe true
    newPhoto.copy(visibility = Private) == photo shouldBe true
  }

  private def buildPhoto(visibility: Visibility): Photo =
    Photo(PhotoId(UUID.randomUUID()), owner, Some("My cat"), "cat.jpg", visibility, Instant.now())