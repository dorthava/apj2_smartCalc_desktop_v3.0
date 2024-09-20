package edu.school21.SmartCalc.service;

import edu.school21.SmartCalc.repository.HistoryRepository;

import java.util.List;

public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService() {
        this.historyRepository = new HistoryRepository();
    }

    public void addHistoryItem(String expression) {
        historyRepository.saveHistoryItem(expression);
    }

    public List<String> getHistoryItems() {
        return historyRepository.loadHistory();
    }

    public void clearHistory() {
        historyRepository.clearHistory();
    }
}
