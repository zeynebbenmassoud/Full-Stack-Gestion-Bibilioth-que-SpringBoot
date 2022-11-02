package com.angular.springboot.Repository;


import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.angular.springboot.Model.Emprunteur;
import com.angular.springboot.Model.Livre;
import com.angular.springboot.Model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation,Long>{

	public List<Reservation> findByDateReservation(Date date_reservation);
	
    public List<Reservation> findByDateReservationAndLivreAndEmp(Date date_reservation, Livre livre, Emprunteur emp);

  
    @Query(value = "SELECT r.id, date_reservation, e.nom, l.title, l.author, livre_id " + 
    		"FROM reservation r, livre l, emprunteur e " + 
    		"WHERE r.emprunteur_id = e.id " + 
    		"AND r.livre_id = l.id" + 
    		"AND r.date_reservation BETWEEN \":date_debut\" AND \":date_fin\" " + 	
    		"AND e.nom LIKE \"%:emp%\" " + 
    		"AND l.title LIKE \"%:title%\" " +
    		"AND l.author LIKE \"%:author%\"", nativeQuery = true)
    public List<Reservation> findReservationLike(@Param("date_debut") Date db, @Param("date_fin") Date df , @Param("title") String title, @Param("emp") String emp, @Param("author") String auth);

    @Query(value = "SELECT count(*) FROM reservation WHERE date_reservation= :date_reservation and livre_id= :livre_id", nativeQuery = true)
     public int countReservation(@Param("date_reservation") Date dres, @Param("livre_id") long livre_id);

    @Query(value = "SELECT count(*) FROM reservation WHERE emprunteur_id= :emprunteur_id ", nativeQuery = true)
    public int ReservationEmprunteur(@Param("emprunteur_id") long emprunteur_id);

    @Query(value = "SELECT id, `date_reservation`, `emprunteur_id`, `livre_id` FROM `reservation` WHERE emprunteur_id = :emprunteur_id", nativeQuery = true)
    public List<Reservation> getReservationEmprunteurList(@Param("emprunteur_id") long emprunteur_id);

}