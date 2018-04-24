package es.uned.lsi.eped.pract2017_2018;

import es.uned.lsi.eped.DataStructures.GTree;
import es.uned.lsi.eped.DataStructures.GTreeIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.ListIF;

public class QueryDepotTree implements QueryDepotIF {

	private GTreeIF<Character> primerNodo;
	
	public QueryDepotTree() {
		primerNodo = new GTree<Character>();
	}
	
	
	public int numQueries() {
		IteratorIF<GTreeIF<Character>> itr = primerNodo.getChildren().iterator();
		while(itr.hasNext()) {
			GTreeIF<Character> temp = itr.getNext();
			
		}
		return 0;
	}

	public int getFreqQuery(String q) {
		return 0;
	}

	@Override
	public ListIF<Query> listOfQueries(String prefix) {
		return null;
	}

	public void incFreqQuery(String q) {
		
	}

}
