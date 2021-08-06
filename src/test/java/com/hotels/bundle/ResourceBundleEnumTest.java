package com.hotels.bundle;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ListResourceBundle;

import org.junit.Test;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

public class ResourceBundleEnumTest {
    @Test
    public void should_generate_an_enum_for_each_key() throws Exception {
        // given
        TestBundle bundle = new TestBundle("messages");
        ResourceBundleEnum rbe = new ResourceBundleEnum(bundle);
        // when
        JavaFile source = rbe.source("com.hotels");
        // then
        System.out.println(source);
        assertThat(source.typeSpec.enumConstants).hasSameSizeAs(bundle.getContents());
    }

    private static class TestBundle extends ListResourceBundle {
        private final String name;

        private TestBundle(String name) {
            this.name = name;
        }

        @Override
        public String getBaseBundleName() {
            return name;
        }

        @Override
        protected Object[][] getContents() {
            return new Object[][]{
                {"app.label", "Label"},
                {"app.title", "Title"},
                {"app.title.caption", "catchy caption"}
            };
        }
    }
}
