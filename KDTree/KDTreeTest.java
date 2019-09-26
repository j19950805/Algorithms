import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class KDTreeTest {

    @Test
    public void TestKDTree() {
        List<Point2D> samplePoint2Ds = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            samplePoint2Ds.add(new Point2D(StdRandom.uniform(), StdRandom.uniform()));
        }

        KdTree kdt = new KdTree();
        for (Point2D p: samplePoint2Ds) {
            kdt.insert(p);
        }

        PointSET ps = new PointSET();
        for (Point2D p: samplePoint2Ds) {
            ps.insert(p);
        }

        assertFalse(kdt.isEmpty());
        assertEquals(kdt.size(), ps.size());

        boolean testContains = false;
        for (int i = 0; i < 10000; i++) {
            double x = StdRandom.uniform();
            double y = StdRandom.uniform();
            boolean c1 = kdt.contains(new Point2D(x, y));
            boolean c2 = ps.contains(new Point2D(x, y));
            testContains = c1 == c2;
        }
        assertTrue(testContains);

        boolean testNearest = false;
        for (int i = 0; i < 10000; i++) {
            double x = StdRandom.uniform();
            double y = StdRandom.uniform();
            Point2D n1 = kdt.nearest(new Point2D(x, y));
            Point2D n2 = ps.nearest(new Point2D(x, y));
            testNearest = n1.equals(n2);
        }
        assertTrue(testNearest);

        double x1 = StdRandom.uniform();
        double x2 = StdRandom.uniform();
        double y1 = StdRandom.uniform();
        double y2 = StdRandom.uniform();
        RectHV rect = new RectHV(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2));
        Iterable<Point2D> r1 = kdt.range(rect);
        Iterable<Point2D> r2 = ps.range(rect);
        Iterator<Point2D> I1 = r1.iterator();
        Iterator<Point2D> I2 = r2.iterator();
        while (I1.hasNext() && I2.hasNext()) {
            System.out.print(I1.next());
            System.out.print(I2.next());
            System.out.println();
        }
    }



    public static void main(String[] args) {
        List<Point2D> samplePoint2Ds = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            samplePoint2Ds.add(new Point2D(StdRandom.uniform(), StdRandom.uniform()));
        }

        long start = System.currentTimeMillis();
        KdTree kdt = new KdTree();
        for (Point2D p: samplePoint2Ds) {
            kdt.insert(p);
        }
        long end = System.currentTimeMillis();
        System.out.println("Total time elapsed(KdTreeInsertion): " + (end - start)/1000.0 +  " seconds.");

        start = System.currentTimeMillis();
        PointSET ps = new PointSET();
        for (Point2D p: samplePoint2Ds) {
            ps.insert(p);
        }
        end = System.currentTimeMillis();
        System.out.println("Total time elapsed(PointSetInsertion): " + (end - start)/1000.0 +  " seconds.");

        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            double x = StdRandom.uniform();
            double y = StdRandom.uniform();
            kdt.contains(new Point2D(x, y));
        }
        end = System.currentTimeMillis();
        System.out.println("Total time elapsed(KDTree.contains): " + (end - start)/1000.0 +  " seconds.");

        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            double x = StdRandom.uniform();
            double y = StdRandom.uniform();
            ps.contains(new Point2D(x, y));
        }
        end = System.currentTimeMillis();
        System.out.println("Total time elapsed(PointSet.contains): " + (end - start)/1000.0 +  " seconds.");

        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            double x = StdRandom.uniform();
            double y = StdRandom.uniform();
            kdt.nearest(new Point2D(x, y));
        }
        end = System.currentTimeMillis();
        System.out.println("Total time elapsed(KDTree.nearest): " + (end - start)/1000.0 +  " seconds.");

        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            double x = StdRandom.uniform();
            double y = StdRandom.uniform();
            ps.nearest(new Point2D(x, y));
        }
        end = System.currentTimeMillis();
        System.out.println("Total time elapsed(PointSet.nearest): " + (end - start)/1000.0 +  " seconds.");

        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            double x1 = StdRandom.uniform();
            double x2 = StdRandom.uniform();
            double y1 = StdRandom.uniform();
            double y2 = StdRandom.uniform();
            RectHV rect = new RectHV(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2));
            kdt.range(rect);
        }
        end = System.currentTimeMillis();
        System.out.println("Total time elapsed(KDTree.range): " + (end - start)/1000.0 +  " seconds.");

        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            double x1 = StdRandom.uniform();
            double x2 = StdRandom.uniform();
            double y1 = StdRandom.uniform();
            double y2 = StdRandom.uniform();
            RectHV rect = new RectHV(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2));
            ps.range(rect);
        }
        end = System.currentTimeMillis();
        System.out.println("Total time elapsed(PointSet.range): " + (end - start)/1000.0 +  " seconds.");
    }
}
