/*
 * Copyright 2003-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.proc;

import com.sun.tools.javac.code.Symbol;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes({
        "com.acme.ann.internal.EntityInternal"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AggregatingProcessor extends AbstractProcessor {

    private Filer filer;
    private FileObject generatedResource;
    private final List<String> items = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!annotations.isEmpty() && !roundEnv.errorRaised()) {
            try {
                if (generatedResource == null) {
                    generatedResource = filer.createResource(
                            StandardLocation.CLASS_OUTPUT,
                            "data",
                            "animals.txt"
                    );
                }
                for (TypeElement annotation : annotations) {
                    Set<? extends Element> annotated = roundEnv.getElementsAnnotatedWith(annotation);
                    System.out.println("Processing " + annotated + " in " + this.getClass().getName());
                    for (Element element : annotated) {
                        if (element instanceof Symbol.ClassSymbol) {
                            Symbol.ClassSymbol clazz = (Symbol.ClassSymbol) element;
                            items.add("I'm a " + clazz.getSimpleName().toString().toLowerCase() + "\n");
                        }
                    }
                }
                // Ugly hack because javac would prevent writing several times to the same resource
                try (Writer wrt = openWriter(generatedResource)) {
                    for (String item : items) {
                        wrt.write(item);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private Writer openWriter(FileObject generatedResource) throws FileNotFoundException {
        File file = new File(generatedResource.toUri());
        file.getParentFile().mkdirs();
        return new OutputStreamWriter(
                new FileOutputStream(
                        file
                )
        );
    }
}
