package application;

public class Processo {

	static int codigoGeral = 0;
	
	int codigo;
	int tempoChegada;
	int tempoExecucao;
	int prioridade;
	int tempoAcessoOperacaoES;
	
	int tempoResposta;
	
	public Processo(Integer[] processos) {
		Processo.codigoGeral++;
		this.codigo = Processo.codigoGeral;
		
		this.tempoChegada = processos[0];
		this.tempoExecucao = processos[1];
		this.prioridade = processos[2];
		if (processos.length == 4)
			this.tempoAcessoOperacaoES = processos[3];
		
		this.tempoResposta = 0;
	}
}
