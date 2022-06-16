package com.example.marvelapp.framework.network.response

import com.google.gson.annotations.SerializedName
import com.igaopk10.core.domain.model.Comic

data class ComicResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("thumbnail")
    val thumbNail: ThumbnailResponse
)


fun ComicResponse.toComicModel(): Comic {
    return Comic(
        id = id,
        imageURL = this.thumbNail.getHttpsURL()
    )
}
