package marcos.eped.test;

import org.junit.jupiter.api.Test;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.ListIF;
import es.uned.lsi.eped.pract2017_2018.Query;
import es.uned.lsi.eped.pract2017_2018.QueryDepotTree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

public class TestQueryDepotTree {

	@Test
	public void testAddlistOfQueries() throws IOException {
		QueryDepotTree qdt = new QueryDepotTree("src/JdP-consultas.txt");
		ListIF<Query> lista = qdt.listOfQueries("car");
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
	public void testAddIncFreqQuery() {
		QueryDepotTree qdt = new QueryDepotTree();
		qdt.incFreqQuery("Prueba");
		assertEquals(1, qdt.numQueries());
	}

	/*@Test
	public void testAddObtenerConsultas() throws IOException{
		QueryDepotTree qdt = new QueryDepotTree("src/Debugging_Consultas.txt");
		System.out.println(qdt.numQueries());
		ListIF<Query> lista = qdt.obtenerConsultas();
		IteratorIF<Query> itr = lista.iterator();
		System.out.println("lul: " + lista.size());
		while(itr.hasNext()) {
			Query temp = itr.getNext();
			System.out.println(temp.getText() + "- (" + temp.getFreq() + ") ");
		}
	}*/
	
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
