package hello.hellospring.controller;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        retur "home";
    }
}