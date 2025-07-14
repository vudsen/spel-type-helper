package io.github.vudsen.spelth

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
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
                    val method = findPsiMethod(element) ?: return emptyArray<PsiReference?>()
                    val result = arrayOfNulls<PsiReference?>(method.parameterList.parameters.size)
                    for ((index, parameter) in method.parameterList.parameters.withIndex()) {
                        val clazz = parameter.type.resolve() ?: continue
                        result[index] = MySpELVariableReference(element, clazz)
                    }
                    return result
                }
            }
        )
    }

    private fun findPsiMethod(base: PsiElement): PsiMethod? {
        var current = base
        while (current != null) {
            if (current is PsiMethod) {
                return current
            }
            current = current.parent
        }
        return null
    }
}