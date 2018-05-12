package marcos.eped.test;

import org.junit.jupiter.api.Test;

import es.uned.lsi.eped.pract2017_2018.QueryDepotTree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

public class TestQueryDepotTree {
	
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
