import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Maciek Niechaj
 */
public class AppClient {

    private static ICostRemote costBean;

    /**
     * @param args the command line arguments
     * parameter 1 - DataSource name
     * parameter 2 - DataBase table name
     */
    public static void main(String[] args) {
        try {
            String dataSourceName = args[0];
            String tableName = args[1];
            Context context = new InitialContext();
            costBean = (ICostRemote) context.lookup("java:global/" + "124634" + "/Cost!ICostRemote");
            DataSource dataSource = (DataSource) context.lookup(dataSourceName);
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            
            resultSet = statement.executeQuery("SELECT x FROM " + tableName + " WHERE x > 0");
            List<Double> xValues = new LinkedList<>();
            while (resultSet.next()) {
                xValues.add(resultSet.getDouble("x"));
            }
            resultSet = statement.executeQuery("SELECT y FROM " + tableName + " WHERE y > 0");
            List<Double> yValues = new LinkedList<>();
            while (resultSet.next()) {
                yValues.add(resultSet.getDouble("y"));
            }
            costBean.setHorizontalLines(xValues);
            costBean.setVerticalLines(yValues);
            Double cost = costBean.countCost();
            System.out.println("Koszt cięcia : " + cost);
            
        } catch (IndexOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
        } catch (NamingException ex) {
            System.out.println(ex.getMessage());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
