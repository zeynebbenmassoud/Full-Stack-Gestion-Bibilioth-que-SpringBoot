package com.angular.springboot.Controller;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.angular.springboot.Service.CommentaireServiceImp;
import com.angular.springboot.Service.EmprunteurServiceImp;
import com.angular.springboot.Service.LivreServiceImp;
import com.angular.springboot.Service.ReservationServiceImpl;
import com.angular.springboot.Model.Livre;
import com.angular.springboot.Model.Reservation;

@Controller
@RequestMapping("/")
public class LivreController {
	
	@Autowired
	LivreServiceImp livre;
	
	@Autowired
	ReservationServiceImpl reservation;
	
	@Autowired
	EmprunteurServiceImp emp;
	
	@Autowired
	CommentaireServiceImp comment;
	
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@GetMapping("index")
	public String getIndexView() {
		return "index";
	}
	
	@GetMapping("image/show")
	String show(Model model, Authentication authentication) {
		
		Livre l = new Livre();
		
		model.addAttribute("searchForm", l);
		
		String name =authentication.getName();
		int val = reservation.ReservationEmprunteur(emp.findByEmail(name).getId());
		log.info("Id :: " + val);
		model.addAttribute("reservation", val);
		
		List<Livre> images = livre.getAllLivre();
		model.addAttribute("categorie", livre.getAllCategories());
		model.addAttribute("images", images);


		return "acceuil";
	}
	

	@GetMapping("image/livreDetails")
	String livreDetails(@RequestParam("id") Long id, Livre book, Model model, Authentication authentication) {
		
		Reservation reser = new Reservation();
		reser.setId(id);
		model.addAttribute("res", reser);
		
		String name =authentication.getName();
		int val = reservation.ReservationEmprunteur(emp.findByEmail(name).getId());
		model.addAttribute("reservation", val);
		
		try {
			log.info("Id :: " + id);
			if (id != 0) {
				book = livre.getLivreById(id);
			
				log.info("products :: " + book);
				if (book != null) {
					model.addAttribute("id", book.getId());
					model.addAttribute("synopsis", book.getSynopsis());
					model.addAttribute("title", book.getTitle());
					model.addAttribute("author", book.getAuthor());
					model.addAttribute("nbr_avai", book.getNbr_availability());
					model.addAttribute("categorie", book.getCategories());
					model.addAttribute("photo", book.getPhoto());
					model.addAttribute("comment", book.getComment());
					return "LivreDetail";
				}
				return "redirect:/index";
			}
		return "redirect:/index";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/index";
		}	
	}
	
	@PostMapping("image/show")
	public String searchLivre(@ModelAttribute("searchForm") Livre l , Model model, Authentication authentication) {

		String name =authentication.getName();
		
		int val = reservation.ReservationEmprunteur(emp.findByEmail(name).getId());
		log.info("Id :: " + val);
		model.addAttribute("reservation", val);
		model.addAttribute("categorie", livre.getAllCategories());
		String title = l.getTitle()=="" ? null:l.getTitle();
		model.addAttribute("images", livre.Search(title, l.getCategories()));
		return "acceuil";
	}
	
				
	@GetMapping("admin/addLivre")
	public ModelAndView AddLivre() {
		ModelAndView model = new ModelAndView();
		Livre l = new Livre();
		model.addObject("LivreForm", l);
		model.setViewName("AddLivre");
		return (model);
	}

	@PostMapping("admin/saveLivre")
	public String saveLivre(@ModelAttribute("LivreForm") Livre l, Model model) {
		livre.saveOrUpdate(l);
		return "redirect:/admin/addLivre";
	}
	
}
