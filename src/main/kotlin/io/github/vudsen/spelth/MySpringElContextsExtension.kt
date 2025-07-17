package io.github.vudsen.spelth

import com.intellij.openapi.components.service
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiAnnotationMethod
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNameValuePair
import com.intellij.psi.PsiVariable
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.spring.el.contextProviders.SpringElContextsExtension
import io.github.vudsen.spelth.options.SpELHelperOptionsManager
import io.github.vudsen.spelth.options.VariableResolveContext


class MySpringElContextsExtension : SpringElContextsExtension() {


    override fun getContextVariables(element: PsiElement): MutableCollection<out PsiVariable> {
        val nameValuePair = PsiTreeUtil.getParentOfType(element.context, PsiNameValuePair::class.java) ?: return mutableListOf()
        val psiAnno = nameValuePair.reference?.resolve()
        if (psiAnno !is PsiAnnotationMethod) {
            return mutableListOf()
        }
        val annotation = PsiTreeUtil.getParentOfType(nameValuePair, PsiAnnotation::class.java) ?: return mutableListOf()

        val ctx = VariableResolveContext(
            psiAnno,
            annotation,
            PsiTreeUtil.getParentOfType(annotation, PsiMethod::class.java) ?: return mutableListOf()
        )
        return service<SpELHelperOptionsManager>().resolveContextVariables(ctx)
    }
}