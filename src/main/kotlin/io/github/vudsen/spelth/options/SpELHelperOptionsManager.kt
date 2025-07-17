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
            val validateResult = validateTag(tag)
            if (validateResult.first != null) {
                continue
            }
            result.addAll(helperOption.resolveContextVariables(validateResult.second!!, context))
        }
        cache[docComment] = CacheEntry(docComment.text.hashCode(), result)
        return result
    }

    private fun validateTag(tag: PsiDocTag): Pair<String?, List<String>?> {
        val option = tag.valueElement?.text ?: return Pair("Option name expected", null)
        val helperOption = optionMap[option] ?: return Pair("Unknown Option", null)
        if (tag.dataElements.size < 2) {
            return Pair("Unknown Error(#1)", null)
        }
        val args = splitWithWhiteSpace(tag.dataElements[1].text)
        return Pair(helperOption.validate(args), args)
    }

    @Nls
    fun validateOption(value: PsiDocTagValue): String? {
        val tag = value.parent
        if (tag !is PsiDocTag) {
            return "Unknown Error(#2)"
        }
        validateTag(tag)
        val sp = splitWithWhiteSpace(value.text)
        if (sp.isEmpty()) {
            return "Option name expected"
        }
        val helperOption = optionMap[sp[0]] ?: return "Unknown option"
        return helperOption.validate(sp.subList(1, sp.size))
    }

    private fun splitWithWhiteSpace(text: String): List<String> {
        val result = mutableListOf<String>()
        val builder = StringBuilder()
        for (element in text) {
            val c = element
            if (c == ' ') {
                if (builder.isNotEmpty()) {
                    result.add(builder.toString())
                } else {
                    builder.clear()
                }
            } else {
                builder.append(c)
            }
        }
        if (builder.isNotEmpty()) {
            result.add(builder.toString())
        }
        return result
    }
}