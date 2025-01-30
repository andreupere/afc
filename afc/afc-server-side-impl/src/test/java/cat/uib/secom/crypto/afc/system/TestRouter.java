package cat.uib.secom.crypto.afc.system;

import cat.uib.secom.crypto.afc.bean.TicketINSignedBean;
import cat.uib.secom.crypto.afc.model.router.AFCRouter;
import cat.uib.secom.crypto.afc.model.router.RouterHelper;
import junit.framework.TestCase;

public class TestRouter extends TestCase {

	
	public void test() {
		
		try {
			AFCRouter.userSystemRegistration("1");
			Thread.sleep(1000);
			AFCRouter.userSystemRegistration("2");
			Thread.sleep(1000);
			AFCRouter.userSystemRegistration("3");
			Thread.sleep(1000);
			AFCRouter.userSystemRegistration("4");
			Thread.sleep(1000);
			AFCRouter.userSystemRegistration("5");
			Thread.sleep(1000);
			AFCRouter.userSystemRegistration("6");
			Thread.sleep(1000);
			AFCRouter.userSystemRegistration("7");
			Thread.sleep(1000);
			AFCRouter.userSystemRegistration("8");
			Thread.sleep(1000);
			AFCRouter.userSystemRegistration("9");
			Thread.sleep(1000);
			AFCRouter.userSystemRegistration("10");
			
			
			TicketINSignedBean tinsb_1 = AFCRouter.systemEntrance("1", "1");
			TicketINSignedBean tinsb_8 = AFCRouter.systemEntrance("8", "1");
			TicketINSignedBean tinsb_7 = AFCRouter.systemEntrance("7", "1");
			
			TicketINSignedBean tinsb_9 = AFCRouter.systemEntrance("9", "1");
			
			RouterHelper toutsb_8 = AFCRouter.systemExit("8", "3", tinsb_8.getTicketIN().getSerialNumber());
	
			
			TicketINSignedBean tinsb_4 = AFCRouter.systemEntrance("4", "1");
	
			TicketINSignedBean tinsb_10 = AFCRouter.systemEntrance("10", "1");
			
			
	
			TicketINSignedBean tinsb_3 = AFCRouter.systemEntrance("3", "1");
	
			TicketINSignedBean tinsb_2 = AFCRouter.systemEntrance("2", "1");
			RouterHelper toutsb_9 = AFCRouter.systemExit("9", "3", tinsb_9.getTicketIN().getSerialNumber());
	
			RouterHelper toutsb_3 = AFCRouter.systemExit("3", "4", tinsb_3.getTicketIN().getSerialNumber());
	
			RouterHelper toutsb_1 = AFCRouter.systemExit("1", "1", tinsb_1.getTicketIN().getSerialNumber());
			RouterHelper toutsb_2 = AFCRouter.systemExit("2", "3", tinsb_2.getTicketIN().getSerialNumber());
			RouterHelper toutsb_4 = AFCRouter.systemExit("4", "1", tinsb_4.getTicketIN().getSerialNumber());
			
			TicketINSignedBean tinsb_6 = AFCRouter.systemEntrance("6", "1");
	
			
			TicketINSignedBean tinsb_5 = AFCRouter.systemEntrance("5", "1");
	
			RouterHelper toutsb_6 = AFCRouter.systemExit("6", "2", tinsb_6.getTicketIN().getSerialNumber());
			RouterHelper toutsb_7 = AFCRouter.systemExit("7", "4", tinsb_7.getTicketIN().getSerialNumber());
			RouterHelper toutsb_5 = AFCRouter.systemExit("5", "3", tinsb_5.getTicketIN().getSerialNumber());
			RouterHelper toutsb_10 = AFCRouter.systemExit("10", "4", tinsb_10.getTicketIN().getSerialNumber());
			
			
			
			//TicketINSignedBean tinsb_1 = AFCRouter.systemEntrance("1", "1");
			
			//TicketINSignedBean tinsb_5 = AFCRouter.systemEntrance("5", "1");
			
			//RouterHelper toutsb_1 = AFCRouter.systemExit("1", "2", tinsb_1.getTicketIN().getSerialNumber());
			
			//TicketINSignedBean tinsb_8 = AFCRouter.systemEntrance("8", "1");
			
			//RouterHelper toutsb_5 = AFCRouter.systemExit("5", "5", tinsb_5.getTicketIN().getSerialNumber());
			//RouterHelper toutsb_8 = AFCRouter.systemExit("8", "3", tinsb_8.getTicketIN().getSerialNumber());
		} catch (Exception e) {
			
		}
		
	}
	
	
}
