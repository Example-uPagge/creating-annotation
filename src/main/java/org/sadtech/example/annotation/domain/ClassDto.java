package org.sadtech.example.annotation.domain;

import java.util.Set;

public class ClassDto {

    private String className;
    private Set<FieldDto> fields;
    private String classPackage;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Set<FieldDto> getFields() {
        return fields;
    }

    public void setFields(Set<FieldDto> fields) {
        this.fields = fields;
    }

    public String getClassPackage() {
        return classPackage;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

}
