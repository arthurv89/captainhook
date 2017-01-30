package nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

@Controller
public class SelfDiagnoseController {
    @Autowired(required = false)
    private AbstractSelfDiagnose _selfDiagnose;

    @RequestMapping("/selfdiagnose")
    public String welcome(Map<String, Object> model) {
        final AbstractSelfDiagnose selfDiagnose = Optional.ofNullable(_selfDiagnose).orElse(new DefaultSelfDiagnose());

        model.put("items", selfDiagnose.getItems());
        model.put("time", Calendar.getInstance().getTime());
        model.put("version", selfDiagnose.getVersion());
        return "plugins/selfdiagnose";
    }
}
