package io.github.color597.shiroko.bundle;

import com.android.tools.build.bundletool.model.AppBundle;
import io.github.color597.shiroko.utils.TimeClock;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

import static com.android.tools.build.bundletool.model.utils.files.FilePreconditions.checkFileExistsAndReadable;

/**
 * Created by YangJing on 2019/10/10 .
 * Email: yangjing.yeoh@bytedance.com
 */
public class AppBundleAnalyzer {

    private static final Logger logger = Logger.getLogger(AppBundleAnalyzer.class.getName());
    private final Path bundlePath;

    public AppBundleAnalyzer(Path bundlePath) {
        checkFileExistsAndReadable(bundlePath);
        this.bundlePath = bundlePath;
    }

    public AppBundle analyze() throws IOException {
        TimeClock timeClock = new TimeClock();
        ZipFile bundleZip = new ZipFile(bundlePath.toFile());
        AppBundle appBundle = AppBundle.buildFromZip(bundleZip);
        System.out.printf("analyze bundle file done, const %s%n", timeClock.getCoast());
        return appBundle;
    }
}
