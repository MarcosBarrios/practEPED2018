package es.uned.lsi.eped.pract2017_2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import es.uned.lsi.eped.DataStructures.GTree;
import es.uned.lsi.eped.DataStructures.GTreeIF;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

public class QueryDepotTree implements QueryDepotIF {
	
	//Nodo raiz del deposito
	public GTreeIF<Query> primerNodo;
	
	private int numeroConsultas;
	
		
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
	 * Devuelve el primer nodo que contiene el deposito
	 * @return primerNodo Primer nodo (Raiz) del arbol
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
	
	public void imprimirArbolRecursivo(GTreeIF<Query> nodo) {
		ListIF<GTreeIF<Query>> l = nodo.getChildren();
		IteratorIF<GTreeIF<Query>> itr = l.iterator();
		System.out.println();
		if(nodo.getRoot()!=null) {
			if(nodo.getRoot().getText().equals("")) {
				System.out.print("[" + "F(" + nodo.getRoot().getFreq() + ")]");
			}else {
				System.out.print("[" + nodo.getRoot().getText() + "(" + nodo.getRoot().getFreq() + ")]");
			}
		}
		while(itr.hasNext()) {
			GTreeIF<Query> temp = itr.getNext();
			System.out.print(" ");
			
			if(temp.getRoot().getText().equals("")) {
				System.out.print("F(" + temp.getRoot().getFreq() + ") | ");
			}else {
				System.out.print(temp.getRoot().getText() + "(" + temp.getRoot().getFreq() + ") | ");
			}
			imprimirArbolRecursivo(temp);
		}
	}
	
	/**
	 * Devuelve el numero de consultas en el deposito
	 * 
	 * @return numeroConsultas Numero de consultas del deposito
	 */
	public int numQueries() {
		return numeroConsultas;
	}
	
	/**
	 * Devuelve la frecuencia de una consulta en el deposito
	 * @param q Cadena de caracteres de la consulta
	 * @return returnVal Frecuencia de la consulta pasada como argumento
	 */
	public int getFreqQuery(String q) {
		return obtenerFrecuenciaConsulta(obtenerDeposito(), q, 0);
	}
	
	/**
	 * Metodo auxiliar de getFreqQuery(...)
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
						//Obtiene el nodo hoja asociado y devuelve su frecuencia
						GTreeIF<Query> nodoFrecuencia = obtenerNodoHoja(temp);
						return nodoFrecuencia.getRoot().getFreq();
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
		
		/*
		 * Primero se pasan las consultas almacenadas en el arbol 
		 * a una lista de consultas ListIF<Query>:
		 * 
		 * 		1. Obtener las consultas almacenadas en forma de lista
		 * 
		 * Despues de obtener la lista, se procede igual que en la implementacion
		 * mediante lista:
		 * 
		 * 		2. Obtener lista con las palabras que empiezan con el prefijo
		 * 		3. Obtener la frecuencia maxima
		 * 		4. Crear una lista ordenada de mayor a menor frecuencia
		 * 		5. Ordenar lexicográficmente la lista
		 */
		
		//Obtenemos el arbol con las consultas que empiezan por el prefijo
		GTreeIF<Query> arbolPrefijo = obtenerArbolPrefijo(obtenerDeposito(), prefix, 0);
		
//		imprimirArbolRecursivo(arbolPrefijo);
		
		//Obtenemos la frecuencia maxima de las consultas almacenadas en arbolPrefijo
		//int frecuenciaMax = obtenerFrecuenciaMaxima(arbolPrefijo);
		
		//Obtenemos un arbol ordenado de mayor a menor frecuencia
		//GTreeIF<Query> arbolOrdenado = obtenerArbolMismaFrecuencia(arbolPrefijo, frecuencia, "", prefix);
		
		//Obtenemos el arbol ordenado lexicograficamente y por frecuencias
		GTreeIF<Query> arbolLexicografico = obtenerArbolLexicografico(arbolPrefijo, prefix);
		
