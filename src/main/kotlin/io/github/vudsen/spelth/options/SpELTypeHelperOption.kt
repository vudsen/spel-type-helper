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


}