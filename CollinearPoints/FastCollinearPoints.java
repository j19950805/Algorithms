import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point p: points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }
        Point[] copyPoints = new Point[points.length];
        System.arraycopy(points, 0, copyPoints, 0, points.length);
        noDuplicatedPoints(copyPoints);
        segments = fastCollinearPoints(copyPoints);
    }

    private void noDuplicatedPoints(Point[] points) {
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    private LineSegment[] fastCollinearPoints(Point[] points) {
        ArrayList<LineSegment> lineList = new ArrayList<>();
        for (int i = 0; i < points.length - 3; i++) {
            Point p = points[i];
            Point[] pSlope = new Point[points.length];
            System.arraycopy(points, 0, pSlope, 0, points.length);
            Arrays.sort(pSlope, p.slopeOrder());
            for (int j = 0; j < pSlope.length - 2; j++) {
                int k = j + 1;
                while (k < pSlope.length) {
                    if (pSlope[k].slopeTo(p) != pSlope[j].slopeTo(p)) {
                        break;
                    }
                    k++;
                }

                if (k > j + 2 && pSlope[j].compareTo(p) > 0) {
                    lineList.add(new LineSegment(p, pSlope[k - 1]));
                }

                j = k - 1;
            }
        }

        LineSegment[] res = new LineSegment[lineList.size()];
        for (int i = 0; i < lineList.size(); i++) {
            res[i] = lineList.get(i);
        }
        return res;
    }



    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        LineSegment[] segmentsCopy = new LineSegment[segments.length];
        System.arraycopy(segments, 0, segmentsCopy, 0, segments.length);
        return segmentsCopy;
    }

}
