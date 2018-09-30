package application;

import java.io.IOException;
import java.util.Comparator;

import static application.IO.carregarArquivo;

/**
 * Classe Principal
 * Solicita a leitura dos dados
 * passa para o RoundRobin
 * exibe os resultados
 *
 * Autores: Mayara e Virgilius
 * CacheData: 30/09/2018
 */
class App {

	public static void main(String[] args) {
		DadosImportados data;
		try {
			data = carregarArquivo("trab-so1-teste4 SR");
		} catch (IOException|ExceptionInInitializerError e) {
			e.printStackTrace();
			return;
		}

		RoundRoubin round = new RoundRoubin(data);
		round.processar();

        print("");
        print("");
        round.processosFinalizados
                .stream()
                .sorted(Comparator.comparingInt(e -> e.codigo))
                .forEach(e->print("processo: " + e.codigo +
                        " tempo de Chegada: " + e.tempoChegada +
                        " tempo de resposta: " + e.getTempoResposta()));


        round.processosFinalizados
                .stream()
                .mapToInt(Processo::getTempoResposta)
                .average()
                .ifPresent(v -> print("\ntempo medio de respotas: " + v));

        print("");
        round.processosFinalizados
                .stream()
                .sorted(Comparator.comparingInt(e -> e.codigo))
                .forEach(e->print("processo: " + e.codigo +
                        " tempo de Chegada: " + e.tempoChegada +
                        " tempo de espera: " + e.getTempoEspera()));

		round.processosFinalizados
                .stream()
				.mapToInt(Processo::getTempoEspera)
				.average()
                .ifPresent(v -> print("\ntempo medio de espera: " + v));

		System.out.println();
	}

	private static void print(String txt) {
		System.out.println(txt);
	}

}
