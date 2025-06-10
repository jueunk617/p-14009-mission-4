package com.back;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.global.rq.Rq;

import java.util.Scanner;

public class App {
    void run() {
        System.out.println("== 명언 앱 ==");

        Scanner scanner = new Scanner(System.in);
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
    }
}