package io.github.vudsen.spelth.options.impl

import com.intellij.icons.AllIcons
import com.intellij.javaee.el.util.ELImplicitVariable
import com.intellij.psi.*
import io.github.vudsen.spelth.options.SpELTypeHelperOption
import io.github.vudsen.spelth.options.VariableResolveContext
import org.jetbrains.plugins.groovy.lang.resolve.processors.inference.type
import javax.swing.Icon

/**
 * `@SpEL def <name> <type>`: Define a variable for the SpEL context.
 */
class DefineVariableOption : SpELTypeHelperOption {


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

    override fun getName(): String {
        return "def"
    }

    override fun validate(args: List<String>): String? {
        if (args.size != 2) {
            return "Expected 2 args, but got ${args.size}"
        }
        return null
    }

    override fun resolveContextVariables(args: List<String>, context: VariableResolveContext): List<PsiVariable> {
        JavaPsiFacade.getInstance(context.annotationMethod.project).findClass(args[1], context.annotationMethod.resolveScope) ?.let {
            val type = it.type()
            return mutableListOf(DefVariable(context.annotation.containingFile, args[0], type, it.containingFile))
        }
        return emptyList()
    }
}