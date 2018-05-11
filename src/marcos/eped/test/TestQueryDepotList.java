package marcos.eped.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.ListIF;
import es.uned.lsi.eped.pract2017_2018.Query;
import es.uned.lsi.eped.pract2017_2018.QueryDepotList;

public class TestQueryDepotList {
	
	/*public void testAddlistOfQueries() throws IOException {
		QueryDepotList qdl = new QueryDepotList("src/Debugging_Consultas.txt");
		ListIF<Query> lista = qdl.listOfQueries("car");
	}*/
	
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
	
	/*@Test
	public void testAddCompararLexicografia() {
		QueryDepotList qdl = new QueryDepotList();
		assertEquals(1, qdl.compararLexicograficamente("cappuccino palmier", "cappuccino piastra"));
	}*/
	
	/*@Test
	public void testAddListaOrdenada() {	
		QueryDepotList qdl = new QueryDepotList();
		qdl.incFreqQuery("perro");
		qdl.incFreqQuery("perrito");
		qdl.incFreqQuery("perrito");
		qdl.incFreqQuery("perrito");
		qdl.incFreqQuery("perrito");
		qdl.incFreqQuery("perri");
		qdl.incFreqQuery("Perro");
		qdl.incFreqQuery("Perro");
		qdl.incFreqQuery("  Perro");
		qdl.incFreqQuery(".Perro");
		qdl.incFreqQuery("pherro");
		qdl.incFreqQuery("barbacoa");
		qdl.incFreqQuery("barbacoa");
		qdl.incFreqQuery("barbacoa");
		qdl.incFreqQuery("barbacoa");
		qdl.incFreqQuery("barbacoa");
		qdl.incFreqQuery("BarBacOa");
		qdl.incFreqQuery("BARBACOA");
		qdl.incFreqQuery("BARBAcoa");
		qdl.incFreqQuery("abc");
		qdl.incFreqQuery("abc");
		qdl.incFreqQuery("aBC");
		qdl.incFreqQuery("aBC");
		qdl.incFreqQuery("aBC");
		qdl.incFreqQuery("aBC");
		qdl.incFreqQuery("aBC");
		qdl.incFreqQuery("aBC");
		qdl.incFreqQuery("aBCdefg");
		qdl.incFreqQuery("Estufa");
		qdl.incFreqQuery("EEEEstufa");
		qdl.incFreqQuery("zapatero");
		qdl.incFreqQuery("Zapatero.");
		qdl.incFreqQuery("z.apatero");
		qdl.incFreqQuery("z.apatero");
		qdl.incFreqQuery("zap.atero");
		qdl.incFreqQuery("b1rb3coa");
		qdl.incFreqQuery("123");
		qdl.incFreqQuery("1234");
		qdl.incFreqQuery("123456");
		qdl.incFreqQuery("a1b2c3");
		qdl.incFreqQuery("a1b2c3");
		qdl.incFreqQuery(".1a");
		qdl.incFreqQuery(".1abaa");
		qdl.incFreqQuery(".1cd");
		qdl.incFreqQuery(".2a");
		qdl.incFreqQuery(".2a");
		qdl.incFreqQuery(".3a");
		
		/*ListIF<Query> lPrefijo = qdl.obtenerListaPrefijo("per");
		IteratorIF<Query> itrlP = lPrefijo.iterator();
		while(itrlP.hasNext()) {
			Query temp = itrlP.getNext();
			System.out.println("Freq=(" + temp.getFreq() + ") " + temp.getText());
		}
		
		int maxFreq = qdl.obtenerMaxFrecuencia(lPrefijo);
		System.out.println("maxfreq = " + maxFreq);
		
		ListIF<Query> lOrdenada = qdl.obtenerListaOrdenada(lPrefijo, maxFreq);
		IteratorIF<Query> itrlO = lOrdenada.iterator();		while(itrlO.hasNext()) {
			Query temp = itrlO.getNext();
			System.out.println("Freq=(" + temp.getFreq() + ") " + temp.getText());
		}
				
		ListIF<Query> listaLexicografica = qdl.listOfQueries("abc");
		IteratorIF<Query> itrlL = listaLexicografica.iterator();
		while(itrlL.hasNext()) {
			Query temp = itrlL.getNext();
			System.out.println( "(" + temp.getFreq() + ") " + temp.getText());
		}
		
	}*/
	
	@Test
	public void testAddTamaño() {
		QueryDepotList qdl = new QueryDepotList();
		qdl.incFreqQuery("caca");
		assertEquals(1, qdl.numQueries());
	}
	
	@Test
	public void testAddListOfQueries() throws IOException {
		QueryDepotList qdl = new QueryDepotList("src/Debugging_Consultas.txt");
		
		ListIF<Query> l = qdl.listOfQueries("cappuccino");
		IteratorIF<Query> itr = l.iterator();
		while(itr.hasNext()) {
			Query temp = itr.getNext();
			System.out.println( "(" + temp.getFreq() + ") " + temp.getText());
		}
	}
	
	@Test
	public void testAddnumQueries() throws IOException{
		QueryDepotList qdl = new QueryDepotList("src/JdP-consultas.txt");
		System.out.println(qdl.numQueries());
	}

}
