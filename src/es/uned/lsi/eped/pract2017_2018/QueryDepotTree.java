package es.uned.lsi.eped.pract2017_2018;

import es.uned.lsi.eped.DataStructures.GTree;
import es.uned.lsi.eped.DataStructures.GTreeIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

public class QueryDepotTree implements QueryDepotIF {

	//Nodo raiz del deposito
	private GTreeIF<Character> primerNodo;
	
	//Metodo constructor
	public QueryDepotTree() {
		primerNodo = new GTree<Character>();
	}
	
	/**
	 * Devuelve el numero de consultas en el deposito
	 * 
	 * @return aux Numero de consultas del deposito
	 */
	public int numQueries() {	
		int aux = 0;
		if(!primerNodo.isEmpty()) {
			//Obtenemos la lista de hijos hoja del nodo inicial del deposito
			//El numero de consultas es igual al numero de frecuencias almacenadas
			//y cada frecuencia es almacenada en un nodo hoja por lo que solo es 
			//necesario calcular cuantas hojas hay en el deposito.
			aux = obtenerNumeroHojas(primerNodo);
		}
		
		return aux;
	}
	
	/**
	 * Itera todos los hijos de un nodo para calcular el numero de hojas.
	 * @param nodo Nodo a iterar
	 * @return aux Numero de hojas
	 */
	private int obtenerNumeroHojas(GTreeIF<Character> nodo) {
		int aux = 0;
		
		ListIF<GTreeIF<Character>> listaAux = nodo.getChildren();
		IteratorIF<GTreeIF<Character>> itr = listaAux.iterator();
		while(itr.hasNext()) {
			GTree<Character> temp = (GTree<Character>) itr.getNext();
			if(temp.isLeaf()) {
				aux++;
			}else {
				//Obtenemos el numero de hojas del hijo mediante una
				//llamada recursiva
				aux = aux + obtenerNumeroHojas(temp);
			}
		}
		
		return aux;
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
