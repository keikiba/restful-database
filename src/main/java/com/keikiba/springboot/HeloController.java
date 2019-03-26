package com.keikiba.springboot;

//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;
import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import org.springframework.beans.factory.annotation.Autowired;
import com.keikiba.springboot.repositories.MyDataRepository;

//@RestController
@Controller
public class HeloController {

//	String[] names = { "tuyano", "hanako", "taro", "sachiko", "ichiro" };
//	String[] mails = { "syoda@tuyano.com", "hanako@flower", "taro@yamda", "sachiko@happy", "ichiro@base" };

	@RequestMapping("/{num}")
	public String index2(@PathVariable int num, Model model) {
		int res = 0;
		for (int i = 1; i <= num; i++)
			res += i;
		model.addAttribute("msg", "total: " + res);
		return "index";		
	}

	@RequestMapping("/mav-{num}")
	public ModelAndView index3(@PathVariable int num, ModelAndView mav) {
		int res = 0;
		for (int i = 1; i <= num; i++)
			res += i;
		mav.addObject("msg", "total: " + res + " (via ModelAndView object)");
		mav.setViewName("index3");
		//
		mav.addObject("id",  num);
		mav.addObject("check", num >= 0);
		mav.addObject("trueVal",  "POSITIVE!");
		mav.addObject("falseVal",  "negative...");
		//
		mav.addObject("num", num);
		//
		return mav;
	}
		
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("msg", "フォームを送信してください");
		return mav;
	}

	@RequestMapping(value="/", method=RequestMethod.POST)
	public ModelAndView send(
			@RequestParam(value="check1", required=false)boolean check1, 
			@RequestParam(value="radio1", required=false)String radio1, 
			@RequestParam(value="select1", required=false)String select1, 
			@RequestParam(value="select2", required=false)String[] select2, 
			ModelAndView mav) {
		String res = "";
		try {
			res = "check:" + check1 + " radio:" + radio1 + " select:" + select1 + " select2:";
		} catch (NullPointerException e) {}
		try {
			res += select2[0];
			for (int i = 1; i < select2.length; i++)
				res += ", " + select2[i];
		} catch (NullPointerException e) {
			res += "null";
		}
		
		mav.addObject("msg", res);
		mav.setViewName("index");
		return mav;
	}
	
	@RequestMapping("/redirect")
	public String other() {
		return "redirect:/";
	}

	@RequestMapping("/forward")
	public String other2() {
		return "forward:/";
	}

//	@RequestMapping("/{num}")
//	public String index2(@PathVariable int num) {
//		int res = 0;
//		for (int i = 1; i <= num; i++)
//			res += i;
//		return "total: " + res;
//	}
	
	@RequestMapping("/dataobject")
	public ModelAndView dataobject(ModelAndView mav) {
		mav.setViewName("index2");
		//
		mav.addObject("datamsg", "current data.");
		DataObject obj = new DataObject(123, "Hanako", "hanako@flower");
		mav.addObject("object", obj);
		// 
		ArrayList<String[]> data = new ArrayList<String[]>();
		data.add(new String[] {"taro", "taro@yamdada", "0901234"});
		data.add(new String[] {"hanako", "hanako@flower", "09010900"});
		data.add(new String[] {"sachiko", "sachiko@happy", "09209823"});
		mav.addObject("data", data);
		//
		ArrayList<DataObject> data2 = new ArrayList<DataObject>();
		data2.add(new DataObject(0, "taro", "taro@yamdada"));
		data2.add(new DataObject(1, "hanako", "hanako@flower"));
		data2.add(new DataObject(2, "sachiko", "sachiko@happy"));
		mav.addObject("data2", data2);
		//
		return mav;
	}
	
	@Autowired
	MyDataRepository repository;
	
	@RequestMapping(value="/repository", method = RequestMethod.GET)
	public ModelAndView repo(
			@ModelAttribute("formModel") MyData mydata, 
			ModelAndView mav) {
		mav.setViewName("repo");
		mav.addObject("msg", "this is sample content.");
		mav.addObject("formModel",  mydata);
		Iterable<MyData> list = repository.findAll();
		mav.addObject("datalist", list);
		return mav;
	}
	
	@RequestMapping(value="/repository", method = RequestMethod.POST)
	@Transactional(readOnly=false)
	public ModelAndView form(
			@ModelAttribute("formModel") @Validated MyData mydata,
			BindingResult result, 
			ModelAndView mov) {
		ModelAndView res = null;
		if (!result.hasErrors()) {
			repository.saveAndFlush(mydata);
			res = new ModelAndView("redirect:/repository");
		} else {
			mov.setViewName("repo");
			mov.addObject("msg",  "error is occurred...");
			Iterable<MyData> list = repository.findAll();
			mov.addObject("datalist", list);
			res = mov;
		}
		return res;
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView edit(
			@ModelAttribute("formModel") MyData mydata, 
			@PathVariable int id, 
			ModelAndView mav) {
		mav.setViewName("edit");
		mav.addObject("title",  "edit mydata.");
		Optional<MyData> data = repository.findById((long)id);
		mav.addObject("formModel",  data.get());
		return mav;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView update(
			@ModelAttribute("formModel") @Validated MyData mydata,
			BindingResult result, 
			ModelAndView mav) {
		ModelAndView res = null;
		if (!result.hasErrors()) {
			repository.saveAndFlush(mydata);
			res = new ModelAndView("redirect:/repository");
		} else {
			mav.setViewName("repo");
			mav.addObject("msg",  "Validation error in updated data.");
			Iterable<MyData> list = repository.findAll();
			mav.addObject("datalist", list);
			res = mav;
		}
		return res;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(
			@PathVariable int id, 
			ModelAndView mav) {
		mav.setViewName("delete");
		mav.addObject("title",  "delete mydata.");
		Optional<MyData> data = repository.findById((long)id);
		mav.addObject("formModel",  data.get());
		return mav;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView remove(
			@RequestParam long id, 
			ModelAndView mav) {
		repository.deleteById(id);
		return new ModelAndView("redirect:/repository");
	}

	@PostConstruct
	public void init() {
		MyData d1 = new MyData();
		d1.setName("taro");
		d1.setAge(35);
		d1.setMail("taro@a.com");
		d1.setMemo("this is memo.");
		repository.saveAndFlush(d1);

		MyData d2 = new MyData();
		d2.setName("jiro");
		d2.setAge(22);
		d2.setMail("jiro@a.com");
		d2.setMemo("this is memo.");
		repository.saveAndFlush(d2);

		MyData d3 = new MyData();
		d3.setName("gon");
		d3.setAge(23);
		d3.setMail("gon@a.com");
		d3.setMemo("this is memo.");
		repository.saveAndFlush(d3);
	}

}

class DataObject {
	private int id;
	private String name;
	private String value;

	public DataObject(int id, String name, String value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public int getId() { 
		return id; 
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

}
