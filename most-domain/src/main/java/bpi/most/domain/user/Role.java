package bpi.most.domain.user;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Entity specifying a Role.
 *
 * @author Jakob Korherr
 */
@Entity
@Table(name = "role",
        uniqueConstraints = @UniqueConstraint(name = "name_UNIQUE", columnNames = {"name"}))
public class Role
{

    @Id
    @GeneratedValue
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Basic(optional = false)
    @Column(name = "description", nullable = false, length = 100)
    private String description;

}
