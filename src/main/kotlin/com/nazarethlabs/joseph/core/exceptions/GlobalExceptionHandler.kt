package com.nazarethlabs.joseph.core.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(ex: ResourceNotFoundException): ResponseEntity<Map<String, String?>> {
        val body = mapOf("error" to ex.message)
        return ResponseEntity(body, HttpStatus.NOT_FOUND)
    }
}
