/**
 * Created by Maciek Niechaj on 10.10.17.
 */
public class Levenshtein {

    private void fillMatrixCell(int[][] m, int row, int col, int cost) {
        m[row][col] = Math.min(m[row - 1][col - 1] + cost, Math.min(m[row - 1][col] + 1, m[row][col - 1] + 1));
    }

    public int countDistance(String w1, String w2) {
        int rows = w1.length();
        int cols = w2.length();
        int[][] dist = new int[rows+1][cols+1];

        for (int i = 0; i <= rows; i++) {
            dist[i][0] = i;
        }
        for (int i = 1; i <= cols; i++) {
            dist[0][i] = i;
        }

        // fill values of all matrix cells
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                if (w1.charAt(i - 1) == w2.charAt(j - 1))
                    fillMatrixCell(dist, i, j, 0);
                else
                    fillMatrixCell(dist, i, j, 1);
            }
        }

        return dist[rows][cols];
    }
}
