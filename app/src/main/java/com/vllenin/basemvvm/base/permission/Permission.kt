package com.vllenin.basemvvm.base.permission

data class Permission constructor(
  val permission: String,
  val granted: Boolean,
  val preventAskAgain: Boolean
)
