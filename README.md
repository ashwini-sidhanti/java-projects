# ashwini-holiday-info

This project includes a service called Ashwini, which provides holiday data by country.
It integrates with the external API [Nager](https://date.nager.at) and applies custom business logic to process and return the relevant data.

## Table of contents
- [About](#about)
- [Features](#features)
- [Getting Started](#getting-started)
    - [Requirements](#requirements)
    - [Install](#install)
- [Usage](#usage)
- [License](#license)
- [Authors](#authors)

## About
Ashwini is a Java-based Micronaut microservice designed to retrieve public holiday data for multiple countries 
using the [Nager](https://date.nager.at) API. The service does not include a fallback mechanism; in the event of an external API 
failure, it returns a 500 Internal Server Error.

## Features
- Given a country name, return the last celebrated 3 holidays (date and name)
- Given a year and a list country code, for each country return a number of public holidays not falling on weekends (sort in descending order)
- Given a year and 2 country codes, return the deduplicated list of dates celebrated in both countries (date + local names)

## Getting Started
This project is publicly available and may be used for legal purposes.
You can start the Ashwini Micronaut application using the following command:
mvn mn:run
Once the application is running, the service endpoints can be accessed via the base URL:
`http://localhost:8080`
Use this URL to interact with the API endpoints and retrieve holiday data based on the requirement.


### Requirements
- **Language:** Java (21 or higher)
- **Framework:** [Micronaut](https://micronaut.io)
- **Build Tool:** Maven
- **External API:** [https://date.nager.at](https://date.nager.at)

## Usage
You can interact with the Ashwini service using any REST client such as Postman or curl.
For a smoother experience, Postman is recommended — it allows you to send requests easily and view formatted JSON responses.
- The service does not require authentication or authorization.
- There are three endpoints, all designed as retrieve endpoints.
- For two endpoints, POST is supported to allow sending a request body and to avoid caching issues.



  | Method         | Endpoint                           | Description                                                                     |
  | -------------- | ---------------------------------- | ------------------------------------------------------------------------------- |
  | `GET`          | `/api/holiday-info/{country}`             | Returns last 3 holidays for the give country (only for the current year)        |
  | `POST`         | `/api/holiday-info/`                      | Returns count of holidays not on a weekend a specific year for a country codes  |
  | `POST`         | `/api/holiday-info/deduplicated-holidays` | Returns a deduplicated list of holidays celebrated in both countries            |


Respective Json responses:
- **GET** `/api/holiday-info/{countryName}`
  - Response
```json
[
    {
        "date": "2025-11-01",
        "name": "All Saints Day"
    }
]
```
- **POST** `/api/holiday-info/`
  - Request
```JSON
 {
  "year": "2025",
  "countryCode": [
    "ES",
    "CN",
    "HU"
  ]
}
```
  - Response
```json
[
    {
        "CountryName": "Spain",
        "count": 26
    },
    {
        "CountryName": "Hungary",
        "count": 9
    },
    {
        "CountryName": "China",
        "count": 5
    }
]
```
- **POST** `/api/holiday-info/deduplicated-holidays`
  - Request
```json
{
    "year": "2025",
    "firstCountryCode": "ES",
    "secondCountryCode": "NL"
}
```
  - Response
```json
[
    {
        "name": "Año Nuevo",
        "date": "2025-01-01"
    },
    {
        "name": "Día de Reyes / Epifanía del Señor",
        "date": "2025-01-06"
    }
]
```

### License
This project is publicly available and may be used for legal purposes.

### Authors
**Ashwini Service
Developed using Micronaut and Java.
Contributions and feedback are welcome!**