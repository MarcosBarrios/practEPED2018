package marcos.eped.test;

import org.junit.jupiter.api.Test;

import es.uned.lsi.eped.DataStructures.GTree;
import es.uned.lsi.eped.DataStructures.GTreeIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;
import es.uned.lsi.eped.pract2017_2018.Query;
import es.uned.lsi.eped.pract2017_2018.QueryDepotTree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

public class TestQueryDepotTree {
	
//	@Test
//	public void testAddListaLexicografica() throws IOException {
//		QueryDepotTree qdt = new QueryDepotTree("src/Debugging_Consultas.txt");
//		GTreeIF<Query> q = new GTree<Query>();
//		q.setRoot(new Query(""));
//		
//		GTreeIF<Query> q2 = new GTree<Query>();
//		Query query1 = new Query("s");
//		q2.setRoot(query1);
//		
//		GTreeIF<Query> q3 = new GTree<Query>();
//		Query query2 = new Query("o");
//		q3.setRoot(query2);
//		
//		q.addChild(1, q3);
//		q.addChild(2, q2);
//		ListIF<GTreeIF<Query>> l = qdt.ordenarLexicograficamente(q);
//		IteratorIF<GTreeIF<Query>> itr = l.iterator();
//		while(itr.hasNext()) {
//			GTreeIF<Query> temp = itr.getNext();
//			System.out.println(temp.getRoot().getText());
//		}
//		assertEquals(1, qdt.compararLexicograficamente("h", "o"));
//	}
	
	@Test
	public void testAddObtenerArbolPrefijo() throws IOException {
		QueryDepotTree qdt = new QueryDepotTree("src/Debugging_Consultas.txt");
		ListIF<Query> l = qdt.listOfQueries("car");
		IteratorIF<Query> itr = l.iterator();
		System.out.println("Imprimir lista");
		while(itr.hasNext()) {
			Query temp = itr.getNext();
			System.out.println( temp.getText() + "(" + temp.getFreq() + ")");
		}
	}
	
	@Test
	public void testAddnumQueriesEmpty() {
		QueryDepotTree qdt = new QueryDepotTree();
		qdt.incFreqQuery("azucah");
		qdt.incFreqQuery("");
		assertEquals(1, qdt.numQueries());
	}
	
	@Test
	public void testAddnumQueriesFromFile() throws IOException {
		QueryDepotTree qdt = new QueryDepotTree("src/JdP-consultas.txt");
		assertEquals(12108, qdt.numQueries());
	}
	
	@Test
	public void testAddgetFreqQuery() {
		QueryDepotTree qdt = new QueryDepotTree();
		qdt.incFreqQuery("a");
		qdt.incFreqQuery("a");
		qdt.incFreqQuery("a");
		qdt.incFreqQuery("a");
		qdt.incFreqQuery("a");
		qdt.incFreqQuery("a");
		qdt.incFreqQuery("a");
		assertEquals(7, qdt.getFreqQuery("a"));
	}
}
