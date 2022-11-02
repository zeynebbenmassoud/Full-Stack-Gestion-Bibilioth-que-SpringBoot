package com.angular.springboot.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.angular.springboot.Model.Livre;
import com.angular.springboot.Model.Reservation;
import com.angular.springboot.Service.EmprunteurServiceImp;
import com.angular.springboot.Service.LivreServiceImp;
import com.angular.springboot.Service.ReservationServiceImpl;

@Controller	
@RequestMapping("/")
public class ReservationController {

	@Autowired
	ReservationServiceImpl reservation;
	
	@Autowired
	EmprunteurServiceImp emp;
	
	@Autowired
	LivreServiceImp livre;
	
	@GetMapping("reserver")
	public String ListReservation(@RequestParam("id") Long id, Model model) {
			Reservation reser = new Reservation();
			reser.setId(id);
			model.addAttribute("res", reser);
			return "AddReservation";
	}
	
	@PostMapping("reservation/save")
	public String saveOrUpdate(@RequestParam("id") Long id, @ModelAttribute("res") Reservation res, Authentication authentication, Model model) {
			String name =authentication.getName();
			res.setEmp(emp.findByEmail(name));
			Livre l = livre.getLivreById(id);
			res.setLivre(l);
			boolean count = reservation.countReservation(res.getDate_reservation(), id) == l.getNbr_availability() ? true : false;
			if(count) {
				model.addAttribute("error", "cette date est déjà reserver, choisir une autre date");
				return "redirect:/image/livreDetails?id="+ id ;
			}else {
				reservation.saveOrUpdate(res);
			}
			return "redirect:/image/show";
	}
	
	@GetMapping("reservationEmprunteur")
	public String reservationEmprunteur(Model model, Authentication authentication) {
		String name =authentication.getName();
		long emprunteur_id = emp.findByEmail(name).getId();
		List<Reservation> res = reservation.getReservationEmprunteurList(emprunteur_id);
		model.addAttribute("reservation", res);
		return "ReservationEmpList";
	}
	
	 @RequestMapping("Edit_reservation_List/{id}")
		public ModelAndView editReservation_List(@PathVariable(name = "id") int id) {
		 	
		 	ModelAndView model = new ModelAndView();
		 	Reservation res = reservation.getReservationById(id);
			model.addObject("res", res);
			model.setViewName("Edit_reservation_List");
			return model;
		    
		}
	
	
}
