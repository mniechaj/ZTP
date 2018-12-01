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
@Table(name = "TBL_COURSES")
public class TblCourses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private int id;
    @Basic(optional = false)
    @Column(name = "COURSENAME")
    private String coursename;
    @Column(name = "COURSEDESCR")
    private String coursedescr;
    @Column(name = "COURSEHOURS")
    private int coursehours;
    @Basic(optional = false)
    @Column(name = "COURSESEM")
    private int coursesem;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblCourses")
    private Collection<TblStudentcourse> tblStudentcourseCollection;

    public TblCourses() {
        
    }

    public TblCourses(int id, String coursename, String coursedescr, int coursehours, int coursesem, Collection<TblStudentcourse> tblStudentcourseCollection) {
        this.id = id;
        this.coursename = coursename;
        this.coursedescr = coursedescr;
        this.coursehours = coursehours;
        this.coursesem = coursesem;
        this.tblStudentcourseCollection = tblStudentcourseCollection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getCoursedescr() {
        return coursedescr;
    }

    public void setCoursedescr(String coursedescr) {
        this.coursedescr = coursedescr;
    }

    public int getCoursehours() {
        return coursehours;
    }

    public void setCoursehours(int coursehours) {
        this.coursehours = coursehours;
    }

    public int getCoursesem() {
        return coursesem;
    }

    public void setCoursesem(int coursesem) {
        this.coursesem = coursesem;
    }

    public Collection<TblStudentcourse> getTblStudentcourseCollection() {
        return tblStudentcourseCollection;
    }

    public void setTblStudentcourseCollection(Collection<TblStudentcourse> tblStudentcourseCollection) {
        this.tblStudentcourseCollection = tblStudentcourseCollection;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.id;
        hash = 53 * hash + Objects.hashCode(this.coursename);
        hash = 53 * hash + Objects.hashCode(this.coursedescr);
        hash = 53 * hash + this.coursehours;
        hash = 53 * hash + this.coursesem;
        hash = 53 * hash + Objects.hashCode(this.tblStudentcourseCollection);
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
        final TblCourses other = (TblCourses) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.coursehours != other.coursehours) {
            return false;
        }
        if (this.coursesem != other.coursesem) {
            return false;
        }
        if (!Objects.equals(this.coursename, other.coursename)) {
            return false;
        }
        if (!Objects.equals(this.coursedescr, other.coursedescr)) {
            return false;
        }
        if (!Objects.equals(this.tblStudentcourseCollection, other.tblStudentcourseCollection)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TblCourses{" + "id=" + id 
                + ", coursename=" + coursename 
                + ", coursedescr=" + coursedescr 
                + ", coursehours=" + coursehours 
                + ", coursesem=" + coursesem + '}';
    }

    
    
}

