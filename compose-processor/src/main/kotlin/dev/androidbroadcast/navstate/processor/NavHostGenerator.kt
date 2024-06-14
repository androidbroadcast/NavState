package dev.androidbroadcast.navstate.processor

import com.google.devtools.ksp.symbol.KSName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec

private val GeneratedAnnotation by lazy { ClassName.bestGuess("javax.annotation.processing.Generated") }
private val ComposableAnnotation by lazy { ClassName.bestGuess("androidx.compose.runtime.Composable") }
private val NavDest: ClassName by lazy { ClassName.bestGuess("dev.androidbroadcast.navstate.NavDest") }
private val NavHost by lazy { ClassName.bestGuess("dev.androidbroadcast.navstate.NavHost") }
private val NavState by lazy { ClassName.bestGuess("dev.androidbroadcast.navstate.NavState") }
private val Modifier by lazy { ClassName.bestGuess("androidx.compose.ui.Modifier") }

private const val DestParamName = "dest"
private const val DestIteratorFunName = "destIterator"

internal fun generateNavHost(
    fileName: String,
    packageName: String,
    funcName: String,
    body: NavHostGenerator.() -> Unit,
): FileSpec {
    val whenIsGenerator = NavHostGenerator()
    whenIsGenerator.start(DestParamName)
    whenIsGenerator.apply(body)
    return whenIsGenerator.end(packageName, fileName, funcName)
}

internal class NavHostGenerator {
    private val whenCodeBlock = CodeBlock.builder()

    private val imports = mutableListOf<KSName>()

    fun start(valueParamName: String) {
        whenCodeBlock.beginControlFlow("when($valueParamName)")
    }

    fun addCase(
        type: ClassName,
        call: CodeBlock,
    ) {
        whenCodeBlock.addStatement("is ${type.simpleName} -> $call")
    }

    fun addImport(qualifiedName: KSName) {
        imports += qualifiedName
    }

    fun end(
        packageName: String,
        fileName: String,
        funcName: String,
    ): FileSpec {
        val whenCodeBlock = whenCodeBlock
            .addStatement("else -> error(\"Unknown destination \$$DestParamName\")")
            .endControlFlow().build()

        val whenIsFun =
            FunSpec
                .builder(DestIteratorFunName)
                .addAnnotation(GeneratedAnnotation)
                .addAnnotation(ComposableAnnotation)
                .addParameter(DestParamName, NavDest)
                .addParameter(
                    ParameterSpec
                        .builder(
                            name = "modifier",
                            type = Modifier,
                        ).defaultValue("Modifier")
                        .build(),
                ).addCode(whenCodeBlock)
                .addModifiers(KModifier.PRIVATE)
                .build()

        val generatetNavHostFun = generateNavHostFun(funcName)

        return FileSpec
            .builder(packageName, fileName)
            .addFunction(whenIsFun)
            .addFunction(generatetNavHostFun)
            .addClassImport(NavState)
            .addClassImport(NavDest)
            .addClassImport(NavHost)
            .addImport("dev.androidbroadcast.navstate", "rememberNavTopEntry")
            .addImports(imports)
            .build()
    }

    private fun generateNavHostFun(funcName: String): FunSpec {
        val initDestParam = "initialDestination"
        val onRootBackParam = "onRootBack"
        val initialStackIdParam = "initialStackId"
        return FunSpec
            .builder(funcName)
            .addAnnotation(GeneratedAnnotation)
            .addAnnotation(ComposableAnnotation)
            .addParameter(initDestParam, NavDest)
            .addParameter(
                onRootBackParam,
                LambdaTypeName.get(returnType = ClassName.bestGuess("kotlin.Unit")),
            ).addParameter(
                ParameterSpec
                    .builder(initialStackIdParam, ClassName.bestGuess("kotlin.String"))
                    .defaultValue("NavState.DefaultStackId")
                    .build(),
            ).addCode(
                CodeBlock.of(
                    """
                    ${NavHost.simpleName}(
                        $initDestParam,
                        $onRootBackParam,
                        $initialStackIdParam
                    ) {
                        $DestIteratorFunName(rememberNavTopEntry().destination)
                    }
                    """.trimIndent(),
                ),
            ).build()
    }
}

private fun FileSpec.Builder.addImports(qualifiedNames: Iterable<KSName>) =
    apply {
        qualifiedNames.forEach { it ->
            addImport(it.getQualifier(), it.getShortName())
        }
    }

private fun FileSpec.Builder.addClassImport(className: ClassName) =
    apply {
        addImport(className.packageName, className.simpleName)
    }
