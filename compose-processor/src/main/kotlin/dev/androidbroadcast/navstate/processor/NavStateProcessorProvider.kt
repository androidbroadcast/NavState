package dev.androidbroadcast.navstate.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal class NavStateProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return NavStateProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            packageName = environment.options.getOrDefault(OPTION_PACKAGE_NAME, ""),
            fileName = environment.options.getOrDefault(OPTION_FILE_NAME, "GeneratedNavHost"),
            funcName = environment.options.getOrDefault(OPTION_FUNCTION_NAME, "DestRouter")
        )
    }
}

const val OPTION_PACKAGE_NAME = "NavStateGenerator.package"
const val OPTION_FILE_NAME = "NavStateGenerator.fileName"
const val OPTION_FUNCTION_NAME = "NavStateGenerator.functionName"
