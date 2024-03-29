package com.demo.code.abstracts;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Food {
    private String name;
    private String type;

    public Food(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
