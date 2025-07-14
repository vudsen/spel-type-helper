package io.github.vudsen.spelth

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.spring.el.lexer.SpringELTokenType
import com.intellij.util.ProcessingContext

class MySpELCompletionContributor : CompletionContributor() {


    init {
        extend(
            CompletionType.BASIC, PlatformPatterns.psiElement(SpringELTokenType.SPEL_IDENTIFIER),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val method = SpELUtils.findPsiMethod(parameters.position) ?: return
                    for (parameter in method.parameterList.parameters) {
                        result.addElement(LookupElementBuilder.create(parameter.name).withIcon(AllIcons.Nodes.Field))
                    }
                    println()
                }

            }
        )
    }

}