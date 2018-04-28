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
		
		if(!nodo.isLeaf()) {
			ListIF<GTreeIF<Character>> listaAux = nodo.getChildren();
			IteratorIF<GTreeIF<Character>> itr = listaAux.iterator();
			while(itr.hasNext()) {
				GTreeIF<Character> temp = itr.getNext();
				aux = aux + obtenerNumeroHojas(temp);
			}
		}else {
			return 1;
		}
		
		return aux;
	}
	
	/**
	 * Devuelve la frecuencia de una consulta en el deposito
	 * @param q Cadena de caracteres de la consulta
	 * @return returnVal Frecuencia de la consulta pasada como argumento
	 */
	public int getFreqQuery(String q) {	
		return obtenerFrecuenciaConsulta(primerNodo, q, 0);
	}
	
	/**
	 * Metodo auxiliar de getFreqQuery(...)
	 * 
	 * El metodo utiliza la recursividad para buscar la frecuencia de
	 * una consulta. 
	 * 
	 * Va comprobando los hijos de cada letra que este contenida en la 
	 * consulta hasta que encuentra una letra hoja que es hija de
	 * una letra de la consulta, en cuyo caso devuelve el valor
	 * de la letra que, pasado a entero, es la frecuencia de la misma.
	 * 
	 * La recursividad se para en caso de no haber encontrado la frecuencia
	 * pero no tener mas letras que coincidan con la de la consulta.
	 * 
	 * @param nodo Nodo a comprobar
	 * @param q Texto de la consulta
	 * @param i Iterador para las letras de la consulta. i == q.length()
	 * @return frecuenciaInterna Frecuencia de la consulta q
	 */
	private int obtenerFrecuenciaConsulta(GTreeIF<Character> nodo, String q, int i) {
		ListIF<GTreeIF<Character>> listaAux = nodo.getChildren();
		char[] aux = q.toCharArray();
		int frecuenciaInterna = 0;
		
		if(!nodo.isLeaf() && i <= q.length()) {
			//Por cada nodo iteramos todos sus hijos
			IteratorIF<GTreeIF<Character>> itr = listaAux.iterator();
			while(itr.hasNext()) {
				GTreeIF<Character> temp = itr.getNext(); //Hijo a tratar
				
				//Si hay un hijo con una letra de la consulta a encontrar la frecuencia
				if(temp.getRoot().equals(aux[i]) && !temp.isLeaf()) {
					//Sumamos a frecuenciaInterna el metodo recursivo
					frecuenciaInterna = obtenerFrecuenciaConsulta(temp, q, i+1);
				}
				
				//Si se tiene como padre la ultima letra de la consulta 
				// y es hoja quiere decir que el nodo contiene la frecuencia
				//de la consulta
				if(temp.isLeaf() && i==q.length()) {
					//Devuelve el valor de la frecuencia de la consulta
					return (int) temp.getRoot();
				}
			}
		}
		return frecuenciaInterna;
	}

	@Override
	public ListIF<Query> listOfQueries(String prefix) {
		return null;
	}
	
	/**
	 * Pasa un pasa un numero a una letra
	 * @return
	 */
	private char numeroALetra(int numero) {
		return (char) numero;
	}

	/**
	 * Añade una consulta al deposito
	 * 
	 * @param q Texto de la consulta
	 */
	public void incFreqQuery(String q) {
		//Comprobamos el tamaño del deposito
		if(numQueries()==0) {
			
		}else { //Si hay una o mas consultas en el deposito
			
		}
		
	}
	
	private void añadirConsulta(GTreeIF<Character> nodo, String q, int i) {
		if(i<=q.length()) {
			char[] aux = q.toCharArray();
			boolean letraEncontrada = false;
			ListIF<GTreeIF<Character>> listaAux = nodo.getChildren();
			IteratorIF<GTreeIF<Character>> itr = listaAux.iterator();
			while(itr.hasNext()) {
				GTreeIF<Character> temp = itr.getNext();
				if(temp.getRoot().equals(aux[i])) {
					letraEncontrada = true;
					añadirConsulta(temp, q, i+1);
				}
			}
			
			if(!letraEncontrada) {
				GTreeIF<Character> nuevoNodo = new GTree<Character>();
				nuevoNodo.setRoot(aux[i]);
				listaAux.insert(nuevoNodo, listaAux.size());
			}
		}
	}

}
