package io.github.color597.shiroko.parser;

import io.github.color597.shiroko.BaseTest;
import io.github.color597.shiroko.model.ResourcesMapping;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by YangJing on 2019/10/14 .
 * Email: yangjing.yeoh@bytedance.com
 */
public class ResourcesMappingParserTest extends BaseTest {

    @Test
    public void test() throws IOException {
        ResourcesMappingParser parser = new ResourcesMappingParser(loadResourceFile("demo/mapping.txt").toPath());
        ResourcesMapping mapping = parser.parse();

        assert !mapping.getDirMapping().isEmpty();
        assert !mapping.getResourceMapping().isEmpty();
    }
}
