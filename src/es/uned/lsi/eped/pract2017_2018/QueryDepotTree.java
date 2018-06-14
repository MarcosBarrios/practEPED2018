package es.uned.lsi.eped.pract2017_2018;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import es.uned.lsi.eped.DataStructures.GTree;
import es.uned.lsi.eped.DataStructures.GTreeIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

public class QueryDepotTree implements QueryDepotIF {
	
	//Nodo raiz del deposito
	private GTreeIF<Query> primerNodo;
	
	private int numeroConsultas;
		
	//Metodo constructor
	public QueryDepotTree() {
		primerNodo = new GTree<Query>();
	}
	
	//Metodo constructor por archivo
	public QueryDepotTree(String pathFile){
		//Lee las consultas de un archivo y las mete en el deposito
		primerNodo = new GTree<Query>();
		Path direccion = Paths.get(pathFile);
		try(BufferedReader lector = Files.newBufferedReader(direccion, StandardCharsets.UTF_8);) {
	    	String consulta;
	    	while((consulta = lector.readLine())!=null) {
	    		incFreqQuery(consulta);
	    	}
		}catch(Exception e) {
			System.err.println("No se pudo obtener las consultas. ");
		}
	}
	
	/**
	 * @return Primer nodo que contiene el deposito
	 */
	private GTreeIF<Query> obtenerDeposito(){
		return primerNodo;
	}
	
	/**
	 * Incrementa el numero de consultas en 1
	 */
	private void incrementarNumeroConsultas() {
		numeroConsultas++;
	}

	/**
	 * @return Numero de consultas del deposito
	 */
	public int numQueries() {
		return numeroConsultas;
	}
	
	/**
	 * @param q Cadena de caracteres de la consulta
	 * @return Frecuencia de la consulta asociada al texto 
	 * pasado como parametro
	 */
	public int getFreqQuery(String q) {
		return obtenerFrecuenciaConsulta(obtenerDeposito(), q, 0);
	}
	
	/**
	 * Metodo recursivo de getFreqQuery(...)
	 * 
	 * El metodo utiliza la recursividad para obtener la frecuencia de
	 * una consulta.
	 * 
	 * Va comprobando los hijos de cada nodo hasta llegar a la ultima
	 * letra de la consulta. Se comprueba que el nodo asignado a la ultima
	 * letra de la consulta tenga un nodo hoja del cual se obtiene la
	 * frecuencia. 
	 * 
	 * En caso de no encontrar alguna letra devuelve 0.
	 * 
	 * @param nodo Nodo a comprobar
	 * @param q Texto de la consulta
	 * @param i Iterador para las letras de la consulta. i == q.length()
	 * @return frecuenciaInterna Frecuencia de la consulta q
	 */
	private int obtenerFrecuenciaConsulta(GTreeIF<Query> nodo, String q, int i) {
		int frecuenciaInterna = 0;
		
		if(nodo.getNumChildren()>0 && i<q.length()) {
			ListIF<GTreeIF<Query>> listaAux = nodo.getChildren();
			IteratorIF<GTreeIF<Query>> itr = listaAux.iterator();
			while(itr.hasNext()) {
				GTreeIF<Query> temp = itr.getNext(); //Hijo a tratar
				String caracterActual = "" + q.charAt(i);
				
				if(temp.getRoot() != null && 
						temp.getRoot().getText().equals(caracterActual)) {
					frecuenciaInterna = obtenerFrecuenciaConsulta(temp, q, i+1);
					
					if((i+1)==q.length()) { 
						//Si hemos llegado al ultimo caracter de la consulta
						GTreeIF<Query> nodoFrecuencia = obtenerNodoDeFrecuencia(temp);
						if(nodoFrecuencia!=null)return nodoFrecuencia.getRoot().getFreq();
					}
				}
			}
		}
		return frecuenciaInterna;
	}

