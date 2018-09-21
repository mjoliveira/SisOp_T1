package application;

import java.io.IOException;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DadosImportados data = null;
		try {
			data = IO.carregarArquivo("teste");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RoundRoubin round = new RoundRoubin(data);
		round.start();
	}

}
