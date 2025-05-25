package com.lemurybiznesu.backend.model.dto.response;

import com.lemurybiznesu.backend.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dane o użytkowniku do panelu edycji")
public class UserDetailsResponse {
    @Schema(description = "Imię", example = "Jan")
    private String firstName;
    @Schema(description = "Nazwisko", example = "Kowalski")
    private String lastName;
    @Schema(description = "Unikalny adres e-mail", example = "jan_kowalski@example.comn")
    private String email;
    @Schema(description = "Numer telefonu", example = "+48123456789")
    private String phoneNumber;

    public static UserDetailsResponse fromEntity(User user) {
        return new UserDetailsResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber());
    }
}