	/**
	 * @param prefix Prefijo que tienen que tener las consultas
	 * @return Lista con las consultas del deposito ordenadas de
	 * mayor a menor frecuencia y por orden lexicografico
	 */
	public ListIF<Query> listOfQueries(String prefix) {
		
		//Obtenemos el arbol con las consultas que empiezan por el prefijo
		GTreeIF<Query> arbolPrefijo = obtenerArbolPrefijo(obtenerDeposito(), prefix, 0);
		
		//Obtenemos el arbol ordenado lexicograficamente y por frecuencias
		GTreeIF<Query> arbolLexicografico = obtenerArbolLexicografico(arbolPrefijo, prefix);
		
		return arbolALista(arbolLexicografico, 0, "", "");
	}
	
	/**
	 * Para cada frecuencia (mayor a menor) se extrae de "arbolPrefijo" 
	 * un arbol cuyas consultas tengan una misma frecuencia que luego es 
	 * ordenado lexicograficamente e insertado como hijo de la raiz del 
	 * arbol lexicografico que devuelve el metodo.
	 * 
	 * @param arbolPrefijo Arbol cuyas consultas empiezan por un prefijo
	 * @return Arbol ordenado lexicograficamente y por frecuencias.
	 */
	private GTreeIF<Query> obtenerArbolLexicografico(GTreeIF<Query> arbolPrefijo, String prefijo){
		GTreeIF<Query> raiz = new GTree<Query>();
		raiz.setRoot(new Query(""));
		
		ListIF<Integer> listaFrecuencias = listaFrecuencias(arbolPrefijo);
		IteratorIF<Integer> itr = listaFrecuencias.iterator();
		while(itr.hasNext()) { //Por cada frecuencia (mayor a menor)
			int frecuencia = itr.getNext();
			
			//Arbol cuyas consultas tienen frecuencia igual a "frecuencia"
			GTreeIF<Query> arbolFrecuencia = obtenerArbolMismaFrecuencia(arbolPrefijo, frecuencia, prefijo, "");
			
			ordenarArbolLexicograficamente(arbolFrecuencia);
			
			//Anadimos cada nodo de "arbolFrecuencia" a "raiz"
			raiz.addChild(raiz.getNumChildren()+1, arbolFrecuencia);
		}
		return raiz;
	}
	
	/**
	 * Obtiene una lista de todas las consultas diferentes en un arbol y
	 * la ordena de mayor a menor.
	 * 
	 * @param arbol Arbol con las consultas
	 * @return Lista de todas las diferentes frecuencias en "arbol" ordenadas 
	 * de mayor a menor.
	 */
	private ListIF<Integer> listaFrecuencias(GTreeIF<Query> arbol){
		ListIF<Integer> listaFrecuencias = obtenerFrecuencias(arbol);
		
		ordenarListaDescendentemente(listaFrecuencias);
		
		return listaFrecuencias;
	}
	
	/**
	 * @param arbol Arbol de consultas
	 * @return Lista con las diferentes frecuencias en un arbol de consultas
	 */
	private ListIF<Integer> obtenerFrecuencias(GTreeIF<Query> arbol){
		ListIF<Integer> l = new List<Integer>();
		
		ListIF<GTreeIF<Query>> lista = arbol.getChildren();
		IteratorIF<GTreeIF<Query>> itr = lista.iterator();
		while(itr.hasNext()) { //Iteramos los hijos de nodo
			GTreeIF<Query> temp = itr.getNext();
			int f = temp.getRoot().getFreq();
			if(!l.contains(f) && f>0){
				l.insert(f, l.size()+1);
			}
			
			//Insertamos las frecuencias obtenidas de los hijos
			ListIF<Integer> aux = listaFrecuencias(temp);
			IteratorIF<Integer> itr2 = aux.iterator();
			while(itr2.hasNext()) {
				int fHijos = itr2.getNext();
				if(!l.contains(fHijos) && fHijos>0){
					l.insert(fHijos, l.size()+1);
				}
			}
		}
		return l;
	}
	
	/**
	 * O(N*N)
	 * Ordena descendentemente (Mayor a menor) una lista de enteros
	 * mediante el algoritmo de ordenamiento burbuja.
	 * 
	 * @param l Lista a ordenar descendentemente
	 */
	private void ordenarListaDescendentemente(ListIF<Integer> l){
		for(int i = 1; i <= l.size(); i++) {
			for(int j = 0; j <= l.size()-i; j++) {
				int e1 = l.get(j);
				int e2 = l.get(j+1);
				
				if(e1 < e2) {
					int aux = e1;
					l.set(j, e2);
					l.set(j+1, aux);
				}
			}
		}
	}
	
