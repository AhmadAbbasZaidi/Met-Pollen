
# Welcome to Met-Pollen!
# Vision

The aim behind this application is to provide Pakistan pollen and weather data to local Pakistani citizens. The source of data is Pakistan Meteorological Department. Currently Android is the target platform.
   
## Author

**Ahmad Abbas Zaidi**


## Licence

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/AhmadAbbasZaidi/Met-Pollen/blob/master/LICENSE.md) file for details

## **Prerequisites**

For android all you will need to do is install android studio latest version with its prerequisite Java. Android studio will get all of other dependencies.

Setup database at your end. In Application there are three types of data that is Districts, Current Weather and Forecast Weather

**Setup**
1. Add your **base url** in 
**Met-Pollen/app/src/main/java/com/cfp/metpollen/data/api/ApiHandler.java**
2. Add your **sub urls** in 
**Met-Pollen/app/src/main/java/com/cfp/metpollen/data/api/ApiCalls.java**

**API Description**

Focusing sub urls in **Met-Pollen/app/src/main/java/com/cfp/metpollen/data/api/ApiCalls.java**

 1. **getstations**

a. Header -> empty

b. Body -> empty

c. Method -> GET

	**Result**
{
  "Result": {
    "Status": 200,
    "Message": [
      {
        "Station_Name": "ISLAMABAD",
        "Latitude": 33.71,
        "Id": 236,
        "Longitude": 73.07
      }
    ]
  }
}

 2. **getforecast**

a. Header -> empty

b. Body ->  
- is_station=0 	
- station_id=0  	
- latitude=33.71158
- longitude=73.05742

c. Method -> POST


	**Result**
{
  "Result": {
    "Status": 200,
    "Message": {
      "Weatherdata": [
        {
          "Min_Temperature": 7,
          "Temperature": 10,
          "Entry_Time": "2017-12-1215: 59: 47.254335",
          "Wind_Speed": 0,
          "Formatted_Weather_Time": "2018-01-2600: 00: 00.0",
          "Max_Temperature": 21,
          "Precipitation": 0,
          "Weather_Time": "00Z24JAN2018",
          "PMSL": 0,
          "Rain_Humidity": 0,
          "Wind_Direction": 0,
          "Cloud": 0,
          "imageurl": "",
          "Id": 873757,
          "Grid_Location_Id": 6778
        }
      ],
      "SunRiseSet": {
        "2018-01-29 20:00:00.0": {
          "SunSet": "2018-01-29 17:36:16.0",
          "SunRise": "2018-01-29 07:07:49.0"
        },
        "2018-01-27 10:00:00.0": {
          "SunSet": "2018-01-27 17:34:17.0",
          "SunRise": "2018-01-27 07:08:59.0"
        },
        "2018-01-26 05:00:00.0": {
          "SunSet": "2018-01-26 17:33:17.0",
          "SunRise": "2018-01-26 07:09:31.0"
        },
        "2018-01-28 15:00:00.0": {
          "SunSet": "2018-01-28 17:35:16.0",
          "SunRise": "2018-01-28 07:08:25.0"
        }
      }
    }
  }
}

 3. **getcurrent**

a. Header -> empty

b. Body ->  
- latitude=33.71
- longitude=73.07

c. Method -> POST


	**Result**

  {
  	"Result": {
    "Status": 200,
    "Message": {
      "Weatherdata": {
        "Min_Temperature": 7,
        "Entry_Time": "2018-01-17 12:23:59.765275",
        "Dew_Point": 4.78577,
        "Wind_Speed": 15.2001,
        "Formatted_Weather_Time": "2018-01-26 12:23:59.0",
        "Max_Temperature": 21,
        "Precipitation": 0.999144,
        "Mean_Temperature": 17,
        "Wind_Direction": "SE",
        "Station_Id": 133,
        "imageurl": "",
        "Visibility": 7.70851,
        "Id": 33,
        "Relative_Humidity": 44.8952
      },
      "Pollen": {
        "StationName": "E-8",
        "total": 64,
        "pollen_percentage": 75,
        "total_status": "High",
        "Pollendata": [
          {
            "name": "Paper_Mulbery",
            "count": 0,
            "status": "Absent"
          }
        ],
        "Id": 2,
        "StationId": 12
      },
      "SunRiseSet": {
        "SunSet": "2018-01-26 17:33:15.0",
        "SunRise": "2018-01-26 07:09:28.0"
      }
    }
  }
}


**Preview**

<html>
<p align="center">
  <img src="https://github.com/AhmadAbbasZaidi/Met-Pollen/blob/master/preview/1.png" width="350"/>
  <img src="https://github.com/AhmadAbbasZaidi/Met-Pollen/blob/master/preview/2.png" width="350"/>
  <img src="https://github.com/AhmadAbbasZaidi/Met-Pollen/blob/master/preview/3.png" width="350"/>
  <img src="https://github.com/AhmadAbbasZaidi/Met-Pollen/blob/master/preview/4.png" width="350"/>
  <img src="https://github.com/AhmadAbbasZaidi/Met-Pollen/blob/master/preview/5.png" width="350"/>
  <img src="https://github.com/AhmadAbbasZaidi/Met-Pollen/blob/master/preview/6.png" width="350"/>
</p>
</html>