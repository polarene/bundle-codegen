package com.hotels.bundle;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.lang.model.element.Modifier;


import org.apache.commons.text.WordUtils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * TODO: Document
 * @author mmirk
 */
public class ResourceBundleEnum {
    private final ResourceBundle bundle;

    public ResourceBundleEnum(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public JavaFile source(String pkg) throws IOException {
        final String fieldName = "key";
        //final ClassName className = ClassName.get(pkg, WordUtils.capitalize(bundle.getBaseBundleName()));
        String className = WordUtils.capitalize(bundle.getBaseBundleName());
        final TypeSpec.Builder builder = TypeSpec.enumBuilder(className)
            .addModifiers(Modifier.PUBLIC)
            .addField(String.class, fieldName, Modifier.PRIVATE, Modifier.FINAL)
            .addMethod(MethodSpec.constructorBuilder()
                .addParameter(String.class, fieldName)
                .addStatement("this.$N = $N", fieldName, fieldName)
                .build());

        final Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            builder.addEnumConstant(key.replaceAll("[.-]", "_").toUpperCase(), TypeSpec.anonymousClassBuilder("$S", key).build());
        }
        final TypeSpec bundleEnum = builder.build();

        final JavaFile javaFile = JavaFile.builder(pkg, bundleEnum)
            .skipJavaLangImports(true)
            .addFileComment("Generated with Java Poet")
            .build();
        return javaFile;
    }
}
