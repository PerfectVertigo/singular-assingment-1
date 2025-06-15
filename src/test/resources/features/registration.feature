# Feature: User Registration API
# As a client of the User API
# I want to register new users
#
# Note:
#   <blank>  will be substituted with an empty string
#   <spaces> will be substituted with 10 whitespace characters

Feature: User Registration

  @positive @registration
  Scenario Outline: Successful registrations
    Given the request payload:
      | username   | password   | confirm_password |
      | <username> | <password> | <confirm>        |
    When I send a POST request to "/register"
    Then the response status code should be 200
    And the JSON response field "code" should be "10"

    Examples: Valid users
      | username                       | password     | confirm      | #Notes                     |
      | usr1                           | validPass123 | validPass123 | #username 4-char boundary  |
      | usr_name_with_exactly_30_chars | validPass123 | validPass123 | #username 30-char boundary |
      | userA                          | password     | password     | #password 8-char boundary  |

  @negative @registration
  Scenario Outline: Missing or invalid parameters
    Given the request payload:
      | username   | password   | confirm_password |
      | <username> | <password> | <confirm>        |
    When I send a POST request to "/register"
    Then the response status code should be 200
    And the JSON response field "code" should be "<expectedCode>"

    Examples: Missing fields
      | username | password  | confirm   | expectedCode | #Notes                          |
      | <blank>  | validPass | validPass | 112          | #missing username               |
      | userA    | <blank>   | validPass | 112          | #missing password field         |
      | userA    | validPass | <blank>   | 112          | #missing confirm password field |

    Examples: Violations
      | username                        | password     | confirm      | expectedCode | #Notes                             |
      | user_name_with_exactly_31_chars | validPass123 | validPass123 | 99           | #username too long (>30 chars)     |
      | usr                             | validPass123 | validPass123 | 99           | #username too short (<4 chars)     |
      | userA                           | short1!      | short1!      | 99           | #password too short (<8 chars)     |
      | userA                           | Pass123!     | Pass124!     | 129          | #password/confirm mismatch         |
      | Pass123!                        | Pass123!     | Pass123!     | 129          | #password matches username         |
      | test1                           | validPass123 | validPass123 | 113          | #username already exists ('test1') |

  @edge @knownIssue @registration
  Scenario Outline: Edge-case inputs (known bug - API accepts these while it shouldn't)
    Given the request payload:
      | username   | password   | confirm_password |
      | <username> | <password> | <confirm>        |
    When I send a POST request to "/register"
    Then the response status code should be 200
    And the JSON response field "code" should be "<expectedCode>"

    Examples: Impostor inputs
      | username                 | password         | confirm           | expectedCode | #Notes                    |
      | <spaces>                 | validPass123     | validPass123      | 112          | #whitespace-only username |
      | userA                    | <spaces>         | <spaces>          | 112          | #whitespace-only password |
      | იუზერი                   | 😁😂🤣😁😂🤣😁 | 😁😂🤣😁😂🤣😁  | 99           | #non-ASCII on both        |
      | '; DROP TABLE users; --' | validPass123     | validPass123      | 10           | #sql injection attempt    |
