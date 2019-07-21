package userspace;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Phone {

    @Id
    @GeneratedValue
    private long id;

    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
