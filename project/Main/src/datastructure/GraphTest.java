package datastructure;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    public static Graph graph= new Graph();

    @BeforeAll
    static void beforeAnything(){
        graph.insertEdge(1,2);
        graph.insertEdge(1,3);

        graph.insertEdge(2,6);
        graph.insertEdge(2,7);

        graph.insertEdge(3,4);
        graph.insertEdge(3,5);
        graph.insertEdge(3,7);

        graph.insertEdge(5,3);
        graph.insertEdge(5,8);

        graph.insertEdge(7,1);
        graph.insertEdge(7,9);

        graph.insertEdge(8,9);

        graph.insertEdge(9,7);

        System.out.println(graph);
    }

    @org.junit.jupiter.api.Test
    void testCase() {
        Graph grr= new Graph();
        grr.insertEdge(1,2);
        grr.insertEdge(2,3);
        grr.insertEdge(3,1);
        grr.insertEdge(4,1);
        grr.insertEdge(2,4);

        assertEquals(grr.getShortestPath(1,3),2);
        grr.insertEdge(4,5);
        assertEquals(grr.getShortestPath(1,5),3);
        assertEquals(grr.getShortestPath(5,1),-1);

    }

    @org.junit.jupiter.api.Test
    void insertEdge() {
        assertTrue(graph.insertEdge(7,6));
        assertTrue(graph.insertEdge(7,8));
        assertFalse(graph.insertEdge(2,6));
    }

    @org.junit.jupiter.api.Test
    void deleteEdge() {
        assertTrue(graph.deleteEdge(1,2));
        assertFalse(graph.deleteEdge(1,2));
        assertFalse(graph.deleteEdge(1,4));
        System.out.println(graph);
    }

    @org.junit.jupiter.api.Test
    void getShortestPath() {
        assertEquals(graph.getShortestPath(1,6),2);
        assertEquals(graph.getShortestPath(1,2),1);
        assertEquals(graph.getShortestPath(1,7),2);
        assertEquals(graph.getShortestPath(1,9),3);
        assertEquals(graph.getShortestPath(3,1),2);
    }
}