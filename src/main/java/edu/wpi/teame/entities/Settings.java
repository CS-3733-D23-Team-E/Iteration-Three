package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public enum Settings {
    INSTANCE;

    @Getter @Setter
    String language;

    Settings(){
        language = "English";
    }
}
