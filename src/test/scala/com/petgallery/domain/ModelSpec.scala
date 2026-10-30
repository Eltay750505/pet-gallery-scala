package com.petgallery.domain

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ModelSpec extends AnyFlatSpec with Matchers:

  "canView" should "allow the owner to view any photo" in pending

  it should "allow anyone to view a public photo" in pending

  it should "allow a follower to view a followers-only photo" in pending

  it should "not allow a stranger to view a followers-only photo" in pending

  it should "not allow a follower to view a private photo" in pending

  "Ids" should "be equal when they wrap the same UUID" in pending

  "Photo.copy" should "change only the given field" in pending
