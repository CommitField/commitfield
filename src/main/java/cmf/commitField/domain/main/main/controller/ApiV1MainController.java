package cmf.commitField.domain.main.main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping
public class ApiV1MainController {
    @GetMapping
    @ResponseBody
    public String getMain() {
        return "Hello! cmf main!";
    }
}
