import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Maciek Niechaj
 */
@Embeddable
public class TblStudentcoursePK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "STUDENTID")
    private int studentid;
    @Basic(optional = false)
    @Column(name = "COURSEID")
    private int courseid;

    public TblStudentcoursePK() {
    }

    public TblStudentcoursePK(int studentid, int courseid) {
        this.studentid = studentid;
        this.courseid = courseid;
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public int getCourseid() {
        return courseid;
    }

    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.studentid;
        hash = 97 * hash + this.courseid;
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
        final TblStudentcoursePK other = (TblStudentcoursePK) obj;
        if (this.studentid != other.studentid) {
            return false;
        }
        if (this.courseid != other.courseid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TblStudentcoursePK{" + "studentid=" + studentid 
                + ", courseid=" + courseid + '}';
    }

    
    
}

