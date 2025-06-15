package steps.transformers;

import io.cucumber.java.DataTableType;
import models.requests.RegisterRequest;

import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
public class DatableTransformer {

    /**
     * Converts a DataTable row into a RegisterRequest object.
     * <p>
     * Special cases:
     * - "<space>" is converted to 10 spaces.
     * - "<blank>" is converted to an empty string.
     *
     * @param row the DataTable row containing the registration data
     * @return a RegisterRequest object with the normalized values
     */
    @DataTableType
    public RegisterRequest toRegister(Map<String, String> row) {
        Function<String, String> normalize = s -> {
            if (s == null) return "";
            String trimmed = s.trim();
            if ("<space>".equalsIgnoreCase(trimmed)) {
                return " ".repeat(10);
            }
            if ("<blank>".equalsIgnoreCase(trimmed)) {
                return "";
            }
            return s;
        };

        return new RegisterRequest(
                normalize.apply(row.get("username")),
                normalize.apply(row.get("password")),
                normalize.apply(row.get("confirm_password"))
        );
    }
}
