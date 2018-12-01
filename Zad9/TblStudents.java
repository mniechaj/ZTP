import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Maciek Niechaj
 */
@Entity
@Table(name = "TBL_STUDENTS")
public class TblStudents implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private int id;
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Column(name = "LASTNAME")
    private String lastname;
    @Basic(optional = false)
    @Column(name = "SEMESTER")
    private int semester;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblStudents")
    private Collection<TblStudentcourse> tblStudentcourseCollection;

    public TblStudents() {
    }

    public TblStudents(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Collection<TblStudentcourse> getTblStudentcourseCollection() {
        return tblStudentcourseCollection;
    }

    public void setTblStudentcourseCollection(Collection<TblStudentcourse> tblStudentcourseCollection) {
        this.tblStudentcourseCollection = tblStudentcourseCollection;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.id;
        hash = 61 * hash + Objects.hashCode(this.firstname);
        hash = 61 * hash + Objects.hashCode(this.lastname);
        hash = 61 * hash + this.semester;
        hash = 61 * hash + Objects.hashCode(this.tblStudentcourseCollection);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TblStudents other = (TblStudents) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.semester != other.semester) {
            return false;
        }
        if (!Objects.equals(this.firstname, other.firstname)) {
            return false;
        }
        if (!Objects.equals(this.lastname, other.lastname)) {
            return false;
        }
        if (!Objects.equals(this.tblStudentcourseCollection, other.tblStudentcourseCollection)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TblStudents{" + "id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", semester=" + semester + '}';
    }
    
}

