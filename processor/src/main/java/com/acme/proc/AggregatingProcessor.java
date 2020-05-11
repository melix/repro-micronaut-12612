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
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes({
        "com.acme.ann.Entity"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AggregatingProcessor extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!annotations.isEmpty()) {
            try {
                FileObject generatedResource = filer.createResource(
                        StandardLocation.CLASS_OUTPUT,
                        "data",
                        "animals.txt"
                );
                StringBuilder sb = new StringBuilder();
                for (TypeElement annotation : annotations) {
                    Set<? extends Element> annotated = roundEnv.getElementsAnnotatedWith(annotation);
                    System.out.println("Processing " + annotated + " in " + this.getClass().getName());
                    for (Element element : annotated) {
                        if (element instanceof Symbol.ClassSymbol) {
                            Symbol.ClassSymbol clazz = (Symbol.ClassSymbol) element;
                            sb.append("I'm a " + clazz.getSimpleName().toString().toLowerCase()).append("\n");
                        }
                    }
                }
                try (Writer wrt = generatedResource.openWriter()) {
                    wrt.write(sb.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
