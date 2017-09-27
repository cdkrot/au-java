package me.cdkrot.javahw;

import java.util.Arrays;

public class Spiral {
    /**
     * Prints square matrix of odd size to stdout
     * @param mat -- matrix to print
     */
    public static void printSpiral(int[][] mat) {
        int n = mat.length;
        
        System.out.println(mat[n / 2][n / 2]);
        int i = n / 2;
        int j = n / 2 + 1;
        
        int dir = 0;
        int len = 2;
        
        while (j != n) {
            switch (dir) {
                case 0:
                    for (int dt = 0; dt != len; ++dt)
                        System.out.println(mat[i + dt][j]);
                    i += len - 1;
                    j -= 1;
                    break;
                case 1:
                    for (int dt = 0; dt != len; ++dt) 
                        System.out.println(mat[i][j - dt]);
                    j -= len - 1;
                    i -= 1;
                    break;
                case 2:
                    for (int dt = 0; dt != len; ++dt)
                        System.out.println(mat[i - dt][j]);
                    i -= len - 1;
                    j += 1;
                    break;
                case 3:
                    for (int dt = 0; dt != len; ++dt)
                        System.out.println(mat[i][j + dt]);
                    j += len;
                    len += 2;                    
            }
            
            dir = (dir + 1) % 4;
        }
    }
    
    /**
     * Helper class for sortByColumns function
     * Contains two integers, implements comparing.
     */
    private static class PairIntInt implements Comparable {
        int first;
        int second;

        @Override
        public int compareTo(Object oth) {
            PairIntInt other = (PairIntInt)oth;
            if (first == other.first)
                return second - other.second;
            else
                return first - other.first;
        }
    };
    
    /**
     * Sorts columns of a matrix by first element
     * @param a -- matrix to sort
     * @return sorted matrix.
     */
    public static int[][] sortByColumns(int[][] a) {
        int cols = a[0].length;
        
        PairIntInt[] heads = new PairIntInt[cols];
        for (int i = 0; i != cols; ++i) {
            heads[i] = new PairIntInt();
            heads[i].first  = a[0][i];
            heads[i].second = i;
        }
        
        Arrays.sort(heads);
        
        int[][] out = new int[a.length][cols];
        for (int j = 0; j != cols; ++j)
            for (int i = 0; i != a.length; ++i)
                out[i][j] = a[i][heads[j].second];
        return out;
    }

}
