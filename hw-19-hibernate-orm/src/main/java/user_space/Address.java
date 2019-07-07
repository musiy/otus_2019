package user_space;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue
    private long id;

    private String street;

    private String building;

    private String apartment;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}
