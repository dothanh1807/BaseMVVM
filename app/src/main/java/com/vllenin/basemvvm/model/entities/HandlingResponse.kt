package com.vllenin.basemvvm.model.entities

/**
 * Created by Vllenin on 3/26/21.
 */
data class HandlingResponse<D> (
    val status: Status,

    val data: D?
)