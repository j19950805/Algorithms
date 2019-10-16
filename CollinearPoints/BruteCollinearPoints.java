import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int numberOfSegments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        Point[] points1 = new Point[points.length];
        System.arraycopy(points, 0, points1, 0, points.length);
        Arrays.sort(points1);
        for (int i = 0; i < points.length - 1; i++) {
            if (points1[i].compareTo(points1[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        ArrayList<LineSegment> segmentsList = new ArrayList<>();
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        if (points1[i].slopeTo(points1[j]) == points1[j].slopeTo(points1[k])
                                && points1[j].slopeTo(points1[k]) == points1[k].slopeTo(points1[l])) {
                            segmentsList.add(new LineSegment(points1[i], points1[l]));
                        }
                    }
                }
            }
        }
        segments = new LineSegment[segmentsList.size()];
        for (int i = 0; i < segments.length; i++) {
            segments[i] = segmentsList.get(i);
        }
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
