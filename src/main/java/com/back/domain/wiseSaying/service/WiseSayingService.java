package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.List;

public class WiseSayingService {
    private final WiseSayingRepository repository;
    private final List<WiseSaying> wiseSayings;
    private int lastId;

    public WiseSayingService() {
        this.repository = new WiseSayingRepository();
        this.wiseSayings = repository.loadAll();
        this.lastId = repository.loadLastId();
    }

    public int write(String content, String author) {
        int id = ++lastId;
        WiseSaying ws = new WiseSaying(id, content, author);
        wiseSayings.add(ws);

        repository.save(ws);
        repository.saveLastId(lastId);

        return id;
    }

    public List<WiseSaying> findAllReverse() {
        List<WiseSaying> reversed = wiseSayings.stream()
                .sorted((a, b) -> b.getId() - a.getId())
                .toList();
        return reversed;
    }

    public WiseSaying findById(int id) {
        return wiseSayings.stream()
                   .filter(ws -> ws.getId() == id)
                   .findFirst()
                   .orElse(null);
    }

    public boolean delete(int id) {
        boolean removed = wiseSayings.removeIf(ws -> ws.getId() == id);
        if (removed) repository.deleteById(id);

        return removed;
    }

    public void modify(WiseSaying ws, String newContent, String newAuthor) {
        ws.setContent(newContent);
        ws.setAuthor(newAuthor);
        repository.save(ws);
    }

    public void buildJson() {
        repository.buildDataJson(wiseSayings);
    }

    public void reset() {
        wiseSayings.clear();
        lastId = 0;
        repository.deleteAll();
        repository.saveLastId(lastId);
    }

}