package io.github.vudsen.spelth.options

import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiAnnotationMethod
import com.intellij.psi.PsiMethod

class VariableResolveContext(
    val annotationMethod: PsiAnnotationMethod,
    val annotation: PsiAnnotation,
    val method: PsiMethod,
)