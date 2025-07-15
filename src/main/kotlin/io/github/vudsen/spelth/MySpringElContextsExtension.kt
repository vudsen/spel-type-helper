package io.github.vudsen.spelth

import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiAnnotationMethod
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNameValuePair
import com.intellij.psi.PsiVariable
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.spring.el.contextProviders.SpringElContextsExtension
import io.github.vudsen.spelth.tag.SpELOptionsTag
import org.jetbrains.plugins.groovy.lang.resolve.processors.inference.type

class MySpringElContextsExtension : SpringElContextsExtension() {

    override fun getContextVariables(element: PsiElement): MutableCollection<out PsiVariable> {
        val nameValuePair = PsiTreeUtil.getParentOfType(element.context, PsiNameValuePair::class.java) ?: return mutableListOf()

        val psiAnno = nameValuePair.reference?.resolve()
        if (psiAnno !is PsiAnnotationMethod) {
            return mutableListOf()
        }
        val docComment = psiAnno.docComment ?: return mutableListOf()
        val annotation = PsiTreeUtil.getParentOfType(nameValuePair, PsiAnnotation::class.java) ?: return mutableListOf()


        val result = mutableListOf<PsiVariable>()
        for (tag in docComment.tags) {
            if (tag.name != SpELOptionsTag.OPTION_PREFIX) {
                continue
            }
            val valueEle = tag.valueElement?.text ?: ""
            if (valueEle == SpELOptionsTag.OPTION_INCLUDE_PARAMETERS) {
                val method = PsiTreeUtil.getParentOfType(annotation, PsiMethod::class.java) ?: return mutableListOf()

                for (parameter in method.parameterList.parameters) {
                    result.add(parameter)
                }
            } else if (valueEle == SpELOptionsTag.OPTION_DEF) {
                val dataElements = tag.dataElements
                if (dataElements.size < 2) {
                    continue
                }
                val list = dataElements[1].text.split(' ')
                if (list.size != 2) {
                    continue
                }
                JavaPsiFacade.getInstance(psiAnno.project).findClass(list[1], psiAnno.resolveScope) ?.let {
                    val type = it.type()
                    result.add(DefVariable(annotation.containingFile, list[0], type, it.containingFile))
                }
            }
        }

        return result
    }
}