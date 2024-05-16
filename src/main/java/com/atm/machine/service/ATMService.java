package com.atm.machine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.atm.machine.dao.ATMRepository;
import com.atm.machine.entity.ATM;

@Service
public class ATMService {

	Logger logger = LoggerFactory.getLogger(ATMService.class);

	@Autowired
	private ATMRepository atmRepository;

	public ATM getATM() {
		return atmRepository.findAll().getFirst();
	}

	public ATM updateATM(ATM newATM) {

		ATM oldATM = atmRepository.findAll().getFirst();
		if (oldATM != null) {
			atmMapper(oldATM, newATM);
			return atmRepository.save(oldATM);
		} else {
			return new ATM();
		}
	}

	private static void atmMapper(ATM oldATM, ATM newATM) {
		oldATM.setDenominationFiveHundreds(newATM.getDenominationFiveHundreds());
		oldATM.setDenominationHundreds(newATM.getDenominationHundreds());
		oldATM.setDenominationTwoHundreds(newATM.getDenominationTwoHundreds());
		oldATM.setDenominationTwoThousands(newATM.getDenominationTwoThousands());
	}

	/**
	 * this method initializes the ATM at the start of application
	 * 
	 * @return ATM
	 * 
	 */
	@EventListener(ApplicationReadyEvent.class)
	public ATM initATM() {

		ATM savedATM = null;

		int count = 10;

		try {
			atmRepository.deleteAll();

			ATM atm = new ATM();
			atm.setDenominationFiveHundreds(count);
			atm.setDenominationHundreds(count);
			atm.setDenominationTwoHundreds(count);
			atm.setDenominationTwoThousands(count);

			savedATM = atmRepository.save(atm);

			logger.info("atm initizialised in Database with all denominations = " + count + " | ATMNumber ::: "
					+ atm.getAtmId());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return savedATM;
	}
}
