//Alexander Malcolm - S1317460

package org.me.myandroidstuff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CarParkListingTestActivity extends Activity implements OnClickListener
{
	//keeping these vars incase needed for debugging parsing stuff
	//private TextView response;
	//private TextView errorText;
	private String result;
    private String sourceListingURL = "http://open.glasgow.gov.uk/api/live/parking.php?type=xml";
    LinkedList <CarPark> carParkList = null;
    
    private TextView cpNameVar, cpStatusVar, cpPercentOccupiedVar, cpSpacesOccupiedVar;
    private Button cpButton1Var, cpButton2Var, cpButton3Var, cpButton4Var, cpButton5Var, cpButton6Var,
    				cpButton7Var, cpButton8Var, cpButton9Var, cpButton10Var, cpButton11Var;
    
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.mainlayoutportrait);
        setContentView(R.layout.mainlayoutportrait2);
        
        // Get the TextView object on which to display the results
       // response = (TextView)findViewById(R.id.urlResponse);
        try
        {
        	// Get the data from the RSS stream as a string
        	result =  sourceListingString(sourceListingURL);
        	Log.e("Debug","finished grabbing the url stuff");
        	// Do some processing of the data to get the individual parts of the XML stream
        	// At some point put this processing into a separate thread of execution
        	carParkList = parseData(result);
        	// Display the string in the TextView object just to demonstrate this capability
        	// This will need to be removed at some point
        	
        	if (carParkList != null)
    		{
        		//currently dead code as i have not added anything into the list, this preparing to right now.
    			Log.e("MyTag","List not null");
    			for (Object carPark : carParkList)
    			{
    				Log.e("MyTag",carPark.toString());
    			}
    		}
    		else
    		{
    			Log.e("MyTag","List is null");
    		}
        	
        	//response.setText(result);
        	
        	assignViews();
        	setButtonTextTemp();
        	
        }
        catch(IOException ae)
        {
        	// Handle error
        	//response.setText("Error");
        	// Add error info to log for diagnostics
        	//errorText.setText(ae.toString());
        } 
        
    } // End of onCreate
	
	//method will assign views to vars so that i can edit and grab them in the app
	private void assignViews()
	{
		Log.e("MyTag", "started assign views");
		//assign all the temp buttons, these are only for the prototype version of my app
		cpButton1Var = (Button)findViewById(R.id.cpButton1);
		cpButton1Var.setOnClickListener(this);
		
		cpButton2Var = (Button)findViewById(R.id.cpButton2);
		cpButton2Var.setOnClickListener(this);
		
		cpButton3Var = (Button)findViewById(R.id.cpButton3);
		cpButton3Var.setOnClickListener(this);
		
		cpButton4Var = (Button)findViewById(R.id.cpButton4);
		cpButton4Var.setOnClickListener(this);
		
		cpButton5Var = (Button)findViewById(R.id.cpButton5);
		cpButton5Var.setOnClickListener(this);
		
		cpButton6Var = (Button)findViewById(R.id.cpButton6);
		cpButton6Var.setOnClickListener(this);
		
		cpButton7Var = (Button)findViewById(R.id.cpButton7);
		cpButton7Var.setOnClickListener(this);
		
		cpButton8Var = (Button)findViewById(R.id.cpButton8);
		cpButton8Var.setOnClickListener(this);
		
		cpButton9Var = (Button)findViewById(R.id.cpButton9);
		cpButton9Var.setOnClickListener(this);
		
		cpButton10Var = (Button)findViewById(R.id.cpButton10);
		cpButton10Var.setOnClickListener(this);
		
		cpButton11Var = (Button)findViewById(R.id.cpButton11);
		cpButton11Var.setOnClickListener(this);
		
		//assign the temp text views, also just for prototype
		cpNameVar = (TextView)findViewById(R.id.cpName);
		cpStatusVar = (TextView)findViewById(R.id.cpStatus);
		cpPercentOccupiedVar = (TextView)findViewById(R.id.cpPercentOccupied);
		cpSpacesOccupiedVar = (TextView)findViewById(R.id.cpSpacesOccupied);
		Log.e("Debug", "all views assigned");
	}
	
	private void setButtonTextTemp()
	{
		//set the text on all buttons to be the name of the car park
		cpButton1Var.setText(carParkList.get(0).getName().split("\\:")[0]);
		cpButton2Var.setText(carParkList.get(1).getName().split("\\:")[0]);
		cpButton3Var.setText(carParkList.get(2).getName().split("\\:")[0]);
		cpButton4Var.setText(carParkList.get(3).getName().split("\\:")[0]);
		cpButton5Var.setText(carParkList.get(4).getName().split("\\:")[0]);
		cpButton6Var.setText(carParkList.get(5).getName().split("\\:")[0]);
		cpButton7Var.setText(carParkList.get(6).getName().split("\\:")[0]);
		cpButton8Var.setText(carParkList.get(7).getName().split("\\:")[0]);
		cpButton9Var.setText(carParkList.get(8).getName().split("\\:")[0]);
		cpButton10Var.setText(carParkList.get(9).getName().split("\\:")[0]);
		cpButton11Var.setText(carParkList.get(10).getName().split("\\:")[0]);
	}
    // Method to handle the reading of the data from the XML stream
	private static String sourceListingString(String urlString)throws IOException
    {
	 	String result = "";
    	InputStream anInStream = null;
    	int response = -1;
    	URL url = new URL(urlString);
    	URLConnection conn = url.openConnection();
    	
    	// Check that the connection can be opened
    	if (!(conn instanceof HttpURLConnection))
    			throw new IOException("Not an HTTP connection");
    	try
    	{
    		// Open connection
    		HttpURLConnection httpConn = (HttpURLConnection) conn;
    		httpConn.setAllowUserInteraction(false);
    		httpConn.setInstanceFollowRedirects(true);
    		httpConn.setRequestMethod("GET");
    		httpConn.connect();
    		response = httpConn.getResponseCode();
    		// Check that connection is Ok
    		if (response == HttpURLConnection.HTTP_OK)
    		{
    			// Connection is Ok so open a reader 
    			anInStream = httpConn.getInputStream();
    			InputStreamReader in= new InputStreamReader(anInStream);
    			BufferedReader bin= new BufferedReader(in);
    			
    			// Read in the data from the XML stream
    			bin.readLine(); // Throw away the header
    			String line = new String();
    			while (( (line = bin.readLine())) != null)
    			{
    				result = result + "\n" + line;
    			}
    		}
    	}
    	catch (Exception ex)
    	{
    			throw new IOException("Error connecting");
    	}
    	
    	// Return result as a string for further processing
    	return result;
    	
    } // End of sourceListingString
    
    private LinkedList<CarPark> parseData(String dataToParse)
	{
    	CarPark carPark = null;
    	LinkedList<CarPark> aList = null;
		try
		{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput( new StringReader ( dataToParse ) );
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) 
			{
					// Found a start tag 
					if(eventType == XmlPullParser.START_TAG) 
					{
						// Check which Tag we have
						if (xpp.getName().equalsIgnoreCase("glasgow_parking"))
						{
							aList  = new LinkedList<CarPark>();
						}
						else
						if (xpp.getName().equalsIgnoreCase("situation"))
						{
							Log.e("ParseTest","Item Start Tag found");
							carPark = new CarPark();		
						}
						else						
						if (xpp.getName().equalsIgnoreCase("carParkIdentity"))
	            		{
							// Now just get the associated text 
	            			String temp = xpp.nextText();
	            			// Do something with text
	            			Log.e("ParseTest","Name: " + temp);
	            			carPark.setName(temp);
	            		}					
						else						
							// Check which Tag we have
							if (xpp.getName().equalsIgnoreCase("carParkStatus"))
							{
								// Now just get the associated text 
								String temp = xpp.nextText();
								// Do something with text
								Log.e("ParseTest","Status: " + temp);
								carPark.setStatus(temp);
							}
							else							
								// Check which Tag we have
								if (xpp.getName().equalsIgnoreCase("totalCapacity"))
								{
									// Now just get the associated text 
									String temp = xpp.nextText();
									// Do something with text
									Log.e("ParseTest","Total Capacity:" + temp);
									carPark.setTotalSpaces(temp);
								}
								else							
									// Check which Tag we have
									if (xpp.getName().equalsIgnoreCase("occupiedSpaces"))
									{
										// Now just get the associated text 
										String temp = xpp.nextText();
										// Do something with text
										Log.e("ParseTest","Occupied Spaces:" + temp);
										carPark.setOccupiedSpaces(temp);
									}
									else							
										// Check which Tag we have
										if (xpp.getName().equalsIgnoreCase("carParkOccupancy"))
										{
											// Now just get the associated text 
											String temp = xpp.nextText();
											// Do something with text
											Log.e("ParseTest","Percent Occupied:" + temp);
											carPark.setPercentOccupied(temp);
										}
					}
					else
						if(eventType == XmlPullParser.END_TAG) 
						{
							if (xpp.getName().equalsIgnoreCase("situation"))
							{
								Log.e("ParseTest","Car Park is: " + carPark.toString());
								aList.add(carPark);
								Log.e("ParseTest","added park to list");
								
							}
							else
								if (xpp.getName().equalsIgnoreCase("glasgow_parking"))
								{
									int size;
									size = aList.size();
									Log.e("ParseTest","Car park list size is " + size);
								}
						}
						
						
				// Get the next event	
				eventType = xpp.next();
				
			} // End of while
		}
 		catch (XmlPullParserException ae1)
 		{
 			Log.e("Debug","Parsing error" + ae1.toString());
 		}
 		catch (IOException ae1)
 		{
 			Log.e("Debug","IO error during parsing");
 		}
		
		Log.e("Debug","End document");
		
		return aList;

	}
    

	public void onClick(View v) 
	{
		if (v==cpButton1Var) 
		{
			cpNameVar.setText("Name: " + carParkList.get(0).getName());
			cpStatusVar.setText("Status:" + carParkList.get(0).getStatus());
			cpPercentOccupiedVar.setText("Percent Occupied: " + carParkList.get(0).getPercentOccupied() + "%");
			cpSpacesOccupiedVar.setText("Occupied Spaces:" + carParkList.get(0).getOccupiedSpaces() + "/" + carParkList.get(0).getTotalSpaces());
		}
		
		if (v==cpButton2Var) 
		{
			cpNameVar.setText("Name: " + carParkList.get(1).getName());
			cpStatusVar.setText("Status:" + carParkList.get(1).getStatus());
			cpPercentOccupiedVar.setText("Percent Occupied: " + carParkList.get(1).getPercentOccupied() + "% occupied");
			cpSpacesOccupiedVar.setText("Occupied Spaces:" + carParkList.get(1).getOccupiedSpaces() + "/" + carParkList.get(1).getTotalSpaces());
		}
		
		if (v==cpButton3Var) 
		{
			cpNameVar.setText("Name: " + carParkList.get(2).getName());
			cpStatusVar.setText("Status:" + carParkList.get(2).getStatus());
			cpPercentOccupiedVar.setText("Percent Occupied: " + carParkList.get(2).getPercentOccupied() + "% occupied");
			cpSpacesOccupiedVar.setText("Occupied Spaces:" + carParkList.get(2).getOccupiedSpaces() + "/" + carParkList.get(2).getTotalSpaces());
		}
		
		if (v==cpButton4Var) 
		{
			cpNameVar.setText("Name: " + carParkList.get(3).getName());
			cpStatusVar.setText("Status:" + carParkList.get(3).getStatus());
			cpPercentOccupiedVar.setText("Percent Occupied: " + carParkList.get(3).getPercentOccupied() + "% occupied");
			cpSpacesOccupiedVar.setText("Occupied Spaces:" + carParkList.get(3).getOccupiedSpaces() + "/" + carParkList.get(3).getTotalSpaces());
		}
		
		if (v==cpButton5Var) 
		{
			cpNameVar.setText("Name: " + carParkList.get(4).getName());
			cpStatusVar.setText("Status:" + carParkList.get(4).getStatus());
			cpPercentOccupiedVar.setText("Percent Occupied: " + carParkList.get(4).getPercentOccupied() + "% occupied");
			cpSpacesOccupiedVar.setText("Occupied Spaces:" + carParkList.get(4).getOccupiedSpaces() + "/" + carParkList.get(4).getTotalSpaces());
		}
		
		if (v==cpButton6Var) 
		{
			cpNameVar.setText("Name: " + carParkList.get(5).getName());
			cpStatusVar.setText("Status:" + carParkList.get(5).getStatus());
			cpPercentOccupiedVar.setText("Percent Occupied: " + carParkList.get(5).getPercentOccupied() + "% occupied");
			cpSpacesOccupiedVar.setText("Occupied Spaces:" + carParkList.get(5).getOccupiedSpaces() + "/" + carParkList.get(5).getTotalSpaces());
		}
		
		if (v==cpButton7Var) 
		{
			cpNameVar.setText("Name: " + carParkList.get(6).getName());
			cpStatusVar.setText("Status:" + carParkList.get(6).getStatus());
			cpPercentOccupiedVar.setText("Percent Occupied: " + carParkList.get(6).getPercentOccupied() + "% occupied");
			cpSpacesOccupiedVar.setText("Occupied Spaces:" + carParkList.get(6).getOccupiedSpaces() + "/" + carParkList.get(6).getTotalSpaces());
		}
		
		if (v==cpButton8Var) 
		{
			cpNameVar.setText("Name: " + carParkList.get(7).getName());
			cpStatusVar.setText("Status:" + carParkList.get(7).getStatus());
			cpPercentOccupiedVar.setText("Percent Occupied: " + carParkList.get(7).getPercentOccupied() + "% occupied");
			cpSpacesOccupiedVar.setText("Occupied Spaces:" + carParkList.get(7).getOccupiedSpaces() + "/" + carParkList.get(7).getTotalSpaces());
		}
		
		if (v==cpButton9Var) 
		{
			cpNameVar.setText("Name: " + carParkList.get(8).getName());
			cpStatusVar.setText("Status:" + carParkList.get(8).getStatus());
			cpPercentOccupiedVar.setText("Percent Occupied: " + carParkList.get(8).getPercentOccupied() + "% occupied");
			cpSpacesOccupiedVar.setText("Occupied Spaces:" + carParkList.get(8).getOccupiedSpaces() + "/" + carParkList.get(8).getTotalSpaces());
		}
		
		if (v==cpButton10Var) 
		{
			cpNameVar.setText("Name: " + carParkList.get(9).getName());
			cpStatusVar.setText("Status:" + carParkList.get(9).getStatus());
			cpPercentOccupiedVar.setText("Percent Occupied: " + carParkList.get(9).getPercentOccupied() + "% occupied");
			cpSpacesOccupiedVar.setText("Occupied Spaces:" + carParkList.get(9).getOccupiedSpaces() + "/" + carParkList.get(9).getTotalSpaces());
		}
		
		if (v==cpButton11Var) 
		{
			cpNameVar.setText("Name: " + carParkList.get(10).getName());
			cpStatusVar.setText("Status:" + carParkList.get(10).getStatus());
			cpPercentOccupiedVar.setText("Percent Occupied: " + carParkList.get(10).getPercentOccupied() + "% occupied");
			cpSpacesOccupiedVar.setText("Occupied Spaces:" + carParkList.get(10).getOccupiedSpaces() + "/" + carParkList.get(10).getTotalSpaces());
		}
		
	}
    
} // End of Activity class