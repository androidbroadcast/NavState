package dev.androidbroadcast.navstate.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ksp.writeTo

internal class NavStateProcessor(
    val codeGenerator: CodeGenerator,
    val logger: KSPLogger,
    val packageName: String,
    val fileName: String,
    val funcName: String,
) : SymbolProcessor {
    private val navDestVisitor: NavDestVisitor = NavDestVisitor()

    private val functions = mutableListOf<KSFunctionDeclaration>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getAllFiles().forEach {
            it.accept(navDestVisitor, Unit)
        }
        return emptyList()
    }

    override fun finish() {

        val fileSpec =
            generateNavHost(
                fileName = fileName,
                packageName = packageName,
                funcName = funcName,
            ) {
                functions.forEach { addDestCase(it) }
            }

        fileSpec.writeTo(codeGenerator, aggregating = false)
    }

    private fun NavHostGenerator.addDestCase(function: KSFunctionDeclaration) {
        val ksAnnotation: KSAnnotation =
            function.annotations
                .first { it.shortName.getShortName() == "NavDest" }
        val destTypeArg: KSValueArgument =
            ksAnnotation.arguments
                .first { it.name?.getShortName() == "dest" }

        val funQualifiedName: KSName = checkNotNull(function.qualifiedName)

        val destArgument = ksAnnotation.arguments[0].value
        check(destArgument is KSType)
        val destType = destArgument.declaration.qualifiedName!!
        addImport(destType)
        addImport(funQualifiedName)

        val caseCodeBlock: CodeBlock =
            if (function.parameters.isEmpty()) {
                CodeBlock.of("${funQualifiedName.getShortName()}()")
            } else {
                with(CodeBlock.Builder()) {
                    add("${funQualifiedName.getShortName()}(")
                    var multipleParam = false
                    function.parameters.onEach { parameter ->
                        if (multipleParam) add(", ")
                        when (parameter.type.resolve().declaration.qualifiedName!!.asString()) {
                            COMPOSE_MODIFIER_QUALIFIED_NAME -> add("modifier")
                            destType.asString() -> add("$DEST_PARAM_NAME")
                            else -> error("Parameter type ${parameter.name!!.asString()} isn't supported")
                        }
                        multipleParam = true
                    }
                    add(")")
                }.build()
            }
        addCase(ClassName.bestGuess(checkNotNull(destTypeArg.value.toString())), caseCodeBlock)
    }

    private inner class NavDestVisitor : KSVisitorVoid() {
        override fun visitFile(
            file: KSFile,
            data: Unit,
        ) {
            file.declarations
                .filterIsInstance<KSFunctionDeclaration>()
                .forEach { declaration: KSFunctionDeclaration ->
                    declaration.accept(this, Unit)
                }
        }

        override fun visitFunctionDeclaration(
            function: KSFunctionDeclaration,
            data: Unit,
        ) {
            function.annotations
                .filter { annotation -> annotation.shortName.getShortName() == "NavDest" }
                .forEach { functions += function }
        }
    }

    private companion object {
        private const val DEST_PARAM_NAME = "dest"
        private const val COMPOSE_MODIFIER_QUALIFIED_NAME = "androidx.compose.ui.Modifier"
    }
}