	/**
	 * Ordena lexicográficamente un árbol
	 * 
	 * @param arbol Arbol ordenado por frecuencia
	 */
	private void ordenarArbolLexicograficamente(GTreeIF<Query> arbol){
		ListIF<GTreeIF<Query>> listaLexicografica = arbol.getChildren();
		ordenarListaLexicograficamente(listaLexicografica);
		
		IteratorIF<GTreeIF<Query>> itr = listaLexicografica.iterator();
		while(itr.hasNext()) {
			ordenarArbolLexicograficamente(itr.getNext());
		}
	}
	
	/**
	 * Ordena lexicograficamente una lista de nodos mediante el logaritmo
	 * burbuja
	 * 
	 * @param nodo Nodo padre de la lista de hijos
	 */
	private void ordenarListaLexicograficamente(ListIF<GTreeIF<Query>> l){
		for(int j = 0; j < l.size(); j++) {
			for(int i = 1; i < l.size()-j; i++) {
				GTreeIF<Query> temp1 = l.get(i);
				GTreeIF<Query> temp2 = l.get(i+1);
				
				String cadena1 = temp1.getRoot().getText();
				String cadena2 = temp2.getRoot().getText();
				
				if(cadena1.compareTo(cadena2)>0) {
					//Si temp1 es mayor que temp2 lexicograficamente
					GTreeIF<Query> aux = temp1;
					l.set(i, temp2);
					l.set(i+1, aux);
				}
			}
		}
	}
	
	/**
	 * Devuelve una lista con las consultas de un arbol que tienen una frecuencia
	 * especifica.
	 * 
	 * @param nodo Nodo recursivo
	 * @param consulta Texto de la consulta
	 * @param frecuencia Frecuencia que tiene que tener la consulta para ser anadida
	 * @return raiz Arbol con consultas de una misma frecuencia
	 */
	private GTreeIF<Query> obtenerArbolMismaFrecuencia(GTreeIF<Query> nodo, int frecuencia, 
			String prefijo, String consulta){
		GTreeIF<Query> aux = new GTree<Query>();
		aux.setRoot(new Query(""));
		
		//Obtenemos una lista cuyas consultas tengan la misma frecuencia que la 
		//especificada en el encabezado
		ListIF<Query> listaConsultasFrecuencia = arbolALista(nodo, frecuencia, consulta, prefijo);
		IteratorIF<Query> itr = listaConsultasFrecuencia.iterator();
		while(itr.hasNext()) {
			//Iteramos la lista
			Query temp = itr.getNext();
			anadirConsulta(aux, temp.getFreq(), temp.getText(), 0);
		}
		
		return aux;
	}
	
	/**
	 * Devuelve el nodo que contiene la ultima letra de prefix, esto es, el nodo
	 * cuyos hijos son consultas que empiezan por prefix.
	 * 
	 * @param nodo Nodo recursivo
	 * @param prefix Prefijo de las consultas
	 * @param i variable recursiva
	 * @return temp Nodo con consultas que empiezan por prefix
	 */
	private GTreeIF<Query> obtenerArbolPrefijo(GTreeIF<Query> nodo, String prefix, int i){
		
		//Si i es menor que el numero de letras de prefix
		if(i<prefix.length()) {
			//Transformamos el prefijo a un array de letras
			char[] letras = prefix.toCharArray();
			
			ListIF<GTreeIF<Query>> l = nodo.getChildren();
			IteratorIF<GTreeIF<Query>> itr = l.iterator();
			while(itr.hasNext()) {
				GTreeIF<Query> temp = itr.getNext();
				//Obtenemos la letra en la posicion i de prefix
				String letraNodo = "" + letras[i];
				
				//Si se encuentra un nodo con una letra igual a la letra en la posicion i de prefix
				if(temp.getRoot().getText().equals(letraNodo)) {
					
					if((i+1)==prefix.length()) {
						return temp;
					}
					return obtenerArbolPrefijo(temp, prefix, i+1);
				}
			}
		}
		GTreeIF<Query> nodoVacio = new GTree<Query>();
		nodoVacio.setRoot(new Query(""));
		return nodoVacio;
	}
	
