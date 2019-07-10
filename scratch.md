# Call detail record

### Introduction

We hope that you find this exercise fun and interesting. There are no trick questions; we want to see your solution to a simple problem with well thought-out and structured code. We realise that there are a lot of topics in the brief and that you may not have the experience or time to complete them all. There is no time limit on how long you spend on the test, however we suggest you spend no more than 8 hours.

## The Brief
Our product team would like you to develop a new call detail record (CDR) business intelligence platform. The idea behind it is that the call records can be queried to find information such as average call cost, longest calls, average number of calls in a given time period.

For this technical test we would like you to create a CDR api. This means that a front end user interface is not required, neither is any consideration of any services which you might expect to be shared such as authentication. A CDR will be provided as a comma separated value file; these files are delivered as often as daily and can be very large (gigabytes), so being able to simply upload new files is a must. 5-8 endpoints demonstrating a variety of insights (i.e not all averages, max, mins), in addition to the CDR upload will be sufficient.

We’d like you to consider what API methods would be useful to other teams writing calling code, how can you make the API easy to use, is each method doing what someone else would expect it to do? Your design should have testing and maintainability at its core and you should consider what testing is appropriate.

The expectation from the product team is that we produce a fully working system as soon as possible, then continue to add features. We hope that you will think about this expectation as you work.

### The Deliverable

Ensure you include the following in your README.md:
1. A covering note explaining the technology choices you have made.
2. Any assumptions you’ve made and why
3. Are there any other considerations/future enhancements you would make given more time?
4. Forbonuspoints,canyoudeployyourworkingapi?(Advanced)

We expect you to treat this project like you’re working as part of a team, and your git ettiequte should reflect this. Email as an attachment or include a link to the git bundled repository showing your commit history with all your commits on the master branch:

`git bundle create tech-test.bundle --all —branches`


|Column | Description | Notes |
|-------|-------------|-------|
|caller_id | Phone number of the caller | |
|recipient | Phone number of the number dialled ||
|call_date | Date on which the call was made ||
|end_time | Time when the call ended ||
|duration | Duration of the call | Seconds |
|reference | Unique reference for the call | |
|currency | for the cost ISO alpha-3 ||
|cost | The billable cost of the call | To 3 decimal places (decipence) |

----------------------------------------------------------------

## Idea for contract:

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
  call_date - Date on which the call was made
  end_time - Time when the call ended
  duration - Duration of the call - Seconds
  reference - Unique reference for the call
```

#### caller_finance:
```
  id
  cost - cost - The billable cost of the call - To 3 decimal places (decipence) - GBP
  currency (FK)
  local_cost
  local_currency - only GBP supported for now.
  exchange_rate
  call_log.id (FK)
```

#### exchange_rate
```
  currency
  local_currency 
  rate
```








    


