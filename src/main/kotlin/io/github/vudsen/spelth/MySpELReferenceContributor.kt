package io.github.vudsen.spelth

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.spring.el.psi.SpringELVariable
import com.intellij.util.ProcessingContext
import org.jetbrains.plugins.groovy.intentions.style.inference.resolve

class MySpELReferenceContributor : PsiReferenceContributor(){
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(SpringELVariable::class.java),
            object : PsiReferenceProvider() {

                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<out PsiReference?> {
                    val method = SpELUtils.findPsiMethod(element) ?: return emptyArray<PsiReference?>()
                    val result = arrayOfNulls<PsiReference?>(method.parameterList.parameters.size)
                    for ((index, parameter) in method.parameterList.parameters.withIndex()) {
                        val clazz = parameter.type.resolve() ?: continue
                        val nameIdentifier = parameter.nameIdentifier ?: continue
                        result[index] = MySpELVariableReference(element, clazz, nameIdentifier)
                    }
                    return result
                }
            }
        )

    }

}

