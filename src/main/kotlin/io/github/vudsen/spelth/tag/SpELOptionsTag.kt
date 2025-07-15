package io.github.vudsen.spelth.tag

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.javadoc.JavadocTagInfo
import com.intellij.psi.javadoc.PsiDocTagValue
import org.jetbrains.annotations.Nls

class SpELOptionsTag : JavadocTagInfo {
    override fun getName(): String? {
        return "SpEL"
    }

    override fun isInline(): Boolean {
        return true
    }

    override fun isValidInContext(element: PsiElement?): Boolean {
        return true
    }

    override fun checkTagValue(value: PsiDocTagValue?): @Nls String? {
        return null
    }

    override fun getReference(value: PsiDocTagValue?): PsiReference? {
        return null
    }


}