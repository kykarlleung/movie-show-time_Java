import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Vector;

public class database {
	
	private static String[] ratingsA = {"G","PG","PG-13","R","NC17", "NR"};
	private static String[] stringA;
	private static String[] timeA;
	private static Object[] rowData = new Object[3];
	public static Connection myConn;
	public static LinkedList<String> ListOfUsers = new LinkedList<String>();
	//public static LinkedList<email> ListOfEmails = new LinkedList<email>();
	//Gets connection to a database with the name email
	final static String url = "jdbc:mysql://:3306/showings?useSSL=false";
	private static PreparedStatement stmt;
	private static ResultSet rs;
	private static String previous_mainDS="";
	private static String mainDS="";
	private static String ratingsDS="";
	private static String timeDS="";
	private static String xDS="";
	private static String yDS="";

	public static void Connect(){
		/*all mySQL url for java require the format jdbc:mysql://
		followed by the host(IP assigned to database):port(3306)/database name*/
		try {
			//connect to database
			myConn = DriverManager.getConnection(url, "root", "root");
		}
		catch (SQLException ex) {
			throw new RuntimeException("Couldn't connect to database");
		}

	}
	
	public database(){
		//loadDataToMovieGUI();
	}
	
	
	//--------------------DisplayGUI---------------
		public static void loadDataToDisplay_CUser(String cinemaName)
		{
			mainDS = "";
			ratingsDS = "";
			timeDS = "";
			previous_mainDS ="";
			String sql = "SELECT showings.movies.movies, showings.movies.ratings, showings.moviesandcinemas.time "
					+ "FROM showings.moviesandcinemas, showings.movies "
					+ "where showings.moviesandcinemas.movies = showings.movies.movies and cinema='"+cinemaName+"'"
					+ "ORDER BY showings.movies.movies, STR_TO_DATE(time,'%h:%i%p');";
			try {
				Connect();
				stmt = myConn.prepareStatement(sql);
				rs = stmt.executeQuery();

				while(rs.next())
				{
					mainDS = rs.getString("movies");
					if(!(previous_mainDS.equals("")) && !(mainDS.equals(previous_mainDS)))
					{
						addMovieToDisplay_CUser(previous_mainDS, ratingsDS, timeDS);
					}
					ratingsDS = rs.getString("ratings");
					timeDS += rs.getString("time") + "  ";
					previous_mainDS = mainDS;
				}
				addMovieToDisplay_CUser(mainDS, ratingsDS, timeDS);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					myConn.close();
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public static void loadDataToDisplay_C(String cinemaName)
		{
			String sql = "SELECT showings.movies.movies, ratings, time FROM showings.moviesandcinemas, showings.movies where showings.movies.movies=showings.moviesandcinemas.movies and cinema='"+cinemaName+"' ORDER BY showings.movies.movies, STR_TO_DATE(time,'%h:%i%p');";
			try {
				Connect();
				stmt = myConn.prepareStatement(sql);
				rs = stmt.executeQuery();

				while(rs.next()){
					addMovieToDisplay_C(rs.getString("movies"), rs.getString("ratings"), rs.getString("time"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					myConn.close();
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public static void loadDataToDisplay_MUser(String movieName)
		{
			mainDS = "";
			ratingsDS = "";
			timeDS = "";
			xDS = "";
			yDS = "";
			previous_mainDS ="";
			
			
			String sql = "SELECT showings.moviesandcinemas.cinema, showings.cinemas.x, showings.cinemas.y, showings.movies.ratings, showings.moviesandcinemas.time FROM showings.movies, showings.cinemas, showings.moviesandcinemas where showings.moviesandcinemas.movies='"+movieName+"' and showings.movies.movies='"+movieName+"' and showings.cinemas.cinemas = showings.moviesandcinemas.cinema "+
					"ORDER BY cinema, STR_TO_DATE(time,'%h:%i%p');";
		
			try {
				Connect();
				stmt = myConn.prepareStatement(sql);
				rs = stmt.executeQuery();

				while(rs.next())
				{
					mainDS = rs.getString("cinema");
					if(!(previous_mainDS.equals("")) && !(mainDS.equals(previous_mainDS)))
					{
						addMovieToDisplay_MUser(previous_mainDS, "( " + xDS + " , " + yDS + " )", timeDS);
					}
					xDS = rs.getString("x");
					yDS = rs.getString("y");
					ratingsDS = "( " + xDS + " , " + yDS + " )";
					timeDS += rs.getString("time") + "  ";
					previous_mainDS = mainDS;
				}
				addMovieToDisplay_MUser(mainDS, ratingsDS, timeDS);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					myConn.close();
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public static void loadDataToDisplay_M(String movieName)
		{
			String sql = "SELECT showings.moviesandcinemas.cinema, showings.cinemas.x, showings.cinemas.y, showings.movies.ratings, showings.moviesandcinemas.time FROM showings.movies, showings.cinemas, showings.moviesandcinemas where showings.moviesandcinemas.movies='"+movieName+"' and showings.movies.movies='"+movieName+"' and showings.cinemas.cinemas = showings.moviesandcinemas.cinema "+
					"ORDER BY cinema, STR_TO_DATE(time,'%h:%i%p');";
			try {
				Connect();
				stmt = myConn.prepareStatement(sql);
				rs = stmt.executeQuery();

				while(rs.next()){
					addMovieToDisplay_M(rs.getString("cinema"), "( " + rs.getString("x") + " , " + rs.getString("y") + " )", rs.getString("time"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					myConn.close();
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public static void addMovieToDisplay_CUser(String movieName, String movieRating, String movieTime)
		{
			rowData[0] = movieName;
			rowData[1] = movieRating;
			rowData[2] = movieTime;
			DisplayGUI.model.addRow(rowData);
			timeDS = "";
		}
		
		public static void addMovieToDisplay_MUser(String cinemaName, String movieRating, String movieTime)
		{
			rowData[0] = cinemaName;
			rowData[1] = movieRating;
			rowData[2] = movieTime;
			DisplayGUI.model.addRow(rowData);
			timeDS = "";
		}

		public static void addMovieToDisplay_C(String movieName, String movieRating, String movieTime)
		{
			rowData[0] = movieName;
			rowData[1] = movieRating;
			rowData[2] = movieTime;
			DisplayGUI.model.addRow(rowData);
		}


		public static void addMovieToDisplay_M(String cinemaName, String movieRating, String movieTime)
		{
			rowData[0] = cinemaName;
			rowData[1] = movieRating;
			rowData[2] = movieTime;
			DisplayGUI.model.addRow(rowData);
		}

		public static void deleteMCT(String mS, String cS, String tS)
		{
			String sql = "DELETE FROM showings.moviesandcinemas WHERE movies='"+mS+"' and cinema='"+cS+"' and time='"+tS+"'";
			try {
				Connect();
				stmt = myConn.prepareStatement(sql);
				stmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					myConn.close();
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
			//----------------------------------

	public static void loadDataToMovie() {

		//I added a couple extra close() calls because it is good practice to close everything
		//to avoid memory leaks. this includes preparedStatements, result sets, and Connections
			String sql = "SELECT movies,ratings FROM showings.movies";
			try {
				Connect();
				stmt = myConn.prepareStatement(sql);
				rs = stmt.executeQuery();
				
				while(rs.next()){
					//Adds the users to a list, just so we can see all the users(for testing)
					addMovieToTable(rs.getString("movies"), rs.getString("ratings"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
					try {
						myConn.close();
						stmt.close();
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
	}
	
	public static void loadDataToCinema() {
	
		String sql = "SELECT cinemas,x,y FROM showings.cinemas";
		try {
			Connect();
			stmt = myConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				//Adds the users to a list, just so we can see all the users(for testing)
				addCinemaToTable(rs.getString("cinemas"), rs.getInt("x"), rs.getInt("y"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
				try {
					myConn.close();
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	

	public static String[] loadCinema() {

		Vector<String> t = new Vector<String>();
		String sql = "SELECT cinemas,x,y FROM showings.cinemas";
		try {
			Connect();
			stmt = myConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				//Adds the users to a list, just so we can see all the users(for testing)
				t.add(rs.getString("cinemas"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
				try {
					myConn.close();
					stmt.close();
					rs.close();
//					System.out.println("ERROR");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		stringA = t.toArray(new String[t.size()]);
		
		return stringA; 
	};
	
	public static String[] loadMovie() {

		Vector<String> t = new Vector<String>();
		String sql = "SELECT movies FROM showings.movies";
		try {
			Connect();
			stmt = myConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				//Adds the users to a list, just so we can see all the users(for testing)
				t.add(rs.getString("movies"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
				try {
					myConn.close();
					stmt.close();
					rs.close();
//					System.out.println("ERROR");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		stringA = t.toArray(new String[t.size()]);
		
		return stringA; 
	};
	

//We'll probably keep this or change it depending on what our hours should be for the Movie Cinemas
	public static String[] loadTime() {
		//Lazy means to load timeA with an array of time
		Vector<String> t = new Vector<String>();
		for(int i = 0; i < 24 ; i++) {
			if(i == 0) {
				t.add(12+":00 AM");
				t.add(12+":30 AM");
			}	
			else if(i == 12) {
				t.add(12+":00 PM");
				t.add(12+":30 PM");
			}
			else if(i > 12){
				t.add(i-12+":00 PM");
				t.add(i-12+":30 PM");
			}
			else {
				t.add(i+":00 AM");
				t.add(i+":30 AM");
			}
			
		}
		timeA = t.toArray(new String[t.size()]);
		return timeA;
	}
	
	public static void setTimeA(String[] timeA) {
		database.timeA = timeA;
	}

	public static void deleteMovie(String movieS, String cinemaS, String timeS) {

		//Originally I was trying to delete them both with the same prepared statement because in SQL
		//You can execute multiple queries if they are separated by a semicolon, but can't do that with JDBC apparently,
		//So 2 different prepared statements are required
		if(cinemaS.equals("") && timeS.equals("")) {
			String del1 = "DELETE FROM showings.`movies`" +
					" WHERE movies = '" + movieS +"'";
			String del2 = "DELETE FROM showings.`moviesandcinemas`"
					+ " WHERE movies = '" +movieS+ "'";
			Connect();
			try {
				stmt = myConn.prepareStatement(del1);
				stmt.executeUpdate();
	
				stmt = myConn.prepareStatement(del2);
				stmt.executeUpdate();
				
				movieGUI.removeMovieJCBox(movieS);	// 11/14 Only removes Movies from the JC Box if all instances of a movie are being deleted
			
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			finally {
				try {
					myConn.close();
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			System.out.println(movieS + " has been removed from DB");
		}
		else {
			String del = "DELETE FROM showings.`moviesandcinemas`"
					+ " WHERE movies = '" +movieS+ "' AND cinema = '" + cinemaS 
					+ "' AND time ='" +timeS+"'";
			
			Connect();
			try {
				stmt = myConn.prepareStatement(del);
				stmt.executeUpdate();

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			finally {
				try {
					myConn.close();
					stmt.close();
										
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			System.out.println("The following showing has been deleted: " +cinemaS + " showing " + movieS + " at " + timeS);
		}
	}

	public static void deleteCinema(String cinemaS) {
		
/*@
//11/9 I hope you don't mind but I mimicked your code above to remove cinemas where it may appear under both the 'cinemas' Table and the 'moviesandcinemas' Table
 * I don't quite understand the add method part yet but we'll need to the same for adding movies to both the 'moviesandcinemas' Table and the 'movies' Table
*/	
		String del1 = "DELETE FROM showings.`cinemas`" +
				" WHERE cinemas = '" + cinemaS +"'";

		String del2 = "DELETE FROM showings.`moviesandcinemas`"
				+ " WHERE cinema = '" +cinemaS+ "'";
		
		Connect();
	try {
		stmt = myConn.prepareStatement(del1);
		stmt.executeUpdate();
		
		stmt = myConn.prepareStatement(del2);
		stmt.executeUpdate();
		movieGUI.removeCinemaJCBox(cinemaS);

	} catch (SQLException ex) {
		ex.printStackTrace();
	}
	finally {
		try {
			myConn.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		//movieGUI.cinemaJCBox.removeItem(cinemaS); //This part may be unnecessary after DB goes live, I'll need more testing
		//System.out.println(cinemaS + " was just removed from cinemaJCBox");
		System.out.println(cinemaS + " has been removed from DB");
		
	}
	
	public static String[] getRatingsA() {
		return ratingsA;
	}

	public static void setRatingsA(String[] ratingsA) {
		database.ratingsA = ratingsA;
	}

	public static String[] getStringA() {
		return stringA;
	}

	public static void setStringA(String[] stringA) {
		database.stringA = stringA;
	}

	public static String[] getTimeA() {
		return timeA;
	}

	public static void getCinemaXY(String cinemaS) {
/*@
 * update movieGUI.xDB and movieGUI.yDB with the xy coordinates of the cinema with the name cinemaS
 * We'll need this for comparing/filtering Cinemas that are within radius of our location under filter
 * Also, I'm not sure how Karl or I will want to use this information for solving how to filter cinemas :\
*/		
		String sql = "SELECT * FROM showings.cinemas"
				+ "WHERE cinemas ='" + cinemaS + "'";
		try {
			Connect();
			stmt = myConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			if(rs.next()){
				movieGUI.xDB = rs.getInt(2);
				movieGUI.yDB = rs.getInt(3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
				try {
					myConn.close();
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
	}

//Dann's Bookmark
    /*if the movie isn't unique, meaning it exist, then we check to see if the rating entered matches the record in the DB and
	the number of instances. if not the same rating or if 3 instances exist already, dont add to the DB */
	public static void addMovieToDB(String movieS, String cinemaS, String ratingS, String timeS) {

//If Movie is Unique && If Cinema does not have 3 instances of movieS
	//insert into database table Movie|Rating and Movie|Cinema|Time
		
		//THE ASSUMPTION HERE IS THAT THE MOVIE ALREADY EXISTS AND THERFORE DO NOT NEED TO CHECK THE UNIQUENESS OF THE MOVIE
		boolean c =  cinemaS.equals("");
		boolean r =  ratingS.equals("");
		boolean t =  timeS.equals("");
		if( r && (!c && ! t) ){	//User called addMovieToDB with Movie Title, Cinema, and Time but no Rating (Conditions for Adding a Showing)
			
			int count = getMovieCount(movieS,cinemaS);
			//boolean isRSame = sameRating(movieS,ratingS);
			if(count < 3 /*&& isRSame*/) {
				
				ratingS = getMovieRating(movieS);
				
				if( (!ratingValid(cinemaS, ratingS)) ) {
					messageBox.doMessage(cinemaS + " does not show films that are rated " + ratingS + ".");
					System.out.println("ratingS:"+ratingS+"..");
					System.out.println("CINEMA DOES NOT TAKE A MOVIE OF THAT RATING");
					return;
				}
				
				Connect();
				try {
					String insert = "Insert into showings.`moviesandcinemas`" +
							"VALUES(?,?,?)";
					stmt = myConn.prepareStatement(insert);
					
					//filling in question marks
					stmt.setString(1, cinemaS);
					stmt.setString(2, movieS);
					stmt.setString(3, timeS);
					
					//int numberUpdated =stmt.executeUpdate();
					stmt.executeUpdate();
					
					System.out.println("Movie added: " + movieS + " rated " + ratingS + " at " + cinemaS + " for " + timeS);

				} catch (SQLException e) {
					messageBox.doMessage("The movie is already showing at that time");
					System.out.println("The movie is already showing at that time");
				}
				finally {
					try {
						myConn.close();
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			else {
				if (!r) messageBox.doMessage("The rating of the movie you are trying to add does not match database records.");
				else if (count >= 3) messageBox.doMessage("The cinema limit on the number of movie showings has reached its max.");
				System.out.println("There are already 3 different showing times for this movie"
						+ " or the rating entered doesn't match the one on record");
			}
		}
		else if (c && t && !r){	//User called addMovieToDB with Movie Title and Rating, but no Cinema and no Time (Conditions for Adding a Movie)
			
			if(!movieIsUnique(movieS)){
				messageBox.doMessage("That movie already exists in the database!");
				System.out.println("That movie already exists in the database!");
				return;
			}	//Check to see if the movie already exists in the database
			
			Connect();
			try {
			/*
				String insert = "Insert into showings.`moviesandcinemas`" +
						"VALUES(?,?,?)";
				stmt = myConn.prepareStatement(insert);
				
				//filling in question marks
				stmt.setString(1, cinemaS);
				stmt.setString(2, movieS);
				stmt.setString(3, timeS);
				
				//int numberUpdated =stmt.executeUpdate();
				stmt.executeUpdate();
			*/
				String insert2 = "Insert into showings.`movies`" +
						"VALUES(?,?)";
				stmt = myConn.prepareStatement(insert2);
				
				stmt.setString(1, movieS);
				stmt.setString(2, ratingS);
				
				stmt.executeUpdate();
				
				movieGUI.addMovieJCBox(movieS);	//11/14 Movie JCBox

				System.out.println("Movie added: " + movieS + " rated " + ratingS + " at " + cinemaS + " for " + timeS);

			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					myConn.close();
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	//Adds cinema to the DB only if the cinema doesnt exist already and if the location is not in use
	public static void addCinemaToDB(String cinemaS, int xInt, int yInt, boolean isG, boolean isPG, boolean isPG13, boolean isR, boolean isNC17, boolean isNR) {

//If Cinema is Unique
	//insert into database table Cinema|X|Y	
		if(CinemaIsUnique(cinemaS, xInt, yInt)) {
				Connect();
			try {
				String insert = "Insert into showings.`cinemas`" +
						"VALUES(?,?,?,?,?,?,?,?,?)";
				stmt = myConn.prepareStatement(insert);
				
				//filling in question marks
				stmt.setString(1, cinemaS); stmt.setInt(2, xInt); stmt.setInt(3, yInt);
				stmt.setBoolean(4, isG); stmt.setBoolean(5, isPG); stmt.setBoolean(6, isPG13);
				stmt.setBoolean(7, isR); stmt.setBoolean(8, isNC17); stmt.setBoolean(9, isNR);
				stmt.executeUpdate();
	
				movieGUI.addCinemaJCBox(cinemaS);		//11/11 Updates CinemaJCBox of new cinema
				System.out.println("Cinema added: " +cinemaS +" xy: " +xInt +"," +yInt);
				//System.out.print("User entered into DB");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					myConn.close();
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			messageBox.doMessage("Cinema already exists or Address is not available");
			System.out.println("Cinema already exists or Location is in use");
			}
	}
	
	public static void addMovieToTable(String movieName, String movieRating)
    {
		rowData[0] = movieName;
    	rowData[1] = movieRating;
    	movieGUI.movieTableModel.addRow(rowData);
    }//Adds row to Movies
	
	public static void addCinemaToTable(String cinemaName, int cinemaX, int cinemaY)
    {
    	rowData[0] = cinemaName;
    	rowData[1] = cinemaX;
    	rowData[2] = cinemaY;
    	movieGUI.cinemaTableModel.addRow(rowData);
    }//Adds row to Cinema
	
	//This method checks to see if the movie already exist
		private static boolean movieIsUnique(String movieS) {
			String sql = "SELECT movies FROM showings.`movies`"
					+ " WHERE movies ='" + movieS +"'";
			Connect();
			try {
				stmt = myConn.prepareStatement(sql);
				rs = stmt.executeQuery();
				//if we have a next row in the result set than the movie already exist and thus not unique
				if(rs.next()){
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
					try {
						myConn.close();
						stmt.close();
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
			return true;
		}
		
		//Checks if cinema is unqiue, first by checking is the location is unique then checking,
		//if the cinema exist already
		private static boolean CinemaIsUnique(String cinemaS, int xInt, int yInt){
			//if the location is not unique then we can not created the cinema.
			if(!locationIsUnique(xInt,yInt)){
				return false;
			}
			String sql = "SELECT cinemas FROM showings.`cinemas`"
					+ " WHERE cinemas ='" + cinemaS +"'";
			Connect();
			try {
				stmt = myConn.prepareStatement(sql);
				rs = stmt.executeQuery();
				//if we have a next row in the result set than the movie already exist and thus not unique
				if(rs.next()){
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
					try {
						myConn.close();
						stmt.close();
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
			return true;
		}

		//This method is used to get the number of instances of the movie in DB
		private static int getMovieCount(String movieS, String cinemaS) {
			int count = 0;
			String sql = "SELECT * FROM showings.`moviesandcinemas`"
					+ " WHERE movies ='" + movieS +"'";
			
			if(!(cinemaS.equals(""))) {
				sql += " AND cinema ='" + cinemaS + "'";
			}													//11/14 Append String If CinemaS parameter is also being used
			
			Connect();
			try {
				stmt = myConn.prepareStatement(sql);
				rs = stmt.executeQuery();
				//if we have a next row in the result set than the movie already exist and thus not unique
				while(rs.next()){
					count++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
					try {
						myConn.close();
						stmt.close();
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			return count;
		}
		
		//this method checks to see if the location is unique, if it isnt return false
		private static boolean locationIsUnique(int xInt, int yInt) {
			String sql = "SELECT cinemas FROM showings.`cinemas`"
					+ " WHERE x = '" + xInt +"' AND y = '" + yInt +"'";
			Connect();
			try {
				stmt = myConn.prepareStatement(sql);
				rs = stmt.executeQuery();
				//if we have a next row in the result set than the movie already exist and thus not unique
				if(rs.next()){
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
					try {
						myConn.close();
						stmt.close();
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
			return true;
		}
		
		//this methods checks to see if the movie rating entered matches the rating of the movie in the DB
		private static boolean sameRating(String movieS, String ratingS) {
			String sql = "SELECT ratings FROM showings.`movies`"
					+ " WHERE movies ='" + movieS +"'";
			Connect();
			try {
				stmt = myConn.prepareStatement(sql);
				rs = stmt.executeQuery();
				//if the ratings match return true, else return false
				if(rs.next()){
					if(rs.getString(1).equals(ratingS)) return true;
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
					try {
						myConn.close();
						stmt.close();
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
			return false;
		}

		private static boolean ratingValid(String cinemaS, String ratingS) {
			String sql = "SELECT `"+ ratingS + "` FROM showings.`cinemas`"
					+ " WHERE cinemas ='" + cinemaS +"'";
			Connect();
			try {
				stmt = myConn.prepareStatement(sql);
				rs = stmt.executeQuery();
				if(rs.next()){
					if(rs.getInt(1) == 1) return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
					try {
						myConn.close();
						stmt.close();
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
			return false;
	}
		
		protected static String getMovieRating(String movieS) {
			String sql = "SELECT ratings FROM showings.`movies`"
					+ " WHERE movies ='" + movieS +"'";
			Connect();
			try {
				stmt = myConn.prepareStatement(sql);
				rs = stmt.executeQuery();
				//If the movie exist, get its rating. if it doesn't exist
				//it doesnt have a rating so return null
				if(rs.next()){
					return rs.getString(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
					try {
						myConn.close();
						stmt.close();
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
			return null;
		}
		
		

}