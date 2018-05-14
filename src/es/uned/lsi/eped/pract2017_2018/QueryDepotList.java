package es.uned.lsi.eped.pract2017_2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.ListIF;

public class QueryDepotList implements QueryDepotIF {

	private ListIF<Query> deposito;
	
	private int numeroConsultas;
	
	/**
	 * Constructor de la clase
	 * Crea un deposito de consultas vacio
	 */
	public QueryDepotList() {
		deposito = new List<Query>();
	}
	
	/**
	 * Crear un deposito de consultas a partir de un registro de consultas
	 * @param pathFile Ubicacion del registro de consultas
	 * @throws IOException En caso de problema
	 */
	public QueryDepotList(String pathFile) throws IOException{
		deposito = new List<Query>();
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
	 * Devuelve el deposito de consultas
	 * 
	 * @return deposito Deposito de consultas
	 */
	private ListIF<Query> obtenerDeposito(){
		return deposito;
	}
	
	/**
	 * Incrementa el numero de consultas
	 *	
	 */
	private void incrementarNumeroConsultas() {
		numeroConsultas++;
	}
	
	/**
	 * Devuelve el numero de consultar que hay en el deposito
	 * 
	 * @return numeroConsultas Numero de consultass en el deposito
	 */
	public int numQueries() {
		return numeroConsultas;
	}

	/**
	 * Devuelve la frecuencia de una consulta
	 * @param q - Texto de la consulta
	 * @return frecuencia - La frecuencia de la consulta
	 */
	public int getFreqQuery(String q) {
		int frecuencia = 0;
		IteratorIF<Query> itr = obtenerDeposito().iterator();
		
		while(itr.hasNext()) {
			Query temp = itr.getNext();
			if(temp.getText().equals(q)) {
				frecuencia = temp.getFreq();
			}
		}
		
		return frecuencia;
	}

	/**
	 * Devuelve una lista con las consultas del deposito
	 * ordenadas de mayor a menor en orden lexicografico
	 * @param prefix - El prefijo de la consulta
	 */
	public ListIF<Query> listOfQueries(String prefix) {
		ListIF<Query> listaLexicografica = new List<Query>();
		
		/*
		 * Algoritmo:
		 * 
		 * 		1. Obtener lista con las palabras que empiezan con el prefijo
		 * 		2. Obtener la frecuencia maxima
		 * 		3. Crear una lista ordenada de mayor a menor frecuencia
		 * 		4. Ordenar lexicográficmente la lista
		 * 
		 */
		
		//Obtenemos la lista con las palabras que empiezan con el prefijo
		ListIF<Query> listaPrefijo = obtenerListaPrefijo(prefix);
		
		//Obtenemos la frecuencia maxima
		int frecuenciaMax = obtenerMaxFrecuencia(listaPrefijo);
		
		//Crear una lista ordenada de mayor a menor frecuencia
		ListIF<Query> listaOrdenada = obtenerListaOrdenada(listaPrefijo, frecuenciaMax);
		
		//imprimirLista(listaOrdenada);
		
		//Ordenar lexicograficamente la lista
		listaLexicografica = obtenerListaLexicografica(listaOrdenada, frecuenciaMax);
		
		//imprimirLista(listaLexicografica);
		
		return listaLexicografica;
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
	
	/**
	 * Ordena lexicograficamente una lista.
	 * 
	 * PRECONDICION: listaOrdenada tiene que estar ordenada de mayor a menor frecuencia
	 * @param listaOrdenada - Lista ordenada de mayor a menor frecuencia
	 * @param frecuenciaMax - Frecuencia maxima de las consultas en listaOrdenada
	 * @return listaLexicografica - Lista ordenada lexicograficamente y de mayor a menor frecuencia
	 */
	private ListIF<Query> obtenerListaLexicografica(ListIF<Query> listaOrdenada, int frecuenciaMax) {
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
			listaMismaFrecuencia = ordenarLexicograficamente(listaMismaFrecuencia);
			
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
	
	//Ordena una lista ordenada por frecuencias a una lista ordenada 
	//por frecuencias y lexicograficamente
	private ListIF<Query> ordenarLexicograficamente(ListIF<Query> listaMismaFrecuencia) {
		ListIF<Query> lista = listaMismaFrecuencia;
		
		//O(N*N*K)
		for(int j = 2; j <= lista.size(); j++) {
			for(int i = 0; i < lista.size()-1; i++) {
				Query temp1 = lista.get(i);
				Query temp2 = lista.get(i+1);
				
				//Compara la lexicografia de temp2 con respecto a temp1
				//Si comparacion==-1 entonces temp2 va antes de temp1 lexicograficamente
				//Si comparacion==0 entonces temp2 y temp1 son iguales lexicograficamente
				//Si comparacion==1 entonces temp2 va despues de temp1 lexicograficamente
				//O(K) (K = nº caracteres consulta con menos caracteres)
				int comparacion = compararLexicograficamente(temp2.getText(), temp1.getText());
				int masPequeño = temp2.getText().length()-temp1.getText().length();			
				
				//Si temp2 es menor que temp1 lexicograficamente
				//O si temp2 es igual que temp1 pero la consulta es mas pequeña
				if(comparacion==1 || (comparacion==0 && masPequeño<0) ) {
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
	 * Obtencion de una lista de palabras que tengan el prefijo
	 * pasado en el parametro.
	 * 
	 * @param prefijo - Prefijo para comparar las consultas
	 * @return aux - Lista con las palabras que tienen el prefijo
	 */
	 private ListIF<Query> obtenerListaPrefijo(String prefijo){
		 
		 ListIF<Query> aux = new List<Query>();
		 IteratorIF<Query> itr = obtenerDeposito().iterator();
		
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
	 * Incrementa la frecuencia de la consulta que ha
	 * sido pasada como parametro.
	 * Si el deposito no tiene una consulta con el mismo
	 * texto del parametro entonces la crea y añade con frecuencia 1.
	 * 
	 * @param q - Consulta a incrementar la frecuencia
	 */
	public void incFreqQuery(String q) {
		IteratorIF<Query> itr = obtenerDeposito().iterator();
		boolean encontrado = false;
		
		//Si no hay consultas guardadas en el deposito
		if(numQueries()==0) {
			//Directamente añadimos la consulta de texto q y frecuencia 1
			Query query = new Query(q);
			query.setFreq(query.getFreq()+1);
			obtenerDeposito().insert(query, 1);
			
			//Aumentamos en 1 el numero de consultas
			incrementarNumeroConsultas();
		}else { //Si hay consultas almacenadas
			while(itr.hasNext()) { //Iterar las consultas
				Query temp = itr.getNext();
				
				//Si una consulta se llama igual que q
				if(temp.getText().equals(q)) {
					//En vez de añadir una consulta a parte simplemente
					//aumentamos la frecuencia de la consulta existente
					temp.setFreq(temp.getFreq()+1);
					encontrado = true;
				}
			}
			
			//Si no se ha encontado una consulta con el mismo texto q
			if(!encontrado) {
				//Se añade una consulta nueva
				Query query = new Query(q);
				query.setFreq(query.getFreq()+1);
				obtenerDeposito().insert(query, obtenerDeposito().size()+1);
				
				//Aumentamos en 1 el numero de consultas
				incrementarNumeroConsultas();
			}
		}
	}

}
