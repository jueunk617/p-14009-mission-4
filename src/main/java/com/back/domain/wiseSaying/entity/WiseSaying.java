package com.back.domain.wiseSaying.entity;

public class WiseSaying {
    private int id;
    private String content;
    private String author;

    public WiseSaying(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public int getId() { return id; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public void setContent(String content) { this.content = content; }
    public void setAuthor(String author) { this.author = author; }

    public String toJson() { // 명언 객체 -> JSON
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"content\": \"" + content.replace("\"", "\\\"") + "\",\n" +
                "  \"author\": \"" + author.replace("\"", "\\\"") + "\"\n" +
                "}";
    }

    public static WiseSaying fromJson(String json) { // JSON -> 명언 객체
        int id = extractIntValue(json, "id");
        String content = extractStringValue(json, "content");
        String author = extractStringValue(json, "author");

        return new WiseSaying(id, content, author);
    }

    private static int extractIntValue(String json, String key) { // 정수값 추출
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) return 0;
        start += pattern.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return Integer.parseInt(json.substring(start, end).trim());
    }

    private static String extractStringValue(String json, String key) { // 문자열값 추출
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        start += pattern.length();
        int quoteStart = json.indexOf("\"", start);
        int quoteEnd = json.indexOf("\"", quoteStart + 1);
        return json.substring(quoteStart + 1, quoteEnd);
    }
}