package io.github.color597.shiroko.executors;

import com.android.tools.build.bundletool.model.AppBundle;
import io.github.color597.shiroko.BaseTest;
import io.github.color597.shiroko.bundle.AppBundleAnalyzer;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by YangJing on 2019/10/10 .
 * Email: yangjing.yeoh@bytedance.com
 */
public class DuplicatedResourcesMergerTest extends BaseTest {

    @Test
    public void test() throws IOException {
        Path outputDirPath = getTempDirPath();
        Path bundlePath = loadResourceFile("demo/demo.aab").toPath();
        AppBundle rawAppBundle = new AppBundleAnalyzer(bundlePath).analyze();
        DuplicatedResourcesMerger merger = new DuplicatedResourcesMerger(bundlePath, rawAppBundle, outputDirPath);
        AppBundle appBundle = merger.merge();
        assert appBundle != null;
    }
}
