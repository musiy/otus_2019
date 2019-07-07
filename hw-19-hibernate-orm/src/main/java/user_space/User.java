package user_space;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Users")
@Data
public final class User {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private int age;

    @OneToOne(
            mappedBy = "user",
            fetch = FetchType.LAZY)
    private Address address;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true)
    private List<Phone> phones;
}
