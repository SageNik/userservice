package userservice.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.relational.core.mapping.Table;
import userservice.enums.Role;

@Table("main.users")
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "users", type = "user")
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

    @Field(type = FieldType.Object)
    private Role role;
}
