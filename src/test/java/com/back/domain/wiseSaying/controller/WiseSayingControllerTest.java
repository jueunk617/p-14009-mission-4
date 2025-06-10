package com.back.domain.wiseSaying.controller;

import com.back.AppTestRunner;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseSayingControllerTest {

    @BeforeEach
    void setUp() {
        // 각 테스트 전에 데이터 초기화
        AppTestRunner.run("초기화");
    }

    @Test
    @DisplayName("등록")
    void t1() {
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                """);

        assertThat(out)
                .contains("명언 :")
                .contains("작가 :")
                .contains("1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("등록 후 목록에서 확인")
    void t2() {
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                목록
                """);

        assertThat(out)
                .contains("1번 명언이 등록되었습니다.")
                .contains("번호 / 작가 / 명언")
                .contains("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("목록")
    void t3() {
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                미래만 바라보지도 마라.
                목록
                """);

        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("2 / 미래만 바라보지도 마라. / 과거에 집착하지 마라.")
                .contains("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("삭제")
    void t4() {
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                미래만 바라보지도 마라.
                삭제?id=1
                목록
                """);

        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.")
                .contains("2 / 미래만 바라보지도 마라. / 과거에 집착하지 마라.")
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("존재하지 않는 명언 삭제")
    void t5() {
        final String out = AppTestRunner.run("""
                삭제?id=1
                """);

        assertThat(out)
                .contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("수정")
    void t6() {
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                수정?id=1
                현재와 자신을 사랑하라.
                홍길동
                목록
                """);

        assertThat(out)
                .contains("명언(기존) : 현재를 사랑하라.")
                .contains("작가(기존) : 작자미상")
                .contains("1번 명언이 수정되었습니다.")
                .contains("1 / 홍길동 / 현재와 자신을 사랑하라.");
    }

    @Test
    @DisplayName("존재하지 않는 명언 수정")
    void t7() {
        final String out = AppTestRunner.run("""
                수정?id=1
                """);

        assertThat(out)
                .contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("빌드")
    void t8() {
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                미래만 바라보지도 마라.
                빌드
                """);

        assertThat(out)
                .contains("data.json 파일의 내용이 갱신되었습니다.");
    }

    @Test
    @DisplayName("초기화")
    void t9() {
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                초기화
                목록
                """);

        assertThat(out)
                .contains("데이터가 초기화되었습니다.")
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("잘못된 명령어")
    void t10() {
        final String out = AppTestRunner.run("""
                잘못된명령어
                """);

        assertThat(out)
                .contains("알 수 없는 명령어입니다.");
    }

    @Test
    @DisplayName("삭제 시 ID 파라미터 누락")
    void t11() {
        final String out = AppTestRunner.run("""
                삭제
                """);

        assertThat(out)
                .contains("ID를 숫자로 입력해주세요.");
    }

    @Test
    @DisplayName("수정 시 ID 파라미터 누락")
    void t12() {
        final String out = AppTestRunner.run("""
                수정
                """);

        assertThat(out)
                .contains("ID를 숫자로 입력해주세요.");
    }

    @Test
    @DisplayName("등록, 수정, 삭제, 목록")
    void t13() {
        final String out = AppTestRunner.run("""
                등록
                삶이 있는 한 희망은 있다.
                키케로
                등록
                산다는것 그것은 치열한 전투이다.
                로망 롤랑
                등록
                하루에 3시간을 걸으면 7년 후에 지구를 한바퀴 돌 수 있다.
                사무엘 존슨
                수정?id=2
                산다는것 그것은 치열한 삶의 전투이다.
                로망 롤랑
                삭제?id=1
                목록
                """);

        assertThat(out)
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.")
                .contains("3번 명언이 등록되었습니다.")
                .contains("2번 명언이 수정되었습니다.")
                .contains("1번 명언이 삭제되었습니다.")
                .contains("3 / 사무엘 존슨 / 하루에 3시간을 걸으면 7년 후에 지구를 한바퀴 돌 수 있다.")
                .contains("2 / 로망 롤랑 / 산다는것 그것은 치열한 삶의 전투이다.")
                .doesNotContain("1 / 키케로 / 삶이 있는 한 희망은 있다.");
    }
}