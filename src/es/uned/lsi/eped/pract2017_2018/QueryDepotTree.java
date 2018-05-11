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
		
		/*
		 * Lo primero que se hara sera pasar las consultas almacenadas
		 * en el arbol a una lista de consultas ListIF<Query>:
		 * 
		 * 		1. Obtener las consultas almacenadas en forma de lista
		 * 
		 * Despues de obtener la lista, se procede igual que en la implementacion
		 * mediante lista:
		 * 		2. Obtener lista con las palabras que empiezan con el prefijo
		 * 		3. Obtener la frecuencia maxima
		 * 		4. Crear una lista ordenada de mayor a menor frecuencia
		 * 		5. Ordenar lexicográficmente la lista
		 */
		
		//1. Obtener las consultas almacenadas en forma de lista
		ListIF<Query> depositoConsultas = obtenerConsultas();
		
		//2. Obtener lista con las palabras que empiezan con el prefijo
		ListIF<Query> listaPrefijo = obtenerListaPrefijo(depositoConsultas, prefix);
		
		//3. Obtener la frecuencia maxima
		int frecuenciaMax = obtenerMaxFrecuencia(listaPrefijo);
		
		ListIF<Query> listaOrdenada = obtenerListaOrdenada(listaPrefijo, frecuenciaMax);
		
		depositoConsultas = ordenarLexicograficamente(listaOrdenada, frecuenciaMax);
		
		return null;
	}

	/**
	 * Devuelve una lista con las consultas almacenadas en el deposito
	 * con implementacion en arbol.
	 * 
	 * @return listaConsultas Lista con todas las consultas almacenadas en el arbol
	 */
	private ListIF<Query> obtenerConsultas() {
		ListIF<Query> listaConsultas = new List<Query>();
		
		//Iteramos tantas veces como consultas haya guardadas
		for(int i = 0; i < numQueries(); i++) {
			//String textoConsulta = obtenerConsulta(listaConsultas, primerNodo, 1, "");
			
			//Obtenemos una consulta que no este ya en listaConsultas
			Query q = añadirConsultaDiferente(listaConsultas, primerNodo, "");
			
			//Solo se añade si se ha encontrado una consulta que no haya
			//sido añadida ya a listaConsultas
			if(q!=null)listaConsultas.insert(q, listaConsultas.size()+1);
		}
		
		return listaConsultas;
	}
	
	private Query añadirConsultaDiferente(ListIF<Query> listaConsultas, GTreeIF<Query> nodo,
		String consulta) {
		ListIF<GTreeIF<Query>> listaNodos = nodo.getChildren();
		IteratorIF<GTreeIF<Query>> itr = listaNodos.iterator();
		while(itr.hasNext()) { //Iteramos todos los hijos de un nodo
			GTreeIF<Query> temp = itr.getNext(); //Pasamos al siguiente hijo
			
			//Concatena la cadena consulta con el texto del nodo
			consulta = consulta.concat(temp.getRoot().getText());
			
			//Si encuentra un nodo con frecuencia mayor que 0
			//y no se ha añadido un Query con el texto consulta
			//y la frecuencia del nodo actual a listaConsultas
			if(temp.getRoot().getFreq()>0 &&
					!contieneCadenaConsulta(consulta, listaConsultas)) {
				//System.out.println("\t dentro: " + consulta);
				
				//Devolvemos una consulta con consulta y freq este nodo
				Query aux = new Query(consulta);
				aux.setFreq(temp.getRoot().getFreq());
				return aux;
			}else { //En cualquier otro caso
				//Continuar la busqueda de una consulta que no haya sido añadida
				//a listaConsultas
				Query aux = añadirConsultaDiferente(listaConsultas, temp, consulta);
				if(aux!=null) { //Si se encuentra una consulta
					//Devolver esa consulta para añadirla a listaConsultas posteriormente
					return aux;
				}
			}
			//Al cambiar de un hijo a otro se acorta
			//consulta por el final 1 vez
			consulta = acortarCadenaNVeces(consulta, 1);
		}
		
		//En caso de que no se encuentre una consulta en
		//la ramificacion actual
		return null; 
	}
	
	//Acorta una cadena n veces por la derecha, eliminando las letras correspondientes
	private String acortarCadenaNVeces(String cadena, int n) {
		return cadena.substring(0, cadena.length()-n);
	}
	
	//Devuelve verdadero si listaConsultas contiene una consulta con el texto
	//consulta
	private boolean contieneCadenaConsulta(String consulta, ListIF<Query> listaConsultas) {
		
		IteratorIF<Query> itr = listaConsultas.iterator();
		while(itr.hasNext()) {
			Query temp = itr.getNext();
			if(temp.getText().equals(consulta)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Obtencion de una lista de palabras que tengan el prefijo
	 * pasado en el parametro.
	 * 
	 * @param prefijo - Prefijo para comparar las consultas
	 * @return aux - Lista con las palabras que tienen el prefijo
	 */
	 private ListIF<Query> obtenerListaPrefijo(ListIF<Query> deposito, String prefijo){
		 ListIF<Query> aux = new List<Query>();
		 IteratorIF<Query> itr = deposito.iterator();
		
		 if(prefijo.length()>=0) {
			 while(itr.hasNext()) { //Por cada palabra en el deposito
				 Query temp = itr.getNext();
				 int tamañoPalabra = temp.getText().length();
				 int aciertos = 0;
				
				//Si el tamaño de la palabra del deposito es mayor o igual que el prefijo
				 if(tamañoPalabra >= prefijo.length()) {
					
					//Comprobamos que los caracteres de la consulta coinciden
					//con los caracteres del prefijo (los (prefijo.length()) primeros
					//caracteres nada mas)
					for(int i = 0; i < prefijo.length(); i++) {
						String textoQuery = temp.getText().toLowerCase();
						
						if(textoQuery.charAt(i) == prefijo.charAt(i)) {
							aciertos++; //sumamos 1 por cada acierto de caracter
						}
					}
				}
				
				if(aciertos == prefijo.length()) {
					aux.insert(temp, aux.size()+1); //La palabra cumple con los requisitos
				}
			}
		 }else {
			 while(itr.hasNext()) {
				 Query temp = itr.getNext();
				 aux.insert(temp, aux.size()+1);
			 }
		 }
		
		 return aux;
	 }
	 
	 /**
	 * Devuelve la frecuencia maxima de un deposito de consultas
	 * 
	 * @param listaPrefijo - Lista con palabras que empiezan por un prefijo especificado
	 * @return frecuenciaMax - Maxima frecuencia de una consulta de la listaPrefijo
	 */
	 private int obtenerMaxFrecuencia(ListIF<Query> listaPrefijo) {
		 IteratorIF<Query> itr = listaPrefijo.iterator();
		 int frecuenciaMax = 0;
		 while(itr.hasNext()) {
			 Query temp = itr.getNext();
			
			 if(temp.getFreq() > frecuenciaMax) {
				 frecuenciaMax = temp.getFreq();
			 }
		 }
		
		 return frecuenciaMax;
	 }
	 
	 /**
	 * Obtiene una lista ordenada segun las frecuencias de sus consultas
	 * de mayor a menor.
	 * 
	 * @param listaPrefijo - Lista con consultas que empiezan por un prefijo
	 * @param frecuenciaMax - Frecuencia maxima de las consultas de la listaPrefijo
	 * @return listaOrdenada - Lista con consultas ordenadas de mayor a menor frecuencia
	 */
	 private ListIF<Query> obtenerListaOrdenada(ListIF<Query> listaPrefijo, int frecuenciaMax){
		ListIF<Query> listaOrdenada = new List<Query>();
		
		for(int i = frecuenciaMax; i > 0; i--) { //Para cada frecuencia	
			
			IteratorIF<Query> itr = listaPrefijo.iterator();
			while(itr.hasNext()) { //Para cada palabra en listaPrefijo
				Query temp = itr.getNext();
				if(temp.getFreq()==i) {
					listaOrdenada.insert(temp, listaOrdenada.size()+1);
				}
			}
		
		}
		
		return listaOrdenada;
	 }
	 
	 /**
	 * Ordena lexicograficamente una lista.
	 * 
	 * PRECONDICION: listaOrdenada tiene que estar ordenada de mayor a menor frecuencia
	 * @param listaOrdenada - Lista ordenada de mayor a menor frecuencia
	 * @param frecuenciaMax - Frecuencia maxima de las consultas en listaOrdenada
	 * @return listaLexicografica - Lista ordenada lexicograficamente y de mayor a menor frecuencia
	 */
	private ListIF<Query> ordenarLexicograficamente(ListIF<Query> listaOrdenada, int frecuenciaMax) {
		
		ListIF<Integer> listaFrecuencias = obtenerFrecuencias(listaOrdenada);
		
		//O(N*N*N*K)
		IteratorIF<Integer> itr = listaFrecuencias.iterator();
		while(itr.hasNext()) { //Para cada frecuencia
			int frecuencia = itr.getNext();
			
			//O(N)
			//Obtenemos la lista de consultas con frecuencia i
			ListIF<Query> listaMismaFrecuencia = obtenerMismaFrecuencia(listaOrdenada, frecuencia);
			
			//O(N*N*K) (K = nº minimo de caracteres entre dos consultas)
			//Obtenemos la lista ordenada lexicograficamente con la frecuencia especifica
			listaMismaFrecuencia = listaLexicografica(listaMismaFrecuencia);
			
			//Obtenemos la primera posicion de listaMismaFrecuencia con respecto a 
			//listaOrdenada para poder sustituir la listaMismaFrecuencia en la posicion 
			//correcta de listaOrdenada tras haber ordenado las consultas de 
			//listaMismaFrecuencia lexicograficamente
			
			//O(N)
			int primeraPos = 0;
			int aux = 1;
			boolean encontrado = false;
			IteratorIF<Query> itr2 = listaOrdenada.iterator();
			while(!encontrado && itr2.hasNext()) {
				//aux++; //Queremos una posicion directa (desde la posicion 1, no desde 0)
				Query temp = itr2.getNext();
				if(temp.getFreq()==frecuencia) {
					primeraPos = aux;
					encontrado = true;
				}
				aux++;
			}
			
			//O(N)
			//Sustituimos la lista ordenada lexicograficamente por la que no lo esta
			//con la misma frecuencia
			for(int z = 0; z < listaMismaFrecuencia.size(); z++) {
				Query temp = listaMismaFrecuencia.get(z+1);
				listaOrdenada.set(primeraPos+z, temp);
			}
			
		}
		
		return listaOrdenada;
	}
	
	/**
	* Devuelve una lista con consultas que tienen la misma frecuencia
	* @param listaOrdenada - Lista ordenada de mayor a menor frecuencia
	* @param frequency - Frecuencia de las consultas
	* @return listaMismaFrecuencia - Lista con consultas de la frecuencia especificada
	*/
	private ListIF<Query> obtenerMismaFrecuencia(ListIF<Query> listaOrdenada, int frequency) {
		ListIF<Query> listaMismaFrecuencia = new List<Query>();
		
		IteratorIF<Query> itr = listaOrdenada.iterator();
		while(itr.hasNext()) {
			Query temp = itr.getNext();
			if(temp.getFreq()==frequency) {
				listaMismaFrecuencia.insert(temp, listaMismaFrecuencia.size()+1);
			}
		}
		
		return listaMismaFrecuencia;
	}
	
	/**
	 * Añade todas las frecuencias de una lista contenedora de consultas
	 * a una lista contenedora de frecuencias.
	 * 
	 * @param listaConsultas - Lista de consultas
	 * @return listaFrecuencias - Todas las distintas frecuencias de listaConsultas
	 */
	private ListIF<Integer> obtenerFrecuencias(ListIF<Query> listaConsultas){
		ListIF<Integer> listaFrecuencias = new List<Integer>();
		
		IteratorIF<Query> itr = listaConsultas.iterator();
		while(itr.hasNext()) {
			Query temp = itr.getNext();
			if(!listaFrecuencias.contains(temp.getFreq())) {
				listaFrecuencias.insert(temp.getFreq(), listaFrecuencias.size()+1);
			}
		}
		
		return listaFrecuencias;
	}
	
	//Ordena una lista ordenada por frecuencias a una lista ordenada 
	//por frecuencias y lexicograficamente
	private ListIF<Query> listaLexicografica(ListIF<Query> listaMismaFrecuencia) {
		ListIF<Query> lista = listaMismaFrecuencia;
		
		//O(N*N*K)
		for(int j = 0; j < lista.size(); j++) {
			for(int i = 0; i < lista.size(); i++) {
				Query temp1 = lista.get(i);
				Query temp2 = lista.get(i+1);
				
				//Compara la lexicografia de temp2 con respecto a temp1
				//Si comparacion==1 entonces temp2 va antes de temp1 lexicograficamente
				//Si comparacion==0 entonces temp2 y temp1 son iguales lexicograficamente
				//Si comparacion==-1 entonces temp2 va despues de temp1 lexicograficamente
				//O(K) (K = nº caracteres consulta con menos caracteres)
				int comparacion = compararLexicograficamente(temp2.getText(), temp1.getText());
				int masPequeño = temp2.getText().length()-temp1.getText().length();			
				
				//Si temp2 es menor que temp1 lexicograficamente
				//O si temp2 es igual que temp1 pero la consulta es mas pequeña
				if(comparacion==1 || 
						(comparacion==0 && masPequeño<0) ) {
					Query aux = new Query(temp2.getText());
					aux.setFreq(temp2.getFreq());
					lista.set(i+1, temp1);
					lista.set(i, aux);
				}
			}
		}
		return lista;
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
	 private int compararLexicograficamente(String a, String b) {
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