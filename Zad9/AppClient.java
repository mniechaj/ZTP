import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author Maciek Niechaj
 */
public class AppClient {

    private static EntityManager entityManager;
    
    public static void main(String[] args) {
        try {
            entityManager = Persistence.createEntityManagerFactory("myPersistence").createEntityManager();
            String filePath = args[0];
            List<String> lines = Files.lines(Paths.get(filePath)).collect(Collectors.toList());
            List<String> split = Arrays.asList(lines.get(1).split("\\s+"));
            String courseName = lines.get(0);
            String firstName = split.get(0);
            String lastName = split.get(1);
            
	    @SuppressWarnings("unchecked")
            List<Integer> marks = (List<Integer>) entityManager.createQuery("SELECT sc.mark FROM TblStudentcourse sc "
                    + "WHERE sc.mark > 50 AND sc.tblStudents.semester = "
                    + "(SELECT c.coursesem FROM TblCourses c WHERE c.coursename = :courseName) "
                    + "AND sc.tblStudentcoursePK.courseid = (SELECT c.id FROM TblCourses c WHERE c.coursename = :courseName) "
                    + "ORDER BY sc.mark ASC")
                    .setParameter("courseName", courseName)
                    .getResultList();
            
	    @SuppressWarnings("unchecked")
            List<Integer> studentMark = (List<Integer>) entityManager.createQuery("SELECT sc.mark FROM TblStudentcourse sc "
                    + "WHERE sc.tblStudents.firstname = :firstName "
                    + "AND sc.tblStudents.lastname = :lastName "
                    + "AND sc.tblStudentcoursePK.courseid = (SELECT c.id FROM TblCourses c WHERE c.coursename = :courseName)")
                    .setParameter("courseName", courseName)
                    .setParameter("lastName", lastName)
                    .setParameter("firstName", firstName)
                    .getResultList();

            double median = countMedian(marks);
            int medianRelativeValue = countMedianRelativeValue(studentMark.get(0), median);
            if (medianRelativeValue == 0) {
                System.out.println("Wynik : " + medianRelativeValue);
            } else {
                System.out.println("Wynik : " + medianRelativeValue + "%");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    // Assuming, that marks are sorted asc
    private static double countMedian(List<Integer> marks) {
        int size = marks.size();
        return size % 2 == 0 ?
                (marks.get(size / 2) + marks.get(size / 2 - 1)) / 2.0 : marks.get(size / 2);
    }
    
    private static int countMedianRelativeValue(Integer mark, Double median) {
        return (int)((mark - median) / median * 100);
    }
    
}

