package pr1Java.networking.dataTransfer;

import java.io.Serializable;

public class SeatsCountDto implements Serializable {
    private final int value;

    public SeatsCountDto(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SeatsCountDto[" + getValue() + "]";
    }
}
