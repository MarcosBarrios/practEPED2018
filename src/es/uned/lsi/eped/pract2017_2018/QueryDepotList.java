package es.uned.lsi.eped.pract2017_2018;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	 * 
	 * @param pathFile Ubicacion del registro de consultas
	 * @throws IOException En caso de problema
	 */
	public QueryDepotList(String pathFile) {
		deposito = new List<Query>();
		Path direccion = Paths.get(pathFile);
		try(BufferedReader lector = Files.newBufferedReader(direccion, 
				StandardCharsets.UTF_8);) {
			
	    	String consulta;
	    	while((consulta = lector.readLine())!=null) {
	    		incFreqQuery(consulta);
	    	}
		}catch(Exception e) {
			System.err.println("No se pudo obtener las consultas. ");
		}
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
	 * 
	 * @param q Texto de la consulta
	 * 
	 * @return frecuencia La frecuencia de la consulta
	 * 
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
	 * Devuelve una lista con las consultas del deposito ordenadas 
	 * de mayor a menor en orden lexicografico y por frecuencia
	 * 
	 * Algoritmo:
	 * 
	 * 		1. Obtener lista con las consultas que empiezan con el prefijo
	 * 		2. Obtener lista consultas ordenadas segun frecuencia descendente
	 * 		3. Ordenar lexicograficmente la lista del paso 2
	 * 
	 * @param prefix El prefijo de la consulta
	 */
	public ListIF<Query> listOfQueries(String prefix) {
		
		//Obtenemos la lista con las consultas que empiezan con el prefijo
		ListIF<Query> lConsultas = listaPrefijo(obtenerDeposito(), prefix);
		
		//Obtener lista con las frecuencias ordenadas descendentemente
		ListIF<Query> lOrdenada = listaFrecuenciasOrdenadas(lConsultas);
				
		//Ordenar lexicograficamente la lista con las frecuencias ordenadas
		ListIF<Query> lLexicografica = listaLexicografica(lOrdenada);
				
		return lLexicografica;
	}
	
	/**
	 * Devuelve una lista de consultas que empiecen por un  prefijo 
	 * especificado.
	 * 
	 * Si el prefijo es igual a "" entonces el metodo devuelve la
	 * lista lConsultas entera.
	 * 
	 * Se comparan los caracteres de cada consulta de lConsultas tal que
	 * si coinciden los (prefijo.length()) primeros caracteres se anade
	 * la consulta a la lista que devuelve el metodo.
	 * 
	 * @param lConsultas Lista con las consultas a comprobar
	 * @param prefijo Prefijo que tienen que tener las consultas
	 * 
	 * @return aux Lista con las palabras que tienen el prefijo
	 */
	private ListIF<Query> listaPrefijo(ListIF<Query> lConsultas, String prefijo){
		
		if(prefijo.length()>0) {
			ListIF<Query> aux = new List<Query>();
			IteratorIF<Query> itr = lConsultas.iterator();
			while(itr.hasNext()) { //Por cada consulta en lConsultas
				Query temp = itr.getNext();
				
				if(coincidePrefijo(prefijo, temp.getText())) {
					//Si la consulta tiene el prefijo "prefijo"
					aux.insert(temp, aux.size()+1);
				}
			}
			return aux;
		}
		
		return lConsultas;
	}
	
	/**
	 * Devuelve verdadero si la cadena "comparando" tiene el prefijo
	 * especificado.
	 * 
	 * @param prefijo Prefijo que tiene uqe tener la cadena comparando
	 * @param comparando Cadena sobre la que se comprueba el prefijo
	 * 
	 * @return Verdadero si el prefijo de comparando coincide con el del
	 * parametro especificado
	 */
	private boolean coincidePrefijo(String prefijo, String comparando) {
		int numeroAciertos = 0;
		
		if(comparando.length() >= prefijo.length()) {
			//Si "comparando" es al menos tan largo como prefijo
			
			for(int i = 0; i < prefijo.length(); i++) {
				if(comparando.charAt(i) == prefijo.charAt(i)) {
					//Incrementamos en 1 por cada caracter igual
					numeroAciertos++;
				}
			}
			
			if(numeroAciertos == prefijo.length()) {
				return true; //"comparando" tiene el prefijo especificado
			}
		}
		
		return false;
	}
	
	/**
	 * O(N*N)
	 * Obtiene una lista ordenada segun las frecuencias de sus consultas
	 * de mayor a menor.
	 * 
	 * @param listaPrefijo Lista con consultas que empiezan por un prefijo
	 * 
	 * @return lConsultasOrdenadas Lista con consultas ordenadas de mayor 
	 * a menor frecuencia
	 */
	private ListIF<Query> listaFrecuenciasOrdenadas(ListIF<Query> listaPrefijo){
		ListIF<Query> lConsultasOrdenadas = new List<Query>();
		
		//O(N*N) Obtenemos todas las frecuencias diferentes en listaPrefijo
		ListIF<Integer> lFrecuencias = listaFrecuencias(listaPrefijo);
		IteratorIF<Integer> itr = lFrecuencias.iterator();
		while(itr.hasNext()) {
			int temp = itr.getNext(); //Frecuencia actual
			
			//O(N) Insertamos las consultas de frecuencia "temp" en lConsultasOrdenadas
			insertarConsultasMismaFrecuencia(listaPrefijo, lConsultasOrdenadas, temp);
		}
		
		return lConsultasOrdenadas;
	}
	
	/**
	 * O(N)
	 * Devuelve una lista con todas las frecuencias de las consultas almacenadas
	 * ordenadas descendentemente (mayor primera, menor ultima)
	 * 
	 * @param listaConsultas Lista de consultas con las diferentes frecuencias
	 * 
	 * @return lFrecuencias Lista con todas las diferentes frecuencias ordenadas
	 * descendentemente
	 */
	private ListIF<Integer> listaFrecuencias(ListIF<Query> listaConsultas){
		 ListIF<Integer> lFrecuencias = new List<Integer>();
		
		 //Anadimos a lFrecuencias las diferentes frecuencias encontradas
		 IteratorIF<Query> itr = listaConsultas.iterator();
		 while(itr.hasNext()) {
			 Query temp = itr.getNext();
			 if(!lFrecuencias.contains(temp.getFreq())) {
				 lFrecuencias.insert(temp.getFreq(), lFrecuencias.size()+1);
			 }
		 }
		
		 ordenarListaDescendentemente(lFrecuencias);
		
		 return lFrecuencias;
	}
	
	/**
	 * O(N)
	 * Comprueba las consultas de la lista "lConsultas" tal que inserta
	 * las que tienen una frecuencia especifica en otra lista "lInsertando"
	 * 
	 * @param lConsultas Lista con las consultas a comprobar
	 * @param lInsertando Lista en las que insertar las consultas
	 * @param frecuencia Frecuencia que las consultas de "lConsultas" tienen que
	 * tener para ser insertadas en "lInsertando"
	 */
	private void insertarConsultasMismaFrecuencia(ListIF<Query> lConsultas, 
			ListIF<Query> lInsertando, int frecuencia) {
		
		IteratorIF<Query> itr = lConsultas.iterator();
		while(itr.hasNext()) { //Por cada consulta
			Query temp = itr.getNext();
			if(temp.getFreq()==frecuencia) {
				lInsertando.insert(temp, lInsertando.size()+1);
			}
		}
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
	 * O(N*N*N)
	 * Ordena lexicograficamente una lista.
	 * 
	 * PRECONDICION: listaOrdenada tiene que estar ordenada de mayor a menor frecuencia
	 * @param listaOrdenada - Lista ordenada de mayor a menor frecuencia
	 * @param frecuenciaMax - Frecuencia maxima de las consultas en listaOrdenada
	 * @return listaLexicografica - Lista ordenada lexicograficamente y de mayor a menor frecuencia
	 */
	private ListIF<Query> listaLexicografica(ListIF<Query> listaOrdenada) {
		ListIF<Query> lLexicografica = new List<Query>();
		
		//O(N*N*N)
		ListIF<Integer> lFrecuencias = listaFrecuencias(listaOrdenada);
		IteratorIF<Integer> itr = lFrecuencias.iterator();
		while(itr.hasNext()) { //Para cada frecuencia en lFrecuencias
			int frecuencia = itr.getNext();
			
			//O(N) Obtenemos la lista de consultas con frecuencia "frecuencia"
			ListIF<Query> consultasMismaFrecuencia = 
					listaConsultasMismaFrecuencia(listaOrdenada, frecuencia);
			
			//O(N*N)
			//Ordenamos lexicograficamente la lista con frecuencia "frecuencia"
			ordenarLexicograficamente(consultasMismaFrecuencia);
			
			//O(N)
			//Insertamos las consultas de frecuencia "frecuencia" ya ordenadas
			//lexicograficamente en la lista de salida lLexicografica
			for(int z = 0; z < consultasMismaFrecuencia.size(); z++) {
				lLexicografica.insert(consultasMismaFrecuencia.get(z+1), 
						lLexicografica.size()+1);
			}
		}
		
		return lLexicografica;
	}
	
	/**
	 * O(N*N)
	 * Ordena una lista lexicograficamente mediante el logaritmo de
	 * ordenamiento burbuja.
	 * 
	 * @param listaMismaFrecuencia Lista de consultas a ordenar lexicograficamente
	 */
	private void ordenarLexicograficamente(ListIF<Query> l) {
		for(int j = 0; j < l.size(); j++) {
			for(int i = 1; i < l.size()-j; i++) {
				Query temp1 = l.get(i);
				Query temp2 = l.get(i+1);
				
				if(temp1.getText().compareTo(temp2.getText())>0) {
					//Si temp1 es mayor que temp2 lexicograficamente
					Query aux = temp1;
					l.set(i, temp2);
					l.set(i+1, aux);
				}
			}
		}
		
	}
	
	/**
	 * Devuelve una lista con consultas que tienen la misma frecuencia
	 * @param listaOrdenada - Lista ordenada de mayor a menor frecuencia
	 * @param frequency - Frecuencia de las consultas
	 * @return listaMismaFrecuencia - Lista con consultas de la frecuencia especificada
	 */
	 private ListIF<Query> listaConsultasMismaFrecuencia(ListIF<Query> listaOrdenada, int frequency) {
		ListIF<Query> listaMismaFrecuencia = new List<Query>();
		
		IteratorIF<Query> itr = listaOrdenada.iterator();
		while(itr.hasNext()) { //Por cada consulta
			Query temp = itr.getNext();
			if(temp.getFreq()==frequency) {
				listaMismaFrecuencia.insert(temp, listaMismaFrecuencia.size()+1);
			}
		}
		
		return listaMismaFrecuencia;
	}

	/**
	 * Incrementa la frecuencia de una consulta. 
	 * 
	 * Si no hay consultas almacenadas se agrega al deposito una nueva
	 * consulta de texto q y frecuencia 1. 
	 * 
	 * Si hay consultas almacenadas se comprueba si alguna de ellas
	 * tiene texto igual a "q" y, en caso de que se de el caso, 
	 * simplemente se incrementa la frecuencia de la consulta. Si
	 * no se encontro ninguna consulta almacenada con el texto "q"
	 * se crea una nueva consulta de texto "q" y frecuencia 1 y se
	 * agrega al deposito de consultas ademas de incrementar en 1
	 * el numero de consultas almacenadas.
	 * 
	 * @param q - Consulta a incrementar la frecuencia
	 */
	public void incFreqQuery(String q) {
		
		if(numQueries()==0) {
			agregarNuevaConsulta(obtenerDeposito(), q);
		}else {
			Query temp = obtenerConsultaAlmacenada(obtenerDeposito(), q);
			if(temp!=null) {
				temp.setFreq(temp.getFreq()+1);
			}else {
				agregarNuevaConsulta(obtenerDeposito(), q);
			}
		}
	}
	
	/**
	 * Inserta una consulta de texto "q" y frecuencia 1 en la lista "l"
	 * 
	 * @param l Lista en la cual insertar la consulta
	 * @param q Texto de la consulta a insertar
	 */
	private void agregarNuevaConsulta(ListIF<Query> l, String q) {
		Query query = new Query(q);
		query.setFreq(1);
		l.insert(query, l.size()+1);
		
		//Aumentamos en 1 el numero de consultas
		incrementarNumeroConsultas();
	}
	
	/**
	 * Devuelve una consulta cuyo texto sea "s"
	 * 
	 * @param l Lista con las consultas
	 * @param s Texto con el que comparar las consultas
	 * 
	 * @return consulta cuyo texto sea "s"
	 */
	private Query obtenerConsultaAlmacenada(ListIF<Query> l, String s) {
		
		IteratorIF<Query> itr = obtenerDeposito().iterator();
		while(itr.hasNext()) { //Iterar las consultas
			Query temp = itr.getNext();
			
			if(temp.getText().equals(s)) {
				return temp;
			}
		}
		return null;
	}

}
