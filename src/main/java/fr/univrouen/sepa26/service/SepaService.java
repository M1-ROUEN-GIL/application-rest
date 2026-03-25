package fr.univrouen.sepa26.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.univrouen.sepa26.model.DrctDbtTxInf;

@Service
public class SepaService {
	private List<DrctDbtTxInf> transactions = new ArrayList<>();
	
	public List<DrctDbtTxInf> getAll() {
		return transactions;
	}
	
	public DrctDbtTxInf getByPmtId(String pmtId) {
		return transactions.stream()
				.filter(t -> t.getPmtId().equals(pmtId))
				.findFirst()
				.orElse(null)
		;
	}
	
	public void add(DrctDbtTxInf tx) {
		transactions.add(tx);
	}
	
	public boolean existsByPmtId(String pmtId) {
		return transactions.stream()
				.anyMatch(t -> t.getPmtId().equals(pmtId))
		;
	}
	
	public boolean deleteByPmtId(String pmtId) {
		return transactions.removeIf(t -> t.getPmtId().equals(pmtId));
	}
}
