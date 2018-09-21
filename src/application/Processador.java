package application;

import java.util.LinkedList;
import java.util.List;

public class Processador {
	
	List<Data> filaProcessosProcessador;
	Data data;
	Boolean trocaDeContexto;
	int fatiaTempo;
	
	public Processador(int fatiaTempo) {
		this.filaProcessosProcessador = new LinkedList<>();
		this.trocaDeContexto = false;
		this.fatiaTempo = fatiaTempo;
	}
	
	public boolean add(Processo p) {
		
		if (this.data == null) {
			
			if (filaProcessosProcessador.isEmpty() 
					|| p.prioridade < filaProcessosProcessador.get(0).processo.prioridade) {
				
				this.data = new Data(p, this.fatiaTempo);
				
			} else {
				
				this.data = filaProcessosProcessador.remove(0);
				
			}

			trocaDeContexto = true;
			return true;
						
		}
		
		if (this.data.processo.prioridade <= p.prioridade) {
			return false;
		}
		
		filaProcessosProcessador.add(this.data);
		filaProcessosProcessador.sort((d1,d2) -> d1.processo.prioridade - d2.processo.prioridade );
		
		this.data = new Data(p, this.fatiaTempo);
		trocaDeContexto = true;
		return true;
	}
	
	public Processo processar() {
		if (trocaDeContexto) {
			System.out.print("C");
			trocaDeContexto = false;
			return null;
		}
		
		if (this.data != null) {
			this.data.fatiaTempo--;
			System.out.print(this.data.processo.codigo);
			
			if (this.data.fatiaTempo == 0) {
				Processo p = this.data.processo;
				this.data = null;
				return p;
			} else {
				return null;
			}
		}
			
		System.out.print("-");
		return null;
	}
	
	private class Data {
		
		Processo processo;
		int fatiaTempo;
		
		public Data(Processo p, int fatiaTempo) {
			this.processo = p;
			this.fatiaTempo = (this.processo.tempoExecucao < fatiaTempo) 
					? this.processo.tempoExecucao : fatiaTempo;
			this.processo.tempoExecucao -= fatiaTempo;
		}
		
	}
	
}
