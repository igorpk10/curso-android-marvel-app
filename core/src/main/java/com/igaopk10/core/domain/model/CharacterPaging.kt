package com.igaopk10.core.domain.model

data class CharacterPaging(
    val offset: Int,
    val total: Int,
    val character: List<Character>
)
