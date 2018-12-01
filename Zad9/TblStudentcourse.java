import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Maciek Niechaj
 */
@Entity
@Table(name = "TBL_STUDENTCOURSE")
public class TblStudentcourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected TblStudentcoursePK tblStudentcoursePK;
    @Basic(optional = false)
    @Column(name = "MARK")
    private int mark;
    @JoinColumn(name = "STUDENTID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TblCourses tblCourses;
    @JoinColumn(name = "STUDENTID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TblStudents tblStudents;

    public TblStudentcourse() {
        
    }

    public TblStudentcourse(TblStudentcoursePK tblStudentcoursePK, int mark, TblCourses tblCourses, TblStudents tblStudents) {
        this.tblStudentcoursePK = tblStudentcoursePK;
        this.mark = mark;
        this.tblCourses = tblCourses;
        this.tblStudents = tblStudents;
    }

    public TblStudentcoursePK getTblStudentcoursePK() {
        return tblStudentcoursePK;
    }

    public void setTblStudentcoursePK(TblStudentcoursePK tblStudentcoursePK) {
        this.tblStudentcoursePK = tblStudentcoursePK;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public TblCourses getTblCourses() {
        return tblCourses;
    }

    public void setTblCourses(TblCourses tblCourses) {
        this.tblCourses = tblCourses;
    }

    public TblStudents getTblStudents() {
        return tblStudents;
    }

    public void setTblStudents(TblStudents tblStudents) {
        this.tblStudents = tblStudents;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.tblStudentcoursePK);
        hash = 59 * hash + this.mark;
        hash = 59 * hash + Objects.hashCode(this.tblCourses);
        hash = 59 * hash + Objects.hashCode(this.tblStudents);
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
        final TblStudentcourse other = (TblStudentcourse) obj;
        if (this.mark != other.mark) {
            return false;
        }
        if (!Objects.equals(this.tblStudentcoursePK, other.tblStudentcoursePK)) {
            return false;
        }
        if (!Objects.equals(this.tblCourses, other.tblCourses)) {
            return false;
        }
        if (!Objects.equals(this.tblStudents, other.tblStudents)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TblStudentcourse{" + "tblStudentcoursePK=" + tblStudentcoursePK + ", mark=" + mark + '}';
    }
    
}

