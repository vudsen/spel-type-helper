package io.github.vudsen.spelth

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase

class MySpELVariableReference(
    element: PsiElement,
    private val clazz: PsiClass,
) : PsiReferenceBase<PsiElement>(element, TextRange(0, element.textLength)) {

    override fun resolve(): PsiElement? {
        return clazz
    }

    override fun getVariants(): Array<Any> {
        return clazz.allFields.map {
            LookupElementBuilder.create(it.name)
                .withTypeText(it.type.presentableText)
        }.toTypedArray()
    }
}