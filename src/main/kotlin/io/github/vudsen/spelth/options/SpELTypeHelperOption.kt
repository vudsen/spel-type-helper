package io.github.vudsen.spelth.options

import com.intellij.psi.PsiVariable

interface SpELTypeHelperOption {

    /**
     * 获取名称
     */
    fun getName(): String

    /**
     * 校验参数是否正确
     */
    fun validate(args: List<String>): String?

    /**
     * 获取上下文变量
     */
    fun resolveContextVariables(args: List<String>, context: VariableResolveContext): List<PsiVariable>

    /**
     * 解析参数
     */
    fun parseArgument(raw: String): List<String> {
        val result = mutableListOf<String>()
        val builder = StringBuilder()
        for (element in raw) {
            val c = element
            if (c == ' ') {
                if (builder.isNotEmpty()) {
                    result.add(builder.toString())
                }
                builder.clear()
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