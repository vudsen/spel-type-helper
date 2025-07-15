package io.github.vudsen.spelth

import com.intellij.icons.AllIcons
import com.intellij.javaee.el.util.ELImplicitVariable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiType
import javax.swing.Icon

class DefVariable(
    scope: PsiElement,
    variableName: String,
    type: PsiType,
    declarationFile: PsiFile,
) : ELImplicitVariable(scope, variableName, type, declarationFile) {


    override fun getIcon(open: Boolean): Icon {
        return AllIcons.Nodes.Variable
    }

}