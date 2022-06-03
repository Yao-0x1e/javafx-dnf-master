package com.garena.dnfmaster.bean;

import javafx.scene.control.TextField;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TextFieldOption {
    private String name;
    private Class<? extends TextField> typeClass;
}
