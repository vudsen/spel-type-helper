package io.github.vudsen.spelth.options.impl

import com.intellij.icons.AllIcons
import com.intellij.javaee.el.util.ELImplicitVariable
import com.intellij.psi.*
import io.github.vudsen.spelth.options.SpELTypeHelperOption
import io.github.vudsen.spelth.options.VariableResolveContext
import org.jetbrains.plugins.groovy.intentions.style.inference.resolve
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
        val factory = JavaPsiFacade.getElementFactory(context.annotationMethod.project)
        val type = factory.createTypeFromText(args[1], null)

        return mutableListOf(DefVariable(context.annotation.containingFile, args[0], type, type.resolve()?.containingFile ?: return emptyList()))
    }

    override fun parseArgument(raw: String): List<String> {
        val result = mutableListOf<String>()
        val builder = StringBuilder()
        var waitingFirstVariable = true
        for (ch in raw) {
            if (ch == ' ' && waitingFirstVariable) {
                result.add(builder.toString())
                builder.clear()
                waitingFirstVariable = false
                continue
            }
            builder.append(ch)
        }
        result.add(builder.toString())
        return result
    }
}