package io.github.vudsen.spelth.options.impl

import com.intellij.psi.PsiVariable
import io.github.vudsen.spelth.options.SpELTypeHelperOption
import io.github.vudsen.spelth.options.VariableResolveContext
/**
 * `@SpEL includeParameters`: Include method parameters in the context.
 *
 */
class IncludeParametersOption : SpELTypeHelperOption {

    override fun getName(): String {
        return "includeParameters"
    }

    override fun validate(args: List<String>): String? {
        if (args.isEmpty()) {
            return null
        }
        return "No argument needed in this option"
    }

    override fun resolveContextVariables(args: List<String>, context: VariableResolveContext): List<PsiVariable> {
        val result = mutableListOf<PsiVariable>()
        for (parameter in context.method.parameterList.parameters) {
            result.add(parameter)
        }
        return result
    }
}