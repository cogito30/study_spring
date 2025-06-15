package hello.core.web;

@Service
@RequiredArgsConstructor
public class LogDemoService {

    // private final MyLogger myLogger;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    public void logic(String testId) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.log("service id = " + id);
    }  
    
}

