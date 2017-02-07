package nl.arthurvlug.captainhook.framework.server.plugins.selfdiagnose;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

        model.put("statusOk", statusOk(selfDiagnose));
        model.put("selfdiagnose", selfDiagnose);
        return "plugins/selfdiagnose";
    }

    private boolean statusOk(final AbstractSelfDiagnose selfDiagnose) {
        return !selfDiagnose.getItems().stream()
                .filter(x -> !x.getValue().isSuccess())
                .findFirst()
                .isPresent();
    }
}
