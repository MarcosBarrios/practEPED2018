package es.uned.lsi.eped.pract2017_2018;

import java.util.StringTokenizer;

public class Operation {

	private String op; //F: frecuencia; S -> sugerencias
	private String arg; //argumento
	
	
	Operation(){
		op = null;
		arg = null;
	}
	
	Operation(String o, String q){
		op = o;
		arg = q;
	}
	
	Operation(Operation O){
		op = O.getOperation();
		arg = O.getArg();
	}
	
	//linea: operacion - query
	Operation(String line){
		StringTokenizer st = new StringTokenizer(line,"\t");
		op = st.nextToken();
		arg = st.nextToken();
	}

	public String getOperation(){
		return op;
	}
	
	public String getArg(){
		return arg;
	}

	
	
	
}
