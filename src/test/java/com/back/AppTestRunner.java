package com.back;

import com.back.standard.util.TestUtil;
import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.global.rq.Rq;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

public class AppTestRunner {
    public static String run(String input) {
        // 표준 출력을 ByteArrayOutputStream으로 리다이렉트
        ByteArrayOutputStream out = TestUtil.setOutToByteArray();
        // 표준 입력을 테스트 입력으로 설정
        Scanner scanner = TestUtil.genScanner(input + "\n종료\n");

        try {
            // App 실행 로직을 직접 구현
            System.out.println("== 명언 앱 ==");
            WiseSayingController wiseSayingController = new WiseSayingController(scanner);
            SystemController systemController = new SystemController();

            while (true) {
                System.out.print("명령) ");
                String cmd = scanner.nextLine().trim();
                Rq rq = new Rq(cmd);

                if (cmd.equals("종료")) {
                    systemController.actionExit();
                    break;
                }
                wiseSayingController.handleCommand(rq);
            }
            scanner.close();
        } catch (Exception e) {
            // 예외 발생 시에도 출력 복원
        } finally {
            // 표준 출력 복원
            TestUtil.clearSetOutToByteArray(out);
        }

        return out.toString();
    }
}