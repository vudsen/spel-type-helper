package io.github.vudsen.spelth

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiVariable
import com.intellij.spring.el.contextProviders.SpringElContextsExtension

class MySpringELContextVariables : SpringElContextsExtension() {

    override fun getContextVariables(element: PsiElement): Collection<PsiVariable?> {
        TODO("Not yet implemented")
    }

}