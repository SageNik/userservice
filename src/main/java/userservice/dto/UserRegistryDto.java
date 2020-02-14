package userservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserRegistryDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    private String firstname;

    private String lastname;

    private String address;
}
