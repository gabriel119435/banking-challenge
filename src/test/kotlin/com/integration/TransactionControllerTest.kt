package com.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.model.Transaction
import com.util.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDateTime

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    val mapper = ObjectMapper()

    @Test
    fun `save returns 400 on null origin and destination`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(TRANSACTION_URL).contentType(APPLICATION_JSON_VALUE).addBasicAuthHeader()
                .content(
                    mapper.writeValueAsString(
                        Transaction(
                            origin = null, destination = null, amount = BigDecimal(10), createdAt = LocalDateTime.now()
                        )
                    )
                )
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath(ERROR_KEY).value(ORIGIN_DEST_NULL))
    }

    @Test
    fun `save returns 400 on bad origin`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(TRANSACTION_URL).contentType(APPLICATION_JSON_VALUE).addBasicAuthHeader()
                .content(
                    mapper.writeValueAsString(
                        Transaction(
                            origin = 12345, destination = 1, amount = BigDecimal(10), createdAt = LocalDateTime.now()
                        )
                    )
                )
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath(ERROR_KEY).value(ORIGIN_DOESNT_EXIST))
    }

    @Test
    fun `save returns 400 on bad destination`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(TRANSACTION_URL).contentType(APPLICATION_JSON_VALUE).addBasicAuthHeader()
                .content(
                    mapper.writeValueAsString(
                        Transaction(
                            origin = 1, destination = 12345, amount = BigDecimal(10), createdAt = LocalDateTime.now()
                        )
                    )
                )
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath(ERROR_KEY).value(DESTINATION_DOESNT_EXIST))
    }

    @Test
    fun `save returns 400 on insufficient funds`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(TRANSACTION_URL).contentType(APPLICATION_JSON_VALUE).addBasicAuthHeader()
                .content(
                    mapper.writeValueAsString(
                        Transaction(
                            origin = 1, destination = null, amount = BigDecimal(30), createdAt = LocalDateTime.now()
                        )
                    )
                )
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath(ERROR_KEY).value(INSUFFICIENT_FUNDS))
    }
}