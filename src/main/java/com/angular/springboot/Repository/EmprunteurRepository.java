package com.angular.springboot.Repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.angular.springboot.Model.Emprunteur;


public interface EmprunteurRepository extends JpaRepository<Emprunteur,Long>{

	//Emprunteur findByUserName(String userName);
	Emprunteur findByEmail(String email);
	
	//Boolean existsByEmail(String Email);
	
	
}
