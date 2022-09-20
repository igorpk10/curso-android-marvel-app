package com.example.marvelapp.presentation.characters

import okhttp3.mockwebserver.MockWebServer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marvelapp.R
import com.example.marvelapp.extension.asJsonString
import com.example.marvelapp.framework.di.BaseURLModule
import com.example.marvelapp.framework.di.CoroutinesModule
import com.example.marvelapp.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(BaseURLModule::class, CoroutinesModule::class)
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CharactersFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var server: MockWebServer

    @Before
    fun setup() {
        server = MockWebServer().apply {
            start(8080)
        }
        launchFragmentInHiltContainer<CharactersFragment>()
    }

    @Test
    fun shouldShowCharactersWhenViewIsCreated() {
        server.enqueue(MockResponse().setBody("characters_p1.json".asJsonString()))
        onView(withId(R.id.recycler_characters))
            .check(matches(isDisplayed()))
    }

//    CORRIGIR ESSA MERDA DEPOIS
//    @Test
//    fun shouldLoadMoreCharactersWhenNewPageIsRequested() {
//        server.enqueue(MockResponse().setBody("characters_p1.json".asJsonString()))
//        server.enqueue(MockResponse().setBody("characters_p2.json".asJsonString()))
//
//
//        onView(withId(R.id.recycler_characters))
//            .check(matches(isDisplayed()))
//            .perform(RecyclerViewActions.scrollToPosition<CharactersLoadMoreStateViewHolder>(20))
//
//        onView(withText("Amora")).check(matches(isDisplayed()))
//    }


    fun shouldShowErrorViewWhenReceiveErrorByApi() {
        //Arrange
        server.enqueue(MockResponse().setResponseCode(404))

        //Action
        onView(
            withId(R.id.text_initial_loading_error)
        ).check(matches(isDisplayed()))

        //Assert
    }


    @After
    fun tearDown() {
        server.shutdown()
    }

}