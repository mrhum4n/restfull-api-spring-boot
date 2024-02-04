package com.project.restfull.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String phone;

    private String email;

    /*
    * RELASI 1:N ANTARA TABLE CONTACT DENGAN USER
    * */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    /*
     * RELASI 1:N ANTARA TABLE CONTACT DENGAN ADDRESS
     * */
    @OneToMany(mappedBy = "contact")
    private List<Address> addresses;
}
