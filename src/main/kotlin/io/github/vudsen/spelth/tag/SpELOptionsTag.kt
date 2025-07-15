package io.github.vudsen.spelth.tag

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.javadoc.JavadocTagInfo
import com.intellij.psi.javadoc.PsiDocTagValue
import org.jetbrains.annotations.Nls

/**
 * Supported Options:
 * - `includeParameters`: Include method parameters in the context.
 * - `def <name> <type>`: Define a variable for the SpEL context.
 */
class SpELOptionsTag : JavadocTagInfo {

    companion object {
        const val OPTION_PREFIX = "SpEL"
        const val OPTION_INCLUDE_PARAMETERS = "includeParameters"
        const val OPTION_DEF = "def"
    }

    override fun getName(): String {
        return OPTION_PREFIX
    }

    override fun isInline(): Boolean {
        return true
    }

    override fun isValidInContext(element: PsiElement?): Boolean {
        return true
    }

    @Nls
    override fun checkTagValue(value: PsiDocTagValue?): String? {
        // TODO options validation.
        return null
    }

    override fun getReference(value: PsiDocTagValue?): PsiReference? {
        return null
    }


}