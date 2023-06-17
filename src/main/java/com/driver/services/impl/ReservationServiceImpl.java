package com.driver.services.impl;

import com.driver.Exceptions.ReservationException;
import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        User user = userRepository3.findById(userId).get();
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

        if(user==null || parkingLot==null)throw new ReservationException();

        List<Spot> spotList = parkingLot.getSpotList();

        SpotType spotType = numberOfWheels==2?SpotType.TWO_WHEELER:numberOfWheels==4?SpotType.FOUR_WHEELER:SpotType.OTHERS;

        Spot foundspot = null;
        int min = Integer.MAX_VALUE;

        for(Spot spot:spotList){
            if(!spot.getOccupied() && spotType==spot.getSpotType() && spot.getPricePerHour()<min){
                min = spot.getPricePerHour();
                foundspot = spot;
            }
        }

        if(foundspot==null)throw new ReservationException();

        Reservation reservation = new Reservation();
//        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);
        reservationRepository3.save(reservation);

        user.getReservationList().add(reservation);
        foundspot.getReservationList().add(reservation);

        foundspot.setOccupied(true);

        userRepository3.save(user);
        spotRepository3.save(foundspot);

        return reservation;
    }
}
