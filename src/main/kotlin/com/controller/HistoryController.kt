package com.controller

import com.service.HistoryService
import com.util.ERROR
import com.util.HISTORY_URL
import com.util.ID_URL
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(HISTORY_URL)
class HistoryController(val historyService: HistoryService) {

    @GetMapping(ID_URL)
    fun findById(@PathVariable id: Int, @RequestParam days: Optional<Int>): ResponseEntity<Any> {
        val result = historyService.findById(id, days)
        return if (result is String) ResponseEntity(mapOf(ERROR to result), BAD_REQUEST)
        else ResponseEntity(result, OK)
    }
}