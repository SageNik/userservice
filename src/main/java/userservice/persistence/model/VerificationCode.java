package userservice.persistence.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;

@Table("main.verification_code")
@Setter
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode {

    @Id
    private Long id;

    private String token;

    private Integer expiration; //days

    private Long userId;

    private Date expiryDate;

}
