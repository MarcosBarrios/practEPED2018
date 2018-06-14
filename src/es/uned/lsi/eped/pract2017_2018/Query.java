package es.uned.lsi.eped.pract2017_2018;

public class Query {

	private String texto;
	
	private int frecuencia;
	
	/* Construye una nueva query con el texto pasado como parametro */
	public Query (String texto) {
		this.texto = texto;
		frecuencia = 0;
	}
	
	/* Modifica la frecuencia de la query */
	public void setFreq(int frequency) {
		this.frecuencia = frequency;
	}
	
	/* Devuelve el texto de una query */
	public int getFreq() {
		return frecuencia;
	}
	
	/* Devuelve la frecuencia de una query */
	public String getText() {
		return texto;
	}
	
	/**
	 * @return Devuelve el texto de la consulta
	 */
	@Override
	public String toString() {
		return getText();
	}
	
	/**
	 * Dos consultas seran iguales si su texto y su
	 * frecuencia son iguales.
	 * 
	 * @param o Objeto con el que comparar esta consulta
	 * 
	 * @return Verdadero si el texto y la frecuencia
	 * de "o" es igual al de esta consulta
	 */
	@Override
	public boolean equals(Object o) {
		Query q = (Query) o;
		if(q.getText().equals(getText()) &&
				q.getFreq()==getFreq()){
			return true;
		}
		return false;
	}

}
