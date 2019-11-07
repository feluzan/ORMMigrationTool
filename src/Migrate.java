//import java.io.BufferedReader;
import java.io.File;
import java.util.Scanner;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import django.OWL2Django;
import jpa.Java2OWL;



public class Migrate {

	public static void main(String[] args){
		
	//	String folderPath = "C:\\Users\\Felix Zanetti\\eclipse-workspace\\JavaTest\\src\\";
	//	File folder = new File(folderPath);
		
		String ORMFOPath = "D:\\Projetos\\ORMMigrationTool\\ontologias\\ormo-o.owl";
		System.out.print("[INFO] Iniciando leitura da ORM-O...");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File ORMFOFile = new File(ORMFOPath);
		System.out.println("file criado...");
		OWLOntology ormfo=null;
		OWLDataFactory factory = manager.getOWLDataFactory();
		System.out.println("data factory criado...");
		try {
			ormfo = manager.loadOntologyFromOntologyDocument(ORMFOFile);
			System.out.println("owl ontology carregada...");
		} catch (OWLOntologyCreationException e) {
			System.out.println("[ERROR] Houve algum problema ao carregar a ontologia.");
			System.out.println("\tO programa ser� encerrado.");
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println(" OK!");
		
		
		
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("Digite o path da pasta que contem os arquivos das classes Java:");
//		String folderPath = scanner.nextLine();
//		
//		System.out.println("Digite o path da arquivo de sa�da que conter� a OWL:");
//		String owlPath = scanner.nextLine();
//		
//		System.out.println("Digite o path da arquivo de sa�da que conter� o c�digo em Ptrhon com Django:");
//		String djangoPath = scanner.nextLine();
//		scanner.close();		
		
		String folderPath = "D:\\Projetos\\DomainModelExample\\src\\model";
		String owlPath = "output.owl";
		String djangoPath = "outputDjango.py";
		
			
	
		System.out.println("[INFO] Carregando pasta...");
		File folder = null;
		try{
			folder = new File(folderPath);
		}catch (Exception e) {
			System.out.println("[ERROR] Erro no carregamento da pasta " + folderPath);
			System.out.println("\tO programa ser� encerrado.");
			System.exit(1);
		}
		
		Java2OWL java2owl = new Java2OWL(folder, ormfo);
		
		
		java2owl.printFile(owlPath);
	
		
//		System.out.println("\n[INFO] Iniciando Etapa OWL -> Python");
//
//		OWL2Django owl2Django = new OWL2Django(owlPath);
//		System.out.println("\n[INFO] Iniciando escrita no arquivo " + djangoPath + "...");
//		owl2Django.printFile(djangoPath);

		System.out.println("Fim!");
		
		
		
	
			
			
			
			
			
			
	}

}
