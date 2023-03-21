package com.learn.code.minioSpring.com.minis;

public class PropertyValue {

    private final String type;
    private final String name;
    private final Object value;

    public PropertyValue(String type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public PropertyValue(String name, Object value) {
        this.type = null;
        this.name = name;
        this.value = value;
    }

    /**
     * 为什么private final String name;
     * 不允许写setter方法？
     *
     */
    /**
     * !! setName() don't do that !
     * <p>
     * When a parameter in a Java class is marked as final, it means that its value cannot be changed once it is initialized.
     * This is because the final keyword indicates that the parameter is a constant and its value cannot be modified.
     * <p>
     * Because of this, there is no need to provide a set method for a final parameter in a Java class, since it cannot be modified after initialization.
     * If you attempt to create a set method for a final parameter, you will get a compilation error indicating that you cannot modify a final parameter.
     * <p>
     * In general, it is a good practice to mark parameters as final if their value should not be modified after initialization.
     * This helps to ensure that the code remains safe and predictable, since it prevents unintended changes to the parameter's value.
     */


    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
