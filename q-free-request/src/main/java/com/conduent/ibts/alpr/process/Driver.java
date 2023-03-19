package com.conduent.ibts.alpr.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Driver {
	public static void main(String[] args) {
		Cust c = new Cust(20, "Danny", 		"xerox", 		"active");
		Cust c1 = new Cust(30, "steven", 	"kaiser", 		"active");
		Cust c2 = new Cust(40, "scott", 	"xerox", 		"inactive");
		Cust c3 = new Cust(50, "lorraine", 	"bluecross", 	"active");
		Cust c4 = new Cust(60, "shawn", 	"xerox", 		"inactive");
		
		List<Cust> list = new ArrayList<>();
		list.add(c);
		list.add(c1);
		list.add(c2);
		list.add(c3);
		list.add(c4);
		Map<String, ?> mapOnCurrentStatus = list.stream().collect(Collectors.groupingBy(Cust::getEmployeeStatus, Collectors.toList()));
		
		mapOnCurrentStatus.forEach((k, v) -> {
			System.out.println("key:"+ k + " value: "+ v);
			for (@SuppressWarnings("unchecked")
				Iterator<? extends Cust> iterator = ((List<? extends Cust>) v).iterator(); iterator.hasNext();) 
			{
				Cust cust = (Cust) iterator.next();
				System.out.println("cust:" +cust);
			}
		});
	}
}
/*
key:inactive value: [Cust [age=40, employeeName=scott, company=xerox, employeeStatus=inactive],  Cust [age=60, employeeName=shawn, company=xerox, employeeStatus=inactive]]
cust:Cust [age=40, employeeName=scott, company=xerox, employeeStatus=inactive]
cust:Cust [age=60, employeeName=shawn, company=xerox, employeeStatus=inactive]

key:active value: [Cust [age=20, employeeName=Danny, company=xerox, employeeStatus=active], Cust [age=30, employeeName=steven, company=kaiser, employeeStatus=active], Cust [age=50, employeeName=lorraine, company=bluecross, employeeStatus=active]]
cust:Cust [age=20, employeeName=Danny, company=xerox, employeeStatus=active]
cust:Cust [age=30, employeeName=steven, company=kaiser, employeeStatus=active]
cust:Cust [age=50, employeeName=lorraine, company=bluecross, employeeStatus=active]
*/
