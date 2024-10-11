package io.github.color597.shiroko.plugin.internal

import com.android.build.gradle.api.ApplicationVariant
import io.github.color597.shiroko.plugin.model.SigningConfig

/**
 * Created by YangJing on 2020/01/06 .
 * Email: yangjing.yeoh@bytedance.com
 */
internal fun getSigningConfig(variant: ApplicationVariant): SigningConfig {
    return SigningConfig(
        variant.signingConfig.storeFile,
        variant.signingConfig.storePassword,
        variant.signingConfig.keyAlias,
        variant.signingConfig.keyPassword
    )
}
