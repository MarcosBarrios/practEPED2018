package marcos.eped.test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.ListIF;
import es.uned.lsi.eped.pract2017_2018.Query;
import es.uned.lsi.eped.pract2017_2018.QueryDepotList;

public class TestQueryDepotList {
	
//	@Test
//	public void testAddgetFreqQuery() throws IOException {
//		QueryDepotList qdl = new QueryDepotList("src/Debugging_Consultas.txt");
//		assertEquals(1, qdl.compararLexicograficamente("car tree sculture" , "car over tree"));
//	}
	
	@Test
	public void testAddIncFrecuency() {
		QueryDepotList qdl1 = new QueryDepotList();
		qdl1.incFreqQuery("holaaa");
		assertEquals(1, qdl1.numQueries());
		
		QueryDepotList qdl2 = new QueryDepotList();
		assertEquals(0, qdl2.numQueries());
		
		QueryDepotList qdl3 = new QueryDepotList();
		qdl3.incFreqQuery("prueba");
		qdl3.incFreqQuery("prueba");
		qdl3.incFreqQuery("prueba");
		qdl3.incFreqQuery("prueba2");
		qdl3.incFreqQuery("prueba3");
		assertEquals(3, qdl3.numQueries());
		assertEquals(3, qdl3.getFreqQuery("prueba"));
	}
	
	@Test
	public void testAddTamaño() {
		QueryDepotList qdl = new QueryDepotList();
		qdl.incFreqQuery("caca");
		assertEquals(1, qdl.numQueries());
	}
	
	@Test
	public void testAddListOfQueries() throws IOException {
		QueryDepotList qdl = new QueryDepotList("src/Debugging_Consultas.txt");
		
		ListIF<Query> l = qdl.listOfQueries("gl");
		IteratorIF<Query> itr = l.iterator();
		while(itr.hasNext()) {
			Query temp = itr.getNext();
			System.out.println( "(" + temp.getFreq() + ") " + temp.getText());
		}
	}
	
	/*@Test
	public void testAddnumQueries() throws IOException{
		QueryDepotList qdl = new QueryDepotList("src/JdP-consultas.txt");
		assertEquals(12108, qdl.numQueries());
	}*/

}
