package com.nazarethlabs.joseph.core.service

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringWriter

@Service
class TemplateStreamService {
    fun openStream(templatePath: String): InputStream {
        return ClassPathResource(templatePath).inputStream
    }
}
