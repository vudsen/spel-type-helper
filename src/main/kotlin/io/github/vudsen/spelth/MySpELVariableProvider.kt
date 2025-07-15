package io.github.vudsen.spelth

import com.intellij.javaee.el.ELElementProcessor
import com.intellij.javaee.el.ELExpressionHolder
import com.intellij.javaee.el.providers.ElVariablesProvider
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil

class MySpELVariableProvider : ElVariablesProvider() {

    override fun processImplicitVariables(
        element: PsiElement,
        holder: ELExpressionHolder,
        processor: ELElementProcessor
    ): Boolean {
        val psiMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java) ?: return true
        val annotation = PsiTreeUtil.getParentOfType(element, PsiAnnotation::class.java) ?: return true
//        annotation.resolveAnnotationType().docComment.tags[0].nameElement

        for (parameter in psiMethod.parameterList.parameters) {
            if (!processor.processVariable(parameter)) {
                return false
            }
        }
        return true;
    }
}