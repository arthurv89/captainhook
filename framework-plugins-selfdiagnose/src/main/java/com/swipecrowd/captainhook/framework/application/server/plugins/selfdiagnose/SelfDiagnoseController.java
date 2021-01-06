package com.swipecrowd.captainhook.framework.application.server.plugins.selfdiagnose;

import com.swipecrowd.captainhook.framework.application.server.plugin.PluginController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Optional;

@Controller
public class SelfDiagnoseController extends PluginController {
    @Autowired(required = false)
    private AbstractSelfDiagnose _selfDiagnose;

    @RequestMapping("/selfdiagnose")
    public ModelAndView welcome() {
        final AbstractSelfDiagnose selfDiagnose = Optional.ofNullable(_selfDiagnose).orElse(new DefaultSelfDiagnose());
        selfDiagnose.refresh();

        final int failingChecks = failingChecks(selfDiagnose);

        ModelAndView modelAndView = new ModelAndView("plugins/selfdiagnose");
        modelAndView.addObject("failingChecks", failingChecks);
        modelAndView.addObject("statusOk", failingChecks == 0);
        modelAndView.addObject("currentTime", Calendar.getInstance());
        modelAndView.addObject("selfdiagnose", selfDiagnose);
        return modelAndView;
    }

    private int failingChecks(final AbstractSelfDiagnose selfDiagnose) {
        return (int) selfDiagnose.getItems().stream()
                .filter(x -> !x.getValue().isSuccess())
                .count();
    }
}
