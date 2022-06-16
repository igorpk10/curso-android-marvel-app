package com.example.marvelapp.factory.response

import com.example.marvelapp.framework.network.response.CharacterResponse
import com.example.marvelapp.framework.network.response.DataContainerResponse
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.network.response.ThumbnailResponse

class DataWrapperResponseFactory {

    fun create() = DataWrapperResponse(
        copyright = "",
        data = DataContainerResponse(
            offset = 0,
            total = 1,
            results = listOf(
                CharacterResponse(
                    id = 10113344,
                    name = "One Punch Man",
                    thumbNail =  ThumbnailResponse(
                        path = "http://igaopk10.com.br/onepunchman",
                        extension = "jpg"
                    )
                )
            )

        )
    )
}