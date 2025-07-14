package io.github.vudsen.spelth

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethod

object SpELUtils {

    fun findPsiMethod(base: PsiElement): PsiMethod? {
        var context: PsiElement? = base
        while (context != null) {
            if (context.context is PsiLiteralExpression) {
                context = context.context
                break
            }
            context = context.parent
        }
        if (context !is PsiLiteralExpression) {
            return null
        }
        var parent: PsiElement? = context.parent
        while (parent != null) {
            if (parent is PsiMethod) {
                return parent
            }
            parent = parent.parent
        }
        return null
    }

}