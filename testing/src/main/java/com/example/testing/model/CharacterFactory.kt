package com.example.testing.model

import com.igaopk10.core.domain.model.Character

class CharacterFactory {

    fun create(hero: Hero): Character = when (hero) {
        Hero.OnePunchMan -> Character(
            name = "One Punch Man",
            imageUrl = "https://igaopk10.com.br/onepunchman.jpg"
        )
    }

    sealed class Hero {
        object OnePunchMan : Hero()
    }
}