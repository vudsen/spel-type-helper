package io.github.vudsen.spelth.options

import com.intellij.openapi.components.service
import com.intellij.psi.PsiAnnotationMethod
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.javadoc.JavadocTagInfo
import com.intellij.psi.javadoc.PsiDocTagValue

class SpELJavadocTagInfo(manager: SpELHelperOptionsManager) : JavadocTagInfo {

    override fun getName(): String {
        return SpELHelperOptionsManager.OPTION_PREFIX
    }

    override fun isInline(): Boolean {
        return true
    }

    override fun isValidInContext(element: PsiElement?): Boolean {
        // TODO ensure under PsiAnnotationMethod
        return element is PsiAnnotationMethod
    }

    override fun checkTagValue(value: PsiDocTagValue?): String? {
        value ?: return null
        return service<SpELHelperOptionsManager>().validateOption(value)
    }

    override fun getReference(p0: PsiDocTagValue?): PsiReference? {
        return null
    }

}