	/**
	 * Se comprueba si cada nodo tiene un hijo con una frecuencia igual a 
	 * "frecuencia" y en caso de tenerla crea un query que almacena la 
	 * cadena formada consulta y la frecuencia del ultimo nodo iterado y 
	 * anade ese query a una lista que es utilizada por el nodo padre para
	 * almacenar las consultas encontradas.
	 * 
	 * Si "frecuencia" es igual a 0 se obtienen las consultas cuya frecuencia sea
	 * mayor que 0 mientras que si "frecuencia" es mayor que 0 se obtienen las
	 * consultas cuya frecuencia sea igual a la especificada.
	 * 
	 * La cadena "consulta" esta formada por el conjunto de letras en cada nodo que
	 * ha sido recorrido hasta llegar al nodo actual. Esto permite que cuando se
	 * encuentre un nodo con frecuencia mayor que 0 se tenga una cadena de caracteres
	 * con el camino que se ha seguido hasta llegar al nodo actual.
	 * 
	 * @param nodo Nodo sobre el cual se esta trabajando
	 * @param frecuencia Frecuencia que tienen que tener las consultas
	 * @param consulta Texto de cada consulta obtenida
	 * @param prefijo Prefijo que tienen que tener las consultas
	 * @return Lista con las consultas almacenadas en el arbol
	 * que contiene el deposito.
	 */
	private ListIF<Query> arbolALista(GTreeIF<Query> nodo, int frecuencia, String consulta,
			String prefijo){
		ListIF<Query> l = new List<Query>();
		
		ListIF<GTreeIF<Query>> listaNodos = nodo.getChildren();
		IteratorIF<GTreeIF<Query>> itr = listaNodos.iterator();
		while(itr.hasNext()) { //Iteramos los hijos de "nodo"
			GTreeIF<Query> temp = itr.getNext();
			
			consulta = consulta.concat(temp.getRoot().getText());
			
			if(frecuencia>0 && temp.getRoot().getFreq()==frecuencia) {
				Query q = new Query(prefijo + consulta);
				q.setFreq(temp.getRoot().getFreq());
				l.insert(q, l.size()+1);
			}else if(frecuencia==0 && temp.getRoot().getFreq()>0){
				Query q = new Query(prefijo + consulta);
				q.setFreq(temp.getRoot().getFreq());
				l.insert(q, l.size()+1);
			}else {
				ListIF<Query> lConsultasHijos = arbolALista(temp, frecuencia, consulta, prefijo);
				IteratorIF<Query> itrl = lConsultasHijos.iterator();
				while(itrl.hasNext()) {
					//Insertamos en la lista "l" las consultas en los hijos de "temp"
					l.insert(itrl.getNext(), l.size()+1);
				}
			}
			
			if(!temp.getRoot().getText().equals("")) {
				consulta = consulta.substring(0, consulta.length()-1);
			}
		}
		return l;
	}
	
	/**
	 * Anade una consulta al deposito
	 * 
	 * @param q Texto de la consulta
	 */
	public void incFreqQuery(String q) {
		anadirConsulta(obtenerDeposito(), 0, q, 0);
	}

