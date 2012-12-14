package bpi.most.domain.user;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

/**
 * Entity specifying a user.
 * 
 * @author Jakob Korherr
 */
@Entity
@Table(name = "user",
        uniqueConstraints = @UniqueConstraint(name = "name_UNIQUE", columnNames = {"name"}))
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Basic(optional = false)
    @Column(name = "password", nullable = false, length = 60, columnDefinition = "binary(60)")
    private byte[] password;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Role.class)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="rid", referencedColumnName="id"))
    private List<Role> roles;
    
}
