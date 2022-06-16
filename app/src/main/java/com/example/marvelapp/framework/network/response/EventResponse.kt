package com.example.marvelapp.framework.network.response

import com.google.gson.annotations.SerializedName
import com.igaopk10.core.domain.model.Event

data class EventResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("thumbnail")
    val thumbNail: ThumbnailResponse
)

fun EventResponse.toEventModel(): Event {
    return Event(
        id = id,
        imageURL = this.thumbNail.getHttpsURL()
    )
}
