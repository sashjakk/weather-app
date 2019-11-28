package sashjakk.weather.app.fixtures

val tawarano = """
{
    "coord": {
        "lon": 139.01,
        "lat": 35.02
    },
    "weather": [
        {
            "id": 800,
            "main": "Clear",
            "description": "clear sky",
            "icon": "01n"
        }
    ],
    "base": "stations",
    "main": {
        "temp": 285.514,
        "pressure": 1013.75,
        "humidity": 100,
        "temp_min": 285.514,
        "temp_max": 285.514,
        "sea_level": 1023.22,
        "grnd_level": 1013.75
    },
    "wind": {
        "speed": 5.52,
        "deg": 311
    },
    "clouds": {
        "all": 0
    },
    "dt": 1485792967,
    "sys": {
        "message": 0.0025,
        "country": "JP",
        "sunrise": 1485726240,
        "sunset": 1485763863
    },
    "id": 1907296,
    "name": "Tawarano",
    "cod": 200
}
""".trimIndent()

val notFound = """
{
    "cod": "404",
    "message": "city not found"
}
""".trimIndent()

val coordinates = """
{
    "coord": {
        "lon": 139,
        "lat": 35
    },
    "weather": [
        {
            "id": 803,
            "main": "Clouds",
            "description": "broken clouds",
            "icon": "04d"
        }
    ],
    "base": "stations",
    "main": {
        "temp": 277.43,
        "pressure": 1024,
        "humidity": 72,
        "temp_min": 275.37,
        "temp_max": 278.71
    },
    "wind": {
        "speed": 0.45,
        "deg": 212,
        "gust": 2.24
    },
    "clouds": {
        "all": 58
    },
    "dt": 1574979316,
    "sys": {
        "type": 3,
        "id": 2019346,
        "country": "JP",
        "sunrise": 1574976678,
        "sunset": 1575012791
    },
    "timezone": 32400,
    "id": 1851632,
    "name": "Shuzenji",
    "cod": 200
}
""".trimIndent()