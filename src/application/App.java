package application;

import java.io.IOException;
import java.util.Comparator;

class App {

	public static void main(String[] args) {
		DadosImportados data;
		try {
			data = IO.carregarArquivo("teste");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		RoundRoubin round = new RoundRoubin(data);
		round.start();

        print("");
        print("");
        round.processados
                .stream()
                .sorted(Comparator.comparingInt(e -> e.codigo))
                .forEach(e->print("processo: " + e.codigo +
                        " tempo de Chegada: " + e.tempoChegada +
                        " tempo de resposta: " + e.getTempoResposta()));


        round.processados
                .stream()
                .mapToInt(Processo::getTempoResposta)
                .average()
                .ifPresent(v -> print("\ntempo medio de respotas: " + v));

        print("");
        round.processados
                .stream()
                .sorted(Comparator.comparingInt(e -> e.codigo))
                .forEach(e->print("processo: " + e.codigo +
                        " tempo de Chegada: " + e.tempoChegada +
                        " tempo de espera: " + e.getTempoEspera()));

		round.processados
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