	/**
	 * Primero comprueba si existe un nodo que contenga la letra actual en
	 * la lista de hijos. En caso de que no haya letra se anade insertandola
	 * en la lista de hijos y aumenta su frecuencia.
	 * 
	 * Por otro lado, si hay un nodo con la letra, se comprueba si tiene un nodo 
	 * hoja con su frecuencia y en caso afirmativo se aumenta su frecuencia en 1.
	 *
	 * Si no encuentra un nodo hoja con frecuencia mayor que 0 entonces lo crea y
	 * asigna su frecuencia a 1.
	 * 
	 * A continuacion se realiza una llamada recursiva a anadirConsulta con el 
	 * parametro "i" aumentado en 1.
	 * 
	 * Si el parametro frecuencia es mayor que 0 la frecuencia asignada a la consulta
	 * sera igual a la frecuencia especificada mientras que si es igual a 0 simplemente
	 * se asigna a 1.
	 * 
	 * @param nodo Nodo con la letra a comprobar
	 * @param frecuencia Frecuencia de la consulta a anadir. Si frecuencia=0->frecuencia=1
	 * @param q Texto de la consulta
	 * @param i Contador. 0 <= i < q.length()
	 */
	private void anadirConsulta(GTreeIF<Query> nodo, int frecuencia, String q, int i) {
		if(i<q.length()) {
			GTreeIF<Query> nodoLetra = obtenerNodoConLetraEspecifica(nodo, ("" + q.charAt(i)) );
			if(nodoLetra!=null) {
				if((i+1)==q.length()) {
					aumentarFrecuenciaNodo(nodoLetra, frecuencia);
				}else {
					anadirConsulta(nodoLetra, frecuencia, q, i+1);
				}
			}else {
				GTreeIF<Query> nuevoNodo = new GTree<Query>();
				Query nuevoQuery = new Query( (""+ q.charAt(i)) );
				//Insertamos el nuevo nodo de frecuencia como hijo hoja de "nodo"
				ListIF<GTreeIF<Query>> listaHijos = nodo.getChildren();
				nuevoNodo.setRoot(nuevoQuery);
				listaHijos.insert(nuevoNodo, listaHijos.size()+1);
				if((i+1)==q.length()) {
					aumentarFrecuenciaNodo(nuevoNodo, frecuencia);
				}else {
					anadirConsulta(nuevoNodo, frecuencia, q, i+1);
				}
			}
		}
	}
	
	/**
	 * @param letra Letra a buscar
	 * @param nodoPadre Nodo padre sobre el que buscar
	 * @return Nodo hijo de "nodoPadre" cuya consulta tenga de texto la 
	 * letra "letra"
	 */
	private GTreeIF<Query> obtenerNodoConLetraEspecifica(GTreeIF<Query> nodoPadre, String letra){
		ListIF<GTreeIF<Query>> listaAux = nodoPadre.getChildren();
		IteratorIF<GTreeIF<Query>> itr = listaAux.iterator();
		while(itr.hasNext()) {
			GTreeIF<Query> temp = itr.getNext();
			if(temp.getRoot().getText().equals(letra)) {
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * Aumenta la frecuencia de un nodo obteniendo su nodo hoja
	 * hijo con la frecuencia de la consulta que encabeza.
	 * 
	 * @param nodo Nodo con la frecuencia a aumentar
	 */
	private void aumentarFrecuenciaNodo(GTreeIF<Query> nodo, int frecuencia) {
		GTreeIF<Query> nodoFrecuencia = obtenerNodoDeFrecuencia(nodo);

		if(nodoFrecuencia==null) {
			
			//Creamos un nodo frecuencia, aumentamos su frecuencia y lo insertamos
			//en la lista de hijos de nodoFrecuencia
			GTreeIF<Query> nuevoNodoFrecuencia = new GTree<Query>();
			nuevoNodoFrecuencia.setRoot(new Query(""));
			if(frecuencia==0) {
				nuevoNodoFrecuencia.getRoot().setFreq(1);
			}else {
				nuevoNodoFrecuencia.getRoot().setFreq(frecuencia);
			}
			nodo.addChild(nodo.getNumChildren()+1, nuevoNodoFrecuencia);

			incrementarNumeroConsultas();
		}else {
			nodoFrecuencia.getRoot().setFreq(nodoFrecuencia.getRoot().getFreq()+1);
		}
	}

	/**
	 * Obtiene un nodo hoja hijo de "nodo" que tenga texto igual a "".
	 * 
	 * Los nodos hoja cuyo texto es igual a "" son nodos de frecuencia que
	 * indican que su nodo padre es nodo cabeza de una consulta.
	 * 
	 * @param nodo Nodo padre del nodo hoja
	 * @return Nodo hoja hijo de "nodo"
	 */
	private GTreeIF<Query> obtenerNodoDeFrecuencia(GTreeIF<Query> nodo) {
		IteratorIF<GTreeIF<Query>> itr = nodo.getChildren().iterator();
		while(itr.hasNext()) {
			GTreeIF<Query> temp = itr.getNext();

			if(temp.isLeaf() && temp.getRoot().getText().equals("")) {
				return temp;
			}
		}
		return null;
	}

}