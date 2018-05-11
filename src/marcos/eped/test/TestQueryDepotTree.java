package marcos.eped.test;

import org.junit.jupiter.api.Test;

import es.uned.lsi.eped.pract2017_2018.QueryDepotTree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

public class TestQueryDepotTree {

	/*
	 * Por cada consulta solo se aumenta la frecuencia
	 * del ultimo nodo del texto de la consulta, de esta manera
	 * cuando se itera el arbol se puede saber que secuencia
	 * de letras forma una consulta.
	 * 
	 * Se iteran los hijos, si se encuentra una letra con una frecuencia
	 * diferente de 0 se aumenta en 1 la variable que contiene el numero
	 * de consultas en el deposito.
	 */
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

	/*Metodo usado para testear el metodo de qdt que obtiene una consulta diferente
	 * cada vez que se le llama que no este en una lista x @Test
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
