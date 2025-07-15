package io.github.vudsen.spelth.tag

import com.intellij.psi.javadoc.CustomJavadocTagProvider
import com.intellij.psi.javadoc.JavadocTagInfo

class SpELJavaDocTagProvider : CustomJavadocTagProvider {

    override fun getSupportedTags(): List<JavadocTagInfo?>? {
        return listOf(SpELOptionsTag())
    }

}