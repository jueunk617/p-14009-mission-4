package com.back.domain.wiseSaying.repository;

import com.back.AppTestRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.*;

class WiseSayingRepositoryTest {
    @BeforeEach
    void setUp() {
        // 각 테스트 전에 데이터 초기화
        AppTestRunner.run("초기화");
    }

    @Test
    @DisplayName("파일 저장 및 로드 테스트")
    void t1() {
        // 명언 등록 후 파일이 생성되는지 확인
        AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                """);

        // 개별 JSON 파일 존재 확인
        File wiseSayingFile = new File("db/wiseSaying/1.json");
        File lastIdFile = new File("db/wiseSaying/lastId.txt");

        assertThat(wiseSayingFile).exists();
        assertThat(lastIdFile).exists();

        // 파일 내용 확인
        try {
            String content = Files.readString(wiseSayingFile.toPath());
            assertThat(content)
                    .contains("\"id\": 1")
                    .contains("\"content\": \"현재를 사랑하라.\"")
                    .contains("\"author\": \"작자미상\"");

            String lastIdContent = Files.readString(lastIdFile.toPath());
            assertThat(lastIdContent.trim()).isEqualTo("1");
        } catch (IOException e) {
            fail("파일 읽기 실패: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("파일 삭제 테스트")
    void t2() {
        AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                """);

        File wiseSayingFile = new File("db/wiseSaying/1.json");
        assertThat(wiseSayingFile).exists();

        AppTestRunner.run("""
                삭제?id=1
                """);

        assertThat(wiseSayingFile).doesNotExist();
    }

    @Test
    @DisplayName("파일 수정 테스트")
    void t3() {
        AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                """);

        AppTestRunner.run("""
                수정?id=1
                과거와 현재를 사랑하라.
                홍길동
                """);

        // 수정된 내용이 파일에 반영되었는지 확인
        File wiseSayingFile = new File("db/wiseSaying/1.json");
        try {
            String content = Files.readString(wiseSayingFile.toPath());
            assertThat(content)
                    .contains("\"content\": \"과거와 현재를 사랑하라.\"")
                    .contains("\"author\": \"홍길동\"");
        } catch (IOException e) {
            fail("파일 읽기 실패: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("data.json 빌드 파일 생성 테스트")
    void t4() {
        AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                미래만 바라보지도 마라.
                빌드
                """);

        File dataJsonFile = new File("data.json");
        assertThat(dataJsonFile).exists();

        try {
            String content = Files.readString(dataJsonFile.toPath());
            assertThat(content)
                    .contains("\"id\": 1")
                    .contains("\"content\": \"현재를 사랑하라.\"")
                    .contains("\"author\": \"작자미상\"")
                    .contains("\"id\": 2")
                    .contains("\"content\": \"과거에 집착하지 마라.\"")
                    .contains("\"author\": \"미래만 바라보지도 마라.\"");
        } catch (IOException e) {
            fail("파일 읽기 실패: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("재시작 후 데이터 유지 테스트")
    void t5() {
        AppTestRunner.run("""
                등록
                지속성 테스트 명언
                테스트 작가
                """);

        final String out = AppTestRunner.run("""
                목록
                """);

        assertThat(out)
                .contains("1 / 테스트 작가 / 지속성 테스트 명언");
    }

    @Test
    @DisplayName("파일 시스템 초기화 테스트")
    void t6() {
        AppTestRunner.run("""
                등록
                삭제될 명언
                삭제될 작가
                """);

        File wiseSayingFile = new File("db/wiseSaying/1.json");
        File lastIdFile = new File("db/wiseSaying/lastId.txt");
        assertThat(wiseSayingFile).exists();
        assertThat(lastIdFile).exists();

        AppTestRunner.run("""
                초기화
                """);

        assertThat(wiseSayingFile).doesNotExist();

        // lastId.txt는 0으로 다시 생성됨
        try {
            String lastIdContent = Files.readString(lastIdFile.toPath());
            assertThat(lastIdContent.trim()).isEqualTo("0");
        } catch (IOException e) {
            fail("lastId.txt 파일 읽기 실패: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("JSON 형식 검증 테스트")
    void t7() {
        AppTestRunner.run("""
                등록
                "인생"은 아름답다. 그리고 \\ 힘들다.
                테스트 작가
                """);

        File wiseSayingFile = new File("db/wiseSaying/1.json");
        try {
            String content = Files.readString(wiseSayingFile.toPath());

            assertThat(content)
                    .contains("\\\"인생\\\"은 아름답다")
                    .contains("그리고 \\")
                    .contains("힘들다.");

            assertThat(content)
                    .startsWith("{")
                    .endsWith("}")
                    .contains("\"id\":")
                    .contains("\"content\":")
                    .contains("\"author\":");
        } catch (IOException e) {
            fail("파일 읽기 실패: " + e.getMessage());
        }
    }
}