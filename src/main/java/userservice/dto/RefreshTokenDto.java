package userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDto {

    private String username;

    private String refreshToken;
}
