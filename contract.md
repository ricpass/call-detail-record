# contract:

1 - POST /upload

response:

201: created

400: invalid data 
	reference duplicated
	invalid format
	currency not supported

500: unknown server error

-- import csv file

--------------------------------------------------

2 -

```
GET /caller/{callerId}/report
	?datetimefrom=xxx
	&datetimeto=xxx
```

response:

200:
```
	{
	    "averageSecondsPercall": 4.2,
	    "medianSecondsPercall": 4.2,
	    "longerCallinSeconds": 16.2,
	    "callVolume": 75, 
	    "totalCost": 43.245,
	    "currency": "GDP"
	}
```

404:
	callerId unknown

400: dateFrom or dateTo invalid format

--------------------------------------------------

3 -

the callerId history

```
GET /caller/{callerId}/history
	?dateFrom=xxx
	&dateTo=xxx
	&page=xxx
	&size=xxx
```

response:

200:
```
{
	"history": [
	  {
		"recipient": "447111111111",
		"call_date": "2019-07-09",
		"end_time": "18:34",
	  	"duration": 540
	  },
	  {
		"recipient": "447123123123",
		"call_date": "2019-07-09",
		"end_time": "2019-08-09",
	  	"duration": 540
	  }
	],
	"page": 0,
	"total": 56
}
```

404: 
	phonenumber unknown

400:
	size limit > 1000
	dateFrom or dateTo invalid format

--------------------------------------------------

4 -

list history of a phonenumber

phonenumber can be either caller_id or the recipient 

```
GET /phonenumber/{phonenumber}/history
	?dateFrom=xxx
	&dateTo=xxx
	&page=xxx
	&size=xxx

```

response:

200:
```
{
	"history": [
	  {
	  	"callerId": "447111111111",
		"recipient": "447111111111",
		"call_date": "2019-07-09",
		"end_time": "18:34",
	  	"duration": 540
	  },
	  {
	  	"callerId": "447111111111",
		"recipient": "447123123123",
		"call_date": "2019-07-09",
		"end_time": "2019-08-09",
	  	"duration": 540
	  }
	],
	"page": 0,
	"total": 56
}
```

404:
	phonenumber unknown

400:
	size limit > 1000
	dateFrom or dateTo invalid format


--------------------------------------------------

5- 
get a list of calls logs order by X

```
GET /report/{json|csv}
	?dateFrom=xxx
	&dateTo=xxx
	&page=xxx
	&size=xxx
	&order=[COST|VOLUME|AVERAGE|MEDIAN|LONGER_CALL]
```

200:
```
{
    "calls": [
        {
            "averageSecondsPercall": 4.2,
            "callVolumeMinute": 76,
            "callerId": "447111111111",
            "longerCallinSeconds": 16.2,
            "medianSecondsPercall": 4.2,
            "totalCost": 27.752
        },
        {
            "averageSecondsPercall": 4.2,
            "callVolumeMinute": 76,
            "callerId": "447111111111",
            "longerCallinSeconds": 16.2,
            "medianSecondsPercall": 4.2,
            "totalCost": 27.752
        }
    ],
    "page": 0,
    "total": 100
}

```


------------------------------------------------
## tables:

#### call_log
```
  id
  caller_id - Phone number of the caller
  recipient - Phone number of the number dialled
  call_start_datetime - Datetime the call started
  call_end_datetime - Datetime the call ended
  reference - Unique reference for the call
```

#### caller_finance:
```
  id
  cost - cost - The billable cost of the call - To 3 decimal places (decipence) - GBP
  currency (FK)
  local_cost
  local_currency
  exchange_rate
  call_log.id (FK)
```

#### exchange_rate
```
  local_currency 
  rate
```








    


