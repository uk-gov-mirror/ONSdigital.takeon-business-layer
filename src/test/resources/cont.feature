Feature: Can search for contributor details

	Scenario: client makes a call to GET contributors by period
	    When the client makes a GET call to /contributor/search/period=201712
		Then the client receives status code of 200
		And the client receives a valid JSON
		And the response should contain at least 1 entity