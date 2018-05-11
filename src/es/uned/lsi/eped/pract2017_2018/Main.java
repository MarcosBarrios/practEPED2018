package es.uned.lsi.eped.pract2017_2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.ListIF;

public class Main {
	
	public static void main(String [] args) throws IOException{
		
		//lectura de parámetros
		
		/*//estructura: L (lista) o T (árbol general)
		String estructure = args[0];
		
		//fichero con el depósito de queries
		String pathDepot = args[1];
		//fichero de operaciones
		String pathOperations = args[2];*/
		
		String estructure = "T";
		String pathDepot = "src/JdP-consultas.txt";
		String pathOperations = "src/JdP-operaciones.txt";

		//creación del depósito de acuerdo a la estructura seleccionada
		QueryDepotIF QD = null;
		if(estructure.equalsIgnoreCase("L")){
			QD = new QueryDepotList();
		}
		if(estructure.equalsIgnoreCase("T")){
			QD = new QueryDepotTree();
		}
		//lectura de las queries del depósito
        FileReader fDepot = new FileReader(pathDepot);
        BufferedReader bDepot = new BufferedReader(fDepot);
        String lineDepot;
        while((lineDepot = bDepot.readLine())!=null) {
        	QD.incFreqQuery(lineDepot);
        }
        bDepot.close();
        System.out.println("Consultas almacenadaS: "+QD.numQueries());
		//lectura y ejecución de las operaciones 
        FileReader fOperations = new FileReader(pathOperations);
        BufferedReader bOperations = new BufferedReader(fOperations);
        String lineOperation;
        while((lineOperation = bOperations.readLine())!=null) {
        	Operation operation = new Operation(lineOperation);
        	String op = operation.getOperation();
        	String arg = operation.getArg();
        	if(op.equalsIgnoreCase("F")){
        		double t0 = System.nanoTime(); 
        		int f = QD.getFreqQuery(arg);
        		double t1 = System.nanoTime()-t0;
        		t1 = t1/1000000.0;
        		System.out.println("La frecuencia de \""+arg+"\" es "+f+".");
        		System.out.println("-Tiempo: "+t1);
        	}
        	if(op.equalsIgnoreCase("S")){
        		double t0 = System.nanoTime(); 
        		ListIF<Query> L = QD.listOfQueries(arg);
        		
        		double t1 = System.nanoTime()-t0;
        		t1 = t1/1000000.0;
        		System.out.println("La lista de sugerencias para \""+arg+"\" es:");
        		IteratorIF<Query> I = L.iterator();
        		while(I.hasNext()){
        			Query query = I.getNext();
        			String textQuery = query.getText();
        			int frec = query.getFreq();
            		System.out.println("\t\""+textQuery+"\" con frecuencia "+frec+".");
        		}
        		System.out.println("-Tiempo: "+t1);

        	}
        }
        bOperations.close();
	}


}
