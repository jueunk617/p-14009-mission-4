package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.*;
import java.util.*;

public class WiseSayingRepository {
    private final String dirPath = "db/wiseSaying";
    private final String lastIdPath = dirPath + "/lastId.txt";

    public WiseSayingRepository() {
        new File(dirPath).mkdirs();
    }

    // {id}.json 형태로 파일 저장
    public void save(WiseSaying ws) {
        File file = new File(dirPath + "/" + ws.getId() + ".json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(ws.toJson());
        } catch (IOException e) {
            System.err.println("저장 실패: " + e.getMessage());
        }
    }

    public void deleteById(int id) {
        File file = new File(dirPath + "/" + id + ".json");
        if (file.exists()) file.delete();
    }

    public void saveLastId(int lastId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(lastIdPath))) {
            writer.write(String.valueOf(lastId));
        } catch (IOException e) {
            System.err.println("ID 저장 실패: " + e.getMessage());
        }
    }

    public int loadLastId() {
        File file = new File(lastIdPath);
        if (!file.exists()) return 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return Integer.parseInt(reader.readLine().trim());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    public List<WiseSaying> loadAll() {
        List<WiseSaying> list = new ArrayList<>();
        File[] files = new File(dirPath).listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line); //.append("\n");
                    }
                    list.add(WiseSaying.fromJson(sb.toString()));
                } catch (IOException e) {
                    System.err.println("파일 읽기 실패: " + file.getName());
                }
            }
        }

        list.sort(Comparator.comparingInt(WiseSaying::getId));
        return list;
    }

    // data.json 파일로 전체 명언 목록 저장
    public void buildDataJson(List<WiseSaying> list) {
        File file = new File("data.json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("[\n");
            for (int i = 0; i < list.size(); i++) {
                writer.write(list.get(i).toJson());
                if (i < list.size() - 1) {
                    writer.write(",\n");
                }
            }
            writer.write("\n]");
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        } catch (IOException e) {
            System.err.println("data.json 저장 실패: " + e.getMessage());
        }
    }

    // 테스트용 초기화 메서드
    public void deleteAll() {
        File dir = new File(dirPath);

        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }

        File lastIdFile = new File(lastIdPath);
        if (lastIdFile.exists()) {
            lastIdFile.delete();
        }
    }
}