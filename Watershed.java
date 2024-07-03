package unsupervised;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Watershed {

    public static class Point {
        int x;
        int y;
        int im;
        int label;

        public Point(int x, int y, int im) {
            this.x = x;
            this.y = y;
            this.im = im;
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getIM() {
            return this.im;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public boolean isEqualOfCoordinate(Point other) {
            return im == other.getIM();
        }
    }
    public static int[][][] Watershed_by_Immersion(int[][][] gray_value) {

        int Height = gray_value.length;
        int Width = gray_value[0].length;
        int dist[][] = new int[Height][Width];
        int lab[][] = new int[Height][Width];

        int INIT = -1;
        int MASK = -2;
        int WSHED = 0;
        
        Point fictitious = new Point(-1, -1, -1);
        
        int curlab = 0;
        int Hmax = Integer.MIN_VALUE, Hmin = Integer.MAX_VALUE;
        Queue<Point> queue = new LinkedList<>();
        ArrayList<Point> Points = new ArrayList<>();

        for (int i = 0; i < gray_value.length; i++) {
            for (int j = 0; j < gray_value[i].length; j++) {
                lab[i][j] = INIT;
                dist[i][j] = 0;
                Hmax = Math.max(Hmax, gray_value[i][j][0]);
                Hmin = Math.min(Hmin, gray_value[i][j][0]);
                Points.add(new Point(j, i, gray_value[i][j][0]));     // x is column/height, y is row/width
            }
        }
        Collections.sort(Points, new Comparator<Point>() {
            public int compare(Point thisPoint, Point otherPoint) {
                if (thisPoint.getIM() == otherPoint.getIM()) {
                    return 0;
    
                }else if (thisPoint.getIM() > otherPoint.getIM()) {
                    return 1;
    
                }else {
                    return -1;
    
                }
            }
        });
        
        ArrayList<Point> subD = new ArrayList<>();
        HashMap<Integer, ArrayList<Point>> D = new HashMap<>();

        int im = Hmin;
        Points.add(fictitious);
        for (Point p: Points) {
            if (p.getIM() != im) {
                D.put(im, subD);
                subD = new ArrayList<>();
                im = p.getIM();
            }
            subD.add(p);
        }
        
        int dx[] = new int[] {0, 0, -1, 1, 1, -1, 1, -1}; int dy[] = new int[] {1, -1, 0, 0, 1, -1, -1, 1};
        int x = 0, y = 0, qX = 0, qY = 0;
        
        for (int h = Hmin; h < Hmax; h++) {
            if (!D.containsKey(h)) {
                continue;
            }
            for (Point p: D.get(h)) {
                x = p.getX(); y = p.getY();
                queue.clear();
                
                lab[y][x] = MASK;
                for (int u = 0; u < 8; u++) {
                    qX = x + dx[u];
                    qY = y + dy[u];
                    if (qX < 0 || qX >= Width || qY < 0 || qY >= Height) {
                        continue;
                    }
                    if (lab[qY][qX] > 0 || lab[qY][qX] == WSHED) {
                        dist[y][x] = 1;
                        queue.offer(p);

                    }
                }
            }
                
            int curdist = 1;
            queue.offer(fictitious);
            
            while (true) {
                Point p = queue.remove();
                if (p.equals(fictitious)) {
                    if (queue.isEmpty()) {
                        break;
                    }else {
                        queue.offer(fictitious);
                        curdist++;
                        p = queue.remove();
                    }
                }
                x = p.getX(); y = p.getY();
                for (int u = 0; u < 8; u++) {
                    qX = x + dx[u];
                    qY = y + dy[u];
                    if (qX < 0 || qX >= Width || qY < 0 || qY >= Height) {
                        continue;
                    }

                    if (dist[qY][qX] < curdist && (lab[qY][qX] > 0 || lab[qY][qX] == WSHED)) {
                        if (lab[qY][qX] > 0) {
                            if (lab[y][x] == MASK || lab[y][x] == WSHED) {
                                lab[y][x] = lab[qY][qX];

                            }else if (lab[y][x] != lab[qY][qX]) {
                                lab[y][x] = WSHED;

                            }

                        }else if (lab[y][x] == MASK){
                            lab[y][x] = WSHED;
                            
                        }else if (lab[qY][qX] == MASK && dist[qY][qX] == 0) {
                            dist[qY][qX] = curdist + 1;
                            if (!(qX < 0 || qX >= Width || qY < 0 || qY >= Height)) {
                                queue.offer(new Point(qX, qY, gray_value[qY][qX][0]));
                            }
                            
                        }
                    }
                }
                
            }
            for (Point p: D.get(h)) {
                x = p.getX();
                y = p.getY();
                dist[y][x] = 0;

                if (lab[y][x] == MASK) {
                    curlab = curlab + 1;
                    queue.offer(p); 
                    lab[y][x] = curlab;

                    Point q;
                    int rX = 0, rY = 0;
                    while (!(queue.isEmpty())) {
                        q = queue.remove();
                        for (int u = 0; u < 8; u++) {
                            rX = q.getX() + dx[u];
                            rY = q.getY() + dy[u];
                            if (rX < 0 || rX >= Width || rY < 0 || rY >= Height) {
                                continue;
                            }
                            if (lab[rY][rX] == MASK) {
                                queue.offer(new Point(rX, rY, gray_value[rY][rX][0]));
                                lab[rY][rX] = curlab;

                            }
                        }
                    }
                }
                
            }
        }
        int[][][] newData = new int[Height][Width][3];
        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                for (int c = 0; c < 3; c++) {
                    newData[i][j][c] = lab[i][j];
                }
            }
        }
        return newData;

    }
}
