package userspace;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Phone> phones;
}
