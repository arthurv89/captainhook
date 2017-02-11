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
        selfDiagnose.refresh();

        final int failingChecks = failingChecks(selfDiagnose);
        model.put("failingChecks", failingChecks);
        model.put("statusOk", failingChecks > 0);
        model.put("currentTime", Calendar.getInstance());
        model.put("selfdiagnose", selfDiagnose);
        return "plugins/selfdiagnose";
    }

    private int failingChecks(final AbstractSelfDiagnose selfDiagnose) {
        return (int) selfDiagnose.getItems().stream()
                .filter(x -> !x.getValue().isSuccess())
                .count();
    }
}
