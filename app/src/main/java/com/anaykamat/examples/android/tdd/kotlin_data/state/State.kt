package com.anaykamat.examples.android.tdd.kotlin_data.state

import io.reactivex.Observable
import com.anaykamat.examples.android.tdd.screens.HomeScreen
import com.anaykamat.examples.android.tdd.screens.Screen
import com.anaykamat.examples.android.tdd.screens.SplashScreen

/**
 * Created by anay on 08/08/18.
 */
data class State(val currentScreen: Screen = SplashScreen(),
                 val splashScreen: SplashScreen = SplashScreen(),
                 val homeScreen: HomeScreen = HomeScreen(),
                 val dataLoaded:Boolean = false,
                 val actions: Observable<Action> = Observable.fromIterable(emptyList<Action>()))