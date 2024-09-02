package dev.androidbroadcast.navstate.processor

import com.google.devtools.ksp.symbol.KSName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec

private val GeneratedAnnotation by lazy { ClassName.bestGuess("javax.annotation.processing.Generated") }
private val ComposableAnnotation by lazy { ClassName.bestGuess("androidx.compose.runtime.Composable") }
private val NavDest: ClassName by lazy { ClassName.bestGuess("dev.androidbroadcast.navstate.NavDest") }
private val Navigator: ClassName by lazy { ClassName.bestGuess("dev.androidbroadcast.navstate.Navigator") }
private val NavHost by lazy { ClassName.bestGuess("dev.androidbroadcast.navstate.NavHost") }
private val NavState by lazy { ClassName.bestGuess("dev.androidbroadcast.navstate.NavState") }

private const val DestParamName = "dest"

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

    fun addImports(vararg qualifiedName: KSName) {
        imports += qualifiedName
    }

    fun end(
        packageName: String,
        fileName: String,
        funcName: String,
    ): FileSpec {
        return FileSpec
            .builder(packageName, fileName)
            .addFunction(generateNavHostFun(funcName))
            .addClassImport(NavState)
            .addClassImport(NavDest)
            .addClassImport(NavHost)
            .addImport("dev.androidbroadcast.navstate", "rememberNavTopEntry")
            .addImports(imports)
            .build()
    }

    private fun generateNavHostFun(funcName: String): FunSpec {
        val navigatorParam = "navigator"
        val onRootBackParam = "onRootBack"
        val elseBranchParam = "orElse"

        val whenCodeBlock = whenCodeBlock
            .addStatement("else -> orElse(dest)")
            .endControlFlow().build()

        val body = CodeBlock.builder()
            .beginControlFlow("NavHost(navigator, onRootBack)")
            .addStatement("val dest = rememberNavTopEntry().destination")
            .add(whenCodeBlock)
            .endControlFlow()
            .build()
        /**
         * @Generated
         * @Composable
         * public fun GeneratedNavHost(
         *     navigator: Navigator,
         *     onRootBack: () -> Unit,
         *     orElse: (NavDest) -> Unit,
         * ) {
         *     NavHost(navigator, onRootBack) {
         *         val dest = rememberNavTopEntry().destination
         *         when (dest) {
         *             is <DEST> -> <COMPOSABLE DEST>
         *                 ...
         *             else -> orElse(dest)
         *         }
         *     }
         * }
         */
        return FunSpec
            .builder(funcName)
            .addAnnotation(GeneratedAnnotation)
            .addAnnotation(ComposableAnnotation)
            .addParameter(navigatorParam, Navigator)
            .addParameter(
                onRootBackParam,
                LambdaTypeName.get(returnType = ClassName.bestGuess("kotlin.Unit")),
            )
            .addParameter(
                ParameterSpec.builder(
                    elseBranchParam,
                    LambdaTypeName.get(
                        parameters = listOf(ParameterSpec.Companion.unnamed(NavDest)),
                        returnType = ClassName.bestGuess("kotlin.Unit"),
                    ),
                )
                    .defaultValue("{ error(\"Unknown destination \$it\") }")
                    .build(),
            )
            .addCode(body)
            .build()
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
