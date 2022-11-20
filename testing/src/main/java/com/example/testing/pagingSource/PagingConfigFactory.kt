package com.example.testing.pagingSource

import androidx.paging.PagingConfig

class PagingConfigFactory {

    fun build(): PagingConfig  = PagingConfig(
        pageSize = 20
    )
}