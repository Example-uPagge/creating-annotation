package org.sadtech.example.annotation.processor;

import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import org.sadtech.example.annotation.ClassCreator;
import org.sadtech.example.annotation.FieldNames;
import org.sadtech.example.annotation.domain.ClassDto;
import org.sadtech.example.annotation.domain.FieldDto;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("org.sadtech.example.annotation.FieldNames")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class FieldNameProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (TypeElement annotation : set) {
            Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(annotation);
            for (Element annotatedElement : annotatedElements) {
                final TypeMirror mirror = annotatedElement.asType();
                final String annotatedElementName = annotatedElement.getSimpleName().toString();
                final FieldNames settings = annotatedElement.getAnnotation(FieldNames.class);
                final String newClassName = annotatedElementName + settings.postfix();

                final Set<FieldDto> fields = annotatedElement.getEnclosedElements().stream()
                        .filter(this::isField)
                        .map(
                                element -> {
                                    final String fieldName = element.getSimpleName().toString();
                                    final String fieldStringName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, fieldName);
                                    return FieldDto.of(fieldStringName, fieldName);
                                }
                        ).collect(Collectors.toSet());

                final ClassDto newClass = new ClassDto();
                newClass.setClassName(newClassName);
                newClass.setFields(fields);
                newClass.setClassPackage(getPackage(mirror));
                ClassCreator.record(newClass, processingEnv);
            }
        }
        return true;
    }

    public boolean isField(Element element) {
        return element != null && element.getKind().isField();
    }

    public static String getPackage(TypeMirror typeMirror) {
        final String[] split = typeMirror.toString().split("\\.");
        return String.join(".", Arrays.copyOf(split, split.length - 1));
    }

}
