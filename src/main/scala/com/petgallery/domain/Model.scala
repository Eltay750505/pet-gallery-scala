package com.petgallery.domain

import java.time.Instant
import java.util.UUID

case class UserId(value: UUID)

enum Visibility:
  case Public, FollowersOnly, Private

case class User(
    id: UserId,
    username: String,
    email: String,
    bio: Option[String],
    registeredAt: Instant
)

case class PhotoId(value: UUID)

case class Photo(
    id: PhotoId,
    owner: UserId,
    caption: Option[String],
    path: String,
    visibility: Visibility,
    createdAt: Instant
)

case class CommentId(value: UUID)

case class Comment(
    id: CommentId,
    photo: PhotoId,
    author: UserId,
    text: String,
    createdAt: Instant
)

case class Like(
    user: UserId,
    photo: PhotoId,
    createdAt: Instant
)

case class Follow(
    follower: UserId,
    followee: UserId,
    createdAt: Instant
)

case class ShareId(value: UUID)

case class Share(
    id: ShareId,
    from: UserId,
    to: UserId,
    photo: PhotoId,
    message: Option[String],
    createdAt: Instant
)

def canView(photo: Photo, viewer: UserId, viewerFollowsOwner: Boolean): Boolean =
  if viewer == photo.owner then true
  else
    photo.visibility match
      case Visibility.Public        => true
      case Visibility.FollowersOnly => viewerFollowsOwner
      case Visibility.Private       => false
