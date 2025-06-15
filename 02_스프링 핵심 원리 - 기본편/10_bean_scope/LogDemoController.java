package hello.core.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    // private final MyLogger myLogger;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    // @RequestMappin("log-demo")
    // @ResponseBody
    // public String logDemo(HttpServletRequest request) {
    //     String requestURL = request.getRequestURL().toString();
    //     myLogger.setRequestURL(requestURL);

    //     myLogger.log("controller test");
    //     logDemoService.logic("testId");
    //     return "OK";
    // }y

    @RequestMappin("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        Thread.sleep(100);
        logDemoService.logic("testId");
        return "OK";
    }
}

