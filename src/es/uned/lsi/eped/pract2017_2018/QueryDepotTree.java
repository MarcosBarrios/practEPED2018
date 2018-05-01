package es.uned.lsi.eped.pract2017_2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import es.uned.lsi.eped.DataStructures.GTree;
import es.uned.lsi.eped.DataStructures.GTreeIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.ListIF;

public class QueryDepotTree implements QueryDepotIF {
	
	//Nodo raiz del deposito
	private GTreeIF<Query> primerNodo;
	
		
	//Metodo constructor
	public QueryDepotTree() {
		primerNodo = new GTree<Query>();
	}
	
	//Metodo constructor por archivo
	public QueryDepotTree(String pathFile) throws IOException{
		//Lee las consultas de un archivo y las mete en el deposito
		primerNodo = new GTree<Query>();
		FileReader registroConsultas;
		registroConsultas = new FileReader(pathFile);
		BufferedReader lector = new BufferedReader(registroConsultas);
        String consulta;
        while((consulta = lector.readLine())!=null) {
        	incFreqQuery(consulta);
        }
        lector.close();
	}
	
	/**
	 * Devuelve el numero de consultas en el deposito
	 * 
	 * @return aux Numero de consultas del deposito
	 */
	public int numQueries() {	
		//Obtenemos la lista de hijos hoja del nodo inicial del deposito
		//El numero de consultas es igual al numero de frecuencias mayor
		//que 0 almacenadas
		return obtenerNumeroConsultas(primerNodo);
	}
	
	/**
	 * Itera todos los hijos de un nodo y aumenta aux en 1 cada vez
	 * que encuentra una frecuencia diferente de 0.
	 * @param nodo Nodo a iterar
	 * @return aux Numero de hojas
	 */
	private int obtenerNumeroConsultas(GTreeIF<Query> nodo) {
		int aux = 0;
		ListIF<GTreeIF<Query>> listaAux = nodo.getChildren();
		IteratorIF<GTreeIF<Query>> itr = listaAux.iterator();
		while(itr.hasNext()) {
			
			GTreeIF<Query> temp = itr.getNext();
			if(temp.getRoot().getFreq()>0) {
				aux++;
			}
			aux = aux + obtenerNumeroConsultas(temp);
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
	 * Va comprobando los hijos de cada nodo hasta llegar a la ultima
	 * letra de la consulta, de la cual obtiene la frecuencia. 
	 * 
	 * En caso de no encontrar ninguna letra devuelve 0.
	 * 
	 * 
	 * @param nodo Nodo a comprobar
	 * @param q Texto de la consulta
	 * @param i Iterador para las letras de la consulta. i == q.length()
	 * @return frecuenciaInterna Frecuencia de la consulta q
	 */
	private int obtenerFrecuenciaConsulta(GTreeIF<Query> nodo, String q, int i) {
		ListIF<GTreeIF<Query>> listaAux = nodo.getChildren();
		char[] aux = q.toCharArray();
		int frecuenciaInterna = 0;
		
		//Si el nodo tiene hijos y no ha llegado a la ultima letra de la consulta
		if(nodo.getNumChildren()>0 && i < q.length()) {
			//Por cada nodo iteramos todos sus hijos
			IteratorIF<GTreeIF<Query>> itr = listaAux.iterator();
			while(itr.hasNext()) {
				GTreeIF<Query> temp = itr.getNext(); //Hijo a tratar
				String textoLetra = "" + aux[i];
				
				//Si encuentra la letra que siguen en la secuencia
				if(temp.getRoot().getText().equals(textoLetra)) {
					frecuenciaInterna = obtenerFrecuenciaConsulta(temp, q, i+1);
					
					//Si la letra encontrada es la ultima en la consulta
					if((i+1)==q.length()) { 
						//Devuelve la frecuencia de la consulta 
						return temp.getRoot().getFreq();
					}
				}
			}
		}
		return frecuenciaInterna;
	}

	/**
	 * Devuelve una lista con las consultas del deposito
	 * ordenadas de mayor a menor en orden lexicografico
	 * @param prefix - El prefijo de la consulta
	 */
	public ListIF<Query> listOfQueries(String prefix) {
		return null;
	}

	/**
	 * Añade una consulta al deposito
	 * 
	 * @param q Texto de la consulta
	 */
	public void incFreqQuery(String q) {
		if(primerNodo==null) {
			System.out.println("null");
		}
		añadirConsulta(primerNodo, q, 0);
	}
	
	/**
	 * Metodo auxiliar de incFreqQuery(...)
	 * 
	 * Va iterando el arbol segun la secuencia de letras obtenida del
	 * parametro q con la ayuda de una variable contadora i.
	 * 
	 * Primero comprueba si existe un nodo que contenga la letra actual en
	 * la lista de hijos. En caso de que no haya letra se añade insertandola
	 * en la lista de hijos y aumentando su frecuencia.
	 * 
	 * Por otro lado, si hay un nodo con la letra, simplemente se aumenta la
	 * frecuencia de este y se pasa a la siguiente letra mediante una llamada
	 * recursiva al propio metodo pero aumentando i en 1 para poder comprobar
	 * la siguiente letra en la secuencia del texto de la consulta.
	 * 
	 * @param nodo Nodo con la letra a comprobar
	 * @param q Texto de la consulta
	 * @param i Contador. 0 <= i < q.length()
	 */
	private void añadirConsulta(GTreeIF<Query> nodo, String q, int i) {
		
		//Si i es menor que el tamaño de la consulta a introducir en el deposito
		if(i<q.length()) {
			
			//Obtenemos las letras a parte del texto de la consulta
			char[] aux = q.toCharArray();
			
			//Por cada letra de la consulta se busca en la lista de hijos
			//y va comprobando si esta contiene la letra en la posicion i
			//del texto de la  consulta. En caso de que exista llama
			//al metodo sobre la letra, si no encuentra la letra la introduce
			//en la lista y se llama de nuevo a añadirConsulta(...) sobre la 
			//siguiente letra
			boolean letraEncontrada = false;
			ListIF<GTreeIF<Query>> listaAux = nodo.getChildren();
			IteratorIF<GTreeIF<Query>> itr = listaAux.iterator();
			while(itr.hasNext()) {
				GTreeIF<Query> temp = itr.getNext();
				String letraNodo = "" + aux[i];
				
				//Si encuentra una letra de consulta se aumenta la frecuencia
				//en 1 y se llama de nuevo a este metodo sobre esa letra
				//para empezar de nuevo el proceso
				if(temp.getRoot().getText().equals(letraNodo)) {
					letraEncontrada = true;
					
					//Solo aumenta la frecuencia si es la ultima letra de la consulta
					if((i+1)==q.length()) {
						temp.getRoot().setFreq(temp.getRoot().getFreq()+1);
					}
					añadirConsulta(temp, q, i+1);
				}
			}
			
			//En caso de que no se haya encontrado una letra se añade al arbol
			//y se llama a añadirConsulta(...) de nuevo pero con el nuevo arbol
			//añadido
			if(!letraEncontrada) {
				GTreeIF<Query> nuevoNodo = new GTree<Query>();
				String temp = "" + aux[i];
				Query nuevoQuery = new Query(temp);
				
				//Solo aumenta la frecuencia si es la ultima letra de la consulta
				if((i+1)==q.length()) {
					nuevoQuery.setFreq(1);
				}
				nuevoNodo.setRoot(nuevoQuery);
				listaAux.insert(nuevoNodo, listaAux.size()+1);
				añadirConsulta(nuevoNodo, q, i+1);
			}
		}
	}

}
