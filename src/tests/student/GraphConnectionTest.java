package tests.student;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import base.Graph;

public class GraphConnectionTest {

     Graph<Character> test = new Graph<Character> (); 


	@Test
	void testAllConnectedIncorrecteVerhalten() {
		String str = new String ("AtLeastCreateTheTestsRight");
		for (int i=0 ; i<10; i++) 
			test.addNode(str.charAt(i));

		test.addEdge(test.getNodes().get(0), test.getNodes().get(1));
		assertEquals(false, test.allNodesConnected());
	}

	@Test
	void testAllConnectedKorrekteVerhalten() {
		String str = new String ("FirstBloodIsNotBetter");
		for (int i=0 ; i<10; i++)  
			test.addNode(str.charAt(i));
		for (int i=0; i<9; i++) 
			test.addEdge(test.getNodes().get(i), test.getNodes().get(i+1));	
		assertEquals(true, test.allNodesConnected()); 
	}

}