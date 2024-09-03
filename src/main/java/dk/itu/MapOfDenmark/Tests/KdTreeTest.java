/*
package dk.itu.MapOfDenmark.Tests;

import dk.itu.MapOfDenmark.Model.Trees.KDTree.KdTree;
import dk.itu.MapOfDenmark.Model.Trees.KDTree.Rect;
import dk.itu.MapOfDenmark.Model.Point2D;
import dk.itu.MapOfDenmark.utils.HelperMethods;

import java.util.*;

public class KdTreeTest {

    public static void main(String[] args) {
        KdTree kdTree = new KdTree(Point2D.class);
        Map<Long, Point2D> map = new HashMap<>();

        for(int x = 0; x < 10; x++) // the added "000" are to avoid duplicated id's
            for(int y = 0; y < 3; y++)
                kdTree.insert(Long.parseLong(x + "000" + y),
                        new Point2D(Long.parseLong(x + "000" + y), x, y));

        kdTree.run();

        rangeTest(kdTree);

    }

    static boolean containsTest(KdTree kdTree, Map<Long, Point2D> map){
        int containsCounter = 0;
        for(Point2D p : map.values())
            if(kdTree.contains(p))
                containsCounter ++;
        System.out.println("KdTree: " + kdTree.getSize());
        return map.size() == containsCounter;
    }

    static boolean containsTest(KdTree kdTree){

        return kdTree.contains(new Point2D(
                Long.parseLong(100000+"0000"+100000), 100000, 100000));
    }

    static boolean buildTreeTest(KdTree kdTree, Map<Long, Point2D> map){
        return map.size() == kdTree.getSize();
    }

//    static void nearest(KdTree tree){
//        Point2D nearestPoint = tree.nearest(new Point2D(HelperMethods.getRandomId(), 20,10));
//        System.out.println(tree.getRoot());
//        System.out.println(nearestPoint);
//    }

    static void rangeTest(KdTree tree){
        Rect rect = new Rect(0,0,2,2);
        System.out.println(tree.range(rect).size());

    }
}
*/
