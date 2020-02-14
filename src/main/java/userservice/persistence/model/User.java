package userservice.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import userservice.enums.Role;

@Table("main.users")
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {


    @Id
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String firstname;

    @JsonIgnore
    private String lastname;

    @JsonIgnore
    private String address;

    private Boolean enabled;

    private Role role;
}
