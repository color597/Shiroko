package com.color597.shiroko.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.configurationcache.extensions.capitalized

/**
 * Created by YangJing on 2019/10/15 .
 * Email: yangjing.yeoh@bytedance.com
 */
class ShirokoPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        checkApplicationPlugin(project)
        project.extensions.create("shiroko", ShirokoExtension::class.java)

        val android = project.extensions.getByName("android") as AppExtension
        project.afterEvaluate {
            android.applicationVariants.all { variant ->
                createShirokoTask(project, variant)
            }
        }
    }

    private fun createShirokoTask(project: Project, variant: ApplicationVariant) {
        val variantName = variant.name.capitalized()
        val bundleTaskName = "bundle$variantName"
        if (project.tasks.findByName(bundleTaskName) == null) {
            return
        }
        val shirokoTaskName = "shiroko$variantName"
        val shirokoTask = if (project.tasks.findByName(shirokoTaskName) == null) {
            project.tasks.create(shirokoTaskName, ShirokoTask::class.java)
        } else {
            project.tasks.getByName(shirokoTaskName) as ShirokoTask
        }
        shirokoTask.setVariantScope(variant)

        val bundleTask: Task = project.tasks.getByName(bundleTaskName)
        val bundlePackageTask: Task = project.tasks.getByName("package${variantName}Bundle")
        bundleTask.dependsOn(shirokoTask)
        shirokoTask.dependsOn(bundlePackageTask)
        // AGP-4.0.0-alpha07: use FinalizeBundleTask to sign bundle file
        // FinalizeBundleTask is executed after PackageBundleTask
        val finalizeBundleTaskName = "sign${variantName}Bundle"
        if (project.tasks.findByName(finalizeBundleTaskName) != null) {
            shirokoTask.dependsOn(project.tasks.getByName(finalizeBundleTaskName))
        }
    }

    private fun checkApplicationPlugin(project: Project) {
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw  GradleException("Android Application plugin required")
        }
    }
}
