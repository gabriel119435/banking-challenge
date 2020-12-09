package com.integration

import com.util.*
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HistoryControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `findById answers 400 on bad account`() {
        mockMvc.perform(MockMvcRequestBuilders.get(HISTORY_URL + ID_URL, 12345).addBasicAuthHeader())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath(ERROR_KEY).value(ACCOUNT_DOESNT_EXIST))
    }

    @Test
    fun `findById answers 200 on simple history`() {
        mockMvc.perform(MockMvcRequestBuilders.get(HISTORY_URL + ID_URL, 1).addBasicAuthHeader())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value("30"))
            .andExpect(MockMvcResultMatchers.jsonPath(TRANSACTIONS_KEY).doesNotHaveJsonPath())
    }

    @Test
    fun `findById answers 200 on 1 days range history`() {
        mockMvc.perform(MockMvcRequestBuilders.get(HISTORY_URL + ID_URL, 1).param("days", "1").addBasicAuthHeader())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath(BALANCE_BALANCE_KEY).value("30"))
            .andExpect(MockMvcResultMatchers.jsonPath(TRANSACTIONS_KEY, hasSize<Array<Any>>(1)))
    }

    @Test
    fun `findById answers 200 on 10 days range history`() {
        mockMvc.perform(MockMvcRequestBuilders.get(HISTORY_URL + ID_URL, 1).param("days", "10").addBasicAuthHeader())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath(BALANCE_BALANCE_KEY).value("30"))
            .andExpect(MockMvcResultMatchers.jsonPath(TRANSACTIONS_KEY, hasSize<Array<Any>>(2)))
    }

    @Test
    fun `findById answers 200 on 50 days range history`() {
        mockMvc.perform(MockMvcRequestBuilders.get(HISTORY_URL + ID_URL, 1).param("days", "50").addBasicAuthHeader())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath(BALANCE_BALANCE_KEY).value("30"))
            .andExpect(MockMvcResultMatchers.jsonPath(TRANSACTIONS_KEY, hasSize<Array<Any>>(3)))
    }
}