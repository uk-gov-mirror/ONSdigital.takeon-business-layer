Feature: Can search for contributor details

	Scenario: client makes a call to GET a single contributor by primary key
		When the client makes a GET call to /contributor/search/reference=49900000001;period=201712;survey=097;
		Then the client receives status code of 200
		And the client receives a valid JSON
		And the response entity at 0 should contain "reference" with value "49900000001"
		And the response entity at 0 should contain "period" with value "201712"
		And the response entity at 0 should contain "survey" with value "097"

	Scenario: client makes a call to GET contributors by period
	    When the client makes a GET call to /contributor/search/period=201712
		Then the client receives status code of 200
		And the client receives a valid JSON
		And the response should contain at least 1 entity

	Scenario: client makes a call to GET contributors by reference like
	    When the client makes a GET call to /contributor/search/reference=0001
		Then the client receives status code of 200
		And the client receives a valid JSON
		And the response should contain at least 1 entity

	Scenario: client makes a call to GET contributors by formID and period
	    When the client makes a GET call to /contributor/search/formId=1;period=201712
		Then the client receives status code of 200
		And the client receives a valid JSON
		And the response should contain at least 1 entity

	Scenario: client makes a call to GET contributors by status and period
        When the client makes a GET call to /contributor/search/status=Dead;period=201712
        Then the client receives status code of 200
        And the client receives a valid JSON
        And the response should contain at least 1 entity

    Scenario: client makes a call to GET contributors by survey
        When the client makes a GET call to /contributor/search/survey=097
        Then the client receives status code of 200
        And the client receives a valid JSON
        And the response should contain at least 1 entity

	Scenario: client makes a call to GET a contributor which doesn't exist
		When the client makes a GET call to /contributor/search/reference=49900000001;period=999912;survey=999;
		Then the client receives status code of 200
		And the client receives a valid JSON
		And the response should be an empty array