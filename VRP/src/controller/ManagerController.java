package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ManagerController {
	
	@RequestMapping("/firstLogin")
	public ModelAndView redirectNotification(){
		return new ModelAndView("firstLogin");
	}
	
	@RequestMapping("/companyInfo")
	public ModelAndView provideCompanyInfo(){
		return new ModelAndView("companyInfo");
	}
	
	@RequestMapping("/managerHomepage")
	public ModelAndView showManagerHomepage(){
		return new ModelAndView("managerHomepage");
	}
}
