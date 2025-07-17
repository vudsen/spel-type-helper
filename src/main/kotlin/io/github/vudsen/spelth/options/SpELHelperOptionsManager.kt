package io.github.vudsen.spelth.options

import com.intellij.openapi.components.Service
import com.intellij.psi.PsiVariable
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.javadoc.PsiDocTag
import com.intellij.psi.javadoc.PsiDocTagValue
import io.github.vudsen.spelth.options.impl.DefineVariableOption
import io.github.vudsen.spelth.options.impl.IncludeParametersOption
import org.jetbrains.annotations.Nls
import java.util.*

@Service(Service.Level.APP)
class SpELHelperOptionsManager {

    private val optionMap = HashMap<String, SpELTypeHelperOption>()

    private val cache = WeakHashMap<PsiDocComment, CacheEntry>()

    private class CacheEntry(
        val docHashCode: Int,
        val variables: MutableList<PsiVariable>,
    )

    companion object {
        const val OPTION_PREFIX = "SpEL"
    }

    init {
        register(IncludeParametersOption())
        register(DefineVariableOption())
    }

    private fun register(option: SpELTypeHelperOption) {
        optionMap[option.getName()] = option
    }


    fun resolveContextVariables(
        context: VariableResolveContext
    ): MutableCollection<PsiVariable> {
        val docComment = context.annotationMethod.docComment ?: return mutableListOf()
        cache[docComment]?.let { cacheEntry ->
            if (cacheEntry.docHashCode == docComment.text.hashCode()) {
                return cacheEntry.variables
            }
        }
        val result = mutableListOf<PsiVariable>()
        for (tag in docComment.tags) {
            if (tag.name != OPTION_PREFIX) {
                continue
            }
            val option = tag.valueElement?.text ?: continue
            val helperOption = optionMap[option] ?: continue
            if (tag.dataElements.size < 2) {
                continue
            }
            val args = helperOption.parseArgument(tag.dataElements[1].text)
            if (helperOption.validate(args) != null) {
                continue
            }
            result.addAll(helperOption.resolveContextVariables(args, context))
        }
        cache[docComment] = CacheEntry(docComment.text.hashCode(), result)
        return result
    }

    @Nls
    fun validateOption(value: PsiDocTagValue): String? {
        val tag = value.parent
        if (tag !is PsiDocTag) {
            return "Unknown Error(#2)"
        }
        val option = tag.valueElement?.text ?: return "Option name expected"
        val helperOption = optionMap[option] ?: return "Unknown Option"
        if (tag.dataElements.size < 2) {
            return "Unknown Error(#1)"
        }
        return helperOption.validate(helperOption.parseArgument(tag.dataElements[1].text))
    }


}