package com.ikarimeister.loginapp.ui.activities

import com.ikarimeister.loginapp.domain.usecases.Logout
import io.mockk.impl.annotations.MockK

class NavigationLoginMainTest {

    @MockK
    lateinit var logoutMock: Logout
}