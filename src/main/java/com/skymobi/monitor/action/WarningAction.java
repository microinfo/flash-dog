package com.skymobi.monitor.action;

import com.skymobi.monitor.model.MetricDog;
import com.skymobi.monitor.model.Project;
import com.skymobi.monitor.service.AlertService;
import com.skymobi.monitor.service.EmailService;
import com.skymobi.monitor.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Author: Hill.Hu
 * Email:  hill.hu@sky-mobi.com
 * Date: 11-12-7 上午11:29
 */
@Controller
public class WarningAction {
    private static Logger logger = LoggerFactory.getLogger(WarningAction.class);

    @Resource
    private ProjectService projectService;
    @Resource
    private EmailService emailService;
    @Resource
    AlertService alertService;


    @RequestMapping(value = "/projects/{projectName}/warnings", method = RequestMethod.GET)
    public String listWarnings(ModelMap map, @PathVariable String projectName) throws IOException {
        Project project = projectService.findProject(projectName);
        map.put("project", project);
        map.put("alerts",alertService.findAlerts(projectName));
        return "warning/list";
    }
    @RequestMapping(value = "/projects/{projectName}/warnings", method = RequestMethod.POST)
    public String update(ModelMap map, @PathVariable String projectName, MetricDog metricDog) throws IOException {
        Project project = projectService.findProject(projectName);
        project.saveDog(metricDog);
        projectService.saveProject(project);
        map.put("project", project);

        return "redirect:/projects/" + projectName + "/settings/warnings";
    }

    @RequestMapping(value = "/projects/{projectName}/warnings/clear", method = RequestMethod.GET)
    public String clear(ModelMap map, @PathVariable String projectName) throws IOException {

        alertService.removeAlerts(projectName);
        return "redirect:/projects/" + projectName + "/warnings";
    }

    @RequestMapping(value = "/projects/{projectName}/warnings/{dogName}/destroy")
    public String remove(ModelMap map, @PathVariable String projectName, @PathVariable  String dogName) throws IOException {
        Project project = projectService.findProject(projectName);
        
        project.removeDog(dogName);
        return "redirect:/projects/" + projectName + "/warnings";
    }
}
