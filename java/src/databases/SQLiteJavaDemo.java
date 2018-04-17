package databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class SQLiteJavaDemo {

	public static void main(String[] args) throws ClassNotFoundException {

		// Using SQLite or MariaDB?
		final boolean USING_SQLITE = true;
		final String PARAM;
		if (USING_SQLITE) {
			PARAM = "path to chinook sqlite file";
		} else {
			PARAM = "host/Chinook?user=root";
		}

		if (args.length != 1) {
			System.out.printf("Usage: java %s <%s>", SQLiteJavaDemo.class.getCanonicalName(), PARAM);
			System.exit(0);
		}

		// load the sqlite-JDBC driver using the current class loader
		// make the connection URI, based on DBMS
		final String CONN_URI;
		if (USING_SQLITE) {
			Class.forName("org.sqlite.JDBC");
			CONN_URI = ("jdbc:sqlite:" + args[0]);
		} else {
			Class.forName("org.mariadb.jdbc.Driver");
			CONN_URI = ("jdbc:mariadb://" + args[0]);
		}

		// using try-with-resource automatically closes
		// connection to the database (and input scanner)
		try (final Connection connection = DriverManager.getConnection(CONN_URI);
				final Scanner input = new Scanner(System.in); ) {
			
			int i = 1;

			while(i != 0) {
			
				// get query
				System.out.printf("Which query? (1-10 task, 11-15 complex, 0 to exit): ");
				@SuppressWarnings("resource")
				Scanner num = new Scanner(System.in);
				i = num.nextInt();
				
		        switch (i) {
		        
		        case 0: 
		        		System.out.printf("%nThanks for using QwikTix, Goodbye.%n%n");
		        		break;
		        
		        case 1:  
		        		System.out.printf("%nRegister a new user!%n");
		        	
		    			// get input(s)
		    			System.out.printf("%nEnter your email: ");
		    			final String param11 = input.nextLine();
		    			System.out.printf("%nEnter your password: ");
		    			final String param21 = input.nextLine();
		    			System.out.printf("%nEnter your first name: ");
		    			final String param31 = input.nextLine();
		    			System.out.printf("%nEnter your last name: ");
		    			final String param41 = input.nextLine();
		    			System.out.printf("%nUpload a profile photo: ");
		    			final String param51 = input.nextLine();
		    			System.out.printf("%nEnter your phone number: ");
		    			final String param61 = input.nextLine();
		    			System.out.printf("%nEnter your street address: ");
		    			final String param71 = input.nextLine();
		    			System.out.printf("%nEnter your city: ");
		    			final String param81 = input.nextLine();
		    			System.out.printf("%nEnter your postal code: ");
		    			final String param91 = input.nextLine();
		    			System.out.printf("%nEnter your country: ");
		    			final String param101 = input.nextLine();
		    			
		    			// Hash a password for the first time
		    			// String hashed = BCrypt.hashpw(param21, BCrypt.gensalt());

		    			// gensalt's log_rounds parameter determines the complexity
		    			// the work factor is 2**log_rounds, and the default is 10
		    			 String hashed = BCrypt.hashpw(param21, BCrypt.gensalt(12));

		    			// Check that an unencrypted password matches one that has
		    			// previously been hashed
		    			if (BCrypt.checkpw(param21, hashed))
		    				System.out.println("Password encrypted");
		    			else
		    				System.out.println("Password not encrypted");
	
		    			// generate parameterized sql
		    			final String sql1;
	
		    				sql1 = "INSERT INTO USER(Email,Pword,Fname,Lname,Photo,Phone,Street,City,Postal,Country) " +
		    					   "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		    			System.out.printf("%nSQL: %s%n%n", sql1);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql1)) {
	
		    				// bind parameter(s)
		    					stmt.setString( 1, param11 );
		    					stmt.setString( 2, hashed );
		    					stmt.setString( 3, param31 );
		    					stmt.setString( 4, param41 );
		    					stmt.setString( 5, param51 );
		    					stmt.setString( 6, param61 );
		    					stmt.setString( 7, param71 );
		    					stmt.setString( 8, param81 );
		    					stmt.setString( 9, param91 );
		    					stmt.setString( 10, param101 );
	
		    				// get results
		    					stmt.executeUpdate();
		    					
		    			}
		    			System.out.printf("User added.%n%n");
		                break;
		        
		        case 2:  
		        		System.out.printf("%nRecord that a user loves a movie!%n");
		        		
		    			// get input(s)
		    			System.out.printf("%nWhich user ID (int)?: ");
		    			final Integer param22 = Integer.parseInt(input.nextLine());
		    			System.out.printf("%nWhich movie ID (int)?: ");
		    			final Integer param23 = Integer.parseInt(input.nextLine());
	
		    			// generate parameterized sql
		    			
		    			final String sql2;
	
		    				sql2 = "INSERT INTO LOVE(UID, MID) " + 
		    					   "VALUES (?, ?)";

		    			System.out.printf("%nSQL: %s%n%n", sql2);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql2)) {
	
		    				// bind parameter(s)
		    					stmt.setInt( 1, param22 );
		    					stmt.setInt( 2, param23 );
	
		    				// get results
		    					stmt.executeUpdate();
		    					
		    			}
		    			System.out.printf("Love added.%n%n");
		                break;

		        case 3:  
		        		System.out.printf("%nOrder a ticket from a local theatre!%n");
		        		
		    			// get input(s)
			        	System.out.printf("%nWhich user ID (int)?: ");
			        	final Integer param32 = Integer.parseInt(input.nextLine());
		    			System.out.printf("%nWhich movie ID (int)?: ");
		    			final Integer param33 = Integer.parseInt(input.nextLine());
		    			System.out.printf("%nWhich vendor ID (int)?: ");
		    			final Integer param34 = Integer.parseInt(input.nextLine());
		    			// System.out.printf("%nTimestamp: ");
		    			// final Integer param35 = Integer.parseInt(input.nextLine());
		    			System.out.printf("%nTotal cost: ");
		    			final Float param36 = Float.parseFloat(input.nextLine());
		    			System.out.printf("%nQuantitiy of tickets: ");
		    			final Integer param37 = Integer.parseInt(input.nextLine());
		    			// System.out.printf("%nDay/time of showing (10-digit timestamp): ");
		    			// final Integer param38 = Integer.parseInt(input.nextLine());
		    		
		    			// generate parameterized sql
		    			
		    			final String sql3;
	
		    				sql3 =  "INSERT INTO ORDERS(UID, MID, VID, TIMESTAMP, COST, QUANTITY, SHOWING) " +
		    						"VALUES (?, ?, ?, strftime('%s', 'now'), ?, ?, strftime('%s', 'now', '+1 day'))";

		    			System.out.printf("%nSQL: %s%n%n", sql3);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql3)) {
	
		    				// bind parameter(s)
		    					stmt.setInt( 1, param32 );
		    					stmt.setInt( 2, param33 );
		    					stmt.setInt( 3, param34 );
		    					stmt.setFloat( 4, param36 );
		    					stmt.setInt( 5, param37 );
	
		    				// get results
		    					stmt.executeUpdate();
		    					
		    			}
		    			System.out.printf("Order added.%n%n");
		                break;

		        case 4:  
		        		System.out.printf("%nCredit an existing actress for a movie!%n");
		        	
		    			// get input(s)
		    			System.out.printf("%nWhich movie ID (int)?: ");
		    			final Integer param42 = Integer.parseInt(input.nextLine());
		    			System.out.printf("%nWhich person ID (int)?: ");
		    			final Integer param43 = Integer.parseInt(input.nextLine());
		    			// System.out.printf("%nWhich role ID?: ");
		    			// final Integer param44 = Integer.parseInt(input.nextLine());
		    			System.out.printf("%nUpload character photo: ");
		    			final String param45 = input.nextLine();
		    			System.out.printf("%nUpload character name: ");
		    			final String param46 = input.nextLine();
	
		    			// generate parameterized sql
		    			// ROLE = 1 because question specified actress/actor
		    			
		    			final String sql4;
	
		    				sql4 = "INSERT INTO CREDIT(MID, PID, ROLE, PHOTO, CHARACTER) " +
		    					   "VALUES (?, ?, 1, ?, ?)";

		    			System.out.printf("%nSQL: %s%n%n", sql4);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql4)) {
	
		    				// bind parameter(s)
		    					stmt.setInt( 1, param42 );
		    					stmt.setInt( 2, param43 );
		    					stmt.setString( 3, param45 );
		    					stmt.setString( 4, param46 );
	
		    				// get results
		    					stmt.executeUpdate();
		    					
		    			}
		    			System.out.printf("Credit added.%n%n");
		                break;
		                
		        case 5:  
		        		System.out.printf("%nProvide a ranked list of revenue generated from the top-10 studios!%n");
		        	
		    			// no inputs for this query
	
		    			// generate parameterized sql
		    			final String sql5;
	
		    				sql5 = "SELECT STUDIO.Name AS Studio, PRINTF('$%.2f', SUM(Orders.Cost)) AS Revenue " +
		    					  "FROM (ORDERS INNER JOIN MOVIE ON ORDERS.MID=MOVIE.MID) " +
		    					  "INNER JOIN STUDIO ON MOVIE.Studio=STUDIO.SID " +
		    					  "GROUP BY MOVIE.Studio " +
		    					  "ORDER BY TOTAL(ORDERS.Cost) DESC " +
		    					  "LIMIT 10 ";
		
		    			System.out.printf("%nSQL: %s%n%n", sql5);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql5)) {
		    				
		    				// no parameters to bind in this query
	
		    				// get results
		    				try (final ResultSet res = stmt.executeQuery()) {
		    					while (res.next()) {
		    						System.out.printf("%s: %s%n", res.getString("Studio"), res.getString("Revenue"));
		    					}
		    				}
		    			}
		    			System.out.printf("%n");
		                break;
		                
		        case 6: 
		        		System.out.printf("%nFind all movies directed by a person (supplied via last name)!%n");
		        	
		        	    // get input(s)
		    			System.out.printf("%nEnter a director's last name: ");
		    			final String param6 = input.nextLine();
	
		    			// generate parameterized sql
		    			final String sql6;
	
		    				sql6 = "SELECT (PERSON.Fname || ' ' || PERSON.Lname) AS Director, MOVIE.Name AS Movie " +
		    						"FROM ((MOVIE INNER JOIN CREDIT ON MOVIE.MID=CREDIT.MID) " +
		    						"INNER JOIN PERSON ON CREDIT.PID=PERSON.PID) " +
		    						"INNER JOIN ROLE ON CREDIT.Role=ROLE.RID " +
		    						"WHERE PERSON.Lname LIKE ? AND ROLE.Name == 'Director' " +
		    						"ORDER BY MOVIE.Name";
	
			    			System.out.printf("%nSQL: %s%n%n", sql6);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql6)) {
	
		    				// bind parameter(s)
		    					stmt.setString( 1, param6 );
	
		    				// get results
		    				try (final ResultSet res = stmt.executeQuery()) {
		    					while (res.next()) {
		    						System.out.printf("%s: %s%n", res.getString("Director"), res.getString("Movie"));
		    					}
		    				}
		    			}
		    			System.out.printf("%n");
		                break;
	            
		        case 7: 
		        		System.out.printf("%nLoad the cover images and names of movies ordered by a particular user!%n");
			        	
		    			// get input(s)
		    			System.out.printf("%nEnter a user ID (int): ");
		    			final String param7 = input.nextLine();
	
		    			// generate parameterized sql
		    			final String sql7;
	
		    				sql7 = "SELECT USER.UID AS User, MOVIE.Name AS Movie, IMAGE.Image AS Cover_image " +
		    						"FROM ((USER INNER JOIN ORDERS ON USER.UID=ORDERS.UID) " +
		    						"INNER JOIN MOVIE ON ORDERS.MID=MOVIE.MID) " +
		    						"INNER JOIN IMAGE ON MOVIE.MID=IMAGE.MID " +
		    						"WHERE USER.UID == ? " +
		    						"ORDER BY MOVIE.Name";
		
			    			System.out.printf("%nSQL: %s%n%n", sql7);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql7)) {
	
		    				// bind parameter(s)
		    					stmt.setString( 1, param7 );
	
		    				// get results
		    				try (final ResultSet res = stmt.executeQuery()) {
		    					while (res.next()) {
		    						System.out.printf("User %s: %s, %s%n", res.getString("User"), res.getString("Movie"), res.getString("Cover_image"));
		    					}
		    				}
		    			}
		    			System.out.printf("%n");
		                break;
		        
		        case 8: 
		        		System.out.printf("%nFind all movies released this year that a user loves but has not ordered!%n");	
		        	
		    			// get input(s)
		    			System.out.printf("%nEnter a user ID (int): ");
		    			final String param8 = input.nextLine();
	
		    			// generate parameterized sql
		    			final String sql8;
	
		    				sql8 =  "SELECT USER.UID AS User, MOVIE.Name AS Movie, strftime('%m-%d-%Y', datetime(Rdate, 'unixepoch')) AS Release_date " +
		    						"FROM (USER INNER JOIN LOVE ON USER.UID=LOVE.UID) " +
		    						"INNER JOIN MOVIE ON LOVE.MID=MOVIE.MID " +
		    						"WHERE strftime('%Y', datetime(Rdate, 'unixepoch')) == strftime('%Y', 'now') AND USER.UID == ? " +
		    						"EXCEPT " +
		    						"SELECT USER.UID AS User, MOVIE.Name AS Movie, strftime('%m-%d-%Y', datetime(Rdate, 'unixepoch')) AS Release_date " +
		    						"FROM (USER INNER JOIN ORDERS ON USER.UID=ORDERS.UID) " +
		    						"INNER JOIN MOVIE ON ORDERS.MID=MOVIE.MID " +
		    						"WHERE strftime('%Y', datetime(Rdate, 'unixepoch')) == strftime('%Y', 'now') AND USER.UID == ? " +
		    						"ORDER BY MOVIE.Name";

	
			    			System.out.printf("%nSQL: %s%n%n", sql8);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql8)) {
	
		    				// bind parameter(s)
		    					stmt.setString( 1, param8 );
	
		    				// get results
		    				try (final ResultSet res = stmt.executeQuery()) {
		    					while (res.next()) {
		    						System.out.printf("User %s: %s (Released %s)%n", res.getString("User"), res.getString("Movie"), res.getString("Release_date"));
		    					}
		    				}
		    			}
		    			System.out.printf("%n");
		                break;

		        case 9: 
		        		System.out.printf("%nFind all people (name, picture, and role) credited for a particular movie (supplied by name)!%n");	
		        	
		    			// get input(s)
		    			System.out.printf("%nEnter a movie name: ");
		    			final String param9 = input.nextLine();
	
		    			// generate parameterized sql
		    			final String sql9;
	
		    				sql9 =  "SELECT (PERSON.Fname || ' ' || PERSON.Lname) AS Name, PERSON.Photo AS Picture, ROLE.Name AS Role, MOVIE.Name AS Movie " +
		    						"FROM ((PERSON INNER JOIN CREDIT ON PERSON.PID=CREDIT.PID) " +
		    						"INNER JOIN MOVIE ON CREDIT.MID=MOVIE.MID) " +
		    						"INNER JOIN ROLE ON CREDIT.ROLE=ROLE.RID " +
		    						"WHERE MOVIE.Name LIKE ? " +
		    						"ORDER BY Name";

			    			System.out.printf("%nSQL: %s%n%n", sql9);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql9)) {
	
		    				// bind parameter(s)
		    					stmt.setString( 1, param9 );
	
		    				// get results
		    				try (final ResultSet res = stmt.executeQuery()) {
		    					while (res.next()) {
		    						System.out.printf("%s: %s, %s, %s%n", res.getString("Movie"), res.getString("Name"), res.getString("Role"), res.getString("Picture"));
		    					}
		    				}
		    			}
		    			System.out.printf("%n");
		                break;
		                
		        case 10:  
		        		System.out.printf("%nProvide a ranked list of revenue generated from the top-3 movie genres!%n");			        	
		        	
		    			// no inputs for this query
	
		    			// generate parameterized sql
		    			final String sql10;
	
		    				sql10 = "SELECT Genre.Name AS Genre, PRINTF('$%.2f', SUM(Orders.Cost)) AS Revenue " +
		    						"FROM (ORDERS INNER JOIN MOVIE ON ORDERS.MID=MOVIE.MID) " +
		    						"INNER JOIN GENRE ON MOVIE.Genre=GENRE.GID " +
		    						"GROUP BY MOVIE.Genre " +
		    						"ORDER BY TOTAL(ORDERS.Cost) DESC " +
		    						"LIMIT 3";
	
			    			System.out.printf("%nSQL: %s%n%n", sql10);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql10)) {
		    				
		    				// no parameters to bind in this query
	
		    				// get results
		    				try (final ResultSet res = stmt.executeQuery()) {
		    					while (res.next()) {
		    						System.out.printf("%s: %s%n", res.getString("Genre"), res.getString("Revenue"));
		    					}
		    				}
		    			}
		    			System.out.printf("%n");
		                break;
		                
		        case 11:
			        	System.out.printf("%nFind all people who ordered a particular movie (supplied by name) within 24 hours of the movie’s release date!%n");	
			        	
		    			// get input(s)
		    			System.out.printf("%nEnter a movie name: ");
		    			final String param50 = input.nextLine();
	
		    			// generate parameterized sql
		    			final String sql11;
	
		    				sql11 = "SELECT USER.UID AS User, ORDERS.Confirmation AS OrderNum, MOVIE.Name as Movie, VENDOR.Name AS Vendor, " +
		    						"strftime('%m-%d-%Y',MOVIE.Rdate, 'unixepoch') AS ReleaseDate, " +
		    						"(ORDERS.Timestamp - MOVIE.Rdate)/3600 AS hoursAfterRelease, " +
		    						"(ORDERS.Timestamp - MOVIE.Rdate)%3600/60 AS minsAfterRelease " +
		    						"FROM ((MOVIE INNER JOIN ORDERS ON MOVIE.MID=ORDERS.MID) " +
		    						"INNER JOIN USER ON USER.UID=ORDERS.UID) " +
		    						"INNER JOIN VENDOR ON ORDERS.VID=VENDOR.VID " +
		    						"WHERE MOVIE.Name LIKE ? " +
		    						"AND (ORDERS.Timestamp - MOVIE.Rdate) <= 86400 " +
		    						"ORDER BY ORDERS.Timestamp, User";

	
			    			System.out.printf("%nSQL: %s%n%n", sql11);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql11)) {
	
		    				// bind parameter(s)
		    					stmt.setString( 1, param50 );
	
		    				// get results
		    				try (final ResultSet res = stmt.executeQuery()) {
		    					while (res.next()) {
		    						System.out.printf("%s: User %s, ordered %s hours, %s minutes after movie release from %s%n", res.getString("Movie"), res.getString("User"), res.getString("hoursAfterRelease"), res.getString("minsAfterRelease"), res.getString("Vendor"));
		    					}
		    				}
		    			}
		    			System.out.printf("%n");
		                break;
		        	
		        case 12:
			        	System.out.printf("%nProvide a ranked list of the top 3 directors in a genre (supplied by name) based on the number of orders that their movies in that genre receive!%n");	
			        	
		    			// get input(s)
		    			System.out.printf("%nEnter a genre name: ");
		    			final String param60 = input.nextLine();
	
		    			// generate parameterized sql
		    			final String sql12;
	
		    				sql12 = "SELECT (PERSON.Fname || ' ' || PERSON.Lname) AS Director, GENRE.Name as Genre, COUNT(ORDERS.Confirmation) AS OrderCount " +
		    						"FROM ((((CREDIT INNER JOIN MOVIE ON CREDIT.MID=MOVIE.MID) " +
		    						"INNER JOIN PERSON ON CREDIT.PID=PERSON.PID) " +
		    						"INNER JOIN ORDERS ON MOVIE.MID=ORDERS.MID) " +
		    						"INNER JOIN ROLE ON CREDIT.Role=ROLE.RID) " +
		    						"INNER JOIN GENRE ON MOVIE.Genre = GENRE.GID " +
		    						"WHERE ROLE.Name == 'Director' AND GENRE.Name LIKE ? " +
		    						"GROUP BY Director " +
		    						"ORDER BY OrderCount DESC, Director " +
		    						"LIMIT 3";
	
			    			System.out.printf("%nSQL: %s%n%n", sql12);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql12)) {
	
		    				// bind parameter(s)
		    					stmt.setString( 1, param60 );
	
		    				// get results
		    				try (final ResultSet res = stmt.executeQuery()) {
		    					while (res.next()) {
		    						System.out.printf("%s: %s, %s orders%n", res.getString("Genre"), res.getString("Director"), res.getString("OrderCount"));
		    					}
		    				}
		    			}
		    			System.out.printf("%n");
		                break;
		        	
		        case 13:
			        	System.out.printf("%nProvide the most popular actors in a given country (supplied by name)!%n");	
			        	
		    			// get input(s)
		    			System.out.printf("%nEnter a country name: ");
		    			final String param200 = input.nextLine();
	
		    			// generate parameterized sql
		    			final String sql13;
	
		    				sql13 = "SELECT (PERSON.Fname || ' ' || PERSON.Lname) AS Actor, COUNT(*) AS Appearances, USER.Country AS Country " +
		    						"FROM ((((MOVIE INNER JOIN ORDERS ON MOVIE.MID=ORDERS.MID) " +
		    						"INNER JOIN USER ON ORDERS.UID=USER.UID) " +
		    						"LEFT JOIN CREDIT ON MOVIE.MID=CREDIT.MID) " +
		    						"INNER JOIN PERSON ON CREDIT.PID=PERSON.PID) " +
		    						"INNER JOIN ROLE ON CREDIT.Role=ROLE.RID " +
		    						"WHERE USER.Country LIKE ? " +
		    						"AND ROLE.Name == 'Actor' " +
		    						"GROUP BY CREDIT.PID " +
		    						"ORDER BY Appearances DESC, Actor " +
		    						"LIMIT 3";

			    			System.out.printf("%nSQL: %s%n%n", sql13);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql13)) {
	
		    				// bind parameter(s)
		    					stmt.setString( 1, param200 );
	
		    				// get results
		    				try (final ResultSet res = stmt.executeQuery()) {
		    					while (res.next()) {
		    						System.out.printf("%s: %s, %s orders%n", res.getString("Country"), res.getString("Actor"), res.getString("Appearances"));
		    					}
		    				}
		    			}
		    			System.out.printf("%n");
		                break;
		        	
		        case 14:
			        	System.out.printf("%nProvide a list of suggested movies for a user (supplied by ID) based on their most recently ordered and loved movie!%n");	
			        	
		    			// get input(s)
		    			System.out.printf("%nEnter a user ID (int): ");
		    			final String param201 = input.nextLine();
	
		    			// generate parameterized sql
		    			final String sql14;
	
		    				sql14 = "SELECT DISTINCT MOVIE.Name AS Movie, GENRE.Name AS Genre, (PERSON.Fname || ' ' || PERSON.Lname) AS Director " +
		    						"FROM ((MOVIE INNER JOIN GENRE ON MOVIE.Genre=GENRE.GID) " +
		    						"INNER JOIN CREDIT ON CREDIT.MID=MOVIE.MID) " +
		    						"INNER JOIN PERSON ON CREDIT.PID=PERSON.PID " +
		    						"WHERE GENRE.GID IN ( " +
		    						"SELECT q1.GID " +
		    						"FROM " +
		    						"( " +
		    						"SELECT LOVE.MID, MAX(ORDERS.Timestamp), GENRE.GID " +
		    						"FROM ((((LOVE INNER JOIN USER ON LOVE.UID = USER.UID) " +
		    						"INNER JOIN ORDERS ON USER.UID=ORDERS.UID) " +
		    						"INNER JOIN MOVIE ON ORDERS.MID=MOVIE.MID) " +
		    						"INNER JOIN GENRE ON GENRE.GID=MOVIE.Genre) " +
		    						"LEFT JOIN CREDIT ON MOVIE.MID=CREDIT.MID " +
		    						"WHERE USER.UID = ? " +
		    						") q1 " +
		    						") " +
		    						"AND CREDIT.PID IN ( " +
		    						"SELECT q2.PID " +
		    						"FROM " +
		    						"( " +
		    						"SELECT LOVE.MID, MAX(ORDERS.Timestamp), CREDIT.PID " +
		    						"FROM (((((LOVE INNER JOIN USER ON LOVE.UID = USER.UID) " +
		    						"INNER JOIN ORDERS ON USER.UID=ORDERS.UID) " +
		    						"INNER JOIN MOVIE ON ORDERS.MID=MOVIE.MID) " +
		    						"INNER JOIN GENRE ON GENRE.GID=MOVIE.Genre) " +
		    						"LEFT JOIN CREDIT ON MOVIE.MID=CREDIT.MID) " +
		    						"INNER JOIN ROLE ON ROLE.RID=CREDIT.Role " +
		    						"WHERE USER.UID = ? AND ROLE.Name == 'Director' " +
		    						") q2 " +
		    						") ";
	
			    			System.out.printf("%nSQL: %s%n%n", sql14);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql14)) {
	
		    				// bind parameter(s)
		    					stmt.setString( 1, param201 );
		    					stmt.setString( 2, param201 );
	
		    				// get results
		    				try (final ResultSet res = stmt.executeQuery()) {
		    					while (res.next()) {
		    						System.out.printf("%s: %s, %s%n", res.getString("Movie"), res.getString("Genre"), res.getString("Director"));
		    					}
		    				}
		    			}
		    			System.out.printf("%n");
		                break;
		        	
		        case 15:
			        	System.out.printf("%nA ranked list of directors whose movies are still being ordered 10 years after their release date!%n");			        	
			        	
		    			// no inputs for this query
	
		    			// generate parameterized sql
		    			final String sql15;
	
		    				sql15 = "SELECT MOVIE.Name AS Movie, (PERSON.Fname || ' ' || PERSON.Lname) AS Director,  COUNT(ORDERS.Confirmation) AS Orders_After_10_Years " +
		    						"FROM " +
		    						"((ORDERS INNER JOIN MOVIE ON ORDERS.MID = MOVIE.MID) " +
		    						"INNER JOIN CREDIT ON MOVIE.MID = CREDIT.MID) " +
		    						"INNER JOIN PERSON ON CREDIT.PID = PERSON.PID " +
		    						"INNER JOIN ROLE ON CREDIT.Role = ROLE.RID " +
		    						"WHERE " +
		    						"ORDERS.Timestamp >= MOVIE.Rdate + 315400000 " +
		    						"AND ROLE.Name == 'Director' " +
		    						"ORDER BY Orders_After_10_Years DESC, Director";

	
			    			System.out.printf("%nSQL: %s%n%n", sql15);
	
		    			// prepare statement
		    			try (final PreparedStatement stmt = connection.prepareStatement(sql15)) {
		    				
		    				// no parameters to bind in this query
	
		    				// get results
		    				try (final ResultSet res = stmt.executeQuery()) {
		    					while (res.next()) {
		    						System.out.printf("%s: %s orders for %s%n", res.getString("Director"), res.getString("Orders_After_10_Years"), res.getString("Movie"));
		    					}
		    				}
		    			}
		    			System.out.printf("%n");
		                break;
		        	
		                
		        default: System.out.printf("%nInvalid query%n%n");
		                 break;
		                 
		        } 	// end switch
			} 		// end while

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.printf("Error connecting to db: %s%n", e.getMessage());
			System.exit(0);
		}

	}

}
