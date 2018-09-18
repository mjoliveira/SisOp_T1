package application;

public class Processo {

	int tempoChegada;
	int tempoExecucao;
	int prioridade;
	int tempoAcesso;
	
	public Processo(Integer[] processos) {
		this.tempoChegada = processos[0];
		this.tempoExecucao = processos[1];
		this.prioridade = processos[2];
		if (processos.length == 4)
			this.tempoAcesso = processos[3];
	}
}
