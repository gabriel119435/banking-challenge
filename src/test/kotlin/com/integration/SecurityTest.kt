package com.integration

import com.util.PEOPLE_URL
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `any url receives 401 without headers`() {
        mockMvc.perform(MockMvcRequestBuilders.get(PEOPLE_URL))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }
}