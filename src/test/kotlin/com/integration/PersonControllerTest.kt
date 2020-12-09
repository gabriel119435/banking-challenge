package com.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.model.Person
import com.util.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

// https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-testing-spring-boot-applications-testing-with-mock-environment

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PersonControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    val mapper = ObjectMapper()

    @Test
    fun `findAll returns same as data sql`() {
        mockMvc.perform(get(PEOPLE_URL).addBasicAuthHeader())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("a test"))
            .andExpect(jsonPath("$[0].cpf").value("73478504872"))
            .andExpect(jsonPath("$[1].name").value("b test"))
            .andExpect(jsonPath("$[1].cpf").value("57683703745"))
    }

    @Test
    fun `findByCpf answers 400 on invalid cpf`() {
        mockMvc.perform(get(PEOPLE_URL + CPF_URL, 1).addBasicAuthHeader())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath(ERROR_KEY).value(INVALID_CPF))
    }

    @Test
    fun `findByCpf answers 204 on not registered valid cpf`() {
        mockMvc.perform(get(PEOPLE_URL + CPF_URL, "74138265007").addBasicAuthHeader())
            .andExpect(status().isNoContent)
            .andExpect(jsonPath(ROOT_KEY).doesNotExist())
    }

    @Test
    fun `findByCpf answers 200 on registered valid cpf`() {
        mockMvc.perform(get(PEOPLE_URL + CPF_URL, "73478504872").addBasicAuthHeader())
            .andExpect(status().isOk)
            .andExpect(jsonPath(NAME_KEY).value("a test"))
            .andExpect(jsonPath(CPF_KEY).value("73478504872"))
    }

    @Test
    fun `save answers 201 on brand new created user`() {
        mockMvc.perform(
            post(PEOPLE_URL).contentType(APPLICATION_JSON_VALUE).addBasicAuthHeader()
                .content(mapper.writeValueAsString(Person(name = "c test", cpf = "16026467408")))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath(NAME_KEY).value("c test"))
            .andExpect(jsonPath(CPF_KEY).value("16026467408"))
    }

    @Test
    fun `save answers 400 on already registered user`() {
        mockMvc.perform(
            post(PEOPLE_URL).contentType(APPLICATION_JSON_VALUE).addBasicAuthHeader()
                .content(mapper.writeValueAsString(Person(name = "a test new", cpf = "73478504872")))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath(ERROR_KEY).value(CPF_ALREADY_REGISTERED))
    }

    @Test
    fun `save answers 400 on bad cpf`() {
        mockMvc.perform(
            post(PEOPLE_URL).contentType(APPLICATION_JSON_VALUE).addBasicAuthHeader()
                .content(mapper.writeValueAsString(Person(name = "new name", cpf = "1")))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath(ERROR_KEY).value(INVALID_CPF))
    }

    @Test
    fun `delete answers 400 on bad cpf`() {
        mockMvc.perform(delete(PEOPLE_URL + CPF_URL, "1").addBasicAuthHeader())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath(ERROR_KEY).value(INVALID_CPF))
    }

    @Test
    fun `delete answers 200 and actually deletes`() {
        val person = Person(name = "new name", cpf = "20866873465")
        mockMvc.perform(
            post(PEOPLE_URL).contentType(APPLICATION_JSON_VALUE).addBasicAuthHeader()
                .content(mapper.writeValueAsString(person))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath(NAME_KEY).value(person.name))
            .andExpect(jsonPath(CPF_KEY).value(person.cpf))

        mockMvc.perform(delete(PEOPLE_URL + CPF_URL, person.cpf).addBasicAuthHeader())
            .andExpect(status().isOk)
            .andExpect(jsonPath(ROOT_KEY).doesNotExist())

        mockMvc.perform(get(PEOPLE_URL + CPF_URL, person.cpf).addBasicAuthHeader())
            .andExpect(status().isNoContent)
            .andExpect(jsonPath(ROOT_KEY).doesNotExist())
    }

    @Test
    fun `delete answers 400 on unsettled balance`() {
        mockMvc.perform(delete(PEOPLE_URL + CPF_URL, "73478504872").addBasicAuthHeader())
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath(ERROR_KEY).value(BALANCE_NOT_SETTLED))

    }
}