package io.github.vudsen.spelth

import com.intellij.psi.javadoc.CustomJavadocTagProvider
import com.intellij.psi.javadoc.JavadocTagInfo
import io.github.vudsen.spelth.tag.SpELOptionsTag

class SpELJavaDocTagProvider : CustomJavadocTagProvider {

    override fun getSupportedTags(): List<JavadocTagInfo?>? {
        return listOf(SpELOptionsTag())
    }

}