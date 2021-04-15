package pr1Java.networking.reflection.datatransfer;

import java.io.Serializable;

public class UsernameDto implements Serializable {
    private final String value;

    public UsernameDto(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "UsernameDto[" + getValue() + "]";
    }
}
