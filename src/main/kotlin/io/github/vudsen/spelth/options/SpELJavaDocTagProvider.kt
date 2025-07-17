package io.github.vudsen.spelth.options

import com.intellij.openapi.components.service
import com.intellij.psi.javadoc.CustomJavadocTagProvider
import com.intellij.psi.javadoc.JavadocTagInfo

class SpELJavaDocTagProvider : CustomJavadocTagProvider {

    override fun getSupportedTags(): List<JavadocTagInfo> {
        return listOf(SpELJavadocTagInfo(service<SpELHelperOptionsManager>()))
    }

}