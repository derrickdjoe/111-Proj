import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ft {

	public static Connection conn = null;
	public static Statement stat;
	public static Scanner input = new Scanner(System.in);
	public static int whereToGo = 999;
	public static fit fittingList[] = new fit[20];
	public static shipL shipList[] = new shipL[512];
	public static modL modList[] = new modL[999];
	private static String HDEF = "Empty High Slot";
	private static String MDEF = "Empty Mid Slot";
	private static String LDEF = "Empty Low Slot";

	private static int holdID = 0;

	static class modL{

		int modId;
		String modName;

		public modL() {

		}

	}

	static class fit{

		int fitIDNum = -999;
		String fitName;
		String shipName;
		int velocity = 0;
		int mass = 0;
		int Damage = 0;
		//int DPS = 0;
		int agility = 0;
		int intertia = 0;
		int droneDamage = 0;

		double armHp = 0;
		double shiHp = 0;
		double strutHp = 0;
		double velocitM = 0;
		double rep = 0;
		double alpha = 0;
		double DPS = 0;
		double scanRez = 0;

		public fit() {

		}

	}

	static class shipL{

		String shipName;
		int shipID = 0;

		public shipL() {

			//this.shipName = shipName;
			//this.shipID = shipID;

		}

	}


	public static void main(String[] args) throws SQLException {

		init();

	}

	public static void init() throws SQLException{

		while(whereToGo != 101){

			System.out.println("1 to Connect to the Gear Room");
			System.out.println("2 to Exit");
			whereToGo = input.nextInt();

			if(whereToGo == 1) {

				connectToDB();
				System.out.println("1 to create a new fitting");
				System.out.println("2 to view existing fits");
				System.out.println("3 to exit");
				whereToGo = input.nextInt();
				m2(whereToGo);

			}

			if(whereToGo == 2) {

				exitFromDB();
				whereToGo = 101;

			}

			if(whereToGo == 999) {

				connectToDB();
				//System.out.println("Connected");
				createFitTable();
				init();

			}

			if(whereToGo == 888) {

				for(int i = 0; i < shipList.length; i++) {

					shipL temp = new shipL();
					temp.shipID = i;
					temp.shipName = "temp";
					shipList[i] = temp;
					//shipList[i].shipID = i;
					//shipList[i].shipName = "temp";

				}

				for(int i = 0; i < fittingList.length; i++) {

					fit temp = new fit();
					temp.fitIDNum = -999;
					fittingList[i] = temp;

				}

				for(int i = 0; i < modList.length; i++) {

					modL temp = new modL();
					temp.modId = i;
					temp.modName = "temp";

				}

			}

		}

		System.exit(0);

	}

	public static void m2(int where) throws SQLException{

		if(where == 1) {

			System.out.println("1 to enter the ship you would like to fit");
			System.out.println("2 to see a list of all ships");
			System.out.println("3 to return to the menu");
			int nextStep = input.nextInt();

			m3(nextStep);

		}

		if(where == 2) {

			Statement stat44 = conn.createStatement();
			ResultSet q44 = stat44.executeQuery("SELECT fitName AS name FROM fittingTable");

			int i = 0;
			while(q44.next()) {

				String name = q44.getString("name");
				System.out.println(i + " " + name);
				fit tfit = new fit();
				tfit.shipName = name;
				fittingList[i] = tfit;
				i++;

			}

			System.out.println("Enter the ID of the fitting you would like to edit");
			int toGet = input.nextInt();

			Statement stat45 = conn.createStatement();
			ResultSet q45 = stat45.executeQuery("SELECT fitID AS id FROM fittingTable WHERE fitName == \"" + fittingList[toGet].fitName + "\"");

			int idholder = 0;
			while(q45.next()) {

				idholder = q45.getInt("id");

			}

			displayFitting(idholder);
			editFitting(idholder);

			/*int count = 0;
			for(int i = 0; i <fittingList.length; i++) {

				if(fittingList[i] != null) {

					count++;

				}

			}

			while(count < 0) {

				for(int i = 0; i < fittingList.length; i++) {

					if(fittingList[i] != null) {

						System.out.println("FittingID : " + i + " Fitting Name : " + fittingList[i].fitName);

					}

				}

				System.out.println("Type the fitting ID to select");
				int whatFit = input.nextInt();
				editFitting(whatFit);

			}

			System.out.println("No saved fittings, exiting");*/
			//helperM2();

		}

		if(where == 3) {

			init();

		}
	}

	public static void helperM2() throws SQLException {

		//System.out.println("At helper");
		System.out.println("1 to create a new fitting");
		System.out.println("2 to view existing fits");
		System.out.println("3 to exit");

		int nextTarget = input.nextInt();
		m2(nextTarget);

	}

	public static void m3(int nextStep) throws SQLException{

		if(nextStep == 1) {

			System.out.println("Enter the name of the ship you want to fit");
			String shipToFit = input.next();
			shipToFit += input.nextLine();

			//System.out.println(shipToFit);
			Statement stat63 = conn.createStatement();
			ResultSet q63 = stat63.executeQuery("SELECT invTypes.typeName AS name " + 
					"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " + 
					"WHERE " + 
					"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
					"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
					"dgmAttributeTypes.attributeName LIKE '%hiSlots%' AND " +
					"invTypes.typeName LIKE '%" + shipToFit + "%' ");


			int i = 0;
			while (q63.next()) {

				String temp = q63.getString("name");
				System.out.println(i + " " + temp);
				shipL stemp = new shipL();
				stemp.shipID = i;
				stemp.shipName = temp;
				shipList[i] = stemp;
				i++;

			}

			//System.out.println(i);
			System.out.println("Enter the ID of the ship you want to fit");
			int id = input.nextInt();
			holdID = id;
			System.out.print("Fit " + shipList[id].shipName + " 1 yes, 2 no");
			int whatNext = input.nextInt();

			if(whatNext == 1) {

				for(int j = 0; j < shipList.length; j++) {

					if(shipList[j].shipID == holdID) {

						q63.close();
						stat63.close();
						int temp = createFitting(holdID);
						displayFitting(temp);
						editFitting(temp);

					}
				}
			}

			if(whatNext == 2) {

				m3(1);

			}



		}

		if(nextStep == 2) {

			shipSearchHelper();
			int whatNext = input.nextInt();

			if(whatNext == 1) {

				for(int i = 0; i < shipList.length; i++) {

					if(shipList[i].shipID == holdID) {

						int temp = createFitting(holdID);
						displayFitting(temp);
						editFitting(temp);

					}

				}

			}

			if(whatNext == 2) {

				m3Helper();

			}

		}

		if(nextStep == 3) {

			helperM2();

		}

	}

	public static void m3Helper() throws SQLException{

		shipSearchHelper();
		int whatNext = input.nextInt();

		if(whatNext == 1) {

			for(int i = 0; i < shipList.length; i++) {

				if(shipList[i].shipID == holdID) {

					int temp = createFitting(holdID);
					displayFitting(temp);
					editFitting(temp);

				}

			}

		}

		if(whatNext == 2) {

			m3Helper();

		}

	}

	public static void shipSearchHelper() throws SQLException{

		Statement stat = conn.createStatement();
		ResultSet q1 = stat.executeQuery("SELECT invTypes.typeName AS holder " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"dgmAttributeTypes.attributeName LIKE '%hiSlots%' ");

		//System.out.println("Did Query");
		int i = 0;

		/*for (int j = 0; j < shipList.length; j++) {
			shipList[j].shipID = j;
			shipList[j].shipName = " ";

		}*/

		while(q1.next()) {

			if(i == 511) {

				break;

			}

			//shipList[i].shipID = i;
			//shipList[i].shipName = q1.getString("holder");
			//System.out.println(i + " " + shipList[i].shipName);
			String temp = q1.getString("holder");
			System.out.println(i + " " + temp);
			shipL stemp = new shipL();
			stemp.shipID = i;
			stemp.shipName = temp;
			shipList[i] = stemp;
			i++;

		}

		//System.out.println(i);
		System.out.println("Enter the ID of the ship you want to fit");
		int id = input.nextInt();
		holdID = id;
		System.out.print("Fit " + shipList[id].shipName + " 1 yes, 2 no");

	}

	public static void connectToDB(){

		try {

			conn = DriverManager.getConnection("jdbc:sqlite:/home/user/Desktop/DBPJ/sqlite-latest.sqlite");

		}catch(SQLException e) {

			System.err.println(e.getMessage());

		}

	}

	public static int createFitting(int fid) throws SQLException{

		shipL myShip = shipList[fid];
		//int newfID = -555;

		/*for(int i = 0; i < fittingList.length; i++) {

			fit temp = fittingList[i];
			//System.out.println(temp.fitIDNum);
			if(temp.fitIDNum == -999) {

				newfID = i;

			}

		}*/

		Statement stat69 = conn.createStatement();
		ResultSet q69 = stat69.executeQuery("SELECT fitID AS id FROM fittingTable");

		int newfID = 0;
		while(q69.next()) {

			int holder = q69.getInt("id");
			if(newfID != holder) {

				break; 

			}

			newfID++;

		}

		System.out.println("Enter a fit name");
		String fname = input.next();
		fit temp = new fit();
		temp.fitIDNum = newfID;
		temp.fitName = fname;
		temp.shipName = myShip.shipName;
		fittingList[newfID] = temp;
		//System.out.println(newfID);
		if(newfID == 20) {

			System.out.println("Too many saved fits, exiting");
			init();

		}

		fit myFit = fittingList[fid];
		int numHi = 0;
		int numMed = 0;
		int numLo = 0;

		Statement stat = conn.createStatement();
		stat.executeUpdate("INSERT INTO fittingTable VALUES( " + newfID  + " , \"" + fname + "\" , \"" + myShip.shipName + "\" , " +
				"-999, -999, -999, -999, -999, -999, -999, -999, -999, " +
				"-998, -998, -998, -998, -998, -998, -998, -998, -998, " +
				"-997, -997, -997, -997, -997, -997, -997, -997, -997, " +
				"-996, -996, -996, " +
				"-995, -995, -995, -995, -995," +
				"-995)");
		Statement stat1 = conn.createStatement();
		ResultSet q2 = stat.executeQuery("SELECT invTypes.typeName, dgmTypeAttributes.valueFloat AS holder " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName LIKE \"" + myShip.shipName + "\" AND " + 
				"dgmAttributeTypes.attributeName LIKE '%hiSlots%'; ");
		Statement stat2 = conn.createStatement();
		ResultSet q3 = stat2.executeQuery("SELECT invTypes.typeName, dgmTypeAttributes.valueFloat AS holder " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName LIKE \"" + myShip.shipName + "\" AND " + 
				"dgmAttributeTypes.attributeName LIKE '%medSlots%'; ");
		Statement stat3 = conn.createStatement();
		ResultSet q4 = stat3.executeQuery("SELECT invTypes.typeName, dgmTypeAttributes.valueFloat AS holder " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName LIKE \"" + myShip.shipName + "\" AND " + 
				"dgmAttributeTypes.attributeName LIKE '%lowSlots%'; ");

		while (q2.next()) {

			numHi = q2.getInt("holder");
			//numHi = Integer.parseInt(temp);

		}

		while(q3.next()) {

			numMed = q3.getInt("holder");
			//numMed = Integer.parseInt(temp);

		}

		while(q4.next()) {

			numLo = q4.getInt("holder");
			//numLo = Integer.parseInt(temp);

		}

		//System.out.print(numHi + " " + numMed + " " + numLo + " ");

		q2.close();
		q3.close();
		q4.close();

		/*String testa = "temp" + 1;
		System.out.print(testa);*/

		for(int i = 0; i < numHi; i++) {

			String tempslot = "h" + (i + 1);
			Statement stat10 = conn.createStatement();
			//System.out.println(tempslot);
			stat10.executeUpdate("UPDATE fittingTable SET \"" + tempslot + "\" = -888");
			stat10.close();

		}

		for(int i = 0; i < numMed; i++) {

			String tempslot = "m" + (i + 1);
			Statement stat11 = conn.createStatement();
			stat11.executeUpdate("UPDATE fittingTable SET \"" + tempslot + "\" = -888");
			stat11.close();

		}

		for(int i = 0; i < numLo; i++) {

			String tempslot = "l" + (i + 1);
			Statement stat12 = conn.createStatement();
			stat12.executeUpdate("UPDATE fittingTable SET \"" + tempslot + "\" = -888");
			stat12.close();

		}

		for(int i = 0; i < 3; i++) {

			String tempslot = "r" + (i + 1);
			Statement stat13 = conn.createStatement();
			stat13.executeUpdate("UPDATE fittingTable SET \"" + tempslot + "\" = -888");
			stat13.close();


		}

		Statement stat19 = conn.createStatement();
		stat19.executeUpdate("UPDATE fittingTable SET a1 = - 888");
		stat19.close();

		//TODO check if dronebay exists

		stat.close();
		stat1.close();
		stat2.close();
		stat3.close();

		return newfID;

	}

	public static void editFitting(int fid) throws SQLException{

		//displayFitting(fid);
		System.out.println("1 for Editing fit");
		System.out.println("2 to delete fit");
		System.out.println("3 to exit");

		whereToGo = input.nextInt();

		if(whereToGo == 1) {

			System.out.println("Enter the slot you want to edit");
			String slotS = input.next();
			System.out.println("1 for editing the current module");
			System.out.println("2 to delete the current module" );
			System.out.println("3 to exit");
			int nextStep = input.nextInt();

			if(nextStep == 1) {

				searchModules(fid, slotS);

			}

			if(nextStep == 2) {

				Statement stat = conn.createStatement();
				stat.executeUpdate("UPDATE fittingTable SET \"" + slotS + "\" = -888");
				displayFitting(fid);
				editFitting(fid);

			}

			if(nextStep == 3) {

				editFitting(fid);
			}
		}

		if(whereToGo == 2) {

			Statement stat = conn.createStatement();
			stat.executeUpdate("DELETE FROM fittingTable WHERE fitID == " + fid + ";");
			fittingList[fid] = null;
			init();

		}

		if(whereToGo == 3) {

			init();
		}

	}

	public static void displayFitting(int fid) throws SQLException{

		System.out.println("Display Fitting");
		Statement stat = conn.createStatement();
		ResultSet q1 = stat.executeQuery("SELECT * FROM fittingTable WHERE fitID = " + fid + " " );
		int i = 0;

		System.out.println("High Slots");
		for(int j = 0; j < 9; j++) {

			int temp = q1.getInt("h" + (i + 1));
			i++;
			if(temp != -999) {

				System.out.print("h" + i + " ");
				if(temp == -888) {

					System.out.println("Empty High Slot");

				}

				Statement stat1 = conn.createStatement();
				ResultSet q2 = stat1.executeQuery("SELECT invTypes.typeName AS holder FROM invTypes WHERE invTypes.typeID == " + temp + " " );

				while(q2.next()) {

					System.out.println(q2.getString("holder"));

				}

				q2.close();
				stat1.close();

			}

		}

		int x = 0;
		System.out.println("Mid Slots");
		for(int j = 0; j < 9; j++) {

			int temp = q1.getInt("m" + (x + 1));
			x++;
			if(temp != -998) {

				System.out.print("m" + x + " ");
				if(temp == -888) {

					System.out.println("Empty Med Slot");

				}

				Statement stat2 = conn.createStatement();
				ResultSet q3 = stat2.executeQuery("SELECT invTypes.typeName AS holder FROM invTypes WHERE invTypes.typeID == " + temp + " ");

				while(q3.next()) {

					System.out.println(q3.getString("holder"));

				}

				q3.close();
				stat2.close();

			}

		}

		int y = 0;
		System.out.println("Low Slots");
		for(int j = 0; j < 9; j++) {

			int temp = q1.getInt("l" + (y + 1));
			y++;
			if(temp != -997) {

				System.out.print("l" + y + " ");
				if(temp == -888) {

					System.out.println("Empty Low Slot");

				}

				Statement stat3 = conn.createStatement();
				ResultSet q4 = stat3.executeQuery("SELECT invTypes.typeName AS holder FROM invTypes WHERE invTypes.typeID == " + temp + " ");

				while(q4.next()) {

					System.out.println(q4.getString("holder"));

				}

				q4.close();
				stat3.close();

			}

		}

		int z = 0;
		System.out.println("Rig Slots");
		for(int j = 0; j < 3; j++) {

			int temp = q1.getInt("r" + (z + 1));
			z++;
			if(temp != -996) {

				System.out.print("r" + z + " ");
				if(temp == -888) {

					System.out.println("Empty Rig Slot");

				}

				Statement stat4 = conn.createStatement();
				ResultSet q5 = stat4.executeQuery("SELECT invTypes.typeName AS holder FROM invTypes WHERE invTypes.typeID == " + temp + " ");

				while(q5.next()) {

					System.out.println(q5.getString("holder"));

				}

				q5.close();
				stat4.close();
			}

		}

		System.out.println("Ammo");
		int temp = q1.getInt("a1");

		if(temp!= -995) {

			if(temp == -888) {

				System.out.println("Empty Ammo Slot");

			}

			Statement gAmmo = conn.createStatement();
			ResultSet ammoG = gAmmo.executeQuery("SELECT invTypes.typeName AS holder FROM invTypes WHERE invTypes.typeID == " + temp + " ");

			while(ammoG.next()) {

				System.out.println(ammoG.getString("holder"));

			}

			ammoG.close();
			gAmmo.close();

		}

		doCalcs(fid);

		//q1.close();
		//stat.close();
	}

	public static void searchModules(int fid, String slot) throws SQLException{

		System.out.println("1 to enter the name of the module you want to fit");
		System.out.println("2 to see a list of all moudles");
		System.out.println("3 to fit ammo");
		//System.out.println("4 to fit drones");
		System.out.println("5 to exit");

		int whereGo = input.nextInt();

		if(whereGo == 1) {

			System.out.println("Enter the name of the module");
			String ms = input.next();
			ms += input.nextLine();

			//System.out.println(ms);
			Statement stat36 = conn.createStatement();
			ResultSet q36 = stat36.executeQuery("SELECT invTypes.typeName AS name " +
					"FROM invTypes " +
					"WHERE " +
					"invTypes.typeName LIKE '%" + ms + "%' ");

			int i = 0;
			while (q36.next()) {

				String modHolder = q36.getString("name");
				System.out.println(i + " " + modHolder);
				modL holder = new modL();
				holder.modId = i;
				holder.modName = modHolder;
				modList[i] = holder;
				i++;

			}

			System.out.println("Enter the ID of the module you want to fit");
			int uIn = input.nextInt();
			System.out.print("Fit " + modList[uIn].modName + " 1 for Yes, 2 for No");
			int nextS = input.nextInt();

			if(nextS == 1) {

				//System.out.println(modList[uIn].modName);
				Statement stat101 = conn.createStatement();
				ResultSet q101 = stat101.executeQuery("SELECT invTypes.typeID AS idn " +
						"FROM invTypes " +
						"WHERE " +
						"invTypes.typeName = '" + modList[uIn].modName + "' ");

				int numToPush = 0;
				while(q101.next()) {

					numToPush = q101.getInt("idn");
					System.out.println(numToPush);

				}

				Statement stat39 = conn.createStatement();
				stat39.executeUpdate("UPDATE fittingTable SET \"" + slot + "\" = \"" + numToPush + "\"");

				stat39.close();
				q101.close();
				stat101.close();
				q36.close();
				stat36.close();
				displayFitting(fid);
				editFitting(fid);

			}

			if(nextS == 2) {

				searchModules(fid, slot);

			}

		}

		if(whereGo == 2) {

			fit temp = fittingList[fid];
			Statement stat = conn.createStatement();
			ResultSet q1 = stat.executeQuery("SELECT valueFloat AS size " + 
					"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " + 
					"WHERE " + 
					"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
					"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
					"dgmAttributeTypes.displayName LIKE '%Rig Size%' AND " + 
					"invTypes.typeName LIKE \"" + temp.shipName + "\" ");

			int modSize = q1.getInt("size");
			//System.out.println(modSize);
			String mSize = null;
			if(modSize == 1) {

				mSize = "small";

			}

			if(modSize == 2) {

				mSize = "medium";

			}

			if(modSize == 3) {

				mSize = "large";

			}

			if(modSize == 4) {

				mSize = "capital";

			}

			if(modSize > 1 || modSize > 4) {

				System.out.println("Trying to fit unsupported ship, exiting");
				init();

			}

			//System.out.println(mSize);
			Statement stat99 = conn.createStatement();
			ResultSet q99 = stat99.executeQuery("SELECT invTypes.typeName AS name " +
					"FROM invTypes " +
					"WHERE " +
					"invTypes.typeName LIKE '%" + mSize + "%' ");

			int i = 0;
			while(q99.next()) {

				String modHolder = q99.getString("name");
				System.out.println(i + " " + modHolder);
				modL holder = new modL();
				holder.modId = i;
				holder.modName = modHolder;
				modList[i] = holder;
				i++;

			}

			System.out.println("Enter the ID of the module you want to fit");
			int uIn = input.nextInt();
			System.out.print("Fit " + modList[uIn].modName + " 1 for Yes, 2 for No");
			int nextS = input.nextInt();

			if(nextS == 1) {

				//System.out.println(modList[uIn].modName);
				Statement stat100 = conn.createStatement();
				ResultSet q100 = stat100.executeQuery("SELECT invTypes.typeID AS idn " +
						"FROM invTypes " +
						"WHERE " +
						"invTypes.typeName = '" + modList[uIn].modName + "' ");

				int numToPush = 0;
				while(q100.next()) {

					numToPush = q100.getInt("idn");
					System.out.println(numToPush);

				}

				Statement stat3 = conn.createStatement();
				stat3.executeUpdate("UPDATE fittingTable SET \"" + slot + "\" = \"" + numToPush + "\"");

				stat3.close();
				q100.close();
				stat100.close();
				q99.close();
				stat99.close();
				displayFitting(fid);
				editFitting(fid);

			}

			if(nextS == 2) {

				searchModules(fid, slot);

			}

		}

		if(whereGo == 3) {

			System.out.println("1 to Enter the Ammo you are trying to use");
			System.out.println("2 to view a list of all Ammo");
			System.out.println("3 to Exit");
			int nextStop = input.nextInt();

			if(nextStop == 1) {

				System.out.println("Enter the name of the module");
				String ms = input.next();
				ms += input.nextLine();

				//System.out.println(ms);
				Statement stat36 = conn.createStatement();
				ResultSet q36 = stat36.executeQuery("SELECT invTypes.typeName AS name " +
						"FROM invTypes " +
						"WHERE " +
						"invTypes.typeName LIKE '%" + ms + "%' ");

				int i = 0;
				while (q36.next()) {

					String modHolder = q36.getString("name");
					System.out.println(i + " " + modHolder);
					modL holder = new modL();
					holder.modId = i;
					holder.modName = modHolder;
					modList[i] = holder;
					i++;

				}

				System.out.println("Enter the ID of the Ammo you want to fit");
				int uIn = input.nextInt();
				System.out.print("Fit " + modList[uIn].modName + " 1 for Yes, 2 for No");
				int nextS = input.nextInt();

				if(nextS == 1) {

					//System.out.println(modList[uIn].modName);
					Statement stat101 = conn.createStatement();
					ResultSet q101 = stat101.executeQuery("SELECT invTypes.typeID AS idn " +
							"FROM invTypes " +
							"WHERE " +
							"invTypes.typeName = '" + modList[uIn].modName + "' ");

					int numToPush = 0;
					while(q101.next()) {

						numToPush = q101.getInt("idn");
						System.out.println(numToPush);

					}

					Statement stat39 = conn.createStatement();
					stat39.executeUpdate("UPDATE fittingTable SET a1 = \"" + numToPush + "\"");

					stat39.close();
					q101.close();
					stat101.close();
					q36.close();
					stat36.close();
					displayFitting(fid);
					editFitting(fid);

				}

				if(nextS == 2) {

					displayFitting(fid);
					editFitting(fid);

				}

			}

			if(nextStop == 2) {

				Statement getAmmo = conn.createStatement();
				ResultSet gotAmmo = getAmmo.executeQuery("SELECT invTypes.typeName " +
						"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " + 
						"WHERE " + 
						"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
						"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
						"dgmTypeAttributes.attributeID = 137 ");

				int iter = 0;
				while(gotAmmo.next()) {

					String modHolder = gotAmmo.getString("name");
					System.out.println(iter + " " + modHolder);
					modL holder = new modL();
					holder.modId = iter;
					holder.modName = modHolder;
					modList[iter] = holder;
					iter++;

				}

				System.out.println("Enter the ID of the module you want to fit");
				int uIn = input.nextInt();
				System.out.print("Fit " + modList[uIn].modName + " 1 for Yes, 2 for No");
				int nextS = input.nextInt();

				if(nextS == 1) {

					//System.out.println(modList[uIn].modName);
					Statement stat100 = conn.createStatement();
					ResultSet q100 = stat100.executeQuery("SELECT invTypes.typeID AS idn " +
							"FROM invTypes " +
							"WHERE " +
							"invTypes.typeName = '" + modList[uIn].modName + "' ");

					int numToPush = 0;
					while(q100.next()) {

						numToPush = q100.getInt("idn");
						//System.out.println(numToPush);

					}

					Statement stat3 = conn.createStatement();
					stat3.executeUpdate("UPDATE fittingTable SET a1 = \"" + numToPush + "\"");

					displayFitting(fid);
					editFitting(fid);

				}

				if(nextS == 2) {

					displayFitting(fid);
					editFitting(fid);

				}
			}

			if(nextStop == 3) {

				displayFitting(fid);
				editFitting(fid);

			}

		}

		if(whereGo == 4) {

		}

		if(whereGo == 5) {

			editFitting(fid);

		}
	}

	public static void doCalcs(int fid) throws SQLException{

		fit toDisplay = new fit();
		Statement stat900 = conn.createStatement();
		ResultSet q900 = stat900.executeQuery("SELECT * " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " ");

		while(q900.next()) {

			toDisplay.mass = q900.getInt("mass");

		}

		Statement exR = conn.createStatement();
		ResultSet exR1 = exR.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 268");

		double aEx = exR1.getInt("value");

		Statement exR2 = conn.createStatement();
		ResultSet exR3 = exR2.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 272");	

		double sEx = exR3.getInt("value");

		Statement kR = conn.createStatement();
		ResultSet kR1 = kR.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 269");

		int aK = kR1.getInt("value");

		Statement kR2 = conn.createStatement();
		ResultSet kR3 = kR2.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 273");

		double sK = kR3.getInt("value");

		Statement tR = conn.createStatement();
		ResultSet tR1 = tR.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 270");

		int aT = tR1.getInt("value");

		Statement tR2 = conn.createStatement();
		ResultSet tR3 = tR2.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 274");

		double sT = tR3.getInt("value");

		Statement eR = conn.createStatement();
		ResultSet eR1 = eR.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 267");

		int aE = eR1.getInt("value");

		Statement eR2 = conn.createStatement();
		ResultSet eR3 = eR2.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 271");

		double sE = eR3.getInt("value");

		Statement mShi = conn.createStatement();
		ResultSet rShi = mShi.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 263");

		double shieldCap = rShi.getInt("value");

		Statement mArm = conn.createStatement();
		ResultSet rArm = mArm.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 265");

		double armCap = rArm.getInt("value");

		Statement mStrut = conn.createStatement();
		ResultSet rStrut = mStrut.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 9");

		double strutCap = rStrut.getInt("value");

		Statement scanR = conn.createStatement();
		ResultSet scanR1 = scanR.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 564");

		double scanTemp = 0;
		while(scanR1.next()) {

			scanTemp = scanR1.getInt("value");

		}

		for(int i = 0; i < 4; i++) {

			strutCap = strutCap + (strutCap * 0.30);

		}

		double t1 = 0;
		double t2 = 0;
		double t3 = 0;
		double t4 = 0;

		t1 = armCap * aE;
		t2 = armCap * aK;
		t3 = armCap * aT;
		t4 = armCap * aEx;

		t1 = armCap - t1;
		t2 = armCap - t2;
		t3 = armCap - t3;
		t4 = armCap - t4;

		armCap = armCap + t1 + t2 + t3 + t4;

		t1 = 0;
		t2 = 0;
		t3 = 0;
		t4 = 0;

		t1 = shieldCap * sE;
		t2 = shieldCap * sK;
		t3 = shieldCap * sT;
		t4 = shieldCap * sEx;

		t1 = shieldCap - t1;
		t2 = shieldCap - t2;
		t3 = shieldCap - t3;
		t4 = shieldCap - t4;

		shieldCap = shieldCap + t1 + t2 + t3 + t4;

		Statement maxVal = conn.createStatement();
		ResultSet maxRVal = maxVal.executeQuery("SELECT dgmTypeAttributes.valueInt AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeName = fittingTable.shipName AND " +
				"fittingTable.fitID = " + fid + " AND " +
				"dgmTypeAttributes.attributeID = 9");

		double velocity = maxRVal.getInt("value");

		fit temp = modCalc(fid);
		fit forDisplay = new fit();

		forDisplay.velocitM = velocity + temp.velocitM;
		forDisplay.armHp = armCap + temp.armHp;
		forDisplay.shiHp = shieldCap + temp.shiHp;
		forDisplay.strutHp = strutCap + temp.strutHp;
		forDisplay.scanRez = temp.scanRez;
		forDisplay.alpha = temp.alpha;
		forDisplay.DPS = temp.DPS;
		forDisplay.rep = temp.rep;

		System.out.println("Alpha Damage: " + forDisplay.alpha);
		//System.out.println("DPS: " + forDisplay.DPS);
		System.out.println("Shield EHP: " + forDisplay.shiHp);
		System.out.println("Armor EHP: " + forDisplay.armHp);
		System.out.println("Structure EHP: " + forDisplay.strutHp);
		System.out.println("Repair Ammount: " + forDisplay.rep);
		System.out.println("Max Velocity: " + forDisplay.velocitM);
		//System.out.println("Scan Rez: " + forDisplay.scanRez);

	}

	public static fit modCalc(int fid) throws SQLException{

		fit modFit = new fit();
		//System.out.println("In modCalc");
		Statement stat = conn.createStatement();
		ResultSet q1 = stat.executeQuery("SELECT * FROM fittingTable WHERE fitID = " + fid + " " );
		int i = 0;

		for(int j = 0; j < 9; j++) {

			int temp = q1.getInt("h" + (i + 1));
			i++;
			if(temp != -999) {

				if(temp == -888) {


				}

				Statement stat1 = conn.createStatement();
				ResultSet q2 = stat1.executeQuery("SELECT invTypes.typeID AS holder FROM invTypes WHERE invTypes.typeID == " + temp + " " );

				while(q2.next()) {

					fit tempf = new fit();
					int holder = q2.getInt("holder");
					tempf = eachMod(holder);
					modFit.velocitM += tempf.velocitM;
					modFit.armHp += tempf.armHp;
					modFit.shiHp += tempf.shiHp;
					modFit.strutHp += tempf.strutHp;
					modFit.DPS += tempf.DPS;
					modFit.alpha += tempf.alpha;
					modFit.rep += tempf.rep;

				}

				q2.close();
				stat1.close();

			}

		}

		int x = 0;
		for(int j = 0; j < 9; j++) {

			int temp = q1.getInt("m" + (x + 1));
			x++;
			if(temp != -998) {

				if(temp == -888) {

				}

				Statement stat2 = conn.createStatement();
				ResultSet q3 = stat2.executeQuery("SELECT invTypes.typeID AS holder FROM invTypes WHERE invTypes.typeID == " + temp + " ");

				while(q3.next()) {

					fit tempf = new fit();
					int holder = q3.getInt("holder");
					tempf = eachMod(holder);
					modFit.velocitM += tempf.velocitM;
					modFit.armHp += tempf.armHp;
					modFit.shiHp += tempf.shiHp;
					modFit.strutHp += tempf.strutHp;
					modFit.DPS += tempf.DPS;
					modFit.alpha += tempf.alpha;
					modFit.rep += tempf.rep;

				}

				q3.close();
				stat2.close();

			}

		}

		int y = 0;
		for(int j = 0; j < 9; j++) {

			int temp = q1.getInt("l" + (y + 1));
			y++;
			if(temp != -997) {

				if(temp == -888) {

				}

				Statement stat3 = conn.createStatement();
				ResultSet q4 = stat3.executeQuery("SELECT invTypes.typeID AS holder FROM invTypes WHERE invTypes.typeID == " + temp + " ");

				while(q4.next()) {

					fit tempf = new fit();
					int holder = q4.getInt("holder");
					tempf = eachMod(holder);
					modFit.velocitM += tempf.velocitM;
					modFit.armHp += tempf.armHp;
					modFit.shiHp += tempf.shiHp;
					modFit.strutHp += tempf.strutHp;
					modFit.DPS += tempf.DPS;
					modFit.alpha += tempf.alpha;
					modFit.rep += tempf.rep;

				}

				q4.close();
				stat3.close();

			}

		}

		int z = 0;
		for(int j = 0; j < 3; j++) {

			int temp = q1.getInt("r" + (z + 1));
			z++;
			if(temp != -996) {

				if(temp == -888) {

				}

				Statement stat4 = conn.createStatement();
				ResultSet q5 = stat4.executeQuery("SELECT invTypes.typeID AS holder FROM invTypes WHERE invTypes.typeID == " + temp + " ");

				while(q5.next()) {

					fit tempf = new fit();
					int holder = q5.getInt("holder");
					tempf = eachMod(holder);
					modFit.velocitM += tempf.velocitM;
					modFit.armHp += tempf.armHp;
					modFit.shiHp += tempf.shiHp;
					modFit.strutHp += tempf.strutHp;
					modFit.DPS += tempf.DPS;
					modFit.alpha += tempf.alpha;
					modFit.rep += tempf.rep;

				}

				q5.close();
				stat4.close();
			}

		}

		int temp = q1.getInt("a1");
		//System.out.println("thinks ammo slot says " + temp + "done");
		if(temp!= -995) {

			if(temp == -888) {

			}

			Statement gAmmo = conn.createStatement();
			ResultSet ammoG = gAmmo.executeQuery("SELECT invTypes.typeID AS holder FROM invTypes WHERE invTypes.typeID == " + temp + " ");

			while(ammoG.next()) {

				int holder = ammoG.getInt("holder");
			//System.out.println("thinks ammo name is " + holder + "done");
				double dpsCal = calcDamage(holder);
				modFit.alpha *= dpsCal;

			}

			ammoG.close();
			gAmmo.close();

		}

		return modFit;
	}

	public static fit eachMod(int typeID) throws SQLException{

		fit sfit = new fit();

		Statement exR = conn.createStatement();
		ResultSet exR1 = exR.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 268");

		double aEx = 0;
		while(exR1.next()) {

			aEx = exR1.getInt("value");

		}

		Statement exR2 = conn.createStatement();
		ResultSet exR3 = exR2.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 272");	

		double sEx = 0;
		while(exR3.next()) {

			sEx = exR3.getInt("value");
		}

		Statement kR = conn.createStatement();
		ResultSet kR1 = kR.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 269");

		double aK = 0;
		while(kR1.next()) {

			aK = kR1.getInt("value");

		}

		Statement kR2 = conn.createStatement();
		ResultSet kR3 = kR2.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 273");

		double sK = 0;
		while(kR3.next()) {

			sK = kR3.getInt("value");

		}

		Statement tR = conn.createStatement();
		ResultSet tR1 = tR.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 270");

		double aT = 0;
		while(tR1.next()) {

			aT = tR1.getInt("value");

		}

		Statement tR2 = conn.createStatement();
		ResultSet tR3 = tR2.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 274");

		double sT = 0;
		while(tR3.next()) {

			sT = tR3.getInt("value");

		}

		Statement eR = conn.createStatement();
		ResultSet eR1 = eR.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 267");

		int aE = 0;
		while(eR1.next()) {

			aE = eR1.getInt("value");

		}

		Statement eR2 = conn.createStatement();
		ResultSet eR3 = eR2.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 271");

		double sE = 0;
		while(eR3.next()) {

			sE = eR3.getInt("value");

		}

		Statement mShi = conn.createStatement();
		ResultSet rShi = mShi.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 72");

		double shieldCap = 0;
		while(rShi.next()) {

			shieldCap = rShi.getInt("value");

		}

		Statement mArm = conn.createStatement();
		ResultSet rArm = mArm.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 1159");

		double armCap = 0;
		while(rArm.next()) {

			armCap = rArm.getInt("value");

		}

		Statement mStrut = conn.createStatement();
		ResultSet rStrut = mStrut.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 9");

		double strutCap = 0;
		while(rStrut.next()) {

			strutCap = rStrut.getInt("value");

		}

		Statement scanR = conn.createStatement();
		ResultSet scanR1 = scanR.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 566");

		double scanTemp = 0;
		while(scanR1.next()) {

			scanTemp = scanR1.getInt("value");

		}
		
		Statement repR = conn.createStatement();
		ResultSet repR1 = repR.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 1230");
		
		double repAm = 0;
		while(repR1.next()) {
			
			repAm = repR1.getInt("value");
			
		}

		Statement dpsC = conn.createStatement();
		ResultSet dpsC1 = dpsC.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 51");

		double dpsTemp = 0;
		while(dpsC1.next()) {

			dpsTemp = dpsC1.getInt("value");

		}

		Statement dmgMod = conn.createStatement();
		ResultSet dmgMod1 = dmgMod.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 64");

		double dmgTemp = 0;
		while(dmgMod1.next()) {

			dmgTemp = dmgMod1.getInt("value");

		}

		double dmg = calcDamage(typeID);
		dmg = dmg * dmgTemp;
		//System.out.println(dmg);

		if(dpsTemp != 0) {
			
			dpsTemp = 60000/dpsTemp;
			dpsTemp = dpsTemp * dmg;
			
		}
		
		for(int i = 0; i < 4; i++) {

			strutCap = strutCap + (strutCap * 0.30);

		}

		double t1 = 0;
		double t2 = 0;
		double t3 = 0;
		double t4 = 0;

		t1 = armCap * aE;
		t2 = armCap * aK;
		t3 = armCap * aT;
		t4 = armCap * aEx;

		t1 = armCap - t1;
		t2 = armCap - t2;
		t3 = armCap - t3;
		t4 = armCap - t4;

		armCap = armCap + t1 + t2 + t3 + t4;

		t1 = 0;
		t2 = 0;
		t3 = 0;
		t4 = 0;

		t1 = shieldCap * sE;
		t2 = shieldCap * sK;
		t3 = shieldCap * sT;
		t4 = shieldCap * sEx;

		t1 = shieldCap - t1;
		t2 = shieldCap - t2;
		t3 = shieldCap - t3;
		t4 = shieldCap - t4;

		shieldCap = shieldCap + t1 + t2 + t3 + t4;

		Statement maxVal = conn.createStatement();
		ResultSet maxRVal = maxVal.executeQuery("SELECT dgmTypeAttributes.valueFloat AS value " + 
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes, fittingTable " + 
				"WHERE " + 
				"invTypes.typeID = dgmTypeAttributes.typeID AND " + 
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " + 
				"invTypes.typeID = " + typeID + " AND " +
				"dgmTypeAttributes.attributeID = 20");

		double velocity = 0;
		while(maxRVal.next()) {

			velocity = maxRVal.getInt("value");	

		}
		
		sfit.velocitM = velocity;
		sfit.armHp = armCap;
		sfit.shiHp = shieldCap;
		sfit.strutHp = strutCap;
		sfit.scanRez = scanTemp;
		sfit.alpha = dmg;
		sfit.DPS = dpsTemp;
		sfit.rep = repAm;
		
		return sfit;

	}

	public static double calcDamage(int typeID) throws SQLException{

		double finalD = 0;

		Statement checkIfAmmo = conn.createStatement();
		ResultSet res = checkIfAmmo.executeQuery("SELECT invTypes.typeName AS holder " +
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " +
				"WHERE " +
				"invTypes.typeID = dgmTypeAttributes.typeID AND " +
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " +
				"dgmAttributeTypes.displayName = 'Charge size' AND " +
				"invTypes.typeID = " + typeID + " AND " +
				"invTypes.groupID IN (86, 85) ");

		int iter = 0;
		while(res.next()) {

			iter++;
			//System.out.println("Checking if " + res.getString("holder") + " is ammo");

		}

		if(iter == 0) {

			finalD = 1;
			return finalD;

		}

		Statement getEM = conn.createStatement();
		ResultSet gotEM = getEM.executeQuery("SELECT dgmTypeAttributes.valueFloat AS holder " +
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " +
				"WHERE " +
				"invTypes.typeID = dgmTypeAttributes.typeID AND " +
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " +
				"dgmTypeAttributes.attributeID = 114 AND " +
				"invTypes.typeID = " + typeID + " AND " +
				"invTypes.groupID IN (86, 85) ");

		int finalEM = 0;
		while(gotEM.next()) {

			finalEM = gotEM.getInt("holder");

		}

		Statement getT = conn.createStatement();
		ResultSet gotT = getT.executeQuery("SELECT dgmTypeAttributes.valueFloat AS holder " +
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " +
				"WHERE " +
				"invTypes.typeID = dgmTypeAttributes.typeID AND " +
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " +
				"dgmTypeAttributes.attributeID = 118 AND " +
				"invTypes.typeID = " + typeID + " AND " +
				"invTypes.groupID IN (86, 85) ");

		int finalT = 0;
		while(gotT.next()) {

			finalT = gotT.getInt("holder");

		}

		Statement getK = conn.createStatement();
		ResultSet gotK = getK.executeQuery("SELECT dgmTypeAttributes.valueFloat AS holder " +
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " +
				"WHERE " +
				"invTypes.typeID = dgmTypeAttributes.typeID AND " +
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " +
				"dgmTypeAttributes.attributeID = 117 AND " +
				"invTypes.typeID = " + typeID + " AND " +
				"invTypes.groupID IN (86, 85) ");

		int finalK = 0;
		while(gotK.next()) {

			finalK = gotK.getInt("holder");

		}

		Statement getEx = conn.createStatement();
		ResultSet gotEx = getEx.executeQuery("SELECT dgmTypeAttributes.valueFloat AS holder " +
				"FROM invTypes, dgmAttributeTypes, dgmTypeAttributes " +
				"WHERE " +
				"invTypes.typeID = dgmTypeAttributes.typeID AND " +
				"dgmTypeAttributes.attributeID = dgmAttributeTypes.attributeID AND " +
				"dgmTypeAttributes.attributeID = 116 AND " +
				"invTypes.typeID = " + typeID + " AND " +
				"invTypes.groupID IN (86, 85) ");

		int finalEx = 0;
		while(gotEx.next()) {

			finalEx = gotEx.getInt("holder");

		}

		finalD = finalEx + finalEM + finalT + finalK;
	/*	System.out.println("Raw Damage Nums");
		System.out.println(finalEx);
		System.out.println(finalEM);
		System.out.println(finalT);
		System.out.println(finalK);*/
		return finalD;

	}

	public static void exitFromDB() throws SQLException{

		conn.close();

	}

	//use once
	public static void createFitTable() throws SQLException{

		Statement stat = conn.createStatement();
		stat.executeUpdate("DROP TABLE IF EXISTS fittingTable");
		stat.executeUpdate("CREATE TABLE fittingTable ( " +
				"fitID DECIMAL(100) NOT NULL, " +
				"fitName VARCHAR(100) NOT NULL, " +
				"shipName VARCHAR(100) NOT NULL, " + 
				"h1 DECIMAL(100) NOT NULL, " +
				"h2 DECIMAL(100) NOT NULL, " +
				"h3 DECIMAL(100) NOT NULL, " +
				"h4 DECIMAL(100) NOT NULL, " +
				"h5 DECIMAL(100) NOT NULL, " +
				"h6 DECIMAL(100) NOT NULL, " +
				"h7 DECIMAL(100) NOT NULL, " +
				"h8 DECIMAL(100) NOT NULL, " +
				"h9 DECIMAL(100) NOT NULL, " +
				"m1 DECIMAL(100) NOT NULL, " +
				"m2 DECIMAL(100) NOT NULL, " +
				"m3 DECIMAL(100) NOT NULL, " + 
				"m4 DECIMAL(100) NOT NULL, " +
				"m5 DECIMAL(100) NOT NULL, " + 
				"m6 DECIMAL(100) NOT NULL, " +
				"m7 DECIMAL(100) NOT NULL, " +
				"m8 DECIMAL(100) NOT NULL, " +
				"m9 DECIMAL(100) NOT NULL, " +
				"l1 DECIMAL(100) NOT NULL, " +
				"l2 DECIMAL(100) NOT NULL, " +
				"l3 DECIMAL(100) NOT NULL, " +
				"l4 DECIMAL(100) NOT NULL, " +
				"l5 DECIMAL(100) NOT NULL, " +
				"l6 DECIMAL(100) NOT NULL, " +
				"l7 DECIMAL(100) NOT NULL, " +
				"l8 DECIMAL(100) NOT NULL, " +
				"l9 DECIMAL(100) NOT NULL, " +
				"r1 DECIMAL(100) NOT NULL, " +
				"r2 DECIMAL(100) NOT NULL, " +
				"r3 DECIMAL(100) NOT NULL, " +
				"d1 DECIMAL(100) NOT NULL, " +
				"d2 DECIMAL(100) NOT NULL, " +
				"d3 DECIMAL(100) NOT NULL, " +
				"d4 DECIMAL(100) NOT NULL, " + 
				"d5 DECIMAL(100) NOT NULL, " +
				"a1 DECIMAL(100) NOT NULL )");

		System.out.println("Created Table");
		/*for(int i = 0; i < 20; i++) {

			Statement stat1 = conn.createStatement();
			stat1.executeUpdate("INSERT INTO fittingTable VALUES(" + i  + "," +
					"-999, -999, -999, -999, -999, -999, -999, -999, 999" +
					"-998, -998, -998, -998, -998, -998, -998, -998, 998" +
					"-997, -997, -997, -997, -997, -997, -997, -997, 997" +
					"-996, -996, -996" +
					"-995, -995, -995, -995, -995");


		}*/

	}

}