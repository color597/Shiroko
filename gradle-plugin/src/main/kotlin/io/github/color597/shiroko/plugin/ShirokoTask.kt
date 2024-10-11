package io.github.color597.shiroko.plugin

import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.tasks.TaskAction
import io.github.color597.shiroko.commands.ObfuscateBundleCommand
import io.github.color597.shiroko.plugin.internal.getBundleFilePath
import io.github.color597.shiroko.plugin.internal.getSigningConfig
import io.github.color597.shiroko.plugin.model.SigningConfig
import org.gradle.api.DefaultTask
import java.io.File
import java.nio.file.Path

/**
 * Created by YangJing on 2019/10/15 .
 * Email: yangjing.yeoh@bytedance.com
 */
abstract class ShirokoTask : DefaultTask() {

    private lateinit var variant: ApplicationVariant

    private lateinit var signingConfig: SigningConfig

    private lateinit var bundlePath: Path

    private lateinit var obfuscatedBundlePath: Path

    private val shiroko = project.extensions.getByName("shiroko") as ShirokoExtension

    init {
        description = "Assemble resource proguard for bundle file"
        group = "bundle"
        outputs.upToDateWhen { false }
    }

    fun setVariantScope(variant: ApplicationVariant) {
        this.variant = variant
        // init bundleFile, obfuscatedBundlePath must init before task action.
        bundlePath = getBundleFilePath(project, variant)
        obfuscatedBundlePath = File(bundlePath.toFile().parentFile, shiroko.obfuscatedBundleFileName).toPath()
    }

    @TaskAction
    private fun execute() {
        println(shiroko.toString())
        // init signing config
        signingConfig = getSigningConfig(variant)
        printSignConfiguration()

        prepareUnusedFile()

        val command = ObfuscateBundleCommand.builder()
            .setEnableObfuscate(shiroko.enableObfuscate)
            .setBundlePath(bundlePath)
            .setOutputPath(obfuscatedBundlePath)
            .setMergeDuplicatedResources(shiroko.mergeDuplicatedRes)
            .setWhiteList(shiroko.whiteList)
            .setFilterFile(shiroko.enableFilterFiles)
            .setFileFilterRules(shiroko.filterList)
            .setRemoveStr(shiroko.enableFilterStrings)
            .setUnusedStrPath(shiroko.unusedStringPath)
            .setLanguageWhiteList(shiroko.languageWhiteList)
        if (shiroko.mappingFile != null) {
            command.setMappingPath(shiroko.mappingFile)
        }

        if (signingConfig.storeFile != null && signingConfig.storeFile!!.exists()) {
            command.setStoreFile(signingConfig.storeFile!!.toPath())
                .setKeyAlias(signingConfig.keyAlias)
                .setKeyPassword(signingConfig.keyPassword)
                .setStorePassword(signingConfig.storePassword)
        }
        command.build().execute()
    }

    private fun prepareUnusedFile() {
        val simpleName = variant.name.replace("Release", "")
        val name = simpleName[0].lowercaseChar() + simpleName.substring(1)
        val resourcePath = "${project.buildDir}/outputs/mapping/$name/release/unused.txt"
        val usedFile = File(resourcePath)
        if (usedFile.exists()) {
            println("find unused.txt : ${usedFile.absolutePath}")
            if (shiroko.enableFilterStrings) {
                if (shiroko.unusedStringPath == null || shiroko.unusedStringPath!!.isBlank()) {
                    shiroko.unusedStringPath = usedFile.absolutePath
                    println("replace unused.txt!")
                }
            }
        } else {
            println(
                "not exists unused.txt : ${usedFile.absolutePath}\n" +
                        "use default path : ${shiroko.unusedStringPath}"
            )
        }
    }

    private fun printSignConfiguration() {
        println("-------------- sign configuration --------------")
        println("\tstoreFile : ${signingConfig.storeFile}")
        println("\tkeyPassword : ${encrypt(signingConfig.keyPassword)}")
        println("\talias : ${encrypt(signingConfig.keyAlias)}")
        println("\tstorePassword : ${encrypt(signingConfig.storePassword)}")
        println("-------------- sign configuration --------------")
    }

    private fun encrypt(value: String?): String {
        if (value == null) return "/"
        if (value.length > 2) {
            return "${value.substring(0, value.length / 2)}****"
        }
        return "****"
    }
}
