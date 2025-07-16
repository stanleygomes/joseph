package com.nazarethlabs.joseph.core.service

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.InputStream
import java.io.InputStreamReader

@Service
class TemplateService {
    fun compileTemplate(templatePath: String, context: Any): String {
        val templateStream = this.openStream(templatePath)
        val mustache = this.compile(templateStream, templatePath)

        return this.buildHtml(mustache, context).also {
            templateStream.close()
        }
    }

    private fun buildHtml(mustache: Mustache, context: Any): String {
        val writer = java.io.StringWriter()
        mustache.execute(writer, context).flush()

        return writer.toString()
    }

    private fun openStream(templatePath: String): InputStream {
        return ClassPathResource(templatePath).inputStream
    }

    private fun compile(templateStream: InputStream, templatePath: String): Mustache {
        val mustacheFactory = DefaultMustacheFactory()
        return mustacheFactory.compile(InputStreamReader(templateStream), templatePath)
    }
}
