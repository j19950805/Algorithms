import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class KdTree {
    private static final Comparator<Point2D> XComparator = Comparator.comparingDouble(Point2D::x);
    private static final Comparator<Point2D> YComparator = Comparator.comparingDouble(Point2D::y);

    private Node root;

    public KdTree() {
    }

    private class Node {
        private Point2D point;
        private Node left, right;
        private int size;

        private Node(Point2D p, int s) {
            point = p;
            size = s;
        }
    }


    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(p, root, XComparator);
    }

    private boolean contains(Point2D p, Node x, Comparator<Point2D> c) {
        if (x == null) {
            return false;
        }
        if (p.equals(x.point)) {
            return true;
        }
        if (c.compare(p, x.point) < 0) {
            return contains(p, x.left, changeComparator(c));
        } else {
            return contains(p, x.right, changeComparator(c));
        }
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = insert(p, root, XComparator);
    }

    private Node insert(Point2D p, Node x, Comparator<Point2D> c) {
        if (x == null) {
            return new Node(p, 1);
        }
        if (p.equals(x.point)) {
            return x;
        }
        if (c.compare(p, x.point) < 0) {
            x.left = insert(p, x.left, changeComparator(c));
        } else {
            x.right = insert(p, x.right, changeComparator(c));
        }
        x.size = sizeOf(x.left) + sizeOf(x.right) + 1;
        return x;
    }

    private int sizeOf(Node x) {
        if (x == null) {
            return 0;
        }
        return x.size;
    }

    public boolean isEmpty() {
        return sizeOf(root) == 0;
    }

    public int size() {
        return sizeOf(root);
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) {
            return;
        }
        x.point.draw();
        draw(x.left);
        draw(x.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> res = new ArrayList<>();
        range(rect, root, res, XComparator);
        return res;
    }

    private void range(RectHV rect, Node x, List<Point2D> res, Comparator<Point2D> c) {
        if (x == null) {
            return;
        }
        if (rect.contains(x.point)) {
            res.add(x.point);
        }
        if (c == XComparator) {
            if (rect.xmin() < x.point.x()) {
                range(rect, x.left, res, changeComparator(c));
            }
            if (rect.xmax() >= x.point.x()) {
                range(rect, x.right, res, changeComparator(c));
            }
        } else {
            if (rect.ymin() < x.point.y()) {
                range(rect, x.left, res, changeComparator(c));
            }
            if (rect.ymax() >= x.point.y()) {
                range(rect, x.right, res, changeComparator(c));
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        Point2D res = root.point;
        double minDist = p.distanceSquaredTo(res);
        RectHV rect = new RectHV(0, 0, 1, 1);
        return nearest(p, res, minDist, rect, root, XComparator);
    }

    private Point2D nearest(Point2D goal, Point2D res, double minDist, RectHV rect, Node x, Comparator<Point2D> c) {
        if (x == null || goal.equals(res)) {
            return res;
        }

        double distance = goal.distanceSquaredTo(x.point);
        if (distance < minDist) {
            minDist = distance;
            res = x.point;
        }

        Node goodSide, badSide;
        RectHV goodRect, badRect;
        if (c == XComparator) {
            badRect = new RectHV(x.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            goodRect = new RectHV(rect.xmin(), rect.ymin(), x.point.x(), rect.ymax());
        } else {
            badRect = new RectHV(rect.xmin(), x.point.y(), rect.xmax(), rect.ymax());
            goodRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.point.y());
        }

        if (c.compare(goal, x.point) < 0) {
            goodSide = x.left;
            badSide = x.right;
        } else {
            goodSide = x.right;
            badSide = x.left;
            RectHV temp = goodRect;
            goodRect = badRect;
            badRect = temp;
        }

        res = nearest(goal, res, minDist, goodRect, goodSide, changeComparator(c));
        minDist = goal.distanceSquaredTo(res);
        if (rect.distanceSquaredTo(goal) < minDist) {
            res = nearest(goal, res, minDist, badRect, badSide, changeComparator(c));
        }
        return res;
    }

    private Comparator<Point2D> changeComparator(Comparator<Point2D> c) {
        if (c == XComparator) {
            return YComparator;
        } else {
            return XComparator;
        }
    }

}