		//Pasamos el arbol que contiene los arboles de las distintas frecuencias.
		ListIF<Query> depositoConsultas = obtenerListaConsultas(arbolLexicografico, 0, "", "");
		
		return depositoConsultas;
	}
	
	/**
	 * Devuelve un arbol ordenado lexicograficamente y por frecuencias.
	 * 
	 * @param arbolPrefijo Arbol cuyas consultas empiezan por un prefijo
	 * @return raiz Arbol lexicografico por frecuencias
	 */
	public GTreeIF<Query> obtenerArbolLexicografico(GTreeIF<Query> arbolPrefijo, String prefijo){
		GTreeIF<Query> raiz = new GTree<Query>();
		raiz.setRoot(new Query(""));
		
		int posAux = 0;
		ListIF<Integer> listaFrecuencias = obtenerListaFrecuencias(arbolPrefijo);
		IteratorIF<Integer> itr = listaFrecuencias.iterator();
		while(itr.hasNext()) { //Iteramos todas las frecuencias de mayor a menor
			int frecuencia = itr.getNext();
//			System.out.println("frecuencia " + frecuencia);
			
			//Obtenemos un arbol cuyas consultas tienen todas la misma frecuencia
			GTreeIF<Query> arbolFrecuencia = obtenerArbolMismaFrecuencia(arbolPrefijo, frecuencia, prefijo, "");
			
			//Ordenamos arbolFrecuencia lexicograficamente
			GTreeIF<Query> arbolLexicografico = ordenarArbolLexicograficamente(arbolFrecuencia);
			
			
			//Añadimos cada consulta en arbolLexicografico a raiz
			raiz.addChild(posAux+1, arbolLexicografico);
			posAux++;
		}
		return raiz;
	}
	
	/**
	 * Añade las consultas de un arbol a otro arbol.
	 * @param añadiendo Arbol al que se le añaden las consultas
	 * @param añadido Arbol con las consultas a añadir
	 * @return raiz Arbol añadiendo con las consultas de añadido añadidas.
	 */
	public GTreeIF<Query> incluirConsultasArbol(GTreeIF<Query> añadiendo, GTreeIF<Query> añadido, 
			String prefijo, String consulta){
		
		ListIF<Query> l = obtenerListaConsultas(añadido, 0, "", "");
		IteratorIF<Query> itr = l.iterator();
		while(itr.hasNext()) {
			Query temp = itr.getNext();
			
			//Quitamos el prefijo del principio de la consulta para no repetirlo
			String nombreConsulta = temp.getText().substring(prefijo.length(), temp.getText().length());
			añadirConsulta(añadiendo, temp.getFreq(), nombreConsulta, 0);
		}
		
		return añadiendo;
	}
	
	/**
	 * Devuelve la lista de frecuencias ordenadas de mayor a menor.
	 * 
	 * @param arbolFrecuencias Arbol de consultas
	 * @return l
	 */
	public ListIF<Integer> obtenerListaFrecuencias(GTreeIF<Query> arbolFrecuencias){
		ListIF<Integer> l = new List<Integer>();
		
		//Obtenemos todas las frecuencias diferentes en arbolFrecuencias
		ListIF<GTreeIF<Query>> lista = arbolFrecuencias.getChildren();
		IteratorIF<GTreeIF<Query>> itr = lista.iterator();
		while(itr.hasNext()) { //Iteramos los hijos de nodo
			GTreeIF<Query> temp = itr.getNext();
			int frecuencia = temp.getRoot().getFreq();
			if(!l.contains(frecuencia) && frecuencia>0){
				l.insert(frecuencia, l.size()+1);
			}
			
			//Añadimos todas las frecuencias en los hijos de cada nodo
			ListIF<Integer> aux = obtenerListaFrecuencias(temp);
			IteratorIF<Integer> itr2 = aux.iterator();
			while(itr2.hasNext()) {
				int temp2 = itr2.getNext();
				if(!l.contains(temp2) && temp2>0){
					l.insert(temp2, l.size()+1);
				}
			}
		}
		
		//Ordenamos la lista obtenida de mayor a menor
		//mediante el logaritmo burbuja
		for(int i = 0; i < l.size(); i++) {
			for(int j = 0; j < l.size(); j++) {
				int freq1 = l.get(i);
				int freq2 = l.get(i+1);
				
				//Guardamos freq2 y sustituimos freq1 en freq2
				//posteriormente sustituimos aux(freq2) en la vieja
				//posicion de freq1
				if(freq2 > freq1) {
					int aux = freq2;
					l.set(i+1, freq1);
					l.set(i, aux);
				}
			}
		}
		
		return l;
	}
	
	/**
	 * Ordena lexicográficamente un árbol de una sola frecuencia.
	 * 
	 * @param arbolPorFrecuencia Arbol ordenado por frecuencia
	 * @return raiz Arbol ordenado por frecuencia y lexicográficamente
	 */
	public GTreeIF<Query> ordenarArbolLexicograficamente(GTreeIF<Query> arbolPorFrecuencia){
		GTreeIF<Query> aux = new GTree<Query>();
		aux.setRoot(arbolPorFrecuencia.getRoot());
		
		ListIF<GTreeIF<Query>> listaLexicografica = ordenarLexicograficamente(arbolPorFrecuencia);
		IteratorIF<GTreeIF<Query>> itr = listaLexicografica.iterator();
		while(itr.hasNext()) { //Iteramos los hijos de nodo
			GTreeIF<Query> temp = itr.getNext();
			//Ordenamos la lista de nodos lexicograficamente por cada hijo del nodo actual
			GTreeIF<Query> nuevoHijo = temp;
			nuevoHijo = ordenarArbolLexicograficamente(nuevoHijo);
			aux.addChild(aux.getNumChildren()+1, nuevoHijo);
		}
		return aux;
	}
	
	/**
	 * Ordena una lista de nodos mediante el logaritmo de la burbuja
	 * @param nodo Nodo padre de la lista de hijos
	 * @return l Lista de nodos ordenados lexicograficamente
	 */
	public ListIF<GTreeIF<Query>> ordenarLexicograficamente(GTreeIF<Query> nodo){
		ListIF<GTreeIF<Query>> l = nodo.getChildren();
		
		//O(U*U) donde U es el numero de nodos
		for(int i = 1; i <= l.size()-1; i++){
			for(int j = 0; j <= l.size()-1; j++) {
				GTreeIF<Query> temp1 = l.get(i);
				GTreeIF<Query> temp2 = l.get(i+1);
				
				//Compara la lexicografia de temp2 con respecto a temp1
				//Si comparacion==-1 entonces temp2 va antes de temp1 lexicograficamente
				//Si comparacion==0 entonces temp2 y temp1 son iguales lexicograficamente
				//Si comparacion==1 entonces temp2 va despues de temp1 lexicograficamente
				//O(K) (K = nº caracteres consulta con menos caracteres)
				int comparacion = compararLexicograficamente(temp1.getRoot().getText(), temp2.getRoot().getText());
				
				//Si temp2 es menor que temp1 lexicograficamente
				//O si temp2 es igual que temp1 pero la consulta es mas pequeña
				if(comparacion==-1 || comparacion==0) {
					//Procedemos a intercambiar las posicones de 
					//los nodos comparados
					GTreeIF<Query> aux = temp2;
					l.set(i+1, temp1);
					l.set(i, aux);
				}
			}
		}	
		
		return l;
	}
	
	/**
	 * Devuelve una lista con las consultas de un arbol que tienen una frecuencia
	 * especifica.
	 * 
	 * @param nodo Nodo recursivo
	 * @param consulta Texto de la consulta
	 * @param frecuencia Frecuencia que tiene que tener la consulta para ser añadida
	 * @return raiz Arbol con consultas de una misma frecuencia
	 */
	public GTreeIF<Query> obtenerArbolMismaFrecuencia(GTreeIF<Query> nodo, int frecuencia, 
			String prefijo, String consulta){
		GTreeIF<Query> aux = new GTree<Query>();
		aux.setRoot(new Query(""));
		
		//Obtenemos una lista cuyas consultas tengan la misma frecuencia que la 
		//especificada en el encabezado
		ListIF<Query> listaConsultasFrecuencia = obtenerListaConsultas(nodo, frecuencia, consulta, prefijo);
		IteratorIF<Query> itr = listaConsultasFrecuencia.iterator();
		while(itr.hasNext()) {
			//Iteramos la lista
			Query temp = itr.getNext();
			añadirConsulta(aux, temp.getFreq(), temp.getText(), 0);
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
	public GTreeIF<Query> obtenerArbolPrefijo(GTreeIF<Query> nodo, String prefix, int i){
		
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
				}else { //Si no hay ninguna letra que coincida con la letra del nodo
					GTreeIF<Query> nodoVacio = new GTree<Query>();
					nodoVacio.setRoot(new Query(""));
					return nodoVacio;
				}
			}
		}
		GTreeIF<Query> nodoVacio = new GTree<Query>();
		nodoVacio.setRoot(new Query(""));
		return nodoVacio;
	}
	
	
	
	/**
	 * Devuelve una lista con las consultas almacenadas en el arbol
	 * que contiene el deposito.
	 * 
	 * La idea es usar la recursividad para ir pasando de un nodo a otro en forma
	 * equivalente a preorder. (Los nodos mas a la izquierda primero)
	 * 
	 * Cada nodo comprueba si tiene un hijo con una frecuencia mayor que 0 y,
	 * en caso de tenerla, crea un query que almacena la cadena formada consulta
	 * y la frecuencia del ultimo nodo iterado y añade ese query a una lista que
	 * es utilizada por el nodo padre para almacenar las consultas encontradas.
	 * 
	 * Si frecuencia es mayor que 0 se obtienen las consultas cuya frecuencia sea
	 * mayor que 0 mientras que si frecuencia es mayor que 0 se obtienen las
	 * consultas cuya frecuencia sea igual a la especificada.
	 * 
	 * La cadena consulta esta formada por el conjunto de letras en cada nodo que
	 * ha sido recorrido hasta llegar al nodo actual. Esto permite que cuando se
	 * encuentre un nodo con frecuencia mayor que 0 se tenga una cadena de caracteres
	 * con el camino que se ha seguido hasta llegar al nodo actual.
	 * 
	 * @param nodo Nodo recursivo
	 * @param consulta Texto de cada consulta obtenida
	 * @param prefijo Prefijo que tienen que tener las consultas
	 * @return lr Lista de consultas
	 */
	private ListIF<Query> obtenerListaConsultas(GTreeIF<Query> nodo, int frecuencia, String consulta,
			String prefijo){
		
		//Creamos una lista que guardara las consultas obtenidas hasta ahora
		ListIF<Query> lr = new List<Query>();
		
		ListIF<GTreeIF<Query>> listaNodos = nodo.getChildren();
		IteratorIF<GTreeIF<Query>> itr = listaNodos.iterator();
		while(itr.hasNext()) { //Iteramos los hijos de nodo
			GTreeIF<Query> temp = itr.getNext();
			
			//Se van añadiendo los nodos recorridos a una cadena consulta
			consulta = consulta.concat(temp.getRoot().getText());
			
			//Si frecuencia es 0 se obtienen las consultas cuya frecuencia sea mayor que 0
			if(frecuencia==0) {
				//Si encuentra un nodo hijo con frecuencia mayor que 0
				//se crea un query de texto consulta y la frecuencia del nodo 
				//actual y se añade a la lista que almacena las consultas obtenidas
				if(temp.getRoot().getFreq()>0) {
					Query q = new Query(prefijo + consulta);
					q.setFreq(temp.getRoot().getFreq());
					lr.insert(q, lr.size()+1);
				}else { //Si el nodo no tiene frecuencia mayor que 0
					//Se comprueba si alguno de sus hijos contiene 
					//frecuencia mayor que 0 y, se obtiene la lista
					//con las consultas que han sido encontradas
					ListIF<Query> l = obtenerListaConsultas(temp, frecuencia, consulta, prefijo);
					IteratorIF<Query> itrl = l.iterator();
					while(itrl.hasNext()) { 
						//Se añade cada consulta encontrada a la lista de este nodo
						Query temp2 = itrl.getNext();
						lr.insert(temp2, lr.size()+1);
					}
				}
			}else { 
				//Si frecuencia es mayor que 0 se obtienen consultas cuya frecuencia 
				//sea la especificada en el encabezado.
				
				//Si encuentra un nodo hijo con frecuencia mayor que 0
				//se crea un query de texto consulta y la frecuencia del nodo 
				//actual y se añade a la lista que almacena las consultas obtenidas
				if(temp.getRoot().getFreq()==frecuencia) {
					Query q = new Query(prefijo + consulta);
					q.setFreq(temp.getRoot().getFreq());
					lr.insert(q, lr.size()+1);
				}else { //Si el nodo no tiene frecuencia mayor que 0
					//Se comprueba si alguno de sus hijos contiene 
					//frecuencia mayor que 0 y, se obtiene la lista
					//con las consultas que han sido encontradas
					ListIF<Query> l = obtenerListaConsultas(temp, frecuencia, consulta, prefijo);
					IteratorIF<Query> itrl = l.iterator();
					while(itrl.hasNext()) { 
						//Se añade cada consulta encontrada a la lista de este nodo
						Query temp2 = itrl.getNext();
						lr.insert(temp2, lr.size()+1);
					}
				}
				
			}
			
			//Si el nodo al que se va ha cambiado no era 
			//un nodo de frecuencia entonces acortar la cadena
			//Esto se debe a que los nodos frecuencia tienen texto
			//igual a "" y, como hay que acortar consulta a cada cambio
			//de hijo que NO sea de frecuencia, entonces ponemos la condicion
			//que detecte cuando un hijo es frecuencia y cuando no.
			if(!temp.getRoot().getText().equals("")) {
				consulta = acortarCadenaNVeces(consulta,  1);
			}
			
		}
		
		return lr;
	}
	
	
	//Acorta una cadena n veces por la derecha, eliminando las letras mas a la derecha
	private String acortarCadenaNVeces(String cadena, int n) {
		return cadena.substring(0, cadena.length()-n);
	}
	
	
	 


	
	/**
	 * NOTA: investigar si las palabras con misma parte compartida
	 * pero letras extras tienen mayor prioridad o menor que las que
	 * son exactamente iguales.
	 * 
	 * 14/4 Las palabras que solo son el prefijo van antes que las palabras
	 * que no son solo el prefijo
	 * 
	 * Metodo usado para comparar dos palabras lexicograficamente
	 * @return 1 si a < b , 0 si a = b, -1 si a > b
	 */
	
	
	
	 public int compararLexicograficamente(String a, String b) {
		int tamañoPalabra = 0;
		
		//Calculamos el tamaño de la palabra de menor tamaño
		if( (a.length() - b.length()) > 0) {
			tamañoPalabra = b.length();
		}else {
			tamañoPalabra = a.length();
		}
		
		//Por cada caracter que puede potencialmente coincidir en
		//las dos palabras
		for(int i = 0; i < tamañoPalabra; i++) {
			if(a.charAt(i) < b.charAt(i)) {
				return 1; //caso a < b
			}else if(a.charAt(i) > b.charAt(i)){
				return -1; //caso a > b
			}
		}
		
		return 0; //caso a = b
	}
	

	 
	 /**
	 * Añade una consulta al deposito
	 * 
	 * @param q Texto de la consulta
	 */
	public void incFreqQuery(String q) {
		añadirConsulta(obtenerDeposito(), 0, q, 0);
	}

	
	/**
	 * Metodo auxiliar de incFreqQuery(...)
	 * 
	 * Va iterando el arbol segun la secuencia de letras obtenida del
	 * parametro q con la ayuda de una variable contadora i.
	 * 
	 * Primero comprueba si existe un nodo que contenga la letra actual en
	 * la lista de hijos. En caso de que no haya letra se añade insertandola
	 * en la lista de hijos y aumenta su frecuencia.
	 * 
	 * Por otro lado, si hay un nodo con la letra, se comprueba si tiene un nodo 
	 * hoja con su frecuencia y si efectivamente tiene uno se aumenta la frecuencia
	 * de este en 1. Si no encuentra un nodo hoja con frecuencia entonces lo crea y
	 * asigna su frecuencia a 1. En ambos casos se pasa a la siguiente letra tras
	 * aumentar la frecuencia mediante una llamada recursiva al propio metodo pero 
	 * aumentando i en 1 para poder comprobar la siguiente letra en la secuencia del 
	 * texto de la consulta.
	 * 
	 * Si el parametro frecuencia es mayor que 0 la frecuencia asignada a la consulta
	 * sera igual a la frecuencia especificada.
	 * 
	 * @param nodo Nodo con la letra a comprobar
	 * @param frecuencia Frecuencia de la consulta a añadir. Si frecuencia=0->frecuencia=1
	 * @param q Texto de la consulta
	 * @param i Contador. 0 <= i < q.length()
	 */
	public void añadirConsulta(GTreeIF<Query> nodo, int frecuencia, String q, int i) {
		
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
						//Obtenemos el nodo hoja que contiene la frecuencia y
						//la aumentamos en 1.
						
						GTreeIF<Query> nodoFrecuencia = obtenerNodoHoja(temp);
						
						//Si tenia frecuencia 0
						if(nodoFrecuencia==null) {
							GTreeIF<Query> nodoFrecuenciaNuevo = new GTree<Query>();
							nodoFrecuenciaNuevo.setRoot(new Query(""));
							
							//Asignamos una frecuencia especificada si frecuencia!=1
							if(frecuencia==0) {
								nodoFrecuenciaNuevo.getRoot().setFreq(1);
							}else {
								nodoFrecuenciaNuevo.getRoot().setFreq(frecuencia);
							}
							
							temp.addChild(temp.getNumChildren()+1, nodoFrecuenciaNuevo);
							
							//Aumentamos el numero de consultas por 1
							incrementarNumeroConsultas();
						}else { //Si NO tenia frecuencia 0
							nodoFrecuencia.getRoot().setFreq(nodoFrecuencia.getRoot().getFreq()+1);
						}
						
					}
					añadirConsulta(temp, frecuencia, q, i+1);
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
					//Añadimos un nodo hoja que contiene la frecuencia y
					//le asignamos el valor 1
					GTreeIF<Query> nodoFrecuencia = new GTree<Query>();
					nodoFrecuencia.setRoot(new Query(""));
					
					//Asignamos una frecuencia especificada si frecuencia!=1
					if(frecuencia==0) {
						nodoFrecuencia.getRoot().setFreq(1);
					}else {
						nodoFrecuencia.getRoot().setFreq(frecuencia);
					}
					nuevoNodo.addChild(nuevoNodo.getNumChildren()+1, nodoFrecuencia);
					
					//Aumentamos el numero de consultas almacenadas
					incrementarNumeroConsultas();
				}
				nuevoNodo.setRoot(nuevoQuery);
				listaAux.insert(nuevoNodo, listaAux.size()+1);
				añadirConsulta(nuevoNodo, frecuencia, q, i+1);
			}
		}
	}
	
	/**
	 * Devuelve un nodo hoja hijo de nodo
	 * @param nodo Nodo padre del nodo hoja
	 */
	private GTreeIF<Query> obtenerNodoHoja(GTreeIF<Query> nodo) {
		
		IteratorIF<GTreeIF<Query>> itr = nodo.getChildren().iterator();
		while(itr.hasNext()) { //Itera todos los hijos de nodo
			GTreeIF<Query> temp = itr.getNext();
			
			//Si encuentra un nodo hoja como hijo de nodo
			if(temp.isLeaf()) {
				return temp;
			}
		}
		
		return null;
	}

}