package io.github.vudsen.spelth

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase

class MySpELVariableReference(
    element: PsiElement,
    private val typeClazz: PsiClass,
    private val parameterPsi: PsiElement
) : PsiReferenceBase<PsiElement>(element, TextRange(0, element.textLength)) {

    override fun resolve(): PsiElement? {
        return parameterPsi
    }

    override fun getVariants(): Array<Any> {
        return typeClazz.allFields.map {
            LookupElementBuilder.create(it.name)
                .withTypeText(it.type.presentableText)
        }.toTypedArray()
    }
}