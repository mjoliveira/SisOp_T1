package application;

import java.io.IOException;
import java.util.Comparator;

public class App {

	public static void main(String[] args) {
		DadosImportados data = null;
		try {
			data = IO.carregarArquivo("teste");
		} catch (IOException e) {
			e.printStackTrace();
		}
		RoundRoubin round = new RoundRoubin(data);
		round.start();

        print("");
        print("");
		round.processados
                .stream()
                .sorted(Comparator.comparingInt(e -> e.codigo))
                .forEach(e->print("" + e.codigo + " " + e.getTempoResposta()+ " " + e.tempoChegada));

		round.processados
                .stream()
                .mapToInt(Processo::getTempoResposta)
                .average()
                .ifPresent(v -> print("\ntempo medio de respotas: " + v));


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
